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
import java.util.stream.Collectors;

import constants.Config;
import model.Paciente;

/**
 * Classe reponsável por realizar o controle dos pacientes da aplicação
 * @since 2020/04/24
 */
public final class PacienteController {

	/**
	 * Método responsável por salvar um paciente
	 * @param {@link Paciente} paciente
	 */
	public void savePaciente(Paciente paciente) {
		try {
			paciente.setId(UUID.randomUUID().toString());
			File file = this.openDataFile();

			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(this.parseToCsv(paciente));
			bw.newLine();

			bw.flush();
			bw.close();

		} catch (Exception e) {
			System.err.println(e);
		}
	}

	/**
	 * Método responsável por listar todos os pacientes
	 * @return {@link List} of {@link Paciente}
	 */
	public List<Paciente> getAllPacientes() {
		List<Paciente> pacientes = new ArrayList<Paciente>();
		try {
			File file = this.openDataFile();
			BufferedReader br = new BufferedReader(new FileReader(file));

			String st;
			while ((st = br.readLine()) != null) {
				String[] data = st.split(",");
				Paciente p = parseCsvToPaciente(data);
				pacientes.add(p);
			}
			
			br.close();

		} catch (Exception e) {
			System.err.println(e);
		}
		
		return pacientes;
	}
	
	/**
	 * Método responsável por filtrar pacientes através da região e sintomas
	 * @param {@link String} city
	 * @param {@link String} state
	 * @param {@link List} of {@link Integer} sintomas
	 * @return {@link List} of {@link Paciente}
	 */
	public List<Paciente> filterPaciente(String city, String state, List<Integer> sintomas) {
		List<Paciente> pacientes = this.getAllPacientes().stream().filter(p -> {
			boolean contains = true;
			if (state != null && state != "") {
				contains = contains && p.getState().toUpperCase().equals(state.toUpperCase());
			}
			
			if (city != null && city != "") {
				contains = contains && p.getCity().toUpperCase().equals(city.toUpperCase());
			}
			
			if (sintomas != null && sintomas.size() > 0) {
				boolean containsSintoma = false;
				for (Integer index: sintomas) {
					containsSintoma = p.getSintomas().contains(index);
				}
				contains = contains && containsSintoma;
			}
			
			return contains;
		}).collect(Collectors.toList());
		
		return pacientes;
	}
	
	/**
	 * Método responsável por pegar um paciente pelo username
	 * @param {@link String} username
	 * @return {@link Paciente}
	 */
	public Paciente getPacienteByUsername(String username) {
		Paciente paciente = new Paciente();
		List<Paciente> pacientes = this.getAllPacientes();
		
		for(Paciente p : pacientes) {
			if (p.getUsername().equals(username)) {
				paciente = p;
			}
		}
		return paciente;
	}
	
	/**
	 * Método responsável por realizar o login do paciente
	 * @param {@link String} username
	 * @param {@link String} password
	 * @return {@link Boolean}
	 */
	public boolean signin(String username, String password) {
		List<Paciente> pacientes = this.getAllPacientes();
		boolean isValid = false;
		
		for(Paciente p : pacientes) {
			if (p.getUsername().equals(username) && p.getPassword().equals(password)) {
				isValid = true;
				break;
			}
		}
		
		return isValid;
	}
	
	/**
	 * Método responsável por abrir o arquivo que contêm todos os pacientes da aplicação
	 * @return {@link File}
	 */
	private File openDataFile() {
		File file = new File(Config.PACIENTE_FILE_PATH);

		try {
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch(IOException e) {
			System.err.println("Erro ao criar o arquivo");
		}
		
		return file;
	}

	/**
	 * Método responsável por parsear os dados csv para um objeto Paciente
	 * @param {@link Array} of {@link String} data
	 * @return {@link Paciente}
	 */
	private Paciente parseCsvToPaciente(String[] data) {
		Paciente p = new Paciente();
		String[] sintomas = data[8].split(";");
		
		p.setId(data[0]);
		p.setUsername(data[1]);
		p.setPassword(data[2]);
		p.setName(data[3]);
		p.setCpf(data[4]);
		p.setState(data[5]);
		p.setCity(data[6]);
		p.setPhone(data[7]);
		p.setSintomas(this.parseSintomas(sintomas));

		return p;
	}
	
	/**
	 * Método responsável por mapear os sintomas do paciente
	 * @param {@link Array} of {@link String} data
	 * @return {@link List} of {@link Integer}
	 */
	public List<Integer> parseSintomas(String[] data) {
		List<Integer> sintomas = new ArrayList<Integer>();
				
		for (int i=0; i<data.length; i++) {
			sintomas.add(Integer.parseInt(data[i]));
		}
		
		return sintomas;
	}

	/**
	 * Método responsável por parsear os dados de um paciente para o csv
	 * @param {@link Paciente} p
	 * @return {@link String}
	 */
	private String parseToCsv(Paciente p) {
		String sintomas = new String();
		
		for(Integer i : p.getSintomas()) {
			sintomas += i.toString() + ";";
		}
		
		return p.getId() + "," + p.getUsername() + "," + p.getPassword() + "," + p.getName() + "," + p.getCpf() + "," + p.getState() + ","
				+ p.getCity() + "," + p.getPhone() + "," + sintomas;
	}
}
