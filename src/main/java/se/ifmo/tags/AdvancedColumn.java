package se.ifmo.tags;

import jakarta.faces.component.UIColumn;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@FacesComponent("se.ifmo.tags.AdvancedColumn")
@Setter
@Getter
@SuppressWarnings("unused")
public class AdvancedColumn extends UIColumn {
    public static final String COMPONENT_FAMILY = "se.ifmo.tags.AdvancedColumn";

    private String field;
    private String headerText;
    private boolean sortable;
    private boolean groupable;
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

        if (filterPattern != null && value instanceof String) {
            String str = (String) value;
            return str.matches(filterPattern);
        }

        if (value instanceof String) {
            String str = (String) value;
            boolean lengthValid = true;
            if (minLength != null) {
                lengthValid = str.length() >= minLength;
            }
            if (maxLength != null) {
                lengthValid = lengthValid && str.length() <= maxLength;
            }
            return lengthValid;
        }

        if (value instanceof Number) {
            Number num = (Number) value;
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
            var context = FacesContext.getCurrentInstance();
            var expression = String.format("#{%s}", field);
            Object value = context.getApplication()
                    .getExpressionFactory()
                    .createValueExpression(context.getELContext(), expression, Object.class)
                    .getValue(context.getELContext());

            // Конвертируем строковые числа в Number при необходимости
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
