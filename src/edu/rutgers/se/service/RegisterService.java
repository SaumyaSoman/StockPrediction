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

@Path("/register")

/**
 * 
 * @author Saumya
 *
 */
public class RegisterService {

	@POST
	@Produces("application/json")
	public WSResponse login(@QueryParam("username") String username , @QueryParam("password") String password, @QueryParam("name") String name,@QueryParam("email") String email,
			@QueryParam("street") String street, @QueryParam("state") String state, @QueryParam("country") String country, @QueryParam("phone") String phone,
			@QueryParam("occupation") String occupation) {

		String message="";
		if(username.isEmpty() || password.isEmpty()){
			message = "Please enter username and password!!";
		}else{
			Statement stmt=DBUtil.connect();
			try {
				int row = stmt.executeUpdate("INSERT INTO `user`(`username`, `password`, `name`, `occupation`, `street`, `state`, `country`, `email`, `phoneNumber`) VALUES ('"+username+"','"+password+"','"+name+"','"+occupation+"','"+street+"','"+state+"','"+country+"','"+email+"','"+phone+"')");
				if(row==1) message = "success";
				else message = "User cannot be registered";
			} catch (SQLException e) {
				message = "User cannot be registered";
			}
		}
		WSResponse resp=new WSResponse();  
		resp.setStatus(message);
		return resp;
	}
}

