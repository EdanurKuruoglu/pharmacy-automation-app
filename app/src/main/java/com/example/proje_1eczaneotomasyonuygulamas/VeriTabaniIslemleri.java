package com.example.proje_1eczaneotomasyonuygulamas;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VeriTabaniIslemleri {

    private DatabaseReference mDatabase;

    public VeriTabaniIslemleri() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void ilacKaydet(String atcAdi, String atcKodu, String barkod, String ilacAdi) {
        String key = mDatabase.child("ilac").push().getKey(); // Yeni bir anahtar oluşturun
        Ilac ilac = new Ilac(atcAdi, atcKodu, barkod, ilacAdi);
        mDatabase.child("ilac").child(key).setValue(ilac); // Veriyi ilgili düğüme kaydedin
    }

    // İlaç sınıfı
    public static class Ilac {
        public String atc_adi;
        public String atc_kodu;
        public String barkod;
        public String ilac_adi;

        public Ilac() {
            // Boş kurucu metod gereklidir
        }

        public Ilac(String atc_adi, String atc_kodu, String barkod, String ilac_adi) {
            this.atc_adi = atc_adi;
            this.atc_kodu = atc_kodu;
            this.barkod = barkod;
            this.ilac_adi = ilac_adi;
        }
    }
}
