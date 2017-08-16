package br.com.proximojogo.proximojogo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.entity.AgendaDO;
import br.com.proximojogo.proximojogo.ui.ClickRecyclerViewInterface;

/**
 * Created by Ale on 15/08/2017.
 */

public class RecyclerAdapterEventosDaAgenda extends RecyclerView.Adapter<RecyclerAdapterEventosDaAgenda.RecyclerTesteViewHolder> {

    public static ClickRecyclerViewInterface clickRecyclerViewInterface;
    Context mctx;
    private List<AgendaDO> mList;

    public RecyclerAdapterEventosDaAgenda(Context ctx, List<AgendaDO> list, ClickRecyclerViewInterface clickRecyclerViewInterface) {
        this.mctx = ctx;
        this.mList = list;
        this.clickRecyclerViewInterface = clickRecyclerViewInterface;
    }

    @Override
    public RecyclerTesteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tabela_eventos_agenda, viewGroup, false);
        return new RecyclerTesteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerTesteViewHolder viewHolder, int i) {
        AgendaDO tabelaModelo = mList.get(i);

        viewHolder.time.setText(tabelaModelo.getTimes().toString());
        viewHolder.adversario.setText(tabelaModelo.getAdversario().toString());
        viewHolder.dataEvento.setText(tabelaModelo.getData().toString());
        viewHolder.horaEvento.setText(tabelaModelo.getHora().toString());
        viewHolder.localEvento.setText(tabelaModelo.getArena().toString());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    protected class RecyclerTesteViewHolder extends RecyclerView.ViewHolder {

        protected TextView time;
        protected TextView adversario;
        protected TextView dataEvento;
        protected TextView horaEvento;
        protected TextView localEvento;

        public RecyclerTesteViewHolder(final View itemView) {
            super(itemView);

            time = (TextView) itemView.findViewById(R.id.time_evento);
            adversario = (TextView) itemView.findViewById(R.id.adversario_evento);
            dataEvento = (TextView) itemView.findViewById(R.id.data_evento);
            horaEvento = (TextView) itemView.findViewById(R.id.hora_evento);
            localEvento = (TextView) itemView.findViewById(R.id.local_evento);

            //Setup the click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clickRecyclerViewInterface.onCustomClick(mList.get(getLayoutPosition()));

                }
            });
        }
    }
}
