package br.com.proximojogo.proximojogo.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.proximojogo.proximojogo.R;

public class ListaEventosAgenda extends Fragment implements ClickRecyclerViewInterface{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_eventos_agenda, container, false);
    }

    @Override
    public void onCustomClick(Object object) {

    }
}
