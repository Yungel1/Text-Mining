package ehes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import analisia.Aurreprozesamendua;
import analisia.Sailkatzailea;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector;
import weka.filters.unsupervised.attribute.Remove;

/**
 * Datuak behar ditugun formatuan egokitzeko klasea
 * @version 1.0, 16/04/2021
 * @author Adrián Sánchez, Mikel Idoyaga, Ander Eiros


 */

public class AurreprozesamenduaTest {
	
	/**
	 * Testu bat egokituko arff fitxategi bat eginez
	 * @return Arff fitxategiko intantziak itzuliko ditu
	 * @throws Exception
	 */
	
	public Instances testaEgokitu() throws Exception {
		
		Scanner myReader;
		String pTest="";
    	
    	File f = new File("src/ehes/resources/proba.txt");
    	
		FileWriter fw = new FileWriter(new File("src/ehes/resources/testa.arff"));
		PrintWriter pw = new PrintWriter(fw);
		
    	myReader = new Scanner(f);
        while (myReader.hasNextLine()) {
          String data = myReader.nextLine();
          data=data.replaceAll("'", " ");
          pTest=pTest.concat(data);
        }
		
		pw.println("@relation mail.data");
		pw.println("@attribute class {'ham','spam'}");
		pw.println("@attribute Text string");
		pw.println("@data");
		
        pw.println("?"+",'"+pTest+"'");
		
		pw.close();
		
		return this.bukEgokitu("src/ehes/resources/testa.arff");
	}
	/**
	 * Arff fitxategia egokituko du header eta hiztegi bat erabiliz
	 * @param pPath Arff fitxategiaren path
	 * @return Intantziak egokituta itzuliko ditu
	 * @throws Exception
	 */
	
	private Instances bukEgokitu(String pPath) throws Exception {
		
		DataSource source = new DataSource(pPath);
        Instances test = source.getDataSet();
        test.setClassIndex(0);
        
		DataSource source2 = new DataSource("src/ehes/resources/header.arff");
        Instances header = source2.getDataSet();
        header.setClassIndex(0);
        
        Instances dicTest = this.testaEgokitu("src/ehes/resources/dictionary.txt", test);
        Instances egoTest = this.egokitu(header, dicTest);
        
        return egoTest;
        
        
	}
	/**
	 * Instantziak egokituko ditu hiztegi jakin baterako
	 * @param path Hiztegiaren direktorioa
	 * @param test Egokitu nahi diren instantziak
	 * @return Instantziak egokituta hiztegiarekiko
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
	 * Test instantziak egokitu beste multzo jakin baterako
	 * @param train Zetzuk insntantziekin egokitu nahi den
	 * @param test Egokitu nahi diren instantziak
	 * @return Testeko instantziak beste multzoarekiko egokituta itzuliko du
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

}
