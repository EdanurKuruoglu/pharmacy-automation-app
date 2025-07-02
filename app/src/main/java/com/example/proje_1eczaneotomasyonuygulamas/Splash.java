package com.example.proje_1eczaneotomasyonuygulamas;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.os.CountDownTimer;
import android.content.Intent;
import android.media.MediaPlayer;


public class Splash extends AppCompatActivity {
    private VeriTabaniIslemleri veriTabani;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        veriTabani = new VeriTabaniIslemleri();

        Button btn_kaydet_veri = findViewById(R.id.btn_kaydet_veri);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        ImageView img_logo_splash =findViewById(R.id.img_logo_splash);

        btn_kaydet_veri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VeriTabaniIslemleri veriTabani = new VeriTabaniIslemleri();
                veriTabani.ilacKaydet("Parol", "12345", "123456789", "Aspirin");
            }
        });

        // CountDownTimer kullanarak 5 saniye bekleyip login ekranına geçiş
        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Her saniyede bir biraz süre varsa progress bar güncellenir.
                int progress = (int) (millisUntilFinished / 1000);
                progressBar.setProgress(progress);
            }

            public void onFinish() {
                // Geri sayım tamamlandığında LoginActivity'e geçiş yapılır.
                Intent intent = new Intent(Splash.this, Giris.class);
                startActivity(intent);
                finish();
            }
        }.start();

    }

}