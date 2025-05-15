package Classes;

import java.sql.Date;

public abstract class Employe {
    protected int id;
    protected String nom;
    protected String prenom;
    protected String email;
    protected Date dateEmbauche;
    protected Boolean disponibilite;
    protected String cin;
    protected String num_tel;

    public Employe(String cin, String nom, String prenom, String email, Date dateEmbauche, Boolean disponibilite, String num_tel) {

        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.dateEmbauche = dateEmbauche;
        this.disponibilite = disponibilite;
        this.cin = cin;
        this.num_tel=num_tel;
    }

    public Employe() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateEmbauche() {
        return dateEmbauche;
    }

    public void setDateEmbauche(Date dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    public Boolean getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(Boolean disponibilite) {
        this.disponibilite = disponibilite;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }
    public String getNum_tel() {
        return num_tel;
    }
    public void setNum_tel(String num_tel) {
        this.num_tel = num_tel;
    }
}
