package br.com.proximojogo.proximojogo.helper;

import android.app.Activity;
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

import org.json.JSONException;
import org.json.JSONObject;

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

/**
 * Created by ale on 08/08/2017.
 */

public class FormularioHelper {

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

    private DatabaseReference mDatabase;

    /*
    * Captura valores inseridos no formulário...
    * */
    public FormularioHelper(View activity, Handler handler) {

        this.handler = handler;

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
        data.setText("01/08/2017");
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

    public void salvar(AgendaDO agenda) {
        try {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            final String userId = agenda.getIdAgenda();
            final AgendaDO agendaDO = agenda;
            mDatabase.child("agendas").child(userId).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            AgendaDO noBanco = dataSnapshot.getValue(AgendaDO.class);
                            if (noBanco == null) {
                                mDatabase.child("agendas").child(userId).setValue(agendaDO);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void excluir(Activity activity) {
        try {
            AgendaDO agenda = pegaAgenda();
//            final DynamoDBMapper mapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
            // Retrieve an item by passing the partition key using the object mapper.
            //  AgendaDO agendaDOAux = new AgendaDO();
            //agendaDOAux.setIdUser("1c6cbbd9-7b2f-484e-a1b2-050b7eb49a86");
            //   agenda = mapper.load(AgendaDO.class,agendaDOAux);
//            AgendaDO agendaBD = mapper.load(AgendaDO.class, "a2800964-55a3-4673-833b-fb8f47f55edd", "Status");

//            boolean apagou = new UsuarioDynamoDBDAO(activity, handler).apagar(agendaBD);
//            if (apagou) {
//                Toast.makeText(activity, "Agenda Apagada com Sucesso!", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(activity, "Erro ao apagar!", Toast.LENGTH_SHORT).show();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        catch (ParseException e) {
//            e.printStackTrace();
//        }
    }

//    public AgendaDO listar(Activity activity) {
//        try {
//            PaginatedScanList<AgendaDO> agendas = new UsuarioDynamoDBDAO(activity, handler).listar();
//            for (AgendaDO agendaDO : agendas) {
//                Log.d("Agendas", "ID agenda: " + agendaDO.getIdAgenda() + " Status: " + agendaDO.getStatus());
//            }
//            return agendas.get(0);
//        } catch (EasyGameException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public boolean validarCampos() {

        return false;
    }

    public AgendaDO pegaAgenda() throws ParseException {

        if (campoDat.getText().toString().equals("")) {
            campoDat.setError("Informe a data!!");
        }
        Date inicio = FormatarData.getFormato().parse(campoDat.getText().toString());

        Eventos evento = (Eventos) campoEvento.getSelectedItem();
        agenda.setEvento(evento.toString());
        agenda.setData(inicio.getTime());

        //String fim = (campoHora.getText().toString());
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

    public void preencheFormulario(AgendaDO agenda) throws ParseException {
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
        this.agenda = agenda;
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
}
