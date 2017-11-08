
package br.com.proximojogo.proximojogo.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.LocalDate;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.date.PickersActivity;
import br.com.proximojogo.proximojogo.entity.AgendaDO;
import br.com.proximojogo.proximojogo.enuns.Eventos;
import br.com.proximojogo.proximojogo.enuns.NomeArena;
import br.com.proximojogo.proximojogo.utils.FormatarData;
import br.com.proximojogo.proximojogo.utils.GetUser;

/**
 * Created by ale on 08/08/2017.
 */

public class HelperAgenda {

    private static final String TAG = "ASYNC_DAO_AGENDA";
    //SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

    //private final EditText campoEvento;
    private Spinner campoEvento;
    //private final EditText campoStatus;
    private EditText campoDat;
    private EditText campoDiaSemana;
    private EditText campoHora;
    //private final EditText campoDiaSemana;
    private EditText campoIdAgenda;
    private EditText campoAdversario;
    private EditText campoValor;
    private Spinner campoLocal;
    private Spinner campoTime;

    private EditText campoObservacao;

    private Spinner sp;
    private Spinner spTimes;
    private Spinner spEvento;
    private EditText data;
    private EditText hora;

    private AgendaDO agenda;
    private Handler handler = null;
    private DatabaseReference mDatabaseAgenda;
    private View viewAtiva;
    private ArrayAdapter<String> adapterTimes;

    /*
    * Captura valores inseridos no formulário...
    * */
    public HelperAgenda(View activity, Handler handler) {
                    this.handler = handler;
                    inicializaCamposTela(activity);
                    viewAtiva = activity;


                }

