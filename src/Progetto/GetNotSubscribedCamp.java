package Progetto;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Servlet utilizzata per ottenere l'elenco delle campagne a cui un utente può iscriversi.
 * Ogni volta che viene chiamata la servlet verifica che non ci siano campagne che abbiano superato la data di fine, quindi seleziona le campagne alle quali l'utente non ha effettuato l'iscrizione.
 * @see CheckExpirationDate#checkExpiration()
 */
public class GetNotSubscribedCamp extends HttpServlet {

    public void doGet (HttpServletRequest request, HttpServletResponse response) {
        CheckExpirationDate ced = new CheckExpirationDate();
        ced.checkExpiration();
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.dbAccess();
        HttpSession session = request.getSession();
        String user = session.getAttribute("user").toString();
        PreparedStatement pstmt;
        ResultSet rs;
        if(user!=null){ //controllo session e null pointer sql
            try {
                pstmt = conn.prepareStatement("SELECT COUNT(*) AS total FROM Subscription");
                rs = pstmt.executeQuery();
                rs.next();
                int check = rs.getInt("total");
                if (check == 0) {
                    pstmt = conn.prepareStatement("SELECT * FROM Campagna WHERE Stato = 1");
                    rs = pstmt.executeQuery();
                }
                else {
                    pstmt = conn.prepareStatement("SELECT C.ID, C.Nome, C.Stato, C.Data_inizio, C.Data_fine FROM Campagna C WHERE C.Stato = 1 AND C.ID NOT IN (SELECT S.Campagna FROM Subscription S WHERE S.User = ?)");
                    pstmt.setString(1, user);
                    rs = pstmt.executeQuery();
                }
                rs.absolute(0);

                ArrayList<Campagna> arrayCampagne = new ArrayList<>();
                while(rs.next()) {
                    Campagna temp = new Campagna();
                    temp.setId(rs.getString("ID"));
                    temp.setNome(rs.getString("Nome"));
                    temp.setStato(rs.getInt("Stato"));
                    temp.setDataInizio(rs.getString("Data_Inizio"));
                    temp.setDataFine(rs.getString("Data_Fine"));
                    arrayCampagne.add(temp);
                }
                request.getSession().setAttribute("notSubbed", arrayCampagne);
                rs.close();
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
