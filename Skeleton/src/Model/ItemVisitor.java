package Model;

public interface ItemVisitor {

    //Absztrakt függvények az ItemVisitor interface-t megvalósító karakterek számára,
    //tárgyak felvételéhez és letevéséhez használatosak
    public void Visit(TVSZ i, boolean pickUp);
    public void Visit(Beer i, boolean pickUp);
    public void Visit(Sponge i, boolean pickUp);
    public void Visit(FFP2 i, boolean pickUp);
    public void Visit(Camembert i, boolean pickUp);
    public void Visit(Transistor i, boolean pickUp);
    public void Visit(Logarlec i, boolean pickUp);
}
