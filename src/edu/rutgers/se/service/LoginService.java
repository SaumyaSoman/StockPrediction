package edu.rutgers.se.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.json.JSONException;
import org.json.JSONObject;

import edu.rutgers.se.service.response.WSResponse;
import edu.rutgers.se.utils.DBUtil;

@Path("/login")

/**
 * 
 * @author Saumya
 *
 */
public class LoginService {
    
  @POST
  @Produces("application/json")
  public WSResponse login(@QueryParam("username") String username , @QueryParam("password") String password) {
  		
	  String message="";
	  if(username.isEmpty() || password.isEmpty()){
		  message = "Please enter username and password!!";
	  }else{
		  if(validate(username,password)){
			  message= "success";
		  }else{
			  message= "User does not exist!";
		  }
	  }
	  WSResponse resp=new WSResponse();  
	  resp.setStatus(message);
      return resp;

  }

private boolean validate(String username, String password) {
	boolean success=false;
	Statement stmt=DBUtil.connect();
	try {
		ResultSet rs = stmt.executeQuery("SELECT * FROM  user WHERE username =  '"+username+"' AND PASSWORD =  '"+password+"'");
		if(rs!=null && rs.first()) return success=true;
		rs.close();
	} catch (SQLException e) {
		e.printStackTrace();
	}
	return success;
}
public static void main(String[] args) {
	System.out.println(new LoginService().validate("saumya","saumya"));
}
  
}
 
