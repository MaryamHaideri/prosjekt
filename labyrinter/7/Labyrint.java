import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

class Labyrint {
    ArrayList<ArrayList<Rute>> lbArray; //todimensjonalt Rute-Arrayliste 
    int kolonne = -1;
    int rad = -1;
    Liste<String> utveier; //list av utveier
    static ArrayList<ArrayList<Rute>> ruteArray = new ArrayList<ArrayList<Rute>>();

    // private konstruktør
    private Labyrint(int rad, int kolonne, ArrayList<ArrayList<Rute>> ruteArray) {
        this.lbArray = ruteArray;
        this.kolonne = kolonne;
        this.rad = rad;

    }

    // innlesning fra fil
    static Labyrint lesFraFil(File fil) throws FileNotFoundException {
        // Lese inn alle linjene fra filen
        Scanner scanner = new Scanner(fil);
        String[] str = scanner.nextLine().split(" ");
        int rad = Integer.parseInt(str[0]);
        int kolonne = Integer.parseInt(str[1]);

        int r = 0;

        // sjekker rutene i filen og lager nye Ruter ut fra informasjonen fra filen
        while (scanner.hasNextLine()) {
            String linje = scanner.nextLine();
            ArrayList<Rute> rader = new ArrayList<Rute>();
            Rute nyRute;

            for (int i = 0; i < kolonne; i++) {
                char tegn = linje.charAt(i);
                if (tegn == '.' && (r == rad - 1 || r == 0 || i == rad - 1 || i == 0)) {
                    nyRute = new Aapning(r, i);
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
                    ruteArray.get(x).get(y+1).vest(ruteArray.get(x).get(y));

                }if(x<rad-1){
                    ruteArray.get(x).get(y).sor(ruteArray.get(x+1).get(y));
                    ruteArray.get(x+1).get(y).nord(ruteArray.get(x).get(y));
                }
            }
        }
        //opprette labyrint basert på fildata og legge den inn i arrayliste
        Labyrint labyrint = new Labyrint(rad, kolonne, ruteArray);
        for(int x=0; x<rad; x++){
            for(int y=0; y<kolonne; y++){
                ruteArray.get(x).get(y).lab(labyrint);
            }
        }
        return labyrint;
    }

   //hele labyrinten returneres i et enkelt format til terminalen 
    @Override
    public String toString(){
        String labyStr = "";
        String chaStr = "";
        for(int x=0; x<this.rad; x++){
            for(int y=0; y<this.kolonne; y++){
                chaStr = String.valueOf(this.lbArray.get(x).get(y).chartilTegn());
                labyStr = labyStr.concat(chaStr);
            }
            labyStr = labyStr.concat("\n");
        }
        return labyStr;
    }

//Når man funnet en utvei, skal strengen med utveien legges inn i en Liste<String>
//og listen returneres
    public Liste<String> finnUtveiFra(int k, int r){
        this.utveier = new Lenkeliste<String>();
        this.lbArray.get(r).get(k).finnUtvei();
        return this.utveier;
    }

    public int hentRad(){
        return this.rad;
    }

    public int hentKolonne(){
        return this.kolonne;
    }

}
