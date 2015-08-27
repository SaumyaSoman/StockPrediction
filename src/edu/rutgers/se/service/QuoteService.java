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

import edu.rutgers.se.service.response.Quote;
import edu.rutgers.se.service.response.QuoteResponse;
import edu.rutgers.se.service.response.TrendingResponse;
import edu.rutgers.se.utils.DBUtil;

@Path("/quote")

/**
 * 
 * @author Saumya
 *
 */
public class QuoteService {

	@POST
	@Produces("application/json")
	public QuoteResponse quote() {

		Statement stmt=DBUtil.connect();
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM `realtimedata` order by date desc,time desc limit 10");
			ArrayList<Quote> list=new ArrayList<Quote>();
			while(rs.next()){
				Quote quote=new Quote();
				String symbol=rs.getString("symbol");
				quote.setSymbol(symbol);
				double price=rs.getDouble("price");
				quote.setPrice(price);
				double change=rs.getDouble("change");
				quote.setChange(change);
				String percent=rs.getString("percentchange");
				quote.setPercent(percent);
				list.add(quote);
			}
			DBUtil.close();
			QuoteResponse resp=new QuoteResponse();
			resp.setQuotelist(list);
			return resp;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	@Path("/single")
	@POST
	@Produces("application/json")
	public Quote getQuote(@QueryParam("symbol") String symbol) {

		Quote quote=new Quote();
		Statement stmt=DBUtil.connect();
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM `realtimedata` where symbol='"+symbol+"' order by date desc,time desc limit 1");
			while(rs.next()){
				quote.setSymbol(symbol);
				double price=rs.getDouble("price");
				quote.setPrice(price);
				double change=rs.getDouble("change");
				quote.setChange(change);
				String percent=rs.getString("percentchange");
				quote.setPercent(percent);
			}
			DBUtil.close();
			return quote;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}



}

