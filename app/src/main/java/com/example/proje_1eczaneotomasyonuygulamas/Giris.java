package com.example.proje_1eczaneotomasyonuygulamas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class Giris extends AppCompatActivity {

    private static final int REQUEST_CODE_QR_SCAN = 101; // Sabit bir kod

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.giris_activity);

        Button btn_recete = findViewById(R.id.btn_recete);
        Button btn_ilac = findViewById(R.id.btn_ilac);
        TextView lbl_hosgeldin = findViewById(R.id.lbl_hosgeldin);

        btn_recete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Giris.this, recete_2.class);
                startActivity(intent);
            }
        });

        btn_ilac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Giris.this, Ilac_Bilgisi.class);
                startActivity(intent);
            }
        });
    }
}
