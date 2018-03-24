package br.com.proximojogo.proximojogo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import br.com.proximojogo.proximojogo.utils.FormatarData;
import br.com.proximojogo.proximojogo.utils.GetUser;

public class ListaEventosAgenda extends Fragment {
    private DatabaseReference mDatabase;//rever acho que estamos usando repetido isso, parece ser a mesma coisa do mDatabaseAgenda
    private ListView mListView;
    AgendaDO agenda;
    private DatabaseReference mDatabaseAgenda;
    private boolean online = false;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdView = getView().findViewById(R.id.adView);


        mAdView.setAdListener(new AdListener() {
            private void showToast(String message) {
                View view = getView();
                if (view != null) {
                    Toast.makeText(getView().getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAdLoaded() {
                showToast("Ad loaded.");
//                showInterstitial();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                showToast(String.format("Ad failed to load with error code %d.", errorCode));
            }

            @Override
            public void onAdOpened() {
                showToast("Ad opened.");
            }

            @Override
            public void onAdClosed() {
                mAdView.destroy();
                mAdView.setVisibility(View.GONE);
                showToast("Ad closed.");
            }

            @Override
            public void onAdLeftApplication() {
                showToast("Ad left application.");
            }
        });

        //produção
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);


        AdRequest adRequest = new AdRequest.Builder().addTestDevice("33BE2250B43518CCDA7DE426D04EE231").build();
        mAdView.loadAd(adRequest);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View eventosDaAgendaView = inflater.inflate(R.layout.fragment_lista_eventos_agenda, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Eventos do Time");

        //parece que isso não fez falta
//        MobileAds.initialize(eventosDaAgendaView.getContext(), "ca-app-pub-3940256099942544~3347511713");

        //anuncio que ocupa a tela inteira
//        mInterstitialAd = new InterstitialAd(eventosDaAgendaView.getContext());
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                // Load the next interstitial.
//                mInterstitialAd.loadAd(new AdRequest.Builder().build());
//            }
//
//        });

        /**
         * Teste da documentação
         */
        verificaClienteOnline();
        /**
         * Teste da documentação
         */
        mDatabaseAgenda = FirebaseDatabase.getInstance().getReference("agendas");
        mDatabaseAgenda.keepSynced(true);
        mListView = (ListView) eventosDaAgendaView.findViewById(R.id.list_view_agenda);

//        mAdView = eventosDaAgendaView.findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {
                agenda = (AgendaDO) lista.getItemAtPosition(position);
                /**
                 * esse bundle envia o valor para outro fragment (evitar acoplamento seria interessante
                 * utilizar uma interface) mas não vi necessidade aqui.
                 */
                Bundle bundle = new Bundle();
                AgendaFragment agendaFragment = new AgendaFragment();
                bundle.putString("idAgenda", agenda.getIdAgenda());
                bundle.putString("evento", agenda.getEvento());
                bundle.putString("local", agenda.getArena());
                bundle.putString("time", agenda.getTimes());
                bundle.putString("data", agenda.getData().toString());
                bundle.putString("hora", agenda.getHora().toString());
                bundle.putString("adversario", agenda.getAdversario());
                bundle.putString("valor", agenda.getValor().toString());
                bundle.putString("observacao", agenda.getObservacao());
                agendaFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.container, agendaFragment).commit();

            }
        });

        Button novaAgenda = (Button) eventosDaAgendaView.findViewById(R.id.novo_agenda);
        novaAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new AgendaFragment()).commit();
            }
        });
//        mDatabaseAgenda.child(GetUser.getUserLogado());
        mDatabase = FirebaseDatabase.getInstance().getReference().child("agendas" + "/" + GetUser.getUserLogado());
        mDatabase.keepSynced(true);
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        c.set(Calendar.DAY_OF_MONTH, day - 1);
        Query queryRef = mDatabase.startAt(c.getTimeInMillis()).orderByChild("data"); // ordena os dados pelo campo informado...
        FirebaseListAdapter<AgendaDO> firebaseListAdapter = new FirebaseListAdapter<AgendaDO>(
                getActivity(),
                AgendaDO.class,
                R.layout.row_evento_excluir,
                queryRef
        ) {
            @Override
            protected void populateView(View v, AgendaDO agenda, int position) {
                final int pos = position;
                TextView time = (TextView) v.findViewById(R.id.time_evento);
                time.setText(agenda.getTimes());
                TextView tipo = (TextView) v.findViewById(R.id.adversario_evento);
                tipo.setText(" " + agenda.getAdversario());
                TextView local = (TextView) v.findViewById(R.id.local_evento);
                local.setText("Local: " + agenda.getArena());
                TextView data = (TextView) v.findViewById(R.id.data_evento);
                data.setText("Data: " + FormatarData.getDf().format(agenda.getData()));
                TextView hora = (TextView) v.findViewById(R.id.hora_evento);
                hora.setText("Hora: " + (FormatarData.getDfHora().format(agenda.getHora())));
                ImageButton btnExcluir = v.findViewById(R.id.main_line_more);
                btnExcluir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AgendaDO item = getItem(pos);
                        mDatabaseAgenda = FirebaseDatabase.getInstance().getReference().child("agendas/" + GetUser.getUserLogado() + "/" + item.getIdAgenda());
                        mDatabaseAgenda.removeValue();
                        //Acao do primeiro botao
                        Toast.makeText(v.getContext(), "Evento excluido. ", Toast.LENGTH_SHORT).show();
                    }
                });
                ImageButton btnEditar = v.findViewById(R.id.row_edit);
                btnEditar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AgendaDO item = getItem(pos);
                        /**
                         * esse bundle qu envia o valor para outro fragment (evitar acoplamento seria interessante
                         * utilizar uma interface) mas não vi necessidade aqui.
                         */
                        Bundle bundle = new Bundle();
                        AgendaFragment agendaFragment = new AgendaFragment();
                        bundle.putString("idAgenda", item.getIdAgenda());
                        bundle.putString("evento", item.getEvento());
                        bundle.putString("local", item.getArena());
                        bundle.putString("time", item.getTimes());
                        bundle.putString("data", item.getData().toString());
                        bundle.putString("hora", item.getHora().toString());
                        bundle.putString("adversario", item.getAdversario());
                        bundle.putString("valor", item.getValor().toString());
                        bundle.putString("observacao", item.getObservacao());
                        agendaFragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.container, agendaFragment).commit();
                        Toast.makeText(v.getContext(), "Evento editado.", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        };
        mListView.setAdapter(firebaseListAdapter);

        return eventosDaAgendaView;
    }

    public void populaListView(){

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
