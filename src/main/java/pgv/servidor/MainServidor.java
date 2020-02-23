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

public class MainServidor {

	private static ServerSocket serverSocket = null;
	private static ArrayList<JugadorEnvia> jugadores = new ArrayList<JugadorEnvia>();

	private static String ip = "";
	private static int puerto = 5555;
	private static InetSocketAddress addr = null;
	
	private static double[][] coordenadas = {{175.0, 100.0}, {175.0, 685.0}};
	
	private static int velocidad = 2000;
	private static Coordenada bola = new Coordenada(235.0, 385.0);
	private static boolean continuar = true;
	
	public static void main(String[] args) throws IOException {
		
		Scanner sc = new Scanner(System.in);
		System.out.println("SERVIDOR PING PONG\n");
		serverSocket = new ServerSocket();
		System.out.println("Introduce la ip del servidor:");
		//ip = sc.nextLine();
		ip = "192.168.0.18";
		addr = new InetSocketAddress(ip, puerto);
		serverSocket.bind(addr);
		System.out.println("Esperando conexiones...");
		
		while (jugadores.size() < 2) {
			Socket socket = serverSocket.accept();
			JugadorEnvia jugador = new JugadorEnvia(socket);
			jugador.setJugadorCoordenadas(coordenadas[jugadores.size()][0], coordenadas[jugadores.size()][1]);
			jugadores.add(jugador);
			jugadores.get(jugadores.size() - 1).setId(1);
		}
		jugadores.get(0).setRival(jugadores.get(1).getJugador().getNick());
		jugadores.get(1).setRival(jugadores.get(0).getJugador().getNick());
		jugadores.get(0).start();
		jugadores.get(1).start();
		System.out.println("Iniciando partida...");
		
		// TO DO: 3 2 1 
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) { }
		while (continuar) {
			try {
				Thread.sleep(velocidad);
				bola.mover();
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

}

class JugadorEnvia extends Thread {
	
    private Socket socket;
    private ObjectOutputStream oos;
    private String rival;
    private boolean continuar = true;
    private JugadorEscucha escucha;
    private Coordenadas coordenadas;
    private int id;
    
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
    	coordenadas = new Coordenadas(escucha.getJugador().getCoordenadas(), MainServidor.getRival(id), MainServidor.getBola());
    }
    
    public Jugador getJugador() {
    	return escucha.getJugador();
    }
    
    @Override
    public void run() {
        try {
			oos.writeObject(rival);
			while (MainServidor.continuar()) {
				try {
					Thread.sleep(MainServidor.getVelocidad());
					coordenadas.setBola(MainServidor.getBola());
					System.out.println(escucha.getJugador().getNick() + " " + coordenadas);
					oos.writeObject(coordenadas);
				} catch (InterruptedException e) {
					System.out.println("Error Thread.sleep()");
					continuar = false;
				}
			}
			//desconnectar();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void desconnectar() throws IOException {
        socket.close();
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
				System.out.println(key);
				
			}
			//desconnectar();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void desconnectar() throws IOException {
        socket.close();
    }
}
