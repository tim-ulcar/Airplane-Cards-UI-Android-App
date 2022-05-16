package com.tim.uv_dn5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class PlaciloActivity extends AppCompatActivity {
    TextView koncnaCenaTextView;
    TextView ime;
    TextView stevilkaKartice;
    TextView ccv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placilo);

        koncnaCenaTextView = findViewById(R.id.koncnaCena);
        ime = findViewById(R.id.ime);
        stevilkaKartice = findViewById(R.id.stevilkaKartice);
        ccv = findViewById(R.id.ccv);

        Intent intent = getIntent();
        int cena = intent.getIntExtra("cena", 0);

        koncnaCenaTextView.setText(String.format("Končna cena: %d €", cena));
    }

    public void placajCB(View view) {
        //preveri če vsi vnosi pravilni
        boolean napaka = false;
        if (ime.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), "Ime ni vpisano.", Toast.LENGTH_SHORT).show();
            napaka = true;
        }
        if (stevilkaKartice.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), "Številka kartice ni vpisana.", Toast.LENGTH_SHORT).show();
            napaka = true;
        }
        if (ccv.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), "CCV ni vpisan.", Toast.LENGTH_SHORT).show();
            napaka = true;
        }
        if (napaka) {
            return;
        }

        Intent intent = new Intent(this, KoncniActivity.class);
        startActivity(intent);
        setResult(RESULT_OK);
        finish();
    }

    public void prekliciCB(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}
