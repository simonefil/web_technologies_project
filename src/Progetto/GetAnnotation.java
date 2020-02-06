package Progetto;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * Servlet utilizzata per selezionare dal database le annotazioni relative a un picco.
 * Le annotazioni vengono formattate in json e restituite alla pagina che ne ha effettuato la richiesta.
 */
public class GetAnnotation extends HttpServlet {

    public void doGet (HttpServletRequest request, HttpServletResponse response) {
        String user = request.getSession().getAttribute("user").toString();
        int idPicco = Integer.parseInt(request.getParameter("id")==null?"0":request.getParameter("id"));
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.dbAccess();
        PreparedStatement pstmt;
        JSONArray json = new JSONArray();

        if(user!=null){ //controllo session e null pointer
            if(idPicco>0){
                try {
                    pstmt = conn.prepareStatement("SELECT * FROM Annotazione WHERE Id_picco = ?");
                    pstmt.setInt(1, idPicco);
                    ResultSet rs = pstmt.executeQuery();
                    ResultSetMetaData rsmd = rs.getMetaData();
                    while(rs.next()) {
                        int numColumns = rsmd.getColumnCount();
                        JSONObject obj = new JSONObject();
                        for (int i=1; i<=numColumns; i++) {
                            String column_name = rsmd.getColumnName(i);
                            obj.put(column_name, rs.getObject(column_name));
                        }
                        json.put(obj);
                    }
                    response.setContentType("text/html; charset=UTF-8");    //per la gestione dei carattere speciali
                    response.setCharacterEncoding("UTF-8");      //per la gestione dei carattere speciali
                    response.getWriter().print(json);
                    rs.close();
                    pstmt.close();
                    conn.close();
                } catch (SQLException | IOException e) {
                    throw new IllegalArgumentException(e.toString());
                }
            } else {
                throw new IllegalArgumentException("Errore nella lettura delle annotazioni");
            }

        } else {
            throw new IllegalArgumentException("Devi effettuare il login");
        }


    }
}
