package pgv.models;

import java.io.Serializable;

public class Coordenadas implements Serializable {

	private static final long serialVersionUID = -6575489000449627139L;
	private Coordenada jugador0;
	private Coordenada jugador1;
	private Coordenada bola;

	public Coordenadas(Coordenada jugador0, Coordenada jugador1, Coordenada bola) {
		this.jugador0 = jugador0;
		this.jugador1 = jugador1;
		this.bola = bola;
	}

	public Coordenadas(Coordenadas coordenadas) {
		this.jugador0 = coordenadas.getJugador0();
		this.jugador1 = coordenadas.getJugador1();
		this.bola = coordenadas.getBola();
	}

	public Coordenada getJugador0() {
		return jugador0;
	}

	public void setJugador0(Coordenada jugador0) {
		this.jugador0 = jugador0;
	}

	public Coordenada getJugador1() {
		return jugador1;
	}

	public void setJugador1(Coordenada jugador1) {
		this.jugador1 = jugador1;
	}

	public Coordenada getBola() {
		return bola;
	}

	public void setBola(Coordenada bola) {
		this.bola = bola;
	}

	@Override
	public String toString() {
		return "Coordenadas [jugador0=" + jugador0 + ", jugador1=" + jugador1 + ", bola=" + bola + "]";
	}

}
