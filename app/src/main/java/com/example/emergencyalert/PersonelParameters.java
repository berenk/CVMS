package com.example.emergencyalert;


import android.util.Base64;

public class PersonelParameters {
    //Header
    private String KullaniciAdi = "KAMPUSACIL";
    private String SicilNo = "1212";
    private String Sifresi = "071634";
    private String ProjeId = "955";


    private String KullaniciAdiBytes = new String(Base64.encode(getKullaniciAdi().getBytes(), Base64.DEFAULT));
    private String SicilNoBytes = new String(Base64.encode(getSicilNo().getBytes(), Base64.DEFAULT));
    private String SifresiBytes = new String(Base64.encode(getSifresi().getBytes(), Base64.DEFAULT));
    private String ProjeIdBytes = new String(Base64.encode(getProjeId().getBytes(), Base64.DEFAULT));


    //Body
    private String DeVtok1 = "071634";
    private String DeVtok2 = "KAMPUSACIL";
    private String DeVtok3 = "955";
    private String YoneticiAdi = "YoneticiAdi" ;
    private String YoneticiSifre = "Acil2019!" ;

    private String DeVtokBytes = new String(Base64.encode(DeVtok1.getBytes(), Base64.DEFAULT)) + "@" +
            new String(Base64.encode(DeVtok2.getBytes(), Base64.DEFAULT)) + "@" +
            new String(Base64.encode(DeVtok3.getBytes(), Base64.DEFAULT));

    public String getKullaniciAdi() {
        return KullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        KullaniciAdi = kullaniciAdi;
    }

    public String getSicilNo() {
        return SicilNo;
    }

    public void setSicilNo(String sicilNo) {
        SicilNo = sicilNo;
    }

    public String getSifresi() {
        return Sifresi;
    }

    public void setSifresi(String sifresi) {
        Sifresi = sifresi;
    }

    public String getProjeId() {
        return ProjeId;
    }

    public void setProjeId(String projeId) {
        ProjeId = projeId;
    }

    public String getKullaniciAdiBytes() {
        return KullaniciAdiBytes;
    }

    public void setKullaniciAdiBytes(String kullaniciAdiBytes) {
        KullaniciAdiBytes = kullaniciAdiBytes;
    }

    public String getSicilNoBytes() {
        return SicilNoBytes;
    }

    public void setSicilNoBytes(String sicilNoBytes) {
        SicilNoBytes = sicilNoBytes;
    }

    public String getSifresiBytes() {
        return SifresiBytes;
    }

    public void setSifresiBytes(String sifresiBytes) {
        SifresiBytes = sifresiBytes;
    }

    public String getProjeIdBytes() {
        return ProjeIdBytes;
    }

    public void setProjeIdBytes(String projeIdBytes) {
        ProjeIdBytes = projeIdBytes;
    }



    public String getYoneticiAdi() {
        return YoneticiAdi;
    }

    public void setYoneticiAdi(String yoneticiAdi) {
        YoneticiAdi = yoneticiAdi;
    }

    public String getYoneticiSifre() {
        return YoneticiSifre;
    }

    public void setYoneticiSifre(String yoneticiSifre) {
        YoneticiSifre = yoneticiSifre;
    }

    public String getDeVtokBytes() {
        return DeVtokBytes;
    }

    public void setDeVtokBytes(String deVtokBytes) {
        DeVtokBytes = deVtokBytes;
    }
    /*
    private String YoneticiAdiBytes = new String(Base64.encode(YoneticiAdi.getBytes(), Base64.DEFAULT));
    private String YoneticiSifreBytes = new String(Base64.encode(YoneticiSifre.getBytes(), Base64.DEFAULT));
*/



}
