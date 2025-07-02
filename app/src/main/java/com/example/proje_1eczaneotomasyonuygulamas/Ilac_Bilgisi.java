package com.example.proje_1eczaneotomasyonuygulamas;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Ilac_Bilgisi extends AppCompatActivity {

    private TableLayout tableLayout;
    private FirebaseDatabase database;
    private DatabaseReference ilaclarRef;
    private SearchView search_ilac;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ilac_bilgisi_activity);
        search_ilac = findViewById(R.id.search_ilac);
        tableLayout = findViewById(R.id.tabla_ilac_2);
        ImageView img_btn_ekle = findViewById(R.id.img_btn_ekle);
        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance();
        ilaclarRef = database.getReference("ilac");

        // Load initial data from Firebase
        loadTableData();


        img_btn_ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ilac_Bilgisi.this, Ilac_Ekle.class);
                startActivity(intent);
                finishActivity(1);
            }
        });
        // SearchView'a TextWatcher ekleme
        search_ilac.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterTable(newText);
                return true;
            }
        });


    }


    private void filterTable(String query) {
        for (int i = 1; i < tableLayout.getChildCount(); i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);

            TextView atcAdi = (TextView) row.getChildAt(0); // atc_adi TextView'i al
            String atcAdiText = atcAdi.getText().toString().toLowerCase(); // Küçük harfe dönüştür

            TextView atcKodu = (TextView) row.getChildAt(1); // atc_kodu TextView'i al
            String atcKoduText = atcKodu.getText().toString().toLowerCase(); // Küçük harfe dönüştür

            TextView barkod = (TextView) row.getChildAt(2); // barkod TextView'i al
            String barkodText = barkod.getText().toString().toLowerCase(); // Küçük harfe dönüştür

            TextView ilacAdi = (TextView) row.getChildAt(3); // ilac_adi TextView'i al
            String ilacAdiText = ilacAdi.getText().toString().toLowerCase(); // Küçük harfe dönüştür

            // Kullanıcı tarafından girilen metni içeren satırları üst sıralara taşı
            if (atcAdiText.contains(query.toLowerCase()) ||
                    atcKoduText.contains(query.toLowerCase()) ||
                    barkodText.contains(query.toLowerCase()) ||
                    ilacAdiText.contains(query.toLowerCase())) {
                tableLayout.removeView(row);
                tableLayout.addView(row, 1); // 0. satırın üstüne ekle (başlık hariç)
            }
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        // Load data whenever the activity is resumed
        loadTableData();
    }
    private void loadTableData() {
        ilaclarRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    // Clear existing rows except the header
                    if (tableLayout.getChildCount() > 1) {
                        tableLayout.removeViews(1, tableLayout.getChildCount() - 1);
                    }
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Ilac ilac = snapshot.getValue(Ilac.class);

                        if (ilac != null ) {
                            TableRow row = new TableRow(Ilac_Bilgisi.this);
                            row.setLayoutParams(new TableRow.LayoutParams(
                                    TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT
                            ));

                            TextView atcAdi = createTextView(ilac.atc_adi);
                            row.addView(atcAdi);

                            TextView atcKodu = createTextView(ilac.atc_kodu);
                            row.addView(atcKodu);

                            TextView barkod = createTextView(ilac.barkod);
                            row.addView(barkod);

                            TextView ilacAdi = createTextView(ilac.ilac_adi);
                            row.addView(ilacAdi);

                            tableLayout.addView(row);
                        }
                    }
                } catch (Exception e) {
                    Log.e("Ilac_Bilgisi", "Veri yükleme hatası", e);
                   // Toast.makeText(Ilac_Bilgisi.this, "Veri yükleme hatası: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Ilac_Bilgisi", "loadTableData:onCancelled", databaseError.toException());
            }
        });
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(Ilac_Bilgisi.this);
        textView.setLayoutParams(new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                1.0f
        ));
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8);
        textView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL); // Metni sola hizala
        textView.setSingleLine(true); // Tek satırda göster
        textView.setEllipsize(TextUtils.TruncateAt.END); // Metni gerektiğinde kes
        return textView;
    }




}