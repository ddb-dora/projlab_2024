package Model;

public class Logarlec extends Item{

    //Fogadja az ItemVisitor interface-t megvalósító karaktert és meghívja a Visit() függvényét saját magával,
    //a pickUp paraméter értékétől függően tárgy felvételre, vagy tárgy letevésre fog a Visit() hívódni
    @Override
    public void Accept(ItemVisitor ch, boolean pickUp) {
         //TestPrint
         System.out.println("Accept(ch, " + pickUp + ") -> Logarléc");
        ch.Visit(this, pickUp);
       
    }

    //A Logarléc használata, nem történik benne semmi,
    //mert a Logarlécnek csak a felvétele számít
    @Override
    public void Use() {
    }

    ///Annak eldöntése, hogy a Logarlécet Student vagy Teacher vette fel
    //Ha Teacher vette fel, akkor azonnal lerakja a szobában
    @Override
    public void OnPickedUpBy(Teacher t){
        //TestPrint
        System.out.print("    OnPickedUpBy(teacher) -> Logarléc\n" +
                         "    Teacher <- PutDown(Logarléc)");
        SetCharacter(t);
        GetCharacter().PutDown(this);
        SetCharacter(null);
        System.out.println("Teacher vette fel, és el is dobta.\n");
    }

    //Ha Student veszi fel, akkor beállítja a hasLogarléc attribútumát true-ra
    //és a Student-ek megnyerik a játékot
    @Override
    public void OnPickedUpBy(Student s){
        SetCharacter(s);
        GetCharacter().SetLogarlec();
        System.out.println("Student vette fel és megnyeri a játékot.\n");
    }
}
