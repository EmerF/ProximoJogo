package br.com.proximojogo.proximojogo.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
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
import java.util.Date;
import java.util.GregorianCalendar;

import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.date.createDatePicker;
import br.com.proximojogo.proximojogo.date.createTimePicker;
import br.com.proximojogo.proximojogo.entity.AgendaDO;
import br.com.proximojogo.proximojogo.enuns.Eventos;
import br.com.proximojogo.proximojogo.enuns.NomeArena;
import br.com.proximojogo.proximojogo.enuns.Times;
import br.com.proximojogo.proximojogo.utils.FormatarData;
import br.com.proximojogo.proximojogo.utils.GetUser;

/**
 * Created by ale on 08/08/2017.
 */

public class FormularioHelperAgenda {

    private static final String TAG = "ASYNC_DAO_AGENDA";
    //SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

    //private final EditText campoEvento;
    private Spinner campoEvento;
    //private final EditText campoStatus;
    private EditText campoDat;
    private EditText campoDiaSemana;
    private EditText campoHora;
    //private final EditText campoDiaSemana;
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

    //private final ImageView campoFoto;
    private AgendaDO agenda;
    private Handler handler = null;

    private DatabaseReference mDatabaseAgenda;
    private View viewAtiva;


    /*
    * Captura valores inseridos no formulário...
    * */
    public FormularioHelperAgenda(View activity, Handler handler) {
        this.handler = handler;
        inicializaCamposTela(activity);
        viewAtiva = activity;


    }

