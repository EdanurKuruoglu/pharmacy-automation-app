package com.example.proje_1eczaneotomasyonuygulamas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ilac_yeri_ekle extends AppCompatActivity {
    private static final String TAG = "ilac_yeri_ekle";
    private DatabaseReference databaseReference;

    private CustomImageView customImageView1;
    private CustomImageView customImageView2;
    private TextView txtSelectedCoordinates;

    private List<float[]> coordinates1;
    private List<float[]> coordinates2;
    private String atc_adi, atc_kodu, barkod, ilac_adi;
    private boolean isIlacFound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilac_yeri_ekle);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("ilac");

        customImageView1 = findViewById(R.id.customImageView_ilac);
        customImageView2 = findViewById(R.id.customImageView2_ilac);
        txtSelectedCoordinates = findViewById(R.id.lbl_resim_yazi_2);
        Button btnSaveCoordinates = findViewById(R.id.button_ilc_yer_ekle);

        coordinates1 = new ArrayList<>();
        coordinates2 = new ArrayList<>();

        // Intent'ten gelen verileri al
        atc_adi = getIntent().getStringExtra("atc_adi");
        atc_kodu = getIntent().getStringExtra("atc_kodu");
        barkod = getIntent().getStringExtra("barkod");
        ilac_adi = getIntent().getStringExtra("ilac_adi");

        Log.d(TAG, "Received barkod: " + barkod);

        DatabaseReference ilacRef = FirebaseDatabase.getInstance().getReference("ilac");
        Query query = ilacRef.orderByChild("barkod").equalTo(barkod);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    isIlacFound = true;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Ilac currentIlac = dataSnapshot.getValue(Ilac.class);
                        if (currentIlac != null) {
                            // İlaç Firebase'den alındı, koordinatları güncelle
                            updateCoordinates(currentIlac);
                        }
                    }
                } else {
                    Log.d(TAG, "Barkoda uygun ilaç bulunamadı.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ilac_yeri_ekle.this, "Firebase'den ilaç bilgileri alınamadı: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Firebase'den ilaç bilgileri alınamadı: " + error.getMessage());
            }
        });

        customImageView1.setOnCoordinateTouchListener(new CustomImageView.OnCoordinateTouchListener() {
            @Override
            public void onCoordinateTouch(float x, float y) {
                float[] coord = {x, y};
                coordinates1.clear();
                coordinates1.add(coord);
                customImageView1.setCoordinates(coordinates1);

                // İkinci resimdeki koordinatları temizle
                coordinates2.clear();
                customImageView2.setCoordinates(coordinates2);

                updateCoordinatesText();
            }
        });

        customImageView2.setOnCoordinateTouchListener(new CustomImageView.OnCoordinateTouchListener() {
            @Override
            public void onCoordinateTouch(float x, float y) {
                float[] coord = {x, y};
                coordinates2.clear();
                coordinates2.add(coord);
                customImageView2.setCoordinates(coordinates2);

                // İlk resimdeki koordinatları temizle
                coordinates1.clear();
                customImageView1.setCoordinates(coordinates1);

                updateCoordinatesText();
            }
        });

        btnSaveCoordinates.setOnClickListener(v -> {
            if (coordinates1.size() >= 1 || coordinates2.size() >= 1) {
                saveCoordinatesToFirebase();
            } else {
                Toast.makeText(ilac_yeri_ekle.this, "Resim için en az bir koordinat belirleyin.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCoordinates(Ilac ilac) {
        if (ilac.koordinatlar != null) {
            if (ilac.koordinatlar.resim1 != null) {
                coordinates1.clear();
                float[] coord1 = {ilac.koordinatlar.resim1.x, ilac.koordinatlar.resim1.y};
                coordinates1.add(coord1);
                customImageView1.setCoordinates(coordinates1);
            }
            if (ilac.koordinatlar.resim2 != null) {
                coordinates2.clear();
                float[] coord2 = {ilac.koordinatlar.resim2.x, ilac.koordinatlar.resim2.y};
                coordinates2.add(coord2);
                customImageView2.setCoordinates(coordinates2);
            }
            updateCoordinatesText();
        }
    }

    private void updateCoordinatesText() {
        StringBuilder sb = new StringBuilder();
        if (!coordinates1.isEmpty()) {
            float[] c1 = coordinates1.get(0);
            sb.append("Resim 1 - X: ").append(c1[0]).append(", Y: ").append(c1[1]).append("\n");
        }
        if (!coordinates2.isEmpty()) {
            float[] c2 = coordinates2.get(0);
            sb.append("Resim 2 - X: ").append(c2[0]).append(", Y: ").append(c2[1]).append("\n");
        }
        txtSelectedCoordinates.setText(sb.toString());
    }

    private void saveCoordinatesToFirebase() {

        Ilac ilac = new Ilac(atc_adi, atc_kodu, barkod, ilac_adi);
        if (ilac.koordinatlar == null) {
            ilac.koordinatlar = new Ilac.Koordinatlar();
        }
        if (!coordinates1.isEmpty()) {
            ilac.koordinatlar.resim1 = new Ilac.ResimKoordinat(coordinates1.get(0)[0], coordinates1.get(0)[1]);
        }
        if (!coordinates2.isEmpty()) {
            ilac.koordinatlar.resim2 = new Ilac.ResimKoordinat(coordinates2.get(0)[0], coordinates2.get(0)[1]);
        }

        // Firebase veritabanına ilacı kaydet
        databaseReference.child(barkod).setValue(ilac)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ilac_yeri_ekle.this, "Koordinatlar başarıyla kaydedildi!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ilac_yeri_ekle.this, Ilac_Bilgisi.class);
                        startActivity(intent);
                        finishActivity(1);
                    } else {
                        Toast.makeText(ilac_yeri_ekle.this, "Koordinatlar kaydedilirken bir hata oluştu: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
