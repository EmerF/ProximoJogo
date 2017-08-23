package br.com.proximojogo.proximojogo.utils;

import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by emer on 22/08/17.
 */

public final class LimparCamposFormulario {
    private boolean validou = true;

    private void clearForm(ViewGroup group)
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


    public boolean validaCamposForm(ViewGroup group)
    {

        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof android.support.design.widget.TextInputEditText) {
                String tx = ((android.support.design.widget.TextInputEditText) view).getText().toString();
                System.out.println("Texto: "+ tx);
                if(tx.equals("")){
                    ((android.support.design.widget.TextInputEditText) view).setError("Campo Obrigatório!!");
                    view.setFocusable(true);
                    view.requestFocus();
                    validou = false;
                }
            }

            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                validaCamposForm((ViewGroup)view);
        }

        return validou;
    }
}
