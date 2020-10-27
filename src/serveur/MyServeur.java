package serveur;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServeur {

	public static void main(String[] args) throws Exception {
		ServerSocket ss = new ServerSocket(1234);
		System.out.println("J'attends la connexion");
		Socket s = ss.accept();
		System.out.println("Adresse ip du client "+s.getRemoteSocketAddress());
		InputStream is = s.getInputStream();
		OutputStream os = s.getOutputStream();
		System.out.println("J'attends que le client envoie un octet");
		int nb = is.read();
		System.out.println("J'ai reçu un nombre");
		int res = nb*5;
		System.out.println("J'ai reçu un nombre"+res);
		os.write(res);
		s.close();
	}

}
