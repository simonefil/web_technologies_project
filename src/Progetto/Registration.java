package Progetto;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Servlet per la registrazione di un utente.
 * I dati dell'utente vengono inviati nella request, la password prima di essere salvata nel database viene criptata.
 * @see AES#encrypt(String, String)
 */
public class Registration extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String key = getServletContext().getInitParameter("SecretKey");
        request.setCharacterEncoding("UTF-8");
        boolean ruoloBool=false;

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String ruolo = request.getParameter("ruolo");

        String password_criptata = AES.encrypt(password, key);
        if(ruolo.equalsIgnoreCase("1"))
            ruoloBool=true;

        String src = username + password + email + nome + cognome + ruolo;
        char[] srcArray = src.toCharArray();
        boolean filterRes = charFilter(srcArray);
        if (!filterRes) throw new IllegalArgumentException("Sono stati inseriti dei caratteri non consentiti.");

        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.dbAccess();

        PreparedStatement pstmt;
        boolean resp;

        try {
            pstmt = conn.prepareStatement("INSERT INTO User (Username, Password, Email, Nome, Cognome, Admin) VALUES (?,?,?,?,?,?)");
            pstmt.setString(1, username);
            pstmt.setString(2, password_criptata);
            pstmt.setString(3, email);
            pstmt.setString(4, nome);
            pstmt.setString(5, cognome);
            pstmt.setBoolean(6, ruoloBool);
            int rows = pstmt.executeUpdate();
            if (rows == 0) resp = false;
            else resp = true;
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Non sono stati rispettati i limiti di forma suggeriti a fianco di ogni casella di input.");
        }

        if (resp) {
            try {
                response.sendRedirect(request.getContextPath() + "/index.html");
            } catch (IOException e) {
                throw new IllegalArgumentException(e.toString());
            }
        }
        else {
            throw new IllegalArgumentException("C'è stato un errore sconosciuto durante la sua richiesta. Si prega di contattare l'amministratore.");
        }
    }

    private boolean charFilter(char[] srcArray) {
        char[] toExclude = {';', ',', '{', '}', '[', ']', '/', '\\'};
        boolean check = true;
        for (char aSrcArray : srcArray) {
            for (char aToExclude : toExclude) {
                if (aSrcArray == aToExclude) check = false;
            }
        }
        return check;
    }
}
