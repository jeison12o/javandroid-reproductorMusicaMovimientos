package com.example.reproductor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import de.hdodenhof.circleimageview.CircleImageView;

import java.io.IOException;

public class GestionarCancion extends AppCompatActivity implements SensorEventListener {

    CircleImageView btn_left, btn_rigth, btn_pause_play, btn_stop, btn_noMute_Mute;
    TextView lblnombreTema;
    boolean isSonando=true;
    boolean isMuteado=true;
    String nombreCancion;
    MediaPlayer mp3;
    boolean isStop=false;

    //sensores
    SensorManager sensorManager;
    Sensor sensor;

    //siguiente, atras
    int position;
    String[] listaNombreCanciones;
    String nuevaCancion = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar_cancion);

        btn_left =  findViewById(R.id.btnLeft);
        btn_rigth = findViewById(R.id.btnRigth);
        btn_pause_play = findViewById(R.id.btnPause);
        btn_stop = findViewById(R.id.btn_detener);
        btn_noMute_Mute = findViewById(R.id.btn_silence);
        lblnombreTema = findViewById(R.id.lblNombretema);

        Bundle b = getIntent().getExtras();
        position = b.getInt("posicion");
        nombreCancion = (String) b.getString("nombre");
        listaNombreCanciones = b.getStringArray("playList");
        //Se reprodice el disco
        iniciarCancion();

        //indico que servicio a utilizar, este caso, el sensor del sistema
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //Elijo el tipo de sensor y lo obtengo, el que se usará normalmente
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        //configuracion para leer el los valores que entrega el sensor, NO OLVIDAR LA IMPLEMENTACION DE SensorEventListener
        sensorManager.registerListener(GestionarCancion.this, sensor, SensorManager.SENSOR_DELAY_NORMAL);



        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position--;
                if(position >= 0){
                    mp3.stop();
                    mp3 = anteriorCancion(position);
                    mp3.start();
                    lblnombreTema.setText(nuevaCancion);
                }else {
                    Toast.makeText(getApplicationContext(), "Ya no hay mas canciones", Toast.LENGTH_SHORT).show();
                    position++;
                }
            }
        });


        btn_rigth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;
                if (position <= listaNombreCanciones.length-1){
                    mp3.stop();
                    mp3 = siguienteCancion(position);
                    mp3.start();
                    lblnombreTema.setText(nuevaCancion);
                }else {
                    Toast.makeText(getApplicationContext(), "No hay mas caciones", Toast.LENGTH_LONG).show();
                    position--;
                }
            }
        });

        btn_pause_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (isStop)
               {
                   btn_pause_play.setImageResource(R.drawable.btn_pause);
                   isStop=false;
                   iniciarCancion();
               }else {
                   if(isSonando == true){
                       btn_pause_play.setImageResource(R.drawable.btn_play);
                       pause();
                       isSonando=false;
                   }else{
                       btn_pause_play.setImageResource(R.drawable.btn_pause);
                       play();
                       isSonando=true;
                   }
               }
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
                btn_pause_play.setImageResource(R.drawable.btn_play);
            }
        });

        btn_noMute_Mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMuteado==true){
                    btn_noMute_Mute.setImageResource(R.drawable.btn_mute);
                    mute();
                    isMuteado=false;

                }else {
                    btn_noMute_Mute.setImageResource(R.drawable.btn_no_mute);
                    noMute();
                    isMuteado=true;
                }
            }
        });
    }


    public void nextTheme(){

    }

    public void beforeTheme(){

    }

    public MediaPlayer anteriorCancion(int posicion){
        nuevaCancion = listaNombreCanciones[posicion];
        Uri urlCancion = Uri.parse(Environment
                .getExternalStorageDirectory().getAbsolutePath()+"/carpetaCanciones/"+nuevaCancion);
        return MediaPlayer.create(this, urlCancion);
    }

    public void iniciarCancion(){
        lblnombreTema.setText(nombreCancion);

        //obtengo la dirrecion para ir hacia la cancion y en ella hacer la accion determinada
        Uri rutaArchivo = Uri.parse(Environment.
                getExternalStorageDirectory().getAbsolutePath()+"/carpetaCanciones/"+nombreCancion);

        try {
            mp3.reset();
        }catch (Exception e) {
        }

        //le asigno a la cancion el reproductor
        mp3 = MediaPlayer.create(this, rutaArchivo);
        play();
    }

    public void play(){
        try {
            //el reproductor coloca a sonar la cancion
            mp3.start();
        }catch (Exception e){

        }
    }

    public void pause(){
        try {
            mp3.pause();
        }catch (Exception e){

        }
    }

    public void stop(){
        try {
            mp3.stop();
            isStop = true;
            lblnombreTema.setText("");
        }catch (Exception e){

        }
    }

    public void mute(){
        mp3.setVolume(0,0);
    }

    public void noMute(){
        mp3.setVolume(1,1);
    }


    //Este evento es que pausa y despausa la cancion
    @Override
    public void onSensorChanged(SensorEvent event) {
            Float distancia = Float.parseFloat(String.valueOf(event.values[0]));
            if(distancia < 5)
            {
                pause();
            } else {
                play();
            }
    }

    public MediaPlayer siguienteCancion(int posicion){
        nuevaCancion = listaNombreCanciones[posicion];
        Uri urlCancion = Uri.parse
                (Environment.getExternalStorageDirectory().getAbsolutePath()+"/EamCanciones/"+nuevaCancion);
        return MediaPlayer.create(this, urlCancion);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}