package pgv.cliente;

import javafx.application.Application;
import javafx.stage.Stage;
import pgv.cliente.controller.GameController;

public class App extends Application {
	
	private static GameController controller;

	@Override
	public void start(Stage primaryStage) throws Exception {
		controller = new GameController();

		primaryStage.setTitle("PingPong");
		primaryStage.setScene(controller.getScene());
		primaryStage.setResizable(false);
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static GameController getController() {
		return controller;
	}
}
