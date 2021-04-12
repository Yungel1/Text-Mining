package analisia;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.ClassifierAttributeEval;
import weka.attributeSelection.CorrelationAttributeEval;
import weka.attributeSelection.GainRatioAttributeEval;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.OneRAttributeEval;
import weka.attributeSelection.ReliefFAttributeEval;
import weka.attributeSelection.SymmetricalUncertAttributeEval;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.SMO;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemovePercentage;

public class Analisia {
	
	private Instances data;
	
	private SMO smo;
	
	public Instances getData() {
		return data;
	}

	public void setData(Instances data) {
		this.data = data;
	}

	public SMO getSmo() {
		return smo;
	}

	public void setSmo(SMO smo) {
		this.smo = smo;
	}

	private Instances instantziakKargatu(String dataPath) throws Exception {
		
		DataSource source = new DataSource(dataPath);
        Instances data = source.getDataSet();
        data.setClassIndex(0);
        return data;
	}
	
	private void bowtfRS() throws Exception {
		
		Aurreprozesamendua pro = new Aurreprozesamendua();
		Sailkatzailea sail = new Sailkatzailea();
		Instances trainBektore = pro.errepresentazioBektoriala(data,"src/analisia/Arff fitxategia/dictionary.txt");
		
		Logistic lo = sail.logisticEntrenatu(trainBektore);
		Evaluation eval= sail.ebaluatu(trainBektore,trainBektore, lo);
	
		System.out.println("@@@@@@BOW");
		
		System.out.println("Accuracy: ");
		System.out.println(eval.pctCorrect());
		
		System.out.println("WPrecision: ");
		System.out.println(eval.weightedPrecision());
		System.out.println("WRecall: ");
		System.out.println(eval.weightedRecall());
		System.out.println("WFM: ");
		System.out.println(eval.weightedFMeasure());
		
		System.out.println("P S: ");
		System.out.println(eval.precision(1));
		System.out.println("R S: ");
		System.out.println(eval.recall(1));
		System.out.println("FM S: ");
		System.out.println(eval.fMeasure(1));
		
		System.out.println("P H: ");
		System.out.println(eval.precision(0));
		System.out.println("R H: ");
		System.out.println(eval.recall(0));
		System.out.println("FM H: ");
		System.out.println(eval.fMeasure(0));
		
		System.out.println("@@@@@@TFIDF");
		
		trainBektore = pro.errepresentazioBektorialaTFIDF(data,"src/analisia/Arff fitxategia/dictionary.txt");
		
		Logistic lo1 = sail.logisticEntrenatu(trainBektore);
		Evaluation eval1= sail.ebaluatu(trainBektore,trainBektore, lo1);
		
		System.out.println("Accuracy: ");
		System.out.println(eval1.pctCorrect());
		
		System.out.println("WPrecision: ");
		System.out.println(eval1.weightedPrecision());
		System.out.println("WRecall: ");
		System.out.println(eval1.weightedRecall());
		System.out.println("WFM: ");
		System.out.println(eval1.weightedFMeasure());
		
		System.out.println("P S: ");
		System.out.println(eval1.precision(1));
		System.out.println("R S: ");
		System.out.println(eval1.recall(1));
		System.out.println("FM S: ");
		System.out.println(eval1.fMeasure(1));
		
		System.out.println("P H: ");
		System.out.println(eval1.precision(0));
		System.out.println("R H: ");
		System.out.println(eval1.recall(0));
		System.out.println("FM H: ");
		System.out.println(eval1.fMeasure(0));
		
	}
	
