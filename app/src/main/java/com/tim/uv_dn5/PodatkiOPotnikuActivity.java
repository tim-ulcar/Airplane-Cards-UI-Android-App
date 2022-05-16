package com.tim.uv_dn5;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PodatkiOPotnikuActivity extends AppCompatActivity {

    private TextView ime;
    private TextView priimek;
    private RadioButton moski;
    private RadioButton zenski;
    private TextView datumRojstva;
    private RadioButton prvi;
    private RadioButton poslovni;
    private RadioButton ekonomski;
    private RadioButton prtljaga;
    private RadioButton prtljagaNe;
    private RadioButton do20kg;
    private RadioButton do25kg;
    private RadioButton do30kg;
    private TextView tezaPrtljageLabel;
    private RadioGroup tezaPrtljageRadioGroup;
    private TextView cenaKarteIzracun;

    private DatePickerDialog.OnDateSetListener datumRojstvaDialogDateSetListener;

    String lokacijaCiljaIzbira;
    boolean povratniLetIzbira;
    boolean prviIzbira;
    boolean poslovniIzbira;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podatki_opotniku);

        //pridobi gradnike
        ime = findViewById(R.id.ime);
        priimek = findViewById(R.id.priimek);
        moski = findViewById(R.id.moski);
        zenski = findViewById(R.id.zenski);
        datumRojstva = findViewById(R.id.datumRojstva);
        prvi = findViewById(R.id.prvi);
        poslovni = findViewById(R.id.poslovni);
        ekonomski = findViewById(R.id.ekonomski);
        prtljaga = findViewById(R.id.prtljaga);
        prtljagaNe = findViewById(R.id.prtljagaNe);
        do20kg = findViewById(R.id.do20kg);
        do25kg = findViewById(R.id.do25kg);
        do30kg = findViewById(R.id.do30kg);
        tezaPrtljageLabel = findViewById(R.id.tezaPrtljageLabel);
        tezaPrtljageRadioGroup = findViewById(R.id.tezaPrtljageRadioGroup);
        cenaKarteIzracun = findViewById(R.id.cenaKarteIzracun);

        //nastavi dialog za izbiro datuma rojstva
        datumRojstva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = 2000;
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(PodatkiOPotnikuActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, datumRojstvaDialogDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        datumRojstvaDialogDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                String date = dayOfMonth + ". " + month + ". " + year;
                datumRojstva.setText(date);
                preracunajCeno(datumRojstva);
            }
        };

        //naloži poslane podatke
        Intent intent = getIntent();
        povratniLetIzbira = intent.getBooleanExtra("povratniLetIzbira", false);
        lokacijaCiljaIzbira = intent.getStringExtra("lokacijaCiljaIzbira");
        prviIzbira = intent.getBooleanExtra("prvi", true);
        poslovniIzbira = intent.getBooleanExtra("poslovni", false);
        if (prviIzbira) {
            prvi.setChecked(true);
            poslovni.setChecked(false);
            ekonomski.setChecked(false);
        }
        else if (poslovniIzbira) {
            prvi.setChecked(false);
            poslovni.setChecked(true);
            ekonomski.setChecked(false);
        }
        else {
            prvi.setChecked(false);
            poslovni.setChecked(false);
            ekonomski.setChecked(true);
        }

        //če bila karta že urejana, naloži podatke
        if (intent.getBooleanExtra("bilaUrejana", false)) {
            ime.setText(intent.getStringExtra("ime"));
            priimek.setText(intent.getStringExtra("priimek"));
            boolean moskiChecked = intent.getBooleanExtra("moski", true);
            if (moskiChecked) {
                moski.setChecked(true);
                zenski.setChecked(false);
            }
            else {
                moski.setChecked(false);
                zenski.setChecked(true);
            }
            datumRojstva.setText(intent.getStringExtra("datumRojstva"));
            boolean prviChecked = intent.getBooleanExtra("prvi", true);
            boolean poslovniChecked = intent.getBooleanExtra("poslovni", false);
            if (prviChecked) {
                prvi.setChecked(true);
                poslovni.setChecked(false);
                ekonomski.setChecked(false);
            }
            else if (poslovniChecked) {
                prvi.setChecked(false);
                poslovni.setChecked(true);
                ekonomski.setChecked(false);
            }
            else {
                prvi.setChecked(false);
                poslovni.setChecked(false);
                ekonomski.setChecked(true);
            }
            boolean prtljagaChecked = intent.getBooleanExtra("prtljaga", true);
            if (prtljagaChecked) {
                prtljaga.setChecked(true);
                prtljagaNe.setChecked(false);
            }
            else {
                prtljaga.setChecked(false);
                prtljagaNe.setChecked(true);
            }
            boolean do20kgChecked = intent.getBooleanExtra("do20kg", true);
            boolean do25kgChecked = intent.getBooleanExtra("do25kg", false);
            if (do20kgChecked) {
                do20kg.setChecked(true);
                do25kg.setChecked(false);
                do30kg.setChecked(false);
            }
            else if (do25kgChecked) {
                do20kg.setChecked(false);
                do25kg.setChecked(true);
                do30kg.setChecked(false);
            }
            else {
                do20kg.setChecked(false);
                do25kg.setChecked(false);
                do30kg.setChecked(true);
            }
        }
    }

    public void shraniCB(View view) {
        //preveri če vsi vnosi
        boolean napaka = false;
        if (ime.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), "Ime ni vpisano.", Toast.LENGTH_SHORT).show();
            napaka = true;
        }
        if (priimek.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), "Priimek ni vpisan.", Toast.LENGTH_SHORT).show();
            napaka = true;
        }
        if (datumRojstva.getText().toString().equals("Izberi datum")) {
            Toast.makeText(getBaseContext(), "Datum rojstva ni vpisan.", Toast.LENGTH_SHORT).show();
            napaka = true;
        }
        if (napaka) {
            return;
        }

        //pošlji vnose nazaj
        Intent resultIntent = new Intent();
        resultIntent.putExtra("ime", ime.getText().toString());
        resultIntent.putExtra("priimek", priimek.getText().toString());
        resultIntent.putExtra("moski", moski.isChecked());
        resultIntent.putExtra("datumRojstva", datumRojstva.getText().toString());
        resultIntent.putExtra("prvi", prvi.isChecked());
        resultIntent.putExtra("poslovni", poslovni.isChecked());
        resultIntent.putExtra("prtljaga", prtljaga.isChecked());
        resultIntent.putExtra("do20kg", do20kg.isChecked());
        resultIntent.putExtra("do25kg", do25kg.isChecked());

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    public void prekliciCB(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void prtljagaChangeCB(View view) {
        if (!prtljaga.isChecked()) {
            tezaPrtljageLabel.setVisibility(View.INVISIBLE);
            tezaPrtljageRadioGroup.setVisibility(View.INVISIBLE);
        }
        else {
            tezaPrtljageLabel.setVisibility(View.VISIBLE);
            tezaPrtljageRadioGroup.setVisibility(View.VISIBLE);
        }

        preracunajCeno(view);
    }

    public void preracunajCeno(View view) {
        boolean otrokPod2Leti = false;
        boolean otrokPod12let = false;

        //pridobi datum rojstva
        if (!datumRojstva.getText().toString().equals("Izberi datum")) {
            String datumRojstvaString = datumRojstva.getText().toString();
            String[] razbitje = datumRojstvaString.split(". ");
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

            if (diffInDays < 730) {
                otrokPod2Leti = true;
            }
            else if (diffInDays < 4380) {
                otrokPod12let = true;
            }
        }


        String zapis = "Cena karte:\nlet v ";

        int faktor = 1;

        switch (lokacijaCiljaIzbira) {
            case "Ljubljana":
                if (otrokPod2Leti) {
                    faktor = 0;
                    zapis += "Ljubljano - otrok pod 2 leti (0€)\n";
                }
                else if (otrokPod12let) {
                    faktor = 200;
                    zapis += "Ljubljano - otrok pod 12 let (200€)\n";
                }
                else {
                    faktor = 400;
                    zapis += "Ljubljano (400€)\n";
                }
                break;
            case "Berlin":
                if (otrokPod2Leti) {
                    faktor = 0;
                    zapis += "Berlin - otrok pod 2 leti (0€)\n";
                }
                else if (otrokPod12let) {
                    faktor = 50;
                    zapis += "Berlin - otrok pod 12 let (50€)\n";
                }
                else {
                    faktor = 100;
                    zapis += "Berlin (100€)\n";
                }
                break;
            case "Pariz":
                if (otrokPod2Leti) {
                    faktor = 0;
                    zapis += "Pariz - otrok pod 2 leti (0€)\n";
                }
                else if (otrokPod12let) {
                    faktor = 50;
                    zapis += "Pariz - otrok pod 12 let (50€)\n";
                }
                else {
                    faktor = 100;
                    zapis += "Berlin (100€)\n";
                }
                break;
            case "London":
                if (otrokPod2Leti) {
                    faktor = 0;
                    zapis += "London - otrok pod 2 leti (0€)\n";
                }
                else if (otrokPod12let) {
                    faktor = 50;
                    zapis += "London - otrok pod 12 let (50€)\n";
                }
                else {
                    faktor = 100;
                    zapis += "London (100€)\n";
                }
                break;
            case "Rim":
                if (otrokPod2Leti) {
                    faktor = 0;
                    zapis += "Rim - otrok pod 2 leti (0€)\n";
                }
                else if (otrokPod12let) {
                    faktor = 100;
                    zapis += "Rim - otrok pod 12 let (100€)\n";
                }
                else {
                    faktor = 200;
                    zapis += "Rim (200€)\n";
                }
                break;
            case "Madrid":
                if (otrokPod2Leti) {
                    faktor = 0;
                    zapis += "Madrid - otrok pod 2 leti (0€)\n";
                }
                else if (otrokPod12let) {
                    faktor = 100;
                    zapis += "Madrid - otrok pod 12 let (100€)\n";
                }
                else {
                    faktor = 200;
                    zapis += "Madrid (200€)\n";
                }
                break;
            case "Bruselj":
                if (otrokPod2Leti) {
                    faktor = 0;
                    zapis += "Bruselj - otrok pod 2 leti (0€)\n";
                }
                else if (otrokPod12let) {
                    faktor = 100;
                    zapis += "Bruselj - otrok pod 12 let (100€)\n";
                }
                else {
                    faktor = 200;
                    zapis += "Bruselj (200€)\n";
                }
                break;
            case "Moskva":
                if (otrokPod2Leti) {
                    faktor = 0;
                    zapis += "Moskva - otrok pod 2 leti (0€)\n";
                }
                else if (otrokPod12let) {
                    faktor = 150;
                    zapis += "Moskva - otrok pod 12 let (150€)\n";
                }
                else {
                    faktor = 300;
                    zapis += "Moskva (300€)\n";
                }
                break;
            case "Zagreb":
                if (otrokPod2Leti) {
                    faktor = 0;
                    zapis += "Zagreb - otrok pod 2 leti (0€)\n";
                }
                else if (otrokPod12let) {
                    faktor = 150;
                    zapis += "Zagreb - otrok pod 12 let (150€)\n";
                }
                else {
                    faktor = 300;
                    zapis += "Zagreb (300€)\n";
                }
                break;
            case "Beograd":
                if (otrokPod2Leti) {
                    faktor = 0;
                    zapis += "Beograd - otrok pod 2 leti (0€)\n";
                }
                else if (otrokPod12let) {
                    faktor = 150;
                    zapis += "Beograd - otrok pod 12 let (150€)\n";
                }
                else {
                    faktor = 300;
                    zapis += "Beograd (300€)\n";
                }
                break;
            default:
                break;
        }
        int cenaKarte = faktor;


        if (prvi.isChecked()) {
            cenaKarte += 200;
            zapis += "+ prvi razred (200€)\n";
        }
        else if (poslovni.isChecked()) {
            cenaKarte += 100;
            zapis += "+ poslovni razred (100€)\n";
        }

        if (prtljaga.isChecked()) {
            if (do20kg.isChecked()) {
                cenaKarte += 20;
                zapis += "+ prtljaga do 20 kg (20€)\n";
            }
            else if (do25kg.isChecked()) {
                cenaKarte += 40;
                zapis += "+ prtljaga do 25 kg (40€)\n";
            }
            else {
                cenaKarte += 80;
                zapis += "+ prtljaga do 30 kg (80€)\n";
            }
        }

        if (povratniLetIzbira) {
            cenaKarte = cenaKarte * 2;
            zapis += " + povratni let (cena x2)\n";
        }

        zapis += String.format("= %d€", cenaKarte);

        cenaKarteIzracun.setText(zapis);
    }
}
