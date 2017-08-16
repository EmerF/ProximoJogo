package br.com.proximojogo.proximojogo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

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
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tabela_abastecimentos, viewGroup, false);
        return new RecyclerTesteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerTesteViewHolder viewHolder, int i) {
        Abastecimento tabelaModelo = mList.get(i);

        viewHolder.preco.setText(tabelaModelo.getPrecoCombustivel().toString());
        viewHolder.valor.setText(tabelaModelo.getValorPago().toString());
        viewHolder.litros.setText(tabelaModelo.getQuantidadeLitros().toString());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    protected class RecyclerTesteViewHolder extends RecyclerView.ViewHolder {

        protected TextView preco;
        protected TextView valor;
        protected TextView litros;

        public RecyclerTesteViewHolder(final View itemView) {
            super(itemView);

            valor = (TextView) itemView.findViewById(R.id.valor);
            preco = (TextView) itemView.findViewById(R.id.preco);
            litros = (TextView) itemView.findViewById(R.id.litros);

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
