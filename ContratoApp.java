package contrato;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;

public class ContratoApp {

	static Scanner ler = new Scanner(System.in);
	static List<Colaborador> colaboradores = new ArrayList<>();
	static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	static List<Contrato> contratos = new ArrayList<>();
	static List<VendaComissionada> vendas = new ArrayList<>();
	
	public static void main(String[] args) throws ParseException {

		int opcao;
		
		do {		
			System.out.println("\n*** Seletor de Opções ***");
			System.out.println("\n1 - Inserir colaborador");
			System.out.println("2 - Registrar contrato");
			System.out.println("3 - Consultar contrato");
			System.out.println("4 - Encerrar contrato");
			System.out.println("5 - Listar colaboradores ativos");
			System.out.println("6 - Consultar contratos do colaborador");
			System.out.println("7 - Lançar venda comissionada");
			System.out.println("8 - Emitir folha de pagamento");
			System.out.println("0 - Fim");
			System.out.print("\nDigite a opção: ");
		
			opcao = ler.nextInt();
			ler.nextLine();
			
			switch (opcao) {
			case 1:
				inserirColaborador();				
				break;
			case 2:
				registrarContrato();				
				break;
			case 3:				
				consultarContrato();				
				break;
			case 4:
				encerrarContrato();				
				break;
			case 5:
				listarColaboradoresAtivos();				
				break;
			case 6:
				consultarContratoColaborador();		
				break;
			case 7:		
				lancarVendasComissionadas();
				break;
			case 8:				
				emitirFolhaPagamento();
				break;
			case 0:
				System.out.println();
				System.out.println("Operação finalizada");
				break;
			default:
				System.out.println();
				System.out.println("Opção incorreta.");				
			}
			
		} while (opcao != 0);
		
		ler.close();
	}

	public static void inserirColaborador() throws ParseException {
		String matricula, nome, cpf;
		Date data;
		Colaborador colaborador = null;
		
		System.out.print("\nDigite a matrícula do colaborador: ");
		matricula = ler.nextLine();
		if (!matricula.matches("[0-9]*")) {
			System.out.println("\nA matrícula deve ser formada só por números.");
			return;
		}
		System.out.print("Digite o CPF do colaborador: ");
		cpf = ler.nextLine();		
		System.out.print("Digite o nome do colaborador: ");
		nome = ler.nextLine();
		System.out.print("Digite a data de nascimento do colaborador: ");
		data = sdf.parse(ler.nextLine());
		
		// calcular idade (https://www.devmedia.com.br/calcule-a-idade-corretamente-em-java/4729)
		Calendar dataNasc = new GregorianCalendar();
		dataNasc.setTime(data);
		Calendar hoje = Calendar.getInstance();
		int idade = hoje.get(Calendar.YEAR) - dataNasc.get(Calendar.YEAR);
		dataNasc.add(Calendar.YEAR, idade);
		if (idade < 18) {
			System.out.println("\nNão é posível inserir colaborador com menos de 18 anos.");
			return;
		}
		
		colaborador = new Colaborador(matricula, cpf, nome, data);
		if (colaborador.validarCpf() == false) {
			System.out.println("\nCPF inválido.");
			return;
		}		
		
		System.out.println("\nColaborador inserido.");
		
	}
	
