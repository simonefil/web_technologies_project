package Progetto;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Effettua il logout dal sito e cancella tutti i dati nella session.
 */
public class Logout extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");    //per la gestione dei carattere speciali
        response.setCharacterEncoding("UTF-8");      //per la gestione dei carattere speciali
        PrintWriter out=response.getWriter();
        HttpSession session = request.getSession();
        session.invalidate();
        out.print("<table width=\"100%\"> <tr> <td width=\"75%\" align=\"center\"> ");
        out.print("<br><br><div style=\"font-size:3vw; font-weight:700;\" >You are successfully logged out!</div>");
        out.write("<br><br><a style=\"color: #8787ff; font-size:3vw; font-weight:700;\" href=\"index.html\" TARGET=\"_top\">Home Page</a><br><br>");
        out.print("</td> <td width=\"25%\"> &nbsp; <td> </tr> </table>");
    }
}
