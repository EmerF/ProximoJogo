package br.com.proximojogo.proximojogo.entity;

import com.google.firebase.database.Exclude;

public class AgendaDO {
    private String idAgenda;
    private String idResultado;
    private String status;
    private String arena;
    private Long data;
    private int diaSemana;
    private String evento;
    private Long hora;
    private String idUser;
    private String observacao;
    private Double valor;

    @Exclude
    private Boolean dataFutura = true;

    @Exclude
    private Resultado resultado;

    public AgendaDO() {
    }

    public String getIdAgenda() {
        return idAgenda;
    }

    public void setIdAgenda(String idAgenda) {
        this.idAgenda = idAgenda;
    }

    public String getIdResultado() {
        return idResultado;
    }

    public void setIdResultado(String idResultado) {
        this.idResultado = idResultado;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getArena() {
        return arena;
    }

    public void setArena(String arena) {
        this.arena = arena;
    }

    public Long getData() {
        return data;
    }

    public void setData(Long data) {
        this.data = data;
    }

    public int getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(int diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public Long getHora() {
        return hora;
    }

    public void setHora(Long hora) {
        this.hora = hora;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getObservacao() {
        return observacao;
    }
    @Exclude
    public Boolean getDataFutura() {
        return dataFutura;
    }

    public void setDataFutura(Boolean dataFutura) {
        this.dataFutura = dataFutura;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
    @Exclude
    public Resultado getResultado() {
        return resultado;
    }

    public void setResultado(Resultado resultado) {
        this.resultado = resultado;
    }
}