	public static void registrarContrato() throws ParseException {
		Date data;
		String matricula, tipo;
		Contrato contrato = null;
		float salMensal, percInsal, percPerc, percComissao, ajudaCusto, valorHora;
		int horaMes;
		
		System.out.print("\nDigite a data de início do contrato: ");
		data = sdf.parse(ler.nextLine());
		if (data.before(new Date())) {
			System.out.println("\nNão é possível registrar contrato com data de início anterior a data atual.");
			return;
		}
		System.out.print("Digite o tipo do contrato [Assalariado | Comissionado | Horista]: ");
		tipo = ler.nextLine();
		System.out.print("Digite a matrícula do colaborador ligado ao contrato: ");
		matricula = ler.nextLine();
		for (Colaborador c : colaboradores) {
			if (matricula == c.getMatricula()) {
				if (tipo.equals("Assalariado")) {
					System.out.println("Digite o salário mensal do colaborador: ");
					salMensal = ler.nextFloat();
					System.out.println("Digite o percentual de insalubridade: ");
					percInsal = ler.nextFloat();
					System.out.println("Digite o percentual de periculosidade: ");
					percPerc = ler.nextFloat();
					contrato = new ContratoAssalariado(data, c, salMensal, percInsal, percPerc);
				}
				if (tipo.equals("Comissionado")) {
					System.out.println("Digite o percentual da comissão do colaborador: ");
					percComissao = ler.nextFloat();
					System.out.println("Digite a ajuda de custo: ");
					ajudaCusto = ler.nextFloat();
					contrato = new ContratoComissionado(data, c, percComissao, ajudaCusto);
				}
				if (tipo.equals("Comissionado")) {
					System.out.println("Digite as horas trabalhadas por mês do colaborador: ");
					horaMes = ler.nextInt();
					System.out.println("Digite o valor da hora de trabalho do colaborador: ");
					valorHora = ler.nextFloat();
					contrato = new ContratoHorista(data, c, horaMes, valorHora);
				}
			} else {
				System.out.println("\nColaborador não encontrado.");
				return;
			}
		}		
		
		System.out.println("\nNúmero do contrato registrado: " + contrato.getId());
		
	}
	
	public static void consultarContrato() {
		int idContrato;
		
		System.out.print("Digite o id do contrato: ");
		idContrato = ler.nextInt();
		for (Contrato c : contratos) {
			if (idContrato == c.getId()) {
				System.out.println("Dados do contrato: ");
				System.out.println("Data de início: " + c.getDataInicio());
				System.out.println("Data de encerramento: " + c.getDataEncerramento());
				System.out.println("Situação: " + (c.isAtivo() == true ? "Ativo" : "Desativo"));
				if (c instanceof ContratoAssalariado) {
					System.out.println("Tipo do contrato: Assalariado");
				}
				if (c instanceof ContratoComissionado) {
					System.out.println("Tipo do contrato: Comissionado");
				}
				if (c instanceof ContratoHorista) {
					System.out.println("Tipo do contrato: Horista");
				}
				System.out.println("Dados do colaborador: ");
				System.out.println("Matrícula: " + c.getColaborador().getMatricula());
				System.out.println("Nome: " + c.getColaborador().getNome());
				System.out.println("CPF: " + c.getColaborador().getCpf());
				System.out.println("Situação: " + (c.getColaborador().isSituacao() == true ? "Ativo" : "Desativo"));
			}
		}
	}
	
	public static void encerrarContrato() throws ParseException {
		int idContrato;
		Date data;
		
		System.out.print("Digite o id do contrato: ");
		idContrato = ler.nextInt();
		for (Contrato c : contratos) {
			if (idContrato == c.getId()) {
				if (c.isAtivo() == false) {
					System.out.println("Contrato já encerrado.");
				}
				System.out.print("Data de encerramento do contrato: ");
				data = sdf.parse(ler.nextLine());
				if (data.before(new Date())) {
					System.out.println("\nNão é possível encerrar contrato com data de encerramento anterior a data atual.");
					return;
				}
				c.setDataEncerramento(data);
				c.encerrarContrato();
				System.out.println("Contrato encerrado.");
			} else {
				System.out.println("Contrato não existe.");
			}
		}
	}
	
	public static void listarColaboradoresAtivos() {
		boolean colaboradorAtivo = false;
		
		System.out.println("Lista de colaboradores ativos");
		for (Colaborador c : colaboradores) {
			if (c.isSituacao() == true) {
				colaboradorAtivo = true;
				System.out.println("\nMatrícula: " + c.getMatricula());
				System.out.println("Nome: " + c.getNome());
				System.out.println("CPF: " + c.getCpf());				
			}
		}
		if (colaboradorAtivo == false) {
			System.out.println("Nenhum colaborador ativo encontrado.");
		}
	}
	
