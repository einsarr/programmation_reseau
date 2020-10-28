package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurMT extends Thread{
	private int nombreClient;
	public static void main(String[] args) {
		new ServeurMT().start();
	}
	@Override
	public void run() {
		try {
			ServerSocket ss = new ServerSocket(1457);
			System.out.println("Démarrage du serveur ...");
			while(true) {
				Socket socket = ss.accept();
				++nombreClient;
				new Conversation(socket,nombreClient).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class Conversation extends Thread{
		private Socket socket;
		private int numeroClient;
		public Conversation(Socket s,int num) {
			this.socket=s;
			this.numeroClient=num;

		}
		
		@Override
		public void run() {
			try {
				InputStream is = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				
				OutputStream os = socket.getOutputStream();
				PrintWriter pw = new PrintWriter(os,true);
				String IP = socket.getRemoteSocketAddress().toString();
				System.out.println("Connexion du client numéro "+numeroClient+" IP="+IP);
				System.out.println("Connexion du client numéro "+numeroClient);
				pw.println("Bienvenue vous etes le client numéro "+numeroClient);
				while(true) {
					String req = br.readLine();
					System.out.println("Le client "+IP+" a envoyé une requête");
					pw.println(req.length());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}

}
