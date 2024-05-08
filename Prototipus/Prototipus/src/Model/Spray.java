package Model;

public class Spray extends Item{

    //Fogadja az ItemVisitor interface-t megvalósító karaktert és meghívja a Visit() függvényét saját magával,
    //a pickUp paraméter értékétől függően tárgy felvételre, vagy tárgy letevésre fog a Visit() hívódni
    @Override
    public void Accept(ItemVisitor ch, boolean pickUp) {
        ch.Visit(this, pickUp);
    }

    //teszteléshez kell proto
    @Override
    public String getName() {
        return "spray";
    }

    //A légfrissítő használata
    @Override
    public void Use() {
    }
    public void Use(Room r) {
        CleanAir(r);
    }


    @Override
    public void Step(int round) {
    }

    //Meghívja a paraméterül kapott szobának a refreshAir függvényét
    public void CleanAir(Room r){
        r.RefreshAir();
    }
    
}
