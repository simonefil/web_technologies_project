package Progetto;

import java.sql.*;

/**
 * Classe utilizzata per stabilire una connessione al database MySQL tramite jdbc.
 */
public class DatabaseConnection {
    private static final String SERVER_IP = "127.0.0.1";
    private static final String SERVER_PORT = "3306";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "12345";
    private static final String DATABASE = "project";


    Connection dbAccess(){

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception e) {
            String message = e.getMessage();
            System.out.println(message);
        }

        Connection conn;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + SERVER_IP + ":" + SERVER_PORT + "/" + DATABASE + "?user=" + USERNAME + "&password=" + PASSWORD + "&useSSL=false");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Server NON raggiungibile al momento");
        }
        return (conn);
    }
}
