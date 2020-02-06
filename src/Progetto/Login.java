package Progetto;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.ArrayList;

/**
 * Servlet utilizzata per effetuare il login nel website.
 * La password viene letta dal database, decriptata e confrontata con quella inserita dall'utente
 * @see AES#decrypt(String, String)
 *
 * Se il login ha avuto successo viene salvato nella session il ruolo dell'utente che ha effettuato il login (manager o lavoratore)
 */
public class Login extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String user = request.getParameter("loginUsername")==null?"":request.getParameter("loginUsername");
        String password = request.getParameter("loginPassword")==null?"":request.getParameter("loginPassword");

        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.dbAccess();
        ResultSet pssw;
        boolean correct = false;
        ResultSet rs;
        Statement stmt;
        String key = getServletContext().getInitParameter("SecretKey");
        if(user!=null){ //controllo session e null pointer sql
            try {
                PreparedStatement pstmt = conn.prepareStatement("SELECT Password FROM User WHERE Username = ?");
                pstmt.setString(1, user);
                pssw = pstmt.executeQuery();
                while (pssw.next()) {
                    String password_criptata = pssw.getString("Password");
                    String password_decriptata = AES.decrypt(password_criptata, key);
                    assert password_decriptata != null;
                    if (password_decriptata.equals(password))
                        correct = true;
                }
                pstmt.close();

                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT * FROM User");

            } catch (SQLException e ) {
                throw new IllegalArgumentException("Impossibile verificare la correttezza dei dati inseriti. Contatta l'amministratore.");
            }

            if (correct) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                try {
                    rs.absolute(0);
                    while(rs.next()) {
                        if(rs.getString("Username").equals(user)) {
                            User temp = new User();
                            temp.setUsername(rs.getString("Username"));
                            temp.setEmail(rs.getString("Email"));
                            temp.setNome(rs.getString("Nome"));
                            temp.setCognome(rs.getString("Cognome"));
                            session.setAttribute("UserFull", temp);
                            break;
                        }
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                IsAdmin adm = new IsAdmin();
                boolean admin = adm.Admin(user);
                    if (admin) {
                        response.addHeader("Admin", "true");
                        request.getSession().setAttribute("isAdmin", true);
                        if(rs!=null){
                            try {
                                ArrayList<User> arrayUsers = new ArrayList<>();
                                rs.absolute(0);
                                while(rs.next()) {
                                    User temp = new User();
                                    temp.setUsername(rs.getString("Username"));
                                    temp.setEmail(rs.getString("Email"));
                                    temp.setNome(rs.getString("Nome"));
                                    temp.setCognome(rs.getString("Cognome"));
                                    temp.setAdmin(rs.getString("Admin"));
                                    arrayUsers.add(temp);
                                }
                                session.setAttribute("usersSet", arrayUsers);
                                rs.close();
                                stmt.close();
                                conn.close();
                            } catch (SQLException e) {
                                throw new IllegalArgumentException(e.toString());
                        }
                    }
                    }
                    else {
                        response.addHeader("Admin", "false");
                        request.getSession().setAttribute("isAdmin", false);
                    }
                response.addHeader("LoginFailure", "false");
            }
            else {
                response.addHeader("LoginFailure", "true");
            }
        } else {
            throw new IllegalArgumentException("Devi effettuare il login");
        }
    }
}
