package Model;

public class Camembert extends Item{

    //Fogadja az ItemVisitor interface-t megvalósító karaktert és meghívja a Visit() függvényét saját magával,
    //a pickUp paraméter értékétől függően tárgy felvételre, vagy tárgy letevésre fog a Visit() hívódni
    @Override
    public void Accept(ItemVisitor ch, boolean pickUp) {
        //TestPrint
        System.out.println("Accept(ch, " + pickUp + ") -> Camembert");
        ch.Visit(this, pickUp);
    }

    //A camembert használata
    @Override
    public void Use() {
        Gassing(GetRoom());
    }

    //Annak a szobának az elgázosítása ahol a camembert használják
    public void Gassing(Room r){
        r.GetPoisonous();
    }
}
