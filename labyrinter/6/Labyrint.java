
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Labyrint {
    ArrayList<ArrayList<Rute>> lbArray; // todimensjonalt Rute-Arrayliste
    int kolonne;
    int rad;
    public MonitorUtvei utveier;
    //Liste<String> utveier; // list av utveier

    // private konstruktør
    private Labyrint(int rad, int kolonne, ArrayList<ArrayList<Rute>> ruteArray) {
        this.lbArray = ruteArray;
        this.kolonne = kolonne;
        this.rad = rad;

    }

    public ArrayList<ArrayList<Rute>> getArray() {
        return this.lbArray;
    }

    // innlesning fra fil
    static Labyrint lesFraFil(File fil) throws FileNotFoundException {
        // Lese inn alle linjene fra filen
        Scanner scanner = new Scanner(fil);
        String[] str = scanner.nextLine().split(" ");
        int rad = Integer.parseInt(str[0]);
        int kolonne = Integer.parseInt(str[1]);

        ArrayList<ArrayList<Rute>> ruteArray = new ArrayList<ArrayList<Rute>>();

        int r = 0;

        // sjekker rutene i filen og lager nye Ruter ut fra informasjonen fra filen
        while (scanner.hasNextLine()) {
            String linje = scanner.nextLine();
            ArrayList<Rute> rader = new ArrayList<Rute>();

            for (int i = 0; i < kolonne; i++) {
                char tegn = linje.charAt(i);
                Rute nyRute;
                if (tegn == '.' && (r == kolonne - 1 || r == 0 || i == kolonne - 1 || i == 0)) {
                    nyRute = new Aapning(r, i);
                    // traade = new Thread(nyRute); // lage nye trade
                    // traade.start(); // starte tråden

                } else if (tegn == '.') {
                    nyRute = new HvitRute(r, i);

                } else {
                    nyRute = new SortRute(r, i);
                }
                rader.add(nyRute);
            }
            r++;
            ruteArray.add(rader);
        }
        for (int x = 0; x < rad; x++) {
            for (int y = 0; y < kolonne; y++) {
                if (y < kolonne - 1) {
                    ruteArray.get(x).get(y).ost(ruteArray.get(x).get(y + 1));
                    ruteArray.get(x).get(y + 1).vest(ruteArray.get(x).get(y));

                }
                if (x < rad - 1) {
                    ruteArray.get(x).get(y).sor(ruteArray.get(x + 1).get(y));
                    ruteArray.get(x + 1).get(y).nord(ruteArray.get(x).get(y));
                }
            }
        }
        // opprette labyrint basert på fildata og legge den inn i arrayliste
        Labyrint labyrint = new Labyrint(rad, kolonne, ruteArray);
        for (int x = 0; x < rad; x++) {
            for (int y = 0; y < kolonne; y++) {
                ruteArray.get(x).get(y).lab(labyrint);
            }
        }
        return labyrint;
    }

    // hele labyrinten returneres i et enkelt format til terminalen
    @Override
    public String toString() {
        String labyStr = "";
        String chaStr = "";
        for (int x = 0; x < this.rad; x++) {
            for (int y = 0; y < this.kolonne; y++) {
                chaStr = String.valueOf(this.lbArray.get(x).get(y).chartilTegn());
                labyStr = labyStr.concat(chaStr);
            }
            labyStr = labyStr.concat("\n");
        }
        return labyStr;
    }

    // Når man funnet en utvei, skal strengen med utveien legges inn i en
    // Liste<String>
    // og listen returneres
    public MonitorUtvei finnUtveiFra(int k, int r) {
        this.utveier = new MonitorUtvei();
        try {
            this.lbArray.get(r).get(k).finnUtvei();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return this.utveier;
    }

}

class MonitorK {//monitor-klassen som ta vare på trålisten 

    
    private Lenkeliste<Thread> liste = new Lenkeliste<Thread>();
    private Lock laas = new ReentrantLock();
    Condition ikkeTom = laas.newCondition();

    void leggTil(Thread vei){
        laas.lock();
        liste.leggTil(vei);

        ikkeTom.signal();
        laas.unlock();
    }

    public Lenkeliste<Thread> hentListen(){
        return liste;
    }

}


class MonitorUtvei {//monitor-klassen som ta vare på utveier 

    
    private Lenkeliste<String> liste = new Lenkeliste<String>();
    private Lock laas = new ReentrantLock();
    Condition ikkeTom = laas.newCondition();

    void leggTil(String utvei){
        laas.lock();
        liste.leggTil(utvei);

        ikkeTom.signal();
        laas.unlock();
    }

    public Lenkeliste<String> hentListen(){
        return liste;
    }
    public int stoerrelse(){
        return liste.stoerrelse();
    }

}