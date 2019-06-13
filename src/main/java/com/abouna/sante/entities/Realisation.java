package com.abouna.sante.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"centreId", "indicateurId", "annee", "moisId"}))
public class Realisation implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "centreId")
    private Centre centre;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "indicateurId")
    private Indicateur indicateur;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "moisId")
    private Mois mois;
    
    @Column(name = "annee")
    private Integer annee;
    
    @Column
    private boolean donneesCorrecte;
    
    @Column
    private int valeur;
    
    @Column
    private int valide;
    
    @Column
    private int subvention;

    public Realisation() {
    }

    public Realisation(Centre centre, Indicateur indicateur, Mois mois, Integer annee, Integer valeur) {
        this.centre = centre;
        this.indicateur = indicateur;
        this.mois = mois;
        this.annee = annee;
        this.valeur = valeur;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Centre getCentre() {
        return centre;
    }

    public void setCentre(Centre centre) {
        this.centre = centre;
    }

    public Indicateur getIndicateur() {
        return indicateur;
    }

    public void setIndicateur(Indicateur indicateur) {
        this.indicateur = indicateur;
    }

    public Mois getMois() {
        return mois;
    }

    public void setMois(Mois mois) {
        this.mois = mois;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public int getValeur() {
        return valeur;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }

    public boolean isDonneesCorrecte() {
        return donneesCorrecte;
    }

    public void setDonneesCorrecte(boolean donneesCorrecte) {
        this.donneesCorrecte = donneesCorrecte;
    }

    public int getValide() {
        return valide;
    }

    public void setValide(int valide) {
        this.valide = valide;
    }

    public int getSubvention() {
        return subvention;
    }

    public void setSubvention(int subvention) {
        this.subvention = subvention;
    }
    
    
}
