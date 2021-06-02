/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diagnosa.penyakit;

import java.util.Arrays; //dibutuhkan pada baris 167,170
import java.util.HashSet; //dibutuhkan pada baris 48,63
import java.util.LinkedList; //dibutuhkan pada baris 75,77,82
import java.util.Scanner; //dibutuhkan pada baris 133
import java.util.List; //dibutuhkan pada baris 38,62,71,77,82,167
import java.util.Queue; //dibutuhkan pada baris 75

/**
 *
 * @author Kel 4 OOP
 */
public class DiagnosaPenyakit {
    
    //karna diakses oleh 'static main' maka variabel dibuat static (bukan new DiagnosaPenyakit().variabel)
    Penyakit f = new Penyakit("Flu", "F"), //buat objek penyakit flu
            d = new Penyakit("Demam", "D"), //buat objek penyakit demam
            b = new Penyakit("Bronkhitis", "B"), //buat objek penyakit bronkhitis
            a = new Penyakit("Alergi", "A"); //buat objek penyakit alergi
    public Gejala[] gejala = new Gejala[] {
        new Gejala("Panas", new Penyakit[] { f, d, b }, 0.8f), //buat objek gejala 'panas' dengan kemungkinan penyakit flu, demam, bronkhitis
        new Gejala("Hidung Buntu", new Penyakit[] { a, f, d }, 0.9f), //buat objek gejala 'hidung buntu' dgn kemungkinan penyakit alergi, flu, demam
        new Gejala("Makan Udang", new Penyakit[] { a }, 0.6f), //buat objek gejala 'makan udang' dengan kemungkinan penyakit alergi
        new Gejala("Sakit Kepala", new Penyakit[] { d, b }, 0.7f), //buat objek gejala 'sakit kepala' dengan kemungkinan penyakit demam, bronkhitis
    };
    