	public static void consultarContratoColaborador() {
		String entrada;
		
		System.out.print("digite a matricula ou o CPF do colaborador: ");
		entrada = ler.nextLine();
		for (Contrato c : contratos) {
			if (entrada == c.getColaborador().getMatricula() || entrada == c.getColaborador().getCpf()) {
				System.out.println("Dados do colaborador: ");
				System.out.println("Matrícula: " + c.getColaborador().getMatricula());
				System.out.println("Nome: " + c.getColaborador().getNome());
				System.out.println("CPF: " + c.getColaborador().getCpf());
				System.out.println("Situação: " + (c.getColaborador().isSituacao() == true ? "Ativo" : "Desativo"));
				System.out.println("\nDados do contrato: ");
				System.out.println("Id: " + c.getId());
				if (c instanceof ContratoAssalariado) {
					System.out.println("Tipo do contrato: Assalariado");
				}
				if (c instanceof ContratoComissionado) {
					System.out.println("Tipo do contrato: Comissionado");
				}
				if (c instanceof ContratoHorista) {
					System.out.println("Tipo do contrato: Horista");
				}
				System.out.println("Data de início: " + c.getDataInicio());
				System.out.println("Data de encerramento: " + c.getDataEncerramento());
				System.out.println("Situação: " + (c.isAtivo() == true ? "Ativo" : "Desativo"));				
			}
		}
	}
	
	public static void lancarVendasComissionadas() {
		int id, mes, ano;
		float valorVenda;
		VendaComissionada vendaCom = null;
		
		System.out.print("Digite o id do contrato: ");
		id = ler.nextInt();
		for (Contrato c : contratos) {
			if (id == c.getId()) {
				if (c.isAtivo() == true) {
					if (c instanceof ContratoComissionado) {
						if (id == vendaCom.getContrComissionado().getId()) {
							System.out.print("Digite o mês: ");
							mes = ler.nextInt();
							System.out.print("Digite o ano: ");
							ano = ler.nextInt();
							System.out.print("Digite o valor total das vendas: ");
							valorVenda  = ler.nextInt();
							vendaCom = new VendaComissionada(mes, ano, valorVenda, (ContratoComissionado) c);
						}						
					} else {
						System.out.println("Este contrato não do tipo comissionado.");
					}
				} else {
					System.out.println("Contrato encerrado.");
				}
			} else {
				System.out.println("Contrato não encontrado.");
			}
		}
	}
	
	public static void emitirFolhaPagamento() {
		int mes, ano;
		float salario = 0, vlFaturam = 0;		
		
		System.out.print("Digite o mês da folha de pagamento: ");
		mes = ler.nextInt();
		System.out.print("Digite o ano da folha de pagamento: ");
		ano = ler.nextInt();
		for (VendaComissionada vc : vendas) {
			if (mes == vc.getMes() && ano == vc.getAno()) {
				vlFaturam += vc.getValor();
			}
		}
		System.out.println("Folha de pagamento de " + mes + "/" + ano);
		for (Contrato c : contratos) {
			if (c.isAtivo() == true) {
				System.out.println("\nDados do contrato: ");
				System.out.println("Id: " + c.getId());
				if (c instanceof ContratoAssalariado) {
					System.out.println("Tipo do contrato: Assalariado");
					salario = ((ContratoAssalariado) c).calcVencimento();
				}
				if (c instanceof ContratoComissionado) {
					System.out.println("Tipo do contrato: Comissionado");
					salario = ((ContratoComissionado) c).calcVencimento(vlFaturam);
				}
				if (c instanceof ContratoHorista) {
					System.out.println("Tipo do contrato: Horista");
					salario = ((ContratoHorista) c).calcVencimento();
				}
				System.out.println("\nDados do colaborador: ");
				System.out.println("Matrícula: " + c.getColaborador().getMatricula());
				System.out.println("Nome: " + c.getColaborador().getNome());
				System.out.printf("\nSalário: R$ %.2f", salario);
			}
		}
	}
	
}
