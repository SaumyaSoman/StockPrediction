package edu.rutgers.se.service.response;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
/**
 * Annotations response  
 * @author Suvigya
 *
 */
public class QuoteResponse {

	private ArrayList<Quote> quotelist;

	/**
	 * @return the quotelist
	 */
	public ArrayList<Quote> getQuotelist() {
		return quotelist;
	}

	/**
	 * @param quotelist the quotelist to set
	 */
	public void setQuotelist(ArrayList<Quote> quotelist) {
		this.quotelist = quotelist;
	}
	
	
	
		
}
