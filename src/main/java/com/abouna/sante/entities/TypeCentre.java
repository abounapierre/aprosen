
package com.abouna.sante.entities;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public enum TypeCentre {
    
    prive("Privé"), publique("Publique"), confessionnel("Confessionnel");

    private String nom;
    
    TypeCentre(String n){
        this.nom = n;
    }
    
    @Override
    public String toString() {
        return nom;
    }
    
    
    
}
