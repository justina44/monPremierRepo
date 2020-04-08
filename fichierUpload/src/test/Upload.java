package test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class Upload
 */
@WebServlet( "/Upload" )
public class Upload extends HttpServlet {
    private static final long  serialVersionUID = 1L;

    public static final int    TAILLE_TAMPON    = 10240;
    public static final String CHEMIN_FICHIERS  = "Users/irina.bidet/Fichiers";
    public static final String VUE              = "/upload.jsp";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Upload() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

        String description = request.getParameter( "description" );
        request.setAttribute( "description", description );

        Part part = request.getPart( "fichier" );

        String nomFichier = getNomFichier( part );

        if ( nomFichier != null && !nomFichier.isEmpty() ) {
            String nomChamp = part.getName();

            nomFichier = nomFichier.substring( nomFichier.lastIndexOf( '/' ) + 1 )
                    .substring( nomFichier.lastIndexOf( '\\' ) + 1 );

            ecrireFichier( part, nomFichier, CHEMIN_FICHIERS );

            request.setAttribute( nomChamp, nomFichier );
        }

        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );

    }

    private void ecrireFichier( Part part, String nomFichier, String chemin ) throws IOException {
        BufferedInputStream entree = null;
        BufferedOutputStream sortie = null;
        try {
            entree = new BufferedInputStream( part.getInputStream(), TAILLE_TAMPON );
            sortie = new BufferedOutputStream( new FileOutputStream( new File( chemin + nomFichier ) ), TAILLE_TAMPON );

            byte[] tampon = new byte[TAILLE_TAMPON];
            int longueur;
            while ( ( longueur = entree.read( tampon ) ) > 0 ) {
                sortie.write( tampon, 0, longueur );
            }
        } finally {
            try {
                sortie.close();
            } catch ( IOException ignore ) {
            }
            try {
                entree.close();
            } catch ( IOException ignore ) {
            }
        }
    }

    private static String getNomFichier( Part part ) {
        for ( String contentDisposition : part.getHeader( "content-disposition" ).split( ";" ) ) {
            if ( contentDisposition.trim().startsWith( "filename" ) ) {
                return contentDisposition.substring( contentDisposition.indexOf( '=' ) + 1 ).trim().replace( "\"", "" );
            }
        }
        return null;
    }

}
