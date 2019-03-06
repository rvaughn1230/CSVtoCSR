package gov.usda.ocio.disc;

import java.util.LinkedHashMap;
import java.util.Map;

public class HeaderRecord {
    private Map<Integer, Columns> colHeadings = new LinkedHashMap<>();

    public HeaderRecord(String header) {
        String[] columns = header.split(",");
        Columns column;
        String name;
        String type;

        for (int x = 0; x < columns.length; x++) {
            String[] pCol = columns[x].trim().split("\\W+");

            type = "";
            name = "";

            for (String s : pCol) {
                // System.out.println("x=" + x + " y=" + y + " value=" + pCol[y]);

                switch (s) {

                    case "I":
                    case "R":
                    case "FD":
                    case "TD":
                    case "FT":
                    case "TT":
                    case " ":
                        type = s;
                        break;
                    default:
                        name = s;

                }
            }

            column = new Columns(name, type);
            colHeadings.put(x, column);

        }
    }

    public Columns get(int x) {
        return colHeadings.get(x);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("\nColumn List\n");

        for (Map.Entry<Integer, Columns> items : colHeadings.entrySet()) {

            s.append("index: ").append(items.getKey()).append(" ").append(items.getValue()).append("\n");
        }

        return s.toString();
    }



protected class Columns {
    private String name;
    private String type;

    Columns(String name, String type) {
        this.name = name;
        this.type = type;
    }

    String getName() {
        return name;
    }

    String getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("\t %-20s \t type: %s", this.name, this.type);
    }
}

}
