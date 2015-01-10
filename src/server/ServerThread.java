package server;
import java.net.*;
import java.io.*;

import MyPackage.Package;
public class ServerThread extends Thread 
{
	private Socket connection;
	public ServerThread(Socket _connection) 
	{
		this.connection = _connection;
		start();
	}
	

	public void run()
	{
		Package mypack = new Package(connection);
		int ptype = mypack.receivePackageType();
		switch (ptype) {
		case Package.UPDATEFILE:
			System.out.println("Client is sending file");
			mypack.DownloadFile("D:\\KuGou\\");
			System.out.println("I am a test!!");
			mypack.setPackageType(Package.TransOK);
			mypack.sendPackageType();
			
			break;

		default:
			break;
		}

	}
}
