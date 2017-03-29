package com.example.valentin.spiapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //Variable globales requise pour les sauvegardes BUNDLE
    ArrayList<String> tabtaches = new ArrayList<String>();
    Date date = new Date();
    String tache;
    int id = 0;
    ArrayList<String> pieceajoute = new ArrayList<String>();
    Date[] tempsTravail = new Date[2];
    Boolean valide = new Boolean(false);

    //Variables photo globales
    SetConnexionRest connexion = new SetConnexionRest();
    Button takePictureButton;
    ImageView imageView;
    Uri file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*System.setProperty("http.proxyHost","cache.univ-lille1.fr");
        System.setProperty("http.proxyPort","3128");*/
        date.setYear(2000 + Integer.parseInt((date.getYear() + "").substring(1, 3)));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view) {
        EditText eT = (EditText) findViewById(R.id.emailChamp);
        EditText pass = (EditText) findViewById(R.id.Password);
        final String login = eT.getText() + "";
        final String password = pass.getText() + "";
        //TEMPORAIRE
        if (login.equals("tech")) {
            afficheCalendar();
        } else if (login.equals("client")) {
            id = 5;
            setContentView(R.layout.client_choix);
        } else {

            connexion.appelRestUser(login, password, this, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    TextView erreur = (TextView) findViewById(R.id.badconnexion);
                    erreur.setText(response);
                /*response = response.substring(1);
                response = response.substring(0, response.length() - 1);
                    JsonReader jsonReader=new JsonReader();
                    String[] allJson;
                    allJson=response.split("\\{");
                    for(int i=0;i<allJson.length;i++){
                        System.out.println(allJson[i]);
                    }


                Gson test = new Gson();
                test testage = test.fromJson(response, test.class);
                    if (testage.getName().compareTo(login) == 0) {
                        afficheCalendar();
                    }*/
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
    }

    public void selectDate(View view) {
        id = 2;
        CalendarView cV = (CalendarView) findViewById(R.id.simpleCalendarView);
        System.out.println("La date est :" + date);
        setContentView(R.layout.tache_jour);
        final ListView maisie = (ListView) findViewById(R.id.listItem);
        TextView tV = (TextView) findViewById(R.id.datejour);
        tV.setText("Taches du " + date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getYear());
        if (tabtaches.isEmpty()) {
            tabtaches.add("Roulettes : Auchan Leers");
            tabtaches.add("Ressort : E.Leclerc Tourcoing");
            tabtaches.add("Poulie : Amazon Paris");
            tabtaches.add("Cables : Carglass Lille");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, tabtaches);
        maisie.setAdapter(adapter);
        maisie.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tache = maisie.getItemAtPosition(position).toString();
                System.out.println(tache);
                rapportGet();
            }
        });
    }

    public void retourCalendar(View view) {
        if (!tabtaches.isEmpty()) {
            tabtaches.clear();
        }
        afficheCalendar();
    }

    public void afficheCalendar() {
        id = 1;
        setContentView(R.layout.emploi_du_temps);
        CalendarView cV = (CalendarView) findViewById(R.id.simpleCalendarView);
        cV.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                date = new Date(year, month, dayOfMonth);
            }
        });

    }

    public void rapportGet() {
        id = 3;
        setContentView(R.layout.entrer_rapport);
        Button bt = (Button) findViewById(R.id.depart);
        bt.setClickable(false);
        TextView tV3 = (TextView) findViewById(R.id.votrerapport);
        tV3.setText("Rapport : " + tache);
        Button button = (Button) findViewById(R.id.retourintervention);
        final TextView tV4 = (TextView) findViewById(R.id.pieceadd);
        Button add = (Button) findViewById(R.id.addpiece);
        final ListView listView = (ListView) findViewById(R.id.liste);
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
                pieceajoute.add(tV4.getText() + "");
                tV4.setText("");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                        android.R.layout.simple_list_item_1, pieceajoute);
                listView.setAdapter(adapter);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
        id = 2;
        CalendarView cV = (CalendarView) findViewById(R.id.simpleCalendarView);
        System.out.println("La date est :" + date);
        setContentView(R.layout.tache_jour);
        final ListView maisie = (ListView) findViewById(R.id.listItem);
        TextView tV = (TextView) findViewById(R.id.datejour);
        if (tabtaches.isEmpty()) {
            TextView end = (TextView) findViewById(R.id.listeVide);
            end.setText("Vous avez completer toutes vos taches");
        }
        tV.setText("Taches du " + date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getYear());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, tabtaches);
        maisie.setAdapter(adapter);
        maisie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tache = maisie.getItemAtPosition(position).toString();
                System.out.println(tache);
                rapportGet();
            }
        });
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("id", id);
        outState.putStringArrayList("listetache", tabtaches);
        if (date != null) {
            outState.putInt("jour", date.getDate());
            outState.putInt("month", (date.getMonth()));
            outState.putInt("year", date.getYear());
        }
        if (tache != null) {
            outState.putString("tache", tache);
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        id = savedInstanceState.getInt("id");
        if (id == 1) {
            afficheCalendar();
        } else if (id > 1 && id<4) {
            date = new Date(savedInstanceState.getInt("year"), savedInstanceState.getInt("month"), savedInstanceState.getInt("jour"));
            tabtaches = savedInstanceState.getStringArrayList("listetache");
            tache = savedInstanceState.getString("tache");
            if (id == 2)
                selectDate(new View(MainActivity.this));
            else
                rapportGet();
        }else if(id==5){
            retourMenuClient(new View(MainActivity.this));
        }else if(id==6){
            prendrePhoto(new View(MainActivity.this));
        }else if(id==7){
            goMaintenance(new View(MainActivity.this));
        }

    }

    public String getHeure(boolean time) {
        Date d = new Date();
        if (time)
            tempsTravail[0] = d;
        else
            tempsTravail[1] = d;
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
        String s = f.format(d);
        s = s.substring(9, 11) + "H" + s.substring(11, 13);
        return s;
    }

    public void setHeureArrivee(View view) {
        TextView tV = (TextView) findViewById(R.id.setArrival);
        Button btdep = (Button) findViewById(R.id.depart);
        Button bt = (Button) findViewById(R.id.ButArrive);
        tV.setText("Arrivée : " + getHeure(true));
        btdep.setClickable(true);
        bt.setClickable(false);
    }

    public void setHeureDepart(View view) {
        TextView tV = (TextView) findViewById(R.id.setDeparture);
        tV.setText("Départ : " + getHeure(false));
        setTempsTravail();
    }

    public void setTempsTravail() {
        TextView tV = (TextView) findViewById(R.id.tdtotal);
        Date tdt = new Date((tempsTravail[1].getTime() - tempsTravail[0].getTime()) - 3600000);
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
        String s = f.format(tdt);
        s = s.substring(9, 11) + "H" + s.substring(11, 13);
        tV.setText("Temps de travail total : " + s);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePictureButton.setEnabled(true);
            }
        }
    }

    public void prendrePhoto(View view){
        id=6;
        setContentView(R.layout.prise_photo);
        Button rapport=(Button) findViewById(R.id.suiteSAV);
        rapport.setVisibility(View.INVISIBLE);
    }

    public void takePicture(View view) {
        takePictureButton = (Button) findViewById(R.id.button_image);
        Button rapport=(Button) findViewById(R.id.suiteSAV);
        rapport.setVisibility(View.VISIBLE);
        imageView= (ImageView) findViewById(R.id.imageview);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                imageView.setImageURI(file);
            }
        }
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

    public void retourMenuClient(View view){
        id=5;
        setContentView(R.layout.client_choix);
    }

    public void goMaintenance(View view){
        id=7;
        String [] tab=new String[]{"Nettoyage","Révision","Réparation"};
        setContentView(R.layout.rdv_maintenance);
        final ListView lV=(ListView)findViewById(R.id.listIntervention);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_checked, tab);
        lV.setAdapter(adapter);
        lV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lV.setItemChecked(position,true);
            }
        });
    }

    public void mail(View view){
        setContentView(R.layout.webnav);
        Button maileur=(Button)findViewById(R.id.mail);
        maileur.setVisibility(View.INVISIBLE);
        Button web=(Button)findViewById(R.id.backSite);
        web.setVisibility(View.VISIBLE);
        connexionWeb("https://www.google.fr/gmail/");
    }

    public void goOnSite(View view){
        setContentView(R.layout.webnav);
        Button web=(Button)findViewById(R.id.backSite);
        web.setVisibility(View.INVISIBLE);
        Button maileur=(Button)findViewById(R.id.mail);
        maileur.setVisibility(View.VISIBLE);
        connexionWeb("http://51.255.131.194/index.html");

    }

    public void connexionWeb(String url){
        WebView webView=(WebView)findViewById(R.id.navsite);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
    }

}

