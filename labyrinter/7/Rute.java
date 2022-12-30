abstract class Rute {
    public int kolonne = -1;
    public int rad = -1;
    public Rute nord, ost, sor, vest;
    public Rute rute;
    public Labyrint labyrint;

    public Rute(int k, int r){
        kolonne = k;
        rad = r;
        
    }

//referanser til nabo-ruter (nord/syd/vest/øst)
    public void nord(Rute rute){
        this.nord = rute;
      }
    
      public void sor(Rute rute){
        this.sor = rute;
      }
    
      public void ost(Rute rute){
        this.ost = rute;
      }
    
      public void vest(Rute rute){
        this.vest = rute;
      }
      public void lab(Labyrint lab){
        this.labyrint = lab;
      }

//abstract metoden chartilTegn() returnerer rutens tegnrepresentasjon
    abstract public char chartilTegn();
//sjekker om ruten har aapening    
    abstract public boolean harAapning();


// denne metoden finner utveier fra en bestemt rute
//strategien er å prøve å gå alle veier bortsett fra den vi kom fra
    public void gaa(Rute forrige, String utvei){
        //Når man kommer til en ny rute, legger man den forrige rutens koordinater til strengen 
    
        // basissteget
        if(this.chartilTegn() == '#'){
            return;
        }
        
        else if(this.harAapning() == true){
            utvei += "(" + this.rad + "," + this.kolonne + ")"; 
            this.labyrint.utveier.leggTil(utvei);
            return;
        }
        
        //rekursjonssteget

        //sjekker hvis ruten er besøkt eller ikke
        else if(utvei.contains("(" + this.rad + "," + this.kolonne + ")")){
            return;
        }

        else{
            utvei += "(" + this.rad + "," + this.kolonne + ")"; 
            utvei = utvei + ("-->");
             //kalle på rekursive metoden i nabo-rutene
            if(this.ost != forrige){
                this.ost.gaa(this, utvei);
            }if(this.nord != forrige){
                this.nord.gaa(this, utvei);
            }if(this.sor != forrige){
                this.sor.gaa(this, utvei);
            }if(this.vest != forrige){
                this.vest.gaa(this, utvei);
            }         
        }  

    }


//denne metoden kaller på gaa()-metode
    public void finnUtvei(){
        this.gaa(this, "");
        
      }

}