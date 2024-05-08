package Model;

public class Sponge extends Item{

    //Méri, hogy meddig bénít még a rongy
    private int time;

    //Fogadja az ItemVisitor interface-t megvalósító karaktert és meghívja a Visit() függvényét saját magával,
    //a pickUp paraméter értékétől függően tárgy felvételre, vagy tárgy letevésre fog a Visit() hívódni
    @Override
    public void Accept(ItemVisitor ch, boolean pickUp) {
        ch.Visit(this, pickUp);
         //TestPrint
         System.out.println("Accept(ch, " + pickUp + ") -> Sponge");
    }

    //A rongy használata
    @Override
    public void Use() {
        //Testprint
        System.out.print("Sponge\n" +
                    "        ");
        Paralyze();

    }

    //A rongy használójával egy szobában lévő Characterek megbénítása
    //A GetParalyzed() függvényt csak Teacher-ek valósítják meg, így csak azokra hat
    public void Paralyze(){
        for(Character ch : GetRoom().GetCharacters()){
            //TestPrint
            System.out.print("Teacher <- Paralyze()\n" +
                            "        ");
            ch.GetParalyzed();
        }
    }
}
