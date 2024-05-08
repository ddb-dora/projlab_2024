package Model;

public class Logarlec extends Item{

    //Igazi vagy hamis a tárgy
    boolean dummy;

    public Logarlec(boolean isDummy){
        dummy = isDummy;
    }

    //teszteléshez kell proto
    @Override
    public String getName() {
        return "logarlec";
    }

    //Fogadja az ItemVisitor interface-t megvalósító karaktert és meghívja a Visit() függvényét saját magával,
    //a pickUp paraméter értékétől függően tárgy felvételre, vagy tárgy letevésre fog a Visit() hívódni
    @Override
    public void Accept(ItemVisitor ch, boolean pickUp) {
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
        SetCharacter(t);
        GetCharacter().PutDown(this);
        SetCharacter(null);
    }

    //Ha Student veszi fel, akkor beállítja a hasLogarléc attribútumát true-ra
    //és a Student-ek megnyerik a játékot
    @Override
    public void OnPickedUpBy(Student s){
        if(!dummy){
            SetCharacter(s);
            GetCharacter().SetLogarlec();
        }
    }

    @Override
    public void Step(int round) {
    }

    public boolean getStatus() {
        return dummy;
    }
}
