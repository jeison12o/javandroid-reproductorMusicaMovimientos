package com.example.reproductor;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String[] listaCanciones;
    ListView listCanciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listCanciones = findViewById(R.id.listaCanciones);
        leerCanciones();


    }

    public void leerCanciones(){
        File carpeta = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "carpetaCanciones");
        if(!carpeta.exists()){
            carpeta.mkdirs();
        }else {
                    //.list() devuelve los nombres que esten en la carpeta, o del contenido que tenga
            listaCanciones=carpeta.list();

            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, listaCanciones);

            listCanciones.setAdapter(arrayAdapter);

            listCanciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String nombreCancion = listaCanciones[position];
                    nextActivity(nombreCancion, position);
                }
            });
        }
    }

    public void nextActivity(String nombreCancion, int position){
        Intent nextActivity = new Intent (getApplicationContext(), GestionarCancion.class);
        nextActivity.putExtra("nombre", nombreCancion);
        nextActivity.putExtra("posicion", position);
        nextActivity.putExtra("playList", listaCanciones);
        startActivity(nextActivity);
    }

}