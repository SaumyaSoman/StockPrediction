package edu.rutgers.se.prediction;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.commons.math3.distribution.NormalDistribution;

import edu.rutgers.se.service.response.ShortTerm;
import Jama.Matrix;

/**
 * Class to predict target value t for a given set of training data vector 
 * and target data vector using Bayesian curve fitting
 * Alpha=0.005, Beta=11.1 and M=4 (fixed values)
 * @author Hitesh
 */

public class Bayesian {

	//Alpha
	static final double A= 0.005;
	//Beta
	static final double B= 11.1;
	//Order of the polynomial
	static final int M=4;
	static Matrix S;
	//number of inputs
	int N;	
	//predict the 10th value
	double x; 
	Matrix X, T;
	// time intervals from 1..N
	double[] xArray; 
	// Values read from csv
	double[] tVal; 

	/**
	 * Constructor
	 * @param fname filename
	 * @param N		number of inputs
	 * @param x		the xth value will be predicted
	 */
	Bayesian(double[] tVal,int N, double x) {
		try {
			this.N=N;
			this.x=x;
			
			this.tVal = tVal;
			xArray = new double[N];
			
			//Populating the array xArray with progressive values
			for (int i = 0; i < N; i++) {
				xArray[i] = i+1;
			}
			// Creating column matrices from xArray and tVal
			X = new Matrix(xArray, xArray.length);
			T = new Matrix(tVal, tVal.length);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			double[] prices=new double[]{366.37, 386.5, 384.87, 385.66 ,366.37, 386.5, 384.87, 385.66,366.37,385.66,0,0,0,0,0,0,0,0,0,0};
			Bayesian.predict(prices);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Find S
	 * @param phixt transpose of phi(x)
	 * @return S
	 */
	private Matrix findS(Matrix phixt) {
		Matrix SInv=  Matrix.identity(M+1, M+1).times(A);
		Matrix phiSum = new Matrix(M + 1, M + 1);
		for(int i=0;i<N-1;i++){
			phiSum.plusEquals(phi(xArray[i]).times(phixt));	
		}
		SInv=SInv.plusEquals(phiSum.times(B));
		return SInv.inverse();
	}
	
	/**
	 * Find phi(x) for a given x where phi(x) = x^i and i= 0...M
	 * @param x
	 * @return 	an mX1 vector phi
	 */

	private Matrix phi(double x){
		Matrix phix = new Matrix(M + 1, 1);
		for (int i = 0; i < M + 1; i++) {
			phix.set(i, 0, (Math.pow(x	, i)));
		}		
		return phix;
	}
	
	/**
	 * Find variance 
	 * @param phix	
	 * @param phixt
	 * @return double variance 
	 */
	private double variance(Matrix phix, Matrix phixt) {
		double var=1/B;
		Matrix v=phixt.times(S);
		v=v.times(phix);
		var=var+v.get(0, 0);
		return var;
	}

	/**
	 * Find mean
	 * @param phixt
	 * @return double mean
	 */
	private double mean(Matrix phixt) {
		Matrix mean= phixt.times(S).times(B);
		Matrix sum = phi(xArray[0]).times(tVal[0]);
		//sum of products
		for(int i=1;i<N-1;i++){ 
			sum.plusEquals(phi(xArray[i]).times(tVal[i]));			
		}
		mean=mean.times(sum);
		return mean.get(0, 0);
	}

	public static  ArrayList<ShortTerm> predict(double[] prices) {
		ArrayList<ShortTerm> list=new ArrayList<ShortTerm>();
		System.out.println();
		for(int i=10;i<20;i++){
			
			Bayesian bayes=new Bayesian(prices,i,i);
			Matrix phix=bayes.phi(bayes.x);
			Matrix phixt=phix.transpose();
			
			//find S
			S=bayes.findS(phixt);
			
			//Calculate mean and variance
			double mean = bayes.mean(phixt);
			double variance = bayes.variance(phix,phixt);
			
			//Find prediction
			NormalDistribution gaussian = new NormalDistribution(mean,Math.sqrt(variance));
			double prediction = gaussian.sample();
			ShortTerm st=new ShortTerm();
			st.setPredicted(prediction);
			st.setIndex(i-10+1);
			list.add(st);
			prices[i]=prediction;
		}
		
		return list;
	}
}
