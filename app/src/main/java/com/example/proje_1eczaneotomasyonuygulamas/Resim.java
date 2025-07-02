package com.example.proje_1eczaneotomasyonuygulamas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Resim extends AppCompatActivity implements CustomImageView.OnCoordinateTouchListener {

    private CustomImageView customImageView1;
    private CustomImageView customImageView2;

    private Map<String, float[]> coordinatesMap1;
    private Map<String, float[]> coordinatesMap2;
    private TextView lbl_resim_yazi;

    private DatabaseReference ilacRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resim_activity);

        lbl_resim_yazi = findViewById(R.id.lbl_resim_yazi);
        customImageView1 = findViewById(R.id.customImageView);
        customImageView2 = findViewById(R.id.customImageView2);

        customImageView1.setOnCoordinateTouchListener(this);
        customImageView2.setOnCoordinateTouchListener(this);

        coordinatesMap1 = new HashMap<>();
        coordinatesMap2 = new HashMap<>();

        ilacRef = FirebaseDatabase.getInstance().getReference().child("ilac");

        // Ilac_2 listesini al
        List<Ilac_2> ilacList = getIntent().getParcelableArrayListExtra("ilac_listesi");
        if (ilacList != null && !ilacList.isEmpty()) {
            for (Ilac_2 ilac : ilacList) {
                String barkod = ilac.getBarkod();
                fetchCoordinatesFromFirebase(barkod);
            }
        } else {
            Toast.makeText(this, "Seçilen ilaç bulunamadı.", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchCoordinatesFromFirebase(String barkod) {
        Query query = ilacRef.orderByChild("barkod").equalTo(barkod);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Ilac ilac = snapshot.getValue(Ilac.class);
                        if (ilac != null && ilac.koordinatlar != null && ilac.koordinatlar.resim1 != null) {
                            float[] coords1 = {ilac.koordinatlar.resim1.x, ilac.koordinatlar.resim1.y};
                            coordinatesMap1.put(ilac.barkod, coords1);
                            customImageView1.setCoordinates(new ArrayList<>(coordinatesMap1.values()));
                        }
                        if (ilac != null && ilac.koordinatlar != null && ilac.koordinatlar.resim2 != null) {
                            float[] coords2 = {ilac.koordinatlar.resim2.x, ilac.koordinatlar.resim2.y};
                            coordinatesMap2.put(ilac.barkod, coords2);
                            customImageView2.setCoordinates(new ArrayList<>(coordinatesMap2.values()));
                        }
                    }
                } else {
                    Toast.makeText(Resim.this, "Barkoda uygun ilaç bulunamadı.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Resim.this, "Firebase'den ilaç bilgileri alınamadı: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCoordinateTouch(float x, float y) {
        lbl_resim_yazi.setText("X: " + x + ", Y: " + y);
    }
}
