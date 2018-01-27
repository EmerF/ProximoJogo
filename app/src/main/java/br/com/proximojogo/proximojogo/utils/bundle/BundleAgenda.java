package br.com.proximojogo.proximojogo.utils.bundle;

import android.os.Bundle;

import br.com.proximojogo.proximojogo.entity.AgendaDO;

/**
 * Created by emer on 28/12/17.
 * Para os casos de preencher um bundle com objeto AgendaDO
 * Casos de clique na lista para detalhar objeto agenda
 */

public class BundleAgenda {
   private Bundle bundle;
   public  Bundle retornaBundle(AgendaDO agenda){
       bundle = new Bundle();

       bundle.putString("idAgenda", agenda.getIdAgenda());
       bundle.putString("evento", agenda.getEvento());
       bundle.putString("local", agenda.getArena());
       bundle.putString("hora", agenda.getHora().toString());
       bundle.putString("data", agenda.getData().toString());
       bundle.putString("valor", agenda.getValor().toString());
       bundle.putString("idResultado", agenda.getResultado().getIdResultado().toString());
       bundle.putString("time1", agenda.getResultado().getTime1().toString());
       bundle.putString("time2", agenda.getResultado().getTime2().toString());
       bundle.putString("gols1", agenda.getResultado().getGols1().toString());
       bundle.putString("gols2", agenda.getResultado().getGols2().toString());
       bundle.putString("dataFutura", agenda.getDataFutura().toString());

       return bundle;

   }


}
