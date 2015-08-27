package edu.rutgers.se.service.response;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement

/**
 * 
 * @author Hitesh
 *
 */
public class ShortTermResponse {
	
	private ArrayList<ShortTerm> list;
	private String status;
	
	
	
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the list
	 */
	public ArrayList<ShortTerm> getList() {
		return list;
	}
	/**
	 * @param list the list to set
	 */
	public void setList(ArrayList<ShortTerm> list) {
		this.list = list;
	}
		
}
