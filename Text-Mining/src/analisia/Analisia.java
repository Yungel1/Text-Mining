package analisia;

import java.io.IOException;

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
		pro.errepresentazioBektoriala(train, "/home/adrian/EHES/Proiektua/ReutersCorn-trainBektore.arff");
		Instances testEgokituta = pro.testaEgokitu("/home/adrian/EHES/Proiektua/ReutersCorn-trainBektore.arff", test);
		System.out.println(testEgokituta.numAttributes());
		
	}

}
