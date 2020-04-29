package model;

/**
 * Classe modelo que representa uma mensagem
 * @since 2020/04/24
 */
public class Mensagem {
	
	private Usuario owner;
	
	private Usuario receiver;
	
	private String message;
	
	public Mensagem(Usuario owner, Usuario receiver, String message) {
		this.setReceiver(receiver);
		this.setMessage(message);
		this.setOwner(owner);
	}
	
	public Mensagem(Usuario owner, String message) {
		this.setMessage(message);
		this.setOwner(owner);
	}

	public Usuario getOwner() {
		return owner;
	}

	public void setOwner(Usuario owner) {
		this.owner = owner;
	}

	public Usuario getReceiver() {
		return receiver;
	}

	public void setReceiver(Usuario receiver) {
		this.receiver = receiver;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String toString() {
		return "Mensagem [owner=" + owner.getUsername() + ", message=" + message + "]";
	}	
}
