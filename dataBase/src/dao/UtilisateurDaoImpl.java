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
	 * Implémentation de la méthode creer() définie dans l'interface UtilisateurDao
	 */
	@Override
	public void creer(Utilisateur utilisateur) throws  DAOException {
		 Connection connexion = null;
		    PreparedStatement preparedStatement = null;
		    ResultSet valeursAutoGenerees = null;

		    try {
		        /* Récupération d'une connexion depuis la Factory */
		        connexion = daoFactory.getConnection();
		        preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true, utilisateur.getEmail(), utilisateur.getMotDePasse(), utilisateur.getNom() );
		        int statut = preparedStatement.executeUpdate();
		        /* Analyse du statut retourné par la requête d'insertion */
		        if ( statut == 0 ) {
		            throw new DAOException( "Échec de la création de l'utilisateur, aucune ligne ajoutée dans la table." );
		        }
		        /* Récupération de l'id auto-généré par la requête d'insertion */
		        valeursAutoGenerees = preparedStatement.getGeneratedKeys();
		        if ( valeursAutoGenerees.next() ) {
		            /* Puis initialisation de la propriété id du bean Utilisateur avec sa valeur */
		            utilisateur.setId( valeursAutoGenerees.getLong( 1 ) );
		        } else {
		            throw new DAOException( "Échec de la création de l'utilisateur en base, aucun ID auto-généré retourné." );
		        }
		    } catch ( SQLException e ) {
		        throw new DAOException( e );
		    } finally {
		        fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
		    }
		
	}

	/*
     * Méthode générique utilisée pour retourner un utilisateur depuis la base
     * de données, correspondant à la requête SQL donnée prenant en paramètres
     * les objets passés en argument.
     */
    private Utilisateur trouver( String sql, Object... objets ) throws DAOException {
    	Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    Utilisateur utilisateur = null;

	    try {
	        /* Récupération d'une connexion depuis la Factory */
	        connexion = daoFactory.getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, sql, false, objets );
	        resultSet = preparedStatement.executeQuery();
	        /* Parcours de la ligne de données de l'éventuel ResulSet retourné */
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
	 * Simple méthode utilitaire permettant de faire la correspondance (le mapping)
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
