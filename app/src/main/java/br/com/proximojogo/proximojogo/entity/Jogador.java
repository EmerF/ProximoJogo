package br.com.proximojogo.proximojogo.entity;

import com.google.firebase.database.Exclude;


import java.io.Serializable;

public class Jogador implements Serializable {

    private String telefoneUser;
    private String tipoUser;
    private String nomeUser;
    private String posicao;

    @Exclude
    private String _idUser;

    @Exclude
    public String get_idUser() {
        return _idUser;
    }

    public void set_idUser(String _idUser) {
        this._idUser = _idUser;
    }

    public String getTelefoneUser() {
        return telefoneUser;
    }

    public void setTelefoneUser(String telefoneUser) {
        this.telefoneUser = telefoneUser;
    }

    public String getTipoUser() {
        return tipoUser;
    }

    public void setTipoUser(String tipoUser) {
        this.tipoUser = tipoUser;
    }

    public String getNomeUser() {
        return nomeUser;
    }

    public void setNomeUser(String nomeUser) {
        this.nomeUser = nomeUser;
    }

    public String getPosicao() {
        return posicao;
    }

    public void setPosicao(String posicao) {
        this.posicao = posicao;
    }
}
