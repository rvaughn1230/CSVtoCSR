package gov.usda.ocio.disc;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CSRRecord {
        private CSRHeader header = new CSRHeader();
        private ArrayList<CSRIdentifier> identifiers = new ArrayList<>();
        private ArrayList<CSRResource> resources = new ArrayList<>();

        public CSRRecord(String record, HeaderRecord heading) {
                String[] cols = record.split(",");
                String colName;
                String colType;
                CSRIdentifier csrIdentifier;
                CSRResource csrResource;

                for (int x = 0; x < cols.length; x++) {
                        HeaderRecord.Columns col = heading.get(x);
                        colName = col.getName();
                        colType = col.getType();

                        // System.out.println("column name: " + colName + " type: " + colType + " value: " + cols[x]);

                        switch (colType) {
                                case "I":
                                        csrIdentifier = new CSRIdentifier(colName, cols[x]);
                                        identifiers.add(csrIdentifier);
                                        break;
                                case "R":
                                        // If resource value is blank or 0, then exit without adding resource
                                        if (cols[x].isEmpty() ||  cols[x].equals("0")){
                                                break;
                                        }
                                        resources.add(new CSRResource(colName, Double.parseDouble(cols[x])));
                                        break;
                                case "TD":
                                        header.setToDate(cols[x]);
                                        break;
                                case "FD":
                                        header.setFromDate(cols[x]);
                                        break;
                                case "TT":
                                        header.setToTime(cols[x]);
                                        break;
                                case "FT":
                                        header.setFromTime(cols[x]);
                                        break;
                                default :
                                        break;

                        }
                }
        }

        public String getCSR(){

                // Return CSR record only if resource count > 0

                if (resources.size() > 0) {

                        StringBuilder s = new StringBuilder(header.getFeed() + ","
                                + header.getFromDate() + ","
                                + header.getToDate() + ","
                                + header.getFromTime() + ","
                                + header.getToTime() + ","
                                + header.getShift() + ","
                                + identifiers.size());

                        for (CSRIdentifier identifier : identifiers) {
                                s.append(",").append(identifier.getName()).append(",").append(identifier.getValue());
                        }

                        s.append(",").append(resources.size());

                        for (CSRResource resource : resources) {
                                s.append(",").append(resource.getName()).append(",").append(resource.getValue());
                        }

                        return s.toString();
                }
                return null;
        }

        public void setFeed(String feed) {
                this.header.setFeed(feed);
        }

        public void addIdentifier (String name, String value){
               identifiers.add(new CSRIdentifier(name, value));
        }

        public void addResource (String name, Double value){
                resources.add(new CSRResource(name, value));
        }

        private class CSRHeader{
                private String feed;
                private String fromDate;
                private String toDate;
                private String fromTime;
                private String toTime;
                private byte shift;
                private final DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

                public CSRHeader() {
                        this.feed = "";
                        this.fromDate =  dateFormat.format(new Date());
                        this.toDate =  dateFormat.format(new Date());
                        this.fromTime = "00:00:00";
                        this.toTime = "00:00:00";
                        this.shift = 1;
                }

                public String getFeed() {
                        return feed;
                }

                public void setFeed(String feed) {
                        this.feed = feed;
                }

                public String getFromDate() {
                        return fromDate;
                }

                public void setFromDate(String fromDate) {
                        try {
                            Date tempDate = new SimpleDateFormat("MM/dd/yyyy").parse(fromDate);
                            this.fromDate = dateFormat.format(tempDate);
                        } catch (ParseException e){
                            e.printStackTrace();
                        }
                }

                public String getToDate() {
                        return toDate;
                }

                public void setToDate(String toDate) {
                        try {
                                Date tempDate = new SimpleDateFormat("MM/dd/yyyy").parse(toDate);
                                this.toDate = dateFormat.format(tempDate);
                        } catch (ParseException e){
                                e.printStackTrace();
                        }
                }

                public String getFromTime() {
                        return fromTime;
                }

                public void setFromTime(String fromTime) {
                        this.fromTime = fromTime;
                }

                public String getToTime() {
                        return toTime;
                }

                public void setToTime(String toTime) {
                        this.toTime = toTime;
                }

                public byte getShift() {
                        return shift;
                }

                public void setShift(byte shift) {
                        this.shift = shift;
                }
        }

        private class CSRIdentifier{
                private String name;
                private String value;

                public CSRIdentifier(String name, String value) {
                        this.name = name;
                        this.value = value;
                }

                public String getName() {
                        return name;
                }

                public String getValue() {
                        return value;
                }

                public void setName(String name) {
                        this.name = name;
                }

                public void setValue(String value) {
                        this.value = value;
                }
        }

        private class CSRResource{
                private String name;
                private Double value;

                public CSRResource(String name, Double value) {
                        this.name = name;
                        this.value = value;
                }

                public String getName() {
                        return name;
                }

                public void setName(String name) {
                        this.name = name;
                }

                public Double getValue() {
                        return value;
                }

                public void setValue(Double value) {
                        this.value = value;
                }
        }
}