                //https://www.simplifiedcoding.net/firebase-realtime-database-crud/
            public boolean salvar (View activity){
                boolean salvou = false;
                try {

                    mDatabaseAgenda = FirebaseDatabase.getInstance().getReference("agendas");
                    AgendaDO agenda = pegaAgenda();
                    if (agenda.getIdAgenda() == null) {
                        //getUser id do Firebase para setar na agenda
                        // e colocar o nome junto com o id para identificar o nó
                        String key = mDatabaseAgenda.push().getKey();
                        agenda.setIdAgenda(key);
                        // substituir pelo id do usuário qdo o login estiver pronto
                        //mDatabaseAgenda.child(agenda.getIdAgenda()).setValue(agenda);
                        mDatabaseAgenda.child(agenda.getIdUser() + "/" + agenda.getIdAgenda()).setValue(agenda);
                        Toast.makeText(activity.getContext(), "Agenda Cadastrada com Sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        mDatabaseAgenda.child(agenda.getIdUser() + "/" + agenda.getIdAgenda()).setValue(agenda);
                        Toast.makeText(activity.getContext(), "Agenda Editada com Sucesso!", Toast.LENGTH_SHORT).show();
                    }
                    salvou = true;
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e("ERRO_SALVAR_AGENDA", "Erro ao salvar Agenda." + e.getStackTrace());
                    salvou = false;
                    Toast.makeText(activity.getContext(), "Erro ao Salvar Agenda." + e.getStackTrace(), Toast.LENGTH_SHORT).show();

                }
                return salvou;
            }

            public void excluir (View activity, String idAgenda){
                try {
                    mDatabaseAgenda = FirebaseDatabase.getInstance().getReference().child("agendas/" + GetUser.getUserLogado() + "/" + idAgenda);
                    mDatabaseAgenda.removeValue();
                    Toast.makeText(activity.getContext(), "Agenda Apagada com Sucesso!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activity.getContext(), "Erro ao Apagadar Agenda!", Toast.LENGTH_SHORT).show();
                }
            }


            public boolean validarCampos () {

                return false;
            }

            public AgendaDO pegaAgenda () throws ParseException {

                Date inicio = FormatarData.getFormato().parse(campoDat.getText().toString());

                Eventos evento = (Eventos) campoEvento.getSelectedItem();
                agenda.setEvento(evento.toString());
                agenda.setData(inicio.getTime());

                Date hora = FormatarData.getFormatoHora().parse(campoHora.getText().toString());
                agenda.setHora(hora.getTime());
                agenda.setDiaSemana(diaDaSemana(inicio));
                agenda.setAdversario(campoAdversario.getText().toString());
                agenda.setIdAgenda(campoIdAgenda.getText().toString());
                agenda.setValor(new Double(campoValor.getText().toString()));
                NomeArena arena = (NomeArena) campoLocal.getSelectedItem();
                agenda.setArena(arena.toString());
                agenda.setTimes(campoTime.getSelectedItem().toString());
                agenda.setObservacao(campoObservacao.getText().toString());
                agenda.setStatus("Status");
                agenda.setIdUser(GetUser.getUserLogado());
                return agenda;

            }

    /*  Preenche o formulário com os dados do objeto recebido como parametro
    *   Seta o objeto recebido no objeto local para fins de edição
    *
    *
     */
            public void preencheFormulario ( final AgendaDO agenda) throws ParseException {
                final List<String> times = new ArrayList<String>();
                mDatabaseAgenda = FirebaseDatabase.getInstance().getReference().child("Times" + "/" + GetUser.getUserLogado());
                mDatabaseAgenda.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                            String areaName = areaSnapshot.child("nomeTime").getValue(String.class);
                            times.add(areaName);
                        }
                        //Aqui chamo de novo os times da base, pode não ser uma melhor saida, mas para desenroscar da boa rsrsrs
                        adapterTimes = new ArrayAdapter<String>
                                (viewAtiva.getContext(), R.layout.support_simple_spinner_dropdown_item, times);
                        adapterTimes.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

                        if (agenda != null) {
                            campoEvento.setSelection(Eventos.valueOf(agenda.getEvento()).ordinal());
                            campoLocal.setSelection(NomeArena.valueOf(agenda.getArena()).ordinal());
                            int pos = adapterTimes.getPosition(agenda.getTimes());
                            campoTime.setSelection(pos);
                            campoDat.setText((String) FormatarData.getDf().format(agenda.getData()));
                            campoHora.setText((String) FormatarData.getDfHora().format(agenda.getHora()));
                            campoDiaSemana.setText(String.valueOf(agenda.getDiaSemana()));
                            campoAdversario.setText(String.valueOf(agenda.getAdversario()));
                            campoIdAgenda.setText(String.valueOf(agenda.getIdAgenda()));
                            campoValor.setText(String.valueOf(agenda.getValor()));
                            campoObservacao.setText(agenda.getObservacao());
//                    this.agenda = agenda;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

            }

            public void carregaImagem (String caminhoFoto){
                if (caminhoFoto != null) {
                    Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
                    Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
            /*campoFoto.setImageBitmap(bitmapReduzido);
            campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
            campoFoto.setTag(caminhoFoto);*/

                }


            }
    /*
    *
    * @param data no formato (dd/MM/yyyy)
    * return dia da semana inteiro. 0 Sunday, 1 monday etc..
    * */

            private int diaDaSemana (Date data){
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime(data);
                int diaDaSemana = gc.get(GregorianCalendar.DAY_OF_WEEK);
                return diaDaSemana;
            }

            private int getProximoDiaJogo ( int dia){
                Date d = new Date();

                int diaAtual = diaDaSemana(d);

                if (dia < diaAtual) {
                    dia = dia + (diaAtual - dia);
                }
                return dia;
            }

            public void limparCamposTela () throws ParseException {
                data.setText(new LocalDate().toString("dd/MM/yyyy"));
                campoValor.setText("");
                campoAdversario.setText("");
                campoIdAgenda.setText(null);
                campoObservacao.setText("");
            }

            public void inicializaCamposTela (View activity){
                //spinner Arenas
                ArrayAdapter<NomeArena> adapter = new ArrayAdapter<NomeArena>(activity.getContext(), R.layout.support_simple_spinner_dropdown_item,
                        NomeArena.values());
                adapter.setDropDownViewResource(android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item);
                sp = (Spinner) activity.findViewById(R.id.formulario_local);
                sp.setAdapter(adapter);

                //Times/Times
                getTimesUsuario(activity);

                //Tipo do evento

                ArrayAdapter<Eventos> adapterEvento = new ArrayAdapter<Eventos>
                        (activity.getContext(), R.layout.support_simple_spinner_dropdown_item, br.com.proximojogo.proximojogo.enuns.Eventos.values());
                adapterEvento.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spEvento = (Spinner) activity.findViewById(R.id.formulario_evento);
                spEvento.setAdapter(adapterEvento);

                data = (EditText) activity.findViewById(R.id.formulario_Data);

                //sempre pego o dia atual
                data.setText(new LocalDate().toString("dd/MM/yyyy"));
                //boqueia o teclado
                data.setInputType(InputType.TYPE_NULL);

                hora = (EditText) activity.findViewById(R.id.formulario_hora);
                hora.setInputType(InputType.TYPE_NULL);
                new PickersActivity(data, activity.getContext(), 0);
                new PickersActivity(hora, activity.getContext(), 1);


                campoEvento = (Spinner) activity.findViewById(R.id.formulario_evento);
                //campoStatus = (EditText)activity.findViewById(R.id.formulario_status);
                campoDat = (EditText) activity.findViewById(R.id.formulario_Data);
                campoHora = (EditText) activity.findViewById(R.id.formulario_hora);
                campoDiaSemana = (EditText) activity.findViewById(R.id.formulario_dia_da_semana);
                campoAdversario = (EditText) activity.findViewById(R.id.formulario_adversario);
                campoIdAgenda = (EditText) activity.findViewById(R.id.idAgenda);
                //campoFoto = (ImageView)activity.findViewById(R.id.formulario_foto);

                campoValor = (EditText) activity.findViewById(R.id.formulario_valor);
                campoLocal = (Spinner) activity.findViewById(R.id.formulario_local);
                campoTime = (Spinner) activity.findViewById(R.id.formulario_time);
                campoObservacao = (EditText) activity.findViewById(R.id.formulario_observacao);
                agenda = new AgendaDO();
            }


            private void getTimesUsuario ( final View activity){
                final List<String> times = new ArrayList<String>();
                mDatabaseAgenda = FirebaseDatabase.getInstance().getReference().child("Times" + "/" + GetUser.getUserLogado());
                mDatabaseAgenda.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Is better to use a List, because you don't know the size
                        // of the iterator returned by dataSnapshot.getChildren() to
                        // initialize the array


                        for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                            String areaName = areaSnapshot.child("nomeTime").getValue(String.class);
                            times.add(areaName);
                        }
                        //Times/Times
                        adapterTimes = new ArrayAdapter<String>
                                (activity.getContext(), R.layout.support_simple_spinner_dropdown_item, times);
                        adapterTimes.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

                        campoTime = (Spinner) activity.findViewById(R.id.formulario_time);
                        campoTime.setAdapter(adapterTimes);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

            }


        }

