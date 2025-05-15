package DAO;

import Classes.Employe;
import Classes.MembreEquipage;
import Classes.Pilote;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOMembreEquipage {

    public static ArrayList<Employe> filtrerParDisponibilite(Boolean disponibilite) {
        ArrayList<Employe> listeMembres = new ArrayList<>();
        String requete;

        if (disponibilite == null) {
            // Si disponibilite est null, retourner tous les membres
            requete = "SELECT * FROM membre_equipage";
        } else {
            // Sinon, filtrer par disponibilité
            requete = "SELECT * FROM membre_equipage WHERE disponibilite = ?";
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
                me.setId(rs.getInt("id_membre_equipage"));
                listeMembres.add(me);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du filtrage par disponibilité : " + e.getMessage());
            e.printStackTrace();
        }

        return listeMembres;
    }
    private static Connection cn = Connexion.seConnecter();
    public static ArrayList<Employe> chercher(String recherche) {
        ArrayList<Employe> listeMembres = new ArrayList<>();

        String requete = "SELECT * FROM membre_equipage WHERE " + "nom LIKE '%" + recherche + "%' OR " + "prenom LIKE '%" + recherche + "%' OR " + "num_tel LIKE '%" + recherche + "%' OR " + "email LIKE '%" + recherche + "%' OR " + "cin LIKE '%" + recherche + "%'";

        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()) {
                Employe p = new Pilote();
                p.setId(rs.getInt(1));
                p.setNom(rs.getString("nom"));
                p.setPrenom(rs.getString("prenom"));
                p.setNum_tel(rs.getString("num_tel"));
                p.setEmail(rs.getString("email"));
                p.setDateEmbauche(rs.getDate("date_embauche"));
                p.setDisponibilite(rs.getBoolean("disponibilite"));
                p.setCin(rs.getString("cin"));


                listeMembres.add(p);
            }
            System.out.println("Recherche membres réussie");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche membres");
        }

        return listeMembres;
    }

    public static boolean existsByCin(String cin) {

        String requete = "SELECT COUNT(*) FROM membre_equipage WHERE cin = ?";

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
        ArrayList<Employe> listeMembres = new ArrayList<>();
        String requete = "SELECT * FROM membre_equipage";

        try (
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(requete)) {

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
                me.setId(rs.getInt("id_membre_equipage"));
                listeMembres.add(me);
            }
        } catch (SQLException e) {
            System.err.println("Erreur DAOMembreEquipage.lister(): " + e.getMessage());
            e.printStackTrace();
        }
        return listeMembres;
    }




    public static boolean ajouter(MembreEquipage me) {

        String requete = "insert into membre_equipage values(null,?,?,?,?,?,?,?)";

        try {
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setString(1, me.getNom());
            pst.setString(2, me.getPrenom());
            pst.setString(7, me.getNum_tel());
            pst.setString(3, me.getEmail());
            pst.setDate(4, me.getDateEmbauche());
            pst.setBoolean(5, me.getDisponibilite());
            pst.setString(6, me.getCin());


            int n = pst.executeUpdate();
            if (n >= 1) {
                System.out.println("ajout réussi de membre equipage");
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("problème de requête : " + ex.getMessage());
        }
        return false;
    }

    public static ArrayList<Employe> chercherMembres(String recherche) {
        ArrayList<Employe> listeMembres = new ArrayList<>();

        String requete = "SELECT * FROM membre_equipage WHERE " + "nom LIKE '%" + recherche + "%' OR " + "prenom LIKE '%" + recherche + "%' OR " + "num_tel LIKE '%" + recherche + "%' OR " + "email LIKE '%" + recherche + "%' OR " + "cin LIKE '%" + recherche + "%'";

        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()) {
                Employe me = new MembreEquipage();
                me.setId(rs.getInt("id_membre_equipage"));
                me.setNom(rs.getString("nom"));
                me.setPrenom(rs.getString("prenom"));
                me.setNum_tel(rs.getString("num_tel"));
                me.setEmail(rs.getString("email"));
                me.setDateEmbauche(rs.getDate("date_embauche"));
                me.setDisponibilite(rs.getBoolean("disponibilite"));
                me.setCin(rs.getString("cin"));


                listeMembres.add(me);
            }
            System.out.println("Recherche membre réussie");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche membre");
        }

        return listeMembres;
    }

    public static boolean supprimer(MembreEquipage me) {

        String requete = "delete from membre_equipage where id_membre_equipage = ?";

        try {
            PreparedStatement pst = cn.prepareStatement(requete);
            pst.setInt(1, me.getId());

            int n = pst.executeUpdate();
            if (n >= 1) {
                System.out.println("Suppression réussie de membre");
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Problème de requête de suppression : " + ex.getMessage());
        }
        return false;
    }
    public static boolean modifier(MembreEquipage me) {
        try {
            String sql = "UPDATE membre_equipage SET nom = ?, prenom = ?, email = ?, date_embauche = ?, disponibilite = ?, num_tel = ?,cin=? WHERE id_membre_equipage = ?";

            PreparedStatement pstmt = cn.prepareStatement(sql);
            pstmt.setString(1, me.getNom());
            pstmt.setString(2, me.getPrenom());
            pstmt.setString(3, me.getEmail());
            pstmt.setDate(4, me.getDateEmbauche());
            pstmt.setBoolean(5, me.getDisponibilite());
            pstmt.setString(6, me.getNum_tel());
            pstmt.setString(7, me.getCin());
            pstmt.setInt(8,me.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static List<String> getAllMembreEquipageNames() {
        List<String> membreNames = new ArrayList<>();
        String query = "SELECT nom FROM membre_equipage";

        try (Connection conn = Connexion.seConnecter();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                membreNames.add(rs.getString("nom"));
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des noms des membres d'équipage: " + e.getMessage());
            e.printStackTrace();
        }

        return membreNames;
    }

}
