package pgv.models;

public class Puntuacion {

	int jugador;
	int rival;

	public Puntuacion(int jugador, int rival) {
		this.jugador = jugador;
		this.rival = rival;
	}

	public int getJugador() {
		return jugador;
	}

	public void setJugador(int jugador) {
		this.jugador = jugador;
	}

	public int getRival() {
		return rival;
	}

	public void setRival(int rival) {
		this.rival = rival;
	}

}
