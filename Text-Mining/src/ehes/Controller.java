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


/**
 * Interfazea kontrolatzeko klasea
 * @version 1.0, 16/04/2021
 * @author Adrián Sánchez, Mikel Idoyaga, Ander Eiros


 */

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
    /**
     * Idatzitako textua analizatu ea Spam edo Ham den
     * @param event analyze botoia sakatu
     * @throws Exception
     */
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
    /**
     * Aplikazioa itxi
     * @param event gurutzea sakatu
     */
    void onClickClose(MouseEvent event) {
    	System.exit(0);
    }
    /**
     * Idatzitako textu guztiak ezabatu
     * @param event zakarrontzian sakatu
     */
    @FXML
    void onClickDelete(MouseEvent event) {
    	txtDetector.setText("");
    	txtSpam.setText("");
    	txtHam.setText("");
    	txtResult.setText("");
    }
    /**
     * modeloa erabili testua ea Spam edo Ham den iragartzeko
     * @throws Exception 
     */
    
    private void iragarpenaEgin() throws Exception {
    	
    	cls = (Classifier) weka.core.SerializationHelper.read("src/ehes/resources/spam.model");
         
        double pred = cls.classifyInstance(test.instance(0));
        double[] predictionDistribution = cls.distributionForInstance(test.instance(0));
         
        txtResult.setText(test.classAttribute().value((int) pred).toUpperCase());
        
        String hamD = String.format("%.2f",predictionDistribution[0]*100);
        String spamD = String.format("%.2f",predictionDistribution[1]*100);
         
        txtHam.setText("%"+hamD);
        txtSpam.setText("%"+spamD);

         
    }
    /**
     * Interfazea abiarazi
     * @param arg0
     * @param arg1
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		txtDetector.setPromptText("Paste here the text.");
	}

}
