package dao;


import static dao.DAOUtilitaire.fermeturesSilencieuses;
import static dao.DAOUtilitaire.initialisationRequetePreparee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import bo.Utilisateur;

public class UtilisateurDaoImpl implements UtilisateurDao {
	private DAOFactory daoFactory;
	private static final String SQL_SELECT_PAR_EMAIL = "SELECT id, email, nom, mot_de_passe, date_inscription FROM Utilisateur WHERE email = ?";
	private static final String SQL_INSERT = "INSERT INTO Utilisateur (email, mot_de_passe, nom, date_inscription) VALUES (?, ?, ?, NOW())";
	
	UtilisateurDaoImpl(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public Utilisateur trouver(String email) throws DAOException {
		return trouver( SQL_SELECT_PAR_EMAIL, email );
	}

	/*
	 * Impl�mentation de la m�thode creer() d�finie dans l'interface UtilisateurDao
	 */
	@Override
	public void creer(Utilisateur utilisateur) throws  DAOException {
		 Connection connexion = null;
		    PreparedStatement preparedStatement = null;
		    ResultSet valeursAutoGenerees = null;

		    try {
		        /* R�cup�ration d'une connexion depuis la Factory */
		        connexion = daoFactory.getConnection();
		        preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true, utilisateur.getEmail(), utilisateur.getMotDePasse(), utilisateur.getNom() );
		        int statut = preparedStatement.executeUpdate();
		        /* Analyse du statut retourn� par la requ�te d'insertion */
		        if ( statut == 0 ) {
		            throw new DAOException( "�chec de la cr�ation de l'utilisateur, aucune ligne ajout�e dans la table." );
		        }
		        /* R�cup�ration de l'id auto-g�n�r� par la requ�te d'insertion */
		        valeursAutoGenerees = preparedStatement.getGeneratedKeys();
		        if ( valeursAutoGenerees.next() ) {
		            /* Puis initialisation de la propri�t� id du bean Utilisateur avec sa valeur */
		            utilisateur.setId( valeursAutoGenerees.getLong( 1 ) );
		        } else {
		            throw new DAOException( "�chec de la cr�ation de l'utilisateur en base, aucun ID auto-g�n�r� retourn�." );
		        }
		    } catch ( SQLException e ) {
		        throw new DAOException( e );
		    } finally {
		        fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
		    }
		
	}

	/*
     * M�thode g�n�rique utilis�e pour retourner un utilisateur depuis la base
     * de donn�es, correspondant � la requ�te SQL donn�e prenant en param�tres
     * les objets pass�s en argument.
     */
    private Utilisateur trouver( String sql, Object... objets ) throws DAOException {
    	Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    Utilisateur utilisateur = null;

	    try {
	        /* R�cup�ration d'une connexion depuis la Factory */
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, sql, false, objets );
	        resultSet = preparedStatement.executeQuery();
	        /* Parcours de la ligne de donn�es de l'�ventuel ResulSet retourn� */
	        if ( resultSet.next() ) {
	            utilisateur = map( resultSet );
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermeturesSilencieuses( resultSet, preparedStatement, connexion );
	    }
	
		return utilisateur;
    }
	
	/*
	 * Simple m�thode utilitaire permettant de faire la correspondance (le mapping)
	 * entre une ligne issue de la table des utilisateurs (un ResultSet) et un bean
	 * Utilisateur.
	 */
	private static Utilisateur map(ResultSet resultSet) throws SQLException {
		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setId(resultSet.getLong("id"));
		utilisateur.setEmail(resultSet.getString("email"));
		utilisateur.setMotDePasse(resultSet.getString("mot_de_passe"));
		utilisateur.setNom(resultSet.getString("nom"));
		utilisateur.setDateInscription(resultSet.getTimestamp("date_inscription"));
		return utilisateur;
	}

	
}
