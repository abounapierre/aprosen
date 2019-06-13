package com.abouna.abouna.projection;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author Vincent Douwe<douwevincent@yahoo.fr>
 */
public class RealisationProjection implements Serializable{
    
    private String indicateur;
    
    private Integer subvention;
    
    private Integer cible;
    
    private boolean estProvisionne;
    
    private Long total = 0L;
    
    private HashMap<String,Long> realisations;

    public RealisationProjection() {
        
        realisations = new HashMap<String, Long>();
    }

    public RealisationProjection(String indicateur, Integer subvention, Integer cible, boolean  provision) {
        this();
        this.indicateur = indicateur;
        this.subvention = subvention;
        this.cible = cible;
        this.estProvisionne = provision;
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

    public Integer getCible() {
        return cible;
    }

    public void setCible(Integer cible) {
        this.cible = cible;
    }

    public HashMap<String, Long> getRealisations() {
        return realisations;
    }

    public void setRealisations(HashMap<String, Long> realisations) {
        this.realisations = realisations;
    }
    
    public void setTotal(Long val){
        this.total = val;
    }
    
    public Long getTotal(){        
        return total;
    }

    public boolean isEstProvisionne() {
        return estProvisionne;
    }

    public void setEstProvisionne(boolean estProvisionne) {
        this.estProvisionne = estProvisionne;
    }
    
    public Long getRealisationTotale(){
        long result = 0L;
        for (Long object : realisations.values()) {
            result += object;
        }
        return result;
    }
}
