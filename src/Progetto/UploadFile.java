package Progetto;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Servlet utilizzata per il caricamento del file JSON contenente i picchi da visualizzare in una campagna.
 * Una volta inseriti i picchi nel database avviene il redirect alla pagina di visualizzazione della mappa.
 * <br>FUNZIONAMENTO:
 * <br>- Si salva in una variabile l'ID del picco caricato più recentemente
 * @see #getLastID()
 * <br>- Viene effettuato il parsing del file JSON e i dati vengono salvati in ArrayList
 * <br>- Viene creata una pool di thread che verranno utilizzati per effettuare delle batch query al database per inserire tutti i dati dei picchi
 * <br>- La pool di thread viene chiusa non appena finiscono l'esecuzione della query. Se non finiscono entro UPLOAD_TIME_LIMIT viene effettuato l'arresto forzato dei Thread.
 * @see #shutdownThreads(ExecutorService)
 * <br>- Se I thread vengono interrotti forzatamente vengono eliminate le righe che sono state scritte nel db durante il tempo UPLOAD_TIME_LIMIT
 * @see #deletePartialEntries(int, int)
 * <br>- Avviene un redirect alla mappa se la query è andata a buon fine. Altrimenti compare un messaggio di errore con un link per tornare alla home.
 */

public class UploadFile extends HttpServlet {

    /**
     * <br>INFO:
     * <br>Per aumentare le performace in caso di inserimenti multipli di un grande numero di picchi le query vengono eseguite
     * <br>in batch multipli con connessioni multiple in multithreading. Il parametro N_QUERY_FOR_BATCH va modificato in base
     * <br>alle caratteristiche prestazionali del server su cui risiede il database e in base al numero di picchi che ci si aspetta
     * <br>che vengano mediamente caricati per file JSON.
     */

    private static final int N_QUERY_FOR_BATCH = 100;

    /**
     * <br> Per evitare che vengano create eccessive connessioni al server va impostato il numero di thread adeguato.
     * <br> Il numero di thread che verranno usati corrisponde al numero di connessioni che verranno create in contemporanea al db.
     * <br> Dato che il carico su CPU dei thread è molto basso si consiglia di utilizzare un numero di thread maggiore del numero di core logici effettivamente disponibili nella CPU per massimizzare le performance.
     * <br> NOTA BENE: di default MySQL ha un limite di connessioni aperte per client di 150! Non eccedere!
     */

    private static final int N_THREADS = (Runtime.getRuntime().availableProcessors()) * 8;
    private static final int UPLOAD_TIME_LIMIT = 120;

