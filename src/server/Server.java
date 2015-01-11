package server;
import MyPackage.Package;

import java.net.*;
import java.io.*;

public class Server {
	Package pack;
	public Server(Socket connection) throws Exception{
		pack = new Package(connection);
	}
	public void PutFileInfo(String file_path) throws Exception{
		File file = new File(file_path);
		if(file.exists())
		{
			pack.sendStringUTF(file.getName());
			pack.sendStringUTF(file.getPath());
			pack.sendStringUTF(file.getAbsolutePath());
			pack.sendStringUTF(file.getParent());
			pack.sentLong(file.length());
			pack.sentLong(file.lastModified());
			pack.sendInt(file.isDirectory()==true?1:0);
		}
	}
	
	public void PutDirectoryInfo() throws Exception{
		File dir;
		String dire_path = pack.getStringUTF();
		
		dir = new File(dire_path);
		if(dir.isDirectory())
		{
			File[] fileArray = dir.listFiles();
			pack.sendInt(fileArray.length);
			for(int i = 0;i<fileArray.length;i++)			
				PutFileInfo(fileArray[i]);
		}
	}
	
	public void PutFileInfo(File file) throws Exception
	{
		if(file.exists())
		{
			pack.sendStringUTF(file.getName());
			pack.sendStringUTF(file.getPath());
			pack.sendStringUTF(file.getAbsolutePath());
			pack.sendStringUTF(file.getParent());
			pack.sentLong(file.length());
			pack.sentLong(file.lastModified());
			pack.sendInt(file.isDirectory()==true?Package.Directory:Package.File);
		}		
	}
	
	public void PutDosCmdResult() throws Exception{
		String command = pack.getStringUTF();
		
		Runtime r = Runtime.getRuntime();
		Process p = r.exec(command);
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String inline;
		while(true)
		{
			inline = br.readLine();
			if(inline==null)
			{
				pack.sendStringUTF(Integer.toString(Package.TransOK));
				break;				
			}
//			System.out.println(inline);
			pack.sendStringUTF(inline);		
		}
	}
}
