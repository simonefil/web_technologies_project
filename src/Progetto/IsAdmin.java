package Progetto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe utilizzata per verificare se un utente ha i permessi di admin o meno.
 */
class IsAdmin {

    boolean Admin (String username) {
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.dbAccess();
        boolean response = false;
        try {
               PreparedStatement pstmt = conn.prepareStatement("SELECT Admin FROM User WHERE Username = ?");
                pstmt.setString(1, username);
                ResultSet isAdmin = pstmt.executeQuery();
                while (isAdmin.next()) {
                    if (isAdmin.getBoolean("Admin"))
                        response = true;
                }
                isAdmin.close();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
            throw new IllegalArgumentException(e.toString());
        }
            return response;
    }
}
