package Progetto;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet utilizzata per la gestione e visualizzazione degli errori.
 * Questa classe permette all'utente che sta utilizzando il website di poter visualizzare una pagina contenente i dettagli dell'errore che si è verificato.
 */
public class ErrorHandler extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        processError(request, response);
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        processError(request, response);
    }

    private void processError(HttpServletRequest request,
                              HttpServletResponse response) throws IOException {
        // Analyze the servlet exception
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");
        if (servletName == null) {
            servletName = "Unknown";
        }
        String requestUrl = (String) request.getAttribute("javax.servlet.error.request_uri");
        if (requestUrl == null) {
            requestUrl = "Unknown";
        }

        // Set response content type
        response.setContentType("text/html; charset=UTF-8");    //per la gestione dei carattere speciali
        response.setCharacterEncoding("UTF-8");      //per la gestione dei carattere speciali
        PrintWriter out = response.getWriter();
        out.write("<html><head><title>Exception/Error Details</title></head><body><br><br>");
        if (statusCode != 500) { //se non è una servlet exception
            out.write("<h3 align=\"center\">Error Details</h3>");
            out.write("<strong align=\"center\">Status Code</strong>:" + statusCode + "<br>");
            out.write("<strong align=\"center\">Requested URL</strong>:" + requestUrl);
        } else if (throwable.getClass().getName().equals("java.lang.IllegalArgumentException")) {
            out.write("<h3 align=\"center\">Exception Details</h3>");
            out.write("<li align=\"center\">Exception Message: <b><big><font style=\"color:rgb(255, 0, 0);\">" + throwable.getMessage() + "</b></font></big></li>");
            out.write("</ul>");
        } else {
            out.write("<h3 align=\"center\">Exception Details</h3>");
            out.write("<ul align=\"center\"><li>Servlet Name:" + servletName + "</li>");
            out.write("<li>Exception Name:" + throwable.getClass().getName() + "</li>");
            out.write("<li>Requested URL:" + requestUrl + "</li>");
            out.write("<li>Exception Message:" + throwable.getMessage() + "</li>");
            out.write("</ul>");
        }

        out.write("<br><br>");
        out.write("<div align=\"center\"><a TARGET=\"_top\" align=\"center\" style=\" color: #8787ff; font-size:3vw; font-weight:700;\" href=\"index.html\">Home Page</a></div>");
        out.write("</body></html>");
    }
}