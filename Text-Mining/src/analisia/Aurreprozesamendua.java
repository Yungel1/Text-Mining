package analisia;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

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
	
	public static void main(String[] args) throws IOException {
		
		Aurreprozesamendua pro = new Aurreprozesamendua();
		pro.arffFitxategia();
		
		
	}
	
}
