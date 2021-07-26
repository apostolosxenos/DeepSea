# DeepSea Reporting Tool

A demo application that is used to parse a sample HTTP log file (NASA_access_log_Aug95.gz) and extract useful information on user's demand.

Instructions:

1) git clone https://github.com/apostolosxenos/deepsea.git
2) cd to project's folder
3) mvn clean package
4) Run the .jar file by providing the full filepath of the log file as the only argument. Log file must be compressed (.gz) 
   
   <b>java -jar deepsea-reporting-tool-1.0.jar "FULL_FILE_PATH"</b>
