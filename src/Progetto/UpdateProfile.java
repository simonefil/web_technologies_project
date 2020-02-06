package Progetto;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

/**
 * Servlet chiamata quando l'admin vuole modificare i dettagli dei vari utenti.
 */
public class UpdateProfile extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String user = request.getSession().getAttribute("user").toString();
        String username = request.getParameter("username")==null?"":request.getParameter("username");
        String nome = request.getParameter("nome")==null?"":request.getParameter("nome");
        String cognome = request.getParameter("cognome")==null?"":request.getParameter("cognome");
        String email = request.getParameter("email")==null?"":request.getParameter("email");

        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.dbAccess();
        PreparedStatement pstmt;
        if(user!=null){ //controllo session e null pointer sql
            try {
                pstmt = conn.prepareStatement("UPDATE User SET Nome= ?, Email=?, Cognome=? WHERE Username=?");
                pstmt.setString(1, nome);
                pstmt.setString(2, email);
                pstmt.setString(3, cognome);
                pstmt.setString(4, username);
                pstmt.executeUpdate();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                throw new IllegalArgumentException("Non sono stati rispettati i limiti di forma suggeriti a fianco di ogni casella di input.");
            }
        } else {
            throw new IllegalArgumentException("Devi effettuare il login");
        }

    }
}
