package Model;

public class Student extends Character{

    //Ha egy Student felveszi a Logarlec-et akkor true, addig false
    private static boolean hasLogarlec = false;
    //A Student-ek száma a labirintusban, ha 0-ra csökken,
    //akkor vége a játéknak és a Student-ek vesztenek
    private static int numberOfStudents;

    //Az méri, hogy mennyi ideig védett még a Student a Teacher-rel szemben
    private int protectedAgainstTeacher;
    //Az méri, hogy mennyi ideig védett még a Student gázos szobában a gázzal szemben
    private int protectedAgainstGas;

    public Student(Room r) {
        super(r);
    }

    //Ez hívódik meg, ha egy Student védtelenül egy szobába kerül egy Teacher-rel
    public void Die(){
        //TestPrint
        System.out.print("Die() -> Student");
        GetRoom().GetCharacters().remove(this);
    }

    //Beállítja a hasLogarlec attribútumot true-ra
    public void SetLogarlec(){
        hasLogarlec = true;
    }

    //Egy adott tárgy használatakor hívódik
    @Override
    public void UseItem(Item i) {
        //TestPrint
        System.out.print("Tester -> UseItem(item)\n" +
                         "    Use -> ");
        i.Use();
        
    }

    @Override
    public void Step() {
    }

    //Ez hívódik meg egy Teacher által, minden körben, azokra a Student-ekre,
    //akik az adott Teacher-rel egy szobában vannak
    @Override
    public void Notify() {
        //TestPrint
        System.out.print("Notify() -> Student\n" + 
                        "        ");
        if(protectedAgainstTeacher == 0){
            Die();
        }
    }

    @Override
    public void GetParalyzed() {
        
    }

    //Beállítja a Student protectedAgainstTeacher attribútumát
    @Override
    public void GetProtectedAgainstTeacher() {
        //TODO
    }

    //Beállítja a Student protectedAgainstGas attribútumát
    @Override
    public void GetProtectedAgainstGas() {
        //TODO
    }

    //A Student teleportál, ha a szoba ahova menni akar nincs tele.
    //Ha nincs tele, akkor a teleportálás sikeres és visszatér true-val,
    //ha tele van, akkor sikertelen és false-al tér vissza
    //Azért van visszatérési érték, hogy a szoba ahonnan elteleportálunk tudjon a teleportálás sikerességéről
    @Override
    public boolean Teleport() {
        if(this.GetLastTransistor().GetRoom().GetCapacity() < this.GetLastTransistor().GetRoom().GetMaxCapacity()){
            Move(this.GetLastTransistor().GetRoom());
            GetLastTransistor().SetActivation(false);
            GetLastTransistor().SetConnection(false);
            SetLastTransistor(null);
            System.out.println("Sikeres teleportálás");
            return true;
        }
        System.out.println("Sikertelen teleportálás, a kiválasztott szoba tele van");
        return false;
    }

    //Logarlec felvételekor ez hívódik meg, ha Student vette fel a tárgyat
    @Override
    public void handleOnPickedUpBy(Item item) {
        item.OnPickedUpBy(this);
    }
}
