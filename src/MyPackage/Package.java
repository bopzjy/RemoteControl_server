package MyPackage;
import java.net.*;
import java.io.*;


public class Package {
	public final static int BufferSize = 8192;
	public final static int UPDATEFILE = 1;
	public final static int DOWNLOADFILE = 2;
	public final static int BrowseFile = 3;
	public final static int BrowseFloder = 4;
	public final static int DosCmd = 5;	
	public final static int Directory = 11;
	public final static int File = 12;
	public final static int TransOK = 65530;
	
	private int ptype;
	//public String content,response;
	
	private Socket connection;
	private DataOutputStream out;
	private DataInputStream in;
	public Package(Socket _connection) {
		connection = _connection;
		try {
			out = new DataOutputStream(connection.getOutputStream());
			in = new DataInputStream(connection.getInputStream());
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("in Constructor,Exception:" + e);
		}
		
	}
	
	public void setPackageType(int _type){
		ptype = _type;
	}
	
	public int getPackageType() {
		return this.ptype;
	}
	
	public String getStringUTF() throws Exception{
		return in.readUTF();
	}
	
	public void sendStringUTF(String str2send) throws Exception {
		out.writeUTF(str2send);
		out.flush();
	}
	
	public long getLong() throws Exception {
		return in.readLong();
	}
	
	public void sentLong(long long2send) throws Exception{
		out.writeLong(long2send);
		out.flush();
	}
	
	public void sendInt(int int2send) throws Exception{
		out.write(int2send);
		out.flush();
	}
	
	public int getInt() throws Exception{
		return in.read();
	}
	
	public void sendPackageType()
	{
		try
		{
			out.write(ptype);
			out.flush();
		}catch(IOException ioe){
			System.out.println("in sending package,IOException:" + ioe);
		}
	}
	
	public int receivePackageType()
	{
		try {
			ptype = in.read();
		} catch (IOException ioe) {
			System.out.println("in receiving package,IOException:" + ioe);
		}
		return ptype;
	}
	
	public void UpdateFile(String file_path)
	{
		try {
			DataInputStream fis = new DataInputStream(  
	                new BufferedInputStream(new FileInputStream(file_path)));
			File fi = new File(file_path);
			
			out.writeUTF(fi.getName());  
            out.flush();  
            out.writeLong((long) fi.length());  
            out.flush();  
            
            byte[] buf = new byte[BufferSize];  
            while (true) {  
                int read = 0;  
                if (fis != null) {  
                    read = fis.read(buf);
                }  

                if (read == -1) {  
                    break;  
                }  
                out.write(buf, 0, read);
                out.flush();
            }
            
            fis.close();             
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("in UpdateFile,Exception:" + e);
		}	
	}
	
	public int DownloadFile(String file_path)
	{
		int passedlen = 0;
		int read = 0; 
		try {						
			String file_name = in.readUTF();
			long len = in.readLong();			
			file_path+=file_name;
			File fi = new File(file_path);

			if(fi.exists())
				fi.delete();
			fi.createNewFile();
			DataOutputStream fos = new DataOutputStream(  
                    new BufferedOutputStream(new FileOutputStream(file_path)));
			
			byte[] buf = new byte[BufferSize];
			
			while (true) {                   
                if (in != null) {  
                    read = in.read(buf);                      
                }                   
                if (read == -1 || read == 0) {  
                    break;  
                }  
                passedlen += read; 
                System.out.println("File has been received:" + (passedlen * 100 / len) + "%\n");
                fos.write(buf, 0, read);  
                if(passedlen==len)
                	break;
            }
			System.out.println("All data has been sended");			
			fos.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("in DownlaodFile,Exception:" + e);
		}	
		return passedlen;
	}
}
