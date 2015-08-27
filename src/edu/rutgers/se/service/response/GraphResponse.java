package edu.rutgers.se.service.response;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
/**
 * Annotations response  
 * @author Saumya
 *
 */
public class GraphResponse {
	
	private ArrayList<DatePrice> list;

	/**
	 * @return the list
	 */
	public ArrayList<DatePrice> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(ArrayList<DatePrice> list) {
		this.list = list;
	}
	
}
