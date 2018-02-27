package br.com.proximojogo.proximojogo.ordenacao;

import java.util.Comparator;

import br.com.proximojogo.proximojogo.entity.AgendaDO;
import br.com.proximojogo.proximojogo.utils.FormatarData;

/**
 * Created by Ale on 26/02/2018.
 */

public class OrdenaEventoTimeData implements Comparator<AgendaDO> {
    @Override
    public int compare(AgendaDO a1, AgendaDO a2) {
        int i = a1.getAdversario().compareTo(a2.getAdversario());
        if (i != 0) {
            return i;
        }
        return a1.getData().compareTo(a2.getData());
    }
}
