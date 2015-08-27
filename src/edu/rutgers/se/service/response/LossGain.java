package edu.rutgers.se.service.response;

import java.util.ArrayList;

/**
 * 
 * @author Ankit
 *
 */
public class LossGain {

	private String symbol;
	private double change;
	private String percent;
	private String message;
	
	
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
	private ArrayList<Double> prices;
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
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the prices
	 */
	public ArrayList<Double> getPrices() {
		return prices;
	}
	/**
	 * @param prices the prices to set
	 */
	public void setPrices(ArrayList<Double> prices) {
		this.prices = prices;
	}
	
	
}
