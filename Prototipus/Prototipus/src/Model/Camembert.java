package Model;

public class Camembert extends Item{

    //Ki lett-e nyitva a Camembert
    private boolean opened = false;

    public Camembert(){}

    public Camembert(boolean opened){
        this.opened = opened;
    }
//teszteléshez kell proto
    @Override
    public String getName() {
        return "camembert";
    }


    //Fogadja az ItemVisitor interface-t megvalósító karaktert és meghívja a Visit() függvényét saját magával,
    //a pickUp paraméter értékétől függően tárgy felvételre, vagy tárgy letevésre fog a Visit() hívódni
    @Override
    public void Accept(ItemVisitor ch, boolean pickUp) {
        ch.Visit(this, pickUp);
    }

    //A camembert használata
    @Override
    public void Use() {
        this.opened = true;
        if(GetRoom() != null){
            if(opened){
                Gassing(GetRoom());
                if(this.GetCharacter() != null && this.GetCharacter().GetProtectedAgainstGas() == 1){
                    this.GetCharacter().GetProtectiveItemAgainstGas().HandleGas();
                }
                if (this.GetCharacter() != null) {
                    this.GetCharacter().RemoveItemFromInventory(this);
                }
            }
            
        }
        
    }

    //Annak a szobának az elgázosítása ahol a camembert használják
    public void Gassing(Room r){
        r.MakePoisonous();
    }

    //Getter az opened attribútumhoz
    public boolean GetOpened(){
        return opened;
    }

    //Setter az opened attribútumhoz
    public void SetOpened(boolean opened){
        this.opened = opened;
    }

    @Override
    public void Step(int round) {
    }
}
