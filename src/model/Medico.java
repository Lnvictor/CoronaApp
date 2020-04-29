package model;

/**
 * Classe modelo que representa um médico
 * @since 2020/04/24
 */
public class Medico extends Usuario {

	private String crm;

	public String getCrm() {
		return crm;
	}

	public void setCrm(String crm) {
		this.crm = crm;
	}
}
