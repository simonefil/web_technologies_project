package Progetto;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * Dato l'ID di una campagna seleziona tutti i relativi picchi presenti nel database.
 * Assegna ad ogni picco un colore in base a:
 * ROSSO: se ha almeno un'annotazione rifiutata
 * VERDE: se non da validare
 * GIALLO: se non ha annotazioni
 * ARANCIONE: se ha 1 o più annotazioni
 *
 * Quindi salva tutti i dati dei picchi col relativo colore associato in json e restituisce il json alla pagina che ha effettuato la richiesta.
 *
 * Questa servlet è utilizzata per mostrare i picchi sulle mappe 2d e 3d.
 *
 * @see #colour(ResultSet, Connection)
 *
 */
public class GetPeaks extends HttpServlet {

    public void doGet (HttpServletRequest request, HttpServletResponse response) {
        String user = request.getSession().getAttribute("user").toString();
        int ID = Integer.parseInt(request.getParameter("id")==null?"0":request.getParameter("id"));
        String provenienza = request.getParameter("provenienza")==null?"":request.getParameter("provenienza");
        String[] coloursList = null;
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.dbAccess();

        PreparedStatement pstmt;
        JSONArray json = new JSONArray();
        if(user!=null){ //controllo session e null pointer sql
            try {
                pstmt = conn.prepareStatement("SELECT ID, Provenienza, Elevazione, Longitudine, Latitudine, Nome, Nomi_localizzati, Da_annotare FROM Picchi WHERE Campagna = ?");
                pstmt.setInt(1, ID);
                ResultSet rs = pstmt.executeQuery();
                if(provenienza.equalsIgnoreCase("manager")){
                    coloursList = colour(rs, conn);
                } else if(provenienza.equalsIgnoreCase("lavoratore")){
                    coloursList = colourUser(rs, conn, user);
                }

                ResultSetMetaData rsmd = rs.getMetaData();
                rs.absolute(0);
                while(rs.next()) {
                    int numColumns = rsmd.getColumnCount();
                    JSONObject obj = new JSONObject();
                    for (int i=1; i<=numColumns; i++) {
                        String column_name = rsmd.getColumnName(i);
                        obj.put(column_name, rs.getObject(column_name));
                    }
                    int row = rs.getRow() - 1;
                    obj.put("colour", coloursList[row]);
                    json.put(obj);
                }
                rs.close();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                throw new IllegalArgumentException(e.toString());
            }
            try {
                response.setContentType("text/html; charset=UTF-8");    //per la gestione dei carattere speciali
                response.setCharacterEncoding("UTF-8");      //per la gestione dei carattere speciali
                response.getWriter().print(json);
            } catch (IOException e) {
                throw new IllegalArgumentException(e.toString());
            }

        } else {
            throw new IllegalArgumentException("Devi effettuare il login");
        }

    }

    private String[] colour (ResultSet rs, Connection conn) throws SQLException {
        String[] coloursList;
        rs.last();
        coloursList = new String[rs.getRow()];
        rs.absolute(0);
        try {
            while (rs.next()) {
                int row = rs.getRow() - 1;
                if (!rs.getBoolean("Da_annotare")) coloursList[row] = "verde";
                else {
                    PreparedStatement pstmt = conn.prepareStatement("SELECT Stato FROM Annotazione WHERE Id_picco = ?");
                    pstmt.setInt(1, rs.getInt("ID"));
                    ResultSet rs2 = pstmt.executeQuery();
                    if (!rs2.next()) coloursList[row] = "giallo";
                    else do {
                        if (!rs2.getBoolean("Stato")) coloursList[row] = "rosso";
                        else if (coloursList[row] != "rosso") coloursList[row] = "arancione";
                    } while (rs2.next());
                    rs2.close();
                    pstmt.close();
                }
            }

        } catch (SQLException e) {throw new IllegalArgumentException(e.toString());}
        return coloursList;
    }

    private String[] colourUser (ResultSet rs, Connection conn, String user) throws SQLException {
        String[] coloursList;
        rs.last();
        coloursList = new String[rs.getRow()];
        rs.absolute(0);
        try {
            while (rs.next()) {
                int row = rs.getRow() - 1;
                if (!rs.getBoolean("Da_annotare"))
                    coloursList[row] = "verde";                     //se non da annotare
                else {
                    PreparedStatement pstmt = conn.prepareStatement("SELECT Stato FROM Annotazione WHERE Id_picco = ? AND User = ?");
                    pstmt.setInt(1, rs.getInt("ID"));
                    pstmt.setString(2, user);
                    ResultSet rs2 = pstmt.executeQuery();
                    if (!rs2.next())
                        coloursList[row] = "giallo";                //picco senza annotazione dell'utente
                    else {
                        coloursList[row] = "rosso";                 //picco già annotato dall'utente
                    }
                    rs2.close();
                    pstmt.close();
                }
            }

        } catch (SQLException e) {throw new IllegalArgumentException(e.toString());}
        return coloursList;
    }
}
