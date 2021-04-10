package ehes;
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
    private Button btnANL;

    @FXML
    private TextField txtResult;

    @FXML
    private FontAwesomeIconView iconExit;

    private Instances test;
    
    
    private Classifier cls;
    
    @FXML
    void onClick(ActionEvent event) throws Exception {

    	AurreprozesamenduaTest ap = new AurreprozesamenduaTest();
    	
    	String msg = txtDetector.getText(); //txtDetector --> String
    	test = ap.testaEgokitu(msg);//Aurreprozesamendua.java egin eta hau erabiliz string --> .arff bihurtu eta FixedDictionary aplikatu
    	iragarpenaEgin();
    }

    @FXML
    void onClickClose(MouseEvent event) {
    	System.exit(0);
    }
    
    private void iragarpenaEgin() throws Exception {
    	
    	 cls = (Classifier) weka.core.SerializationHelper.read("resources/spam.model");
         
         double pred = cls.classifyInstance(test.instance(0));
         txtResult.setText(test.classAttribute().value((int) pred).toUpperCase());
         
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		txtDetector.setPromptText("Paste here the text.");
	}

}