    public void doPost(HttpServletRequest request, HttpServletResponse response) {

        DatabaseConnection db = new DatabaseConnection();
        String id = request.getSession().getAttribute("idCampagna").toString();
        List<Double> Elevazione = new ArrayList<>();
        int Campagna = Integer.parseInt(request.getSession().getAttribute("idCampagna").toString());
        List<String> Provenienza = new ArrayList<>();
        List<String> Nome = new ArrayList<>();
        List<String> Nomi_localizzati = new ArrayList<>();
        boolean Da_annotare = false;
        List<Double> Latitudine = new ArrayList<>();
        List<Double> Longitudine = new ArrayList<>();
        ExecutorService executor = Executors.newWorkStealingPool(N_THREADS);
        int last_ID = getLastID();


        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
                for (FileItem item : multiparts) {
                    if (!item.isFormField()) {
                        String content = item.getString("UTF-8");
                        content = content.replace("\n", "");
                        JSONArray jarr = new JSONArray(content);
                        for (int i = 0; i < jarr.length(); i++) {
                            JSONObject jobj = jarr.getJSONObject(i);
                            Provenienza.add(jobj.getString("provenance"));
                            if (jobj.isNull("elevation")) Elevazione.add(null);
                            else Elevazione.add(jobj.getDouble("elevation"));
                            Latitudine.add(jobj.getDouble("latitude"));
                            Longitudine.add(jobj.getDouble("longitude"));
                            if (jobj.isNull("name")) Nome.add(null);
                            else Nome.add(jobj.getString("name"));
                            String nomi_localizzati_string = "";
                            if (!jobj.isNull("localized_names")) {
                                JSONArray nomi_localizzati = jobj.getJSONArray("localized_names");
                                if (nomi_localizzati.length() > 0) {
                                    for (int j = 0; j < nomi_localizzati.length(); j++) {
                                        nomi_localizzati_string += nomi_localizzati.getJSONArray(j).getString(1) + "(" + nomi_localizzati.getJSONArray(j).getString(0) + ")";
                                        if (j != nomi_localizzati.length() - 1) {
                                            nomi_localizzati_string += ", ";
                                        }
                                    }
                                }
                            }
                            Nomi_localizzati.add(nomi_localizzati_string);
                        }
                    }
                    if (item.getFieldName().equals("daAnnotare")) Da_annotare = true;
                }
            } catch (Exception e) {
                throw new IllegalArgumentException(e.toString());
            }
        } else throw new IllegalArgumentException("Error parsing the file");
        try {
            int query_size = Provenienza.size();
            double quoziente = query_size / N_QUERY_FOR_BATCH;
            double resto = query_size % N_QUERY_FOR_BATCH;
            int quoziente_intero = (int) quoziente;
            int w;
            for (w = 0; w < quoziente_intero; w++) {
                boolean finalDa_annotare = Da_annotare;
                int finalW = w;
                executor.execute(() -> {
                    try {
                        Connection conn = db.dbAccess();
                        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Picchi VALUES (NULL,?,?,?,?,?,?,?,?)");
                        for (int y = N_QUERY_FOR_BATCH * finalW; y < N_QUERY_FOR_BATCH * (finalW + 1); y++) {
                            pstmt.setString(1, Provenienza.get(y));
                            if (Elevazione.get(y) == null) pstmt.setNull(2, Types.DOUBLE);
                            else pstmt.setDouble(2, Elevazione.get(y));
                            pstmt.setDouble(3, Longitudine.get(y));
                            pstmt.setDouble(4, Latitudine.get(y));
                            if (Nome.get(y) == null) pstmt.setNull(5, Types.VARCHAR);
                            else pstmt.setString(5, Nome.get(y));
                            if (Nomi_localizzati.get(y).equals("")) pstmt.setNull(6, Types.VARCHAR);
                            else pstmt.setString(6, Nomi_localizzati.get(y));
                            pstmt.setInt(7, Campagna);
                            pstmt.setBoolean(8, finalDa_annotare);
                            pstmt.addBatch();
                        }
                        pstmt.executeBatch();
                        pstmt.close();
                        conn.close();
                    } catch (SQLException e) {
                        throw new IllegalArgumentException(e.toString());
                    }
                });
            }
            int finalW1 = w;
            boolean finalDa_annotare1 = Da_annotare;
            executor.execute(() -> {
                try {
                    Connection conn = db.dbAccess();
                    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Picchi VALUES (NULL,?,?,?,?,?,?,?,?)");
                    for (int y = N_QUERY_FOR_BATCH * finalW1; y < (N_QUERY_FOR_BATCH * finalW1) + resto; y++) {
                        pstmt.setString(1, Provenienza.get(y));
                        if (Elevazione.get(y) == null) pstmt.setNull(2, Types.DOUBLE);
                        else pstmt.setDouble(2, Elevazione.get(y));
                        pstmt.setDouble(3, Longitudine.get(y));
                        pstmt.setDouble(4, Latitudine.get(y));
                        if (Nome.get(y) == null) pstmt.setNull(5, Types.VARCHAR);
                        else pstmt.setString(5, Nome.get(y));
                        if (Nomi_localizzati.get(y).equals("")) pstmt.setNull(6, Types.VARCHAR);
                        else pstmt.setString(6, Nomi_localizzati.get(y));
                        pstmt.setInt(7, Campagna);
                        pstmt.setBoolean(8, finalDa_annotare1);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                    pstmt.close();
                    conn.close();
                } catch (SQLException e) {
                    throw new IllegalArgumentException(e.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean threadStatus = shutdownThreads(executor);
        if (!threadStatus) {
            deletePartialEntries(last_ID, Campagna);
            PrintWriter out = null;
            try {
                response.setContentType("text/html; charset=UTF-8");    //per la gestione dei carattere speciali
                response.setCharacterEncoding("UTF-8");      //per la gestione dei carattere speciali
                out = response.getWriter();
            } catch (IOException e) {
                e.printStackTrace();
            }
            out.print("<table width=\"100%\"> <tr> <td width=\"75%\" align=\"center\"> ");
            out.print("<br><br><div style=\"font-size:2vw; font-weight:700;\" >The JSON file you uploaded is too big or with wrong syntax.</div>");
            out.write("<br><br><a style=\"color: #8787ff; font-size:2vw; font-weight:700;\" href=\"adminHome.jsp\" TARGET=\"_top\">Home Page</a><br><br>");
            out.print("</td> <td width=\"25%\"> &nbsp; <td> </tr> </table>");
        } else {
            try {
                response.sendRedirect(request.getContextPath() + "/viewMapAdmin.html?x=" + id + "&provenienza=manager");
            } catch (IOException e) {
                throw new IllegalArgumentException(e.toString());
            }
        }
    }

    boolean shutdownThreads(ExecutorService pool) {
        pool.shutdown();
        boolean status = false;
        try {
            if (!pool.awaitTermination(UPLOAD_TIME_LIMIT, TimeUnit.SECONDS)) {
                status = false;
                pool.shutdownNow();
            } else status = true;
        } catch (InterruptedException e) {
            new IllegalArgumentException("C'è stato un errore durante la scrittura nel database. " + e.toString());
        } finally {
            while (!pool.isTerminated()) {
                pool.shutdownNow();
            }
        }
        return status;
    }

    void deletePartialEntries(int lastID, int campID) {
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.dbAccess();
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Picchi WHERE ID > ? AND Campagna = ?");
            pstmt.setInt(1, lastID);
            pstmt.setInt(2, campID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("C'è stato un errore imprevisto durante la scrittura nel database. Contattare l'amministratore. " + e.toString());
        }
    }

    int getLastID () {
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.dbAccess();
        int id = 0;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ID FROM Picchi ORDER BY ID DESC LIMIT 1");
            while (rs.next()) {
                id = rs.getInt("ID");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.toString());
        }
        return id;
    }
}


