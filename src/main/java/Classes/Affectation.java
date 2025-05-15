package Classes;

public class Affectation {
    private Avion avion;
    private Vol vol;
    private Pilote pilote;
    private MembreEquipage membreEquipage;

    public Affectation() {
    }

    public Avion getAvion() {
        return avion;
    }

    public void setAvion(Avion avion) {
        this.avion = avion;
    }

    public Vol getVol() {
        return vol;
    }

    public void setVol(Vol vol) {
        this.vol = vol;
    }

    public Pilote getPilote() {
        return pilote;
    }

    public void setPilote(Pilote pilote) {
        this.pilote = pilote;
    }

    public MembreEquipage getMembreEquipage() {
        return membreEquipage;
    }

    public void setMembreEquipage(MembreEquipage membreEquipage) {
        this.membreEquipage = membreEquipage;
    }

    public Affectation(Avion avion, Vol vol, Pilote pilote, MembreEquipage membreEquipage) {
        this.avion = avion;
        this.vol = vol;
        this.pilote = pilote;
        this.membreEquipage = membreEquipage;
    }
}