	private void bowtf() throws Exception {
		
		Aurreprozesamendua pro = new Aurreprozesamendua();
		Sailkatzailea sail = new Sailkatzailea();
		double[] accArray= new double[5];
		
		double[] wPArray= new double[5];
		double[] wRArray= new double[5];
		double[] wFMArray= new double[5];
		
		double[] pSArray= new double[5];
		double[] rSArray= new double[5];
		double[] fmSArray= new double[5];
		
		double[] pHArray= new double[5];
		double[] rHArray= new double[5];
		double[] fmHArray= new double[5];
		
		
		for(int i=0;i<5;i++) {
			Instances [] trainTest = pro.randomSplit(data, i);
			Instances trainBektore = pro.errepresentazioBektoriala(trainTest[0],"src/analisia/Arff fitxategia/dictionary.txt");
			Instances testBektore = pro.testaEgokitu("src/analisia/Arff fitxategia/dictionary.txt", trainTest[1]);	
			//entrenamedu
			Logistic lo = sail.logisticEntrenatu(trainBektore);
			Evaluation eval= sail.ebaluatu(testBektore,trainBektore, lo);
		
			accArray[i]=eval.pctCorrect();
			
			wPArray[i] = eval.weightedPrecision();
			wRArray[i] = eval.weightedRecall();
			wFMArray[i] = eval.weightedFMeasure();
			
			pSArray[i] = eval.precision(1);
			rSArray[i] = eval.recall(1);
			fmSArray[i] = eval.fMeasure(1);
			
			pHArray[i] = eval.precision(0);
			rHArray[i] = eval.recall(0);
			fmHArray[i] = eval.fMeasure(0);

		}
		System.out.println("#######BOW#######");
		System.out.println("Accuracy: ");
		sail.calculateSD(accArray);
		
		System.out.println("Weighted Precision: ");
		sail.calculateSD(wPArray);
		System.out.println("Weighted Recall: ");
		sail.calculateSD(wRArray);
		System.out.println("Weighted FM: ");
		sail.calculateSD(wFMArray);
		
		System.out.println("Precision SPAM: ");
		sail.calculateSD(pSArray);
		System.out.println("Recall SPAM: ");
		sail.calculateSD(rSArray);
		System.out.println("FM SPAM: ");
		sail.calculateSD(fmSArray);
		
		System.out.println("Precision HAM: ");
		sail.calculateSD(pHArray);
		System.out.println("Recall HAM: ");
		sail.calculateSD(rHArray);
		System.out.println("FM HAM: ");
		sail.calculateSD(fmHArray);
		
		for(int i=0;i<5;i++) {
			Instances [] trainTest = pro.randomSplit(data, i);
			Instances trainBektore = pro.errepresentazioBektorialaTFIDF(trainTest[0],"src/analisia/Arff fitxategia/dictionary.txt");
			Instances testBektore = pro.testaEgokitu("src/analisia/Arff fitxategia/dictionary.txt", trainTest[1]);	
			//entrenamedu
			Logistic lo = sail.logisticEntrenatu(trainBektore);
			Evaluation eval= sail.ebaluatu(testBektore,trainBektore, lo);

			accArray[i]=eval.pctCorrect();
			
			wPArray[i] = eval.weightedPrecision();
			wRArray[i] = eval.weightedRecall();
			wFMArray[i] = eval.weightedFMeasure();
			
			pSArray[i] = eval.precision(1);
			rSArray[i] = eval.recall(1);
			fmSArray[i] = eval.fMeasure(1);
			
			pHArray[i] = eval.precision(0);
			rHArray[i] = eval.recall(0);
			fmHArray[i] = eval.fMeasure(0);

		}
		System.out.println("#######TF-IDF#######");
		System.out.println("Accuracy: ");
		sail.calculateSD(accArray);
		
		System.out.println("Weighted Precision: ");
		sail.calculateSD(wPArray);
		System.out.println("Weighted Recall: ");
		sail.calculateSD(wRArray);
		System.out.println("Weighted FM: ");
		sail.calculateSD(wFMArray);
		
		System.out.println("Precision SPAM: ");
		sail.calculateSD(pSArray);
		System.out.println("Recall SPAM: ");
		sail.calculateSD(rSArray);
		System.out.println("FM SPAM: ");
		sail.calculateSD(fmSArray);
		
		System.out.println("Precision HAM: ");
		sail.calculateSD(pHArray);
		System.out.println("Recall HAM: ");
		sail.calculateSD(rHArray);
		System.out.println("FM HAM: ");
		sail.calculateSD(fmHArray);
	}
	
