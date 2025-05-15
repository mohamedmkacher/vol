package Classes;

import java.sql.Date;

public class MembreEquipage extends Employe implements Comparable<MembreEquipage> {
    public MembreEquipage(String cin, String nom, String prenom, String email, Date dateEmbauche, Boolean disponibilite, String num_tel) {
        super(cin, nom, prenom, email, dateEmbauche, disponibilite,
                    num_tel);
    }

    public MembreEquipage() {
    }

    @Override
    public int compareTo(MembreEquipage me) {
        return this.cin.compareTo(me.cin);
    }
}
