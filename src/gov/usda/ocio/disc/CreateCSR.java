package gov.usda.ocio.disc;

import java.io.*;

public class CreateCSR {

    public static void main(String[] args) {

       CSRRecord csrRecord;

       final String usage = "Usage: CreateCSR -feed feedname [-fromdate mm/dd/yyyy] [-todate mm/dd/yyyy] -input inputfile -output outputfile";

       // Parse args

       boolean feedSwitch = false;
       boolean inputSwitch = false;
       boolean outputSwitch = false;
       boolean fromDateSwitch = false;
       boolean toDateSwitch = false;


       String feed = "DEFAULT";
       String inputFile = "";
       String outputFile = "";
       String fromDate = "";
       String toDate = "";

       for (int i=0 ; i<args.length;i++) {
           if (args[i].equals("-feed")){
                   feed = args[++i];
                   feedSwitch = true;
           }
           if (args[i].equals("-input")) {
                   inputFile = args[++i];
                   inputSwitch = true;
           }
           if (args[i].equals("-output")) {
                   outputFile = args[++i];
                   outputSwitch = true;
           }
           if (args[i].equals("-fromdate")) {
                   fromDate = args[++i];
                   fromDateSwitch = true;
           }
           if (args[i].equals("-todate")) {
                   toDate = args[++i];
                   toDateSwitch = true;
           }
       }

       if (!feedSwitch || !outputSwitch || !inputSwitch){
           System.err.println(usage);
           return;
       }

       // Read input file and generate csr output file

       FileWriter writer;
       BufferedWriter bufferedWriter;

       BufferedReader reader;

        try {
            // reader = new BufferedReader(new FileReader("C:\\Temp\\20190228_SAN.txt"));
            reader = new BufferedReader(new FileReader(inputFile));
            writer = new FileWriter(outputFile);
            bufferedWriter = new BufferedWriter(writer);

            String line = reader.readLine();

            //  First record read will be header record.  Header record should contain identifiers and resources.
            HeaderRecord heading = new HeaderRecord(line);

            do {
                line = reader.readLine();

                if (line == null){
                    break;
                }

                csrRecord = new CSRRecord(line, heading);
                csrRecord.setFeed(feed);

                if (fromDateSwitch){
                    csrRecord.setFromDate(fromDate);
                }

                if (toDateSwitch){
                    csrRecord.setToDate(toDate);
                }

                csrRecord.addIdentifier("FEED", feed);

                // getCSR will return null of no resources

                if (csrRecord.getCSR() != null) {
                    bufferedWriter.write(csrRecord.getCSR());
                    bufferedWriter.newLine();
                    // System.out.println(csrRecord.getCSR());
                }

            } while (true);

            reader.close();
            bufferedWriter.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
