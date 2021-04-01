package ehes;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Main {

	public static void main(String[] args) throws Exception {
		
		DataSource source = new DataSource(args[0]);
        Instances data = source.getDataSet();
        data.setClassIndex(data.numAttributes() - 1);

	}

}
