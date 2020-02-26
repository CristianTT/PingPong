package pgv.models;

import java.io.Serializable;

import pgv.servidor.MainServidor;

public class Coordenada implements Serializable {

	private static final long serialVersionUID = -8631397112906118169L;
	private double x;
	private double y;
	private int direccionX = 1;
	private int direccionY = 1;

	public Coordenada(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void moverBola(double jugador0X, double jugador1X) {
		int velocidad = 9;
		double tempX = x + velocidad * direccionX;
		double tempY = y + (velocidad + 2) * direccionY;
		if (tempX >= 0 && tempX < 500 - 30) {
			x = tempX;
		} else {
			direccionX = -direccionX;
		}
		if (tempX > jugador0X && tempX < jugador0X + 150 && tempY < 111 && tempY > 100
				|| tempX + 30 > jugador0X && tempX + 30 < jugador0X + 150 && tempY < 111 && tempY > 100
				|| tempX > jugador0X && tempX < jugador0X + 150 && tempY + 30 < 111 && tempY + 30 > 100
				|| tempX + 30 > jugador0X && tempX + 30 < jugador0X + 150 && tempY + 30 < 111 && tempY + 30 > 100) {
			direccionY = -direccionY;
		} else if (tempX > jugador1X && tempX < jugador1X + 150 && tempY > 1089 && tempY < 1100
				|| tempX + 30 > jugador1X && tempX + 30 < jugador1X + 150 && tempY > 1089 && tempY < 1100
				|| tempX > jugador1X && tempX < jugador1X + 150 && tempY + 30 > 1089 && tempY + 30 < 1100
				|| tempX + 30 > jugador1X && tempX + 30 < jugador1X + 150 && tempY + 30 > 1089 && tempY + 30 < 1100) {
			direccionY = -direccionY;
		} else {
			if (tempY >= 0 && tempY < 1200 - 30) {
				y = tempY;
			} else if (tempY < 0) { // Rebota en el lado izquierdo
				MainServidor.getPuntuacion().puntoJugador1();
				//direccionY = -direccionY;
				setRandomDireccionBola();
				MainServidor.enviarPuntuaciones(1);
			} else {
				MainServidor.getPuntuacion().puntoJugador0();
				//direccionY = -direccionY;
				setRandomDireccionBola();
				MainServidor.enviarPuntuaciones(0);
			}
		}
	}

	public void moverJugador(int direccion) {
		double tempX = x + 20 * direccion;
		if (tempX >= 0 && tempX < 500 - 150) {
			x = tempX;
		} else if (tempX < 0) {
			x = 0;
		} else {
			x = 350;
		}
	}

	public void setRandomDireccionBola() {
		x = 235.0;
		y = 585.0;
		direccionX = (((int) Math.floor(Math.random() * 2)) == 0) ? -1 : 1;
		direccionY = (((int) Math.floor(Math.random() * 2)) == 0) ? -1 : 1;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Coordenada [x=" + x + ", y=" + y + "]";
	}

}
