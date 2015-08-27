package edu.rutgers.se.service;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import edu.rutgers.se.service.response.DatePrice;
import edu.rutgers.se.service.response.GraphResponse;
import edu.rutgers.se.utils.DBUtil;

@Path("/plot")

/**
 * 
 * @author Suvigya
 *
 */
public class GraphService {
    
  @POST
  @Produces("application/json")
  public GraphResponse plot(@QueryParam("symbol") String symbol,@QueryParam("start") String startDate,@QueryParam("end") String endDate) {
  	
	  GraphResponse resp=null;
	  Statement stmt=DBUtil.connect();
		try {
			ResultSet rs = stmt.executeQuery("SELECT symbol,date,close FROM `historicaldata` where symbol='"+symbol+"' and date between '"+startDate+"' and '"+endDate+"' group by date order by date asc");
			ArrayList<DatePrice> list=new ArrayList<DatePrice>();
			resp=new GraphResponse();
			//System.out.println("asd");
			while(rs.next()){
				SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
				DatePrice dp=new DatePrice();
				Date date=rs.getDate("date");
				Double price=rs.getDouble("close");
				dp.setDate(sdf.format(date));
				dp.setPrice(price);
				list.add(dp);
			}
			resp.setList(list);
			DBUtil.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return resp;
  }


  
}
 
