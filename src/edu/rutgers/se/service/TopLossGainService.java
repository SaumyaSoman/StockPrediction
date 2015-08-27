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

import edu.rutgers.se.service.response.LossGain;
import edu.rutgers.se.service.response.LossGainResponse;
import edu.rutgers.se.service.response.TrendingResponse;
import edu.rutgers.se.utils.DBUtil;

@Path("/lossgain")

/**
 * 
 * @author Suvigya
 *
 */
public class TopLossGainService {

	@POST
	@Produces("application/json")
	public LossGainResponse trending() {
		
		LossGainResponse resp=new LossGainResponse();
		Statement stmt=DBUtil.connect();
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM `realtimedata` order by date desc,time desc limit 10");
			LossGain gainer=null; LossGain loser=null;
			while(rs.next()){
				String symbol=rs.getString("symbol");
				Double change=rs.getDouble("change");
				if(change <0){
					if(loser!=null && change < loser.getChange()){
						loser.setChange(change);
						loser.setSymbol(symbol);
						loser.setPercent(rs.getString("percentchange"));
					}else if(loser==null){
						loser=new LossGain();
						loser.setChange(change);
						loser.setSymbol(symbol);
						loser.setPercent(rs.getString("percentchange"));
					}
				}else{
					if(gainer!=null && change>gainer.getChange()){
						gainer.setChange(change);
						gainer.setSymbol(symbol);
						gainer.setPercent(rs.getString("percentchange"));
					}else if(gainer==null){
						gainer=new LossGain();
						gainer.setChange(change);
						gainer.setSymbol(symbol);
						gainer.setPercent(rs.getString("percentchange"));
					}
				}
			}
			DBUtil.close();
			if(loser!=null){
				ArrayList<Double> prices=new ArrayList<Double>();
				Statement stmt1=DBUtil.connect();
				ResultSet rs1 = stmt1.executeQuery("SELECT symbol, date,time ,volume FROM `realtimedata` where symbol= '"+loser.getSymbol()+"' order by date desc,time desc limit 100");
				while(rs1.next()){
					Double vol=rs1.getDouble("volume");
					prices.add(vol);
				}
				rs1.close();
				DBUtil.close();
				
				loser.setPrices(prices);
			}else{
				loser=new LossGain();
				loser.setMessage("No losers");
			}

			if(gainer!=null){
				ArrayList<Double> prices=new ArrayList<Double>();
				Statement stmt1=DBUtil.connect();
				ResultSet rs1 = stmt1.executeQuery("SELECT symbol, date,time ,volume FROM `realtimedata` where symbol= '"+gainer.getSymbol()+"' order by date desc,time desc limit 100");
				while(rs1.next()){
					Double vol=rs1.getDouble("volume");
					prices.add(vol);
				}
				rs1.close();
				DBUtil.close();
				
				gainer.setPrices(prices);
				
			}else{
				gainer=new LossGain();
				gainer.setMessage("No gainers");
			}
			
			resp.setGainer(gainer);
			resp.setLoser(loser);
			return resp;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}



}

