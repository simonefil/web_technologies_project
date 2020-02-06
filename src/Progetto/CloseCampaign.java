package Progetto;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Classe utilizzata quando un admin vuole chiudere una campagna. Richiesta inviata in campaignInfoAdmin.jsp
 * Viene impostato lo stato della campagna selezionato a chiuso (2)
 * Quindi viene aggiornata la lista delle campagne che sono visualizzate nella homepage del manager e ne viene effettuato il redirect.
 */
public class CloseCampaign extends HttpServlet {

    public void doGet (HttpServletRequest request, HttpServletResponse response) {
        Integer Campagna = Integer.valueOf(request.getSession().getAttribute("idCampagna").toString());
        String adminUser = (String) request.getSession().getAttribute("user");

        if(adminUser!=null){   //controllo session e null pointer sql
            if(Campagna>0){
                DatabaseConnection db = new DatabaseConnection();
                Connection conn = db.dbAccess();

                try {
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate("UPDATE Campagna SET Stato = 2 WHERE ID =" + Campagna);

                    ResultSet rs = stmt.executeQuery("SELECT * FROM Campagna INNER JOIN Subscription ON Campagna.ID = Subscription.Campagna WHERE Subscription.user='"+adminUser+"'");
                    rs.absolute(0);


                    ArrayList<Campagna> arrayCampagne = new Campagna().fillCampagne(rs);

                    request.getSession().setAttribute("resultSet", arrayCampagne);

                    rs.close();
                    stmt.close();
                    conn.close();

                } catch (SQLException e) {
                    throw new IllegalArgumentException(e.toString());
                }
                try {
                    response.sendRedirect(request.getContextPath() + "/adminHome.jsp");
                } catch (IOException e) {
                    throw new IllegalArgumentException(e.toString());
                }
            } else {
                throw new IllegalArgumentException("Errore nella chiusura della campagna");
            }

        } else {
            throw new IllegalArgumentException("Devi effettuare il login");
        }


    }
}
