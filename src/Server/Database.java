package Server;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static Database _instance;
    Connection connection;

    public static Database getInstance(String fileName) {
        if (_instance == null) {
            _instance = new Database(fileName);
        }
        return _instance;
    }

    public Database(String fileName) {
        try {
            // db parameters
            String url = "jdbc:sqlite:" + fileName;
            connection = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (Exception e) {
            System.out.println("Couldn't find the said database file... \n Creating a new one.");
            createNewDatabase(fileName);
            System.out.println("Creating a new database;");
        } finally {
            createTables();
        }
    }
    public void createNewDatabase(String fileName) {
        String url = "jdbc:sqlite:" + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
                connection = conn;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed creating a new Database file");
        }
    }
    public void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS wordDatabase (\n"
                + "	word text PRIMARY KEY,\n"
                + "	definition text NOT NULL,\n"
                + "	addedBy text NOT NULL\n"
                + ");";
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean insert(String word, String definition, String addedBy) {
        String sql = "INSERT INTO wordDatabase(word,definition,addedBy) VALUES(?,?,?)";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, word);
            pstmt.setString(2, definition);
            pstmt.setString(3, addedBy);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Couldn't add your word");
            return false;
        }
    }
    public Word getWord(String word){
        String sql = "SELECT word, definition, addedBy "
                + "FROM wordDatabase WHERE word = ?";

        try {
            PreparedStatement pstmt  = connection.prepareStatement(sql);
            pstmt.setString(1, word);
            ResultSet rs  = pstmt.executeQuery();
            while (rs.next()) {
                return new Word(rs.getString("word"),rs.getString("definition"),rs.getString("addedBy"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean delete(String word) {
        String sql = "DELETE FROM wordDatabase WHERE word = ?";

        try {
            PreparedStatement pstmt  = connection.prepareStatement(sql);
            pstmt.setString(1, word);
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return true;
        }
    }
}

class Word{
    public String word;
    public String definition;
    public String addedBy;

    public  Word(String word, String definition, String addedBy){
        this.word = word;
        this.definition = definition;
        this.addedBy = addedBy;
    }


}