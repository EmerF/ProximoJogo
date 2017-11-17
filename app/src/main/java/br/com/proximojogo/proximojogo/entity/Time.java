package br.com.proximojogo.proximojogo.entity;

import java.io.Serializable;

/**
 * Created by emer on 16/09/17.
 */

public class Time implements Serializable {
    private String idTime;
    private String idResponsavel;
    private String nomeTime;
    private String responsavelTime;
    private String telefoneResponsavel;

    public String getIdTime() {
        return idTime;
    }

    public void setIdTime(String idTime) {
        this.idTime = idTime;
    }

    public String getIdResponsavel() {
        return idResponsavel;
    }

    public void setIdResponsavel(String idResponsavel) {
        this.idResponsavel = idResponsavel;
    }

    public String getNomeTime() {
        return nomeTime;
    }

    public void setNomeTime(String nomeTime) {
        this.nomeTime = nomeTime;
    }

    public String getResponsavelTime() {
        return responsavelTime;
    }

    public void setResponsavelTime(String responsavelTime) {
        this.responsavelTime = responsavelTime;
    }

    public String getTelefoneResponsavel() {
        return telefoneResponsavel;
    }

    public void setTelefoneResponsavel(String telefoneResponsavel) {
        this.telefoneResponsavel = telefoneResponsavel;
    }
}
