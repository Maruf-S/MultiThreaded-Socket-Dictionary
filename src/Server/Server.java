/**
 * @author Aaron-Qiu, mgsweet@126.com, student_id:1101584
 */
package Server;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private int port = 4444;
	private Dictionary dict;
	private ServerSocket server;
	private int numOfClient = 0;
	private ServerUI ui;
	public static void main(String[] args) {
		try {
			if (Integer.parseInt(args[0]) <= 1024 || Integer.parseInt(args[0]) >= 49151) {
				System.out.println("Invalid Port Number: Port number should be between 1024 and 49151!");
				System.exit(-1);
			}
			Server server = new Server(args[0], args[1]);
			server.run();
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Lack of Parameters:\nPlease run like \"java - jar Server.jar <port> <dictionary-file>\"!");
		} catch (NumberFormatException e) {
			System.out.println("Invalid Port Number: Port number should be between 1024 and 49151!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Server(String p, String path) {
		this.port = Integer.parseInt(p);
		this.dict = new Dictionary(path);
		this.ui = null;
		this.server = null;
	}
	public void displayOnConsoleAndScreen(String str) {
		System.out.println(str);
		if (ui != null) ui.getlogArea().append(str + '\n');
	}
	
	public void run() {
		try {
			this.server = new ServerSocket(this.port);
			System.out.println("Running on PORT " + port);
			System.out.println("Current IP address : " + InetAddress.getLocalHost());

			this.ui = new ServerUI(InetAddress.getLocalHost().getHostAddress(), String.valueOf(port), dict.getPath());
			ui.getFrame().setVisible(true);
			while(true) {
				Socket client = server.accept();
				numOfClient++;
				displayOnConsoleAndScreen("Server: A client connect.\n Number of clients: " + String.valueOf(numOfClient));
				WordRequestHandler dcThread = new WordRequestHandler(this, client, dict);
				dcThread.start();
			}
		} catch (BindException e) {
			System.out.println("Address already in use (Bind failed), try another address!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void onClientDisconnect() {
		displayOnConsoleAndScreen("Server: A client has disconnected");
		numOfClient--;
		displayOnConsoleAndScreen("Server: Number of clients: " + numOfClient + "\n");
	}
}
