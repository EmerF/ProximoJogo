package br.com.proximojogo.proximojogo.entity;

/**
 * Created by emer on 14/12/17.
 */

public class Resultado {
    private String idResultado;
    private String time1;
    private String time2;
    private String gols1 = "0";
    private String gols2 = "0";

    public Resultado(String idResultado, String time1, String time2, String gols1, String gols2) {
        this.idResultado = idResultado;
        this.time1 = time1;
        this.time2 = time2;
        this.gols1 = gols1;
        this.gols2 = gols2;
    }
    public Resultado(){

    }
    public String getIdResultado() {
        return idResultado;
    }

    public void setIdResultado(String idResultado) {
        this.idResultado = idResultado;
    }

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public String getTime2() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }

    public String getGols1() {
        return gols1;
    }

    public void setGols1(String gols1) {
        this.gols1 = gols1;
    }

    public String getGols2() {
        return gols2;
    }

    public void setGols2(String gols2) {
        this.gols2 = gols2;
    }
}
