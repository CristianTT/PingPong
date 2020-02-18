package pgv.models;

public class Coordenadas {

	private Coordenada jugador;
	private Coordenada rival;
	private Coordenada bola;

	public Coordenadas(Coordenada jugador, Coordenada rival, Coordenada bola) {
		this.jugador = jugador;
		this.bola = bola;
	}

	public Coordenada getJugador() {
		return jugador;
	}

	public void setJugador(Coordenada jugador) {
		this.jugador = jugador;
	}

	public Coordenada getRival() {
		return rival;
	}

	public void setRival(Coordenada rival) {
		this.rival = rival;
	}

	public Coordenada getBola() {
		return bola;
	}

	public void setBola(Coordenada bola) {
		this.bola = bola;
	}

}
