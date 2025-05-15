package Classes;

public class Avion {
    private int id_avion;
    private String matricule;
    private String marque;
    private String modele;
    private int capacite;
    private boolean disponible;

    public boolean isDisponible() {
        return disponible;
    }

    public Avion() {
    }

    public Avion(String matricule, String marque, String modele, int capacite, boolean disponible) {
        this.matricule = matricule;
        this.marque = marque;
        this.modele = modele;
        this.capacite = capacite;
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "Avion{" +
                "id_avion=" + id_avion +
                ", matricule='" + matricule + '\'' +
                ", marque='" + marque + '\'' +
                ", modele='" + modele + '\'' +
                ", capacite=" + capacite +
                ", disponible=" + disponible +
                '}';
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public int getId_avion() {
        return id_avion;
    }

    public void setId_avion(int id_avion) {
        this.id_avion = id_avion;
    }


}
