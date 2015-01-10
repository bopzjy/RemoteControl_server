package server;
import java.io.*;
import java.net.*;


public class MyServer {
	public static final int port = 8998; 
	public static void main(String[] args) {		
		try
		{
			ServerSocket server = new ServerSocket(port);
			System.out.println("Server started on " + server.getLocalPort());
			while(true)
			{
				Socket connection = server.accept();
				System.out.println("New conneciton moved to thread.");
				ServerThread handler = new ServerThread(connection);
			}
		}catch(IOException ioe){
			System.err.println("Error:" + ioe);
		}

	}

}
