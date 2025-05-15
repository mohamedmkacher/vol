package DAO;

import Classes.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TestDAO {
    public static void main(String[] args) {
        Connexion c=new Connexion();
        c.setUser("root");
        c.setPassWord("");
        c.seConnecter();
//        //ArrayList<Pilote> col=DAOPilote.lister();
//        //System.out.println(col.toString());
//        Date d=new Date(2024-02-01);
//        Date d1=new Date(2024-05-10);
//        Date d2=new Date(2024-06-04);
//        Pilote p1=new Pilote("123456","Najjar","Yassine","yassine@",d,Boolean.TRUE,"12365478");
//        Pilote p2=new Pilote("123654","Mohsni","Sarra","Sarra@",d1,Boolean.TRUE,"12365478");
//        Pilote p3=new Pilote("147852","Zayani","Mohamed","Mohamed@",d2,Boolean.TRUE,"12365478");
//        //DAOPilote.ajouter(p1);
//        //DAOPilote.ajouter(p2);
//        //DAOPilote.ajouter(p3);
//        //DAOPilote.supprimer(p1);
//        MembreEquipage me1=new MembreEquipage("852963","Riahi","Nour","Nour@",d,Boolean.TRUE,"12365478");
//        MembreEquipage me2=new MembreEquipage("741852","Mkacher","Hiba","Hiba@",d2,Boolean.TRUE,"12365478");
//        //DAOMembreEquipage.ajouter(me1);
//        //DAOMembreEquipage.ajouter(me2);
//        //DAOMembreEquipage.supprimer(me2);
//        //System.out.println(DAOPilote.chercherParCIN("147852"));
//        //System.out.println(DAOPilote.chercherParNomOuPrenom("","Nour"));
//        //System.out.println(DAOMembreEquipage.chercherParNomOuPrenom("Riahi",""));
//        //System.out.println(DAOAvion.lister());
//        Avion a1=new Avion("m741852","Airbus","modele1",250,Boolean.TRUE);
//        Avion a2=new Avion("m852147","Comac ","modele2",500,Boolean.TRUE);
//        Avion a3=new Avion("j852","airfrance ","modele3",500,Boolean.TRUE);
//        DAOAvion.ajouter(a3);
//        //DAOAvion.ajouter(a2);
//        //DAOAvion.supprimer(a1);
//        //System.out.println(DAOAvion.chercherAvion("m852147"));
//        LocalDateTime depart = LocalDateTime.of(2023, 12, 25, 14, 30);
//        LocalDateTime arrivee = LocalDateTime.of(2023, 12, 25, 17, 00);
//        Avion a=new Avion();
//        a.getId_avion();
//        Pilote p=new Pilote();
//        p.getId();
//        Vol v1=new Vol("vol123",depart,arrivee,"Paris",TypeTrajet.COURT,"Tunisie", 200,a,p);
//        //DAOVol.ajouter(v1);
//        //DAOVol.supprimer("vol123");
//


    }}
