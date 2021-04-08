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
	
	private void bowtf() throws Exception {
		
		Aurreprozesamendua pro = new Aurreprozesamendua();
		Sailkatzailea sail = new Sailkatzailea();
		double[] numArray= new double[5];
		
		for(int i=0;i<5;i++) {
			Instances [] trainTest = pro.randomSplit(data, i);
			Instances trainBektore = pro.errepresentazioBektoriala(trainTest[0],"src/analisia/Arff fitxategia/dictionary.txt");
			Instances testBektore = pro.testaEgokitu("src/analisia/Arff fitxategia/dictionary.txt", trainTest[1]);	
			//entrenamedu
			Logistic lo = sail.logisticEntrenatu(trainBektore);
			Evaluation eval= sail.ebaluatu(testBektore,trainBektore, lo);
			System.out.println(eval.pctCorrect());
			numArray[i]=eval.pctCorrect();
		}
		sail.calculateSD(numArray);
		
		for(int i=0;i<5;i++) {
			Instances [] trainTest = pro.randomSplit(data, i);
			Instances trainBektore = pro.errepresentazioBektorialaTF(trainTest[0],"src/analisia/Arff fitxategia/dictionary.txt");
			Instances testBektore = pro.testaEgokitu("src/analisia/Arff fitxategia/dictionary.txt", trainTest[1]);	
			//entrenamedu
			Logistic lo = sail.logisticEntrenatu(trainBektore);
			Evaluation eval= sail.ebaluatu(testBektore,trainBektore, lo);
			System.out.println(eval.pctCorrect());
			numArray[i]=eval.pctCorrect();
		}
		sail.calculateSD(numArray);
	}
	
	private void attSelFroga() throws Exception {
		
		Aurreprozesamendua pro = new Aurreprozesamendua();
		Sailkatzailea sail = new Sailkatzailea();
		
		ArrayList<ASEvaluation> evAttList = new ArrayList<ASEvaluation>();
		
		evAttList.add(new ClassifierAttributeEval());
		evAttList.add(new CorrelationAttributeEval());
		evAttList.add(new GainRatioAttributeEval());
		evAttList.add(new InfoGainAttributeEval());
		evAttList.add(new OneRAttributeEval());
		evAttList.add(new ReliefFAttributeEval());
		evAttList.add(new SymmetricalUncertAttributeEval());
		
		int[] attKopList = new int[1];
		attKopList[0] = 500;
		/*attKopList[1] = 6000;
		attKopList[2] = 7000;
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
				//Instances testAS = pro.egokitu(trainAS, testBektore);
				//Logistic lo = sail.logisticEntrenatu(trainAS);

				Evaluation evaluation = sail.ebaluatuCrossVal(trainAS,new Logistic(),3,new Random(1));
				System.out.println(evaluation.pctCorrect());
			}
		}
		
	}
	
	private void svmEkorketa() throws Exception {
		
		Aurreprozesamendua pro = new Aurreprozesamendua();
		Sailkatzailea sail = new Sailkatzailea();
		
		Instances [] trainTest = pro.randomSplit(data, 1);
		Instances trainBektore = pro.errepresentazioBektoriala(trainTest[0],"src/analisia/Arff fitxategia/dictionary.txt");
		Instances testBektore = pro.testaEgokitu("src/analisia/Arff fitxategia/dictionary.txt", trainTest[1]);	
		
		Instances trainAS = pro.attributeSelection(trainBektore, new ClassifierAttributeEval(), 1000);
		Instances testAS = pro.egokitu(trainAS, testBektore);
		
		double[] balioak = new double[] {0.001,0.01,0.1,1,10,100};
		double c;
		double g;
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Aukeratu Kernela: ");
		System.out.println("1. Linear");
		System.out.println("2. RBF");
		
		int aukera = sc.nextInt();
		
		for(int i=0;i<balioak.length;i++) {
			c=balioak[i];
			if(aukera==1) {
				SMO svm = sail.entrenatuSVM(trainAS, c, -1, aukera);
				Evaluation evaluation = sail.ebaluatu(testAS,trainAS,svm);
				System.out.println(evaluation.pctCorrect());
			}
			else {
				for(int j=0;j<balioak.length;j++) {
					g=balioak[j];
					SMO svm = sail.entrenatuSVM(trainAS, c, g, aukera);
					Evaluation evaluation = sail.ebaluatu(testAS,trainAS,svm);
					System.out.println("--->"+evaluation.pctCorrect());
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
