package com.example.valentin.spiapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //Variable globales requise pour les sauvegardes BUNDLE
    ArrayList<String> tabtaches=new ArrayList<String>();
    Date date=new Date();
    String tache;
    int id=0;
    ArrayList<String> pieceajoute=new ArrayList<String>();
    Date [] tempsTravail=new Date[2];
    Boolean valide=new Boolean(false);
    SetConnexionRest connexion=new SetConnexionRest();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        date.setYear(2000+Integer.parseInt((date.getYear()+"").substring(1,3)));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view) {
        EditText eT = (EditText) findViewById(R.id.emailChamp);
        final String login = eT.getText() + "";


        connexion.appelRestUser(login, this, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.substring(1);
                response = response.substring(0, response.length() - 1);
                Gson test = new Gson();
                test testage = test.fromJson(response, test.class);
                if (testage.getName().compareTo(login) == 0) {
                    afficheCalendar();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                TextView erreur = (TextView) findViewById(R.id.badconnexion);
                erreur.setText("Echec de la connexion, vous n'êtez pas inscrit !\n\n - Si vous êtes un technicien, " +
                        "contactez un administrateur afin qu'un compte vous soit crée\n- Si vous êtes un client, inscrivez vous sur le site WEB et réessayez");
            }
        });
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
        Button bt=(Button)findViewById(R.id.depart);
        bt.setClickable(false);
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

    public String getHeure(boolean time){
        Date d = new Date();
        if(time)
            tempsTravail[0]=d;
        else
            tempsTravail[1]=d;
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
        String s = f.format(d);
        s=s.substring(9,11)+"H"+s.substring(11,13);
        return s;
    }
    public void setHeureArrivee(View view){
        TextView tV=(TextView)findViewById(R.id.setArrival);
        Button btdep=(Button)findViewById(R.id.depart);
        Button bt=(Button)findViewById(R.id.ButArrive);
        tV.setText("Arrivée : "+getHeure(true));
        btdep.setClickable(true);
        bt.setClickable(false);
    }

    public void setHeureDepart(View view){
        TextView tV=(TextView)findViewById(R.id.setDeparture);
        tV.setText("Départ : "+getHeure(false));
        setTempsTravail();
    }

    public void setTempsTravail(){
        TextView tV=(TextView)findViewById(R.id.tdtotal);
        Date tdt=new Date((tempsTravail[1].getTime()-tempsTravail[0].getTime())-3600000);
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
        String s = f.format(tdt);
        s=s.substring(9,11)+"H"+s.substring(11,13);
        tV.setText("Temps de travail total : "+s);
    }

}

