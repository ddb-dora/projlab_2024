package Model;

public class Sponge extends Item{

    //Méri, hogy meddig bénít még a rongy
    private int time;

    public Sponge(){
        time = 3;//default érték
    }

//teszteléshez kell proto
@Override
public String getName() {
    return "sponge";
}

    public Sponge(int time){
        this.time = time;
    }

    //Fogadja az ItemVisitor interface-t megvalósító karaktert és meghívja a Visit() függvényét saját magával,
    //a pickUp paraméter értékétől függően tárgy felvételre, vagy tárgy letevésre fog a Visit() hívódni
    @Override
    public void Accept(ItemVisitor ch, boolean pickUp) {
        ch.Visit(this, pickUp);
    }

    //A rongy használata
    @Override
    public void Use() {
        Paralyze();
        this.GetCharacter().RemoveItemFromInventory(this);
    }

    //Getter a time attribútumhoz
    public int GetTime(){
        return time;
    }

    //Setter a time attribútumhoz
    public void SetTime(int time){
        this.time = time;
    }

    //A rongy használójával egy szobában lévő Characterek megbénítása
    //A GetParalyzed() függvényt csak Teacher-ek valósítják meg, így csak azokra hat
    public void Paralyze(){
        for(Character ch : GetRoom().GetCharacters()){
            ch.SetParalyzed(time);
        }
    }

    @Override
    public void Step(int round) {
    }
}
