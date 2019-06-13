package com.abouna.sante.transfer;

import java.io.Serializable;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class DistrictRealisation implements Serializable{
    
    private String district;
    
    private long valeur;

    public DistrictRealisation(String district, long valeur) {
        this.district = district;
        this.valeur = valeur;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public long getValeur() {
        return valeur;
    }

    public void setValeur(long valeur) {
        this.valeur = valeur;
    }
}
