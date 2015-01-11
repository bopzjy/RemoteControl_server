package server;
import java.net.*;
import java.io.*;

import MyPackage.Package;
public class ServerThread extends Thread 
{
	private Socket connection;
	private Server sr;
	public ServerThread(Socket _connection) 
	{

		try
		{
			sr = new Server(_connection);
			this.connection = _connection;
			start();
		}catch(Exception e){
			System.err.println("ServerThread.ServerThread()" + e);
		}
	}
	

	public void run()
	{
		try{
			Package mypack = new Package(connection);
			int ptype = mypack.receivePackageType();
			switch (ptype) {
			case Package.UPDATEFILE:
				System.out.println("Client is sending file");
				mypack.DownloadFile("D:\\KuGou\\");
				System.out.println("I am a test!!");
				mypack.setPackageType(Package.TransOK);
				mypack.sendPackageType();
				
			case Package.BrowseFile:
				System.out.println("Client is browsing file");
				String file_path = mypack.getStringUTF();
				sr.PutFileInfo(file_path);
				break;
			case Package.BrowseFloder:
				System.out.println("Client is browsing floder!!");
				//String dir_path = mypack.getStringUTF();
				sr.PutDirectoryInfo();
				break;
	
			case Package.DosCmd:
				System.out.println("Client is requesting exec dos cmd");
				sr.PutDosCmdResult();
				break;
			default:
				break;
			}
		}catch(Exception e){
			System.err.println("ServerThread.run():" + e);
		}

	}
}
