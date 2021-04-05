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

public class Controller implements Initializable{

    @FXML
    private TextArea txtDetector;

    @FXML
    private Button btnANL;

    @FXML
    private TextField txtResult;

    @FXML
    private TextField txtSpam;

    @FXML
    private TextField txtNoSpam;

    @FXML
    private FontAwesomeIconView iconExit;

    @FXML
    void onClick(ActionEvent event) {

    }

    @FXML
    void onClickClose(MouseEvent event) {
    	System.exit(0);
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}