    //https://www.simplifiedcoding.net/firebase-realtime-database-crud/
    public void salvar(View activity) {
        try {

            mDatabaseAgenda = FirebaseDatabase.getInstance().getReference("agendas");
            AgendaDO agenda = pegaAgenda();
            if (agenda.getIdAgenda() == null) {
                //getUser id do Firebase para setar na agenda
                // e colocar o nome junto com o id para identificar o nó
                String key = mDatabaseAgenda.push().getKey();
                agenda.setIdAgenda(key);
                agenda.setIdUser("Txr5w0STR5YX2r9QjGuqab0KOB13");// substituir pelo id do usuário qdo o login estiver pronto
                //mDatabaseAgenda.child(agenda.getIdAgenda()).setValue(agenda);
                mDatabaseAgenda.child(agenda.getIdUser()+ "/" +  agenda.getIdAgenda()).setValue(agenda);

                Toast.makeText(activity.getContext(), "Agenda Cadastrada com Sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                mDatabaseAgenda.child(agenda.getIdUser()+  "/" + agenda.getIdAgenda()).setValue(agenda);
                Toast.makeText(activity.getContext(), "Agenda Editada com Sucesso!", Toast.LENGTH_SHORT).show();
            }
            limparCamposTela();
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ERRO_SALVAR_AGENDA", "Erro ao salvar Agenda." + e.getStackTrace());
            Toast.makeText(activity.getContext(), "Erro ao Salvar Agenda." + e.getStackTrace(), Toast.LENGTH_SHORT).show();

        }
    }

    public void excluir(View activity, String idAgenda) {
        try {
            mDatabaseAgenda = FirebaseDatabase.getInstance().getReference().child("agendas/" + GetUser.getUserLogado() + "/" + idAgenda);
            mDatabaseAgenda.removeValue();
            limparCamposTela();
            Toast.makeText(activity.getContext(), "Agenda Apagada com Sucesso!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity.getContext(), "Erro ao Apagadar Agenda!", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean validarCampos() {

        return false;
    }

    public AgendaDO pegaAgenda() throws ParseException {
//        agenda.setIdAgenda("-KrYLr6m0kc9TH_TJhaZ");
        /*if (campoDat.getText().toString().equals("")) {
            campoDat.setError("Informe a data!!");
        }*/


            Date inicio = FormatarData.getFormato().parse(campoDat.getText().toString());

            Eventos evento = (Eventos) campoEvento.getSelectedItem();
            agenda.setEvento(evento.toString());
            agenda.setData(inicio.getTime());

            Date hora = FormatarData.getFormatoHora().parse(campoHora.getText().toString());
            agenda.setHora(hora.getTime());
            agenda.setDiaSemana(diaDaSemana(inicio));
            agenda.setAdversario(campoAdversario.getText().toString());
            agenda.setValor(new Double(campoValor.getText().toString()));
            NomeArena arena = (NomeArena) campoLocal.getSelectedItem();
            agenda.setArena(arena.toString());
            Times time = (Times) campoTime.getSelectedItem();
            agenda.setTimes(time.toString());
            agenda.setObservacao(campoObservacao.getText().toString());
            agenda.setStatus("Status");
        return agenda;

    }

    public void preencheFormulario(String idAgenda) throws ParseException {
        mDatabaseAgenda = FirebaseDatabase.getInstance().getReference().child("agendas/" + GetUser.getUserLogado()+ "/" +idAgenda);
        mDatabaseAgenda.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                agenda = dataSnapshot.getValue(AgendaDO.class);
                if (agenda != null) {
                    int item;
                    //Eventos.values();
                    //item = setValorSpinner(agenda.getEvento(),Eventos.values());
                    campoEvento.setSelection(Eventos.valueOf(agenda.getEvento()).ordinal());
                    campoLocal.setSelection(NomeArena.valueOf(agenda.getArena()).ordinal());
                    campoTime.setSelection(Times.valueOf(agenda.getTimes()).ordinal());
                    campoDat.setText((String) FormatarData.getDf().format(agenda.getData()));
                    campoHora.setText((String) FormatarData.getDfHora().format(agenda.getHora()));
                    campoDiaSemana.setText(String.valueOf(agenda.getDiaSemana()));
                    campoAdversario.setText(String.valueOf(agenda.getAdversario()));
                    campoValor.setText(String.valueOf(agenda.getValor()));
                    campoObservacao.setText(agenda.getObservacao());
//                this.agenda = agenda;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
            }
        });
    }


    public void imprimeValores(AgendaDO agenda) {
       /* Log.d("Evento", agenda.getEvento().name());
        Log.d("Time", agenda.getTimeEnum().toString());
        Log.d("Inicio", FormatarData.geagenda.getInicio());*/

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

    private int getProximoDiaJogo(int dia) {
        Date d = new Date();

        int diaAtual = diaDaSemana(d);

        if (dia < diaAtual) {
            dia = dia + (diaAtual - dia);
        }
        return dia;
    }

    public void limparCamposTela() throws ParseException {
        data.setText(new LocalDate().toString("dd/MM/yyyy"));
        campoValor.setText("");
        campoAdversario.setText("");
        campoObservacao.setText("");
    }

    public void inicializaCamposTela(View activity) {
        //spinner Arenas
        ArrayAdapter<NomeArena> adapter = new ArrayAdapter<NomeArena>(activity.getContext(), R.layout.support_simple_spinner_dropdown_item,
                NomeArena.values());
        adapter.setDropDownViewResource(android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item);
        sp = (Spinner) activity.findViewById(R.id.formulario_local);
        sp.setAdapter(adapter);

        //Times/Times
        ArrayAdapter<Times> adapterTimes = new ArrayAdapter<Times>
                (activity.getContext(), R.layout.support_simple_spinner_dropdown_item, br.com.proximojogo.proximojogo.enuns.Times.values());
        adapterTimes.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spTimes = (Spinner) activity.findViewById(R.id.formulario_time);
        spTimes.setAdapter(adapterTimes);

        //Tipo do evento

        ArrayAdapter<Eventos> adapterEvento = new ArrayAdapter<Eventos>
                (activity.getContext(), R.layout.support_simple_spinner_dropdown_item, br.com.proximojogo.proximojogo.enuns.Eventos.values());
        adapterEvento.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spEvento = (Spinner) activity.findViewById(R.id.formulario_evento);
        spEvento.setAdapter(adapterEvento);

        data = (EditText) activity.findViewById(R.id.formulario_Data);
        //sempre pego o dia atual
        data.setText(new LocalDate().toString("dd/MM/yyyy"));
        hora = (EditText) activity.findViewById(R.id.formulario_hora);
        createDatePicker dtIni = new createDatePicker(data, activity.getContext(), "Data", new Date().getTime());
        createTimePicker dtFim = new createTimePicker(hora, activity.getContext(), "Hora");


        campoEvento = (Spinner) activity.findViewById(R.id.formulario_evento);
        //campoStatus = (EditText)activity.findViewById(R.id.formulario_status);
        campoDat = (EditText) activity.findViewById(R.id.formulario_Data);
        campoHora = (EditText) activity.findViewById(R.id.formulario_hora);
        campoDiaSemana = (EditText) activity.findViewById(R.id.formulario_dia_da_semana);
        campoAdversario = (EditText) activity.findViewById(R.id.formulario_adversario);
        //campoFoto = (ImageView)activity.findViewById(R.id.formulario_foto);

        campoValor = (EditText) activity.findViewById(R.id.formulario_valor);
        campoLocal = (Spinner) activity.findViewById(R.id.formulario_local);
        campoTime = (Spinner) activity.findViewById(R.id.formulario_time);
        campoObservacao = (EditText) activity.findViewById(R.id.formulario_observacao);
        agenda = new AgendaDO();
    }
}
