package Progetto;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

/**
 * Servlet che restituisce un arraylist contenente tutti gli utenti con i rispettivi dettagli. Viene utilizzata quando l'admin vuole visualizzare, e nel caso modificare, i dettagli di altri utenti.
 */
public class UpdateUsersList extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String user = request.getSession().getAttribute("user").toString();
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.dbAccess();

        if(user!=null){ //controllo session e null pointer sql
            try {

                Statement stmt2 = conn.createStatement();
                ResultSet rss = stmt2.executeQuery("SELECT * FROM User");
                rss.absolute(0);

                ArrayList<User> arrayUsers = new ArrayList<>();
                while(rss.next()) {
                    User temp = new User();
                    temp.setUsername(rss.getString("Username"));
                    temp.setPassword(rss.getString("Password"));
                    temp.setEmail(rss.getString("Email"));
                    temp.setNome(rss.getString("Nome"));
                    temp.setCognome(rss.getString("Cognome"));
                    temp.setAdmin(rss.getString("Admin"));
                    arrayUsers.add(temp);
                }
                request.getSession().setAttribute("usersSet", arrayUsers);
                rss.close();
                stmt2.close();
                conn.close();
            } catch (SQLException e) {
                throw new IllegalArgumentException("Non sono stati rispettati i limiti di forma suggeriti a fianco di ogni casella di input.");
            }

            try {
                response.setContentType("text/html; charset=UTF-8");    //per la gestione dei carattere speciali
                response.setCharacterEncoding("UTF-8");      //per la gestione dei carattere speciali
                PrintWriter out = response.getWriter();
                out.write("1");
            } catch (IOException e) {
                throw new IllegalArgumentException(e.toString());
            }
        } else {
            throw new IllegalArgumentException("Devi effettuare il login");
        }
    }
}
