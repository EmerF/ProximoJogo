package br.com.proximojogo.proximojogo.helper;

import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.proximojogo.proximojogo.entity.Time;


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
        viewAtiva = activity;


    }


    public Time pegaTime(){

        time.setNomeTime(campoNomeTime.getText().toString());
        time.setResponsavelTime(campoResponsavelTime.getText().toString());
        time.setTelefoneResponsavel(campoTelefoneResponsavel.getText().toString());

        return time;

    }

    public void salvar(View activity) {

        mDatabaseAgenda = FirebaseDatabase.getInstance().getReference("Times");
        Time time = pegaTime();
        if (time.getIdTime() == null) {
            //getUser id do Firebase para setar na agenda
            // e colocar o nome junto com o id para identificar o nó
            String key = mDatabaseAgenda.push().getKey();
            time.setIdTime(key);
            time.setIdResponsavel("Txr5w0STR5YX2r9QjGuqab0KOB13");// substituir pelo id do usuário qdo o login estiver pronto
            //mDatabaseAgenda.child(agenda.getIdAgenda()).setValue(agenda);
            mDatabaseAgenda.setValue(time);

            Toast.makeText(activity.getContext(), "Agenda Cadastrada com Sucesso!", Toast.LENGTH_SHORT).show();
        } else {
            mDatabaseAgenda.child(time.getIdTime()).setValue(time);
            Toast.makeText(activity.getContext(), "Agenda Editada com Sucesso!", Toast.LENGTH_SHORT).show();
        }
    }

    public EditText getCampoNomeTime() {
        return campoNomeTime;
    }

    public void setCampoNomeTime(EditText campoNomeTime) {
        this.campoNomeTime = campoNomeTime;
    }

    public EditText getCampoResponsavelTime() {
        return campoResponsavelTime;
    }

    public void setCampoResponsavelTime(EditText campoResponsavelTime) {
        this.campoResponsavelTime = campoResponsavelTime;
    }

    public EditText getCampoTelefoneResponsavel() {
        return campoTelefoneResponsavel;
    }

    public void setCampoTelefoneResponsavel(EditText campoTelefoneResponsavel) {
        this.campoTelefoneResponsavel = campoTelefoneResponsavel;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
}
