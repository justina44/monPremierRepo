package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bdd.Test;

/**
 * Servlet implementation class GestionTest
 */
@WebServlet("/GestionTest")
public class GestionTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static final String ATT_MESSAGES = "messages";
    public static final String VUE          = "/WEB-INF/vue.jsp";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GestionTest() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Test test = new Test();
        List<String> messages = test.executerTests( request );

        /* Enregistrement de la liste des messages dans l'objet requête */
        request.setAttribute( ATT_MESSAGES, messages );

        /* Transmission vers la page en charge de l'affichage des résultats */
        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
    }

	

  
    

}
