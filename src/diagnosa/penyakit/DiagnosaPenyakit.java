/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diagnosa.penyakit;

import java.util.Arrays; //dibutuhkan pada baris 81,84
import java.util.Scanner; //dibutuhkan pada baris 47
import java.util.List; //dibutuhkan pada baris 81

/**
 *
 * @author Kel 4 OOP
 */
public class DiagnosaPenyakit {
    
    //karna diakses oleh 'static main' maka variabel dibuat static (bukan new DiagnosaPenyakit().variabel)
    static Penyakit f = new Penyakit("Flu", "F"), //buat objek penyakit flu
            d = new Penyakit("Demam", "D"), //buat objek penyakit demam
            b = new Penyakit("Bronkhitis", "B"), //buat objek penyakit bronkhitis
            a = new Penyakit("Alergi", "A"); //buat objek penyakit alergi
    public static Gejala[] gejala = new Gejala[] {
        new Gejala("Panas", new Penyakit[] { f, d, b }, 0.8f), //buat objek gejala 'panas' dengan kemungkinan penyakit flu, demam, bronkhitis
        new Gejala("Hidung Buntu", new Penyakit[] { a, f, d }, 0.9f), //buat objek gejala 'hidung buntu' dgn kemungkinan penyakit alergi, flu, demam
        new Gejala("Makan Udang", new Penyakit[] { a }, 0.6f), //buat objek gejala 'makan udang' dengan kemungkinan penyakit alergi
        new Gejala("Sakit Kepala", new Penyakit[] { d, b }, 0.7f), //buat objek gejala 'sakit kepala' dengan kemungkinan penyakit demam, bronkhitis
        new Gejala("Meriang", new Penyakit[] { d, f }, 0.5f) //buat objek gejala 'sakit kepala' dengan kemungkinan penyakit demam, bronkhitis
    };
    
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
        System.out.println("Gejala yang diketahui");
        for(int d=0; d<gejala.length; d++)
            System.out.println(String.format("(%d) %s", d+1, gejala[d].nama));
        System.out.println();
        Scanner scanner = new Scanner(System.in); //objek membaca inputan console
        System.out.print(String.format("Banyaknya gejala [1-%d]:", gejala.length));
        int diagnosis;
        /*
        (baris 54-56)selama diagnosis < 1 atau diagnosis > banyaknya
          gejala yg diketahui maka selalu menginput bilangan bulat
        */
        do {
            diagnosis = scanner.nextInt(); //menginput bilangan bulat (integer) ke 'diagnosis'
        } while(diagnosis < 1 || diagnosis > gejala.length);
        Gejala[] diagnosa = new Gejala[diagnosis]; //membuat larik/array 'diagnosa' sebanyak 'diagnosis'
        //(baris 59-73)menginput gejala yg akan di-diagnosa
        for(int d=0; d<diagnosis; d++){
            int g;
            System.out.print(String.format("Gejala %d [1-%d]:", d+1, gejala.length));
            do {
                g = scanner.nextInt(); //menginput bilangan bulat ke 'g'
                /*
                (baris 68-70)jika g<1 atau g>banyaknya gejala yg diketahui atau
                  gejala sudah dipilih maka tampil pesan '(silahkan pilih yang lain)'
                */
                if(g < 1 || g > gejala.length || //mengetahui apakah g<1 atau g>banyaknya gejala yg diketahui
                    arraySearch(diagnosa, gejala[g-1]) >= 0) //mengetahui apakah gejala sudah dipilih
                    System.out.println("(silahkan pilih yang lain)");
            } while(g < 1 || g > gejala.length ||
                    arraySearch(diagnosa, gejala[g-1]) >= 0);
            diagnosa[d] = gejala[g-1];
            System.out.println(String.format("(Gejala %d:(%d) %s)", d+1, g, diagnosa[d].nama));
        }
        System.out.println();
        System.out.println("Gejala yang dipilih");
        for (Gejala diagnosa1 : diagnosa)
            System.out.println(String.format("(%d) %s", arraySearch(gejala, diagnosa1) + 1, diagnosa1.nama));
        System.out.println();
        List<Gejala> densitas = Gejala.hitung(Arrays.asList(diagnosa));
        Gejala hasil = densitas.get(0);
        System.out.println(String.format("Kemungkinan terbesar menderita penyakit %s sebesar %s",
                Arrays.toString(hasil.penyakit.toArray()),
                (Math.round(hasil.cf * 10000) / 100f) + "%"));
        for (Gejala d : densitas)
            System.out.println(d);
    }
    
}
