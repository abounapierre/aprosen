package com.abouna.abouna.projection;

import java.io.Serializable;

/**
 *
 * @author Vincent Douwe<douwevincent@yahoo.fr>
 */
public class RealisationTrimestrielle implements Serializable{
    
    private Integer cible;
    
    private Integer subvention;
    
    private Long realisation;
    
    private boolean estProvisionne;

    public RealisationTrimestrielle() {
    }

    public RealisationTrimestrielle(Integer cible, Integer subvention, Long realisation, boolean  provision) {
        this.cible = cible;
        this.subvention = subvention;
        this.realisation = realisation;
        this.estProvisionne = provision;
    }

    public Integer getCible() {
        return cible;
    }

    public void setCible(Integer cible) {
        this.cible = cible;
    }

    public Integer getSubvention() {
        return subvention;
    }

    public void setSubvention(Integer subvention) {
        this.subvention = subvention;
    }

    public Long getRealisation() {
        return realisation;
    }

    public void setRealisation(Long realisation) {
        this.realisation = realisation;
    }

    public boolean isEstProvisionne() {
        return estProvisionne;
    }

    public void setEstProvisionne(boolean estProvisionne) {
        this.estProvisionne = estProvisionne;
    }
}
