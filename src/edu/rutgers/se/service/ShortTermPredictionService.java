package edu.rutgers.se.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.json.JSONException;
import org.json.JSONObject;

import edu.rutgers.se.prediction.Bayesian;
import edu.rutgers.se.service.response.ShortTerm;
import edu.rutgers.se.service.response.ShortTermResponse;
import edu.rutgers.se.utils.DBUtil;

@Path("/shortterm")

/**
 * 
 * @author Ankit
 *
 */
public class ShortTermPredictionService {
	
	@POST
	@Produces("application/json")
	public ShortTermResponse predict(@QueryParam("symbol") String symbol) {
		
		ShortTermResponse resp=new ShortTermResponse();
		double[] prices = getPrices(symbol);
		if(prices!=null){
			ArrayList<ShortTerm> list= Bayesian.predict(prices);
			resp.setList(list);
			resp.setStatus("success");
		}else{
			resp.setStatus("No prediction available. Check back later!!");
		}
		return resp;
	}
	
	public double[] getPrices(String symbol){
		double[] prices = null;
		Statement stmt=DBUtil.connect();
		try {
			String sql = "SELECT * FROM realtimedata WHERE symbol='"+symbol+"' ORDER BY DATE DESC, time desc limit 10";
			ResultSet rs = stmt.executeQuery(sql);	
			prices=new double[20]; int i=0;
			while(rs.next()){
				//Retrieve by column name
				Double price  = rs.getDouble("PRICE");
				prices[i]=price;i++;}	
			DBUtil.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return prices;
	}


}

