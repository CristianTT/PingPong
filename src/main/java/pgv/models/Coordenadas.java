package pgv.models;

import java.io.Serializable;

public class Coordenadas implements Serializable {

	private Coordenada jugador;
	private Coordenada rival;
	private Coordenada bola;

	public Coordenadas(Coordenada jugador, Coordenada rival, Coordenada bola) {
		this.jugador = jugador;
		this.rival = rival;
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

	@Override
	public String toString() {
		return "Coordenadas [jugador=" + jugador + ", rival=" + rival + ", bola=" + bola + "]";
	}

}
