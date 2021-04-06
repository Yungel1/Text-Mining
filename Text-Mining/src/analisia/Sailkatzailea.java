package analisia;

import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.core.Instances;

public class Sailkatzailea {

	public Logistic logisticEntrenatu(Instances train) throws Exception {
		
		Logistic logCLS = new Logistic();
		logCLS.buildClassifier(train);
		return logCLS;
	}
	
	public Evaluation ebaluatu(Instances test, Instances train, Classifier cls) throws Exception {
		Evaluation e = new Evaluation(train);
		e.evaluateModel(cls, test);
		return e;
	}
	
	public Evaluation ebaluatuCrossVal(Instances data, Classifier cls, int k, Random r) throws Exception {
		
		Evaluation e = new Evaluation(data);
		e.crossValidateModel(cls, data, k, r);
		return e;
		
	}
	public  void calculateSD(double numArray[])
    {
        double sum = 0.0;
        double standardDeviation = 0.0;
        int length = numArray.length;

        for(double num : numArray) {
            sum += num;
        }

        double mean = sum/length;
        System.out.println(mean);

        for(double num: numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        System.out.println(Math.sqrt(standardDeviation/length));
    }
}

