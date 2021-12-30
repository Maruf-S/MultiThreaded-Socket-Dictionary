/**
 * @author Aaron-Qiu, mgsweet@126.com, student_id:1101584
 */
package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import States.States;

public class WordRequestHandler extends Thread{
	private Dictionary dict;
	private Socket clientSocket;
	private Server server;
	
	private String state2String(int state) {
		String s = "UnKnown";
		switch (state) {
		case States.QUERY:
			s = "QUERY";
			break;
		case States.ADD:
			s = "ADD";
			break;
		case States.REMOVE:
			s = "REMOVE";
			break;
		default:
			break;
		}
		return s;
	}
	
	public WordRequestHandler(Server server, Socket client, Dictionary dict) {
		this.server = server;
		this.clientSocket = client;
		this.dict = dict;
	}
	
	private JSONObject createResJSON(int state, String meaning) {
		JSONObject requestJson = new JSONObject();
		requestJson.put("state", String.valueOf(state));
		requestJson.put("meaning", meaning);
		return requestJson;
	}
	
	private JSONObject parseReqString(String res) {
		JSONObject reqJSON = null;
		try {
			JSONParser parser = new JSONParser();
			reqJSON = (JSONObject) parser.parse(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reqJSON;
	}
	
	@Override
	public void run() {
		try {
			DataInputStream reader = new DataInputStream(clientSocket.getInputStream());
			DataOutputStream writer = new DataOutputStream(clientSocket.getOutputStream());
			JSONObject reqJSON = parseReqString(reader.readUTF());
			int command = Integer.parseInt(reqJSON.get("command").toString());
			String word = (String) reqJSON.get("word");
			server.displayOnConsoleAndScreen("-- Get Request --\n  Command: " + state2String(command) + "\n  word: " + word);
			int state = States.FAIL;
			String meaning = (String) reqJSON.get("meaning");
			
			switch (command) {
			case States.QUERY:
				if (dict.isWordExist(word)) {
					Word leWord = dict.query(word);
					meaning = leWord.definition;
					state = States.SUCCESS;
					server.displayOnConsoleAndScreen("QUERY SUCCESS!");
				} else {
					state = States.FAIL;
					server.displayOnConsoleAndScreen("QUERY FAIL: Word Not Exist!");
				}
				writer.writeUTF(createResJSON(state, meaning).toJSONString());
				writer.flush();
				break;
			case States.ADD:
				if (!dict.isWordExist(word)) {
					dict.add(word, meaning);
					state = States.SUCCESS;
					server.displayOnConsoleAndScreen("ADD SUCCESS: " + word + "\nMeaning: " + meaning);
				} else {
					server.displayOnConsoleAndScreen("ADD FAIL: Word Exist!");
					state = States.FAIL;
				}
				writer.writeUTF(createResJSON(state, "").toJSONString());
				writer.flush();
				break;
			case States.REMOVE:
				if (dict.isWordExist(word)) {
					dict.remove(word);
					state = States.SUCCESS;
					server.displayOnConsoleAndScreen("REMOVE SUCCESS: " + word);
				} else {
					state = States.FAIL;
					server.displayOnConsoleAndScreen("ADD FAIL: Word Exist!");
				}
				writer.writeUTF(createResJSON(state, "").toJSONString());
				writer.flush();
				break;
			default:
				break;
			}
			reader.close();
			writer.close();
			clientSocket.close();
			this.server.onClientDisconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
