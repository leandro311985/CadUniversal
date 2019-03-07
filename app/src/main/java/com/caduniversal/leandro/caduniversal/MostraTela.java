package com.caduniversal.leandro.caduniversal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MostraTela extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_tela);
        Intent intent5 = getIntent();
        Bitmap bitmap = BitmapFactory.decodeFile(intent5.getStringExtra("img"));
        ImageView imageView = findViewById(R.id.mostraFoto);
        if (bitmap == null){
            imageView.setImageResource(R.drawable.perfil);
        }else
        imageView.setImageBitmap(bitmap);



        Intent intent = getIntent();
        String parametro = (String)intent.getSerializableExtra("nome");
        TextView textView = findViewById(R.id.textView1);
        Typeface font1 = Typeface.createFromAsset(getAssets(), "Tox_Typewriter.ttf");
        textView.setTypeface(font1);
        textView.setText(parametro);

        Intent intent2 = getIntent();
        String endereço = (String)intent2.getSerializableExtra("end");
        TextView textView2 = findViewById(R.id.textView2);
        Typeface font2 = Typeface.createFromAsset(getAssets(), "Tox_Typewriter.ttf");
        textView2.setTypeface(font2);
        textView2.setText(endereço);

        Intent intent3 = getIntent();
        String fone = (String)intent3.getSerializableExtra("tel");
        TextView textView3 = findViewById(R.id.textView3);
        Typeface font = Typeface.createFromAsset(getAssets(), "Tox_Typewriter.ttf");
        textView3.setTypeface(font);
        textView3.setText(fone);

        Intent intent4 = getIntent();
        String dta = (String)intent4.getSerializableExtra("dta");
        TextView textView4 = findViewById(R.id.textView4);
        Typeface font4 = Typeface.createFromAsset(getAssets(), "Tox_Typewriter.ttf");
        textView4.setTypeface(font4);
        textView4.setText( dta);

        Intent inten = getIntent();
        String ped = (String)inten.getSerializableExtra("ped");
        TextView textView5 = findViewById(R.id.textView5);
        Typeface font5 = Typeface.createFromAsset(getAssets(), "Tox_Typewriter.ttf");
        textView5.setTypeface(font5);
        textView5.setText( ped);




    }
}
