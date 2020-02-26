package pgv.servidor;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import pgv.models.Coordenada;
import pgv.models.Coordenadas;
import pgv.models.Jugador;
import pgv.models.Puntuacion;

public class MainServidor {

	private static ServerSocket serverSocket = null;
	private static ArrayList<JugadorEnvia> jugadores = new ArrayList<JugadorEnvia>();

	private static String ip = "";
	private static int puerto = 5555;
	private static InetSocketAddress addr = null;
	
	private static double[][] coordenadas = {{175.0, 100.0}, {175.0, 1085.0}};
	
	private static int velocidad = 30;
	private static Coordenada bola = new Coordenada(235.0, 585.0);
	private static boolean continuar = true, pausa = false;
	private static Puntuacion puntuacion = new Puntuacion();
	
	public static void main(String[] args) throws IOException {
		
		Scanner sc = new Scanner(System.in);
		System.out.println("SERVIDOR PING PONG\n");
		serverSocket = new ServerSocket();
		System.out.println("Introduce la ip del servidor:");
		//ip = sc.nextLine();
		ip = "192.168.0.24";
		addr = new InetSocketAddress(ip, puerto);
		serverSocket.bind(addr);
		System.out.println("Esperando conexiones...");
		
		while (jugadores.size() < 2) {
			Socket socket = serverSocket.accept();
			JugadorEnvia jugador = new JugadorEnvia(socket);
			jugador.setJugadorCoordenadas(coordenadas[jugadores.size()][0], coordenadas[jugadores.size()][1]);
			jugadores.add(jugador);
			jugadores.get(jugadores.size() - 1).setId(jugadores.size() - 1);
		}
		jugadores.get(0).setRival(jugadores.get(1).getJugador().getNick());
		jugadores.get(1).setRival(jugadores.get(0).getJugador().getNick());
		jugadores.get(0).start();
		jugadores.get(1).start();
		sc.close();
		System.out.println("Preparando partida...");
		
		// 3 2 1
		bola.setRandomDireccionBola();
		try {
			Thread.sleep(1000);
			System.out.println("La partida empezará en: 3");
			Thread.sleep(1500);
			System.out.println("La partida empezará en: 2");
			Thread.sleep(1500);
			System.out.println("La partida empezará en: 1");
			Thread.sleep(2500);
			System.out.println("Partida iniciada");
		} catch (InterruptedException e) { }
		while (continuar) {
			try {
				Thread.sleep(velocidad);
				if (!pausa) {
					bola.moverBola(jugadores.get(0).getJugador().getCoordenadas().getX(), jugadores.get(1).getJugador().getCoordenadas().getX());
				}
			} catch (InterruptedException e) {
				System.out.println("Error Thread.sleep()");
				continuar = false;
			}
		}
	}
	
	public static int getVelocidad() {
		return velocidad;
	}
	
	public static Coordenada getBola() {
		return bola;
	}
	
	public static Puntuacion getPuntuacion() {
		return puntuacion;
	}
	
	public static Coordenada getRival(int jugador) {
		Coordenada rival;
		if (jugador == 0) {
			rival = jugadores.get(1).getJugador().getCoordenadas();
		} else {
			rival = jugadores.get(0).getJugador().getCoordenadas();
		}
		return rival;
	}
	
	public static boolean continuar() {
		return continuar;
	}
	
	public static boolean pausa() {
		return pausa;
	}
	
	public static void enviarPuntuaciones(int jugador) {
		pausa = true;
		System.out.println("Punto para " + jugadores.get(jugador).getJugador().getNick());
		jugadores.get(0).enviarPuntuacion(puntuacion);
		jugadores.get(1).enviarPuntuacion(puntuacion);
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) { }
		pausa = false;
	}

}

class JugadorEnvia extends Thread {
	
    private Socket socket;
    private ObjectOutputStream oos;
    private String rival;
    private JugadorEscucha escucha;
    private Coordenadas coordenadas;
    private int id;
    private boolean pausaHilo = false;
    
    public JugadorEnvia(Socket socket) throws IOException {
        this.socket = socket;
        oos = new ObjectOutputStream(socket.getOutputStream());
		escucha = new JugadorEscucha(socket);
		escucha.start();
    }
    
    public void setId(int id) {
    	this.id = id;
    }
    
    public void setJugadorCoordenadas(double x, double y) {
		escucha.setJugadorCoordenadas(x, y);
    }
    
    public void setRival(String rival) {
    	this.rival = rival;
    	if (id == 0) {
    		coordenadas = new Coordenadas(escucha.getJugador().getCoordenadas(), MainServidor.getRival(id), MainServidor.getBola());
    	} else {
    		coordenadas = new Coordenadas(MainServidor.getRival(id), escucha.getJugador().getCoordenadas(), MainServidor.getBola());
    	}
    }
    
    public Jugador getJugador() {
    	return escucha.getJugador();
    }
    
    public void enviarPuntuacion(Puntuacion p) {
		try {
			pausaHilo = true;
			Thread.sleep(MainServidor.getVelocidad());
			oos.writeObject(new Puntuacion(p));
			Thread.sleep(MainServidor.getVelocidad());
			pausaHilo = false;
		} catch (Exception e) { }
    }
    
    @Override
    public void run() {
        try {
			oos.writeObject(rival);
			oos.writeObject(id);
			Thread.sleep(4000);
			while (MainServidor.continuar()) {
				try {
					Thread.sleep(MainServidor.getVelocidad());
					if (!pausaHilo) {
						coordenadas.setBola(MainServidor.getBola());
						Coordenadas coordenadasParaEnviar = new Coordenadas(coordenadas);
						oos.reset();
						oos.writeUnshared(coordenadasParaEnviar);
						oos.flush();
					}
				} catch (Exception e) {
					System.out.println("Error al enviar un objeto Coordenadas");
					desconnectar();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void desconnectar() throws IOException {
        socket.close();
        System.exit(0);
    }
}

class JugadorEscucha extends Thread {
	
    private Socket socket;
    private DataInputStream dis;
    private Jugador jugador;
    private boolean continuar = true;
    
    public JugadorEscucha(Socket socket) throws IOException {
        this.socket = socket;
        dis = new DataInputStream(socket.getInputStream());
		String nick = dis.readUTF();
		jugador = new Jugador(nick, new Coordenada(0.0, 0.0));
    }
    
    public void setJugadorCoordenadas(double x, double y) {
    	jugador.setCoordenadas(new Coordenada(x, y));
    }
    
    public Jugador getJugador() {
    	return jugador;
    }
    
    @Override
    public void run() {
    	System.out.println("Se ha conectado " + jugador.getNick());
        try {
			while (continuar) {
				String key = dis.readUTF();
				switch (key) {
					case "UP":
						jugador.getCoordenadas().moverJugador(-1);
						break;
					case "DOWN":
						jugador.getCoordenadas().moverJugador(1);
						break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void desconnectar() throws IOException {
        socket.close();
        System.exit(0);
    }
}
