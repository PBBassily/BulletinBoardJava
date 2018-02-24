import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;


public class PropertiesParser {

	private InputStream inputStream = null; 
	private Properties properties = null ;
	
	public PropertiesParser () {
		
		properties = new Properties();
		
		try {
			inputStream = new FileInputStream("system.properties");	
			properties.load(inputStream);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getServerIP(){
		return properties.getProperty("RW.server");
		
	}
	public String getServerPort(){
		return properties.getProperty("RW.server.port");
		
	}
	public int getReadersNum(){
		String readersNum = properties.getProperty("RW.numberOfReaders");
		return Integer.parseInt(readersNum);
		
	}
	public String getReader(int i){
		return properties.getProperty("RW.reader"+i);
		
	}
	public int getWritersNum(){
		String writersNum = properties.getProperty("RW.numberOfWriters");
		
		return Integer.parseInt(writersNum);
		
	}
	public String getWriter(int i){
		return properties.getProperty("RW.writer"+i);
		
	}
	public int getAccessNum(){
		String accessNum = properties.getProperty("RW.numberOfAccesses");
		return Integer.parseInt(accessNum);
		
	}
	
	public static void main (String[] args){
		PropertiesParser parseTester = new PropertiesParser();
		System.out.println(parseTester.getServerIP());
		System.out.println(parseTester.getServerPort());
		System.out.println(parseTester.getReadersNum());
		System.out.println(parseTester.getReader(3));
		System.out.println(parseTester.getWritersNum());
		System.out.println(parseTester.getWriter(26));
		System.out.println(parseTester.getAccessNum());
	}
	
}
