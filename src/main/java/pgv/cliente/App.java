package pgv.cliente;

import javafx.application.Application;
import javafx.stage.Stage;
import pgv.cliente.controller.GameController;

public class App extends Application {
	
	GameController controller;

	@Override
	public void start(Stage primaryStage) throws Exception {
		controller = new GameController();

		primaryStage.setTitle("PingPong");
		primaryStage.setScene(controller.getScene());
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
