package analisia;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.ASSearch;
import weka.attributeSelection.Ranker;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.RemovePercentage;

/**
 * Datuak behar ditugun formatuan egokitzeko klasea
 * @version 1.0, 16/04/2021
 * @author Adrián Sánchez, Mikel Idoyaga, Ander Eiros


 */

public class Aurreprozesamendua {
	
	/**
	 * Datuak arff fitxategi bilakatzen ditu. Arff fitxategiaren path-a jarrrita eta Ham eta Spam fitxategien path-a jarrita
	 * @throws IOException
	 */
	
	public void arffFitxategia() throws IOException {
		
		FileWriter fw = new FileWriter(new File("/home/mikel/Desktop/Proiektua/mailDatuak.arff"));
		PrintWriter pw = new PrintWriter(fw);
		
		pw.println("@relation mail.data");
		pw.println("@attribute class {'ham','spam'}");
		pw.println("@attribute Text string");
		pw.println("@data");
		
		this.textToArff("/home/mikel/Desktop/Downloads/mail_spam/mail_spam/spam/", pw);
		this.textToArff("/home/mikel/Desktop/Downloads/mail_spam/mail_spam/ham/", pw);
		
		pw.close();
		
	}
	
	
	/**
	 * Karpeta bateko .txt arff fitxategi batean sartu karpetaren path-aren azken zatia klasearen izena izanda
	 * @param pathData .txt-ak dauden karpetaren path-a
	 * @param pw PrintWriter-a arff fitxategian idazketak gauzatzeko
	 * @throws IOException
	 */
	
	
	public void textToArff(String pathData, PrintWriter pw ) throws IOException {
		File folder = new File(pathData);
		String[] zatiak = pathData.split("/");
		String klasea = zatiak[zatiak.length-1];
		File[] listOfFiles = folder.listFiles();
		Scanner myReader;
		String unekoText="";
		for (File file : listOfFiles) {
	    	
		    if (file.isFile()) {
		    	myReader = new Scanner(file);
		        while (myReader.hasNextLine()) {
		          String data = myReader.nextLine();
		          data=data.replaceAll("'", " ");
		          unekoText=unekoText.concat(data);
		        }
		        pw.println("'"+klasea+"'"+",'"+unekoText+"'");
		        unekoText="";
		     
		    }
		}
	}
	/**
	 * Bektorizatu gabeko instatziak BOW erabiliz bektorizatu
	 * @param train Bektorizatu nahi diren instantzia
	 * @param path Gorde nahi den sortutato hiztegiaren path-a
	 * @return Bektorizatutako intantziak itzuliko ditu
	 * @throws Exception
	 */
	
	public Instances errepresentazioBektoriala(Instances train, String path) throws Exception {
		
		File f = new File(path);
		StringToWordVector filter = new StringToWordVector();
		filter.setDictionaryFileToSaveTo(f);
		filter.setWordsToKeep(100000);
		filter.setAttributeNamePrefix("#");

		//Defektuz 1000 proba egiteko
    	filter.setInputFormat(train);
    	Instances trainBektore=Filter.useFilter(train, filter);
    	//Atributuak gorde
    	/*FileWriter fw = new FileWriter(path);
    	for(int i=0;i<trainBektore.numAttributes();i++) {
    		fw.write(trainBektore.attribute(i).name().substring(1)+"\n");
    	}
    	System.out.println(trainBektore.numAttributes());
    	fw.close();*/
    	System.out.println(trainBektore.numAttributes());
    	return trainBektore;

	}
	/**
	 * Bektorizatu gabeko instatziak TF-IDF erabiliz bektorizatu
	 * @param train Bektorizatu nahi diren instantzia
	 * @param path Gorde nahi den sortutato hiztegiaren path-a
	 * @return bektorizatutako intantziak itzuliko ditu
	 * @throws Exception
	 */
	
	public Instances errepresentazioBektorialaTFIDF(Instances train, String path) throws Exception {
		
		File f = new File(path);
		StringToWordVector filter = new StringToWordVector();
		filter.setDictionaryFileToSaveTo(f);
		filter.setWordsToKeep(100000);
		filter.setTFTransform(true);
		filter.setIDFTransform(true);
		filter.setAttributeNamePrefix("#");

		//Defektuz 1000 proba egiteko
		filter.setInputFormat(train);
		Instances trainBektore=Filter.useFilter(train, filter);
		//Atributuak gorde
		/*FileWriter fw = new FileWriter(path);
		for(int i=0;i<trainBektore.numAttributes();i++) {
			fw.write(trainBektore.attribute(i).name().substring(1)+"\n");
		}
		System.out.println(trainBektore.numAttributes());
		fw.close();*/
		System.out.println(trainBektore.numAttributes());
		return trainBektore;

	}
	/**
	 * Atributuen hautapena klasearekiko egin eta intantziak atributu bakar horietara egokitu
	 * @param data Egokitu nahi diren instantziak
	 * @param evaluator Erabiliko den evaluator-a atributuak hautatzeko
	 * @param attKop Hautatutako atributu kopurua
	 * @return Instatziak atributu bakar horietara egokituta itzuliko du
	 * @throws Exception
	 */
	
