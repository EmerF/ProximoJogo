package br.com.proximojogo.proximojogo.utils;

import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;

import java.lang.reflect.Type;

/**
 * Classe que limpa ou valida todos os campos Edite Text do formulario
 */

public final class LimparCamposFormulario {
    private boolean validou = true ;

    public static void clearForm(ViewGroup group)
    {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setText("");
            }

            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                clearForm((ViewGroup)view);
        }
    }


    /*
    * Percorre o formulario validando se os dados foram preenchidos corretamente
    * Percorre os Contentores.
    * Ex: Se o form tiver vários FrameLayout ele vai percorrer todos e validar os campos do tipo indicado.
    *
    *
    *
     */
    public boolean validaEditTextVazio(ViewGroup group)
    {


        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof android.support.design.widget.TextInputEditText) {
                android.support.design.widget.TextInputEditText view2 = ((android.support.design.widget.TextInputEditText) view);
                String tx = ( view2.getText().toString());

                if(tx.equals("") && view2.getTag() == null){
                    view2.setError("Campo Obrigatório!!");
                    view2.setFocusable(true);

                    validou = false;
                }
            }

            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                validaEditTextVazio((ViewGroup)view);
        }

        return validou;
    }
}
