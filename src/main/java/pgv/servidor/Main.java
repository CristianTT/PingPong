package pgv.servidor;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import pgv.servidor.model.Coordenada;
import pgv.servidor.model.Jugador;

public class Main {

	private static ServerSocket serverSocket = null;
	private static ArrayList<EscucharServ> jugadores = new ArrayList<EscucharServ>();
	
	private static int puerto = 5555;
	static InetSocketAddress addr = null;
	
	private static double[][] coordenadas = {{175.0, 100.0}, {175.0, 685.0}};

	public static void main(String[] args) throws IOException {

		serverSocket = new ServerSocket();
		InetSocketAddress addr = new InetSocketAddress("127.0.0.1", puerto);
		serverSocket.bind(addr);
		
		while (jugadores.size() < 2) {
			Socket socket = serverSocket.accept();
			EscucharServ escucha = new EscucharServ(socket);
			escucha.setJugadorCoordenadas(coordenadas[jugadores.size()][0], coordenadas[jugadores.size()][1]);
			escucha.start();
			jugadores.add(escucha);			
		}
		
		
		
		/* Socket cliente = servidor.accept();

	        ObjectOutputStream outObjeto = new ObjectOutputStream(cliente.getOutputStream());
	        ObjectInputStream inObjeto = new ObjectInputStream(cliente.getInputStream());
	    
	        Datos alum = new Datos("Juan", "PGV", "7");
	        outObjeto.writeObject(alum);
	        System.out.println("Envio: " + alum.getNombre() + " " + alum.getAsignatura() + " " + alum.getNota());

	        Datos dato = (Datos) inObjeto.readObject();
	        System.out.println("Recibo: " + dato.getNombre() + " " + dato.getAsignatura() + " " + alum.getNota());*/
		
		
		
		
		
		
/*
		Scanner sc = new Scanner(System.in);
		System.out.println("Introduce la ip del servidor:");
		ip = sc.nextLine();
		puerto = 5555;
		System.out.println("Introduce un nick:");
		nickJugador = sc.nextLine();

		jugadorRectangle = new Rectangle();
		rivalRectangle = new Rectangle();
		bolaCircle = new Circle();

		addr = new InetSocketAddress(ip, puerto);
		try {
			serverSocket.connect(addr);
			entrada = new DataInputStream(serverSocket.getInputStream());
			salida = new DataOutputStream(serverSocket.getOutputStream());

			nickRival = entrada.readUTF();

			EscucharServ escucha = new EscucharServ(serverSocket);
			escucha.start();
		} catch (IOException error) {
			error.printStackTrace();
		}
		sc.close();*/
	}

}

class EscucharServ extends Thread {
	
    private Socket socket;
    private DataInputStream dis;
    private Jugador jugador;
    
    public EscucharServ(Socket socket) throws IOException {
        this.socket = socket;
        dis = new DataInputStream(socket.getInputStream());
		String nick = dis.readUTF();
		jugador = new Jugador(nick, new Coordenada(0.0, 0.0));
    }
    
    public void setJugadorCoordenadas(double x, double y) {
    	jugador.setCoordenadas(new Coordenada(x, y));
    }
    
    public void desconnectar() throws IOException {
        socket.close();
    }
    
    @Override
    public void run() {
    	System.out.println("Se ha conectado " + jugador.getNick());
		/*do {
			Object obj = (Object) dis.readUTF();
			if (obj instanceof Coordenadas) { // Posiciones de los objetos en el mapa
				GameController.moverObjeto(GameController.getJugadorRectangle(), ((Coordenadas) obj).getJugador().getX(), ((Coordenadas) obj).getJugador().getY());
				GameController.moverObjeto(GameController.getRivalRectangle(), ((Coordenadas) obj).getRival().getX(), ((Coordenadas) obj).getRival().getY());
				GameController.moverObjeto(GameController.getBolaCircle(), ((Coordenadas) obj).getBola().getX(), ((Coordenadas) obj).getBola().getY());
			} else if (obj instanceof Puntuacion) { // Cuando se marca un punto
				GameController.getPuntuacionLabel().setText(((Puntuacion) obj).getJugador() + " | " + ((Puntuacion) obj).getRival());
			} else if (obj instanceof String) { // Nick
				
			}
		} while (true);
    	
    	
        String tecla = dis.readUTF();
        if(accion.equals("hola")){
            System.out.println("El cliente con idSesion "+this.idSessio+" saluda");
            dos.writeUTF("adios");
        }
        */
        
        
        
        try {
			desconnectar();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}