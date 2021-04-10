package ehes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import analisia.Aurreprozesamendua;
import analisia.Sailkatzailea;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector;
import weka.filters.unsupervised.attribute.Remove;

public class AurreprozesamenduaTest {
	
	
	
	public Instances testaEgokitu(String pTest) throws Exception {
		
		FileWriter fw = new FileWriter(new File("src/ehes/testa.arff"));
		PrintWriter pw = new PrintWriter(fw);
		
		pw.println("@relation mail.data");
		pw.println("@attribute class {'ham','spam'}");
		pw.println("@attribute Text string");
		pw.println("@data");
		
        pw.println("?"+",'"+pTest+"'");
		
		pw.close();
		
		return this.bukEgokitu("src/ehes/testa.arff");
	}
	
	private Instances bukEgokitu(String pPath) throws Exception {
		
		DataSource source = new DataSource(pPath);
        Instances test = source.getDataSet();
        test.setClassIndex(0);
        
		DataSource source2 = new DataSource("src/ehes/header.arff");
        Instances header = source2.getDataSet();
        header.setClassIndex(0);
        
        Instances dicTest = this.testaEgokitu("src/ehes/dictionary.txt", test);
        Instances egoTest = this.egokitu(header, dicTest);
        
        return egoTest;
        
        
	}
	
	public Instances testaEgokitu(String path,Instances test) throws Exception {//**HIZTEGI EGOKIA SARTU**
		
		FixedDictionaryStringToWordVector filter = new FixedDictionaryStringToWordVector();
		filter.setDictionaryFile(new File(path));
		filter.setAttributeNamePrefix("#");
    	filter.setInputFormat(test);
    	Instances testEgokituta=Filter.useFilter(test, filter);
    	
    	return testEgokituta;
	}
	
	
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
	
	public Instances remove(Instances data,int[] aukAtt) throws Exception {
		
		Remove r = new Remove();
		r.setAttributeIndicesArray(aukAtt);
		r.setInvertSelection(true);
		r.setInputFormat(data);
		
		Instances ins = Filter.useFilter(data, r);
		
		return ins;
		
		
	}

}
