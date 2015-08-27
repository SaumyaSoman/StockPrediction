package edu.rutgers.se.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.json.JSONException;
import org.json.JSONObject;

import edu.rutgers.se.prediction.SVMPredict;
import edu.rutgers.se.service.response.LongTermResponse;
import edu.rutgers.se.service.response.Quote;
import edu.rutgers.se.service.response.QuoteResponse;
import edu.rutgers.se.service.response.TrendingResponse;
import edu.rutgers.se.utils.DBUtil;

@Path("/predict")

/**
 * 
 * @author Hitesh
 *
 */
public class LongTermPredictionService {
	
	String[] buy=new String[]{"We think this is a great buy option now, you will make some money", "This appears to have good promise in the near future, go ahead and invest some money here", "Perhaps it's about time you made some money.. buy it!", "This is just the beginning, good things are in store for this security, go ahead and buy it!", "The stock is at a pivotal point to make some good amount of change! BUY it now"};
	String[] sell=new String[]{"This may be the time for you to sell it", "It's time to cut our losses and try else where, this security doesn't have anything to offer", "Please sell now, the stock has reached its maximum potential", "The stock seems to be very volatile right now, sell it.","The stock doesn't seem to have what you need"};
	String[] hold=new String[]{"We are unsure of where this stock is going, hold on for a few days", "The stock isn't doing all that much and is relatively flat, just hold tight", "This isn't the best time to buy or sell", "The stock is lower than usual but not lowest, it still has potential to bounce back, don't do anything","Better of holding at this point"};

	@POST
	@Produces("application/json")
	public LongTermResponse predict(@QueryParam("symbol") String symbol) {
		
		LongTermResponse resp=new LongTermResponse();
		ArrayList<Double> prices = getPrices(symbol);
		if(prices!=null){
			int vote= SVMPredict.predict(prices, symbol);
			SimpleRegression st=new SimpleRegression();
			int len=prices.size()<10 ? prices.size() :10;
			for(int i=0;i<len;i++){
				st.addData(len-i, prices.get(i));
			}
			double predicted=st.predict(len+5);
			vote= predicted>prices.get(0) ? vote+1 : vote-1;
			
			int i= (int) Math.random()*4;
			if(predicted>prices.get(0) && vote>2){
				resp.setStatus(buy[i]);
				resp.setPredicted(predicted);
			}else if(predicted<prices.get(0) && vote<2){
				resp.setStatus(sell[i]);
				resp.setPredicted(predicted);
			}else{
				resp.setStatus(hold[i]);
				resp.setPredicted(predicted);
			}
			System.out.println("predicted..."+predicted+".."+prices.get(0));
		}else{
			resp.setStatus("No prediction available. Check back later!!");
		}
		return resp;
	}
	
	public ArrayList<Double> getPrices(String symbol){
		ArrayList<Double> prices = null;
		Statement stmt=DBUtil.connect();
		try {
			String sql = "SELECT * FROM historicaldata WHERE symbol='"+symbol+"' Group by date ORDER BY DATE DESC  LIMIT 31";
			ResultSet rs = stmt.executeQuery(sql);	
			prices=new ArrayList<Double>();
			while(rs.next()){
				//Retrieve by column name
				Double price  = rs.getDouble("close");
				if(price!=null) prices.add(price);
			}
			DBUtil.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return prices;
	}


}