    /*
    (baris 38-55)karna objek1 != objek2 walaupun isinya sama maka di cari tahu
      apakah isinya juga sama.
    */
    Gejala persis(List<Penyakit> penyakit, List<Gejala> densitas) { //mencari densitas sesuai isi larik 'penyakit'
        for (Gejala d: densitas) {
            if (penyakit == d.penyakit) return d; //jika alamat objek sama
            if (penyakit.size() != d.penyakit.size()) //jika ukuran larik (banyaknya objek dalam larik) tidak sama maka dianggap tidak sama
                continue;
            /*
            (baris 48-52)mencari irisan larik 'penyakit' dan 'd.penyakit'
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
    
    int compDescCF(Gejala d1, Gejala d2) {//method statis membandingkan 'cf' untuk diurutkan dari terbesar ke terkecil
        return -((Float)d1.cf).compareTo(d2.cf);
    }
    
    //(baris 62-69)mengambil fod dari gabungan semua 'penyakit' di 'diagnosa'
    Penyakit[] fod(List<Gejala> diagnosa) {//method statis sbg 'theta' (frame of discernment) yg merupakan semesta pembicara
        HashSet<Penyakit> env = new HashSet();
        for (Gejala diagnosa1 : diagnosa)
            env.addAll(diagnosa1.penyakit);
        Penyakit[] hasil = new Penyakit[env.size()];
        env.toArray(hasil);
        return hasil;
    }

    public List hitung(List<Gejala> diagnosa) {//menghitung dengan metode 'dempster shafer'
        if (diagnosa.size() < 2) return diagnosa;//jika banyaknya isi list 'diagnosa' < 2 maka menghitung dengan metode 'dempster shafer' dibatalkan
        Penyakit[] fod = fod(diagnosa);//mengambil fod dari gabungan 'penyakit' di 'diagnosa'
        //(baris 75-76)mengubah larik menjadi queue
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
            //(baris 88-90)membuat 'cf' theta
            float theta = 1;
            for (Gejala d : densitas2)
                theta -= d.cf;
            densitas2.add(new Gejala(null, fod, theta));//membuat theta (ke baris terakhir kolom 0)
            int m = densitas2.size();
            float k = 0;//konflik bukti
            densitas.clear();
            //(baris 96-107)mencari irisan
            for (int y=0; y<m; y++)
                for (int x=0; x<2; x++) {
                    if (y != m-1 || x!=1) {//theta diabaikan
                        Gejala vw = densitas1[x].irisan(densitas2.get(y));
                        if (vw.penyakit.size() > 0) {
                            Gejala d = persis(vw.penyakit, densitas);
                            if (d == null)
                                densitas.add(vw);
                            else d.cf += vw.cf;
                        } else k += vw.cf;//jika himpunan kosong maka masuk ke 'konflik bukti'
                    }
                }
            for (Gejala d: densitas)
                d.cf /= 1-k;
        }
        densitas.sort(this::compDescCF);//mengurutkan hasil berdasarkan 'cf' dari yg terbesar
        return densitas;
    }
    
    static int arraySearch(Object[] array, Object var) { //method statis mencari index objek dari array
        for(int i=0; i<array.length; i++) {
            if(array[i] == var)
                return i;
        }
        return -1;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        DiagnosaPenyakit dp = new DiagnosaPenyakit();
        System.out.println("Gejala yang diketahui");
        for(int d=0; d<dp.gejala.length; d++)
            System.out.println(String.format("(%d) %s", d+1, dp.gejala[d].nama));
        System.out.println();
        Scanner scanner = new Scanner(System.in); //objek membaca inputan console
        System.out.print(String.format("Banyaknya gejala [1-%d]:", dp.gejala.length));
        int diagnosis;
        /*
        (baris 140-142)selama diagnosis < 1 atau diagnosis > banyaknya
          gejala yg diketahui maka selalu menginput bilangan bulat
        */
        do {
            diagnosis = scanner.nextInt(); //menginput bilangan bulat (integer) ke 'diagnosis'
        } while(diagnosis < 1 || diagnosis > dp.gejala.length);
        Gejala[] diagnosa = new Gejala[diagnosis]; //membuat larik/array 'diagnosa' sebanyak 'diagnosis'
        //(baris 145-161)menginput gejala yg akan di-diagnosa
        for (int d=0; d<diagnosis; d++) {
            int g;
            System.out.print(String.format("Gejala %d [1-%d]:", d+1, dp.gejala.length));
            do {
                g = scanner.nextInt(); //menginput bilangan bulat ke 'g'
                /*
                (baris 154-156)jika g<1 atau g>banyaknya gejala yg diketahui atau
                  gejala sudah dipilih maka tampil pesan '(silahkan pilih yang lain)'
                */
                if(g < 1 || g > dp.gejala.length || //mengetahui apakah g<1 atau g>banyaknya gejala yg diketahui
                    arraySearch(diagnosa, dp.gejala[g-1]) >= 0) //mengetahui apakah gejala sudah dipilih
                    System.out.println("(silahkan pilih yang lain)");
            } while(g < 1 || g > dp.gejala.length ||
                    arraySearch(diagnosa, dp.gejala[g-1]) >= 0);
            diagnosa[d] = dp.gejala[g-1];
            System.out.println(String.format("(Gejala %d:(%d) %s)", d+1, g, diagnosa[d].nama));
        }
        System.out.println();
        System.out.println("Gejala yang dipilih");
        for (Gejala diagnosa1 : diagnosa)
            System.out.println(String.format("(%d) %s", arraySearch(dp.gejala, diagnosa1) + 1, diagnosa1.nama));
        System.out.println();
        List<Gejala> densitas = dp.hitung(Arrays.asList(diagnosa));
        Gejala hasil = densitas.get(0);
        System.out.println(String.format("Kemungkinan terbesar menderita penyakit %s sebesar %s",
                Arrays.toString(hasil.penyakit.toArray()),
                (Math.round(hasil.cf * 10000) / 100f) + "%"));
        for (Gejala d : densitas)
            System.out.println(d);
    }
    
}
