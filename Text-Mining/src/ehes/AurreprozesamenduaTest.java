package ehes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import analisia.Aurreprozesamendua;
import analisia.Sailkatzailea;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class AurreprozesamenduaTest {
	
	
	
	public Instances testaEgokitu(String pTest) throws Exception {
		
		FileWriter fw = new FileWriter(new File("src/ehes/aurreProz/testa.arff"));
		PrintWriter pw = new PrintWriter(fw);
		
		pw.println("@relation mail.data");
		pw.println("@attribute class {'ham','spam'}");
		pw.println("@attribute Text string");
		pw.println("@data");
		
        pw.println("?"+",'"+pTest+"'");
		
		pw.close();
		
		return this.bukEgokitu("src/ehes/aurreProz/testa.arff");
	}
	
	private Instances bukEgokitu(String pPath) throws Exception {
		//igual publiko egin behar instantziakKargatu metodoa
		
		Aurreprozesamendua pro = new Aurreprozesamendua();
		
		DataSource source = new DataSource(pPath);
        Instances test = source.getDataSet();
        test.setClassIndex(0);
        
		DataSource source2 = new DataSource("src/ehes/aurreProz/header.arff");
        Instances header = source2.getDataSet();
        header.setClassIndex(0);
        
        Instances dicTest = pro.testaEgokitu("src/ehes/aurreProz/dictionary.txt", test);
        Instances egoTest = pro.egokitu(header, dicTest);
        
        return egoTest;
        
        
	}

}
