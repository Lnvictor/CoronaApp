package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe modelo que representa os sintomas da COVID-19
 * @since 2020/04/24
 */
public class Sintomas {

	private List<String> sintomas;
	
	public Sintomas() {
		this.sintomas = new ArrayList<String>();
		this.sintomas.add("Tosse");
		this.sintomas.add("Febre");
		this.sintomas.add("Cansaço");
		this.sintomas.add("Coriza");
		this.sintomas.add("Dor de garganta");
		this.sintomas.add("Dificuldade para respirar");
	}
	
	public List<String> getSintomas() {
		return this.sintomas;
	}
	
	public String getSintomaByIndex(Integer index) {
		if (index < this.sintomas.size()) {
			String sintoma = this.sintomas.get(index);
			return sintoma != null ? sintoma : "";
		}
		return "";
	}
}