	private void attSelFroga() throws Exception {
		
		Aurreprozesamendua pro = new Aurreprozesamendua();
		Sailkatzailea sail = new Sailkatzailea();
		
		ArrayList<ASEvaluation> evAttList = new ArrayList<ASEvaluation>();
		
		//evAttList.add(new ClassifierAttributeEval());
		//evAttList.add(new CorrelationAttributeEval());
		evAttList.add(new GainRatioAttributeEval());
		//evAttList.add(new InfoGainAttributeEval());
		//evAttList.add(new OneRAttributeEval());
		//evAttList.add(new ReliefFAttributeEval());
		//evAttList.add(new SymmetricalUncertAttributeEval());
		
		int[] attKopList = new int[2];
		attKopList[0] = 5000;
		attKopList[1] = 6000;
		/*attKopList[2] = 6000;
		attKopList[3] = 8000;
		attKopList[4] = 9000;
		attKopList[5] = 10000;*/
		
		int attKop;
		ASEvaluation eval;

		//Instances [] trainTest = pro.randomSplit(data, 1);
		Instances trainBektore = pro.errepresentazioBektoriala(data,"src/analisia/Arff fitxategia/dictionary.txt");
		//Instances testBektore = pro.testaEgokitu("src/analisia/Arff fitxategia/dictionary.txt", trainTest[1]);	
		
		for(int i=0;i<evAttList.size();i++) {
			eval = evAttList.get(i);
			for(int j=0;j<attKopList.length;j++) {
				attKop = attKopList[j];
				Instances trainAS = pro.attributeSelection(trainBektore, eval, attKop);
				System.out.println("Instantziak gordetzen...");
				/*ArffSaver s= new ArffSaver();
				s.setInstances(trainAS);
				s.setFile(new File("src/analisia/Arff fitxategia/trainAS.arff"));
				s.writeBatch();*/
				//Instances testAS = pro.egokitu(trainAS, testBektore);
				//Logistic lo = sail.logisticEntrenatu(trainAS);

				Evaluation evaluation = sail.ebaluatuCrossVal(trainAS,new Logistic(),3,new Random(1));
				
				System.out.println("Accuracy: ");
				System.out.println(evaluation.pctCorrect());
				
				System.out.println("WPrecision: ");
				System.out.println(evaluation.weightedPrecision());
				System.out.println("WRecall: ");
				System.out.println(evaluation.weightedRecall());
				System.out.println("WFM: ");
				System.out.println(evaluation.weightedFMeasure());
				
				System.out.println("P S: ");
				System.out.println(evaluation.precision(1));
				System.out.println("R S: ");
				System.out.println(evaluation.recall(1));
				System.out.println("FM S: ");
				System.out.println(evaluation.fMeasure(1));
				
				System.out.println("P H: ");
				System.out.println(evaluation.precision(0));
				System.out.println("R H: ");
				System.out.println(evaluation.recall(0));
				System.out.println("FM H: ");
				System.out.println(evaluation.fMeasure(0));
			}
		}
		
	}
	
