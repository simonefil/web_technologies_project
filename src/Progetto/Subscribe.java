package Progetto;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Serlvet utilizzata quando un utente vuole iscrviersi a una campagna.
 * Viene inserita nella table Subscription un coppia (user, campagna a cui si è iscritto)
 * Infine viene aggiornata la lista delle campagne a cui non è iscritto visibile nella home page
 */
public class Subscribe extends HttpServlet {

    public void doGet (HttpServletRequest request, HttpServletResponse response) {
        String user = request.getSession().getAttribute("user").toString();
        int id = Integer.parseInt(request.getParameter("id")==null?"0":request.getParameter("id"));

        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.dbAccess();
        PreparedStatement pstmt;
        int stato;
        if(user!=null){ //controllo session e null pointer sql
            try {
                pstmt = conn.prepareStatement("SELECT Stato FROM Campagna WHERE ID = ?");
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                rs.next();
                stato = rs.getInt("Stato");
                rs.close();
                if (stato == 1) {
                    try {
                        pstmt = conn.prepareStatement("INSERT INTO Subscription VALUES (?,?)");
                        pstmt.setString(1, user);
                        pstmt.setInt(2, id);
                        pstmt.execute();
                    } catch (SQLException e) {
                        throw new IllegalArgumentException(e.toString());
                    }
                }
                else {
                    throw new IllegalArgumentException("La campagna selezionata non è nello stato Avviato");
                }
                pstmt.close();
                conn.close();
                getServletContext().getRequestDispatcher("/GetSubscribedCamp").forward(request, response);
            } catch (SQLException | ServletException | IOException e) {
                throw new IllegalArgumentException(e.toString());
            }
        } else {
            throw new IllegalArgumentException("Devi effettuare il login");
        }
    }
}
