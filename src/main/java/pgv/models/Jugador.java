package pgv.models;

public class Jugador {

	private String nick;
	private Coordenada coordenadas;

	public Jugador(String nick, Coordenada coordenadas) {
		this.nick = nick;
		this.coordenadas = coordenadas;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Coordenada getCoordenadas() {
		return coordenadas;
	}

	public void setCoordenadas(Coordenada coordenadas) {
		this.coordenadas = coordenadas;
	}

}
