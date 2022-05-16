package com.tim.uv_dn5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class UrejanjeKartActivity extends AppCompatActivity {
    String lokacijaOdhodaIzbira;
    String lokacijaCiljaIzbira;
    String datumOdhodaIzbira;
    String datumVrnitveIzbira;
    boolean povratniLetIzbira;
    boolean prviIzbira;
    boolean poslovniIzbira;
    int steviloPotnikovIzbira;

    TextView karta1;
    TextView karta2;
    TextView karta3;
    TextView karta4;
    TextView karta5;
    TextView karta6;
    TextView karta7;
    TextView karta8;
    Button uredi1;
    Button uredi2;
    Button uredi3;
    Button uredi4;
    Button uredi5;
    Button uredi6;
    Button uredi7;
    Button uredi8;
    Button izbrisi1;
    Button izbrisi2;
    Button izbrisi3;
    Button izbrisi4;
    Button izbrisi5;
    Button izbrisi6;
    Button izbrisi7;
    Button izbrisi8;

    ArrayList<TextView> karteTextViews = new ArrayList<>();
    ArrayList<Button> urediButtons = new ArrayList<>();
    ArrayList<Button> izbrisiButtons = new ArrayList<>();

    int REQUEST = 1;
    ArrayList<Karta> karte = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urejanje_kart);

        //preberi posredovane podatke
        Intent intent = getIntent();
        lokacijaOdhodaIzbira = intent.getStringExtra("lokacijaOdhodaIzbira");
        lokacijaCiljaIzbira = intent.getStringExtra("lokacijaCiljaIzbira");
        datumOdhodaIzbira = intent.getStringExtra("datumOdhodaIzbira");
        datumVrnitveIzbira = intent.getStringExtra("datumVrnitveIzbira");
        povratniLetIzbira = intent.getBooleanExtra("povratniLetIzbira", false);
        prviIzbira = intent.getBooleanExtra("prviIzbira", false);
        poslovniIzbira = intent.getBooleanExtra("poslovniIzbira", false);
        steviloPotnikovIzbira = intent.getIntExtra("steviloPotnikovIzbira", 0);

        //poišči gradnike
        karta1 = findViewById(R.id.karta1);
        karta2 = findViewById(R.id.karta2);
        karta3 = findViewById(R.id.karta3);
        karta4 = findViewById(R.id.karta4);
        karta5 = findViewById(R.id.karta5);
        karta6 = findViewById(R.id.karta6);
        karta7 = findViewById(R.id.karta7);
        karta8 = findViewById(R.id.karta8);
        uredi1 = findViewById(R.id.uredi1);
        uredi2 = findViewById(R.id.uredi2);
        uredi3 = findViewById(R.id.uredi3);
        uredi4 = findViewById(R.id.uredi4);
        uredi5 = findViewById(R.id.uredi5);
        uredi6 = findViewById(R.id.uredi6);
        uredi7 = findViewById(R.id.uredi7);
        uredi8 = findViewById(R.id.uredi8);
        izbrisi1 = findViewById(R.id.izbrisi1);
        izbrisi2 = findViewById(R.id.izbrisi2);
        izbrisi3 = findViewById(R.id.izbrisi3);
        izbrisi4 = findViewById(R.id.izbrisi4);
        izbrisi5 = findViewById(R.id.izbrisi5);
        izbrisi6 = findViewById(R.id.izbrisi6);
        izbrisi7 = findViewById(R.id.izbrisi7);
        izbrisi8 = findViewById(R.id.izbrisi8);

        //dodaj gradnike v sezname
        karteTextViews.addAll(Arrays.asList(karta1, karta2, karta3, karta4, karta5, karta6, karta7, karta8));
        urediButtons.addAll(Arrays.asList(uredi1, uredi2, uredi3, uredi4, uredi5, uredi6, uredi7, uredi8));
        izbrisiButtons.addAll(Arrays.asList(izbrisi1, izbrisi2, izbrisi3, izbrisi4, izbrisi5, izbrisi6, izbrisi7, izbrisi8));

        //prikaži ustrezno število vrstic
        for (int i = 1; i <= 8; i++) {
            if (i > steviloPotnikovIzbira) {
                int indeks = i - 1;
                karteTextViews.get(indeks).setVisibility(View.INVISIBLE);
                urediButtons.get(indeks).setVisibility(View.INVISIBLE);
                izbrisiButtons.get(indeks).setVisibility(View.INVISIBLE);
            }
        }

        //naredi ustrezno število kart
        for (int i = 0; i < steviloPotnikovIzbira; i++) {
            Karta karta = new Karta();
            karte.add(karta);
        }
    }


    public void urediCB(View view) {
        //ustvari intent in naslednjo aktivnost PodatkiOPotnikuActivity
        //requestCode je enak indeksu v seznamu karte, ki ga spreminjamo
        int requestCode = pridobiIndeksPritisnjenegaGumba(view);
        Intent intent = new Intent(this, PodatkiOPotnikuActivity.class);
        //če karta že bila urejana, naloži podatke nazaj
        if (karte.get(requestCode).ime != null) {
            Karta karta = karte.get(requestCode);
            intent.putExtra("bilaUrejana", true);
            intent.putExtra("ime", karta.ime);
            intent.putExtra("priimek", karta.priimek);
            intent.putExtra("moski", karta.moski);
            intent.putExtra("datumRojstva", karta.datumRojstva);
            intent.putExtra("prvi", karta.prvi);
            intent.putExtra("poslovni", karta.poslovni);
            intent.putExtra("prtljaga", karta.prtljaga);
            intent.putExtra("do20kg", karta.do20kg);
            intent.putExtra("do25kg", karta.do25kg);
        }
        else {
            intent.putExtra("bilaUrejana", false);
            intent.putExtra("prvi", prviIzbira);
            intent.putExtra("poslovni", poslovniIzbira);
        }
        intent.putExtra("povratniLetIzbira", povratniLetIzbira);
        intent.putExtra("lokacijaCiljaIzbira", lokacijaCiljaIzbira);

        startActivityForResult(intent, requestCode);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //requesti za karte v PodatkiOPotnikuActivity
        if (requestCode < 99) {
            if (resultCode == RESULT_OK) {
                Karta karta = karte.get(requestCode);
                karta.ime = data.getStringExtra("ime");
                karta.priimek = data.getStringExtra("priimek");
                karta.moski = data.getBooleanExtra("moski", false);
                karta.datumRojstva = data.getStringExtra("datumRojstva");
                karta.prvi = data.getBooleanExtra("prvi", false);
                karta.poslovni = data.getBooleanExtra("poslovni", false);
                karta.prtljaga = data.getBooleanExtra("prtljaga", false);
                karta.do20kg = data.getBooleanExtra("do20kg", false);
                karta.do25kg = data.getBooleanExtra("do25kg", false);

                TextView kartaTextView = karteTextViews.get(requestCode);
                kartaTextView.setText(String.format("Karta %d (%s %s)", requestCode + 1, karta.ime, karta.priimek));
            }
        }
        //request za plačilo v PlaciloActivity
        else if (requestCode == 99) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }


    public void izbrisiCB(View view) {
        //pridobi indeks gumba, ki je bil pritisnjen
        int indeks = pridobiIndeksPritisnjenegaGumba(view);
        //izbriši karto in ustrezno vrstico
        karte.remove(indeks);
        steviloPotnikovIzbira--;
        for (int i = indeks; i < steviloPotnikovIzbira; i++) {
            TextView kartaTextView = karteTextViews.get(i);
            if (karte.get(i).ime == null) {
                kartaTextView.setText(String.format("Karta %d (neizpolnjeno)", i + 1));
            }
            else {
                kartaTextView.setText(String.format("Karta %d (%s %s)", i + 1, karte.get(i).ime, karte.get(i).priimek));
            }
        }
        karteTextViews.get(steviloPotnikovIzbira).setVisibility(View.INVISIBLE);
        urediButtons.get(steviloPotnikovIzbira).setVisibility(View.INVISIBLE);
        izbrisiButtons.get(steviloPotnikovIzbira).setVisibility(View.INVISIBLE);
    }


    //indeksi od 0 dalje
    public int pridobiIndeksPritisnjenegaGumba(View view) {
        int id = view.getId();
        String name = getResources().getResourceEntryName(id);
        String lastChar = name.substring(name.length() - 1);
        int number = Integer.parseInt(lastChar);
        int indeks = number - 1;
        return indeks;
    }


    public void dodajKartoCB(View view) {
        if (steviloPotnikovIzbira >= 8) {
            Toast.makeText(getBaseContext(), "Maksimalno število kart že dodano.", Toast.LENGTH_SHORT).show();
            return;
        }
        steviloPotnikovIzbira++;
        karte.add(new Karta());
        karteTextViews.get(steviloPotnikovIzbira - 1).setText(String.format("Karta %d (neizpolnjeno)", steviloPotnikovIzbira));
        karteTextViews.get(steviloPotnikovIzbira - 1).setVisibility(View.VISIBLE);
        urediButtons.get(steviloPotnikovIzbira - 1).setVisibility(View.VISIBLE);
        izbrisiButtons.get(steviloPotnikovIzbira - 1).setVisibility(View.VISIBLE);
    }


    public void placajCB(View view) {
        //preveri če vsi vnosi pravilni
        String errorMessage = "Manjkajo podatki v kartah: ";
        boolean error = false;
        for (int i = 0; i < karte.size(); i++) {
            Karta karta = karte.get(i);
            boolean napaka = false;
            if (karta.ime == null) {
                napaka = true;
            }
            if (karta.priimek == null) {
                napaka = true;
            }
            if (karta.datumRojstva == null) {
                napaka = true;
            }
            if (napaka) {
                error = true;
                errorMessage = errorMessage + (i + 1) + ", ";
            }
        }
        if (error) {
            errorMessage = errorMessage.substring(0, errorMessage.length() - 2);
            Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_SHORT).show();
            return;
        }

        if (karte.size() == 0) {
            Toast.makeText(getBaseContext(), "Dodana ni nobena karta.", Toast.LENGTH_SHORT).show();
            return;
        }



        //izračunaj ceno kart
        int cena = 0;
        int faktor = 1;

        switch (lokacijaCiljaIzbira) {
            case "Ljubljana":
                faktor = 400;
                break;
            case "Berlin":
                faktor = 100;
                break;
            case "Pariz":
                faktor = 100;
                break;
            case "London":
                faktor = 100;
                break;
            case "Rim":
                faktor = 200;
                break;
            case "Madrid":
                faktor = 200;
                break;
            case "Bruselj":
                faktor = 200;
                break;
            case "Moskva":
                faktor = 300;
                break;
            case "Zagreb":
                faktor = 300;
                break;
            case "Beograd":
                faktor = 300;
                break;
            default:
                break;
        }

        //izračunaj ceno vsake karte in te cene seštej v končno ceno
        for (int i = 0; i < karte.size(); i++) {
            //pridobi datum rojstva
            Karta karta = karte.get(i);
            String datumRojstva = karta.datumRojstva;
            String[] razbitje = datumRojstva.split(". ");
            String day = razbitje[0];
            if (day.length() == 1) {
                day = "0" + day;
            }
            String month = razbitje[1];
            if (month.length() == 1) {
                month = "0" + month;
            }
            String year = razbitje[2];

            //pridobi današnji datum
            Calendar cal = Calendar.getInstance();
            String yearToday = Integer.toString(cal.get(Calendar.YEAR));
            String monthToday = Integer.toString(cal.get(Calendar.MONTH));
            if (monthToday.length() == 1) {
                monthToday = "0" + monthToday;
            }
            String dayToday = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
            if (dayToday.length() == 1) {
                dayToday = "0" + dayToday;
            }

            //izračunaj razliko v dnevih
            SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
            String inputString1 = String.format("%s %s %s", day, month, year);
            String inputString2 = String.format("%s %s %s", dayToday, monthToday, yearToday);

            long diffInDays = 0;
            try {
                Date date1 = myFormat.parse(inputString1);
                Date date2 = myFormat.parse(inputString2);
                long diff = date2.getTime() - date1.getTime();
                diffInDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int cenaKarte = faktor;
            if (diffInDays < 730) {
                cenaKarte = 0;
            }
            else if (diffInDays < 4380) {
                cenaKarte = faktor / 2;
            }

            if (karta.prvi) {
                cenaKarte += 200;
            }
            else if (karta.poslovni) {
                cenaKarte += 100;
            }

            if (karta.prtljaga) {
                if (karta.do20kg) {
                    cenaKarte += 20;
                }
                else if (karta.do25kg) {
                    cenaKarte += 40;
                }
                else {
                    cenaKarte += 80;
                }
            }

            if (povratniLetIzbira) {
                cenaKarte = cenaKarte * 2;
            }

            cena += cenaKarte;
        }

        //naredi intent za KoncniActivity in vanj pošlji končno ceno
        Intent intent = new Intent(this, PlaciloActivity.class);
        intent.putExtra("cena", cena);

        startActivityForResult(intent, 99);
    }

    public void nazajCB(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}


class Karta {
    String ime;
    String priimek;
    Boolean moski;
    String datumRojstva;
    Boolean prvi;
    Boolean poslovni;
    Boolean prtljaga;
    Boolean do20kg;
    Boolean do25kg;

    Karta() {}
}
