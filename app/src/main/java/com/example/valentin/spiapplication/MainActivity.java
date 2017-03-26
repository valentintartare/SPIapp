package com.example.valentin.spiapplication;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import junit.framework.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //Variable globales requise pour les sauvegardes BUNDLE
    ArrayList<String> tabtaches=new ArrayList<String>();
    Date date=new Date();
    String tache;
    int id=0;
    ArrayList<String> pieceajoute=new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        date.setYear(2000+Integer.parseInt((date.getYear()+"").substring(1,3)));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view){
        EditText eT=(EditText)findViewById(R.id.emailChamp);
        if(eT.getText().equals("client")){

        }else {
            afficheCalendar();
        }
    }

    public void selectDate(View view){
        id=2;
        CalendarView cV=(CalendarView)findViewById(R.id.simpleCalendarView);
        System.out.println("La date est :"+date);
        setContentView(R.layout.tache_jour);
        final ListView maisie=(ListView)findViewById(R.id.listItem);
        TextView tV=(TextView)findViewById(R.id.datejour);
        tV.setText("Taches du "+date.getDate()+"/"+(date.getMonth()+1)+"/"+date.getYear());
        if(tabtaches.isEmpty()) {
            tabtaches.add("Roulettes : Auchan Leers");
            tabtaches.add("Ressort : E.Leclerc Tourcoing");
            tabtaches.add("Poulie : Amazon Paris");
            tabtaches.add("Cables : Carglass Lille");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, tabtaches);
        maisie.setAdapter(adapter);
        maisie.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tache=maisie.getItemAtPosition(position).toString();
                System.out.println(tache);
                rapportGet();
            }
        });
    }

    public void retourCalendar(View view){
        if(!tabtaches.isEmpty()){
           tabtaches.clear();
        }
        afficheCalendar();
    }

    public void afficheCalendar(){
        id=1;
        setContentView(R.layout.emploi_du_temps);
        CalendarView cV=(CalendarView)findViewById(R.id.simpleCalendarView);
        cV.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                date=new Date(year,month,dayOfMonth);
            }
        });

    }

    public void rapportGet() {
        id=3;
        setContentView(R.layout.entrer_rapport);
        TextView tV3=(TextView)findViewById(R.id.votrerapport);
        tV3.setText("Rapport : "+tache);
        Button button=(Button)findViewById(R.id.retourintervention);
        final TextView tV4=(TextView)findViewById(R.id.pieceadd);
        Button add=(Button)findViewById(R.id.addpiece);
        final ListView listView=(ListView)findViewById(R.id.liste);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(tache);
                tabtaches.remove(tabtaches.indexOf(tache));
                returntache(new View(MainActivity.this));
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pieceajoute.add(tV4.getText()+"");
                tV4.setText("");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                        android.R.layout.simple_list_item_1, pieceajoute);
                listView.setAdapter(adapter);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pieceajoute.remove(pieceajoute.indexOf(listView.getItemAtPosition(position).toString()));
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                        android.R.layout.simple_list_item_1, pieceajoute);
                listView.setAdapter(adapter);
            }
        });
    }

    public void returntache(View view) {
        id=2;
        CalendarView cV = (CalendarView) findViewById(R.id.simpleCalendarView);
        System.out.println("La date est :" + date);
        setContentView(R.layout.tache_jour);
        final ListView maisie = (ListView) findViewById(R.id.listItem);
        TextView tV = (TextView) findViewById(R.id.datejour);
        if(tabtaches.isEmpty()){
            TextView end=(TextView)findViewById(R.id.listeVide);
            end.setText("Vous avez completer toutes vos taches");
        }
        tV.setText("Taches du " + date.getDate() + "/" + (date.getMonth()+1) + "/" +date.getYear());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, tabtaches);
        maisie.setAdapter(adapter);
        maisie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tache=maisie.getItemAtPosition(position).toString();
                System.out.println(tache);
                rapportGet();
            }
        });
    }

    public void onSaveInstanceState(Bundle outState)
    {
        outState.putInt("id",id);
        outState.putStringArrayList("listetache",tabtaches);
        if(date!=null) {
            outState.putInt("jour", date.getDate());
            outState.putInt("month", (date.getMonth()));
            outState.putInt("year", date.getYear());
        }
        if(tache!=null){
            outState.putString("tache",tache);
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState){
        id=savedInstanceState.getInt("id");
        if(id==1){
           afficheCalendar();
        } else if(id>1){
            date=new Date(savedInstanceState.getInt("year"),savedInstanceState.getInt("month"),savedInstanceState.getInt("jour"));
            tabtaches=savedInstanceState.getStringArrayList("listetache");
            tache=savedInstanceState.getString("tache");
            if(id==2)
                selectDate(new View(MainActivity.this));
            else
                rapportGet();
        }

    }
}

