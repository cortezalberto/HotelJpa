package org.jcr.entidades;


import java.io.Serializable;

public class ReservaException extends Exception implements Serializable {
    public ReservaException(String message) {
        super(message);
    }
}
