package gov.usda.ocio.disc;

import java.io.*;

public class CreateCSR {

    public static void main(String[] args) {

       CSRRecord csrRecord;
       String feed = "DEFAULT";

       final String usage = "Usage: CreateCSR -feed feedname [-fromdate mm/dd/yyyy] [-todate mm/dd/yyyy] -input inputfile -output outputfile";

       // Parse args

       boolean feedSwitch = false;
       boolean inputSwitch = false;
       boolean outputSwitch = false;

       String inputFile = "";
       String outputFile = "";
       String fromDate;
       String toDate;

       for (int i=0 ; i<args.length;i++) {
           if (args[i].equals("-feed")){
               if (i < args.length){
                   feed = args[++i];
                   feedSwitch = true;
               }else{
                   System.err.println(usage);
                   return;
               }
           }
           if (args[i].equals("-input")) {
               if (i < args.length) {
                   inputFile = args[++i];
                   inputSwitch = true;
               } else {
                   System.err.println(usage);
                   return;
               }
           }
           if (args[i].equals("-output")) {
               if (i < args.length) {
                   outputFile = args[++i];
                   outputSwitch = true;
               } else {
                   System.err.println(usage);
                   return;
               }
           }
           if (args[i].equals("-fromdate")) {
               if (i < args.length) {
                   fromDate = args[++i];
               } else {
                   System.err.println(usage);
                   return;
               }
           }
           if (args[i].equals("-todate")) {
               if (i < args.length) {
                   toDate = args[++i];
               } else {
                   System.err.println(usage);
                   return;
               }
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

                csrRecord.addIdentifier("FEED", feed);

                bufferedWriter.write(csrRecord.getCSR());
                bufferedWriter.newLine();
                // System.out.println(csrRecord.getCSR());

            } while (true);

            reader.close();
            bufferedWriter.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
