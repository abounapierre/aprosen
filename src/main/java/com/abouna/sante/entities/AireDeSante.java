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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"nom"}))
public class AireDeSante implements Serializable{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String nom;
    
    

    public AireDeSante() {
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
