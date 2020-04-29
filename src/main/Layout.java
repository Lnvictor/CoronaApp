package main;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import controller.MedicoController;
import controller.MensagemController;
import controller.PacienteController;
import model.Medico;
import model.Mensagem;
import model.Paciente;
import model.Sintomas;

/**
 * Classe reponsável por realizar a renderização da interface da aplicação
 * @since 2020/04/24
 */
public final class Layout {
	
	public void renderMainMenu() {
		Sintomas sintomas = new Sintomas();
		Scanner scanner = new Scanner(System.in);
		Scanner scannerTemSintoma = new Scanner(System.in);
		int option = 0;
		
		while (option != 6) {
			System.out.println("-------------CoronaApp--------------");
			System.out.println("1. Listar Sintomas da Covid-19");
			System.out.println("2. Fazer login como paciente");
			System.out.println("3. Fazer login como médico");
			System.out.println("4. Fazer Cadastro como paciente");
			System.out.println("5. Fazer Cadastro como médico");
			System.out.println("6. Sair da aplicação");
			System.out.println("Opção: ");
			option = scanner.nextInt();
			
			if (option == 1) {
				int index = 0;
				
				System.out.println("Principais sintomas da COVID-19");
				for (String sintoma : sintomas.getSintomas()) {
					System.out.println(++index + " - "+ sintoma);
				}
				
				String temSintoma = "";
				
				while (!temSintoma.toUpperCase().equals("S") && !temSintoma.toUpperCase().equals("N")) {
					System.out.println("Possui alguns dos sintomas citados acima? [s/n]");
					temSintoma = scannerTemSintoma.nextLine();
				}
				
				if (temSintoma.toUpperCase().equals("S")) {
					renderPacienteForm();
				}
			} else if (option == 2) {
				renderPacienteLoginForm();
			} else if (option == 3) {
				renderMedicoLoginForm();
			} else if (option == 4) {
				renderPacienteForm();
			} else if (option == 5) {
				renderMedicoForm();
			}
		}
		
		scanner.close();
	}
	
	private static void renderMedicoMessageSender(Medico medico) {
		PacienteController pc = new PacienteController();
		Scanner dataScanner = new Scanner(System.in);
		String usernamePaciente, message;
		
		Paciente paciente;
		
		System.out.println("Digite o username do paciente: ");
		usernamePaciente = dataScanner.nextLine();
		paciente = pc.getPacienteByUsername(usernamePaciente);
		
		if (paciente.getUsername() == null) {
			System.out.println("Paciente não encontrado...");
		} else {
			System.out.println("Digite a mensagem: ");
			message = dataScanner.nextLine();
			
			Mensagem messageToSend = new Mensagem(medico, paciente, message);
			MensagemController mc = new MensagemController();
			mc.sendMessage(messageToSend);
			
			System.out.println("Mensagem enviada com sucesso!");
		}
	}
	
	private static void renderMessagesMedico(Medico medico) {
		MensagemController mc = new MensagemController();
		PacienteController pc = new PacienteController();
		Scanner dataScanner = new Scanner(System.in);
		
		System.out.println("Digite o username do paciente para ver as mensagens: ");
		String usernamePaciente = dataScanner.nextLine();
		Paciente paciente = pc.getPacienteByUsername(usernamePaciente);
		
		if (paciente.getUsername() == null) {
			System.out.println("Paciente não encontrado...");
		} else {
			List<Mensagem> msgs = mc.getMessages(paciente.getId());
			
			if(!msgs.isEmpty()) {
				System.out.println("-------------Mensagens------------");
				for(Mensagem msg: msgs) {
					System.out.println(msg.getOwner().getUsername() + ": " +msg.getMessage());
				}
			}
		}
	}
	
	private static void renderPacienteMenu(Paciente p) {
		Scanner scannerPaciente = new Scanner(System.in);
		Sintomas sintoma = new Sintomas();
		int option = 0;
		
		while (option != 4) {
			System.out.println("------ Bem vindo " + p.getUsername() + "-------");
			System.out.println("1. Meus sintomas");
			System.out.println("2. Ver mensagens");
			System.out.println("3. Enviar mensagem");
			System.out.println("4. Logout");
			System.out.println("Opção: ");
			option = scannerPaciente.nextInt();
			
			if (option == 1) {
				System.out.println("---------MEUS SINTOMAS----------");
				for (Integer index: p.getSintomas()) {
					System.out.println("  - " + sintoma.getSintomaByIndex(index-1));
				}
			} else if (option == 2) {
				renderPacienteMessages(p);
			} else if (option == 3) {
				renderPacienteMessageSender(p);
			}
		}
	}
	
	private static void renderPacienteMessageSender(Paciente p) {
		Scanner dataScanner = new Scanner(System.in);
		String message = "";
		
		MensagemController mc = new MensagemController();
		File file = mc.findFileByUserId(p.getId());
		
		if (file != null) {
			System.out.println("Digite a mensagem: ");
			message = dataScanner.nextLine();
			
			Mensagem messageToSend = new Mensagem(p, message);
			mc.sendMessage(messageToSend, file);
			System.out.println("Mensagem enviada com sucesso!");
		} else {
			System.out.println("Não há um médico vinculado para o envio de mensagem. "
					+ "Espere um médico entrar em contato...");
		}
	}
	
	private static void renderPacienteMessages(Paciente p) {
		MensagemController mc = new MensagemController();
		List<Mensagem> msgs = mc.getMessages(p.getId());
		
		if(!msgs.isEmpty()) {
			System.out.println("-------------Mensagens------------");
			for(Mensagem msg: msgs) {
				System.out.println(msg.getOwner().getUsername() + ": " +msg.getMessage());
			}
		}
	}
	
