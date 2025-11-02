package se.ifmo.tags;

import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UIData;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@FacesComponent("se.ifmo.tags.AdvancedTable")
@Setter
@Getter
@SuppressWarnings("unused")
public class AdvancedTable extends UIData {
    public static final String COMPONENT_FAMILY = "se.ifmo.tags.AdvancedTable";

    private boolean exportable;

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        var writer = context.getResponseWriter();
        var clientId = getClientId(context);

        writer.startElement("div", this);
        writer.writeAttribute("id", clientId + "_wrapper", null);
        writer.writeAttribute("class", "advanced-table-wrapper", null);

        if (isExportable()) {
            encodeExportButton(context, clientId);
        }

        writer.startElement("table", this);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", "advanced-table", null);

        encodeTableHeader(context);

        writer.startElement("tbody", this);

        Object value = getValue();
        if (value instanceof List<?> items) {
            for (int i = 0; i < items.size(); i++) {
                setRowIndex(i);
                encodeRow(context);
            }
        }

        writer.endElement("tbody");
    }

    private void encodeRow(FacesContext context) throws IOException {
        var writer = context.getResponseWriter();
        writer.startElement("tr", this);

        for (AdvancedColumn column : getColumns()) {
            writer.startElement("td", this);

            String fieldExpression = String.format("#{%s.%s}", getVar(), column.getField());
            Object cellValue = context.getApplication()
                    .getExpressionFactory()
                    .createValueExpression(context.getELContext(), fieldExpression, Object.class)
                    .getValue(context.getELContext());

            writer.write(cellValue != null ? cellValue.toString() : "");

            writer.endElement("td");
        }

        writer.endElement("tr");
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        setRowIndex(-1);
        var writer = context.getResponseWriter();
        writer.endElement("table");

        if (isExportable()) {
            encodeExportScript(context);
        }

        writer.endElement("div");
    }

    protected void encodeTableHeader(FacesContext context) throws IOException {
        var writer = context.getResponseWriter();
        writer.startElement("thead", this);
        writer.startElement("tr", this);

        getColumns().forEach(column -> {
            try {
                column.encodeBegin(context);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        writer.endElement("tr");
        writer.endElement("thead");
    }

    protected void encodeExportButton(FacesContext context, String tableId) throws IOException {
        var writer = context.getResponseWriter();
        writer.startElement("button", this);
        writer.writeAttribute("type", "button", null);
        writer.writeAttribute("class", "export-csv-btn", null);
        writer.writeAttribute("onclick", "exportTableToCSV('" + tableId + "')", null);
        writer.write("Экспорт в CSV");
        writer.endElement("button");
    }

    protected void encodeExportScript(FacesContext context) throws IOException {
        var writer = context.getResponseWriter();
        writer.startElement("script", this);
        writer.write("""
                    function exportTableToCSV(tableId) {
                        const table = document.getElementById(tableId);
                        const rows = table.querySelectorAll('tr');
                        const csv = Array.from(rows).map(row => 
                            Array.from(row.querySelectorAll('td,th'))
                                .map(cell => '"' + cell.innerText.replace(/"/g, '""') + '"')
                                .join(',')
                        ).join('\\n');
                
                        const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
                        const link = document.createElement('a');
                        link.href = URL.createObjectURL(blob);
                        link.download = 'export.csv';
                        link.click();
                    }
                """);
        writer.endElement("script");
    }

    public List<AdvancedColumn> getColumns() {
        return getChildren().stream()
                .filter(AdvancedColumn.class::isInstance)
                .map(AdvancedColumn.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    public Object getValue() {
        var value = super.getValue();
        if (!(value instanceof List<?> list)) {
            return value;
        }

        return list.stream()
                .filter(this::passesAllFilters)
                .collect(Collectors.toList());
    }

    private boolean passesAllFilters(Object item) {
        return getColumns().stream()
                .allMatch(column -> column.passesFilter(item));
    }
}