	private void svmEkorketa() throws Exception {
		
		Aurreprozesamendua pro = new Aurreprozesamendua();
		Sailkatzailea sail = new Sailkatzailea();
		
		/*Instances [] trainTest = pro.randomSplit(data, 1);
		Instances trainBektore = pro.errepresentazioBektoriala(trainTest[0],"src/analisia/Arff fitxategia/dictionary.txt");
		Instances testBektore = pro.testaEgokitu("src/analisia/Arff fitxategia/dictionary.txt", trainTest[1]);	*/
		
		//Instances trainAS = pro.attributeSelection(trainBektore, new ClassifierAttributeEval(), 1000);
		//Instances testAS = pro.egokitu(trainAS, testBektore);
		
		DataSource source = new DataSource("src/analisia/Arff fitxategia/trainAS.arff");
		Instances trainAS = source.getDataSet();
		trainAS.setClassIndex(0);
		
		double[] balioakC = new double[] {1.0};
		double[] balioakGE = new double[] {1.0};
		double c;
		double g;
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Aukeratu Kernela: ");
		System.out.println("1. Linear");
		System.out.println("2. RBF");
		System.out.println("3. Polynomial");
		
		int aukera = sc.nextInt();
		
		Evaluation evaluation;
		
		for(int i=0;i<balioakC.length;i++) {
			c=balioakC[i];
			if(aukera==1) {
				SMO svm = sail.sortuSVM(c, -1, aukera);
				evaluation = sail.ebaluatuCrossVal(trainAS,svm,3,new Random(1));
				
				System.out.println("Accuracy: ");
				System.out.println(evaluation.pctCorrect());
				
				System.out.println("WPrecision: ");
				System.out.println(evaluation.weightedPrecision());
				System.out.println("WRecall: ");
				System.out.println(evaluation.weightedRecall());
				System.out.println("WFM: ");
				System.out.println(evaluation.weightedFMeasure());
				
				System.out.println("P S: ");
				System.out.println(evaluation.precision(1));
				System.out.println("R S: ");
				System.out.println(evaluation.recall(1));
				System.out.println("FM S: ");
				System.out.println(evaluation.fMeasure(1));
				
				System.out.println("P H: ");
				System.out.println(evaluation.precision(0));
				System.out.println("R H: ");
				System.out.println(evaluation.recall(0));
				System.out.println("FM H: ");
				System.out.println(evaluation.fMeasure(0));
			}
			else {
				for(int j=0;j<balioakGE.length;j++) {
					g=balioakGE[j];
					SMO svm = sail.sortuSVM(c, g, aukera);
					evaluation = sail.ebaluatuCrossVal(trainAS,svm,3,new Random(1));
					
					//Gorde eredua
					/*System.out.println("Gordetzen...");
					SMO svmGorde = sail.entrenatuSVM(trainAS, c, g, aukera);
					weka.core.SerializationHelper.write("src/ehes/resources/spam.model", svmGorde);*/
					
					System.out.println("Accuracy: ");
					System.out.println(evaluation.pctCorrect());
					
					System.out.println("WPrecision: ");
					System.out.println(evaluation.weightedPrecision());
					System.out.println("WRecall: ");
					System.out.println(evaluation.weightedRecall());
					System.out.println("WFM: ");
					System.out.println(evaluation.weightedFMeasure());
					
					System.out.println("P S: ");
					System.out.println(evaluation.precision(1));
					System.out.println("R S: ");
					System.out.println(evaluation.recall(1));
					System.out.println("FM S: ");
					System.out.println(evaluation.fMeasure(1));
					
					System.out.println("P H: ");
					System.out.println(evaluation.precision(0));
					System.out.println("R H: ");
					System.out.println(evaluation.recall(0));
					System.out.println("FM H: ");
					System.out.println(evaluation.fMeasure(0));
				}
			}
		}
		
		
		
		
	}

