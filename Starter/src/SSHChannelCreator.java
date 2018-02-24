import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;


public class SSHChannelCreator {
	
public final int PORT = 22;
	
	public  void ssh(String user , String password , String host, String command) {

		try {
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, PORT);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			System.out.println("Establishing Connection...");
			session.connect();
			
			//System.out.println("Creating SFTP Channel.");
			ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
			sftpChannel.connect();
			//System.out.println("SFTP Channel created.");

			System.out.println("Connection established with "+user+" : "+host);
			// create the excution channel over the session

			ChannelExec channelExec = (ChannelExec) session.openChannel("exec");

			// Gets an InputStream for this channel. All data arriving in as messages from
			// the remote side can be read from this stream.

			InputStream in = channelExec.getInputStream();

			// Set the command that you want to execute

			// In our case its the remote shell script

			channelExec.setCommand(command);

			// Execute the command
			channelExec.connect();
			
			 BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			 String s;
			 while ((s = reader.readLine()) != null)
					System.out.println(s);
			 reader.close();

		} catch (Exception e) {
			System.err.print(e);
		}
	}

}
