package br.com.proximojogo.proximojogo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import br.com.proximojogo.proximojogo.MainActivity;
import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.entity.AgendaDO;
import br.com.proximojogo.proximojogo.entity.Time;
import br.com.proximojogo.proximojogo.utils.FormatarData;
import br.com.proximojogo.proximojogo.utils.GetUser;

public class ListaTimesUsuario extends Fragment {
    private DatabaseReference mDatabase;
    private ListView mListView;
    Time time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View timesUsuarioView = inflater.inflate(R.layout.fragment_lista_times_usuario, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Meus Times");
        mListView = (ListView) timesUsuarioView.findViewById(R.id.list_view_times);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {
                 time = (Time) lista.getItemAtPosition(position);
                /**
                 * esse bundle qu envia o valor para outro fragment (evitar acoplamento seria interessante
                 * utilizar uma interface) mas não vi necessidade aqui.
                 */
                Bundle bundle = new Bundle();
                TimeFragment timeFragment = new TimeFragment();

                bundle.putString("idTime", time.getIdTime());
                bundle.putString("nome", time.getNomeTime());
                bundle.putString("responsavel", time.getResponsavelTime());
                bundle.putString("telefone", time.getTelefoneResponsavel());
                timeFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.container, timeFragment).commit();

            }
        });

        Button novoTime = (Button) timesUsuarioView.findViewById(R.id.novo_time);
        novoTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new TimeFragment()).commit();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Times"+ "/" +GetUser.getUserLogado());

        FirebaseListAdapter<Time> firebaseListAdapter = new FirebaseListAdapter<Time>(
                getActivity(),
                Time.class,
                R.layout.row_time,
                mDatabase.orderByChild("nomeTime") // ordena os dados pelo campo informado...


        ) {
            @Override
            protected void populateView(View v, Time time, int position) {
                TextView nome = (TextView) v.findViewById(R.id.nome_time_lista);
                nome.setText(time.getNomeTime());

                TextView resp = (TextView) v.findViewById(R.id.responsavel_time_lista);
                resp.setText("Responsável: " +time.getResponsavelTime());

                TextView tel = (TextView) v.findViewById(R.id.telefone_responsavel_time_lista);
                tel.setText("Telefone: " +time.getTelefoneResponsavel());

            }


        };
        mListView.setAdapter(firebaseListAdapter);

        return timesUsuarioView;
    }


}
