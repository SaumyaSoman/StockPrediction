package edu.rutgers.se.prediction;

//import scala.Tuple2;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.GradientBoostedTrees;
import org.apache.spark.mllib.tree.configuration.BoostingStrategy;
import org.apache.spark.mllib.tree.model.GradientBoostedTreesModel;
import org.apache.spark.mllib.util.MLUtils;

public class GBRT {

	public static void main(String[] args) {
		SimpleRegression st=new SimpleRegression();
		double[] tStrArray = new double[]{366.37, 386.5, 384.87, 385.66 ,366.37, 386.5, 384.87, 385.66,366.37,385.66,366.37};
		for(int i=0;i<7;i++){
			st.addData(i+1, tStrArray[i]);
		}
		System.out.println(st.getN());
		System.out.println(st.predict(11));
//		SparkConf sparkConf = new SparkConf().setAppName("JavaGradientBoostedTrees");
//		JavaSparkContext sc = new JavaSparkContext(sparkConf);
//
//		// Load and parse the data file.
//		String datapath = "data/mllib/sample_libsvm_data.txt";
//		JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(sc.sc(), datapath).toJavaRDD();
//		// Split the data into training and test sets (30% held out for testing)
//		JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[]{0.7, 0.3});
//		JavaRDD<LabeledPoint> trainingData = splits[0];
//		JavaRDD<LabeledPoint> testData = splits[1];
//
//		// Train a GradientBoostedTrees model.
//		//  The defaultParams for Regression use SquaredError by default.
//		BoostingStrategy boostingStrategy = BoostingStrategy.defaultParams("Regression");
//		boostingStrategy.setNumIterations(3); // Note: Use more iterations in practice.
//		boostingStrategy.getTreeStrategy().setMaxDepth(5);
//		//  Empty categoricalFeaturesInfo indicates all features are continuous.
//		Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<Integer, Integer>();
//		boostingStrategy.treeStrategy().setCategoricalFeaturesInfo(categoricalFeaturesInfo);
//
//		final GradientBoostedTreesModel model =
//		  GradientBoostedTrees.train(trainingData, boostingStrategy);
//
//		// Evaluate model on test instances and compute test error
//		JavaPairRDD<Double, Double> predictionAndLabel =
//		  testData.mapToPair(new PairFunction<LabeledPoint, Double, Double>() {
//		    @Override
//		    public Tuple2<Double, Double> call(LabeledPoint p) {
//		      return new Tuple2<Double, Double>(model.predict(p.features()), p.label());
//		    }
//		  });
//		Double testMSE =
//		  predictionAndLabel.map(new Function<Tuple2<Double, Double>, Double>() {
//		    @Override
//		    public Double call(Tuple2<Double, Double> pl) {
//		      Double diff = pl._1() - pl._2();
//		      return diff * diff;
//		    }
//		  }).reduce(new Function2<Double, Double, Double>() {
//		    @Override
//		    public Double call(Double a, Double b) {
//		      return a + b;
//		    }
//		  }) / data.count();
//		System.out.println("Test Mean Squared Error: " + testMSE);
//		System.out.println("Learned regression GBT model:\n" + model.toDebugString());
	}
}
