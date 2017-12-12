package br.com.proximojogo.proximojogo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.lang.ref.WeakReference;
import java.text.ParseException;

import br.com.proximojogo.proximojogo.MainActivity;
import br.com.proximojogo.proximojogo.R;
import br.com.proximojogo.proximojogo.entity.Arena;
import br.com.proximojogo.proximojogo.helper.HelperArena;
import br.com.proximojogo.proximojogo.utils.LimparCamposFormulario;

public class ArenaFragment extends Fragment implements View.OnClickListener {

    private HelperArena helper;


    private View arenaView;
    private EditText obs;
    //estava usando só no Dynamo
    private Handler handler;
    private Activity activity;
    private boolean salvou;
    private boolean validarCampos;
    private InputMethodManager imm;
    private Button btExcluir;
    private Button btSalvar;
    private String idArena;

    static class ArenaHandler extends Handler {
        WeakReference<ArenaFragment> weakArenaFragment;

        public ArenaHandler(ArenaFragment arenaFragment) {
            weakArenaFragment = new WeakReference<ArenaFragment>(arenaFragment);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            if (weakArenaFragment != null) {
                new AlertDialog.Builder(weakArenaFragment.get().activity)
                        .setTitle("Aviso")
                        .setMessage("Arena Salva com Sucesso!!!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
//                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // do nothing
//
//                            }
//                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        handler = new ArenaHandler(this);
        arenaView = inflater.inflate(R.layout.fragment_cadastrar_arena, container, false);
        helper = new HelperArena(arenaView, handler);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Cadastrar Arena");
        btSalvar = (Button) arenaView.findViewById(R.id.bt_salvar_arena);
        btSalvar.setOnClickListener(this);

        btExcluir = (Button) arenaView.findViewById(R.id.bt_excluir_arena);
         btExcluir.setOnClickListener(this);

        setRetainInstance(true);
        return arenaView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            idArena = bundle.getString("idArena");
            Arena arena = new Arena();
            arena.setIdArena(idArena    );
            arena.setNomeArena(bundle.getString("nomeArena"));
            arena.setEndereco(bundle.getString("enderecoArena"));
            arena.setTelefone(bundle.getString("telefoneArena"));

            if (arena != null) {
                try {
                    helper.preencheFormulario(arena);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }

    }

    public void salvarArena(View v) {
        try {
           LimparCamposFormulario lf = new LimparCamposFormulario();
           validarCampos = lf.validaEditTextVazio((ViewGroup)this.getView());
            if(!validarCampos){

                ExibirToast.ExibirToastComIcone(activity,R.drawable.alerta,R.color.colorRed,"Preencha os campos, meu Bem!");

            }else {
                salvou = helper.salvar(v);
                getFragmentManager().beginTransaction().replace(R.id.container, new ListaArenas()).commit();

            }
        } catch (Exception e) {
            ExibirToast.ExibirToastComIcone(activity,R.drawable.alerta,R.color.colorRed,"Erro ao salvar a arena ): ");
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.bt_salvar_arena) {

            salvarArena(v);

            if (salvou) {
                getFragmentManager().beginTransaction().replace(R.id.container, new ListaArenas()).commit();

            }

        }else if(i == R.id.bt_excluir_arena){
                helper.excluir(v,idArena);
                getFragmentManager().beginTransaction().replace(R.id.container, new ListaArenas()).commit();
        }
    }
        @Override
        public void onCreate (@Nullable Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        @Override
        public void onConfigurationChanged (Configuration newConfig){
            super.onConfigurationChanged(newConfig);
        }


    }

