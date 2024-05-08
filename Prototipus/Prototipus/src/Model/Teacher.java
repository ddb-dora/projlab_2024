package Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Teacher extends Character{

    //A Teacher le van-e bénítva
    private int paralyzed = 0;

    public Teacher(Room r) {
        super(r);
    }

    public Teacher(Room r, int uncon, int para) {
        super(r, uncon);
        paralyzed = para;
    }
    //A teacher mozgása, ha nem eszméletlen, nics lebénítva
    //és ha az a szoba ahova mozogni akar nincs tele
    @Override
    public void Move(Room r, boolean transistorMove) {
        if(r.GetCapacity() < r.GetMaxCapacity() && GetUnconscious() == 0 && paralyzed == 0 && GetRoom().HasNeigbor(r)){
            this.GetRoom().Remove(this);
            r.Accept(this);

            /*if(r.GetPoisonous()){
                Faint();
            }*/
        }
    }

    @Override
    public void UseItem(Item i) {
    }

    @Override
    public void Faint(){
        this.SetUnconscious();
        List<Item> temp = new ArrayList<>(GetItems());
        for(Item item : temp) PutDown(item);
    }

    //A Teacher-ek léptetése minden körben
    @Override
    public void Step(int round) {


        //Ideiglenes karakter lista, amit ráállítunk az eredeti karakterlistára és ezen iterálunk végig
        //Az ideiglenesen iterálunk, de az eredetit módosítjuk
        //Ez azért kell hogy elkerüljük a ConcurrentModificationException hibát
        List<Character> tempCharacterList = new ArrayList<>(this.GetRoom().GetCharacters());
        
        if(paralyzed == 0){

            Random random = new Random();

            //Random szobába mozgás
            if(getRandom(random) && !this.GetRoom().GetNeighbors().isEmpty()){
                this.Move(getRandomRoom(random), false);
            }
            //Random tárgyfelvétel
            if(getRandom(random)){
                this.PickUp(getRandomItemFromRoom(random));
            }
            //Random tárgylerakás
            if(getRandom(random)){
                this.PutDown(getRandomItemFromInventory(random));
            }

            for(Character ch : tempCharacterList){
                ch.Kill();
            }
        }
        else{
            DecreaseParalyzedTime();
        }
    }

    @Override
    public void Kill() {
    }

    //Beállítja a Teacher paralyzed attribútumát, amikor ronggyal megbénítják
    @Override
    public void SetParalyzed(int time) {
        paralyzed = time;
    }

    public int IsParalyzed(){
        return paralyzed;
    }

    public void DecreaseParalyzedTime(){
        paralyzed--;
    }

    @Override
    public void SetProtectedAgainstTeacher(int n) {
    }

    @Override
    public void SetProtectedAgainstGas(int n) {
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

    @Override
    public int GetProtectedAgainstTecher() {
        return 0;
    }

    @Override
    public int GetProtectedAgainstGas() {
        return 0;
    }

    @Override
    public void Leave(){

        Random random = new Random();
        if(this.GetUnconscious() == 0 && this.paralyzed == 0){
            Move(getRandomRoom(random), false);
        }
        
    }

}
