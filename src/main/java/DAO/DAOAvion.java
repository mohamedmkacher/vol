package DAO;

import Classes.Avion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOAvion {
    public static ArrayList<Avion> lister() {
        ArrayList<Avion> listeAvions = new ArrayList<>();
        Connection cn = Connexion.seConnecter();
        String requete = "SELECT * FROM avion;";
        int id_avion, capacite;
        String matricule, marque, modele;
        boolean disponible;
        Avion a;
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                id_avion = rs.getInt("id_avion");
                matricule = rs.getString("matricule");
                marque = rs.getString("marque");
                modele = rs.getString("modele");
                capacite = rs.getInt("capacite");
                disponible = rs.getBoolean("disponible");

                a = new Avion(matricule, marque, modele, capacite, disponible);
                a.setId_avion(id_avion);

                listeAvions.add(a);
            }
            System.out.println("Consultation avion OK");
        } catch (SQLException e) {
            System.out.println("Problème de consultation avion");
        }
        return listeAvions;
    }

    private static Connection cn = Connexion.seConnecter();

    public static boolean ajouter(Avion a) {

        String requete = "insert into avion values(null,?,?,?,?,?)";

        try {
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setString(1, a.getMatricule());
            pst.setString(2, a.getMarque());
            pst.setString(3, a.getModele());
            pst.setInt(4, a.getCapacite());
            pst.setBoolean(5, a.isDisponible());

            int n = pst.executeUpdate();
            if (n >= 1) {
                System.out.println("ajout réussi d'avion");
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("problème de requête : " + ex.getMessage());
        }
        return false;
    }

    public static boolean supprimer(Avion a) {

        String requete = "delete from avion where matricule = ?";

        try {
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setString(1, a.getMatricule());

            int n = pst.executeUpdate();
            if (n >= 1) {
                System.out.println("Suppression réussie d'avion");
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Problème de requête de suppression : " + ex.getMessage());
        }
        return false;
    }

    public static ArrayList<Avion> chercherAvion(String recherche) {
        ArrayList<Avion> listeAvions = new ArrayList<>();

        String requete = "SELECT * FROM avion WHERE "
                + "matricule LIKE '%" + recherche + "%' OR "
                + "marque LIKE '%" + recherche + "%' OR "
                + "modele LIKE '%" + recherche + "%'";

        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()) {
                Avion a = new Avion();
                a.setId_avion(rs.getInt("id_avion"));
                a.setMatricule(rs.getString("matricule"));
                a.setMarque(rs.getString("marque"));
                a.setModele(rs.getString("modele"));
                a.setCapacite(rs.getInt("capacite"));
                a.setDisponible(rs.getBoolean("disponible"));

                listeAvions.add(a);
            }
            System.out.println("Recherche avion réussie");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche avion");
        }

        return listeAvions;
    }

    public static boolean modifier(Avion avion) {
        // Implémentez la logique de mise à jour dans la base de données
        // Exemple avec JDBC:
        String sql = "UPDATE avion SET marque=?, modele=?, capacite=?, disponible=? WHERE matricule=?";

        try {
            PreparedStatement pstmt = cn.prepareStatement(sql);

            pstmt.setString(1, avion.getMarque());
            pstmt.setString(2, avion.getModele());
            pstmt.setInt(3, avion.getCapacite());
            pstmt.setBoolean(4, avion.isDisponible());
            pstmt.setString(5, avion.getMatricule());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean existeMatricule(String matricule) {
        String requete = "SELECT COUNT(*) FROM avion WHERE matricule = ?";

        try {
            PreparedStatement pst = cn.prepareStatement(requete);

            pst.setString(1, matricule);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            System.out.println("Erreur vérification matricule: " + ex.getMessage());
        }
        return false;
    }
    public static Avion getAvionById(int idAvion) {
        String requete = "SELECT * FROM avion WHERE id_avion = ?";
        Avion avion = null;

        try {
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setInt(1, idAvion);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                avion = new Avion();
                avion.setId_avion(rs.getInt("id_avion"));
                avion.setMatricule(rs.getString("matricule"));
                avion.setMarque(rs.getString("marque"));
                avion.setModele(rs.getString("modele"));
                avion.setCapacite(rs.getInt("capacite"));
                avion.setDisponible(rs.getBoolean("disponible"));
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la récupération de l'avion: " + ex.getMessage());
        }
        return avion;
    }

    public static List<String> getAllAvionIdentifiers() {
        List<String> identifiers = new ArrayList<>();
        String query = "SELECT matricule FROM avion";

        try (Connection conn = Connexion.seConnecter();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                identifiers.add(rs.getString(1));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des identifiants d'avions: " + e.getMessage());
            e.printStackTrace();
        }

        return identifiers;
    }

}
