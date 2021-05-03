/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diagnosa.penyakit;

/**
 *
 * @author user
 */
public class Penyakit {
    String nama, kode;
    
    public Penyakit(String nama, String kode) {
        this.nama = nama;
        this.kode = kode;
    }
    
    @Override
    public String toString() {
        return String.format("%1s: %2s", kode, nama);
    }
}
