package br.com.proximojogo.proximojogo.helper;

import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;

import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.entity.AgendaDO;
import br.com.proximojogo.proximojogo.entity.Time;
import br.com.proximojogo.proximojogo.enuns.Eventos;
import br.com.proximojogo.proximojogo.enuns.NomeArena;
import br.com.proximojogo.proximojogo.utils.FormatarData;
import br.com.proximojogo.proximojogo.utils.GetUser;


/**
 * Created by emer on 16/09/17.
 */

public class HelperTime {

    private final View viewAtiva;
    private EditText campoNomeTime;
    private EditText campoResponsavelTime;
    private EditText campoTelefoneResponsavel;
    private Handler handler = null;
    private Time time;
    private DatabaseReference mDatabaseAgenda;


    public HelperTime(View activity,Handler handler){
        this.handler = handler;
        inicializaCamposTela(activity);
        viewAtiva = activity;


    }


    public Time pegaTime(){
        time.setNomeTime(campoNomeTime.getText().toString());
        time.setResponsavelTime(campoResponsavelTime.getText().toString());
        time.setTelefoneResponsavel(campoTelefoneResponsavel.getText().toString());
        time.setIdResponsavel(GetUser.getUserLogado());

        return time;

    }

    public void inicializaCamposTela(View activity) {
        campoNomeTime = (EditText) activity.findViewById(R.id.nome_time);
        campoResponsavelTime= (EditText) activity.findViewById(R.id.responsavel_time);
        campoTelefoneResponsavel = (EditText) activity.findViewById(R.id.telefone_responsavel_time);

        time = new Time();

    }

    public void preencheFormulario(Time time) throws ParseException {
        if (time != null) {
            campoNomeTime.setText(time.getNomeTime());
            campoResponsavelTime.setText(time.getResponsavelTime());
            campoTelefoneResponsavel.setText(time.getTelefoneResponsavel());
            this.time = time;
        }

    }

    public void salvarTime(View activity) {

        mDatabaseAgenda = FirebaseDatabase.getInstance().getReference("Times");

        Time time = pegaTime();
        if (time.getIdTime() == null) {
            //getUser id do Firebase para setar na agenda
            // e colocar o nome junto com o id para identificar o nó
            String key = mDatabaseAgenda.push().getKey();
            time.setIdTime(key);
            //time.setIdResponsavel(id);// substituir pelo id do usuário qdo o login estiver pronto
            //mDatabaseAgenda.child(agenda.getIdAgenda()).setValue(agenda);
            mDatabaseAgenda.child(time.getIdResponsavel()+ "/" +  time.getIdTime()).setValue(time);

            Toast.makeText(activity.getContext(), "Time Cadastrado com Sucesso!", Toast.LENGTH_SHORT).show();
        } else {
            mDatabaseAgenda.child(time.getIdResponsavel()+ "/" + time.getIdTime()).setValue(time);
            Toast.makeText(activity.getContext(), "Time Editado com Sucesso!", Toast.LENGTH_SHORT).show();
        }
    }


    public void excluir(View activity, String idTime) {
        try {
            mDatabaseAgenda = FirebaseDatabase.getInstance().getReference().child("Times/" + GetUser.getUserLogado() + "/" + idTime);
            mDatabaseAgenda.removeValue();
            Toast.makeText(activity.getContext(), "Time apagado com Sucesso!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity.getContext(), "Erro ao Apagar Time!", Toast.LENGTH_SHORT).show();
        }
    }
}
