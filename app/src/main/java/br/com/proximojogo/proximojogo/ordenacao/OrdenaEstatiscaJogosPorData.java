package br.com.proximojogo.proximojogo.ordenacao;

import java.util.Comparator;

import br.com.proximojogo.proximojogo.utils.EstatisticaDeJogos;

/**
 * Created by Ale on 26/02/2018.
 */

public class OrdenaEstatiscaJogosPorData implements Comparator<EstatisticaDeJogos> {
    @Override
    public int compare(EstatisticaDeJogos a1, EstatisticaDeJogos a2) {
        return a1.getDataUltimoComfronto().compareTo(a2.getDataUltimoComfronto());
    }
}
