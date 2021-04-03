package analisia;

import java.io.IOException;

import weka.attributeSelection.ClassifierAttributeEval;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Analisia {
	
	private Instances instantziakKargatu(String dataPath) throws Exception {
		
		DataSource source = new DataSource(dataPath);
        Instances data = source.getDataSet();
        data.setClassIndex(data.numAttributes() - 1);
        return data;
	}

	public static void main(String[] args) throws Exception {
		
		/*if (args.length != 3) {
            System.out.println("\ntrain .arff-a sartu, test .arff sartu, train bektorizatuta gordetzeko path-a\n");
            System.exit(1);
        }*/
		Analisia analisia = new Analisia();
		
		Instances train = analisia.instantziakKargatu("/home/adrian/EHES/Proiektua/ReutersCorn-train.arff");
		Instances test = analisia.instantziakKargatu("/home/adrian/EHES/Proiektua/ReutersCorn-test.arff");
		
		Aurreprozesamendua pro = new Aurreprozesamendua();
		//MIKEL
		pro.arffFitxategia();
		//ADRIÁN
		Instances trainBektore=pro.errepresentazioBektoriala(train, "/home/adrian/EHES/Proiektua/ReutersCorn-trainBektore.arff");
		System.out.println(trainBektore.numAttributes()+"##"+trainBektore.classIndex()+"##"+trainBektore.classAttribute().name());
		Instances trainASBektore = pro.attributeSelection(trainBektore, new ClassifierAttributeEval(), 4000);
		System.out.println(trainASBektore.numAttributes()+"##"+trainASBektore.classIndex()+"##"+trainASBektore.classAttribute().name());
		Instances testEgokituta = pro.testaEgokitu("/home/adrian/EHES/Proiektua/ReutersCorn-trainBektore.arff", test);
		System.out.println(testEgokituta.numAttributes());
		
	}

}
