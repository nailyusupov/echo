# Echo - Freight Service

****************************************************************************************************
HOW TO START ECHO
****************************************************************************************************

1. UNZIP echo-on-jetty-9.4.2.v20170220.zip in your C:\ drive

2. launch start.bat in the main (jetty-distribution-9.4.2.v20170220) folder

service is running on localhost:8080


***************************************************************************************************
HOW TO STOP ECHO
***************************************************************************************************

1. close the batch window

service is stopped


***************************************************************************************************
HOW TO CHANGE THE DEFAULT PORT
***************************************************************************************************

1. open start.bat with a text editor

2. change last 4 digits (by default 8080) to a desired port

3. save the start.bat file

4. restart the service

port is changed


***************************************************************************************************
SERVICE DETAILS
***************************************************************************************************

Before making changes in the source code make sure you have echo track crenedtials that are needed
in com.engine.QuoteServlet.java and com.engine.con.java, as well, as datasource credentials needed
in com.datasource
