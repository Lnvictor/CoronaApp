package model;

import java.util.List;

/**
 * Classe modelo que representa um paciente
 * @since 2020/04/24
 */
public class Paciente extends Usuario {

	private String city;
	
	private String phone;
	
	private String cpf;
	
	private String state;
	
	private List<Integer> sintomas;
	

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public List<Integer> getSintomas() {
		return sintomas;
	}

	public void setSintomas(List<Integer> sintomas) {
		this.sintomas = sintomas;
	}

	public String toString() {
		return "{ ID=" + this.getId() + "," +
				" Nome: " + this.getName() + "," +
				" Username: " + this.getUsername() + "," +
				" Password: " + this.getPassword() + "," +
				" CPF: " + this.getCpf() + "," +
				" Estado: " + this.getState() + "," +
				" Cidade: " + this.getCity() + "," +
				" Telefone: " + this.getPhone() + "}";
	}
}
