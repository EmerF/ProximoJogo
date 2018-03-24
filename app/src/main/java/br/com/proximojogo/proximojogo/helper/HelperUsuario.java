
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.LocalDate;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.date.PickersActivity;
import br.com.proximojogo.proximojogo.entity.AgendaDO;
import br.com.proximojogo.proximojogo.entity.Resultado;
import br.com.proximojogo.proximojogo.entity.Usuario;
import br.com.proximojogo.proximojogo.enuns.Eventos;
import br.com.proximojogo.proximojogo.utils.FormatarData;
import br.com.proximojogo.proximojogo.utils.GetUser;

/**
 * Created by ale on 08/08/2017.
 */

public class HelperUsuario {

    private static final String TAG = HelperUsuario.class.getName().toUpperCase();
    //SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

    private Calendar c = Calendar.getInstance();

    private TextView campoObservacao;

    private Spinner spEvento;
    private EditText data;
    private EditText hora;

    private AgendaDO agenda;
    private Handler handler = null;
    private DatabaseReference mDatabaseAgenda;
    private View viewAtiva;
    private DatabaseReference mDatabaseRes;


    /*
        * Captura valores inseridos no formulário...
        * */
    public HelperUsuario(View view, Handler handler) {
        this.handler = handler;
        viewAtiva = view;
        //inicializaCamposTela(viewAtiva);

    }

    //https://www.simplifiedcoding.net/firebase-realtime-database-crud/
    public boolean salvar(View activity, Usuario usuario) {
        boolean salvou = false;
        try {

            if (usuario.get_id().equals("")) {
                //getUser id do Firebase para setar na agenda
                // e colocar o nome junto com o id para identificar o nó
                String key = mDatabaseAgenda.push().getKey();
                usuario.set_id(key);
                // substituir pelo id do usuário qdo o login estiver pronto

            }
            mDatabaseAgenda = FirebaseDatabase.getInstance().getReference("Users");
            mDatabaseAgenda.child(usuario.get_id()).setValue(usuario);
            Toast.makeText(activity.getContext(), "Usuário salvo..", Toast.LENGTH_SHORT).show();
            salvou = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Erro ao salvar Usuário" + e.getStackTrace());
            salvou = false;
            Toast.makeText(activity.getContext(), "Erro ao salvar Usuário." + e.getStackTrace(), Toast.LENGTH_SHORT).show();

        }
        return salvou;
    }




   /* public AgendaDO pegaAgenda() throws ParseException {

        Date inicio = FormatarData.getFormato().parse(campoDat.getText().toString());

        Eventos evento = (Eventos) campoEvento.getSelectedItem();
        agenda.setEvento(evento.toString());
        agenda.setData(inicio.getTime());

        Date hora = FormatarData.getFormatoHora().parse(campoHora.getText().toString());
        agenda.setHora(hora.getTime());
        agenda.setDiaSemana(diaDaSemana(inicio));
        agenda.setIdAgenda(campoIdAgenda.getText().toString());
        agenda.setValor(new Double(campoValor.getText().toString()));
        agenda.setArena(campoLocal.getSelectedItem().toString());
        agenda.setStatus("Status");
        agenda.setIdUser(GetUser.getUserLogado());
        return agenda;

    }
*/

   /* public Resultado pegaResultadoTela() {
        resultado.setTime1(campoTime.getSelectedItem().toString());
        resultado.setTime2(campoAdversario.getSelectedItem().toString());
        if(!agenda.getDataFutura()){
            resultado.setGols1(gols1.getText().toString());
            resultado.setGols2(gols2.getText().toString());
        }


        return resultado;
    }*/


    /*  Preenche o formulário com os dados do objeto recebido como parametro
    *   Seta o objeto recebido no objeto local para fins de edição
    *   Os spinners são preenchidos na inicialização da tela
    *
     */

    /*public void preencheFormulario(final AgendaDO agenda) throws ParseException {


        if (agenda != null) {
            campoEvento.setSelection(Eventos.valueOf(agenda.getEvento()).ordinal());
            campoDat.setText(FormatarData.getDf().format(agenda.getData()));
            campoHora.setText(FormatarData.getDfHora().format(agenda.getHora()));
            if(!agenda.getDataFutura()){
                gols1.setText(agenda.getResultado().getGols1());
                gols2.setText(agenda.getResultado().getGols2());
            }

            //campoDiaSemana.setText(String.valueOf(agenda.getDiaSemana()));
            campoIdAgenda.setText(String.valueOf(agenda.getIdAgenda()));
            campoValor.setText(String.valueOf(agenda.getValor()));

        }


        this.agenda = agenda;
    }*/

    /*public void carregaImagem(String caminhoFoto) {
        if (caminhoFoto != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
            *//*campoFoto.setImageBitmap(bitmapReduzido);
            campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
            campoFoto.setTag(caminhoFoto);*//*

        }


    }*/


}




