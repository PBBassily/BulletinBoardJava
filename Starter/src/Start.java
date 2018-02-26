

public class Start {
		
	public static void main (String[] args){
		Start invoker = new Start ();		
		invoker.execute();	
	}
	
	
	private void execute() {

		PropertiesParser propertiesParser = new PropertiesParser();
		
		setupServer(propertiesParser);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int i=0;
		for( ; i < propertiesParser.getReadersNum() ; i++){
			System.out.println("********"+i);
			setupClient(propertiesParser , i,true);
		}
		for(; i < 2*propertiesParser.getWritersNum() ; i++){
			System.out.println("********"+i);
			setupClient(propertiesParser , i,false);
		}
		
	}


	private void setupClient(PropertiesParser propertiesParser, int id , boolean isReader) {
		 
		String command = "cd Desktop/BulletinBoardJava/Client/src;java Client"
				+" "+ propertiesParser.getServerIP()
				+" "+ propertiesParser.getServerPort()
				+" "+ isReader
				+" "+ id
				+" "+ propertiesParser.getAccessNum();
				
		String clientIP =isReader? propertiesParser.getReader(id):propertiesParser.getWriter(id-propertiesParser.getReadersNum()) ;   
		SSHChannelCreator sshChannelCreator = new SSHChannelCreator("mirna", "mirna123",clientIP  , command);
		sshChannelCreator.start();
		
		
		
		
	}

	private void setupServer(PropertiesParser propertiesParser) {
		
		String command = "cd Desktop/BulletinBoardJava/Server/src;java Server "+propertiesParser.getServerPort();
		SSHChannelCreator sshChannelCreator = new SSHChannelCreator("mirna", "mirna123", propertiesParser.getServerIP() , command);
		sshChannelCreator.start();
		
	}

}