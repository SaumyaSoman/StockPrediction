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

import edu.rutgers.se.service.response.TrendingResponse;
import edu.rutgers.se.utils.DBUtil;

@Path("/trending")

/**
 * 
 * @author Hitesh
 *
 */
public class TrendingService {
    
  @POST
  @Produces("application/json")
  public TrendingResponse trending() {
  		
	  Statement stmt=DBUtil.connect();
		try {
			ResultSet rs = stmt.executeQuery("SELECT symbol,date,avg(volume) as vol FROM `realtimedata` group by symbol, date order by date desc limit 10");
			double maxVol=0; String maxSymbol="";
			while(rs.next()){
				String symbol=rs.getString("symbol");
				Double vol=rs.getDouble("vol");
				if(vol>maxVol){
					maxVol=vol;
					maxSymbol=symbol;
				}
			}
			DBUtil.close();
			ArrayList<Double> prices=new ArrayList<Double>();
			 Statement stmt1=DBUtil.connect();
			 ResultSet rs1 = stmt1.executeQuery("SELECT symbol, date,time ,volume FROM `realtimedata` where symbol= '"+maxSymbol+"' order by date desc,time desc limit 100");
				while(rs1.next()){
					Double vol=rs1.getDouble("volume");
					prices.add(vol);
				}
				rs1.close();
				DBUtil.close();
				TrendingResponse resp=new TrendingResponse();
				resp.setSymbol(maxSymbol);
				resp.setPrices(prices);
				return resp;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
  }


  
}
 
