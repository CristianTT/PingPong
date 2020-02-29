package pgv.cliente.controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import pgv.cliente.App;
import pgv.models.Coordenadas;
import pgv.models.Puntuacion;

public class GameController implements Initializable {

	@FXML
	private AnchorPane root;

	private Scene scene;

	private ObjectInputStream entrada = null;
	private DataOutputStream salida = null;
	private static Socket clientSocket = null;
	private String ip = "";
	private String nickJugador = "", nickRival = "";
	private int puerto = 5555;
	private InetSocketAddress addr = null;

	private Rectangle jugador0Rectangle, jugador1Rectangle;
	private Circle bolaCircle;
	
	@FXML
	private Label jugador0Label, jugador1Label, puntuacionLabel, numeroInicio;

	public GameController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("PING PONG\n");
		
		scene = new Scene(root);

		clientSocket = new Socket();
		Scanner sc = new Scanner(System.in);
		System.out.println("Introduce la ip del servidor:");
		//ip = sc.nextLine();
		ip = "192.168.0.24";
		puerto = 5555;
		System.out.println("Introduce un nick:");
		//nickJugador = sc.nextLine();
		nickJugador = "Jugador" + (int) Math.floor(Math.random() * 99);

		jugador0Rectangle = new Rectangle();
		jugador0Rectangle.setFill(Color.rgb(0, 162, 232));
		jugador0Rectangle.setWidth(15);
		jugador0Rectangle.setHeight(150);
		jugador1Rectangle = new Rectangle();
		jugador1Rectangle.setFill(Color.rgb(0, 162, 232));
		jugador1Rectangle.setWidth(15);
		jugador1Rectangle.setHeight(150);
		bolaCircle = new Circle();
		bolaCircle.setFill(Color.rgb(255, 100, 100));
		bolaCircle.setRadius(15);
		bolaCircle.setVisible(false);
		root.getChildren().addAll(jugador0Rectangle, jugador1Rectangle, bolaCircle);
		moverObjeto(jugador0Rectangle, 175.0, 100.0);
		moverObjeto(jugador1Rectangle, 175.0, 1085.0);
		moverObjeto(bolaCircle, 235.0, 585.0);

		addr = new InetSocketAddress(ip, puerto);
		try {
			clientSocket.connect(addr);
			salida = new DataOutputStream(clientSocket.getOutputStream());
			entrada = new ObjectInputStream(clientSocket.getInputStream());
			salida.writeUTF(nickJugador);
			System.out.println("Esperando rival...");
			nickRival = (String) entrada.readObject();
			int id = (int) entrada.readObject();
			if (id == 0) {
				jugador0Label.setText(nickJugador);
				jugador1Label.setText(nickRival);
				System.out.println("ID: 0");
			} else {
				jugador1Label.setText(nickJugador);
				jugador0Label.setText(nickRival);
				System.out.println("ID: 1");
			}
			System.out.println("Jugarás contra " + nickRival);

			EscucharCli escucha = new EscucharCli(entrada, id);
			escucha.start();

			// addEventHandler
			scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent e) {
					String code = e.getCode().toString();
					try {
						pulsarTecla(code);
					} catch (IOException error) {
						error.printStackTrace();
					}
				}
			});
		} catch (IOException | ClassNotFoundException error) {
			error.printStackTrace();
			System.exit(0);
		}
		sc.close();
	}

	public Scene getScene() {
		return scene;
	}
	
	public void iniciarContador(int num) {
		if (num != 0) {
			numeroInicio.setText("" + num);
			FadeTransition transicionFade = new FadeTransition();
			transicionFade.setAutoReverse(true);
			transicionFade.setCycleCount(1);
			transicionFade.setDuration(Duration.millis(1500));
			transicionFade.setToValue(0.0);
			transicionFade.setFromValue(1.0);
			transicionFade.setNode(numeroInicio);
			transicionFade.setInterpolator(Interpolator.LINEAR);
			transicionFade.play();
			ScaleTransition transicionScale = new ScaleTransition();
			transicionScale.setAutoReverse(true);
			transicionScale.setCycleCount(1);
			transicionScale.setDuration(Duration.millis(1500));
			transicionScale.setFromX(1);
			transicionScale.setToX(3);
			transicionScale.setFromY(1);
			transicionScale.setToY(3);
			transicionScale.setNode(numeroInicio);
			transicionScale.setInterpolator(Interpolator.EASE_BOTH);
			transicionScale.play();
		} else {
			numeroInicio.setText("");
			bolaCircle.setVisible(true);
		}
	}

	public void pulsarTecla(String key) throws IOException {
		salida.writeUTF(key);
	}
	
	public void moverObjeto(Node node, double top, double left) {
		AnchorPane.setTopAnchor(node, top);
		AnchorPane.setLeftAnchor(node, left);
	}

	public Rectangle getJugador0Rectangle() {
		return jugador0Rectangle;
	}

	public Rectangle getJugador1Rectangle() {
		return jugador1Rectangle;
	}

	public Circle getBolaCircle() {
		return bolaCircle;
	}

	public void setPuntuacionLabel(String text) {
		puntuacionLabel.setText(text);
	}
	
	public Socket getSocket() {
		return clientSocket;
	}
}

class EscucharCli extends Thread {

	ObjectInputStream entrada = null;
	int id;
	GameController gameController;
	boolean continuar = true;
	int inicio;

	public EscucharCli(ObjectInputStream entrada, int id) throws IOException {
		this.entrada = entrada;
		this.id = id;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) { }
		System.out.println("Preparando el juego...");
		inicio = 3;
		while (inicio >= 1) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					System.out.println("El juego iniciará en: " + inicio);
					gameController = App.getController();
					gameController.iniciarContador(inicio);
					inicio--;
				}
			});
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) { }
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				gameController = App.getController();
				gameController.iniciarContador(0);
			}
		});
		System.out.println("Juego iniciado");
		do {
			try {
				Object obj = (Object) entrada.readUnshared();
				if (obj instanceof Coordenadas) { // Posiciones de los objetos en el mapa
					gameController = App.getController();
					//System.out.println(((Coordenadas) obj));
					gameController.moverObjeto(
							gameController.getJugador0Rectangle(), 
							((Coordenadas) obj).getJugador0().getX(), 
							((Coordenadas) obj).getJugador0().getY());
					gameController.moverObjeto(
							gameController.getJugador1Rectangle(), 
							((Coordenadas) obj).getJugador1().getX(), 
							((Coordenadas) obj).getJugador1().getY());
					gameController.moverObjeto(gameController.getBolaCircle(), 
							((Coordenadas) obj).getBola().getX(), 
							((Coordenadas) obj).getBola().getY());
				} else if (obj instanceof Puntuacion) { // Cuando se marca un punto
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							gameController.setPuntuacionLabel(((Puntuacion) obj).toString());
						}
					});
				} else if (obj instanceof String) { // victoria / derrota 
					System.out.println("Victoria/Derrota: " + obj);
				} else {
					System.out.println("Hey: " + obj.toString());
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				continuar = false;
			}
		} while (continuar);
		try {
			entrada.close();
		} catch (IOException e) { }
	}

}
