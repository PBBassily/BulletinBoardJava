import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Server {

	private Semaphore rNumLock = new Semaphore(1);
	private Semaphore valLock = new Semaphore(1);

	public static List<int[]> readers = new ArrayList<int[]>();
	public static List<int[]> writers = new ArrayList<int[]>();

	private static int sSeq = 0;
	private static int rNum = 0;
	private static int oVal = -1;

	/**
	 * Class status
	 */
	public class status {
		private int oVal;
		private int sSeq;
		private int rNum;

		public status(int oVal, int sSeq, int rNum) {
			this.oVal = oVal;
			this.sSeq = sSeq;
			this.rNum= rNum;
					
		}

		public int getVal() {
			return oVal;
		}

		public int getServSeq() {
			return sSeq;
		}
		public int getReadersNum() {
			return rNum;
		}
	}

	/**
	 * Return status object containing oVal and sSeq
	 */
	public status readVal() {
		try {
			valLock.acquire();
		} catch (InterruptedException e) {
			System.out.println("Wait to acquire the read lock");
		}
		int sSeqCopy = ++sSeq;
		int oValCopy = oVal;
		valLock.release();
		return new status(oValCopy, sSeqCopy, rNum);

	}

	/**
	 * Change the oVal Return sSeq
	 */
	public int writeVal(int newVal) {
		try {
			valLock.acquire();
		} catch (InterruptedException e) {
			System.out.println("wait to acquire the read lock");
		}
		oVal = newVal;
		int sSeqCopy = ++sSeq;

		valLock.release();
		return sSeqCopy;

	}


	/**
	 * 
	 * @return
	 */
	public int incReadersNum() {

		try {
			rNumLock.acquire();
		} catch (InterruptedException e) {
			System.out.println("wait for lock acquire rNum");
		}
		int rNumCopy = ++rNum;
		rNumLock.release();
		return rNumCopy;

	}

	/**
	 * 
	 * @return
	 */
	public int decReadersNum() {

		try {
			rNumLock.acquire();
		} catch (InterruptedException e) {
			System.out.println("wait for lock acquire rNum");
		}
		int rNumCopy = rNum--;
		rNumLock.release();
		return rNumCopy;
	}

	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		Server server = new Server();
		int serverPortNum = Integer.parseInt(args[0]);
		System.out.println("port num is "+serverPortNum);
		int rSeq = 0;

		// start the server
		ServerSocket serverSocket = new ServerSocket(serverPortNum);
		System.out.println("server started successfully.");

		// output file
		// BufferedWriter writer = new BufferedWriter(new FileWriter("output"));

		while (true) {
			System.out.println("waiting for client...");

			// create new TCP connection
			Socket clientSocket = serverSocket.accept();

			// start new thread
			new ServerListener(server, clientSocket, ++rSeq).start();

		}
	}

}
