package edu.rutgers.se.prediction;

/**
 * 
 * @author Saumya
 *
 */
public class ExponentialMovingAverage {

	private final float weight;
	private double prevEMA;

	public double update(final double newValue) {
		prevEMA = (weight * (newValue - prevEMA)) + prevEMA;
		return prevEMA;
	}

	public double getAverage() { return prevEMA; }
	public ExponentialMovingAverage(final int periods) {
		this.weight = 2 / (float)(1 + periods);
		this.prevEMA = 0;
	}


	public void reset(){

		this.prevEMA = 0;
	}



}