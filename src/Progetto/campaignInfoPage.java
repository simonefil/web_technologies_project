package Progetto;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Quando viene richiesto di visualizzare i dettagli di una campagna, l'id della campagna viene salvato nella session in modo tale che per ulteriori azioni di aggiunta/modifica della campagna, il metodo chiamato possa recuperare l'id della campagna di riferimento direttamente dalla session.
 */
public class campaignInfoPage extends HttpServlet {

    public void doGet (HttpServletRequest request, HttpServletResponse response) {

        String id = request.getParameter("id");
        request.getSession().setAttribute("idCampagna",id);
    }
}

