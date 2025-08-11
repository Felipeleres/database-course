package jbase;

import java.sql.*;
import java.util.Scanner;

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
			System.out.println("Conexão bem sucedida!!");
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
		System.out.println("Inserindo produtos...");
	}
	
	public static void atualizar() {
		System.out.println("Atualizando produtos...");
	}
	
	public static void deletar() {
		System.out.println("Deletando produtos...");
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
