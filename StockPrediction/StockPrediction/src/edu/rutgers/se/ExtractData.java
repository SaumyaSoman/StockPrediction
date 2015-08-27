package edu.rutgers.se;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * Class to extract Real Time Data and save it in MySQL database
 *
 */
public class ExtractData {


	static Connection conn = null;
	static Statement stmt = null;

	/**
	 * The main method to extract real time data
	 * @param args
	 */
	public static void main(String[] args) {
		
		ExtractData hdata=new ExtractData();
		JSONObject jsonObject=null;

		try{
			//Make JDBC connection to database ; username - root ; passwors - (blank)
			conn = DriverManager.getConnection("jdbc:mysql://localhost/stock", "root", "");
			stmt = conn.createStatement();
			try {
				while (true) {
					//Collect data using yahoo api , this returns a JSON object
					jsonObject=hdata.collectData();
					if(jsonObject!=null){
						//Save the data in json to database
						hdata.saveData(jsonObject);
					}
					jsonObject=null;
					//Wait for a minute before fetching data again
					Thread.sleep(60 * 1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}catch(SQLException se){
		}finally{
			//Close the connections
			try{
				if(stmt!=null)
					conn.close();
			}catch(SQLException se){
			}
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
			}
		}			
	}

	private void saveData(JSONObject jsonObject) {

		try {
			JSONArray quotes = jsonObject.getJSONObject("query").getJSONObject("results").optJSONArray("quote");
			for (int i=0;i<quotes.length();i++) {
				JSONObject quote=(JSONObject) quotes.get(i);	
				String sql = "INSERT INTO realtimedata VALUES (\'"+quote.getString("Symbol")+"\', "+quote.getDouble("LastTradePriceOnly")+", DATE_FORMAT( NOW(),'%Y-%m-%d') ,  TIME_FORMAT(NOW(),'%H:%i:%s') , "+quote.getLong("Volume")+")";
				stmt.executeUpdate(sql);
			}
			System.out.println("Inserted");
		} catch (JSONException e) {
		} catch (SQLException e) {
		}

	}

	/**
	 * This method collects data from Yahoo API using YQL
	 * @return JSONObject
	 */
	private JSONObject collectData() {
		URL url;
		HttpURLConnection conn = null;
		JSONObject jsonObject=null;
		try {
			//Sample query for Amazon. Symbol is AMZN . Uncomment other queries for EBAY, BBY (BestBuy), GOOG (Google) and YHOO (Yahoo).
			url = new URL("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22YHOO%22%2C%22AMZN%22%2C%22GOOG%22%2C%22EBAY%22%2C%22BBY%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");
//			url = new URL("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22YHOO%22%2C%22BBY%22%2C%22GOOG%22%2C%22EBAY%22%2C%22BBY%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");
//			url = new URL("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22YHOO%22%2C%22AMZN%22%2C%22GOOG%22%2C%22EBAY%22%2C%22EBAY%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");
//			url = new URL("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22YHOO%22%2C%22AMZN%22%2C%22GOOG%22%2C%22EBAY%22%2C%22YHOO%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");
//			url = new URL("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22YHOO%22%2C%22AMZN%22%2C%22GOOG%22%2C%22EBAY%22%2C%22GOOG%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			StringBuilder sb = new StringBuilder();
			String inputStr;
			//Read response to StringBuilder object
			while ((inputStr = br.readLine()) != null)
				sb.append(inputStr);
			
			//Convert the String to JSON object
			jsonObject = new JSONObject(sb.toString());
		} catch(MalformedURLException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			conn.disconnect();
		}	
		return jsonObject;
	}
}
