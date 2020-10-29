package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.omg.PortableServer.RequestProcessingPolicy;


public class ServeurChat extends Thread{
	
	private boolean isActive=true;
	private int nombreClient=0;
	
	private List<Conversation> clients = new ArrayList<>();

	public static void main(String[] args) {
		new ServeurChat().start();
		System.out.println("démarrage du serveur ...");
	}
	
	@Override
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(4567);
			while(isActive) {
				Socket socket = serverSocket.accept();
				++nombreClient;
				Conversation conversation = new Conversation(socket,nombreClient);
				clients.add(conversation);
				conversation.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class Conversation extends Thread{
		public int numero;
		public Socket socket;
		public Conversation(Socket socket,int num) {
			this.socket = socket;
			this.numero=num;
		}
		public void broadcastMessage(String message,Socket sockett,int numeroClient) {
			try {
				for(Conversation client : clients) {
					if(client.socket!=sockett) {
						if(client.numero==numeroClient || numeroClient==-1 ) {
							PrintWriter printWriter = new PrintWriter(client.socket.getOutputStream(),true);
							printWriter.println(message);
						}
						
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@Override
		public void run() {
			try {
				InputStream inputStream = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(inputStream);
				BufferedReader br = new BufferedReader(isr);
				
				PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);
				String ipClient = socket.getRemoteSocketAddress().toString();
				pw.println("Bienvenue, vous êtes le client numéro "+numero);
				System.out.println("Connexion du client numéro "+numero+", IP = "+ipClient);
				while(true) {
					String req = br.readLine();
					if(req.contains("=")) {
						String[] requestParams = req.split("=");
						if(requestParams.length==2);
							String message = requestParams[1];
						int numeroClient = Integer.parseInt(requestParams[0]);
						broadcastMessage(message, socket,numeroClient);
					}else {
						broadcastMessage(req, socket,-1);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
