import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class Server {

	private Semaphore rNumLock = new Semaphore(1);
	private Semaphore valLock = new Semaphore(1);

	private static BufferedWriter readersFile;
	private static BufferedWriter writersFile;

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
			this.rNum = rNum;

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
	public status readVal(int rId) {
		try {
			valLock.acquire();
		} catch (InterruptedException e) {
			System.out.println("Wait to acquire the read lock");
		}
		int sSeqCopy = ++sSeq;
		int oValCopy = oVal;

		// write into log file
		String st = String.format("%-10s %-10s %-10s %-10s%n", sSeqCopy, oValCopy, rId, rNum);
		writeIntoFile(readersFile, st);

		valLock.release();
		return new status(oValCopy, sSeqCopy, rNum);

	}

	/**
	 * Change the oVal Return sSeq
	 */
	public int writeVal(int newVal, int wId) {
		try {
			valLock.acquire();
		} catch (InterruptedException e) {
			System.out.println("wait to acquire the read lock");
		}
		oVal = newVal;
		int sSeqCopy = ++sSeq;

		// write into log file
		String st = String.format("%-10s %-10s %-10s%n", sSeqCopy, newVal, wId);
		writeIntoFile(writersFile, st);

		valLock.release();
		return sSeqCopy;

	}

	/**
	 * 
	 * @param fileWriter
	 * @param st
	 */
	public void writeIntoFile(BufferedWriter fileWriter, String st) {
		try {
			fileWriter.append(st);
			fileWriter.flush();

		} catch (IOException e) {
			System.out.println(e);
		}
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
	public static void main(String[] args) {

		Server server = new Server();
		int serverPortNum = Integer.parseInt(args[0]);
		System.out.println("port num is " + serverPortNum);
		int rSeq = 0;

		// start the server

		try {
			ServerSocket serverSocket = new ServerSocket(serverPortNum);

			System.out.println("server started successfully.");

			// output files
			readersFile = new BufferedWriter(new FileWriter("Readers") );
			readersFile.append("Readers\n");
			readersFile.append(String.format("%-10s %-10s %-10s %-10s%n", "sSeq" ,"oVal", "rId" ,"rNum"));

			writersFile = new BufferedWriter(new FileWriter("Writers"));
			writersFile.append("Writers\n");
			writersFile.append(String.format("%-10s %-10s %-10s%n", "sSeq" ,"oVal", "wId"));

			while (true) {
				System.out.println("waiting for client...");

				// create new TCP connection
				Socket clientSocket = serverSocket.accept();

				// start new thread
				new ServerListener(server, clientSocket, ++rSeq).start();

			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

}