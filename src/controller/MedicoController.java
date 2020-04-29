package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import constants.Config;
import model.Medico;

/**
 * Classe reponsável por realizar o controle dos médicos da aplicação
 * @since 2020/04/24
 */
public final class MedicoController {

	/**
	 * Método responsável por salvar um médico
	 * @param {@link Medico} medico
	 */
	public void saveMedico(Medico medico) {
		try {
			medico.setId(UUID.randomUUID().toString());
			File file = this.openDataFile();

			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(this.parseMedicoToCsv(medico));
			bw.newLine();

			bw.flush();
			bw.close();

		} catch (Exception e) {
			System.err.println("[MEDICO-CONTROLLER]" + e.getMessage());
		}
	}
	
	/**
	 * Método responsável por buscar um médico através do username
	 * @param {@link String} username
	 * @return {@link Medico}
	 */
	public Medico getMedicoByUsername(String username) {
		Medico medico = new Medico();
		List<Medico> medicos = this.getAllMedicos();
		
		for(Medico m : medicos) {
			if (m.getUsername().equals(username)) {
				medico = m;
			}
		}
		return medico;
	}

	/**
	 * Método responsável por listar todos os médicos
	 * @return {@link List} of {@link Medico}
	 */
	public List<Medico> getAllMedicos() {
		List<Medico> medicos = new ArrayList<Medico>();
		try {
			File file = this.openDataFile();
			BufferedReader br = new BufferedReader(new FileReader(file));

			String st;
			while ((st = br.readLine()) != null) {
				String[] data = st.split(",");
				Medico p = parseCsvToMedico(data);
				medicos.add(p);
			}
			
			br.close();

		} catch (Exception e) {
			System.err.println("[MEDICO-CONTROLLER]" + e.getMessage());
		}
		
		return medicos;
	}
	
	/**
	 * Método responsável por realizar o login do médico
	 * @param {@link String} username
	 * @param {@link String} password
	 * @return {@link Boolean}
	 */
	public boolean signin(String username, String password) {
		List<Medico> medicos = this.getAllMedicos();
		boolean isValid = false;
		
		for(Medico m : medicos) {
			if (m.getUsername().equals(username) && m.getPassword().equals(password)) {
				isValid = true;
				break;
			}
		}
		
		return isValid;
	}
	
	/**
	 * Método responsável por abrir o arquivo que contêm todos os médicos da aplicação
	 * @return {@link File}
	 */
	private File openDataFile() {
		File file = new File(Config.MEDICO_FILE_PATH);

		try {
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch(IOException e) {
			System.err.println("[MEDICO-CONTROLLER] Erro ao criar o arquivo");
		}
		
		return file;
	}

	/**
	 * Método responsável por parsear os dados csv para um objeto Medico
	 * @param {@link Array} of {@link String} data
	 * @return {@link Medico}
	 */
	private Medico parseCsvToMedico(String[] data) {
		Medico m = new Medico();
		
		m.setId(data[0]);
		m.setUsername(data[1]);
		m.setPassword(data[2]);
		m.setName(data[3]);
		m.setCrm(data[4]);

		return m;
	}

	/**
	 * Método responsável por parsear os dados de um médico para o csv
	 * @param {@link Medico} m
	 * @return {@link String}
	 */
	private String parseMedicoToCsv(Medico m) {
		return m.getId() + "," + m.getUsername() + "," + m.getPassword() + "," + m.getName() + "," 
			+ m.getCrm();
	}
}
