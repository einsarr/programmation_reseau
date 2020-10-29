package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ServeurJeu extends Thread{

	private boolean isActive=true;
	private int nombreClient=0;
	private int nombreSecret;
	private boolean fin;
	private String gagnant;
	
	public static void main(String[] args) {
		new ServeurJeu().start();
		System.out.println("démarrage du serveur ...");
	}
	
	@Override
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(3456);
			nombreSecret = new Random().nextInt(1000);
			System.out.println("Le serveur a choisi son secret : "+nombreSecret);
			while(isActive) {
				Socket socket=serverSocket.accept();
				++nombreClient;
				new Conversation(socket,nombreClient).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class Conversation extends Thread {
		private Socket socket;
		private int numero;
		public Conversation(Socket socket,int numero) {
			this.socket = socket;
			this.numero = numero;
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
				pw.println("Devinez le nombre secret ....");
				
				while(true) {
					String req = br.readLine();
					int nombre=0;
					boolean correctFormaRequest=false;
					try {
						nombre = Integer.parseInt(req);
						correctFormaRequest=true;
					} catch (NumberFormatException e) {
						correctFormaRequest=false;
					}
					
					if(correctFormaRequest) {
						System.out.println("Client "+ipClient+" Tentative avec le nombre :"+nombre);
						if(fin==false) {
							if(nombre > nombreSecret) {
								pw.println("Votre nombre est supéieur au nombre secret");
							}else if(nombre < nombreSecret){
								pw.println("Votre nombre est inférieur au nombre secret");
							}else {
								pw.println("BRAVO, vous avez gagné");
								gagnant=ipClient;
								System.out.println("Bravo au gagnant, IP client :"+gagnant);
							}
						}else{
							pw.println("Jeu terminé, le gagnant est "+gagnant);
						}
					}else {
						pw.println("Format de nombre incorrecte");
					}
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
