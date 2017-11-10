package br.com.proximojogo.proximojogo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import br.com.proximojogo.proximojogo.MainActivity;
import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.entity.Arena;
import br.com.proximojogo.proximojogo.utils.GetUser;

public class ListaArenas extends Fragment {
    private DatabaseReference mDatabase;
    private ListView mListView;
    Arena arena;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View eventosArenaView = inflater.inflate(R.layout.fragment_lista_arenas, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Arenas");
        mListView = (ListView) eventosArenaView.findViewById(R.id.list_view_arenas);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {
                 arena = (Arena) lista.getItemAtPosition(position);
                /**
                 * esse bundle que envia o valor para outro fragment (evitar acoplamento seria interessante
                 * utilizar uma interface) mas não vi necessidade aqui.
                 */
                Bundle bundle = new Bundle();
                ArenaFragment arenaFragment = new ArenaFragment();
                bundle.putString("idArena", arena.getIdArena());
                bundle.putString("nomeArena", arena.getNomeArena());
                bundle.putString("enderecoArena", arena.getEndereco());
                bundle.putString("telefoneArena", arena.getTelefone());
                bundle.putString("horaIni", arena.getHorarioIniAtendimento().toString());
                bundle.putString("horaFim", arena.getHorarioFimAtendimento().toString());
                arenaFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.container, arenaFragment).commit();

            }
        });

        Button novaarena = (Button) eventosArenaView.findViewById(R.id.novo_arena);
        novaarena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new ArenaFragment()).commit();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Arenas");
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        c.set(Calendar.DAY_OF_MONTH, day-1);



        FirebaseListAdapter<Arena> firebaseListAdapter = new FirebaseListAdapter<Arena>(
                getActivity(),
                Arena.class,
                R.layout.row_arena_excluir,
                mDatabase.orderByChild("nomeArena") // ordena os dados pelo campo informado...


        ) {
            @Override
            protected void populateView(View v, Arena arena, int position) {
                TextView nome = (TextView) v.findViewById(R.id.nome_arena_lista);
                nome.setText("Arena: " +arena.getNomeArena());

                TextView endereco = (TextView) v.findViewById(R.id.endereco_arena_lista);
                endereco.setText("Endereço: " + arena.getEndereco());

                TextView telefone = (TextView) v.findViewById(R.id.telefone_arena_lista);
                telefone.setText("Telefone: " + arena.getTelefone());

                TextView horario = (TextView) v.findViewById(R.id.horario_funcionamento_arena_lista);
                horario.setText("Horário de Funcionamento:  " + arena.getHorarioIniAtendimento() + "-" + arena.getHorarioFimAtendimento());

            }


        };
        mListView.setAdapter(firebaseListAdapter);

        return eventosArenaView;
    }


}
