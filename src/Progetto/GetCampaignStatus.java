package Progetto;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.PrintWriter;

/**
 * Servlet utilizzata per ottenere lo stato attuale di una campagna.
 */
public class GetCampaignStatus extends HttpServlet {

    public void doGet (HttpServletRequest request, HttpServletResponse response) {
        String user = request.getSession().getAttribute("user").toString();
        Integer Campagna = Integer.valueOf(request.getSession().getAttribute("idCampagna").toString());
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.dbAccess();
        Integer tmp = null;

        if(user!=null){ //controllo session e null pointer sql
            if(Campagna>0){
                try {
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT Stato FROM Campagna WHERE ID =" + Campagna);
                    while (rs.next()) {
                        tmp = rs.getInt("Stato");
                    }
                    String stato = null;
                    if (tmp == 0) stato = "Creata";
                    if (tmp == 1) stato = "Avviata";
                    if (tmp == 2) stato = "Chiusa";
                    response.setContentType("text/html; charset=UTF-8");    //per la gestione dei carattere speciali
                    response.setCharacterEncoding("UTF-8");      //per la gestione dei carattere speciali
                    PrintWriter out = response.getWriter();
                    out.write(stato);
                    out.close();
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException | IOException e) {
                    throw new IllegalArgumentException(e.toString());
                }
            } else {
                throw new IllegalArgumentException("Errore nella lettura delle Campagne");
            }
        } else {
            throw new IllegalArgumentException("Devi effettuare il login");
        }
    }
}
