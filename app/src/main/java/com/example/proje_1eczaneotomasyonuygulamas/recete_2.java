package com.example.proje_1eczaneotomasyonuygulamas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class recete_2 extends AppCompatActivity {
    private TableLayout tableLayout2;
    private FirebaseDatabase database;
    private DatabaseReference ilaclarRef;
    private Button btnIlacEkle, btnIleri;
    private RecyclerView recyclerView;
    private IlacAdapter ilacAdapter;
    private List<Ilac_2> ilacList;
    private String selectedIlacAdi;
    private String selectedBarkod;
    private TableRow lastSelectedRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recete_2);

        btnIlacEkle = findViewById(R.id.btn_ekle_ilac_2);
        btnIleri = findViewById(R.id.btn_ileri_2);
        recyclerView = findViewById(R.id.recyclerView);
        tableLayout2 = findViewById(R.id.tabla_ilac_2);

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance();
        ilaclarRef = database.getReference("ilac");

        // Load initial data from Firebase
        loadTableData();
        ilacList = new ArrayList<>();
        ilacAdapter = new IlacAdapter(ilacList);
        recyclerView.setAdapter(ilacAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnIleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ilacAdapter.getItemCount() == 0) {
                    Toast.makeText(recete_2.this, "İlaç eklemediniz.", Toast.LENGTH_SHORT).show();
                } else {
                    List<Ilac_2> allIlacList = ilacAdapter.getIlacList();
                    Intent intent = new Intent(recete_2.this, Resim.class);
                    // Ilac_2 listesi intent'e ekle
                    intent.putParcelableArrayListExtra("ilac_listesi", new ArrayList<>(allIlacList));
                    // Diğer aktiviteyi başlat
                    startActivity(intent);
                }
            }
        });

        btnIlacEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedIlacAdi != null && selectedBarkod != null) {
                    ilacList.add(new Ilac_2(selectedIlacAdi, selectedBarkod));
                    ilacAdapter.notifyDataSetChanged();
                    selectedIlacAdi = null;
                    selectedBarkod = null;
                }
            }
        });
    }

    private void filterTable(String query) {
        for (int i = 1; i < tableLayout2.getChildCount(); i++) {
            TableRow row = (TableRow) tableLayout2.getChildAt(i);

            TextView atcAdi = (TextView) row.getChildAt(0);
            String atcAdiText = atcAdi.getText().toString().toLowerCase();

            TextView atcKodu = (TextView) row.getChildAt(1);
            String atcKoduText = atcKodu.getText().toString().toLowerCase();

            TextView barkod = (TextView) row.getChildAt(2);
            String barkodText = barkod.getText().toString().toLowerCase();

            TextView ilacAdi = (TextView) row.getChildAt(3);
            String ilacAdiText = ilacAdi.getText().toString().toLowerCase();

            if (atcAdiText.contains(query.toLowerCase()) ||
                    atcKoduText.contains(query.toLowerCase()) ||
                    barkodText.contains(query.toLowerCase()) ||
                    ilacAdiText.contains(query.toLowerCase())) {
                tableLayout2.removeView(row);
                tableLayout2.addView(row, 1);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTableData();
    }

    private void loadTableData() {
        ilaclarRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if (tableLayout2.getChildCount() > 1) {
                        tableLayout2.removeViews(1, tableLayout2.getChildCount() - 1);
                    }
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Ilac ilac = snapshot.getValue(Ilac.class);

                        if (ilac != null) {
                            TableRow row = new TableRow(recete_2.this);
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

                            row.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    selectedIlacAdi = ilac.ilac_adi;
                                    selectedBarkod = ilac.barkod;

                                    if (lastSelectedRow != null) {
                                        lastSelectedRow.setBackgroundColor(Color.TRANSPARENT); // Son seçili satırın rengini sıfırla
                                    }
                                    row.setBackgroundColor(Color.LTGRAY); // Seçili satırı gri yap
                                    lastSelectedRow = row; // Son seçili satırı güncelle
                                }
                            });

                            tableLayout2.addView(row);
                        }
                    }
                } catch (Exception e) {
                    Log.e("Ilac_Bilgisi", "Veri yükleme hatası", e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Ilac_Bilgisi", "loadTableData:onCancelled", databaseError.toException());
            }
        });
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(recete_2.this);
        textView.setLayoutParams(new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                1.0f
        ));
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8);
        textView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        return textView;
    }
}
