package pgv.models;

public class Jugador {

	private String nick;
	private Coordenada coordenadas;
	private String ip;

	public Jugador(String nick, Coordenada coordenadas, String ip) {
		this.nick = nick;
		this.coordenadas = coordenadas;
		this.ip = ip;
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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
