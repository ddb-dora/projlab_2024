package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Cleaner extends Character {
    
    public Cleaner(Room r) {
        super(r);
    }

    public void CleanRoom(Room r){
        r.Clean();
        r.RefreshAir();
    }

    

    @Override
    public void Step(int round) {

        //Ideiglenes karakter lista, amit ráállítunk az eredeti karakterlistára és ezen iterálunk végig
        //Az ideiglenesen iterálunk, de az eredetit módosítjuk
        //Ez azért kell hogy elkerüljük a ConcurrentModificationException hibát
        List<Character> tempCharacterList = new ArrayList<>(this.GetRoom().GetCharacters());
        
        Random random = new Random();

        //Random szobába mozgás ha vannak szomszédos szobák
        if(getRandom(random) && !this.GetRoom().GetNeighbors().isEmpty()){
            this.Move(getRandomRoom(random), false);
        }

        for(Character ch : tempCharacterList){
            ch.Leave();
        }

        CleanRoom(this.GetRoom());
    }

    @Override
    public void UseItem(Item i) {
    }

    

    @Override
    public void Kill() {
    }

    @Override
    public void SetParalyzed(int time) {
    }

    @Override
    public void SetProtectedAgainstTeacher(int n) {
    }

    @Override
    public void SetProtectedAgainstGas(int n) {
    }

    @Override
    public boolean Teleport() {
        return false;
    }

    @Override
    public void SetLogarlec() {
    }

    @Override
    public void handleOnPickedUpBy(Item item) {
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
    public void Faint() {
    }
}
