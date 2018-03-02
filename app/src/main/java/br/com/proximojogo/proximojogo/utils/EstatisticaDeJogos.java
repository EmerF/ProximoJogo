package br.com.proximojogo.proximojogo.utils;

/**
 * Created by Ale on 26/02/2018.
 */

public class EstatisticaDeJogos {

    public EstatisticaDeJogos(){

    }

    public EstatisticaDeJogos(Long dataUltimoComfronto, String time1, String time2, String obs) {
        this.dataUltimoComfronto = dataUltimoComfronto;
        this.time1 = time1;
        this.time2 = time2;
        this.obs = obs;
    }

    private Long dataUltimoComfronto;
    private String time1;
    private String time2;
    private Integer vitoriasTime1;
    private Integer vitoriasTime2;
    private Integer derrotasTime1;
    private Integer derrotasTime2;
    private Integer empates;
    private Integer golsTime1;
    private Integer golsTime2;
    private String obs;

    public Long getDataUltimoComfronto() {
        return dataUltimoComfronto;
    }

    public String dataFormatada() {
        return FormatarData.getDf().format(this.dataUltimoComfronto);
    }

    public void setDataUltimoComfronto(Long dataUltimoComfronto) {
        this.dataUltimoComfronto = dataUltimoComfronto;
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

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    @Override
    public String toString() {
        return "EstatisticaJogos{" +
                "dataUltimoComfronto=" + dataUltimoComfronto +
                ", time1='" + time1 + '\'' +
                ", time2='" + time2 + '\'' +
                ", obs='" + obs + '\'' +
                '}';
    }
}
