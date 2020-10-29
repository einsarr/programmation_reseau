package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMT extends Thread{
	
	private boolean isActive=true;
	private int nombreClient=0;
	
	public static void main(String[] args) {
		new ServerMT().start();
		System.out.println("démarrage du serveur ...");
	}
	
	@Override
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(2345);
			while(isActive) {
				Socket socket = serverSocket.accept();
				++nombreClient;
				new Conversation(socket,nombreClient).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	class Conversation extends Thread{
		private int numero;
		private Socket socket;
		public Conversation(Socket socket,int num) {
			this.socket = socket;
			this.numero=num;
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
					String reponse = "Length = "+req.length();
					pw.println(reponse);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
