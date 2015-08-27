package edu.rutgers.se.service.response;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
/**
 * Annotations response  
 * @author Saumya
 *
 */
public class LongTermResponse {

	private String status;
	private double predicted;
	
	
	/**
	 * @return the predicted
	 */
	public double getPredicted() {
		return predicted;
	}
	/**
	 * @param predicted the predicted to set
	 */
	public void setPredicted(double predicted) {
		this.predicted = predicted;
	}
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
	
}
