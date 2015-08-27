To run the code, 
- Import the zip file StockPrediction.zip to J2EE Eclipse.
- Install MySQL database, we used WampServer 2.4
- Switch on the database, username is root and password is left blank. this are default values.
- Create database 'stock' with 3 tables as per the database schema.
- Make sure the internet is on as the program collects data from Yahoo API.
- Run the HistoricalData class as java application. This would save the data for default symbol (BBY) from 02/01/2014 to 03/02/2015. Change the query in collectData() method for other symbols namely AMZN,GOOG,YHOO and EBAY to collect historical data.
- Run the ExtractData class as java application. This would save the data for default symbol (AMZN). Change the query in collectData() method for other symbols namely BBY,GOOG,YHOO and EBAY to collect real-time data.The queries are commented (can collect by uncommenting them)
