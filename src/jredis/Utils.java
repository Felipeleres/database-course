package jredis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Utils {
	
	static Scanner teclado = new Scanner(System.in);

	public static String geraId(){
		Jedis conn = conectar();
		String chave =  conn.get("chave");

		if(chave != null){

			chave = String.valueOf(conn.incr("chave"));
			desconectar(conn);
			return chave;

		}else{

			conn.set("chave","1");
			desconectar(conn);
			return "1";
		}

	}

	public static Jedis conectar() {
		Jedis conn = new Jedis("localhost");
		return conn;
	}


	public static void desconectar (Jedis conn){
		conn.disconnect();
	}
	
	public static void listar() {
		Jedis conn = conectar();

		try{

			Set<String> res = conn.keys("produtos:*");

			if(res.size() >0) {
				System.out.println("Listando produtos...");
				System.out.println("--------------------");
				for(String chave:res){
					Map<String,String> produto = conn.hgetAll(chave);
					System.out.print("Id: "+chave);
					System.out.println("Produto: "+ produto.get("nome"));
					System.out.println("Preco: "+ produto.get("preco"));
					System.out.println("Estoque: "+ produto.get("estoque"));
					System.out.println("-------------------");
				}

			}else {

				System.err.println("Não existe produto cadastrado!!");
			}

		}catch (JedisConnectionException e){

			System.err.println("Verifique se a conexão com o redis esta ativa!");
		}

		desconectar(conn);

	}
	
	public static void inserir() {
		Jedis conn = conectar();

		System.out.println("Informe o nome do produto: ");
		String nome = teclado.nextLine();
		System.out.println("Informe o preço do produto:");
		String preco = teclado.nextLine();
		System.out.println("Informe a  quantidade em estoque:");
		String estoque = teclado.nextLine();

		Map<String,String> produto = new HashMap<String,String>();
		produto.put("nome",nome);
		produto.put("preco", preco);
		produto.put("estoque",estoque);

		String chave = "produto:"+ Utils.geraId();

		try{

			String res = conn.hmset(chave,produto);

			if( res != null){

				System.out.println("O produto "+ nome+" foi inserido com sucesso!");
			}else{

				System.out.println("Não foi possível inserir o produto!");
			}

		}catch (JedisConnectionException e){
			System.err.println("Verifique se o redis está ativo!");
			e.printStackTrace();
		}

		desconectar(conn);

	}
	
	public static void atualizar() {
		Jedis conn = conectar();

		System.out.println("Informe o nome do produto: ");
		String nome = teclado.nextLine();
		System.out.println("Informe o preço do produto:");
		String preco = teclado.nextLine();
		System.out.println("Informe a  quantidade em estoque:");
		String estoque = teclado.nextLine();

		System.out.println("Informe a chave do produto!");
		String chave = teclado.nextLine();

		Map<String,String> produto = new HashMap<String,String>();
		produto.put("nome",nome);
		produto.put("preco", preco);
		produto.put("estoque",estoque);

		try{

			String res = conn.hmset(chave,produto);

			if( res != null){

				System.out.println("O produto "+ nome+" foi atualizado com sucesso!");
			}else{

				System.out.println("Não foi possível atualizar o produto!");
			}

		}catch (JedisConnectionException e){
			System.err.println("Verifique se o redis está ativo!");
			e.printStackTrace();
		}

		desconectar(conn);

	}
	
	public static void deletar() {

		Jedis conn = conectar();

		System.out.println("Informe a chave do produto!");
		String chave = teclado.nextLine();

		try{
			Long ret = conn.del(chave);

			if(ret>0){
				System.out.println("O produto foi deletado com sucesso!");
			}else{
				System.out.println("Não existe produto com a chave informada");
			}

		}catch (JedisConnectionException e ){

			System.err.println("Verifique sua conexão!");
			e.printStackTrace();
		}

		desconectar(conn);

	}
	
	public static void menu() {
		System.out.println("==================Gerenciamento de Produto===============");
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
