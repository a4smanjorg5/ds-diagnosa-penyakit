/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diagnosa.penyakit;

import java.util.Arrays; //dibutuhkan pada baris 23,43
import java.util.HashSet; //dibutuhkan pada baris 30,62,77
import java.util.LinkedList; //dibutuhkan pada baris 89,91,96
import java.util.List; //dibutuhkan pada baris 20,52,76,85,91,96,115
import java.util.Queue; //dibutuhkan pada baris 89

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

    //(baris 29-36)mencari irisan list 'penyakit' dan mengalikan 'cf'
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
    
    /*
    (baris 52-69)karna objek1 != objek2 walaupun isinya sama maka di cari tahu
      apakah isinya juga sama.
    */
    static Gejala persis(List<Penyakit> penyakit, List<Gejala> densitas) { //mencari densitas sesuai isi larik 'penyakit'
        for (Gejala d: densitas) {
            if (penyakit == d.penyakit) return d; //jika alamat objek sama
            if (penyakit.size() != d.penyakit.size()) //jika ukuran larik (banyaknya objek dalam larik) tidak sama maka dianggap tidak sama
                continue;
            /*
            (baris 62-66)mencari irisan larik 'penyakit' dan 'd.penyakit'
              jika panjang irisan sama dengan banyaknya isi larik 'penyakit'
              maka diberikan alamat densitas
            */
            HashSet<Penyakit> its = new HashSet();
            its.addAll(penyakit);
            its.retainAll(d.penyakit);
            if (its.size() == penyakit.size())
                return d;
        }
        return null;
    }
    
    static int compDescCF(Gejala d1, Gejala d2) {//method statis membandingkan 'cf' untuk diurutkan dari terbesar ke terkecil
        return -((Float)d1.cf).compareTo(d2.cf);
    }
    
    //(baris 76-84)mengambil fod dari gabungan semua 'penyakit' di 'diagnosa'
    static Penyakit[] fod(List<Gejala> diagnosa) {//method statis sbg 'theta' (frame of discernment) yg merupakan semesta pembicara
        HashSet<Penyakit> env = new HashSet();
        for (Gejala diagnosa1 : diagnosa)
            env.addAll(diagnosa1.penyakit);
        Penyakit[] hasil = new Penyakit[env.size()];
        env.toArray(hasil);
        return hasil;
    }

    public static List hitung(List<Gejala> diagnosa) {//menghitung dengan metode 'dempster shafer'
        if (diagnosa.size() < 2) return diagnosa;//jika banyaknya isi list 'diagnosa' < 2 maka menghitung dengan metode 'dempster shafer' dibatalkan
        Penyakit[] fod = Gejala.fod(diagnosa);//mengambil fod dari gabungan 'penyakit' di 'diagnosa'
        //(baris 89-90)mengubah larik menjadi queue
        Queue<Gejala> gejala = new LinkedList();
        gejala.addAll(diagnosa);
        List<Gejala> densitas = new LinkedList();
        while (gejala.size() > 0) {
            Gejala[] densitas1 = new Gejala[2];//sebagai kolom tabel
            densitas1[0] = gejala.remove();//mengambil gejala yg lebih awal (ke kolom 1 baris 0)
            densitas1[1] = new Gejala(null, fod, 1-densitas1[0].cf);//membuat theta(berdasarkan metode 'dempster shafer') (ke kolom 2 baris 0)
            List<Gejala> densitas2 = new LinkedList();//sebagai baris tabel 
            if (densitas.size() > 0) {
                for (Gejala d : densitas)
                    densitas2.add(d);//jika densitas sebelumnya ada maka diletakkan di baris tabel (ke baris berikutnya kolom 0)
            } else densitas2.add(gejala.remove());//jika densitas sebelumnya tidak ada maka mengambil gejala yg lebih awal (ke baris 1 kolom 0)
            //(baris 102-104)membuat 'cf' theta
            float theta = 1;
            for (Gejala d : densitas2)
                theta -= d.cf;
            densitas2.add(new Gejala(null, fod, theta));//membuat theta (ke baris terakhir kolom 0)
            int m = densitas2.size();
            float k = 0;//konflik bukti
            densitas.clear();
            //(baris 110-122)mencari irisan
            for (int y=0; y<m; y++)
                for (int x=0; x<2; x++) {
                    if (y != m-1 || x!=1) {//theta diabaikan
                        Gejala vw = densitas1[x].irisan(densitas2.get(y));
                        if (vw.penyakit.size() > 0) {
                            Gejala d = Gejala.persis(vw.penyakit, densitas);
                            if (d == null)
                                densitas.add(vw);
                            else d.cf += vw.cf;
                        } else k += vw.cf;//jika himpunan kosong maka masuk ke 'konflik bukti'
                    }
                }
            for (Gejala d: densitas)
                d.cf /= 1-k;
        }
        densitas.sort(Gejala::compDescCF);//mengurutkan hasil berdasarkan 'cf' dari yg terbesar
        return densitas;
    }
}
