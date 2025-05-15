package Classes;

import java.sql.Date;

public class Pilote extends Employe{

    public Pilote() {
    }

    public Pilote(String cin, String nom, String prenom, String email, Date dateEmbauche, Boolean disponibilite, String num_tel) {
        super(cin, nom, prenom, email, dateEmbauche, disponibilite,num_tel);
    }

}
