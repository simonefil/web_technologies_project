package Progetto;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Servlet utilizzare per cambiare lo stato di una campagna da "creata" ad "avviata".
 */
public class StartCampaign extends HttpServlet {

    public void doGet (HttpServletRequest request, HttpServletResponse response) {
        Integer Campagna = Integer.valueOf(request.getSession().getAttribute("idCampagna").toString()==null?"0":request.getSession().getAttribute("idCampagna").toString());
        String user = request.getSession().getAttribute("user").toString();
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.dbAccess();
        String ID;
        if(user!=null){ //controllo session e null pointer sql
            try {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("UPDATE Campagna SET Stato = 1 WHERE ID =" + Campagna);
                ArrayList<Campagna> campagneArray =(ArrayList<Campagna>) request.getSession().getAttribute("resultSet");

                for(Campagna temp : campagneArray){
                    ID = temp.getId();
                    if(ID.equalsIgnoreCase(Campagna.toString())) {
                        temp.setStato(1);
                        break;
                    }
                }
                request.getSession().setAttribute("resultSet", campagneArray);

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
            throw new IllegalArgumentException("Devi effettuare il login");
        }
    }
}
