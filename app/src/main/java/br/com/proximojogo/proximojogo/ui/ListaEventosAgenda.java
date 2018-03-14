package br.com.proximojogo.proximojogo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import br.com.proximojogo.proximojogo.MainActivity;
import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.entity.AgendaDO;
import br.com.proximojogo.proximojogo.entity.Resultado;
import br.com.proximojogo.proximojogo.utils.FormatarData;
import br.com.proximojogo.proximojogo.utils.GetUser;
import br.com.proximojogo.proximojogo.utils.bundle.BundleAgenda;
import br.com.proximojogo.proximojogo.utils.snapshot.ConverteSnapshotResultado;

public class ListaEventosAgenda extends Fragment {
    private ListView mListView;
    AgendaDO agenda;
    private DatabaseReference mDatabaseAgenda;
    private boolean online = false;
    private Resultado resultado;
    private DatabaseReference mResultados;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View eventosDaAgendaView = inflater.inflate(R.layout.fragment_lista_eventos_agenda, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Eventos do Time");
        /**
         * Teste da documentação
         */
        verificaClienteOnline();
        /**
         * Teste da documentação
         */
        mDatabaseAgenda = FirebaseDatabase.getInstance().getReference("agendas");
        mDatabaseAgenda.keepSynced(true);
        mListView = eventosDaAgendaView.findViewById(R.id.list_view_agenda);


        Button novaAgenda =  eventosDaAgendaView.findViewById(R.id.novo_agenda);
        novaAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new AgendaFragment()).commit();
            }
        });
//        mDatabaseAgenda.child(GetUser.getUserLogado());
        mDatabaseAgenda = FirebaseDatabase.getInstance().getReference().child("Agendas" + "/" + GetUser.getUserLogado());
        mDatabaseAgenda.keepSynced(true);
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        c.set(Calendar.DAY_OF_MONTH, day - 1);


        Query queryRef = mDatabaseAgenda.startAt(c.getTimeInMillis()).orderByChild("data"); // ordena os dados pelo campo informado...
        FirebaseListAdapter<AgendaDO> firebaseListAdapter = new FirebaseListAdapter<AgendaDO>(
                getActivity(),
                AgendaDO.class,
                R.layout.row_evento_excluir,
                queryRef
        ) {

            @Override
            protected void populateView(final View v, final AgendaDO agenda, int position) {
                final int pos = position;
                mResultados = FirebaseDatabase.getInstance().getReference().child("Resultados/" + agenda.getIdResultado());
                mResultados.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        setResultado(dataSnapshot);
                        agenda.setResultado(ConverteSnapshotResultado.converteSnapShotParaResultado(dataSnapshot));

                        if(agenda.getIdResultado() != null){
                            TextView time =  v.findViewById(R.id.time_evento);
                            time.setText(dataSnapshot.child("time1").getValue(String.class));

                            TextView adv =  v.findViewById(R.id.adversario_evento);
                            adv.setText(dataSnapshot.child("time2").getValue(String.class));

                        }

                        TextView data =  v.findViewById(R.id.data_evento);
                        data.setText("Data: " + FormatarData.getDf().format(agenda.getData()));

                        TextView hora =  v.findViewById(R.id.hora_evento);
                        hora.setText("Hora: " + (FormatarData.getDfHora().format(agenda.getHora())));

                        TextView local =  v.findViewById(R.id.local_evento);
                        local.setText("Local: " + agenda.getArena());


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                ImageButton btnExcluir = v.findViewById(R.id.excluir_evento);
                btnExcluir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AgendaDO item = getItem(pos);
                        mDatabaseAgenda = FirebaseDatabase.getInstance().getReference().child("Agendas/" + GetUser.getUserLogado()
                                + "/" + item.getIdAgenda());
                        mResultados = FirebaseDatabase.getInstance().getReference().child("Resultados/" + agenda.getIdResultado());
                        mDatabaseAgenda.removeValue();
                        mResultados.removeValue();
                        //Acao do primeiro botao
                        Toast.makeText(v.getContext(), "Evento excluido. ", Toast.LENGTH_SHORT).show();
                    }
                });
                ImageButton btnEditar = v.findViewById(R.id.row_edit);
                btnEditar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AgendaDO agenda2 = getItem(pos);
                        agenda2.setDataFutura(true);
                        agenda2.setResultado(agenda.getResultado());
                        /**
                         * esse bundle envia o valor para outro fragment (evitar acoplamento seria interessante
                         * utilizar uma interface) mas não vi necessidade aqui.
                         */
                        BundleAgenda bundleAgenda = new BundleAgenda();
                        Bundle bundle = bundleAgenda.retornaBundle(agenda2);
                        AgendaFragment agendaFragment = new AgendaFragment();
                        agendaFragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.container, agendaFragment).commit();
                    }
                });

            }

        };
        mListView.setAdapter(firebaseListAdapter);

        return eventosDaAgendaView;
    }

    private void setResultado(DataSnapshot dataSnapshot) {
        this.resultado = ConverteSnapshotResultado.converteSnapShotParaResultado(dataSnapshot);
    }


    /**
     * Teste de ver quando tem internet
     */
    public boolean verificaConexao() {
        final boolean[] online = {false};
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    System.out.println("connected");
                    online[0] =true;
                    if (getActivity() != null)
                        Toast.makeText(getActivity(), "Conectado", Toast.LENGTH_SHORT).show();

                } else {
                    System.out.println("not connected");
                    online[0] =false;
                    if (getActivity() != null)
                        Toast.makeText(getActivity(), "Não Conectado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
                online[0] =false;
            }
        });
        return online[0];
    }

    public void verificaClienteOnline() {
        // since I can connect from multiple devices, we store each connection instance separately
// any time that connectionsRef's value is null (i.e. has no children) I am offline
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myConnectionsRef = database.getReference("users/fuba_ale_grilo/connections");

// stores the timestamp of my last disconnect (the last time I was seen online)
        final DatabaseReference lastOnlineRef = database.getReference("/users/fuba_ale_grilo/ultimaVezOnline");

        final DatabaseReference connectedRef = database.getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    DatabaseReference con = myConnectionsRef.push();

                    // when this device disconnects, remove it
                    con.onDisconnect().removeValue();

                    // when I disconnect, update the last time I was seen online
                    lastOnlineRef.onDisconnect().setValue(ServerValue.TIMESTAMP);

                    // add this device to my connections list
                    // this value could contain info about the device or a timestamp too
                    con.setValue(Boolean.TRUE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled at .info/connected");
            }
        });
    }


}
