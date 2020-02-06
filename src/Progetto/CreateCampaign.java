package Progetto;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Servlet utilizzata per creare una campagna, i parametri vengono passati nella richiesta POST.
 * La campagna viene creata con stato "creata" (0), quindi viene aggiornata la lista delle campagne create dall'admin e avviene il redirect alla home page dell'admin.
 */
public class CreateCampaign extends HttpServlet {

    public void doPost (HttpServletRequest request, HttpServletResponse response) {

        String nome = request.getParameter("nome")==null?"":request.getParameter("nome");
        String datainizio_str = request.getParameter("datainizio")==null?"":request.getParameter("datainizio");
        String datafine_str = request.getParameter("datafine")==null?"":request.getParameter("datafine");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed = null;
        String username = (String) request.getSession().getAttribute("user");
        Integer idr=0;

        try {
            parsed = sdf.parse(datainizio_str);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.toString());
        }
        java.sql.Date datainizio = new java.sql.Date(parsed.getTime());
        try {
            parsed = sdf.parse(datafine_str);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.toString());
        }
        java.sql.Date datafine = new java.sql.Date(parsed.getTime());

        if(username!=null){ //controllo session e null pointe sql

            DatabaseConnection db = new DatabaseConnection();
            Connection conn = db.dbAccess();

            PreparedStatement pstmt;

            try {
                pstmt = conn.prepareStatement("INSERT INTO Campagna (ID, Nome, Stato, Data_inizio, Data_fine) VALUES (NULL,?,?,?,?);");
                pstmt.setString(1, nome);
                pstmt.setInt(2, 0); // 0 = creata, 1 = avviata (gli user possono iscriversi), 2 = chiusa
                pstmt.setDate(3, datainizio);
                pstmt.setDate(4, datafine);
                pstmt.executeUpdate();
                pstmt.close();

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT MAX(ID) AS IDR FROM Campagna;  ");
                while(rs.next()){
                    idr = rs.getInt("IDR");
                    request.getSession().setAttribute("idCampagna",idr);
                }
                rs = stmt.executeQuery("SELECT * FROM Campagna");
                rs.absolute(0);

                pstmt = conn.prepareStatement("INSERT INTO Subscription (User, Campagna) VALUES (?,?);");
                pstmt.setString(1, username);
                pstmt.setInt(2, idr);
                pstmt.executeUpdate();
                pstmt.close();

                ArrayList<Campagna> arrayCampagne = new Campagna().fillCampagne(rs);

                request.getSession().setAttribute("resultSet", arrayCampagne);
                rs.close();
                stmt.close();
                conn.close();

            } catch (SQLException e) {
                throw new IllegalArgumentException(e.toString());
            }
            GetCampaignInfo gci = new GetCampaignInfo();
            gci.doGet(request, response);
            try {
                response.sendRedirect(request.getContextPath() + "/campaignInfoAdmin.jsp");
            } catch (IOException e) {
                throw new IllegalArgumentException(e.toString());
            }
        }  else {
            throw new IllegalArgumentException("Devi effettuare il login");
        }


    }
}
