package br.com.proximojogo.proximojogo.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by emer on 22/08/17.
 */

public final class LimparCamposFormulario {

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


    private void validaCamposForm(ViewGroup group)
    {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setText("");
                if(((EditText) view).getText().equals("")){
                    ((EditText) view).setError("Campo Obrigatório!!");
                }
            }

            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                clearForm((ViewGroup)view);
        }
    }
}
