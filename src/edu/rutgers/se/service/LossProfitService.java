package edu.rutgers.se.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.json.JSONException;
import org.json.JSONObject;

import edu.rutgers.se.prediction.SVMPredict;
import edu.rutgers.se.service.response.LossGain;
import edu.rutgers.se.service.response.LossGainResponse;
import edu.rutgers.se.service.response.LossProfitResponse;
import edu.rutgers.se.service.response.TrendingResponse;
import edu.rutgers.se.utils.DBUtil;
																													
@Path("/lossprofit")

/**
 * 
 * @author Ankit
 *
 */
public class LossProfitService {

	@POST
	@Produces("application/json")
	public LossProfitResponse lossprofit(@QueryParam("symbol") String symbol) {

		LossProfitResponse resp=new LossProfitResponse();
		LongTermPredictionService predict=new LongTermPredictionService();
		ArrayList<Double> prices =predict.getPrices(symbol);
		if(prices!=null){
			int vote= SVMPredict.predict(prices, symbol);
			try{
				Statement stmt=DBUtil.connect();
				ResultSet rs = stmt.executeQuery("SELECT close FROM `historicaldata` where symbol='"+symbol+"' order by date desc limit 7");
				ArrayList<Double> priceList=new ArrayList<Double>();
				while(rs.next()){
					Double close=rs.getDouble("close");
					priceList.add(close);
				}
				double dev=calculate(priceList);
				DBUtil.close();
				Calendar currentDate = Calendar.getInstance(); //Get the current date
				SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd"); //format it as per your requirement
				String dateNow = formatter.format(currentDate.getTime());
				System.out.println("Now the date is :=>  " + dateNow);
				if(vote>=1){
					Statement stmt1=DBUtil.connect();
					ResultSet rs1 = stmt1.executeQuery("SELECT symbol, max(price) as max FROM `realtimedata` where symbol='"+symbol+"' and date='"+dateNow+"'");
					double high=0;
					while(rs1.next()){
						high=rs1.getDouble("max");
					}
					resp.setStatus("profit");
					resp.setValue(high-dev);
					DBUtil.close();
				}else{
					Statement stmt1=DBUtil.connect();
					ResultSet rs1 = stmt1.executeQuery("SELECT symbol, min(price) as min FROM `realtimedata` where symbol='"+symbol+"' and date='"+dateNow+"'");
					double low=0;
					while(rs1.next()){
						low=rs1.getDouble("min");
					}
					resp.setStatus("loss");
					resp.setValue(low-dev);
					DBUtil.close();
				}
			}catch(SQLException e){
				e.printStackTrace();
			}
		}

		return resp;
	}

	private double calculate(ArrayList<Double> prices) {
		double[] delta=new double[6];
		for (int i=1;i<prices.size();i++) {
			delta[i-1]=prices.get(i)-prices.get(i-1);
		}
		double sum=0;
		for (double d : delta) {
			sum+=d;
		}
		return sum/6;
	}



}

