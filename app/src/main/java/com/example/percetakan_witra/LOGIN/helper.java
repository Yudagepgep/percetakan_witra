package com.example.percetakan_witra.LOGIN;

public class helper {

    private int id_pelanggan;
    private  String nama,email,kode,hp,alamat,photo;

    public helper(int id_pelanggan,String nama,String email,String kode,String hp,
                  String alamat, String photo){

        this.id_pelanggan= id_pelanggan;
        this.nama=nama;
        this.email=email;
        this.kode=kode;
        this.hp=hp;
        this.alamat=alamat;
        this.photo=photo;
    }

    public int getId_pelanggan() {
        return id_pelanggan;
    }

    public String getNama() {
        return nama;
    }

    public String getEmail() {
        return email;
    }

    public String getKode() {
        return kode;
    }

    public String getHp() {
        return hp;
    }

    public String getAlamat() {
        return alamat;
    }


    public String getPhoto() {
        return photo;
    }
}
