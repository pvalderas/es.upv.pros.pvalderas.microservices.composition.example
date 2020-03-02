package main;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Main {

	private static final String exchange="composition";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		
		Scanner teclado=new Scanner(System.in);

		ConnectionFactory factory = new ConnectionFactory();
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(exchange, BuiltinExchangeType.TOPIC);
		
		String microservice, composition, message;
		
		System.out.println("Introduce microservice:");
		System.out.println("[INTRO (Default): Customers]");
		microservice=teclado.nextLine();
		if(microservice.length()==0) microservice="customers";
		
		System.out.println("Introduce composition ID:");
		System.out.println("[INTRO (Default): ProcessOrder]");
		composition=teclado.nextLine();
		if(composition.length()==0) composition="ProcessOrder";
		
		System.out.println("Introduce an event:");
		System.out.println("[INTRO (Default): ProcessPurchaseOrder]");
		message=teclado.nextLine();
		if(message.length()==0)  message="ProcessPurchaseOrder";
		
		String client=String.valueOf(System.currentTimeMillis());
		
		String routingKey=microservice+"."+composition.toLowerCase()+"."+client;
	
		
		String BPMNMessage=composition+"_"+message+"Message";
		String messageJSON="{\"message\":\""+BPMNMessage+"\",\"client\":\""+client+"\"}";
		
		if(message!="quit"){
			channel.basicPublish(exchange, routingKey, null, messageJSON.getBytes());
		}
		
		teclado.close();
		channel.close();
		connection.close();
		
	}

}
