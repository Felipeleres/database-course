package jfirebase;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;
import java.util.Scanner;

public class Utils {
	
	static Scanner teclado = new Scanner(System.in);

	public static HttpClient conectar() {

		HttpClient conn = HttpClient.newBuilder().build();
		return conn;
	}

	
	public static void listar() {

		HttpClient conn  = conectar();

		String link = "https://guniversity-1215-default-rtdb.firebaseio.com/produtos.json";

		HttpRequest requisicao =  HttpRequest.newBuilder().uri(URI.create(link)).build();

		try{

			HttpResponse<String> resposta =  conn.send(requisicao, HttpResponse.BodyHandlers.ofString());

			if(resposta.body().equals("null")){
				System.out.println("Não existem produtos cadastrados");
			}else{
				JSONObject obj =  new JSONObject(resposta.body());
				System.out.println("Listando produtos....");
				System.out.println(".......................");
				for(int i = 0; i < obj.length(); i++){
					JSONObject prod =  (JSONObject) obj.get(obj.names().getString(i));
					System.out.println("Id: "+obj.names().getString(i));
					System.out.println("Produto: "+ prod.get("nome"));
					System.out.println("preço: "+ prod.get("preco"));
					System.out.println("Estoque: "+ prod.get("estoque"));
					System.out.println("-------------------------------");
				}
			}

		}catch(IOException e ){

			System.out.println("Houve um erro na conexao");
			e.printStackTrace();
		}catch(InterruptedException e){
			System.out.println("Houve um erro na conexao");
			e.printStackTrace();
		}

	}
	
	public static void inserir() {

		HttpClient conn =  conectar();
		String link = "https://guniversity-1215-default-rtdb.firebaseio.com/produtos.json";

		System.out.println("Informe o nome do produto: ");
		String nome = teclado.nextLine();
		System.out.println("Informe o preço do produto:");
		float preco = teclado.nextFloat();
		System.out.println("Informe a  quantidade em estoque:");
		int estoque = teclado.nextInt();

		JSONObject nproduto = new JSONObject();

		nproduto.put("nome",nome);
		nproduto.put("preco",preco);
		nproduto.put("estoque",estoque);

		HttpRequest requisicao = HttpRequest.newBuilder().uri(URI.create(link))
				.POST(HttpRequest.BodyPublishers.ofString(nproduto.toString()))
				.header("Content-Type","application/json").build();

		try {

			HttpResponse<String> resposta =  conn.send(requisicao, HttpResponse.BodyHandlers.ofString());

			JSONObject obj = new JSONObject(resposta.body());

			if(resposta.statusCode()==200){
				System.out.println("O produto foi cadastrado com sucesso");

			}else{
				System.out.println(obj);
				System.out.println("Status "+ resposta.statusCode());
			}

		}catch (IOException e){

			System.out.println("Houve um erro de conexão");
			e.printStackTrace();
		}catch (InterruptedException e) {

			System.out.println("Houve um erro de conexão");
			e.printStackTrace();
		}

	}
	
	public static void atualizar() {

		HttpClient conn = conectar();

		System.out.println("Informe o id do produto: ");
		String id = teclado.nextLine();

		System.out.println("Informe o nome do produto: ");
		String nome = teclado.nextLine();
		System.out.println("Informe o preço do produto:");
		float preco = teclado.nextFloat();
		System.out.println("Informe a  quantidade em estoque:");
		int estoque = teclado.nextInt();


		String link  = "https://guniversity-1215-default-rtdb.firebaseio.com/produtos/"+ id+".json";

		JSONObject nproduto = new JSONObject();

		nproduto.put("nome",nome);
		nproduto.put("preco",preco);
		nproduto.put("estoque",estoque);

		HttpRequest requisicao =  HttpRequest.newBuilder().uri(URI.create(link))
				.PUT(HttpRequest.BodyPublishers.ofString(nproduto.toString()))
				.header("Content-Type","application/json").build();

		try {

			HttpResponse<String> resposta =  conn.send(requisicao, HttpResponse.BodyHandlers.ofString());

			JSONObject obj = new JSONObject(resposta.body());

			if(resposta.statusCode()==200){
				System.out.println("O produto foi atualizado com sucesso");
				System.out.println(resposta.body());

			}else{
				System.out.println(obj);
				System.out.println("Status "+ resposta.statusCode());
			}

		}catch (IOException e){

			System.out.println("Houve um erro de conexão");
			e.printStackTrace();
		}catch (InterruptedException e) {

			System.out.println("Houve um erro de conexão");
			e.printStackTrace();
		}




	}
	
	public static void deletar() {
		HttpClient conn = conectar();

		System.out.println("Informe o id do produto: ");
		String id = teclado.nextLine();

		String link  = "https://guniversity-1215-default-rtdb.firebaseio.com/produtos/"+ id+".json";

		HttpRequest requisicao = HttpRequest.newBuilder().uri(URI.create(link)).DELETE().header("Content-Type","application/json").build();

		try{

			HttpResponse<String> resposta = conn.send(requisicao, HttpResponse.BodyHandlers.ofString());

			if(resposta.statusCode()==200 && !resposta.body().equals("null")){
				System.out.println("O produto foi deletado com sucesso!");
			}else{
				System.out.println("Não existe produto com o ID "+ id);
			}

		}catch(IOException e ){
			System.out.println("Houve erro na conexão");
			e.printStackTrace();
		}catch (InterruptedException e){
			System.out.println("Houve erro na requisição");
			e.printStackTrace();
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
