package br.com.proximojogo.proximojogo.ui;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import br.com.proximojogo.proximojogo.R;

/**
 * Created by emer on 25/08/17.
 */

public class ExibirToast {

    public static void ExibirToastComIcone(Context activity, int imagem, int color, String mensagem){


        Toast imageToast = new Toast(activity);
        LinearLayout toastLayout = new LinearLayout(activity);
        toastLayout.setOrientation(LinearLayout.VERTICAL);
        ImageView image = new ImageView(activity);

        TextView text = new TextView(activity);
        text.setTextSize(20);
        image.setImageResource(imagem);
        text.setText(mensagem);
        toastLayout.addView(image);
        toastLayout.addView(text);
        imageToast.setView(toastLayout);
        imageToast.setDuration(Toast.LENGTH_LONG);
        imageToast.setGravity(Gravity.BOTTOM,10,40);

        imageToast.show();

    }

}
