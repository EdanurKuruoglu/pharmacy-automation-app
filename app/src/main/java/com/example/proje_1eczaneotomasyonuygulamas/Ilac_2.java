package com.example.proje_1eczaneotomasyonuygulamas;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Ilac_2 implements Parcelable{
    private String ilacAdi;
    private String barkod;

    public Ilac_2(String ilacAdi, String barkod) {
        this.ilacAdi = ilacAdi;
        this.barkod = barkod;
    }

    protected Ilac_2(Parcel in) {
        ilacAdi = in.readString();
        barkod = in.readString();
    }

    public static final Creator<Ilac_2> CREATOR = new Creator<Ilac_2>() {
        @Override
        public Ilac_2 createFromParcel(Parcel in) {
            return new Ilac_2(in);
        }

        @Override
        public Ilac_2[] newArray(int size) {
            return new Ilac_2[size];
        }
    };

    public String getIlacAdi() {
        return ilacAdi;
    }

    public void setIlacAdi(String ilacAdi) {
        this.ilacAdi = ilacAdi;
    }

    public String getBarkod() {
        return barkod;
    }

    public void setBarkod(String barkod) {
        this.barkod = barkod;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(ilacAdi);
        dest.writeString(barkod);
    }
}
