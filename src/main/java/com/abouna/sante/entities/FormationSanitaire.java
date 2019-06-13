/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"nom", "aireDeSanteId"}))
public class FormationSanitaire implements Serializable{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String nom;
    @ManyToOne(optional= false)
    @JoinColumn(name="aireDeSanteId")
    private AireDeSante aireDeSante;

    public FormationSanitaire() {
    }

    public AireDeSante getAireDeSante() {
        return aireDeSante;
    }

    public void setAireDeSante(AireDeSante aireDeSante) {
        this.aireDeSante = aireDeSante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    
    
}
