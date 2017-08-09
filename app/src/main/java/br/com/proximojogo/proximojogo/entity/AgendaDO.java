package br.com.proximojogo.proximojogo.entity;

public class AgendaDO {
    private String _idAgenda;
    private String _status;
    private String _adversario;
    private String _arena;
    private Long _data;
    private int _diaSemana;
    private String _evento;
    private Long _hora;
    private String _idUser;
    private String _observacao;
    private String _times;
    private Double _valor;

    public AgendaDO() {
    }

    public String getIdAgenda() {
        return _idAgenda;
    }

    public void setIdAgenda(final String _idAgenda) {
        this._idAgenda = _idAgenda;
    }

    public String getStatus() {
        return _status;
    }

    public void setStatus(final String _status) {
        this._status = _status;
    }

    public String getAdversario() {
        return _adversario;
    }

    public void setAdversario(final String _adversario) {
        this._adversario = _adversario;
    }

    public String getArena() {
        return _arena;
    }

    public void setArena(final String _arena) {
        this._arena = _arena;
    }

    public Long getData() {
        return _data;
    }

    public void setData(final Long _data) {
        this._data = _data;
    }

    public int getDiaSemana() {
        return _diaSemana;
    }

    public void setDiaSemana(final int _diaSemana) {
        this._diaSemana = _diaSemana;
    }

    public String getEvento() {
        return _evento;
    }

    public void setEvento(final String _evento) {
        this._evento = _evento;
    }

    public Long getHora() {
        return _hora;
    }

    public void setHora(final Long _hora) {
        this._hora = _hora;
    }

    public String getIdUser() {
        return _idUser;
    }

    public void setIdUser(final String _idUser) {
        this._idUser = _idUser;
    }

    public String getObservacao() {
        return _observacao;
    }

    public void setObservacao(final String _observacao) {
        this._observacao = _observacao;
    }

    public String getTimes() {
        return _times;
    }

    public void setTimes(final String _times) {
        this._times = _times;
    }

    public Double getValor() {
        return _valor;
    }

    public void setValor(final Double _valor) {
        this._valor = _valor;
    }

}
