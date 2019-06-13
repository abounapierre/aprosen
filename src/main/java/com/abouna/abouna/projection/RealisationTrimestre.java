package com.abouna.abouna.projection;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class RealisationTrimestre {
    
    private String trimestre;
    
    private long realisation;

    public RealisationTrimestre() {
    }

    public RealisationTrimestre(String trimestre, long realisation) {
        this.trimestre = trimestre;
        this.realisation = realisation;
    }

    public String getTrimestre() {
        return trimestre;
    }

    public void setTrimestre(String trimestre) {
        this.trimestre = trimestre;
    }

    public long getRealisation() {
        return realisation;
    }

    public void setRealisation(long realisation) {
        this.realisation = realisation;
    }
}
