package Progetto;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.ArrayList;

/**
 * Servlet utilizzata per ottenere le informazioni di una data campagna. Viene utilizzata sia per mostrare le informazioni nella homepage sia nella pagina di statistiche di una campagna per l'admin.
 */
public class GetCampaignInfo extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String user = request.getSession().getAttribute("user").toString();
        CheckExpirationDate ced = new CheckExpirationDate();
        ced.checkExpiration();
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.dbAccess();

        if(user!=null) { //controllo session e null pointer sql
            String operation = "login";  //operazione default è login, se arriva come parametro allora prende quella
            if(request.getParameter("operation")!=null)
                operation = request.getParameter("operation");

            if(operation.equalsIgnoreCase("statistics")){
                String idCampagna = request.getParameter("id")==null?"0":request.getParameter("id");

                try {

                    if(Integer.parseInt(idCampagna)>0){
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT * FROM Campagna WHERE ID="+idCampagna);
                        rs.absolute(0);

                        Campagna campagna = new Campagna();
                        while(rs.next()) {
                            Campagna campagnaTmp = new Campagna();
                            campagnaTmp.setId(rs.getString("ID"));
                            campagnaTmp.setNome(rs.getString("Nome"));
                            campagnaTmp.setStato(rs.getInt("Stato"));
                            campagnaTmp.setDataInizio(rs.getString("Data_Inizio"));
                            campagnaTmp.setDataFine(rs.getString("Data_Fine"));
                        }

                        rs.close();
                        rs = stmt.executeQuery("SELECT * FROM Picchi WHERE Campagna="+idCampagna);
                        rs.absolute(0);
                        ArrayList<Picco> arrayPicchi = new ArrayList<>();
                        while(rs.next()) {
                            Picco piccoTmp = new Picco();
                            String idPicco = rs.getString("ID");
                            piccoTmp.setId(idPicco);
                            piccoTmp.setProvenienza(rs.getString("Provenienza"));
                            piccoTmp.setElevazione(rs.getInt("Elevazione"));
                            piccoTmp.setLongitudine(rs.getString("Longitudine"));
                            piccoTmp.setLatitudine(rs.getString("Latitudine"));
                            piccoTmp.setNome(rs.getString("Nome"));
                            piccoTmp.setNomi_localizzati(rs.getString("Nomi_localizzati"));
                            piccoTmp.setCampagnaId(rs.getInt("Campagna"));
                            piccoTmp.setDaAnnotare(rs.getInt("Da_annotare"));
                            arrayPicchi.add(piccoTmp);
                        }

                        rs.close();
                        rs = stmt.executeQuery("SELECT Annotazione.ID AS IDR, Annotazione.Data_creazione AS Data_creazione, Annotazione.Validita AS Validita, Annotazione.Elevazione AS Elevazione, Annotazione.Nome AS Nome, Annotazione.Nomi_localizzati AS Nomi_localizzati, Annotazione.Stato AS Stato, Annotazione.User AS User, Annotazione.Id_picco AS Id_picco FROM Annotazione INNER JOIN Picchi ON Picchi.ID = Annotazione.Id_picco WHERE Picchi.Campagna ="+idCampagna);
                        rs.absolute(0);
                        ArrayList<Annotazione>  arrayAnnotazioni = new ArrayList<>();
                        while(rs.next()) {
                            Annotazione annotazioneTmp = new Annotazione();
                            annotazioneTmp.setId(rs.getString("IDR"));
                            annotazioneTmp.setData_creazione(rs.getString("Data_creazione"));
                            annotazioneTmp.setValidita(rs.getInt("Validita"));
                            annotazioneTmp.setElevazione(rs.getInt("Elevazione"));
                            annotazioneTmp.setNome(rs.getString("Nome"));
                            annotazioneTmp.setNomi_localizzati(rs.getString("Nomi_localizzati"));
                            annotazioneTmp.setStato(rs.getInt("Stato"));
                            annotazioneTmp.setUser(rs.getString("User"));
                            annotazioneTmp.setId_picco(rs.getInt("Id_picco"));
                            arrayAnnotazioni.add(annotazioneTmp);
                        }
                        //scorro i picchi e aggiungo le annotazioni al picco giusto
                        for(Picco piccoTemp : arrayPicchi){
                            piccoTemp.fillAnnotazioni(arrayAnnotazioni);
                        }

                        campagna.setPicchi(arrayPicchi);
                        request.getSession().setAttribute("CAMPAGNA", campagna);

                        rs.close();
                        stmt.close();
                        conn.close();
                    } else {
                        throw new IllegalArgumentException("errore nella lettura delle Campagne");
                    }

                } catch (SQLException e) {
                    throw new IllegalArgumentException(e.toString());
                }
            }

            if(operation.equalsIgnoreCase("login")){
                try {
                    String adminUser = (String) request.getSession().getAttribute("user");
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM Campagna INNER JOIN Subscription ON Campagna.ID = Subscription.Campagna WHERE Subscription.user='"+adminUser+"'");
                    rs.absolute(0);

                    ArrayList<Campagna> arrayCampagne = new Campagna().fillCampagne(rs);

                    request.getSession().setAttribute("resultSet", arrayCampagne);
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                    throw new IllegalArgumentException(e.toString());
                }
            }

        } else {
            throw new IllegalArgumentException("Devi effettuare il login");
        }
    }
}
