package br.com.proximojogo.proximojogo.helper;

import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.util.Date;

import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.date.PickersActivity;
import br.com.proximojogo.proximojogo.entity.AgendaDO;
import br.com.proximojogo.proximojogo.entity.Arena;
import br.com.proximojogo.proximojogo.enuns.Eventos;
import br.com.proximojogo.proximojogo.enuns.NomeArena;
import br.com.proximojogo.proximojogo.helper.HelperAgenda;
import br.com.proximojogo.proximojogo.utils.FormatarData;
import br.com.proximojogo.proximojogo.utils.GetUser;

/**
 * Created by emer on 07/11/17.
 */

public class HelperArena {
    private static final String TAG = "Helper Arena" ;
    private final Handler handler;
    private final View viewAtiva;
    private EditText campoNomeArena;
    private EditText campoTelefoneArena;
    private EditText campoEnderecoArena;
    private EditText campoResponsavelArena;
    private EditText campoHorarioIniArena;
    private EditText campoHorarioFimArena;
    private DatabaseReference mDatabaseArena;
    private Arena arena;

    public HelperArena(View activity, Handler handler) {
        this.handler = handler;
        inicializaCamposTela(activity);
        viewAtiva = activity;

    }

    public boolean salvar(View activity) {
        boolean salvou = false;

        mDatabaseArena = FirebaseDatabase.getInstance().getReference("Arenas");
        Arena arena = pegaArena();
        if (arena.getIdArena() == null) {
            //getUser id do Firebase para setar na agenda
            // e colocar o nome junto com o id para identificar o nó
            String key = mDatabaseArena.push().getKey();
            arena.setIdArena(key);
            // substituir pelo id do usuário qdo o login estiver pronto
            //mDatabaseAgenda.child(agenda.getIdAgenda()).setValue(agenda);
            mDatabaseArena.child(arena.getIdArena()).setValue(arena);

            Toast.makeText(activity.getContext(), "Arena Cadastrada com Sucesso!", Toast.LENGTH_SHORT).show();
        } else {
            mDatabaseArena.child(arena.getIdArena()).setValue(arena);
            Toast.makeText(activity.getContext(), "Agenda Editada com Sucesso!", Toast.LENGTH_SHORT).show();
        }
        salvou = true;
        return salvou;
    }

    public void excluir(View activity, String idArena) {
        try {
            mDatabaseArena = FirebaseDatabase.getInstance().getReference().child("Arenas/" + idArena);
            mDatabaseArena.removeValue();
            Toast.makeText(activity.getContext(), "Arena Apagada com Sucesso!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity.getContext(), "Erro ao Apagar Arena!", Toast.LENGTH_SHORT).show();
        }
    }

    private Arena pegaArena() {

        try {
            arena.setNomeArena(campoNomeArena.getText().toString());
            arena.setEndereco(campoEnderecoArena.getText().toString());
            arena.setTelefone(campoTelefoneArena.getText().toString());
            arena.setResponsavel(campoResponsavelArena.getText().toString());
            arena.setHorarioIniAtendimento(campoHorarioIniArena.getText().toString());
            arena.setHorarioFimAtendimento(campoHorarioFimArena.getText().toString());

        }catch (Exception e){
            Log.d(TAG, "erro pegaArena: ");
            e.printStackTrace();
        }
        return arena;
    }

    /*  Preenche o formulário com os dados do objeto recebido como parametro
  *   Seta o objeto recebido no objeto local para fins de edição
  *
  *
   */
    public void preencheFormulario(Arena arena) throws ParseException {

        if (arena != null) {

            campoNomeArena.setText(arena.getNomeArena());
            campoEnderecoArena.setText(arena.getEndereco());
            campoTelefoneArena.setText(arena.getTelefone());
            campoHorarioIniArena.setText(arena.getHorarioIniAtendimento().toString());
            campoHorarioFimArena.setText(arena.getHorarioFimAtendimento().toString());
            this.arena = arena;
        }

    }

    private void inicializaCamposTela(View activity) {

        campoNomeArena = (EditText) activity.findViewById(R.id.nome_arena);
        campoTelefoneArena = (EditText) activity.findViewById(R.id.telefone_arena);
        campoEnderecoArena = (EditText) activity.findViewById(R.id.endereco_arena);
        campoResponsavelArena=(EditText) activity.findViewById(R.id.responsavel_arena);
        // horario de inicio do atendimento
        campoHorarioIniArena = (EditText) activity.findViewById(R.id.horario_ini_arena);
        campoHorarioIniArena.setInputType(InputType.TYPE_NULL);
        new PickersActivity(campoHorarioIniArena, activity.getContext(), 1);
        // horario de fim do atendimento
        campoHorarioFimArena = (EditText) activity.findViewById(R.id.horario_fim_arena);
        campoHorarioFimArena.setInputType(InputType.TYPE_NULL);
        new PickersActivity(campoHorarioFimArena, activity.getContext(), 1);
        arena = new Arena();
    }
}
