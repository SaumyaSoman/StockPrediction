package edu.rutgers.se.service.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement

/**
 * 
 * @author Ankit
 *
 */
public class Quote {
	
	private String symbol;
	private double price;
	private double change;
	private String percent;
	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}
	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
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
	/**
	 * @return the change
	 */
	public double getChange() {
		return change;
	}
	/**
	 * @param change the change to set
	 */
	public void setChange(double change) {
		this.change = change;
	}
	/**
	 * @return the percent
	 */
	public String getPercent() {
		return percent;
	}
	/**
	 * @param percent the percent to set
	 */
	public void setPercent(String percent) {
		this.percent = percent;
	}
	
	
}
