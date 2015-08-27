package edu.rutgers.se.prediction;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import utils.DataFileReader;
import ca.uwo.csd.ai.nlp.kernel.KernelManager;
import ca.uwo.csd.ai.nlp.kernel.RBFKernel;
import ca.uwo.csd.ai.nlp.libsvm.svm_model;
import ca.uwo.csd.ai.nlp.libsvm.svm_parameter;
import ca.uwo.csd.ai.nlp.libsvm.ex.Instance;
import ca.uwo.csd.ai.nlp.libsvm.ex.SVMPredictor;
import edu.rutgers.se.utils.Constants;



/**
 * Class which predicts using SVM, RSI and MACD
 * @author Saumya
 *
 */
public class SVMPredict {

	public static int predict(ArrayList<Double> prices, String symbol){		

		int vote=0;
		if(prices!=null && prices.size()>0){
			createTestFile(prices, symbol);
			svm_model model;
			try {
				//Setup parameters
				svm_parameter param = new svm_parameter();
				param.probability = 1;
				param.gamma = 0.1;
				param.nu = 0.5;
				param.C = 1;
				param.svm_type = svm_parameter.C_SVC;       
				param.cache_size = 20000;
				param.eps = 0.001;

				//Register kernel function
				KernelManager.setCustomKernel(new RBFKernel(param));
				model = SVMPredictor.loadModel(Constants.foldername+"\\model\\"+symbol);
				Instance[] testingInstances = DataFileReader.readDataFile(Constants.foldername+"\\test\\"+symbol);
				//Predict results
				double[] predictions = SVMPredictor.predict(testingInstances, model, true);
				vote= predictions[0] >0 ? vote+1 : vote-1;
				System.out.println("predictions[0] ..."+predictions[0] );
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
						
			double macd=calculateMACD(prices);
			System.out.println(macd);
			vote= macd>0 ? vote+1 : vote-1;
			
			double rsi = calculateRSI(prices, 15);
			System.out.println("rsi..."+rsi);
			vote= rsi<30 ? vote+1 : vote;
			vote= rsi>80 ? vote-1 : vote;		
		}
		
		System.out.println("vote..."+vote);
		return vote;
		
	}
	
	private static double calculateMACD(ArrayList<Double> prices) {
		ExponentialMovingAverage slow = new ExponentialMovingAverage(26);
		ExponentialMovingAverage fast = new ExponentialMovingAverage(12);
		for(int i=0;i<prices.size() && i<15;i++) {
			double price = prices.get(i);			
			slow.update(price);
			if(i<15){
				fast.update(price);
			}
		}

		double slowAv = slow.getAverage();
		double fastAv = fast.getAverage();
		return fastAv-slowAv;
	}

	private static double calculateRSI(ArrayList<Double> prices, int timeframe) {
		ExponentialMovingAverage avU = new ExponentialMovingAverage(timeframe);
		ExponentialMovingAverage avD = new ExponentialMovingAverage(timeframe);
		double oldPrice = prices.get(0);
		prices.remove(0);

		for(int i=0;i<prices.size() && i<15;i++) {
			
			double price = prices.get(i);
			double diff =  price - oldPrice;
			if (diff > 0) 
				avU.update(diff);
			else
				avD.update(Math.abs(diff));
			oldPrice = price;
		}
		double uAv = avU.getAverage();
		double dAv = avD.getAverage();
		if(uAv > 0 && dAv > 0)
			return (100 - (100/(1+(avU.getAverage()/avD.getAverage()))));
		else //needs further investigation
			return 50;
	}
	
	private static void createTestFile(ArrayList<Double> prices, String symbol) {

		StringBuffer sb=new StringBuffer();
		int count=1;
		PrintWriter writer;
		try {
			File f=new File(Constants.foldername+"\\test\\"+symbol);
			f.createNewFile();
			writer = new PrintWriter(Constants.foldername+"\\test\\"+symbol, "UTF-8");
			for(int i=0;i<prices.size();i++){
				if(count<5){
					sb.append(" "+count+":"+prices.get(i));
					count++;
				}else{
//					if(prices.get(i)>=prices.get(i-1)){
//						sb.insert(0, "+1");
//					}else{
//						sb.insert(0, "-1");
//					}
					sb.insert(0, "+1");
					count=1;
					//System.out.println(sb.toString());
					writer.println(sb.toString());
					sb.delete(0, sb.length());
				}
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
