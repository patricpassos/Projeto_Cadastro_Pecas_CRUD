package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import model.entities.Produtos;
import model.exceptions.DomainException;

public class Cadastro {

	public static void leituraArquivo(String path, List<Produtos> listaProdutos) throws DomainException {
		System.out.print("\nLISTA DE PEÇAS");
		listaProdutos.clear(); // limpeza da lista na memoria não acumula dados de leitura
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String line = br.readLine();
			if (line != null) {
				String[] header = line.split(",");
				System.out.printf("\n%-15s %-15s %-15s %-15s %-15s %-15s \n", header[0].trim(), header[1].trim(),
						header[2].trim(), header[3].trim(), header[4].trim(), header[4].trim());
				System.out.println(
						"-----------------------------------------------------------------------------------------------");
			}

			line = br.readLine();
			while (line != null) {
				String[] fields = line.split(",");
				String marca = fields[0].trim();
				String modalidade = fields[1].trim();
				String serie = fields[2].trim();
				Integer quantidade = Integer.parseInt(fields[3].trim());
				Double preco = Double.parseDouble(fields[4].trim());

				Produtos peca = new Produtos(marca, modalidade, serie, quantidade, preco);
				listaProdutos.add(peca);
				line = br.readLine();
			}
			for (Produtos p : listaProdutos) {
				System.out.println(p);
			}
			System.out.println(
					"-----------------------------------------------------------------------------------------------");
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public static void salvamentoArquivo(String path, List<Produtos> listaPeca) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
			bw.write("marca, modalidade, serie, quantidade, preco, total");
			bw.newLine();
			for (Produtos pa : listaPeca) {
				bw.write(pa.getMarca() + ", " + pa.getModalidade() + ", " + pa.getSerie() + ", " + pa.getQuantidade()
						+ ", " + pa.getPreco() + ", " + pa.total());
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Produtos buscarPeca(List<Produtos> listaProdutos, String serie) {
		for (Produtos p : listaProdutos) {
			if (p.getSerie().equalsIgnoreCase(serie)) {
				return p;
			}
		}
		return null;
	}

	public static void main(String[] args) throws DomainException {

		Locale.setDefault(Locale.US);
		Scanner sc = new Scanner(System.in);
		List<Produtos> listaProdutos = new ArrayList<>();

		boolean cricaoSubPasta = new File("c:\\temp\\crudCadastroPecas").mkdir();
		if (cricaoSubPasta == true) {
			System.out.println("Pasta criada com sucesso!!");
		}

		String path = "c:\\temp\\crudCadastroPecas\\crudPecas.csv";
		boolean arquivoExistente = new File(path).exists();

		int opcao;
		do {
			try {
				System.out.println("\n=== MENU DE CADASTRO ===");
				System.out.println("1 - Lista de peças");
				System.out.println("2 - Cadastro de peças");
				System.out.println("3 - Atualizar peças");
				System.out.println("4 - Excluir peças");
				System.out.println("0 - Sair");
				System.out.print("Escolha um opção: ");
				opcao = sc.nextInt();
			} catch (InputMismatchException e) {
				System.err.println("Error: Digite apenas números");
				sc.nextLine();
				opcao = -1;
			}

			switch (opcao) {
			case 1:
				// Monstrar lista de peças
				leituraArquivo(path, listaProdutos);
				break;
			case 2:
				// cadastro de peças
				char resp;
				try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {
					if (!arquivoExistente) {
						bw.write("marca, modalidade, série, quantidade, preco, total");
						bw.newLine();
					}
					do {
						try {
							sc.nextLine();
							System.out.print("\nMarca: ");
							String marca = sc.nextLine();
							System.out.print("Modalidade: ");
							String modalidade = sc.nextLine();
							System.out.print("Série: ");
							String serie = sc.nextLine();
							System.out.print("Quantidade: ");
							Integer quantidade = sc.nextInt();
							System.out.print("Preço: ");
							Double preco = sc.nextDouble();
							sc.nextLine();

							Produtos peca = new Produtos(marca, modalidade, serie, quantidade, preco);
							listaProdutos.add(peca);

							bw.write(peca.getMarca() + ", " + peca.getModalidade() + ", " + peca.getSerie() + ", "
									+ peca.getQuantidade() + ", " + peca.getPreco() + ", " + peca.total());
							bw.newLine();
						} catch (DomainException e) {
							System.out.println(e);
						}

						System.out.print("Deseja continuar (s/n): ");
						resp = sc.next().toLowerCase().charAt(0);
					} while (resp != 'n');
					System.out.println("\nCadastro(s) finalizado(s)!");

				} catch (IOException e) {
					System.err.println("Error: " + e.getMessage());
				}
				break;

			case 3:
				// atualizar
				leituraArquivo(path, listaProdutos);
				System.out.print("Digite a série da peça que deseja atualizar: ");
				sc.nextLine();
				String serieBusca = sc.nextLine().toLowerCase().trim();

				Produtos peca = buscarPeca(listaProdutos, serieBusca);

				if (peca != null) {
					boolean flagParada = false;
					while (flagParada == false) {
						try {
							System.out.print("Qual campo você deseja atualizar(0-Marca, 1-Modalidade, 2-Série, 3-Quantidade, 4-Preço)?: ");
							int campo = sc.nextInt();
							while (campo < 0 || campo > 4) {
								System.err.println("Erro: Opção inválida! Escolha de 0 a 4");
								System.out.print("Qual campo você deseja atualizar(0-Marca, 1-Modalidade, 2-Série, 3-Quantidade, 4-Preço)?: ");
								campo = sc.nextInt();
							}
							sc.nextLine();
							switch (campo) {
							case 0:
								System.out.print("Nova marca: ");
								peca.setMarca(sc.nextLine());
								break;
							case 1:
								System.out.print("Nova modalidade: ");
								peca.setModalidade(sc.nextLine());
								break;
							case 2:
								System.out.print("Nova serie: ");
								peca.setSerie(sc.nextLine());
							case 3:
								System.out.print("Nova quantidade: ");
								peca.setQuantidade(sc.nextInt());
								break;
							case 4:
								System.out.print("Novo preço: ");
								peca.setPreco(sc.nextDouble());
								break;
							}

							flagParada = true;
							salvamentoArquivo(path, listaProdutos);
							System.out.println("Item Atualizado com sucesso!");
						} catch (DomainException e) {
							System.err.println(e.getMessage());
						} catch (InputMismatchException e) {
							System.err.println("Error: selecione o campo correto!");
							sc.nextLine();
						}
					}
				} else {
					System.out.print("\nSérie não localizada no sistema.\n");
				}
				break;
			case 4:
				// excluir
				leituraArquivo(path, listaProdutos);
				System.out.print("Qual série você deseja excluir: ");
				sc.nextLine();
				serieBusca = sc.nextLine().toLowerCase().trim();

				peca = buscarPeca(listaProdutos, serieBusca);

				if (peca != null) {
					listaProdutos.remove(peca);
					salvamentoArquivo(path, listaProdutos);
					System.out.println("Item excluído com sucesso!");
				} else {
					System.out.println("Série não localizada.");
				}
				break;
			case 0:
				System.out.println("Saindo do sistema...");
				break;
			default:
				System.out.println("Opção inválida! Tente novamente.");
				break;
			}

		} while (opcao != 0);

		sc.close();

	}

}
