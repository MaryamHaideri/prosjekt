import java.util.Iterator;
public class Lenkeliste<T> implements Liste<T> {
    protected int stoerrelse;
    protected Node start, slutt;

    protected class Node {
      T innhold;
      Node neste, forrige;

      Node (T innhold) {
        this.innhold = innhold;
      }
    }

    private class LenkeListeIterator implements Iterator<T> {
      private Lenkeliste<T>.Node gjeldendeElement; // variabel som holder på det elementet iteratoren er på.

      public LenkeListeIterator() {
        gjeldendeElement = start;
      }

      @Override
      public T next() {
        gjeldendeElement = gjeldendeElement.neste;
        return gjeldendeElement.innhold;
      }
      
      @Override
      public boolean hasNext() {
        return gjeldendeElement != slutt.forrige;
      }
    }
  
    public Iterator<T> iterator() {
      return new LenkeListeIterator();
    }
  
    public Lenkeliste() {
      stoerrelse = 0;
      start = new Node(null);
      slutt = new Node(null);
      start.neste = slutt;
      slutt.forrige = start;
    }
  
    @Override
    public int stoerrelse() {
      return stoerrelse;
    }
  
  
  
    @Override
    public void leggTil(T x) {
      Node ny = new Node(x);
      ny.forrige = slutt.forrige;
      ny.forrige.neste = ny;
      ny.neste = slutt;
      slutt.forrige = ny;

      stoerrelse++;
    }
  
    @Override
    public void leggTil(int pos, T x) throws UgyldigListeIndeks {
      if (pos > stoerrelse || pos < 0) {
        UgyldigListeIndeks e = new UgyldigListeIndeks(pos);
        throw e;  
      }
      if (stoerrelse == 0 && pos != 0) {
        UgyldigListeIndeks e = new UgyldigListeIndeks(-1);  
        throw e;
      }
  
      Node ny = new Node(x);
      Node foran = start;
  
      for (int n=0; n<pos; n++) {
        foran = foran.neste;
      }
  
      ny.neste = foran.neste;
      ny.neste.forrige = ny;
      ny.forrige = foran;
      foran.neste = ny;
      stoerrelse++;
    }
  
    @Override
    public void sett(int pos, T x) throws UgyldigListeIndeks {
  
      if (pos >= stoerrelse || pos < 0) {
        UgyldigListeIndeks e = new UgyldigListeIndeks(pos);
        throw e;
      }
  

      if (stoerrelse == 0) {  
        UgyldigListeIndeks e = new UgyldigListeIndeks(-1);
        throw e;
      }
  
      Node foran = start;
  
      for (int n=0; n<pos; n++) {
        foran = foran.neste;
      }

      foran.neste.innhold = x;
    }
  
  
  
  
  
    @Override
  
    public T hent(int pos) throws UgyldigListeIndeks {
  
      if (pos >= stoerrelse || pos < 0) {
        UgyldigListeIndeks e = new UgyldigListeIndeks(pos);
        throw e;
      }
  
      if (stoerrelse == 0) {
        UgyldigListeIndeks e = new UgyldigListeIndeks(-1);
        throw e;
      }

      Node foran = start;

      for (int n=0; n<pos; n++) {
        foran = foran.neste;
      }  
      T returneres = foran.neste.innhold;
      return returneres;
    }
  
  
  
  
  
    @Override
  
    public T fjern(int pos) throws UgyldigListeIndeks {
  
      if (pos >= stoerrelse || pos < 0) {  
        UgyldigListeIndeks e = new UgyldigListeIndeks(pos);
        throw e;
      }
  
      if (stoerrelse == 0) {
        UgyldigListeIndeks e = new UgyldigListeIndeks(-1);
        throw e;
      }
      Node foran = start;
  
  
      for (int n=0; n<pos; n++) {
        foran = foran.neste;
      }

      //ikke pos-1 fordi start alltid er null osv.
      Node fjern = foran.neste;
      Node etter = fjern.neste;
  
      foran.neste = etter;
      etter.forrige = foran;
      stoerrelse--;

      return fjern.innhold;
    }
  
  
  
    @Override
    public T fjern() throws UgyldigListeIndeks {
      if (stoerrelse == 0) {
        UgyldigListeIndeks e = new UgyldigListeIndeks(-1);
        throw e;
      }

      Node fjern = start.neste;  
      Node etter = fjern.neste;
  
      start.neste = etter;
      etter.forrige = start;
      stoerrelse--;

      return fjern.innhold;  
    }
  }