	public Instances attributeSelection(Instances data,ASEvaluation evaluator,int attKop) throws Exception {
		
		AttributeSelection as = new AttributeSelection();
		Ranker ranker = new Ranker();
		ranker.setNumToSelect(attKop);
		as.setEvaluator(evaluator);
		as.setSearch(ranker);
		as.setInputFormat(data);
		
		Instances ins = Filter.useFilter(data, as);
		
		Instances insOrd = egokitu(ins,data);
		
		return insOrd;
	}
	/**
	 * Test-eko instantziak train multzoarekin bateratu
	 * @param train Entrenamendu multzorako erabili diren instantziak
	 * @param test Testeatzeko erabiliko diren instantziak
	 * @return Test-eko instantzia egokituak itzuliko ditu
	 * @throws Exception
	 */
	
	public Instances egokitu(Instances train, Instances test) throws Exception {
		
		ArrayList<Integer> ind = new ArrayList<Integer>();
		ArrayList<Attribute> list = Collections.list(train.enumerateAttributes());
		list.add(train.classAttribute());
		
		if(!test.equalHeaders(train)) {
			for(int i=0;i<test.numAttributes();i++) {
				if(list.contains(test.attribute(i))) {
					ind.add(i);
				}
			}
			
			int[] indArray = new int[ind.size()];
			for(int a=0;a<ind.size();a++) {
				indArray[a]=ind.get(a);
			}
			test = this.remove(test,indArray);
			
		}
		
		return test;
	}
	/**
	 * Hautatutako atributuak mantendu
	 * @param data Zein instantzietan kendu nahi diren atributuak
	 * @param aukAtt Mantendu nahi diten atributuen array-a
	 * @return Instantziak atributuak kenduta
	 * @throws Exception
	 */
	
	public Instances remove(Instances data,int[] aukAtt) throws Exception {
		
		Remove r = new Remove();
		r.setAttributeIndicesArray(aukAtt);
		r.setInvertSelection(true);
		r.setInputFormat(data);
		
		Instances ins = Filter.useFilter(data, r);
		
		return ins;
		
		
	}
	
	/**
	 * Testa egokituko du hiztegia erabilita; hau da, Test-a bektorizatuko du hiztegiaren arabera
	 * @param path Hiztegia dagoen Path-a
	 * @param test Bektorizatu nahi diren instantziak
	 * @return Intantziak bektorizatuta itzuliko ditu
	 * @throws Exception
	 */
	
	public Instances testaEgokitu(String path,Instances test) throws Exception {//**HIZTEGI EGOKIA SARTU**
		
		FixedDictionaryStringToWordVector filter = new FixedDictionaryStringToWordVector();
		filter.setDictionaryFile(new File(path));
		filter.setAttributeNamePrefix("#");
    	filter.setInputFormat(test);
    	Instances testEgokituta=Filter.useFilter(test, filter);
    	
    	return testEgokituta;
	}
	
	/**
	 * Intantziak randomizatu eta bi multzotan banatuko ditu bat %70 instatziekin eta bestea %30-arekin
	 * @param data Ramdomizatu eta banadu nahi diren instantziak
	 * @param rand Ramdomizatzeko erabiliko den hazia
	 * @return Array bat itzuliko non arrayeko lehenengo posiziona %70-eko izango duen eta bigarrenean %30-ekoa
	 * @throws Exception
	 */
	
	public Instances[] randomSplit(Instances data,int rand) throws Exception {
		data.randomize(new Random(rand));
		RemovePercentage filter = new RemovePercentage();
		filter.setInputFormat(data);
		filter.setInvertSelection(true);
		filter.setPercentage(70);
		Instances train = Filter.useFilter(data, filter);
		filter.setInputFormat(data);
		filter.setInvertSelection(false);
		Instances test = Filter.useFilter(data, filter);
		Instances [] emaitza= {train,test};
		return emaitza;
	}
}
