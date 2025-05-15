package DAO;

import Classes.Vol;
import Classes.Avion;
import Classes.Pilote;
import Classes.TypeTrajet;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DAOVol {
    private static Connection cn = Connexion.seConnecter();

    // Formatters for date and time
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static boolean ajouter(Vol vol) {
        try {
            String sql = "INSERT INTO vol (numero_vol, date_heure_depart, date_heure_arrivee, "
                    + "destination, provenance, type_trajet, nombre_passagers, id_avion) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, vol.getNumero_vol());
            ps.setObject(2, vol.getDate_heure_depart());
            ps.setObject(3, vol.getDate_heure_arrivee());
            ps.setString(4, vol.getDestination());
            ps.setString(5, vol.getProvenance());
            ps.setString(6, vol.getType_trajet().toString());
            ps.setInt(7, vol.getNombre_passagers());

            // Gestion de l'avion null
            if (vol.getAvion() != null) {
                ps.setInt(8, vol.getAvion().getId_avion());
            } else {
                ps.setNull(8, java.sql.Types.INTEGER); // ou une valeur par défaut si votre DB le permet
            }

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean modifier(Vol vol) {
        try {
            String sql = "UPDATE vol SET numero_vol = ?, date_heure_depart = ?, "
                    + "date_heure_arrivee = ?, destination = ?, provenance = ?, "
                    + "type_trajet = ?, nombre_passagers = ?, id_avion = ? "
                    + "WHERE id_vol = ?";

            PreparedStatement ps = cn.prepareStatement(sql);

            // Set des paramètres
            ps.setString(1, vol.getNumero_vol());
            ps.setObject(2, vol.getDate_heure_depart());
            ps.setObject(3, vol.getDate_heure_arrivee());
            ps.setString(4, vol.getDestination());
            ps.setString(5, vol.getProvenance());
            ps.setString(6, vol.getType_trajet().toString());
            ps.setInt(7, vol.getNombre_passagers());

            // Gestion de l'avion (peut être null)
            if (vol.getAvion() != null) {
                ps.setInt(8, vol.getAvion().getId_avion());
            } else {
                ps.setNull(8, Types.INTEGER);
            }

            ps.setInt(9, vol.getId_vol());

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du vol: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean supprimer(int idVol) {
        String query = "DELETE FROM vol WHERE id_vol=?";

        try {
            PreparedStatement pst = cn.prepareStatement(query);
            pst.setInt(1, idVol);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du vol: " + e.getMessage());
        }
        return false;
    }
    public static boolean supprimer(Vol vol) {
        return supprimer(vol.getId_vol());  // Appelle la méthode existante
    }

    public static List<Vol> lister() {
        List<Vol> vols = new ArrayList<>();
        String query = "SELECT * FROM vol ORDER BY date_heure_depart";

        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Vol vol = new Vol();
                vol.setId_vol(rs.getInt("id_vol"));
                vol.setNumero_vol(rs.getString("numero_vol"));
                vol.setDate_heure_depart(rs.getTimestamp("date_heure_depart").toLocalDateTime());
                vol.setDate_heure_arrivee(rs.getTimestamp("date_heure_arrivee").toLocalDateTime());
                vol.setDestination(rs.getString("destination"));
                vol.setProvenance(rs.getString("provenance"));
                vol.setType_trajet(TypeTrajet.valueOf(rs.getString("type_trajet")));
                vol.setNombre_passagers(rs.getInt("nombre_passagers"));

                // Get associated aircraft and pilot
                Avion avion = DAOAvion.getAvionById(rs.getInt("id_avion"));
                Pilote pilote = DAOPilote.getPiloteById(rs.getInt("id_pilote"));

                vol.setAvion(avion);
                vol.setPilote(pilote);

                vols.add(vol);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des vols: " + e.getMessage());
        }
        return vols;
    }

    public static List<Vol> rechercherVols(String searchTerm) {
        List<Vol> vols = new ArrayList<>();
        String query = "SELECT * FROM vol WHERE numero_vol LIKE ? OR destination LIKE ? OR provenance LIKE ? "
                + "ORDER BY date_heure_depart";

        try {
            PreparedStatement pst = cn.prepareStatement(query);
            pst.setString(1, "%" + searchTerm + "%");
            pst.setString(2, "%" + searchTerm + "%");
            pst.setString(3, "%" + searchTerm + "%");

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Vol vol = new Vol();
                vol.setId_vol(rs.getInt("id_vol"));
                vol.setNumero_vol(rs.getString("numero_vol"));
                vol.setDate_heure_depart(rs.getTimestamp("date_heure_depart").toLocalDateTime());
                vol.setDate_heure_arrivee(rs.getTimestamp("date_heure_arrivee").toLocalDateTime());
                vol.setDestination(rs.getString("destination"));
                vol.setProvenance(rs.getString("provenance"));
                vol.setType_trajet(TypeTrajet.valueOf(rs.getString("type_trajet")));
                vol.setNombre_passagers(rs.getInt("nombre_passagers"));

                // Get associated aircraft and pilot
                Avion avion = DAOAvion.getAvionById(rs.getInt("id_avion"));
                Pilote pilote = DAOPilote.getPiloteById(rs.getInt("id_pilote"));

                vol.setAvion(avion);
                vol.setPilote(pilote);

                vols.add(vol);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche des vols: " + e.getMessage());
        }
        return vols;
    }

    public static Vol getVolById(int idVol) {
        String query = "SELECT * FROM vol WHERE id_vol=?";

        try {
            PreparedStatement pst = cn.prepareStatement(query);
            pst.setInt(1, idVol);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                Vol vol = new Vol();
                vol.setId_vol(rs.getInt("id_vol"));
                vol.setNumero_vol(rs.getString("numero_vol"));
                vol.setDate_heure_depart(rs.getTimestamp("date_heure_depart").toLocalDateTime());
                vol.setDate_heure_arrivee(rs.getTimestamp("date_heure_arrivee").toLocalDateTime());
                vol.setDestination(rs.getString("destination"));
                vol.setProvenance(rs.getString("provenance"));
                vol.setType_trajet(TypeTrajet.valueOf(rs.getString("type_trajet")));
                vol.setNombre_passagers(rs.getInt("nombre_passagers"));

                // Get associated aircraft and pilot
                Avion avion = DAOAvion.getAvionById(rs.getInt("id_avion"));
                Pilote pilote = DAOPilote.getPiloteById(rs.getInt("id_pilote"));

                vol.setAvion(avion);
                vol.setPilote(pilote);

                return vol;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du vol: " + e.getMessage());
        }
        return null;
    }

    public static boolean existeNumeroVol(String numeroVol) {
        String query = "SELECT COUNT(*) FROM vol WHERE numero_vol=?";

        try {
            PreparedStatement pst = cn.prepareStatement(query);
            pst.setString(1, numeroVol);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification du numéro de vol: " + e.getMessage());
        }
        return false;
    }

    public static boolean estAvionDisponible(int idAvion, LocalDateTime depart, LocalDateTime arrivee) {
        String query = "SELECT COUNT(*) FROM vol WHERE id_avion=? AND "
                + "((date_heure_depart BETWEEN ? AND ?) OR "
                + "(date_heure_arrivee BETWEEN ? AND ?) OR "
                + "(date_heure_depart <= ? AND date_heure_arrivee >= ?))";

        try {
            PreparedStatement pst = cn.prepareStatement(query);
            pst.setInt(1, idAvion);
            pst.setTimestamp(2, Timestamp.valueOf(depart));
            pst.setTimestamp(3, Timestamp.valueOf(arrivee));
            pst.setTimestamp(4, Timestamp.valueOf(depart));
            pst.setTimestamp(5, Timestamp.valueOf(arrivee));
            pst.setTimestamp(6, Timestamp.valueOf(depart));
            pst.setTimestamp(7, Timestamp.valueOf(arrivee));

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de disponibilité de l'avion: " + e.getMessage());
        }
        return false;
    }

    public static boolean estPiloteDisponible(int idPilote, LocalDateTime depart, LocalDateTime arrivee) {
        String query = "SELECT COUNT(*) FROM vol WHERE id_pilote=? AND "
                + "((date_heure_depart BETWEEN ? AND ?) OR "
                + "(date_heure_arrivee BETWEEN ? AND ?) OR "
                + "(date_heure_depart <= ? AND date_heure_arrivee >= ?))";

        try {
            PreparedStatement pst = cn.prepareStatement(query);
            pst.setInt(1, idPilote);
            pst.setTimestamp(2, Timestamp.valueOf(depart));
            pst.setTimestamp(3, Timestamp.valueOf(arrivee));
            pst.setTimestamp(4, Timestamp.valueOf(depart));
            pst.setTimestamp(5, Timestamp.valueOf(arrivee));
            pst.setTimestamp(6, Timestamp.valueOf(depart));
            pst.setTimestamp(7, Timestamp.valueOf(arrivee));

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de disponibilité du pilote: " + e.getMessage());
        }
        return false;
    }
}