/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diagnosa.penyakit;

import java.util.Arrays; //dibutuhkan pada baris 23,41
import java.util.HashSet; //dibutuhkan pada baris 28
import java.util.List; //dibutuhkan pada baris 18

/**
 *
 * @author user
 */
public class Gejala {
    public String nama;
    public List<Penyakit> penyakit;
    public float cf;
    public Gejala(String nama, Penyakit[] penyakit, float cf) {
        this.penyakit = Arrays.asList(penyakit);
        this.nama = nama;
        this.cf = cf;
    }

    //(baris 27-34)mencari irisan list 'penyakit' dan mengalikan 'cf'
    Gejala irisan(Gejala densitas) {
        HashSet<Penyakit> its = new HashSet();
        its.addAll(penyakit);//masukkan semua objek dari list 'penyakit' objek ini (this.penyakit)
        its.retainAll(densitas.penyakit);//keluarkan semua objek yg tidak ada di larik 'densitas.penyakit'
        Penyakit[] p = new Penyakit[its.size()];
        its.toArray(p);
        return new Gejala(null, p, cf * densitas.cf);
    }

    @Override
    public String toString() {
        String np = this.nama;
        if (np == null) np = "";
        return String.format("%s{ penyakit: %s, cf: %s }", np + " ",
            Arrays.toString(penyakit.toArray()),
            (Math.round(cf*10000) / 100f) + "%"
        );
    }
}
