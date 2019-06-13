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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"categorieId", "centreId", "annee"}))
public class IndicateurCentre implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name ="categorieId")
    private CategorieIndicateur categorieIndicateur;
    
    @ManyToOne
    @JoinColumn(name = "centreId")
    private Centre centre;
    
    @Column(name = "annee")
    private Integer annee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CategorieIndicateur getCategorieIndicateur() {
        return categorieIndicateur;
    }

    public void setCategorieIndicateur(CategorieIndicateur categorieIndicateur) {
        this.categorieIndicateur = categorieIndicateur;
    }

    public Centre getCentre() {
        return centre;
    }

    public void setCentre(Centre centre) {
        this.centre = centre;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }
}
