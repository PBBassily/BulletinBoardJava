

public class Start {
		
	public static void main (String[] args){
		Start invoker = new Start ();
		
		invoker.start();
		
	}

	private void start() {
		SSHChannelCreator sshCreator =  new SSHChannelCreator();
		String user = "mirna" , password = "mirna123",ip="192.168.1.189",command="./readerScript.sh";
		
		PropertiesParser propertiesParser = new PropertiesParser();
		
		//setupServer(sshCreator ,propertiesParser);
		setupClient(sshCreator , propertiesParser , 36,false);
		for(int i=0 ; i < propertiesParser.getReadersNum() ; i++){
//			setup client
		}
		for(int i=0 ; i < propertiesParser.getWritersNum() ; i++){
//			setup client
		}
		
	}


	private void setupClient(SSHChannelCreator sshChannelCreator ,PropertiesParser propertiesParser, int id , boolean isReader) {
		 
		String command = "cd BulletinBoard/client/;java Client"
				+" "+ propertiesParser.getServerIP()
				+" "+ propertiesParser.getServerPort()
				+" "+ isReader
				+" "+ id
				+" "+ propertiesParser.getAccessNum();
				
		System.out.println(command);
		sshChannelCreator.ssh("paula", "Neversaynever12", "localhost", command);
		
	}

	private void setupServer(SSHChannelCreator sshChannelCreator ,PropertiesParser propertiesParser) {
		
		//String command = "java Server "+propertiesParser.getServerPort();
		String command = "cd BulletinBoard/server/;java Server "+propertiesParser.getServerPort();
		sshChannelCreator.ssh("paula", "Neversaynever12", "localhost", command);
		
	}

}