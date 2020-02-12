package pgv.game.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

public class GameController implements Initializable {

	public GameController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	public VBox getView() {
		return root;
	}

}
