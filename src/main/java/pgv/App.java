package pgv;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pgv.game.controller.GameController;

public class App extends Application {
	
	GameController controller;

	@Override
	public void start(Stage primaryStage) throws Exception {
		controller = new GameController();

		Scene scene = new Scene(controller.getView());

		primaryStage.setTitle("PingPong");
		//primaryStage.setFullScreen(true);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		controller.setScene(scene);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static String getRuta() {
		return ruta;
	}
	
	public static String getImagenExtension() {
		return imagenExtension;
	}
	
}
