package com.abouna.sante.transfer;

import java.io.Serializable;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class GraphiqueElement implements Serializable{
    
    private String label;
    
    private Double value;

    public GraphiqueElement(String label, Double value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
