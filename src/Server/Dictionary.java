/**
 * @author Aaron-Qiu, mgsweet@126.com, student_id:1101584
 */
package Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class Dictionary {
	private String path = "db.db";
	Database database ;
	
	public Dictionary(String path) {
		this.path = path;
		database = Database.getInstance(path);
	}
	public Dictionary(){

	}
	
	public String getPath() {
		return path;
	}

	
	public synchronized boolean isWordExist(String word) {
		Word found  =  database.getWord(word);
		return found != null;
	}
	
	public synchronized Word query(String word) {
		return  database.getWord(word);
	}
	
	public synchronized boolean add(String word, String definition) {
		if (isWordExist(word)) {
			return false;
		} else {
			database.insert(word,definition,"");
			return true;
		}
	}
	
	public synchronized boolean remove(String word) {
		if (isWordExist(word)) {
			database.delete(word);
			return true;
		} else {
			return false;
		}
	}
}












