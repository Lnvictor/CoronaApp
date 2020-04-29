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

import constants.Config;
import model.Mensagem;
import model.Usuario;

/**
 * Classe reponsável por realizar o controle das mensagens e a persistência dos dados
 * @since 2020/04/24
 */
public final class MensagemController {
	
	/**
	 * Método responsável por enviar uma mensagem e salvar no arquivo .csv
	 * @param {@link Mensagem} msg
	 */
	public void sendMessage(Mensagem msg) {
		String filename = msg.getOwner().getId() + "." + msg.getReceiver().getId() + ".csv";
		File file = this.openDataFile(filename);
		
		try {
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(this.parseMessageToCsv(msg));
			bw.newLine();
	
			bw.flush();
			bw.close();
		} catch(Exception e) {
			System.err.println("[MESSAGEM-CONTROLLER]" + e.getMessage());
		}
	}
	
	/**
	 * Método responsável por enviar uma mensagem e salvar no arquivo .csv
	 * @param {@link Mensagem} msg
	 * @param {@link File} file
	 */
	public void sendMessage(Mensagem msg, File file) {
		try {
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(this.parseMessageToCsv(msg));
			bw.newLine();
	
			bw.flush();
			bw.close();
		} catch(Exception e) {
			System.err.println("[MESSAGEM-CONTROLLER]" + e.getMessage());
		}
	}
	
	/**
	 * Método responsável por pegar todas as mensagens enviadas dado o id de um usuário
	 * @param {@link String} id
	 * @return {@link List} of {@link Mensagem}
	 */
	public List<Mensagem> getMessages(String id) {
		List<Mensagem> msgs = new ArrayList<Mensagem>();
		File file = this.findFileByUserId(id);
		try {
			if (file != null) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String st;
				
				while ((st = br.readLine()) != null) {
					String[] data = st.split(",");
					Mensagem m = parseCsvToMessage(data);
					msgs.add(m);
				}
				br.close();
			} else {
				System.out.println("Mensagens não encontradas");
			}
		} catch (Exception e) {
			System.err.println(e);
		}
		
		return msgs;
	}
	
	/**
	 * Método responsável por buscar um arquivo pelo id do usuário
	 * @param {@link String} id
	 * @return {@link File}
	 */
	public File findFileByUserId(String id) {
		File folder = new File(Config.FULL_PATH);
		File[] listOfFiles = folder.listFiles();
				
		for(File file: listOfFiles) {
			if (file.isFile() && file.getName().indexOf(id) != -1) {
				return file;
			}
		}
		
		return null;
	}
	
	/**
	 * Método responsável por abrir um arquivo
	 * @param {@link String} filename
	 * @return {@link File}
	 */
	private File openDataFile(String filename) {
		File file = new File(Config.FULL_PATH + filename);

		try {
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch(IOException e) {
			System.err.println("[MESSAGE-CONTROLLER] Erro ao criar o arquivo");
		}
		
		return file;
	}
	
	/**
	 * Método responsável por parsear os dados csv para um objeto Mensagem
	 * @param {@link Array} of {@link String} data
	 * @return {@link Mensagem}
	 */
	private Mensagem parseCsvToMessage(String data[]) {
		Usuario owner = new Usuario();
		owner.setUsername(data[0]);
		Mensagem msg = new Mensagem(owner, data[1]);
		
		return msg;
	}
	
	/**
	 * Método responsável por parsear os dados de uma mensagem para o csv
	 * @param {@link Mensagem} msg
	 * @return {@link String}
	 */
	private String parseMessageToCsv(Mensagem msg) {
		return msg.getOwner().getUsername() + "," + msg.getMessage();
	}
}
