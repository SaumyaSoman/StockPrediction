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
 * Class to extract Historical data using Yahoo API
 *
 */
public class HistoricalData {

	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args) {		
		HistoricalData hdata=new HistoricalData();	
		// Collect data using Yahoo API
		JSONObject jsonObject=hdata.collectData();
		if(jsonObject!=null){
			//save data
			hdata.saveData(jsonObject);
		}	
	}

	/**
	 * This method parses JSON and saves it in MySQL database.
	 * @param jsonObject object to be saved
	 */
	private void saveData(JSONObject jsonObject) {
		Connection conn = null;
		Statement stmt = null;
		try{
			//Get quotes array from the response
			JSONArray quotes = jsonObject.getJSONObject("query").getJSONObject("results").optJSONArray("quote");
			//Make JDBC connection to database ; username - root ; passwors - (blank)
			conn = DriverManager.getConnection("jdbc:mysql://localhost/stock", "root", "");
			stmt = conn.createStatement();


			for (int i=0;i<quotes.length();i++) {
				JSONObject quote=(JSONObject) quotes.get(i);
				//query to insert into database
				String sql = "INSERT INTO historicalData " +
						"VALUES (\'"+quote.getString("Symbol")+"\', "+quote.getDouble("Open")+", "
						+ ""+quote.getDouble("Close")+", "+quote.getDouble("High")+","+quote.getDouble("Low")+","+quote.getLong("Volume")+" , "
						+ " \'"+quote.getString("Date")+"\' , "+quote.getDouble("Adj_Close")+")";
				stmt.executeUpdate(sql);
			}
		}catch(SQLException se){
			se.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}finally{
			//Close connections
			try{
				if(stmt!=null)
					conn.close();
			}catch(SQLException se){
			}
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
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
			//Sample query to extract data for BestBuy (BBY) from Feb 1,2014 to March 2nd 2015
			url = new URL("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22BBY%22%20and%20startDate%20%3D%20%222014-02-01%22%20and%20endDate%20%3D%20%222015-03-01%22&format=json&env=http%3A%2F%2Fdatatables.org%2Falltables.env&callback=");
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
