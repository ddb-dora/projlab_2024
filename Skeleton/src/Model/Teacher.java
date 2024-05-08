package Model;

public class Teacher extends Character{

    //A Teacher le van-e bénítva
    private boolean paralyzed = false;

    public Teacher(Room r) {
        super(r);
    }

    //A teacher mozgása, ha nem eszméletlen, nics lebénítva
    //és ha az a szoba ahova mozogni akar nincs tele
    @Override
    public void Move(Room r) {
        if(r.GetCapacity() < r.GetMaxCapacity() && !GetUnconscious() && !paralyzed){
            //Testhez
            System.out.println("Tester: Move(Room2) -> Teacher\n" + 
                                "    Remove(ch) -> Room1\n" + 
                                "    Accept(teacher) -> Room2\n");

            this.GetRoom().Remove(this);
            r.Accept(this);
            
            return;
        }
        System.out.println("Couldn't move, capacity full!");
    }

    @Override
    public void UseItem(Item i) {
    }

    //A Teacher-ek léptetése minden körben
    @Override
    public void Step() {
        //TODO
        System.out.println("Tester -> Step(round) -> Teacher\n");
    }

    @Override
    public void Notify() {
    }

    //Beállítja a Teacher paralyzed attribútumát true-ra, amikor ronggyal megbénítják
    @Override
    public void GetParalyzed() {
        paralyzed = true;
    }

    @Override
    public void GetProtectedAgainstTeacher() {
    }

    @Override
    public void GetProtectedAgainstGas() {
    }

    @Override
    public void SetLastTransistor(Transistor t) {
    }

    @Override
    public boolean Teleport() {
        return false;
    }

    @Override
    public void SetLogarlec() {
    }

    ////Logarlec felvételekor ez hívódik meg, ha Teacher vette fel a tárgyat
    @Override
    public void handleOnPickedUpBy(Item item) {
        item.OnPickedUpBy(this);
    }

}
