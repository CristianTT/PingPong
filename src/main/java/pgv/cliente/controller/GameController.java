package pgv.cliente.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

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
import pgv.models.Coordenada;
import pgv.models.Coordenadas;
import pgv.models.Puntuacion;

public class GameController implements Initializable {

	@FXML
	AnchorPane root;

	private Scene scene;

	private DataInputStream entrada = null;
	private DataOutputStream salida = null;
	private Socket clientSocket = null;
	private String ip = "";
	private String nickJugador = "", nickRival = "";
	private boolean continuar = true;
	private int puerto = 5555;
	InetSocketAddress addr = null;

	private static Rectangle jugadorRectangle, rivalRectangle;
	private static Circle bolaCircle;
	
	@FXML
	private static Label jugadorLabel, rivalLabel, puntuacionLabel;

	public GameController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		scene = new Scene(root);

		clientSocket = new Socket();

		Scanner sc = new Scanner(System.in);
		System.out.println("Introduce la ip del servidor:");
		ip = sc.nextLine();
		puerto = 5555;
		System.out.println("Introduce un nick:");
		nickJugador = sc.nextLine();

		jugadorRectangle = new Rectangle();
		jugadorRectangle.setFill(Color.rgb(0, 162, 232));
		jugadorRectangle.setWidth(15);
		jugadorRectangle.setHeight(150);
		rivalRectangle = new Rectangle();
		rivalRectangle.setFill(Color.rgb(0, 162, 232));
		rivalRectangle.setWidth(15);
		rivalRectangle.setHeight(150);
		bolaCircle = new Circle();
		bolaCircle.setFill(Color.rgb(255, 100, 100));
		bolaCircle.setRadius(15);
		root.getChildren().addAll(jugadorRectangle, rivalRectangle, bolaCircle);
		moverObjeto(jugadorRectangle, 175.0, 100.0);
		moverObjeto(rivalRectangle, 175.0, 685.0);
		moverObjeto(bolaCircle, 235.0, 385.0);

		addr = new InetSocketAddress(ip, puerto);
		try {
			clientSocket.connect(addr);
			entrada = new DataInputStream(clientSocket.getInputStream());
			salida = new DataOutputStream(clientSocket.getOutputStream());

			jugadorLabel.setText(nickJugador);
			salida.writeUTF(nickJugador);
			nickRival = entrada.readUTF();
			rivalLabel.setText(nickRival);

			EscucharCli escucha = new EscucharCli(clientSocket);
			escucha.start();

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
		} catch (IOException error) {
			error.printStackTrace();
		}
		sc.close();
	}

	public Scene getScene() {
		return scene;
	}

	public void pulsarTecla(String key) throws IOException {
		while (continuar) {
			salida.writeUTF(key);
		}
	}
	
	public static void moverObjeto(Node node, double top, double left) {
		AnchorPane.setTopAnchor(node, top);
		AnchorPane.setLeftAnchor(node, left);
	}

	public static Rectangle getJugadorRectangle() {
		return jugadorRectangle;
	}

	public static Rectangle getRivalRectangle() {
		return rivalRectangle;
	}

	public static Circle getBolaCircle() {
		return bolaCircle;
	}

	public static Label getPuntuacionLabel() {
		return puntuacionLabel;
	}
}

class EscucharCli extends Thread {

	ObjectInputStream entrada = null;

	public EscucharCli(Socket clientSocket) throws IOException {
		entrada = new ObjectInputStream(clientSocket.getInputStream());
	}

	@Override
	public void run() {
		try {
			do {
				Object obj = (Object) entrada.readUTF();
				if (obj instanceof Coordenadas) { // Posiciones de los objetos en el mapa
					GameController.moverObjeto(GameController.getJugadorRectangle(), ((Coordenadas) obj).getJugador().getX(), ((Coordenadas) obj).getJugador().getY());
					GameController.moverObjeto(GameController.getRivalRectangle(), ((Coordenadas) obj).getRival().getX(), ((Coordenadas) obj).getRival().getY());
					GameController.moverObjeto(GameController.getBolaCircle(), ((Coordenadas) obj).getBola().getX(), ((Coordenadas) obj).getBola().getY());
				} else if (obj instanceof Puntuacion) { // Cuando se marca un punto
					GameController.getPuntuacionLabel().setText(((Puntuacion) obj).getJugador() + " | " + ((Puntuacion) obj).getRival());
				} else if (obj instanceof String) { // victoria / derrota 
					System.out.println(obj);
				}
			} while (true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

// nick otro jugador x
// puntuaciones + cuando ganas puntos
// coordenadas ambos jugadores
// coordenadas bola
// partida terminada / ganador
