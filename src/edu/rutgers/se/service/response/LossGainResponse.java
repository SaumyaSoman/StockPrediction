package edu.rutgers.se.service.response;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
/**
 * Annotations response  
 * @author Saumya
 *
 */
public class LossGainResponse {

	private LossGain loser;	
	private LossGain gainer;
	/**
	 * @return the loser
	 */
	public LossGain getLoser() {
		return loser;
	}
	/**
	 * @param loser the loser to set
	 */
	public void setLoser(LossGain loser) {
		this.loser = loser;
	}
	/**
	 * @return the gainer
	 */
	public LossGain getGainer() {
		return gainer;
	}
	/**
	 * @param gainer the gainer to set
	 */
	public void setGainer(LossGain gainer) {
		this.gainer = gainer;
	}
	
}
