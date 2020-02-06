package Progetto;

import java.sql.*;

/**
 * Classe utilizzata per chiudere in automatico le campagne che hanno superato la data di scadenza.
 * Vengono selezionate tutte le campagne dal database, se la data odierna è successiva alla data di scadenza di una data campagna, questa viene chiusa.
 * @see #closeCampaign(int, Connection)
 */
public class CheckExpirationDate {

    public void checkExpiration(){
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.dbAccess();
        java.util.Date today = new java.util.Date();
        java.util.Date fine;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ID, Stato, Data_fine FROM Campagna");
            while (rs.next()) {
                int ID = rs.getInt("ID");
                Date date = rs.getDate("Data_fine");
                int stato = rs.getInt("Stato");
                if (stato == 0 || stato == 1) {
                    fine = new java.util.Date(date.getTime());
                    if (today.after(fine)) {
                        closeCampaign(ID, conn);
                    }
                }
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.toString());
        }
    }

    private void closeCampaign(int ID, Connection conn) {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("UPDATE Campagna SET Stato = 2 WHERE ID = ?");
            pstmt.setInt(1, ID);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.toString());
        }
    }
}