	private static void renderMedicoMenu(String username) {
		Scanner scannerMedico = new Scanner(System.in);
		Medico medico = new MedicoController().getMedicoByUsername(username);
		int option = 0;
		
		while (option != 4) {
			System.out.println("------ Bem vindo " + username + "-------");
			System.out.println("1. Filtrar pacientes");
			System.out.println("2. Enviar mensagem a um paciente");
			System.out.println("3. Ver mensagens");
			System.out.println("4. Logout");
			System.out.println("Opção: ");
			option = scannerMedico.nextInt();
			
			if (option == 1) {
				renderFilterPaciente();
			} else if (option == 2) {
				renderMedicoMessageSender(medico);
			} else if (option == 3) {
				renderMessagesMedico(medico);
			}
		}
	}
	
	private static void renderFilterPaciente() {
		PacienteController pc = new PacienteController();
		Sintomas sintoma = new Sintomas();
		Scanner scannerPaciente = new Scanner(System.in);
		String state, city = "";
		
		System.out.println("----------Filtrar Paciente----------");
		System.out.println("Digite o estado: ");
		state = scannerPaciente.nextLine();
		System.out.println("Digite a cidade: ");
		city = scannerPaciente.nextLine();
		System.out.println("Digite os ID's dos sintomas: (ex: 1,3,5)");
		String[] sintomasSplited = scannerPaciente.nextLine().split(",");
		List<Integer> sintomas = pc.parseSintomas(sintomasSplited);
		
		System.out.println("Resultado:");
		List<Paciente> pacientes = pc.filterPaciente(city, state, sintomas);
		
		if (pacientes.size() > 0) {
			for (Paciente p: pacientes) {
				System.out.println("---------------------------------------------");
				System.out.println("Nome: " + p.getName());
				System.out.println("CPF: " + p.getCpf());
				System.out.println("Username: " + p.getUsername());
				System.out.println("Estado: " + p.getState());
				System.out.println("Cidade: " + p.getCity());
				System.out.println("Telefone: " + p.getPhone());
				System.out.println("Sintomas: ");
				for (Integer index: p.getSintomas()) {
					System.out.println("  - " + sintoma.getSintomaByIndex(index-1));
				}
			}
		} else {
			System.out.println("Sem resultado");
		}
		System.out.println("---------------------------------------------\n");
	}
	
	private static void renderPacienteLoginForm() {
		PacienteController pc = new PacienteController();
		Scanner scannerLogin = new Scanner(System.in);
		System.out.println("-----------LOGIN----------");
		System.out.println("usuário: ");
		String username = scannerLogin.nextLine();
		System.out.println("Senha: ");
		String password = scannerLogin.nextLine();
		
		if (pc.signin(username, password)) {
			Paciente paciente = pc.getPacienteByUsername(username);
			renderPacienteMenu(paciente);
		} else {
			System.out.println("Usuário inválido");
		}
	}
	
	@SuppressWarnings("resource")
	private static void renderMedicoLoginForm() {
		MedicoController mc = new MedicoController();
		Scanner scannerLogin = new Scanner(System.in);
		System.out.println("-----------LOGIN Médico (Acesso restrito)----------");
		System.out.println("usuário: ");
		String username = scannerLogin.nextLine();
		System.out.println("Senha: ");
		String password = scannerLogin.nextLine();
		
		if (mc.signin(username, password)) {
			renderMedicoMenu(username);
		} else {
			System.out.println("Usuário inválido");
		}
	}
	
	private static void renderPacienteForm() {
		Scanner scannerPaciente = new Scanner(System.in);
		Paciente p = new Paciente();
		PacienteController pc = new PacienteController();
		
		System.out.println("----Cadastre-se no nosso sistema----");
		System.out.println("Digite seu nome: ");
		p.setName(scannerPaciente.nextLine());
		System.out.println("Digite seu username: ");
		p.setUsername(scannerPaciente.nextLine());
		System.out.println("Digite sua senha: ");
		p.setPassword(scannerPaciente.nextLine());
		System.out.println("Digite seu estado: ");
		p.setState(scannerPaciente.nextLine());
		System.out.println("Digite sua cidade: ");
		p.setCity(scannerPaciente.nextLine());
		System.out.println("Digite seu CPF: ");
		p.setCpf(scannerPaciente.nextLine());
		System.out.println("Digite seu telefone: ");
		p.setPhone(scannerPaciente.nextLine());
		System.out.println("Digite os ID's dos sintomas que você se identificou: (ex: 1,3,5)");
		String[] sintomasSplited = scannerPaciente.nextLine().split(",");
		p.setSintomas(pc.parseSintomas(sintomasSplited));
		
		pc.savePaciente(p);
	}
	
	private static void renderMedicoForm() {
		Scanner scannerMedico = new Scanner(System.in);
		Medico m = new Medico();
		MedicoController mc = new MedicoController();
		
		System.out.println("----Cadastro do médico (ACESSO RESTRITO)----");
		System.out.println("Digite seu nome: ");
		m.setName(scannerMedico.nextLine());
		System.out.println("Digite seu username: ");
		m.setUsername(scannerMedico.nextLine());
		System.out.println("Digite sua senha: ");
		m.setPassword(scannerMedico.nextLine());
		System.out.println("Digite seu CRM: ");
		m.setCrm(scannerMedico.nextLine());
		mc.saveMedico(m);
	}
}
