package gov.usda.ocio.disc;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class CSRRecord {
        private CSRHeader header = new CSRHeader();
        private ArrayList<CSRIdentifier> identifiers = new ArrayList<>();
        private ArrayList<CSRResource> resources = new ArrayList<>();

        CSRRecord(String record, HeaderRecord heading) {
                String[] cols = record.split(",");
                String colName;
                String colType;
                CSRIdentifier csrIdentifier;

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
                                case "IN":
                                        csrIdentifier = new CSRIdentifier(cols[x],cols[++x]);
                                        identifiers.add(csrIdentifier);
                                        break;
                                case "R":
                                        // If resource value is blank or 0, then exit without adding resource
                                        if (cols[x].isEmpty() ||  cols[x].equals("0")){
                                                break;
                                        }
                                        resources.add(new CSRResource(colName, Double.parseDouble(cols[x])));
                                        break;
                                case "RN":
                                        // If resource value is blank or 0, then exit without adding resource
                                        if (cols[x+1].isEmpty() ||  cols[x+1].equals("0")){
                                                break;
                                        }
                                        resources.add(new CSRResource(cols[x], Double.parseDouble(cols[++x])));
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

        String getCSR(){

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
                                if (identifier.getValue().contains(" ")) {
                                        s.append(",").append(identifier.getName()).append(",").append('"').append(identifier.getValue()).append('"');
                                }else{
                                        s.append(",").append(identifier.getName()).append(",").append(identifier.getValue());
                                }
                        }

                        s.append(",").append(resources.size());

                        for (CSRResource resource : resources) {
                                s.append(",").append(resource.getName()).append(",").append(resource.getValue());
                        }

                        return s.toString();
                }
                return null;
        }

        void setFeed(String feed) {
                this.header.setFeed(feed);
        }
        void setFromDate(String fromDate){this.header.setFromDate(fromDate);}
        void setToDate(String toDate){this.header.setToDate(toDate);}

        void addIdentifier(String name, String value){
               identifiers.add(new CSRIdentifier(name, value));
        }

        void addResource (String name, Double value){
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

                CSRHeader() {
                        this.feed = "";
                        this.fromDate =  dateFormat.format(new Date());
                        this.toDate =  dateFormat.format(new Date());
                        this.fromTime = "00:00:00";
                        this.toTime = "00:00:00";
                        this.shift = 1;
                }

                String getFeed() {
                        return feed;
                }

                void setFeed(String feed) {
                        this.feed = feed;
                }

                String getFromDate() {
                        return fromDate;
                }

                void setFromDate(String fromDate) {
                        try {
                            Date tempDate = new SimpleDateFormat("MM/dd/yyyy").parse(fromDate);
                            this.fromDate = dateFormat.format(tempDate);
                        } catch (ParseException e){
                            e.printStackTrace();
                        }
                }

                String getToDate() {
                        return toDate;
                }

                void setToDate(String toDate) {
                        try {
                                Date tempDate = new SimpleDateFormat("MM/dd/yyyy").parse(toDate);
                                this.toDate = dateFormat.format(tempDate);
                        } catch (ParseException e){
                                e.printStackTrace();
                        }
                }

                String getFromTime() {
                        return fromTime;
                }

                void setFromTime(String fromTime) {
                        this.fromTime = fromTime;
                }

                String getToTime() {
                        return toTime;
                }

                void setToTime(String toTime) {
                        this.toTime = toTime;
                }

                byte getShift() {
                        return shift;
                }

                public void setShift(byte shift) {
                        this.shift = shift;
                }
        }

        private class CSRIdentifier{
                private String name;
                private String value;

                CSRIdentifier(String name, String value) {
                        this.name = name;
                        this.value = value;
                }

                String getName() {
                        return name;
                }

                String getValue() {
                        return value;
                }

                void setName(String name) {
                        this.name = name;
                }

                void setValue(String value) {
                        this.value = value;
                }
        }

        private class CSRResource{
                private String name;
                private Double value;

                CSRResource(String name, Double value) {
                        this.name = name;
                        this.value = value;
                }

                String getName() {
                        return name;
                }

                void setName(String name) {
                        this.name = name;
                }

                Double getValue() {
                        return value;
                }

                void setValue(Double value) {
                        this.value = value;
                }
        }
}

