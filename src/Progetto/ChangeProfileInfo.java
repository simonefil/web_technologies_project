package Progetto;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Classe per la modifica delle informazioni di profilo dell'utente.
 * L'utente viene preso dalla session, le informazioni da modificare vengono inviate come parametri.
 * Vengono aggiornati solo i campi in cui l'utente scrive delle informazioni diverse.
 * Al termine dell'operazione avviene il redirect alla home dell'utente che ha richiesto la modifica.
 */
public class ChangeProfileInfo extends HttpServlet {

    public void doGet (HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String user = session.getAttribute("user").toString();
        String password = request.getParameter("password")==null?"":request.getParameter("password");
        String email = request.getParameter("email")==null?"":request.getParameter("email");
        String nome = request.getParameter("nome")==null?"":request.getParameter("nome");
        String cognome = request.getParameter("cognome")==null?"":request.getParameter("cognome");

        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.dbAccess();
        PreparedStatement pstmt = null;

        if(user!=null){     //controllo session altrimenti null pointer su sql
            try {
                if (!password.equals("")) {
                    String key = getServletContext().getInitParameter("SecretKey");
                    String password_criptata = AES.encrypt(password, key);
                    pstmt = conn.prepareStatement("UPDATE User SET Password = ? WHERE Username = ?");
                    pstmt.setString(1, password_criptata);
                    pstmt.setString(2, user);
                    pstmt.executeUpdate();
                }
                if (!email.equals("")){
                    pstmt = conn.prepareStatement("UPDATE User SET Email = ? WHERE Username = ?");
                    pstmt.setString(1, email);
                    pstmt.setString(2, user);
                    pstmt.executeUpdate();
                }
                if (!nome.equals("")) {
                    pstmt = conn.prepareStatement("UPDATE User SET Nome = ? WHERE Username = ?");
                    pstmt.setString(1, nome);
                    pstmt.setString(2, user);
                    pstmt.executeUpdate();
                }
                if (!cognome.equals("")) {
                    pstmt = conn.prepareStatement("UPDATE User SET Cognome = ? WHERE Username = ?");
                    pstmt.setString(1, cognome);
                    pstmt.setString(2, user);
                    pstmt.executeUpdate();
                }
                assert pstmt != null;
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                throw new IllegalArgumentException("Impossibile aggiornare le informazioni di profilo");
            }
            IsAdmin admin = new IsAdmin();
            if (admin.Admin(user)) response.sendRedirect(request.getContextPath() + "/adminHome.jsp");
            else response.sendRedirect(request.getContextPath() + "/home.jsp");
        } else {
            throw new IllegalArgumentException("Devi effettuare il login");
        }

    }
}
