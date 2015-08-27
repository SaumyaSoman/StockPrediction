package edu.rutgers.se.service.response;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
/**
 * Annotations response  
 * @author Hitesh
 *
 */
public class TrendingResponse {

	private String symbol;
	private ArrayList<Double> prices;
	
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
	
		
}
