package ehes;
import java.io.FileWriter;
import java.net.URL;
import java.util.ResourceBundle;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Controller implements Initializable{

    @FXML
    private TextArea txtDetector;
    
    @FXML
    private TextField txtSpam;

    @FXML
    private TextField txtHam;

    @FXML
    private Button btnANL;

    @FXML
    private TextField txtResult;

    @FXML
    private FontAwesomeIconView iconExit;
    
    @FXML
    private FontAwesomeIconView iconDelete;

    private Instances test;
    
    
    private Classifier cls;
    
    @FXML
    void onClick(ActionEvent event) throws Exception {
    	
    	AurreprozesamenduaTest ap = new AurreprozesamenduaTest();
    	
    	String msg = txtDetector.getText().replaceAll("\n", System.getProperty("line.separator")); //txtDetector --> String
    	FileWriter fw = new FileWriter("src/ehes/resources/proba.txt");
    	fw.write(msg);
    	fw.close();
    	
    	test = ap.testaEgokitu();//Aurreprozesamendua.java egin eta hau erabiliz string --> .arff bihurtu eta FixedDictionary aplikatu
    	iragarpenaEgin();
    	
    }

    @FXML
    void onClickClose(MouseEvent event) {
    	System.exit(0);
    }
    
    @FXML
    void onClickDelete(MouseEvent event) {
    	txtDetector.setText("");
    	txtSpam.setText("");
    	txtHam.setText("");
    	txtResult.setText("");
    }
    
    private void iragarpenaEgin() throws Exception {
    	
    	 cls = (Classifier) weka.core.SerializationHelper.read("src/ehes/resources/spam.model");
         
         double pred = cls.classifyInstance(test.instance(0));
         txtResult.setText(test.classAttribute().value((int) pred).toUpperCase());
         
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		txtDetector.setPromptText("Paste here the text.");
	}

}
