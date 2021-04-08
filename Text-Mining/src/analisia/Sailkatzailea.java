package analisia;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.core.Instances;
import weka.core.Option;

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
	
	/*public double[] ekortuSVM(Instances data) throws Exception {
		
		SMO svm = new SMO();
		svm.buildClassifier(data);
		RBFKernel kernel = new RBFKernel();
		double[] emaitza = new double[2];
		emaitza[0] = -1;
		emaitza[1] = -1;
		double C = -1;
		double gamma = -1;
		double accuracy = 0.00;
		int atalase = data.numInstances()/5;
		System.out.println(data.numInstances());
		for (int i=1;i<=atalase;i++) {
			
			svm.setC(i);
			//System.out.println("owo no he empezado este round");
			
			for (int j=1;j<=10;j++) {
				
				kernel.setGamma(j);
				svm.setKernel(kernel);
				this.entrenatuSVM(data, i, j);
				Evaluation e = this.ebaluatuCrossVal(data, svm, 10, new Random(1));
				if (e.pctCorrect()>=accuracy) {
					
					C=i;
					gamma=j;
					accuracy=e.pctCorrect();
					
				}
				
				
			}
			//System.out.println("Ekortuado "+i+" veces");
			if (i%10==0) {
				
				System.out.println("llevo "+i+" iteraciones");
				
			}
			
		}
		emaitza[0]=C;
		emaitza[1]=gamma;
		
		return emaitza;
	}*/
	
	public SMO entrenatuSVM(Instances data, double c, double gamma, int aukera) throws Exception {
		
		
		SMO svm = new SMO();
		System.out.println("#####Entrenamendua#####");
		if(aukera==1) {
			System.out.println("---> Kernel lineala");
			PolyKernel kernel = new PolyKernel();
			svm.setKernel(kernel);
			
		}
		else {
			System.out.println("---> RBF Kernela");
			System.out.println("---> Gamma = "+gamma);
			RBFKernel kernel = new RBFKernel();
			kernel.setGamma(gamma);
			svm.setKernel(kernel);
		}
		System.out.println("---> C = "+c);
		
		svm.setC(c);
		
		svm.buildClassifier(data);
		return svm;
		
	}
	
	public SMO sortuSVM(double c, double gamma, int aukera) throws Exception {
		
		
		SMO svm = new SMO();
		System.out.println("#####Entrenamendua#####");
		if(aukera==1) {
			System.out.println("---> Kernel lineala");
			PolyKernel kernel = new PolyKernel();
			svm.setKernel(kernel);
			
		}
		else {
			System.out.println("---> RBF Kernela");
			System.out.println("---> Gamma = "+gamma);
			RBFKernel kernel = new RBFKernel();
			kernel.setGamma(gamma);
			svm.setKernel(kernel);
		}
		System.out.println("---> C = "+c);
		
		svm.setC(c);

		return svm;
		
	}
	
	
	
}

