package pgv.models;

import java.io.Serializable;

public class Coordenada implements Serializable {

	private double x;
	private double y;
	
	public Coordenada(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void mover() {
		x += 10.9;
		y += 10;
		System.out.println(x + " " + y);
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
