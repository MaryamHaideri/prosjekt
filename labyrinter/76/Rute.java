import java.util.ArrayList;

abstract class Rute {
    public int kolonne;
    public int rad;
    public Rute nord, ost, sor, vest;
    public Rute rute;
    public Labyrint labyrint;
    ArrayList<Rute> naboliste;
    public MonitorK traader;
    private String utvei;
    public ArrayList<Rute> gyldigenaboer;


    public Rute(int k, int r) {
        kolonne = k;
        rad = r;
        utvei = "";


    }

    // referanser til nabo-ruter (nord/syd/vest/øst)
    public void nord(Rute rute) {
        this.nord = rute;
    }

    public void sor(Rute rute) {
        this.sor = rute;
    }

    public void ost(Rute rute) {
        this.ost = rute;
    }

    public void vest(Rute rute) {
        this.vest = rute;
    }

    public void lab(Labyrint lab) {
        this.labyrint = lab;
    }

    // abstract metoden chartilTegn() returnerer rutens tegnrepresentasjon
    abstract public char chartilTegn();

    // sjekker om ruten har aapening
    abstract public boolean harAapning();


    class IndreRute implements Runnable {// en indre klass som implementere Runneble
        private Rute r;
        
        public IndreRute(Rute r){
            this.r = r;
        }
        public void run() { 
    
            try {
                r.gaa(rute,utvei);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void gaa(Rute rute, String vei) throws InterruptedException {
        
        
        gyldigenaboer();
        traader = new MonitorK();

        if(this.harAapning() == true){
            if(! utvei.contains( "(" + this.rad + "," + this.kolonne + ")")){
                utvei = vei + "(" + this.rad + "," + this.kolonne + ")";
            }
            this.labyrint.utveier.leggTil(utvei);
            return;
        }
            
        else if(gyldigenaboer.size() == 0){
            return;
        }

        else{
            int teller = gyldigenaboer.size();
            utvei = vei + "(" + this.rad + "," + this.kolonne + ")" + ("-->");
            
 
            for(int i=0; i < gyldigenaboer.size(); i++){ //lager nye tråder for gyldige nabo-rutene

                if(gyldigenaboer.size() == 1){
                    
                }

                if(gyldigenaboer.size() > 1) {
                    if(! utvei.contains("(" + gyldigenaboer.get(i).rad + "," + gyldigenaboer.get(i).kolonne + ")")){

                        Thread traad = new Thread(new IndreRute(gyldigenaboer.get(i)));
                        traader.leggTil(traad);
                        traad.start();
                        teller--;
                        
                    }      

                }
                else{//den gamle tråden
                    if(! utvei.contains("(" + rute.rad + "," + rute.kolonne + ")")){
                        Rute nabo = gyldigenaboer.get(gyldigenaboer.size()-1);
                        nabo.gaa(rute, utvei);
                    }
                }

            }
            for(Thread t : traader.hentListen()){//slik vill den gamle tråden vite at alle trådene den har startet opp er terminert
                t.join();
            }   
        }  
    } 

    public void finnUtvei() throws InterruptedException {
        this.gaa(this, utvei);
    }

    public ArrayList<Rute> gyldigenaboer(){ //finner gyldige naboer, altså de som er hvite og har ikke samme koordinat som startpunkt

        gyldigenaboer = new ArrayList<Rute>();

        naboliste = new ArrayList<Rute>();
        naboliste.add(this.nord);
        naboliste.add(this.sor);
        naboliste.add(this.ost);
        naboliste.add(this.vest);


        for(int i=0; i<naboliste.size(); i++){
            if(naboliste.get(i) == null){
            }
            else if(naboliste.get(i).chartilTegn() == '.'){
                gyldigenaboer.add(naboliste.get(i));
            }
        }
        return gyldigenaboer;
    }

}