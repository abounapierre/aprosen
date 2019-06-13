/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abouna.sante.entities;

import java.io.Serializable;
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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"annee", "aireDeSanteId"}))
public class Population implements Serializable{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
   private Long id;
   private long valeur;
   private Integer annee;
    @ManyToOne(optional= false)
    @JoinColumn(name = "aireDeSanteId")
   private AireDeSante aireDeSante;

    public AireDeSante getAireDeSante() {
        return aireDeSante;
    }

    public void setAireDeSante(AireDeSante aireDeSante) {
        this.aireDeSante = aireDeSante;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getValeur() {
        return valeur;
    }

    public void setValeur(long valeur) {
        this.valeur = valeur;
    }

    public Population(long valeur, Integer annee, AireDeSante aireDeSante) {
        this.valeur = valeur;
        this.annee = annee;
        this.aireDeSante = aireDeSante;
    }

    public Population() {
    }
   
   
}
