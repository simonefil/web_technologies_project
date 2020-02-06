package Progetto;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Calendar;

/**
 * Classe utilizzata per scrivere le annotazioni di un utente nel database.
 * L'utente che richieste l'inserimento di un'annotazione viene preso dalla session.
 * I parametri dell'annotazione vengono inviati come corpo della post request.
 * Il metodo restituisce un valore true di conferma.
 *
 * La richiesta viene effettuata in usrAnnotation.js userAnnotation.jsp
 */

public class AddAnnotation extends HttpServlet {

    public void doPost (HttpServletRequest request, HttpServletResponse response) {
        String user = request.getSession().getAttribute("user").toString();
        Double elevazione = null;
        String nome = null;
        String nomi_localizzati = null;
        boolean alreadyIn = false;
        boolean daAnnotare = true;

        if(user!=null){ //controllo session e null pointer sql
            int picco = Integer.parseInt(request.getParameter("picco")==null?"0":request.getParameter("picco"));
            boolean validita = Boolean.parseBoolean(request.getParameter("validita")==null?"false":request.getParameter("validita"));

            if (validita){  //validità dell'annotazione
                elevazione = Double.parseDouble(request.getParameter("elevazione")==null?"0":request.getParameter("elevazione"));
                nome = request.getParameter("nome");
                nomi_localizzati = request.getParameter("nomi_localizzati")==null?"":request.getParameter("nomi_localizzati");
            }
            java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            DatabaseConnection db = new DatabaseConnection();
            Connection conn = db.dbAccess();
            PreparedStatement pstmt;
            Statement stmt = null;

            try{
                stmt = conn.createStatement();
                pstmt = conn.prepareStatement("SELECT * FROM Annotazione WHERE User=? AND Id_picco=?");
                pstmt.setString(1, user);
                pstmt.setInt(2, picco);
                ResultSet rs = pstmt.executeQuery();
                rs.absolute(0);
                if(rs.next()){
                    alreadyIn=true;
                } else {
                    //controllo che il picco sia da annotare
                    rs.close();
                    rs = stmt.executeQuery("SELECT Da_annotare FROM Picchi WHERE ID="+picco);
                    rs.absolute(0);
                    rs.next();
                    if(rs.getInt("Da_annotare")==1){
                        pstmt = conn.prepareStatement("INSERT INTO Annotazione VALUES (NULL,?,?,?,?,?,?,?,?)");
                        pstmt.setDate(1, date);
                        pstmt.setBoolean(2, validita);
                        if (elevazione == null) pstmt.setNull(3, Types.INTEGER);
                        else {pstmt.setDouble(3, elevazione);}
                        if (nome == null) pstmt.setNull(4, Types.VARCHAR);
                        else {pstmt.setString(4, nome);}
                        if (nomi_localizzati == null) pstmt.setNull(5, Types.VARCHAR);
                        else {pstmt.setString(5, nomi_localizzati);}
                        pstmt.setBoolean(6, true); // false rifiutata true non rifiutata
                        pstmt.setString(7, user);
                        pstmt.setInt(8, picco);
                        pstmt.executeUpdate();
                        pstmt.close();
                    } else {
                        daAnnotare=false;
                    }
                }
                stmt.close();
                rs.close();
                conn.close();
            } catch (SQLException e) {
                throw new IllegalArgumentException(e.toString());
            }
            try {
                response.setContentType("text/html; charset=UTF-8");    //per la gestione dei carattere speciali
                response.setCharacterEncoding("UTF-8");      //per la gestione dei carattere speciali
                PrintWriter out = response.getWriter();
                if(daAnnotare==false)
                    out.write("-1");    //dà errore sulla jsp se l'annotazione non è da annotare
                else if(alreadyIn==true)
                    out.write("0");     //dà errore sulla jsp se provo a inserire più di un'annotazione per lo stesso picco
                else if(alreadyIn==false)
                    out.write("1");     //se tutto va bene e inserisco l'annotazione

            } catch (IOException e) {
                throw new IllegalArgumentException(e.toString());
            }
        } else {
            throw new IllegalArgumentException("Devi effettuare il login");
        }



    }
}