	public static void main(String[] args) throws Exception {
		
		
		Scanner sc = new Scanner(System.in);
		Analisia analisia = new Analisia();
		
		
		analisia.setData(analisia.instantziakKargatu("src/analisia/Arff fitxategia/mailDatuak.arff"));
		
		System.out.println("Aukeratu: ");
		System.out.println("1. BoW eta TF alderatu");
		System.out.println("2. AttributeSelection optimoa");
		System.out.println("3. SVM parametro ekorketa");
		System.out.println("\n Sartu aukera (zenbakia): ");
		int aukera = sc.nextInt();

		if(aukera==1) {
			analisia.bowtf();
		} 
		else if(aukera==2) {
			analisia.attSelFroga();
		}
		else if(aukera==3) {
			analisia.svmEkorketa();
		}
		else {
			System.out.println("Txarto sartu duzu aukera");
			System.exit(1);
		}
		/*
		
		Instances train = analisia.instantziakKargatu("/home/adrian/EHES/Proiektua/ReutersCorn-train.arff");
		Instances test = analisia.instantziakKargatu("/home/adrian/EHES/Proiektua/ReutersCorn-test.arff");
		
		Aurreprozesamendua pro = new Aurreprozesamendua();
		Sailkatzailea sail = new Sailkatzailea();

		
		Instances data = analisia.instantziakKargatu("/home/mikel/Desktop/Proiektua/mailDatuak.arff");
		//Instances test = analisia.instantziakKargatu("/home/adrian/EHES/Proiektua/ReutersCorn-test.arff");
		
		for(int i=0;i<5;i++) {
			Instances [] trainTest = pro.randomSplit(data, i);
			Instances trainBektore = pro.errepresentazioBektoriala(trainTest[0],"/home/mikel/Desktop/Proiektua/dictionary");
			Instances testBektore = pro.testaEgokitu("/home/mikel/Desktop/Proiektua/dictionary", trainTest[1]);	
			//entrenamedu
			Logistic lo = sail.logisticEntrenatu(trainBektore);
			Evaluation eval= sail.ebaluatu(testBektore,trainBektore, lo);
			System.out.println(eval.fMeasure(0));
			numArray[i]=eval.fMeasure(0);
		}
		sail.calculateSD(numArray);
		
		for(int i=0;i<5;i++) {
			Instances [] trainTest = pro.randomSplit(data, i);
			Instances trainBektore = pro.errepresentazioBektorialaTF(trainTest[0],"/home/mikel/Desktop/Proiektua/dictionary");
			Instances testBektore = pro.testaEgokitu("/home/mikel/Desktop/Proiektua/dictionary", trainTest[1]);	
			//entrenamedu
			Logistic lo = sail.logisticEntrenatu(trainBektore);
			Evaluation eval= sail.ebaluatu(testBektore,trainBektore, lo);
			System.out.println(eval.fMeasure(0));
			numArray[i]=eval.fMeasure(0);
		}
		sail.calculateSD(numArray);


		

		
		//MIKEL
		//pro.arffFitxategia();
		//ADRIÁN
		//Instances trainBektore=pro.errepresentazioBektoriala(train, "/home/adrian/EHES/Proiektua/ReutersCorn-trainBektore.arff");
		//System.out.println(trainBektore.numAttributes()+"##"+trainBektore.classIndex()+"##"+trainBektore.classAttribute().name());
		//Instances trainASBektore = pro.attributeSelection(trainBektore, new ClassifierAttributeEval(), 4000);
		//System.out.println(trainASBektore.numAttributes()+"##"+trainASBektore.classIndex()+"##"+trainASBektore.classAttribute().name());
		//Instances testEgokituta = pro.testaEgokitu("/home/adrian/EHES/Proiektua/ReutersCorn-trainBektore.arff", test);
		//System.out.println(testEgokituta.numAttributes());
		//Instances trainBektore=pro.errepresentazioBektoriala(train, "/home/adrian/EHES/Proiektua/ReutersCorn-trainBektore.arff");
		System.out.println(trainBektore.numAttributes()+"##"+trainBektore.classIndex()+"##"+trainBektore.classAttribute().name());
		Instances trainASBektore = pro.attributeSelection(trainBektore, new ClassifierAttributeEval(), 4000);
		System.out.println(trainASBektore.numAttributes()+"##"+trainASBektore.classIndex()+"##"+trainASBektore.classAttribute().name());
		Instances testEgokituta = pro.testaEgokitu("/home/adrian/EHES/Proiektua/ReutersCorn-trainBektore.arff", test);
		System.out.println(testEgokituta.numAttributes());
		
		//ANDER
		Sailkatzailea svm = new Sailkatzailea();
		DataSource source = new DataSource(args[0]);
		Instances data = source.getDataSet();
		data.setClassIndex(data.numAttributes()-1);
		double[] param = svm.ekortuSVM(data);
		System.out.println(param[0]+" "+ param[1]);
		SMO sailk = svm.entrenatuSVM(data, param[0], param[1]);
		Evaluation e = svm.ebaluatuCrossVal(data, sailk, 10, new Random(1));
		System.out.println(e.toSummaryString());
		*/
		
	}

}
