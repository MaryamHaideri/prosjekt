
class HvitRute extends Rute {
  public HvitRute(int r, int k) {
    super(r, k);
  }

  public char chartilTegn() {// returnerer rutens tegnrepresentasjon
    return '.';
  }

  @Override
  public boolean harAapning() {// sjekker om ruten har aapening
    return false;
  }

}