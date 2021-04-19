package ehes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Interfazea eta controller-a abiarazteko main klasea
 * @version 1.0, 16/04/2021
 * @author Adrián Sánchez, Mikel Idoyaga, Ander Eiros


 */
public class Main extends Application{

	Parent controllerUI;
	Stage stage;
	Controller controller;
	
	@Override
	/**
	 * Leihoa bistaratu
	 * @param primaryStage Bistaratu nahi den leihoa
	 * @throws Exception
	 */
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
	/**
	 * Interfazea eta controller-a abiarazi
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		launch(args);
	}

}
