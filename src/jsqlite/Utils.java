package jsqlite;

import java.sql.*;
import java.util.Scanner;

public class Utils {
	
	static Scanner teclado = new Scanner(System.in);

	public static Connection conectar() {
		String url = "jdbc:sqlite:src/jsqlite/jsqlite3.geek";

		try {

			Connection conn =  DriverManager.getConnection(url);
			String TABLE ="CREATE TABLE IF NOT EXISTS produtos("
					+"id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+"nome TEXT NOT NULL,"
					+"preco REAL NOT NULL,"
					+"estoque INTEGER NOT NULL);";

			Statement stmt = conn.createStatement();
			stmt.executeUpdate(TABLE);

			return conn;

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Não foi possível conectar ao sqlite");
			return null;
		}

	}


	public static void desconectar (Connection conn){

		try{
		if(conn != null){
			conn.close();
		}}
		catch (SQLException e){
			System.out.println("Erro ao fechar conexão!!");
		}
	}
	
	public static void listar() {
		String buscarTodos =  "SELECT * FROM produtos";
		try {

			Connection conn = conectar();
			PreparedStatement produtos = conn.prepareStatement(buscarTodos);
			ResultSet resultado = produtos.executeQuery();

				while(resultado.next()){

					System.out.println("----------Produto-----------");
					System.out.println("ID: " + resultado.getInt(1));
					System.out.println("Produto: " + resultado.getString(2));
					System.out.println("Preço: " + resultado.getFloat(3));
					System.out.println("Estoque: " + resultado.getInt(4));
					System.out.println("-------------------------------------");
				}

			produtos.close();
			desconectar(conn);

		}catch (Exception e){
			e.printStackTrace();
			System.err.println("Erro buscando produtos");
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

			int res = insercao.executeUpdate();

			if (res>0) {
				System.out.println("O produto foi inserido com sucesso!");

			}else{

				System.out.println("Não foi possivel inserir o produto!");
			}


			insercao.close();
			desconectar(conn);

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erro Inserindo produto!");
			System.exit(-42);
		}

	}
	
	public static void atualizar() {

		System.out.println("Informe o codigo do produto?");
		int id = Integer.parseInt(teclado.nextLine());

		try {

			Connection conn = conectar();
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

				int res = atualizando.executeUpdate();
				atualizando.close();
				conn.close();

				if(res>0) {
					System.out.println("O produto " + nome + " foi atualizado com sucesso!");
				}else{
					System.out.println("Não foi possível atualizar o produto com id "+id);
				}

				atualizando.close();
				desconectar(conn);

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
		try {

			Connection conn = conectar();
			PreparedStatement deletando = conn.prepareStatement(deletar);
			deletando.setInt(1, id);
			int res = deletando.executeUpdate();

			if (res >0) {
				System.out.println("O produto foi deletado com sucesso!");
			}else {
				System.out.println("Não foi possivel deletar o produto!");
			}

			deletando.close();
			desconectar(conn);

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
		if(opcao == 1) {
			listar();
		}else if(opcao == 2) {
			inserir();
		}else if(opcao == 3) {
			atualizar();
		}else if(opcao == 4) {
			deletar();
		}else {
			System.out.println("Opção inválida.");
		}
	}
}
