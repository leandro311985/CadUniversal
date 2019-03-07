package com.caduniversal.leandro.caduniversal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditActivity extends AppCompatActivity  {
    private ContatoInfo contato;

    private View layout;

    private ImageButton foto;
    private EditText nome;
    private EditText ref;
    private TextView dta;
    private EditText fone;
    private EditText end;
    private TextView dataatual;

    public ImageView btnMicro1;
    public ImageView btnMicro2;
    public ImageView btnMicro3;
    public ImageView btnMicro4;
    public ImageView btnMicro5;
    private Button salvar;
    private final int CAMERA = 1;
    private final int GALERIA = 2;
    private final String IMAGE_DIR = "/FotosContatos";

    private DatePickerDialog.OnDateSetListener mDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        btnMicro1 = findViewById(R.id.micro1Id);
        btnMicro2 = findViewById(R.id.micro2Id);
        btnMicro3 = findViewById(R.id.micro3Id);
        btnMicro4 = findViewById(R.id.micro4Id);
        btnMicro5 = findViewById(R.id.imagDate);

        contato = getIntent().getParcelableExtra("contato");

        layout = findViewById(R.id.mainLayout);

        foto = findViewById(R.id.fotoContato);
        nome = findViewById(R.id.nomeContato);
        ref = findViewById(R.id.refContato);
        dta = findViewById(R.id.emailContato);
        fone = findViewById(R.id.foneContato);
        end = findViewById(R.id.endContato);

        nome.setText(contato.getNome());
        ref.setText(contato.getRef());
        dta.setText(contato.getEmail());
        fone.setText(contato.getFone());
        end.setText(contato.getEnd());

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertaImagem();
            }
        });

        File imgFile = new File(contato.getFoto());
        if(imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            foto.setImageBitmap(bitmap);
        }

        salvar = findViewById(R.id.btnSalvar);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contato.setNome(nome.getText().toString());
                contato.setRef(ref.getText().toString());
                contato.setEmail(dta.getText().toString());
                contato.setFone(fone.getText().toString());
                contato.setEnd(end.getText().toString());

                if (contato.getNome().equals("")) {
                    Toast.makeText(EditActivity.this, "É necessário um nome para salvar!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent i = new Intent();
                i.putExtra("contato", contato);
                setResult(RESULT_OK, i);
                finish();
            }
        });
//----------------------------------------------calendario----------------------------------------------------
        btnMicro5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Calendar cal = Calendar.getInstance();

                int ano = cal.get(Calendar.YEAR);
                int mes = cal.get(Calendar.MONTH);
                int dia = cal.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog dialog = new DatePickerDialog(
                        EditActivity.this,
                        mDateSetListener,
                        ano, mes, dia);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                dialog.show();






            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                mes = mes + 1;

                String date = dia + "/" + mes + "/" + ano;
                dta.setText(date);


                Calendar dataInicio = Calendar.getInstance();

                mes = mes -1 ;
                dataInicio.set(ano,mes,dia);
                // Data de hoje
                long datapp = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String dateString = sdf.format(datapp);
                dataatual.setText(dateString);
                // Calcula a diferença entre hoje e da data de inicio
                long diferenca = datapp -
                        dataInicio.getTimeInMillis() ;
                // Quantidade de milissegundos em um dia
                int tempoDia = 1000 * 60 * 60 * 24;
                long diasDiferenca = diferenca / tempoDia;



                if (diasDiferenca >= 7  ){


                    mensagem(diasDiferenca);


                }



            }
        };

        dataatual = findViewById(R.id.dataatual);
        String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        dataatual.setText(currentDateTimeString);

//-----------------------------------------final----------------------------------------------------------------
        }

    private void alertaImagem(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione a fonte da imagem");
        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clicaTirarFoto();
            }
        });
        builder.setNegativeButton("Galeria", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clicaCarregaImagem();
            }
        });
        builder.create().show();
    }

    private void clicaTirarFoto(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED){
            requestCameraPermission();
        } else {
            showCamera();
        }



        // botao para voz-------------------------------------------------------------------------------

    }
    public void micro1 (View view){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        try {
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Não foi possivel Escutar", Toast.LENGTH_SHORT).show();
        }
    }
    public void micro2 (View view){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        try {
            startActivityForResult(intent, 11);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Não foi possivel Escutar", Toast.LENGTH_SHORT).show();
        }
    }
    public void micro3 (View view){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        try {
            startActivityForResult(intent, 22);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Não foi possivel Escutar", Toast.LENGTH_SHORT).show();
        }
    }
    public void micro4 (View view){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        try {
            startActivityForResult(intent, 3);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Não foi possivel Escutar", Toast.LENGTH_SHORT).show();


        }

    }
    //------------------------------fim botao escuta ---------------------------------
    //
    private void requestCameraPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)){

            Snackbar.make(layout, "É necessário permitir para utilizar a câmera!",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(EditActivity.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            CAMERA);
                }
            }).show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    CAMERA);
        }
    }

    private void showCamera(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, CAMERA);
    }

    private void clicaCarregaImagem(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){
            requestGaleriaPermission();
        } else {
            showGaleria();
        }
    }

    private void requestGaleriaPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)){

            Snackbar.make(layout, "É necessário permitir para utilizar a galeria!",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(EditActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            GALERIA);
                }
            }).show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    GALERIA);
        }
    }

    private void showGaleria(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, GALERIA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    clicaTirarFoto();
                }
                break;
            case GALERIA:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    clicaCarregaImagem();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_CANCELED || data == null){
            return;
        }
        if(requestCode == GALERIA){
            Uri contentURI = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                contato.setFoto(saveImage(bitmap));
                foto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(requestCode == CAMERA){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            contato.setFoto(saveImage(bitmap));
            foto.setImageBitmap(bitmap);
        }
        //--------------------------------------------request para Ouvir voz ------------------------------
        else if(requestCode == 0){
            if(resultCode == RESULT_OK && data != null){

                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                nome.setText(result.get(0));

            }
        }
        else if(requestCode == 11){
            if(resultCode == RESULT_OK && data != null){

                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                end.setText(result.get(0));

            }
        }
        else if(requestCode == 22){
            if(resultCode == RESULT_OK && data != null){

                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                fone.setText(result.get(0));

            }
        }
        else if(requestCode == 3){
            if(resultCode == RESULT_OK && data != null){

                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                ref.setText(result.get(0));

            }
        }
//----------------------------------------------------------------------------------------------------------
    }

    private String saveImage(Bitmap bitmap){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        File directory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIR);

        if(!directory.exists()){
            directory.mkdirs();
        }

        try {
            File f = new File(directory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());

            MediaScannerConnection.scanFile(this, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
            fo.close();
            return f.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
    public void mensagem(long diasDiferenca){

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notifications)
                .setTicker("Hearty365")
                .setPriority(NotificationCompat.PRIORITY_HIGH).setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentTitle("Alerta De Almas ")
                .setContentText("Essa Pessoa Encontar A " + diasDiferenca  +" Dias sem presença")
                .setContentInfo("Info").build();

        notificationManager.notify(1, notificationBuilder.build());
    }



}


