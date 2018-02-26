import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class Client {

	public static void main(String[] args) {

		String serverIP = args[0];
		int serverPortNum = Integer.parseInt(args[1]);
		boolean isReader = (Boolean.parseBoolean(args[2])); // true: reader , false: writer
		int clientNum = Integer.parseInt(args[3]);
		int numberOfAccess = Integer.parseInt(args[4]);

		System.out.println("client ID : " + clientNum + "\nserver IP : " + serverIP + "\nserver port :" + serverPortNum
				+ "\nreader : " + isReader + "\naccessNum : " + numberOfAccess);

		// Open output file
		BufferedWriter fileWriter = null;
		try {
			fileWriter = new BufferedWriter(new FileWriter("log" + clientNum));

			if (isReader)
				fileWriter.append("Client type: Reader\n");
			else
				fileWriter.append("Client type: Writer\n");
			fileWriter.append("Client Name: " + clientNum + "\n");

			if (isReader)
				fileWriter.append(String.format("%-10s %-10s %-10s%n", "rSeq", "sSeq", "oVal"));
			else
				fileWriter.append(String.format("%-10s %-10s%n", "rSeq", "sSeq"));
		} catch (IOException e) {
			System.out.println(e + "couldn't open output file");
		}

		//
		for (int i = 0; i < numberOfAccess; i++) {
			Socket socket;
			try {
				socket = new Socket(serverIP, serverPortNum);

				System.out.println("==> " + clientNum +"   connected to server successfully.");

				PrintWriter wtr = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String response;

				// Reader
				if (isReader) {
					// Send Request
					wtr.println("Read " + clientNum);

					// Read Response
					response = reader.readLine();
					System.out.println("==> " + clientNum +"   Message received : " + response);
				}
				// Writer
				else {
					int newVal = clientNum;
					// Send Request
					wtr.println("Write " + clientNum + " " + newVal);

					// Read Response
					response = reader.readLine();
					System.out.println("==> " + clientNum +"   Message received : " + response);

				}
				fileWriter.append(response + "\n");

			} catch (UnknownHostException e) {
				System.out.println(e + "==> " + clientNum +"   couldn't find server host");
			} catch (IOException e) {
				System.out.println(e + "==> " + clientNum +"   couldn't write into output file");

			}
		}
		try {
			fileWriter.close();
		} catch (IOException e) {
			System.out.println(e + "==> " + clientNum +"   couldn't close output file");
		}

	}
}