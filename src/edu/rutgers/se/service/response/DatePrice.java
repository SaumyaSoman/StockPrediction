package edu.rutgers.se.service.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement

/**
 * 
 * @author Ankit
 *
 */
public class DatePrice {

	private String date;
	private double price;
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	
	
	
	
}
