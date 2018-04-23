package br.com.proximojogo.proximojogo.helper;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.proximojogo.proximojogo.entity.Jogador;

public class HelperJogador {

    private static final String TAG = HelperJogador.class.getName().toUpperCase();
    private DatabaseReference mDatabaseAgenda;

    public HelperJogador() {

    }

    //https://www.simplifiedcoding.net/firebase-realtime-database-crud/
    public boolean salvar(View activity, Jogador Jogador) {
        boolean salvou = false;
        try {

            if (Jogador.get_idUser().equals("")) {

                String key = mDatabaseAgenda.push().getKey();
                Jogador.set_idUser(key);
            }
            mDatabaseAgenda = FirebaseDatabase.getInstance().getReference("Users");
            mDatabaseAgenda.child(Jogador.get_idUser()).setValue(Jogador);
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

    public boolean atualizarTipoUser(View activity, Jogador Jogador) {
        boolean salvou = false;
        try {

            mDatabaseAgenda = FirebaseDatabase.getInstance().getReference("Users");
            mDatabaseAgenda.child(Jogador.get_idUser()).child("tipoUser").setValue("Administrador");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Erro ao atualizar Usuário" + e.getStackTrace());
            salvou = false;

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
