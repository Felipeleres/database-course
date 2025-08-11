package jpostgresql;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Utils {
	
	static Scanner teclado = new Scanner(System.in);

	public static Connection conectar(){
		Properties props = new Properties();
		props.setProperty("user","geek");
		props.setProperty("password","1234567");
		props.setProperty("ssl","false");
		String url = "jdbc:postgresql://localhost:5432/jpostgresql";

		try {

			return DriverManager.getConnection(url,props);

		}catch (Exception e){

			e.printStackTrace();
			if(e instanceof ClassNotFoundException){
				System.err.println("Verifique o driver de conexão!");
			}else{
				System.err.println("Verifique se o servidor está ativo!");
			}

			return null;
		}

	}


	public static void desconectar (Connection conn){

		try{
		if(conn != null){
			conn.close();
		}
		} catch (SQLException e){
			System.out.println("Erro ao fechar conexão!!");
		}
	}
	
	public static void listar() {
		String buscarTodos =  "SELECT * FROM produtos";
		try {

			Connection conn = conectar();
			PreparedStatement produtos = conn.prepareStatement(buscarTodos, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet resultado = produtos.executeQuery();

			resultado.last();
			int qtd = resultado.getRow();
			resultado.beforeFirst();

			if (qtd> 0){
				System.out.println("Listando produtos....");
				System.out.println("---------------------");
				while(resultado.next()){

					System.out.println("ID: " + resultado.getInt(1));
					System.out.println("Produto: " + resultado.getString(2));
					System.out.println("Preço: " + resultado.getFloat(3));
					System.out.println("Estoque: " + resultado.getInt(4));
					System.out.println("-------------------------------------");
				}


			}else{
				System.out.println("Não existem produtos cadastrados!!");
			}
			produtos.close();
			desconectar(conn);

		}catch (Exception e){
			e.printStackTrace();
			System.err.println("Erro buscando produtos");
			System.exit(-42);
		}


	}
	
	public static void inserir() {
		System.out.println("Informe o nome do produto");
		String produto = teclado.nextLine();
		System.out.println("Informe o preço");
		float preco  = Float.parseFloat(teclado.nextLine());
		System.out.println("Digite a quantidade em estoque");
		int estoque  = Integer.parseInt(teclado.nextLine());

		String inserir ="INSERT INTO produtos (nome,preco,estoque) VALUES (?,?,?)";

		try {

			Connection conn = conectar();
			PreparedStatement insercao = conn.prepareStatement(inserir);

			insercao.setString(1, produto);
			insercao.setFloat(2,preco);
			insercao.setInt(3,estoque);

			insercao.executeUpdate();

			insercao.close();
			desconectar(conn);
			System.out.println("O produto foi inserido com sucesso!");


		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erro Inserindo produto!");
			System.exit(-42);
		}


	}
	
	public static void atualizar() {
		System.out.println("Informe o codigo do produto?");
		int id = Integer.parseInt(teclado.nextLine());

		String buscarPorId = "SELECT * FROM produtos WHERE id=?";

		Connection conn = conectar();

		try {

			PreparedStatement atualizar = conn.prepareStatement(buscarPorId, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			atualizar.setInt(1,id);

			ResultSet result = atualizar.executeQuery();

			result.last();
			int qtd = result.getRow();
			result.beforeFirst();

			if(qtd >0){

				System.out.println("Informe o nome do produto!");
				String nome = teclado.nextLine();

				System.out.println("Informe o preço do produto!");
				float preco = Float.parseFloat(teclado.nextLine());

				System.out.println("Informe a quantidade em estoque!");
				int estoque = teclado.nextInt();

				String consulta = "UPDATE produtos SET nome=?, preco=?, estoque=? WHERE id=?";

				PreparedStatement atualizando = conn.prepareStatement(consulta);

				atualizando.setString(1,nome);
				atualizando.setFloat(2, preco);
				atualizando.setInt(3,estoque);
				atualizando.setInt(4,id);

				atualizando.executeUpdate();
				atualizando.close();
				conn.close();

				System.out.println("O produto "+nome+ " foi atualizado com sucesso!");


			}else {

				System.out.println("O produto não foi encontrado!");
			}


		}catch (Exception e ){
			e.printStackTrace();
			System.err.println("Não foi possível atualizar o produto!");
			System.exit(-42);
		}


	}
	
	public static void deletar() {

		String deletar = "DELETE FROM produtos WHERE id=?";

		System.out.println("Informe o codigo do produto?");
		int id = Integer.parseInt(teclado.nextLine());

		String buscarPorId = "SELECT * FROM produtos WHERE id=?";

		Connection conn = conectar();

		try {

			PreparedStatement consultar = conn.prepareStatement(buscarPorId, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			consultar.setInt(1, id);

			ResultSet result = consultar.executeQuery();

			result.last();
			int qtd = result.getRow();
			result.beforeFirst();

			if (qtd > 0) {

				PreparedStatement deletando = conn.prepareStatement(deletar);
				deletando.setInt(1, id);
				deletando.executeUpdate();

				deletando.close();
				desconectar(conn);

				System.out.println("O produto foi deletado com sucesso!");
			} else {

				System.out.println("Não existe produto com o id cadastrado!");
			}


		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erro ao deletar produto!");
		}
	}

		public static void menu() {
			System.out.println("==================Gerenciamento de Produtos===============");
			System.out.println("Selecione uma opção: ");
			System.out.println("1 - Listar produtos.");
			System.out.println("2 - Inserir produtos.");
			System.out.println("3 - Atualizar produtos.");
			System.out.println("4 - Deletar produtos.");

			int opcao = Integer.parseInt(teclado.nextLine());
			if (opcao == 1) {
				listar();
			} else if (opcao == 2) {
				inserir();
			} else if (opcao == 3) {
				atualizar();
			} else if (opcao == 4) {
				deletar();
			} else {
				System.out.println("Opção inválida.");
			}
		}
	}
