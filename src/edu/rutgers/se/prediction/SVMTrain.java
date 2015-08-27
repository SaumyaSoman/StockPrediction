package edu.rutgers.se.prediction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.xml.ws.RespectBindingFeature;

import libsvm.svm_node;
import libsvm.svm_problem;
//import ca.uwo.csd.ai.nlp.libsvm.svm_node;
import edu.rutgers.se.svm.Instance;
import edu.rutgers.se.svm.KernelManager;
import edu.rutgers.se.svm.LinearKernel;
import edu.rutgers.se.svm.RBFKernel;
import edu.rutgers.se.svm.SVMPredictor;
import edu.rutgers.se.svm.SVMTrainer;
import edu.rutgers.se.svm.svm_model;
import edu.rutgers.se.svm.svm_parameter;
import edu.rutgers.se.utils.Constants;
//import ca.uwo.csd.ai.nlp.libsvm.svm_problem;
import edu.rutgers.se.utils.DataFileReader;

/**
 * Train svm for the tickers
 * @author Ankit
 */
public class SVMTrain {

	public static void createModels() throws IOException, ClassNotFoundException {

		String[] symbols=new String[]{"YHOO","AMZN","GOOG","BBY","EBAY","LNKD","MSFT","FB","TWTR","AAPL"};
		//String[] symbols=new String[]{"AMZN"};
		for (String symbol : symbols) {
			String trainFileName = Constants.foldername+"\\training\\"+symbol;
			//Read training file
			Instance[] trainingInstances = DataFileReader.readDataFile(trainFileName);             


			//Setup parameters
			svm_parameter param = new svm_parameter();
			param.probability = 1;
			param.gamma = 0.2;
			param.nu = 0.5;
			param.C = 1;
			param.svm_type = svm_parameter.C_SVC;       
			param.cache_size = 20000;
			param.eps = 0.001;

			//Register kernel function
			KernelManager.setCustomKernel(new RBFKernel(param));   

			//Train the model
			System.out.println("Training started...");
			svm_model model = SVMTrainer.train(trainingInstances, param);
			System.out.println("Training completed.");

			//Save the trained model
			SVMTrainer.saveModel(model, Constants.foldername+"\\model\\"+symbol);
			//model = SVMPredictor.load_model("a1a.model");

		}
	}

	private static void writeOutputs(String outputFileName, double[] predictions) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));
		for (double p : predictions) {
			writer.write(String.format("%.0f\n", p));
		}
		writer.close();
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {    

		createInstances();
		createModels();    	
	}

	private static void createInstances() {

		String[] symbols=new String[]{"YHOO","AMZN","GOOG","BBY","EBAY","LNKD","MSFT","FB","TWTR","AAPL"};
		//String[] symbols=new String[]{"AMZN"};
		Connection conn = null;
		Statement stmt = null;
		for (String symbol : symbols) {
			try{
				//Make JDBC connection to database ; username - root ; passwors - (blank)
				conn = DriverManager.getConnection("jdbc:mysql://localhost/stock", "root", "");
				stmt = conn.createStatement();
				System.out.println("hmm");
				String sql = " SELECT Adj_Close FROM historicaldata WHERE symbol= '"+symbol+"' ORDER BY date Desc LIMIT 501";
				ResultSet rs = stmt.executeQuery(sql);	
				ArrayList<Double> prices=new ArrayList();
				System.out.println("got");
				while(rs.next()){
					//Retrieve by column name
					Double price  = rs.getDouble("Adj_Close");
					prices.add(price);
				}
				createFile(prices,symbol);
				rs.close();
			}catch(SQLException se){
			}finally{
				try {
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}	
		}

	}

	private static void createFile(ArrayList<Double> prices, String symbol) {

		StringBuffer sb=new StringBuffer();
		int count=1;
		PrintWriter writer;
		try {
			File f=new File(Constants.foldername+"\\training\\"+symbol);
			f.createNewFile();
			writer = new PrintWriter(Constants.foldername+"\\training\\"+symbol, "UTF-8");
			for(int i=0;i<prices.size();i++){
				if(count<=5){
					sb.append(" "+count+":"+prices.get(i));
					count++;
				}else{
					if(prices.get(i)>=prices.get(i-1)){
						sb.insert(0, "+1");
					}else{
						sb.insert(0, "-1");
					}
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