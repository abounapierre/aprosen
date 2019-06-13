package com.abouna.sante.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
@Entity
public class Indicateur implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String code;
    
    @Column
    private String designation;
    
    @ManyToOne(optional = false)
    private CategorieIndicateur categorie;
    
    @ManyToOne(optional = false)
    private SousCategorie sousCategorie;
    
    @Column
    private Double poids;
    
    @Column
    private boolean estFige = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public CategorieIndicateur getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieIndicateur categorie) {
        this.categorie = categorie;
    }

    public Double getPoids() {
        return poids;
    }

    public void setPoids(Double poids) {
        this.poids = poids;
    }

    public boolean isEstFige() {
        return estFige;
    }

    public void setEstFige(boolean estFige) {
        this.estFige = estFige;
    }

    public SousCategorie getSousCategorie() {
        return sousCategorie;
    }

    public void setSousCategorie(SousCategorie sousCategorie) {
        this.sousCategorie = sousCategorie;
    }

    @Override
    public String toString() {
        return code ;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (this.code != null ? this.code.hashCode() : 0);
        hash = 13 * hash + (this.designation != null ? this.designation.hashCode() : 0);
        hash = 13 * hash + (this.sousCategorie != null ? this.sousCategorie.hashCode() : 0);
        hash = 13 * hash + (this.poids != null ? this.poids.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Indicateur other = (Indicateur) obj;
        if ((this.code == null) ? (other.code != null) : !this.code.equals(other.code)) {
            return false;
        }
        return true;
    }
}