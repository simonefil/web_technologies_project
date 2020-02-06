package Progetto;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Servlet utilizzata per rifiutare un'annotazione. Chiamata in admAnnotation.js
 */
public class RefuseAnnotation extends HttpServlet {

    public void doGet (HttpServletRequest request, HttpServletResponse response) {
        String user = request.getSession().getAttribute("user").toString();
        int idAnn = Integer.parseInt(request.getParameter("id")==null?"0":request.getParameter("id"));
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.dbAccess();
        if(user!=null){ //controllo session e null pointer sql
            try {
                PreparedStatement pstmt = conn.prepareStatement("UPDATE Annotazione SET Stato = false WHERE ID = ?");
                pstmt.setInt(1 ,idAnn);
                pstmt.executeUpdate();
                pstmt.close();
                conn.close();

                response.sendRedirect(request.getContextPath() + "/viewMapAdmin.html");
            } catch (SQLException | IOException e) {
                throw new IllegalArgumentException(e.toString());
            }
        } else {
            throw new IllegalArgumentException("Devi effettuare il login");
        }
    }
}
