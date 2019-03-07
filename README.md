# CSVtoCSR
Create a CSR file from a CSV.

This program will use the column header record in a csv file to determine how to generate the CSR file.  The following keywords
are used to determine what key will be generated in the CSR file and the value for that key will come from the same column in the
csv file.

KEYWORDS:
  [I]   -  Identifer
  [R]   -  Resource/Rate Code
  [FD]  -  From date
  [TD]  -  To date
  
Note:  Keywords can be enclosed within brackets [] or not.
  
USAGE:

Usage: CreateCSR -feed feedname [-fromdate mm/dd/yyyy] [-todate mm/dd/yyyy] -input inputfile -output outputfile
  
Example:

java -jar CreateCSR.jar -feed VMWARE -input c:\temp\vmware_20190228.csv -ouput c:\temp\vmware_20190228.txt

SERVER [I], FromDate [FD], ToDate TD, DESCRIPTION I, LNXBASE2 [R], LNXCPU2 R
oirdbox00001, 02/01/2019, 02/28/2019, Linux base service, 1, 4

Resulting CSR file:
VMWARE,20190201,20190228,1,3,SERVER,oirdbox00001,DESCRIPTION,Linux base service,FEED,VMWARE,2,LNXBASE2,1,LNXCPU2,4
