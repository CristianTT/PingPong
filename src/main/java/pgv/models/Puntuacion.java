package pgv.models;

import java.io.Serializable;

public class Puntuacion implements Serializable {

	private static final long serialVersionUID = 2729879307460456158L;
	int jugador0;
	int jugador1;

	public Puntuacion() {
		this.jugador0 = 0;
		this.jugador1 = 0;
	}

	public Puntuacion(Puntuacion p) {
		this.jugador0 = p.getJugador0();
		this.jugador1 = p.getJugador1();
	}

	public int getJugador0() {
		return jugador0;
	}

	public void puntoJugador0() {
		this.jugador0 += 1;
	}

	public int getJugador1() {
		return jugador1;
	}

	public void puntoJugador1() {
		this.jugador1 += 1;
	}

	@Override
	public String toString() {
		return jugador0 + " | " + jugador1;
	}
	

}
