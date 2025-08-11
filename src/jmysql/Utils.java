package jmysql;

import java.sql.*;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Utils {
	
	static Scanner teclado = new Scanner(System.in);

	public static Connection conectar() {
		String classeDriver = "com.mysql.cj.jdbc.Driver";
		String username = "developergeek";
		String password ="1234567";
		String urlConexao ="jdbc:mysql://localhost:3306/jmysql?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

		try {
			Class.forName(classeDriver);
			Connection conn = DriverManager.getConnection(urlConexao, username, password);
			return conn;

		} catch (ClassNotFoundException e ){
			System.out.println("Driver jdbc não encontrado!!");
			e.printStackTrace();
		} catch(SQLException e ) {
			System.out.println("Erro ao conectar com banco!!");
			e.printStackTrace();
		}
		return null;
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
		}
	}
	
	public static void inserir() {
		System.out.println("Informe o nome do produto: ");
		String nome = teclado.nextLine();
		System.out.println("Informe o preço do produto:");
		float preco = teclado.nextFloat();
		System.out.println("Informe a  quantidade em estoque:");
		int estoque = teclado.nextInt();
		teclado.nextLine();

		String inserir = "INSERT INTO produtos (nome,preco,estoque) VALUES (?,?,?)";
		//evitar sql injection

		try{
			Connection conn = conectar();
			PreparedStatement salvar = conn.prepareStatement(inserir,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			salvar.setString(1,nome);
			salvar.setFloat(2,preco);
			salvar.setInt(3,estoque);
			salvar.executeUpdate();

			desconectar(conn);
			salvar.close();
			System.out.println("o produto "+ nome+" foi inserido com sucesso!");
		} catch (Exception e ){
			e.printStackTrace();
			System.err.println("Erro salvando produto");
		}

	}
	
	public static void atualizar() {
		System.out.println("Informe o código do produto: ");
		int id = Integer.parseInt(teclado.nextLine());
		String buscarPorId = "SELECT * FROM produtos WHERE id =?";

		try{
			Connection conn = conectar();
			PreparedStatement produto =  conn.prepareStatement(buscarPorId,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			produto.setInt(1,id);
			ResultSet res = produto.executeQuery();

			res.last();
			int qtd = res.getRow();
			res.beforeFirst();

			if (qtd>0){
				System.out.println("Digite o nome do produto:");
				String nome= teclado.nextLine();
				System.out.println("Digite o preco do produto:");
				float preco = teclado.nextFloat();
				System.out.println("Digite a quantidade em estoque");
				int estoque = teclado.nextInt();
				teclado.nextLine();

				String atualizar = "UPDATE produtos SET nome=?, preco=?,estoque=? WHERE id=?";

				PreparedStatement atualizando = conn.prepareStatement(atualizar);
				atualizando.setString(1,nome);
				atualizando.setFloat(2,preco);
				atualizando.setInt(3,estoque);
				atualizando.setInt(4,id);
				atualizando.executeUpdate();

				atualizando.close();
				desconectar(conn);

				System.out.println("O produto "+ nome+" com o id "+ id+" foi atualizado com sucesso");
			}else{

				System.out.println("Não existe produto com o id informado!");
			}

		}catch (Exception e ){
			e.printStackTrace();

		}

	}
	
	public static void deletar() {

		System.out.println("Informe o código do produto: ");
		int id = Integer.parseInt(teclado.nextLine());

		String deletar = "DELETE from produtos WHERE id=?";
		String buscarPorId = "SELECT * FROM produtos WHERE id =?";

		try{
			Connection conn = conectar();
			PreparedStatement produto =  conn.prepareStatement(buscarPorId,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			produto.setInt(1,id);
			ResultSet res = produto.executeQuery();

			res.last();
			int qtd = res.getRow();
			res.beforeFirst();

			if (qtd>0){

				PreparedStatement deletando = conn.prepareStatement(deletar);
				deletando.setInt(1,id);
				deletando.executeUpdate();

				deletando.close();
				desconectar(conn);

				System.out.println("O produto com o id "+ id+" foi deletado com sucesso");
			}else{

				System.out.println("Não existe produto com o id informado!");
			}
		}catch (Exception e ){
			e.printStackTrace();
			System.err.println("Erro ao deletar produto");
		}

	}
	
	public static void menu() {
		System.out.println("==================Gerenciamento de Produtos===============");
		System.out.println("Selecione uma opção: ");
		System.out.println("1 - Listar produtos.");
		System.out.println("2 - Inserir produtos.");
		System.out.println("3 - Atualizar produtos.");
		System.out.println("4 - Deletar produtos.");
		System.out.println("5 - Sair do sistema!!");
		
		int opcao = Integer.parseInt(teclado.nextLine());
		while(opcao != 5) {
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

			System.out.println("==================Gerenciamento de Produtos===============");
			System.out.println("Selecione uma opção: ");
			System.out.println("1 - Listar produtos.");
			System.out.println("2 - Inserir produtos.");
			System.out.println("3 - Atualizar produtos.");
			System.out.println("4 - Deletar produtos.");
			System.out.println("5 - Sair do sistema!!");

			opcao = Integer.parseInt(teclado.nextLine());

		}
	}
}
