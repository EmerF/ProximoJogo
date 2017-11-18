
package br.com.proximojogo.proximojogo.helper;

import android.app.Activity;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.date.PickersActivity;
import br.com.proximojogo.proximojogo.entity.AgendaDO;
import br.com.proximojogo.proximojogo.enuns.Eventos;
import br.com.proximojogo.proximojogo.utils.FormatarData;
import br.com.proximojogo.proximojogo.utils.GetUser;

/**
 * Created by ale on 08/08/2017.
 */

public class HelperAgenda {

    private static final String TAG = "ASYNC_DAO_AGENDA";
    //SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

    private Spinner campoEvento;
    private EditText campoDat;
    private EditText campoDiaSemana;
    private EditText campoHora;
    //private final EditText campoDiaSemana;
    private EditText campoIdAgenda;
    private Spinner campoAdversario;
    private EditText campoValor;
    private Spinner campoLocal;
    private Spinner campoTime;

    private EditText campoObservacao;

    private Spinner spEvento;
    private EditText data;
    private EditText hora;

    private AgendaDO agenda;
    private Handler handler = null;
    private DatabaseReference mDatabaseAgenda;
    private View viewAtiva;



    /*
        * Captura valores inseridos no formulário...
        * */
    public HelperAgenda(View view, Handler handler) {
        this.handler = handler;
        viewAtiva = view;
        //inicializaCamposTela(viewAtiva);

    }

    //https://www.simplifiedcoding.net/firebase-realtime-database-crud/
    public boolean salvar(View activity) {
        boolean salvou = false;
        try {

            mDatabaseAgenda = FirebaseDatabase.getInstance().getReference("agendas");
            AgendaDO agenda = pegaAgenda();
            if (agenda.getIdAgenda().equals("")) {
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

    public void excluir(View activity, String idAgenda) {
        try {
            mDatabaseAgenda = FirebaseDatabase.getInstance().getReference().child("agendas/" + GetUser.getUserLogado() + "/" + idAgenda);
            mDatabaseAgenda.removeValue();
            Toast.makeText(activity.getContext(), "Agenda Apagada com Sucesso!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity.getContext(), "Erro ao Apagadar Agenda!", Toast.LENGTH_SHORT).show();
        }
    }

    public AgendaDO pegaAgenda() throws ParseException {

        Date inicio = FormatarData.getFormato().parse(campoDat.getText().toString());

        Eventos evento = (Eventos) campoEvento.getSelectedItem();
        agenda.setEvento(evento.toString());
        agenda.setData(inicio.getTime());

        Date hora = FormatarData.getFormatoHora().parse(campoHora.getText().toString());
        agenda.setHora(hora.getTime());
        agenda.setDiaSemana(diaDaSemana(inicio));
        agenda.setAdversario(campoAdversario.getSelectedItem().toString());
        agenda.setIdAgenda(campoIdAgenda.getText().toString());
        agenda.setValor(new Double(campoValor.getText().toString()));
        agenda.setArena(campoLocal.getSelectedItem().toString());
        agenda.setTimes(campoTime.getSelectedItem().toString());
        agenda.setObservacao(campoObservacao.getText().toString());
        agenda.setStatus("Status");
        agenda.setIdUser(GetUser.getUserLogado());
        return agenda;

    }

    /*  Preenche o formulário com os dados do objeto recebido como parametro
    *   Seta o objeto recebido no objeto local para fins de edição
    *   Os spinners são preenchidos na inicialização da tela
    *
     */
    public void preencheFormulario(final AgendaDO agenda) throws ParseException {


                if (agenda != null) {
                    campoEvento.setSelection(Eventos.valueOf(agenda.getEvento()).ordinal());
                    campoDat.setText((String) FormatarData.getDf().format(agenda.getData()));
                    campoHora.setText((String) FormatarData.getDfHora().format(agenda.getHora()));
                    campoDiaSemana.setText(String.valueOf(agenda.getDiaSemana()));
                    campoIdAgenda.setText(String.valueOf(agenda.getIdAgenda()));
                    campoValor.setText(String.valueOf(agenda.getValor()));
                    campoObservacao.setText(agenda.getObservacao());

                }


        this.agenda = agenda;
    }

    public void carregaImagem(String caminhoFoto) {
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

    private int diaDaSemana(Date data) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(data);
        int diaDaSemana = gc.get(GregorianCalendar.DAY_OF_WEEK);
        return diaDaSemana;
    }


    public void inicializaCamposTela(View activity, AgendaDO agenda) {
        String valorCampo = agenda.getArena();
        String no = "Arenas";
        String nomeCampo = "nomeArena";

        // -------- Spinner de Arenas-----------------//
        CarregarSpinner(activity, valorCampo, no,
                nomeCampo,R.id.formulario_local);
        // -----------------------------------------//

        valorCampo = "";
        nomeCampo = "";
        no = "";

        // -------- Spinner de Times-----------------//
        valorCampo = agenda.getTimes();
        no = "Times" + "/" + GetUser.getUserLogado();
        nomeCampo = "nomeTime";


        CarregarSpinner(activity, valorCampo, no,
                nomeCampo, R.id.formulario_time);
        // -----------------------------------------//

        // -------- Spinner de Adversario-----------------//
        valorCampo = agenda.getAdversario() ;
        nomeCampo = "nomeTime";
        no = "Times" + "/" + GetUser.getUserLogado();

        CarregarSpinner(activity, valorCampo, no,
                nomeCampo,R.id.formulario_adversario);
        // -----------------------------------------//
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
        campoAdversario = (Spinner) activity.findViewById(R.id.formulario_adversario);
        campoIdAgenda = (EditText) activity.findViewById(R.id.idAgenda);
        //campoFoto = (ImageView)activity.findViewById(R.id.formulario_foto);

        campoValor = (EditText) activity.findViewById(R.id.formulario_valor);
        campoLocal = (Spinner) activity.findViewById(R.id.formulario_local);
        campoTime = (Spinner) activity.findViewById(R.id.formulario_time);
        campoObservacao = (EditText) activity.findViewById(R.id.formulario_observacao);

        this.agenda = new AgendaDO();
    }

    /*
        Preenche o spinner com os dados que serão buscado no firebase
        @param:
        View: onde está o spinner
        valorParaFiltrarPosicao: valor que será setado no spinner em caso de estar editando o registro
        no: local do firebase onde os dados serão buscados
        campoTabela: campo que será usado para preencher os valores do spinner. Ex: nomeTime
        idSpinner: R.id do spinner
     */

    public void CarregarSpinner(final View activity, final String valorParaFiltrarPosicao, String no,
                                final String campoTabela, final int idSpinner) {

        mDatabaseAgenda = FirebaseDatabase.getInstance().getReference().child(no);
        mDatabaseAgenda.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array

                final List<String> times = new ArrayList<>();
                for (DataSnapshot timeSnapshot : dataSnapshot.getChildren()) {
                    String areaName = timeSnapshot.child(campoTabela).getValue(String.class);
                    times.add(areaName);
                }
                Collections.sort(times);
                ArrayAdapter<String> adapterTimes;
                adapterTimes = new ArrayAdapter<String>
                        (activity.getContext(), R.layout.support_simple_spinner_dropdown_item, times);
                adapterTimes.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);


                int pos = 0;
                    Spinner campoLocal = (Spinner) activity.findViewById(idSpinner);

                    if (valorParaFiltrarPosicao != null) {
                        pos = adapterTimes.getPosition(valorParaFiltrarPosicao);
                    }

                    campoLocal.setAdapter(adapterTimes);
                    campoLocal.setSelection(pos);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
    }



    }




