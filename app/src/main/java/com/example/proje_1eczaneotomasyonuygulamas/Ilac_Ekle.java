package com.example.proje_1eczaneotomasyonuygulamas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Ilac_Ekle extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ilac_ekle_activity);

        EditText txt_atc_adi = findViewById(R.id.txt_atc_adi);
        EditText txt_atc_kodu = findViewById(R.id.txt_atc_kodu);
        EditText txt_barkod = findViewById(R.id.txt_barkod);
        EditText txt_ilac_adi = findViewById(R.id.txt_ilac_adi);
        Button btn_ekle = findViewById(R.id.btn_ekle_ilac_2);

        btn_ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EditText'lerden değerleri al
                String atc_adi = txt_atc_adi.getText().toString().trim();
                String atc_kodu = txt_atc_kodu.getText().toString().trim();
                String barkod = txt_barkod.getText().toString().trim();
                String ilac_adi = txt_ilac_adi.getText().toString().trim();

                // Kontroller
                if (atc_adi.isEmpty() || atc_kodu.isEmpty() || barkod.isEmpty() || ilac_adi.isEmpty()) {
                    Toast.makeText(Ilac_Ekle.this, "Tüm alanlar doldurulmalıdır.", Toast.LENGTH_SHORT).show();
                } else if (!barkod.matches("\\d{13}")) {
                    Toast.makeText(Ilac_Ekle.this, "Barkod 13 karakterli bir sayı olmalıdır.", Toast.LENGTH_SHORT).show();
                } else {
                    // Tüm kontroller başarılıysa, intent oluştur ve ilac_yeri_ekle aktivitesine geçiş yap
                    Intent intent = new Intent(Ilac_Ekle.this, ilac_yeri_ekle.class);
                    intent.putExtra("atc_adi", atc_adi);
                    intent.putExtra("atc_kodu", atc_kodu);
                    intent.putExtra("barkod", barkod);
                    intent.putExtra("ilac_adi", ilac_adi);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
