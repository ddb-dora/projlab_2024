package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Student extends Character implements IStudent{

    //Ha egy Student felveszi a Logarlec-et akkor true, addig false
    private static boolean hasLogarlec = false;
    //A Student-ek száma a labirintusban, ha 0-ra csökken,
    //akkor vége a játéknak és a Student-ek vesztenek
    private static int numberOfStudents = 0;

    //Az méri, hogy mennyi ideig védett még a Student a Teacher-rel szemben
    private int protectedAgainstTeacher = 0;
    //Az méri, hogy mennyi ideig védett még a Student gázos szobában a gázzal szemben
    private int protectedAgainstGas;

    public Student(Room r) {
        super(r);
        numberOfStudents++;
    }
    public Student(Room r, int uncon) {
        super(r, uncon);
        numberOfStudents++;
    }
    //A Student mozgását valósítja meg
    public void Move(Room r, boolean transistorMove){
        if(r.GetCapacity() < r.GetMaxCapacity() && GetUnconscious() == 0 && !GetRoom().GetCursed() && (GetRoom().HasNeigbor(r) || transistorMove)){
            this.GetRoom().Remove(this);
            r.Accept(this);

            if(r.GetPoisonous()){
                if(this.protectedAgainstGas == 1){
                    GetProtectiveItemAgainstGas().HandleGas();
                }else{
                    Faint();
                }
            }
        }
    }

    //Ez hívódik meg, ha egy Student védtelenül egy szobába kerül egy Teacher-rel
    public void Die(){
        //Ha a hallgató meghal, eldobódnak az item-jei
        /*for (Item item : this.GetItems()){ 
            this.PutDown(item);
        }*/

        GetRoom().Remove(this);
        numberOfStudents--;
        System.out.println("Student died");
    }

    //Beállítja a hasLogarlec attribútumot true-ra
    public void SetLogarlec(){
        hasLogarlec = true;
    }

    //Egy adott tárgy használatakor hívódik
    @Override
    public void UseItem(Item i) {
        i.Use();

        
    }

    //A hallgatónál lévő tárgyak léptetése minden körben
    @Override
    public void Step(int round) {
        for(Item i : GetItems()){
            i.Step(round);
        }
    }

    //Ez hívódik meg egy Teacher által, minden körben, azokra a Student-ekre,
    //akik az adott Teacher-rel egy szobában vannak
    @Override
    public void Kill() {
        if(protectedAgainstTeacher == 0){
            Die();
        }
        else{
            GetProtectiveItemAgainstTeacher().HandleAttack();
        }
    }

    @Override
    public void SetParalyzed(int time) {
        
    }

    //Beállítja a Student protectedAgainstTeacher attribútumát
    @Override
    public void SetProtectedAgainstTeacher(int n) {
        protectedAgainstTeacher = 1;
    }

    //Beállítja a Student protectedAgainstGas attribútumátunc
    @Override
    public void SetProtectedAgainstGas(int n) {
        protectedAgainstGas = 1;
    }

    @Override
    public int GetProtectedAgainstTecher() {
        return  protectedAgainstTeacher;
    }
    @Override
    public int GetProtectedAgainstGas() {
        return protectedAgainstGas;
    }

    //A Student teleportál, ha a szoba ahova menni akar nincs tele.
    //Ha nincs tele, akkor a teleportálás sikeres és visszatér true-val,
    //ha tele van, akkor sikertelen és false-al tér vissza
    //Azért van visszatérési érték, hogy a szoba ahonnan elteleportálunk tudjon a teleportálás sikerességéről
    @Override
    public boolean Teleport() {
        if(this.GetLastTransistor().GetRoom().GetCapacity() < this.GetLastTransistor().GetRoom().GetMaxCapacity()){
            Move(this.GetLastTransistor().GetRoom(), true);
            GetLastTransistor().SetActivation(false);
            GetLastTransistor().SetConnection(false);
            SetLastTransistor(null);
            return true;
        }
        return false;
    }

    //Logarlec felvételekor ez hívódik meg, ha Student vette fel a tárgyat
    @Override
    public void handleOnPickedUpBy(Item item) {
        item.OnPickedUpBy(this);
    }
    @Override
    public void Faint() {
        //TODO kéne ide valami, hogy lefusson egy kör minden játékosra, hogy használja-e az eszközeit, stb.
        if(this.GetProtectedAgainstGas() == 0){
            this.SetUnconscious();
            List<Item> temp = new ArrayList<>(GetItems());
            for(Item item : temp) PutDown(item);
        }
        else{
            this.GetProtectiveItemAgainstGas().HandleGas();
        }
        
    }

    //A szoba elhagyása egy random szomszéd irányába
    @Override
    public void Leave(){

        Random random = new Random();
        if(this.GetUnconscious() == 0){
            Move(getRandomRoom(random), false);
        }
    }

    public int GetnumberOfStudents(){
        return numberOfStudents;
    }

    public boolean DoesStudentsHaveLogarlec(){
        return hasLogarlec;
    }
    
    
}
