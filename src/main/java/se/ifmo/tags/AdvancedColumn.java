package se.ifmo.tags;

import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UIColumn;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.logging.Logger;

@FacesComponent("se.ifmo.tags.AdvancedColumn")
@Setter
@Getter
@SuppressWarnings("unused")
public class AdvancedColumn extends UIColumn {
    public static final String COMPONENT_FAMILY = "se.ifmo.tags.AdvancedColumn";
    private static final Logger LOGGER = Logger.getLogger(AdvancedColumn.class.getName());

    private String field;
    private String headerText;
    private boolean sortable;
    private String filterPattern;
    private Integer minLength;
    private Integer maxLength;
    @Setter
    @Getter
    private Number min;
    @Setter
    @Getter
    private Number max;

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("th", this);
        writer.writeAttribute("class", sortable ? "sortable-column" : null, null);
        writer.write(headerText != null ? headerText : "");

        if (sortable) {
            encodeSortControls(writer);
        }

        writer.endElement("th");
    }

    private void encodeSortControls(ResponseWriter writer) throws IOException {
        writer.startElement("span", null);
        writer.writeAttribute("class", "sort-controls", null);
        writer.endElement("span");
    }

    private void handleMinValue(Object value) {
        if (value instanceof String) {
            try {
                this.min = Double.valueOf((String) value);
            } catch (NumberFormatException ignored) {
            }
        } else if (value instanceof Number) {
            this.min = (Number) value;
        }
    }

    private void handleMaxValue(Object value) {
        if (value instanceof String) {
            try {
                this.max = Double.valueOf((String) value);
            } catch (NumberFormatException ignored) {
            }
        } else if (value instanceof Number) {
            this.max = (Number) value;
        }
    }

    public void setMinValue(Object min) {
        handleMinValue(min);
    }

    public void setMaxValue(Object max) {
        handleMaxValue(max);
    }

    public boolean passesFilter(Object item) {
        if (field == null) return true;

        Object value = getValueFromItem(item);
        if (value == null) return true;

        if (filterPattern != null && value instanceof String str) {
            return str.matches(filterPattern);
        }

        if (minLength != null || maxLength != null) {
            String str = value.toString();
            boolean lengthValid = true;
            if (minLength != null) {
                lengthValid = str.length() >= minLength;
            }
            if (maxLength != null) {
                lengthValid = lengthValid && str.length() <= maxLength;
            }
            return lengthValid;
        }

        if (value instanceof Number num) {
            boolean rangeValid = true;
            if (min != null) {
                rangeValid = num.doubleValue() >= min.doubleValue();
            }
            if (max != null) {
                rangeValid = rangeValid && num.doubleValue() <= max.doubleValue();
            }
            return rangeValid;
        }

        return true;
    }

    private Object getValueFromItem(Object item) {
        try {
            if (item == null || field == null) {
                return null;
            }

            var context = FacesContext.getCurrentInstance();
            var elContext = context.getELContext();

            context.getApplication()
                    .getELResolver()
                    .setValue(elContext, null, "item", item);

            var expression = String.format("#{item.%s}", field);
            Object value = context.getApplication()
                    .getExpressionFactory()
                    .createValueExpression(elContext, expression, Object.class)
                    .getValue(elContext);

            if (value instanceof String && (min != null || max != null)) {
                try {
                    return Double.valueOf((String) value);
                } catch (NumberFormatException e) {
                    return value;
                }
            }
            return value;
        } catch (Exception e) {
            return null;
        }
    }
}
