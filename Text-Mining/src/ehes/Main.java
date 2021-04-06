package ehes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application{

	Parent controllerUI;
	Stage stage;
	Controller controller;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			
			primaryStage.initStyle(StageStyle.UNDECORATED);
			stage = primaryStage;
			FXMLLoader loaderController = new FXMLLoader(getClass().getResource("resources/spam.fxml"));
			controllerUI = (Parent)loaderController.load();        
			stage.setScene(new Scene(controllerUI));
			stage.show();
	    
			
		} catch(Exception e) {
			
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		
		launch(args);
	}

}
