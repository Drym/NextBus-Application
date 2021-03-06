package g.nextbus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

import fr.rolandl.blog_gps.R;

/**
 * Created by jonathan on 14/01/16.
 */

public class InformationActivity extends Activity implements Runnable {

    private String numeroBus;
    private String place;
    private String distanceTempsFinal;
    private String distbusarret;
    private String vitesse;

    public boolean boucle = true;

    private String minutesA;
    private String secondesA;
    private String minutesB;
    private String secondesB;

    //initialisation des champs de texte
    private TextView placerestantes = null;

    private TextView minutesArretproche = null;
    private TextView secondesArretproche = null;

    private TextView minutesArretDest = null;
    private TextView secondesArretDest = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information);

        placerestantes = (TextView) findViewById(R.id.textView11);

        minutesArretproche = (TextView) findViewById(R.id.textView13);
        secondesArretproche = (TextView) findViewById(R.id.textView18);

        minutesArretDest = (TextView) findViewById(R.id.textView16);
        secondesArretDest = (TextView) findViewById(R.id.textView20);

        Bundle bundle = getIntent().getExtras();

        //recuperation des bundles
        int numeroLigne = bundle.getInt("NUMLINE");
        String arretProcheRecup = bundle.getString("ARRET");
        String arretDestiRecup = bundle.getString("ARRETDEST");
        numeroBus = bundle.getString("NUMBUS");

        //extraction de JSON
        try {
            Bundle bundle2 = getIntent().getExtras();
            String infoTempsReel = bundle2.getString("TEMPSREEL");
            JSONObject infos = new JSONObject(infoTempsReel);
            infos = (JSONObject) infos.get(numeroBus);
            infos = (JSONObject) infos.get("BUS");
            vitesse = infos.getString("Vitesse");
            distbusarret = infos.getString("DistanceBusArret");

        }
        catch(Exception e){
            e.printStackTrace();
        }

        //ajout de l'arret le plus proche
        TextView t1 = (TextView) findViewById(R.id.textView2);
        t1.setText(arretProcheRecup);

        //ajout de l'arret dans la destination
        TextView t2 = (TextView) findViewById(R.id.textView7);
        t2.setText(arretProcheRecup);

        //ajout de l'arrete de dest dans la destination
        TextView t3 = (TextView) findViewById(R.id.textView9);
        t3.setText(arretDestiRecup);

        //ajout de la ligne de bus
        TextView t4 = (TextView) findViewById(R.id.textView5);
        t4.setText("Ligne 1");

        //ajout du numero de bus
        TextView t5 = (TextView) findViewById(R.id.textView4);
        t5.setText(numeroBus);

        //lancement du thread
        Thread h1;
        h1=new Thread(this);
        h1.start();

    }

    @Override
    public void run() {

        while(boucle) {


            Bundle bundle = getIntent().getExtras();

            String infoTempsReel = bundle.getString("TEMPSREEL");



            try {



                JSONObject infos = new JSONObject(infoTempsReel);

                infos = (JSONObject) infos.get(numeroBus);
                infos = (JSONObject) infos.get("BUS");

                place = infos.getString("PlacesRestantes");
                vitesse = infos.getString("Vitesse");
                distbusarret = infos.getString("DistanceBusArret");
                distanceTempsFinal = infos.getString("DistanceBusArretArrive");

                double vites = Double.parseDouble(vitesse);
                double dist = Double.parseDouble(distbusarret);
                double distFinal = Double.parseDouble(distanceTempsFinal);

                //temps arrivée bus a l'arret le plus proche
                double temp = (dist / vites)*3.6;
                temp = Math.round(temp);
                int arround = (int)temp;


                //temps arrivée bus a l'arret de destination
                double temp2 = (distFinal / vites)*3.6;
                temp2 = Math.round(temp2);
                int arround2 = (int)temp2;


                //convertion minutes secondes arret le plus proche
                minutesA = ""+arround/60;
                secondesA = ""+arround%60;

                //convertion minutes secondes arret de destination
                minutesB = ""+arround2/60;
                secondesB = ""+arround2%60;

                if (place!=null || minutesA!=null || secondesA!=null || minutesB!=null || secondesB!=null) {

                    placerestantes.setText(place);

                    minutesArretproche.setText(minutesA);
                    secondesArretproche.setText(secondesA);
                    minutesArretDest.setText(minutesB);
                    secondesArretDest.setText(secondesB);
                }
                Thread.sleep(1000);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onBackPressed(){
        boucle = false;
        super.onBackPressed();
    }


    //}



}