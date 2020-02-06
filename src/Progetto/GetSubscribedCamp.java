package Progetto;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Servlet utilizzata per richiedere l'elenco delle campagne a cui un utente è iscritto.
 */
public class GetSubscribedCamp extends HttpServlet {

    public void doGet (HttpServletRequest request, HttpServletResponse response) {
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.dbAccess();
        HttpSession session = request.getSession();
        String user = session.getAttribute("user").toString();
        PreparedStatement pstmt;
        PreparedStatement pstmt2=null;
        ResultSet rs;
        ResultSet rs2=null;

        if(user!=null){ //controllo session e null pointer sql
            try {
                pstmt = conn.prepareStatement("SELECT COUNT(*) AS total FROM Subscription");
                rs = pstmt.executeQuery();
                rs.next();
                int check = rs.getInt("total");
                if (check == 0) {
                    rs = null;
                }
                else {
                    pstmt = conn.prepareStatement("SELECT C.ID, C.Nome, C.Stato, C.Data_inizio, C.Data_fine FROM Campagna C JOIN Subscription S on C.ID = S.Campagna WHERE S.User = ? AND C.Stato = 1");
                    pstmt.setString(1, user);
                    rs = pstmt.executeQuery();

                    pstmt2 = conn.prepareStatement("SELECT C.ID, C.Nome, C.Stato, C.Data_inizio, C.Data_fine FROM Campagna C JOIN Subscription S on C.ID = S.Campagna WHERE S.User = ? AND C.Stato = 2");
                    pstmt2.setString(1, user);
                    rs2 = pstmt2.executeQuery();
                }
                ArrayList<Campagna> arrayCampagne = new ArrayList<>();
                if (rs != null) {
                    rs.absolute(0);
                    while (rs.next()) {
                        Campagna temp = new Campagna();
                        temp.setId(rs.getString("ID"));
                        temp.setNome(rs.getString("Nome"));
                        temp.setStato(rs.getInt("Stato"));
                        temp.setDataInizio(rs.getString("Data_Inizio"));
                        temp.setDataFine(rs.getString("Data_Fine"));
                        arrayCampagne.add(temp);
                    }
                    request.getSession().setAttribute("Subbed", arrayCampagne);
                    rs.close();
                }

                ArrayList<Campagna> arrayCampagneChiuse = new ArrayList<>();
                if (rs2 != null) {
                    rs2.absolute(0);
                    while (rs2.next()) {
                        Campagna temp = new Campagna();
                        temp.setId(rs2.getString("ID"));
                        temp.setNome(rs2.getString("Nome"));
                        temp.setStato(rs2.getInt("Stato"));
                        temp.setDataInizio(rs2.getString("Data_Inizio"));
                        temp.setDataFine(rs2.getString("Data_Fine"));
                        arrayCampagneChiuse.add(temp);
                    }
                    request.getSession().setAttribute("Closed", arrayCampagneChiuse);
                    rs2.close();
                }

                pstmt2.close();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                throw new IllegalArgumentException(e.toString());
            }
        } else {
            throw new IllegalArgumentException("Devi effettuare il login");
        }
    }
}
