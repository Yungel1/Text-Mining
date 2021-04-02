package analisia;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class Aurreprozesamendua {
	
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
		//FileWriter fw = new FileWriter(new File(pPath));
		//PrintWriter pw = new PrintWriter(fw);
	}
	
	public void errepresentazioBektoriala(Instances train, String path) throws Exception {
		
		StringToWordVector filter = new StringToWordVector();
		filter.setWordsToKeep(100000);
		filter.setAttributeNamePrefix("#");
		//Defektuz 1000 proba egiteko
    	filter.setInputFormat(train);
    	Instances trainBektore=Filter.useFilter(train, filter);
    	
    	//Atributuak gorde
    	FileWriter fw = new FileWriter(path);
    	for(int i=1;i<trainBektore.numAttributes();i++) {
    		fw.write(trainBektore.attribute(i).name().substring(1)+"\n");
    	}
    	fw.close();

	}
	
	public Instances testaEgokitu(String path,Instances test) throws Exception {//**HIZTEGI EGOKIA SARTU**
		
		FixedDictionaryStringToWordVector filter = new FixedDictionaryStringToWordVector();
		filter.setDictionaryFile(new File(path));
		filter.setAttributeNamePrefix("#");
    	filter.setInputFormat(test);
    	Instances testEgokituta=Filter.useFilter(test, filter);
    	
    	return testEgokituta;
	}
	
}
