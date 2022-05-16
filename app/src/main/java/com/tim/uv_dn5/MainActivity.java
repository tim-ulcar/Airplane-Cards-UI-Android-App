package com.tim.uv_dn5;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TextView datumOdhoda;
    private TextView datumVrnitve;
    private TextView datumVrnitveLabel;
    private DatePickerDialog.OnDateSetListener datumOdhodaDialogDateSetListener;
    private DatePickerDialog.OnDateSetListener datumVrnitveDialogDateSetListener;
    private NumberPicker steviloPotnikov;
    private Spinner lokacijaOdhoda;
    private Spinner lokacijaCilja;
    private RadioButton povratniLet;
    private RadioButton enosmerniLet;
    private RadioButton prvi;
    private RadioButton poslovni;
    private RadioButton ekonomski;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //pridobi gradnike
        lokacijaOdhoda = findViewById(R.id.lokacijaOdhoda);
        lokacijaCilja = findViewById(R.id.lokacijaCilja);
        povratniLet = findViewById(R.id.povratniLet);
        enosmerniLet = findViewById(R.id.enosmerniLet);
        prvi = findViewById(R.id.prvi);
        poslovni = findViewById(R.id.poslovni);
        ekonomski = findViewById(R.id.ekonomski);
        datumOdhoda = findViewById(R.id.datumOdhoda);
        datumVrnitve = findViewById(R.id.datumVrnitve);
        datumVrnitveLabel = findViewById(R.id.datumVrnitveLabel);
        steviloPotnikov = findViewById(R.id.steviloPotnikov);


        //nastavi dialog za izbiro datuma odhoda
        datumOdhoda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, datumOdhodaDialogDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        datumOdhodaDialogDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                String date = dayOfMonth + ". " + month + ". " + year;
                datumOdhoda.setText(date);
            }
        };


        //nastavi dialog za izbiro datuma vrnitve
        datumVrnitve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, datumVrnitveDialogDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        datumVrnitveDialogDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                String date = dayOfMonth + ". " + month + ". " + year;
                datumVrnitve.setText(date);
            }
        };


        //nastavi stevilo potnikov možnosti
        steviloPotnikov.setMaxValue(8);
        steviloPotnikov.setMinValue(1);
        steviloPotnikov.setValue(1);

        lokacijaCilja.setSelection(3);
    }


    public void ustvariKarte(View view) {
        //pridobi izbire
        String lokacijaOdhodaIzbira = (String) lokacijaOdhoda.getSelectedItem();
        String lokacijaCiljaIzbira = (String) lokacijaCilja.getSelectedItem();
        String datumOdhodaIzbira = (String) datumOdhoda.getText();
        String datumVrnitveIzbira = (String) datumVrnitve.getText();
        boolean povratniLetIzbira = povratniLet.isChecked();
        boolean prviIzbira = prvi.isChecked();
        boolean poslovniIzbira = poslovni.isChecked();
        int steviloPotnikovIzbira = steviloPotnikov.getValue();

        //preveri vnose
        boolean napaka = false;
        if (lokacijaOdhodaIzbira.equals(lokacijaCiljaIzbira)) {
            Toast.makeText(getBaseContext(), "Lokacija odhoda in cilja ne smeta biti enaki.", Toast.LENGTH_SHORT).show();
            napaka = true;
        }
        if (datumOdhodaIzbira.equals("")) {
            Toast.makeText(getBaseContext(), "Datum odhoda ni izbran.", Toast.LENGTH_SHORT).show();
            napaka = true;
        }
        else if (!datumKasnejeKotDanes(datumOdhodaIzbira)) {
            Toast.makeText(getBaseContext(), "Datum odhoda mora biti kasneje kot danes.", Toast.LENGTH_SHORT).show();
            napaka = true;
        }
        if (povratniLetIzbira) {
            if (datumVrnitveIzbira.equals("")) {
                Toast.makeText(getBaseContext(), "Datum vrnitve ni izbran.", Toast.LENGTH_SHORT).show();
                napaka = true;
            }
            else if (!datumKasnejeKotDanes(datumVrnitveIzbira)) {
                Toast.makeText(getBaseContext(), "Datum vrnitve mora biti kasneje kot danes.", Toast.LENGTH_SHORT).show();
                napaka = true;
            }
            else if (!datumOdhodaIzbira.equals("")) {
                if (!datumKasnejeKotDrugDatum(datumVrnitveIzbira, datumOdhodaIzbira)) {
                    Toast.makeText(getBaseContext(), "Datum vrnitve mora biti kasneje kot datum odhoda.", Toast.LENGTH_SHORT).show();
                    napaka = true;
                }
            }
        }
        if (napaka) {
            return;
        }

        //ustvari intent in sekundarno aktivnost UrejanjeKartActivity
        Intent intent = new Intent(this, UrejanjeKartActivity.class);
        intent.putExtra("lokacijaOdhodaIzbira", lokacijaOdhodaIzbira);
        intent.putExtra("lokacijaCiljaIzbira", lokacijaCiljaIzbira);
        intent.putExtra("datumOdhodaIzbira", datumOdhodaIzbira);
        intent.putExtra("datumVrnitveIzbira", datumVrnitveIzbira);
        intent.putExtra("povratniLetIzbira", povratniLetIzbira);
        intent.putExtra("prviIzbira", prviIzbira);
        intent.putExtra("poslovniIzbira", poslovniIzbira);
        intent.putExtra("steviloPotnikovIzbira", steviloPotnikovIzbira);

        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                pobrisiVse(findViewById(R.id.pobrisiVse));
            }
        }
    }


    public boolean datumKasnejeKotDanes(String datum) {
        String[] razbitje = datum.split(". ");
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
        String monthToday = Integer.toString(cal.get(Calendar.MONTH) + 1);
        if (monthToday.length() == 1) {
            monthToday = "0" + monthToday;
        }
        String dayToday = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        if (dayToday.length() == 1) {
            dayToday = "0" + dayToday;
        }

        //izračunaj razliko v dnevih
        SimpleDateFormat myFormat = new SimpleDateFormat("MM dd yyyy");
        String inputString1 = String.format("%s %s %s", month, day, year);
        String inputString2 = String.format("%s %s %s", monthToday, dayToday, yearToday);

        try {
            Date date1 = myFormat.parse(inputString1);
            Date date2 = myFormat.parse(inputString2);
            if (date1.after(date2)) {
                return true;
            }
            else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }


    public boolean datumKasnejeKotDrugDatum(String datum1, String datum2) {
        String[] razbitje = datum1.split(". ");
        String day = razbitje[0];
        if (day.length() == 1) {
            day = "0" + day;
        }
        String month = razbitje[1];
        if (month.length() == 1) {
            month = "0" + month;
        }
        String year = razbitje[2];


        String[] razbitje2 = datum2.split(". ");
        String day2 = razbitje2[0];
        if (day2.length() == 1) {
            day2 = "0" + day;
        }
        String month2 = razbitje2[1];
        if (month2.length() == 1) {
            month2 = "0" + month2;
        }
        String year2 = razbitje2[2];


        //izračunaj razliko v dnevih
        SimpleDateFormat myFormat = new SimpleDateFormat("MM dd yyyy");
        String inputString1 = String.format("%s %s %s", month, day, year);
        String inputString2 = String.format("%s %s %s", month2, day2, year2);

        try {
            Date date1 = myFormat.parse(inputString1);
            Date date2 = myFormat.parse(inputString2);
            if (date1.after(date2)) {
                return true;
            }
            else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }


    public void povratniLetChangeCB(View view) {
        if (enosmerniLet.isChecked()) {
            datumVrnitve.setVisibility(View.INVISIBLE);
            datumVrnitveLabel.setVisibility(View.INVISIBLE);
        }
        else {
            datumVrnitve.setVisibility(View.VISIBLE);
            datumVrnitveLabel.setVisibility(View.VISIBLE);
        }
    }

    public void pobrisiVse(View view) {
        lokacijaOdhoda.setSelection(0);
        lokacijaCilja.setSelection(3);
        datumOdhoda.setText("");
        datumOdhoda.setHint("Izberi datum");
        datumVrnitve.setText("");
        datumVrnitve.setHint("Izberi datum");
        povratniLet.setChecked(true);
        enosmerniLet.setChecked(false);
        prvi.setChecked(true);
        poslovni.setChecked(false);
        ekonomski.setChecked(false);
        steviloPotnikov.setValue(1);

        datumVrnitve.setVisibility(View.VISIBLE);
        datumVrnitveLabel.setVisibility(View.VISIBLE);
    }
}
