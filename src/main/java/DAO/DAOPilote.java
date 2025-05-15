package DAO;

import Classes.Employe;
import Classes.MembreEquipage;
import Classes.Pilote;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOPilote {
    private static Connection cn = Connexion.seConnecter();
    public static boolean existsByCin(String cin) {
        String requete = "SELECT COUNT(*) FROM pilote WHERE cin = ?";

        try (
                PreparedStatement pst = cn.prepareStatement(requete)) {

            pst.setString(1, cin);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Erreur DAOPilote.existsByCin(): " + e.getMessage());
        }
        return false;
    }
    public static ArrayList<Employe> lister() {
        ArrayList<Employe> listePilotes = new ArrayList<>();
        String requete = "SELECT * FROM pilote";

        try (
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(requete)) {

            while (rs.next()) {
                Pilote p = new Pilote(
                        rs.getString("cin"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getDate("date_embauche"),
                        rs.getBoolean("disponibilite"),
                        rs.getString("num_tel")
                );
                p.setId(rs.getInt("id_pilote"));
                listePilotes.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Erreur DAOPilote.lister(): " + e.getMessage());
            e.printStackTrace();
        }
        return listePilotes;
    }
    public static ArrayList<Employe> filtrerParDisponibilite(Boolean disponibilite) {
        ArrayList<Employe> listePilotes = new ArrayList<>();
        String requete;

        if (disponibilite == null) {
            // Si disponibilite est null, retourner tous les membres
            requete = "SELECT * FROM pilote";
        } else {
            // Sinon, filtrer par disponibilité
            requete = "SELECT * FROM pilote WHERE disponibilite = ?";
        }

        try {
            PreparedStatement pst = cn.prepareStatement(requete);

            if (disponibilite != null) {
                pst.setBoolean(1, disponibilite);
            }

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                MembreEquipage me = new MembreEquipage(
                        rs.getString("cin"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getDate("date_embauche"),
                        rs.getBoolean("disponibilite"),
                        rs.getString("num_tel")
                );
                me.setId(rs.getInt("id_pilote"));
                listePilotes.add(me);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du filtrage par disponibilité : " + e.getMessage());
            e.printStackTrace();
        }

        return listePilotes;
    }

    public static boolean ajouter(Pilote p) {
        String requete = "INSERT INTO pilote VALUES(null,?,?,?,?,?,?,?)";

        try (
                PreparedStatement pst = cn.prepareStatement(requete)) {

            pst.setString(1, p.getNom());
            pst.setString(2, p.getPrenom());
            pst.setString(3, p.getEmail());
            pst.setDate(4, p.getDateEmbauche());

            pst.setBoolean(5, p.getDisponibilite());
            pst.setString(6, p.getCin());
            pst.setString(7, p.getNum_tel());

            return pst.executeUpdate() >= 1;
        } catch (SQLException ex) {
            System.err.println("Erreur DAOPilote.ajouter(): " + ex.getMessage());
            return false;
        }
    }

    public static ArrayList<Employe> chercherPilotes(String recherche) {
        ArrayList<Employe> listePilotes = new ArrayList<>();

        String requete = "SELECT * FROM pilote WHERE " + "nom LIKE '%" + recherche + "%' OR " + "prenom LIKE '%" + recherche + "%' OR " + "num_tel LIKE '%" + recherche + "%' OR " + "email LIKE '%" + recherche + "%' OR " + "cin LIKE '%" + recherche + "%'";

        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()) {
                Employe p = new Pilote();
                p.setId(rs.getInt("id_pilote"));
                p.setNom(rs.getString("nom"));
                p.setPrenom(rs.getString("prenom"));
                p.setNum_tel(rs.getString("num_tel"));
                p.setEmail(rs.getString("email"));
                p.setDateEmbauche(rs.getDate("date_embauche"));
                p.setDisponibilite(rs.getBoolean("disponibilite"));
                p.setCin(rs.getString("cin"));


                listePilotes.add(p);
            }
            System.out.println("Recherche pilote réussie");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche pilote");
        }

        return listePilotes;
    }

    public static boolean supprimer(Pilote p) {

        String requete = "delete from pilote where id_pilote = ?";

        try {
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setInt(1, p.getId());

            int n = pst.executeUpdate();
            if (n >= 1) {
                System.out.println("Suppression réussie de pilote");
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Problème de requête de suppression : " + ex.getMessage());
        }
        return false;
    }

    public static boolean modifier(Pilote p) {
        try {
            String sql = "UPDATE pilote SET nom = ?, prenom = ?, email = ?, date_embauche = ?, disponibilite = ?, num_tel = ?,cin=? WHERE id_pilote = ?";

            PreparedStatement pstmt = cn.prepareStatement(sql);
            pstmt.setString(1, p.getNom());
            pstmt.setString(2, p.getPrenom());
            pstmt.setString(3, p.getEmail());
            pstmt.setDate(4, p.getDateEmbauche());
            pstmt.setBoolean(5, p.getDisponibilite());
            pstmt.setString(6, p.getNum_tel());
            pstmt.setString(7, p.getCin());
            pstmt.setInt(8,p.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static Pilote getPiloteById(int idPilote) {
        String requete = "SELECT * FROM pilote WHERE id_pilote = ?";
        Pilote pilote = null;

        try  {
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setInt(1, idPilote);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // Création du pilote avec les données de la base
                pilote = new Pilote(
                        rs.getString("cin"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getDate("date_embauche"),
                        rs.getBoolean("disponibilite"),
                        rs.getString("num_tel")
                );
                pilote.setId(rs.getInt("id_pilote"));

                // Si votre classe Pilote a d'autres attributs spécifiques (comme numero_licence)
                // vous devrez les ajouter ici
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la récupération du pilote par ID: " + ex.getMessage());
        }
        return pilote;
    }
    public static List<String> getAllPiloteNames() {
        List<String> piloteNames = new ArrayList<>();
        String query = "SELECT nom FROM pilote";

        try (Connection conn =Connexion.seConnecter();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                piloteNames.add(rs.getString("nom"));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des noms des pilotes: " + e.getMessage());
            e.printStackTrace();
        }

        return piloteNames;
    }
}
