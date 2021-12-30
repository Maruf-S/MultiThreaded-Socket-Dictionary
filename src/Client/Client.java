/**
 * @author Aaron-Qiu, mgsweet@126.com, student_id:1101584
 */
package Client;

import java.util.concurrent.TimeoutException;


import States.States;

public class Client {
	private String address;
	private int port;
	private int operationCount = 0;
	private ClientUI ui;
	public static void main(String[] args) {
		try {
			// Check port format.
			if (Integer.parseInt(args[1]) <= 1024 || Integer.parseInt(args[1]) >= 49151) {
				System.out.println("Invalid Port Number: Port number should be between 1024 and 49151!");
				System.exit(-1);
			}
			System.out.println("Dictionary Client");
			Client client = new Client(args[0], Integer.parseInt(args[1]));
			client.run();
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Lack of Parameters:\nPlease run like \"java -java Client.java <server-adress> <server-port>");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("Invalid Port Number: Port number should be between 1024 and 49151!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Client(String address, int port) {
		this.address = address;
		this.port = port;
		this.operationCount = 0;
		ui = null;
	}
	
	public void run() {
		try {
			this.ui = new ClientUI(this);
			ui.getFrame().setVisible(true);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Please enter <server-adress> <server-port>");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addLog(int state, String word, String meaning) {
		System.out.println("--LOG: " + String.valueOf(operationCount) + " ------");
		System.out.println("  Request:");
		switch (state) {
		case States.ADD:
			System.out.println("  Command: ADD");
			break;
		case States.QUERY:
			System.out.println("  Command: QUERY");
			break;
		case States.REMOVE:
			System.out.println("  Command: REMOVE");
			break;
		default:
			System.out.println("  Error: Unknown Command");
			break;
		}
		System.out.println("  Word: " + word);
		if (state == States.ADD) System.out.println("  Meaning:\n\t" + meaning);
		operationCount++;
	}
	
	private void printResponse(int state, String meaning) {
		System.out.println("  Response:");
		switch (state) {
		case States.SUCCESS:
			System.out.println("  State: SUCCESS");
			break;
		case States.FAIL:
			System.out.println("  State: FAIL");
			break;
		default:
			System.out.println("  Error: Unknown State");
			break;
		}
		System.out.println("  Meaning:\n\t" + meaning);
	}
	
	public int add(String word, String meaning) {
		String[] resultArr = execute(States.ADD, word, meaning);
		return Integer.parseInt(resultArr[0]);
	}
	
	public int remove(String word) {
		String[] resultArr = execute(States.REMOVE, word, "");
		return Integer.parseInt(resultArr[0]);
	}
	
	public String[] query(String word) {
		String[] resultArr = execute(States.QUERY, word, "");
		return resultArr;
	}
	
	private String[] execute(int command, String word, String meaning) {
		int state = States.FAIL;
		addLog(command, word, meaning);
		try {
			System.out.println("Trying to connect to server...");
			ExecuteThread eThread = new ExecuteThread(address, port, command, word, meaning);
			eThread.start();
			eThread.join(2000);
			if (eThread.isAlive()) {
				eThread.interrupt();
				throw new TimeoutException();
			}
			String[] eThreadResult = eThread.getResult();
			state = Integer.parseInt(eThreadResult[0]);
			meaning = eThreadResult[1];
			System.out.println("Connect Success!");
		} catch (TimeoutException e) {
			state = States.TIMEOUT;
			meaning = "";
		} catch (Exception e) {
			e.printStackTrace();
		}
		printResponse(state, meaning);
		String[] resultArr = {String.valueOf(state), meaning};
		return resultArr;
	}
}
