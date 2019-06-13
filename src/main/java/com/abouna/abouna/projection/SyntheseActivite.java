package com.abouna.abouna.projection;

import java.util.HashMap;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class SyntheseActivite {
    
    private String indicateur;
    
    private Integer subvention;
    
    private boolean estProvisionne;
    
    private HashMap<String, Long> values;
    
    public SyntheseActivite(){
        values = new HashMap<String, Long>();
    }

    public String getIndicateur() {
        return indicateur;
    }

    public void setIndicateur(String indicateur) {
        this.indicateur = indicateur;
    }

    public Integer getSubvention() {
        return subvention;
    }

    public void setSubvention(Integer subvention) {
        this.subvention = subvention;
    }

    public HashMap<String, Long> getValues() {
        return values;
    }

    public void setValues(HashMap<String, Long> values) {
        this.values = values;
    }

    public boolean isEstProvisionne() {
        return estProvisionne;
    }

    public void setEstProvisionne(boolean estProvisionne) {
        this.estProvisionne = estProvisionne;
    }
    
}
