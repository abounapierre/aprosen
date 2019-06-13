package com.abouna.abouna.projection;

import java.io.Serializable;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class NombreRealisation implements Serializable{
    
    private String mois;
    
    private long valeur;
    
    public NombreRealisation(){
        
    }

    public NombreRealisation(String mois, long valeur) {
        this.mois = mois;
        this.valeur = valeur;
    }

    public String getMois() {
        return mois;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

    public long getValeur() {
        return valeur;
    }

    public void setValeur(long valeur) {
        this.valeur = valeur;
    }   
}