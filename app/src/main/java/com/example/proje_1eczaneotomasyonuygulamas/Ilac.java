package com.example.proje_1eczaneotomasyonuygulamas;

public class Ilac {
    public String atc_adi;
    public String atc_kodu;
    public String barkod; // Barkod yerine ilac_barkod olarak değiştirdim
    public String ilac_adi;
    public Koordinatlar koordinatlar; // Yeni eklenen alan

    public Ilac() {
        // Boş yapıcı yöntem Firebase tarafından gereklidir.
    }

    public Ilac(String atc_adi, String atc_kodu, String barkod, String ilac_adi) {
        this.atc_adi = atc_adi;
        this.atc_kodu = atc_kodu;
        this.barkod = barkod;
        this.ilac_adi = ilac_adi;
    }

    public static class Koordinatlar {
        public ResimKoordinat resim1;
        public ResimKoordinat resim2;

        public Koordinatlar() {
        }

        public Koordinatlar(ResimKoordinat resim1, ResimKoordinat resim2) {
            this.resim1 = resim1;
            this.resim2 = resim2;
        }
    }

    public static class ResimKoordinat {
        public float x;
        public float y;

        public ResimKoordinat() {
        }

        public ResimKoordinat(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
