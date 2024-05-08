package Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class Room implements IRoom{

    private List<Room> neighbors = new ArrayList<>();
    private List<Character> characters = new ArrayList<>();
    private List<Item> items = new ArrayList<>();

    //A szoba max kapacitása
    private int maxCapacity = 5;
    //A szoba jelenlegi kapacitása
    private int capacity = 0;
    //Ha a szoba el van gázosítva, akkor true, egyébként false
    private boolean poisonous = false;
    //Ha a szoba el van átkozva, akkor true, egyébként false
    private boolean cursed = false;
    //Ha a szoba ragacsos, akkor true, egyébként false
    private boolean sticky = false;
    //Ennyi karakter volt a szobában az utolsó takarítás óta
    private int charactersVisitedSinceCleaned = 0; 


    //A szoba beengedi magába a karaktert, ha van szabad hely
    public void Accept(Character ch){
        if(capacity < maxCapacity){
            this.characters.add(ch);
            this.capacity += 1;
            this.charactersVisitedSinceCleaned++;
            ch.SetRoom(this);
        }
        if(poisonous){
            if(ch.GetProtectedAgainstGas() == 0){
                while(!ch.GetItems().isEmpty()){
                    ch.PutDown(ch.GetItems().get(0));
                }
                ch.SetUnconscious();
            }
        }
    }

    //A szoba eltávolitja magából a karaktert
    public void Remove(Character ch){
        this.characters.remove(ch);
        capacity -= 1;
    }

    //Karakter hozzáadása a szobához manuálisan
    //A játék indításánállesz nagyobb szerepe
    public void AddCharacter(Character ch) {
        if(capacity + 1 > maxCapacity){
            try {
                throw new Exception("Too many students");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.characters.add(ch);
        capacity += 1;
    }
    

    public Room(int maxc,boolean pois, boolean curs, boolean stic){
        maxCapacity = maxc;
        poisonous = pois;
        cursed = curs;
        sticky = stic;
    }
    public Room() {
        //Alapértelmezett értékek
    }


    //Getter a maxCapacity attribútumhoz
    public int GetMaxCapacity(){
        return maxCapacity;
    }

    //Getter a capacity attribútumhoz
    public int GetCapacity(){
        return capacity;
    }

    //Getter a character listához
    public List<Character> GetCharacters(){
        return characters;
    }
    //Getter a szomszédaihoz
    public List<Room> GetNeighbors(){
        return neighbors;
    }
    //Getter az Itemekhez
    public List<Item> GetItems(){
        return items;
    }

//tesztelés, inventory kiírása proto
    public String getFormattedItems() {
        StringBuilder formattedItems = new StringBuilder();
        List<Item> items = GetItems();
        if (items != null) { 
            for (Item item : items) {
                formattedItems.append(item.getName()).append(",");
            }
            if (formattedItems.length() > 0) {
                formattedItems.setLength(formattedItems.length() - 1);
            }
        }
        return formattedItems.toString();
    }

    
    //Szomszéd hozzáadása a szobához
    //A játék indításánál lesz nagyobb szerepe
    public void SetNeighbor(Room r){
        neighbors.add(r);
    }

    //Igazzal tér vissza ha az adott szoba
    //rendelkezik ilyen szomszéddal
    public boolean HasNeigbor(Room r){
        return neighbors.contains(r);

    }

    //Eltávolítja a szomszédai közül a paraméteként kapott szobát
    public void RemoveNeighbor(Room r){
        neighbors.remove(r);
    }

    //Random számot generál,
    //a véletlenszerűen történő eseményeknél lesz szerepe
    private static boolean getRandom(Random random){
        int i = random.nextInt(2) + 1; // Random int between 1 and 2
        if(i == 1){
            return true;
        }
        else{
            return false;
        }
    }

    //A szoba léptetése
    public void Step(int round, Random random){

        //Ideiglenes karakter lista, amit ráállítunk az eredeti karakterlistára és ezen iterálunk végig
        //Az ideiglenesen iterálunk, de az eredetit módosítjuk
        //Ez azért kell hogy elkerüljük a ConcurrentModificationException hibát
        List<Character> tempCharacterList = new ArrayList<>(characters);
        
        if(random != null){
            //Random merge, separate, cursed
            if(getRandom(random)){
                if(!neighbors.isEmpty()){
                    int randomroom_num = random.nextInt(neighbors.size());
                    Room r = neighbors.get(randomroom_num);
                    MergeRooms(r);
                }
            }
            if(getRandom(random)){
                SeparateRoom(random);
            }

            if(getRandom(random)){
                SetCursed();
            }
        }
        
        if(poisonous){
            //If the room is poisonous the all characters faint
            for(Character ch : characters){
                ch.Faint();
            }
        }

System.out.println(charactersVisitedSinceCleaned);

        if(charactersVisitedSinceCleaned >= 5){
            SetSticky();
        }

        //Each character takes its turn
        for(Character ch : tempCharacterList){
            if(ch.GetUnconscious() > 0){
                ch.DecreaseUnconsciousTime();
            }
            else ch.Step(round);
        }
    }

    //Szoba kettéválása
    //A visszatérési értékre a Proto miatt van szükség
    public Room SeparateRoom(Random random){

        //Létrejön az új szoba, ami szomszédos lesz ezzel
        Room r = new Room();

        

        //A létrejövő szoba rendelkezni fog az eredeti tulajdonságaival
        if(poisonous)
            r.poisonous = true;

        if(cursed)
            r.cursed = true;
        r.maxCapacity = capacity;

        //Megkapja a másik szobát mint szomszéd
        r.neighbors.add(this);

        if(random != null){
            //Véletlenszerűen kap az új szoba szomszédokat
            for(Room r1 : neighbors){
                if(random != null && getRandom(random)){
                    r.neighbors.add(r1);
                    //Ha a másik szoba szomszédos ezzel
                    if(r1.HasNeigbor(this)){
                        r1.RemoveNeighbor(this);
                        r1.neighbors.add(r);
                    }
                }
            }
            //Eltávolítja a másik szobához sorolt szomszédokat
            if(!r.neighbors.isEmpty()){
                for(Room r1 : r.neighbors){
                    neighbors.remove(r1);
                }
            }

            //Véletlenszerűen kap az új szoba tárgyakat
            for(Item i1 : items){
                if(getRandom(random)){
                    r.items.add(i1);
                    i1.SetRoom(r);
                }
            }
            //Eltávolítja a másik szobához sorolt tárgyakat
            if(!r.items.isEmpty()){
                for(Item i1 : r.items){
                    items.remove(i1);
                }
            }
        }else{
            //Ha nincsen véletlenszerűség, akkor az összes szomszédot megkapja
            r.neighbors.addAll(neighbors);
        }
        
        neighbors.add(r);

        

        return r;
    }

    //Szobák egyesülése
    //Ezt a szobát egyesíti egy kapott másikkal
    public void MergeRooms(Room r){
        //Ha nem férnének el a karakterek, akkor nem egyesül a két szoba
        if(r.capacity + capacity > Math.max(r.maxCapacity, maxCapacity)){
            return;
        }
        //A nagyobb befogadóképességgel fog rendelkezni
        maxCapacity = Math.max(r.maxCapacity, maxCapacity);

        //Iterálás a másik szoba szomszédain
        for(Room r1 : neighbors){
            //Ha a másik szobának szomszédja(kétirányú ajtó)
            //Átállítja az asszociációt
            if(r1.HasNeigbor(r)){
                r1.RemoveNeighbor(r);
                r1.SetNeighbor(this);
            }
            //Ha ennek a szobának még nem szomszédja
            if(!neighbors.contains(r1)){
                neighbors.add(r1);
            }
        }

        //Minden a másik szobában található tárgy ebbe kerül
        for(Item i : r.GetItems()){
            i.SetRoom(this);
            items.add(i);
        }
        //Minden a másik szobában található character ebbe kerül
        for(Character c : r.characters){
            c.SetRoom(this);
        }
        characters.addAll(r.characters);
        capacity += r.capacity;

        //Ha mérgező vagy átkozott volt a másik
        if(r.poisonous)
            poisonous = true;
        
        if(r.cursed)
            cursed = true;
        
    }

    //A szoba elgázosításakor a poisonous attribútum true-ra állítása
    public void MakePoisonous(){
        poisonous = true;
    }
    //Getter a poisonous attribútumhoz
    public boolean GetPoisonous(){
        return poisonous;
    }
    //A szoba poisonous attribútumának false-ra állítása
    public void RefreshAir(){
        poisonous = false;
    }
    //A szoba sticky attribútumának false-ra állítása
    public void Clean(){
        sticky = false;
        charactersVisitedSinceCleaned = 0;
    }


    //A szoba sticky attribútumának true-ra állítása
    public void SetSticky(){
        sticky = true;
    }
    public boolean GetSticky(){
        return sticky;
    }


    //Getter a cursed attribútumhoz
    public boolean GetCursed(){
        return cursed;
    }
    //A szoba elátkozott attribútumának állítása
    public void SetCursed(){
        if(cursed == true) cursed = false;
        else cursed = true;
    }

    public void IncreaseCharactersVisited(){
        charactersVisitedSinceCleaned += 1;
    }

    //Egy tárgy karakternek való átadásának kezdeményezése, akkor, ha a szoba nem ragacsos,
    //a szoba hívja meg
    public void GiveItemTo(ItemVisitor v, Item i){
        if(sticky == false){
            int it = items.indexOf(i);
            items.get(it).Accept(v,true);
            RemoveItemFromRoom(i);
        }
    }

    //Tárgy eltávolítása a szobából,
    //a tárgy felvétele esetén
    public void RemoveItemFromRoom(Item i){
        items.remove(i);
        i.SetRoom(null);
    }

    //Tárgy hozzáadása a szobához,
    //a tárgy letevése esetén
    public void AddItemToRoom(Item i){
        items.add(i);
        i.SetRoom(this);
    }

    //Tranzisztor lehelyezése a szobában
    //Ha csatlakoztatott tranzisztort teszünk le akkor teleportálás is történik, 
    //különben csak le lesz téve (át lesz adva a szobának)
    //Javított, ha aktivált tranzistort teszünk le, + a student lastTransitor érték állítása ha csatlakozott
    public void PlaceTransistor(Transistor t, Character ch){
        this.AddItemToRoom(t);
        ch.RemoveItemFromInventory(t);
        if(ch.GetLastTransistor() == null && t.GetConnected()){
            ch.SetLastTransistor(t);
        }
        boolean succesful = false;
        if(t.GetActivation() && t.GetConnected()){
            succesful = ch.Teleport();
            System.out.println(succesful);
        }
        if(succesful){
            t.SetActivation(false);
            t.SetConnection(false);
        }
        
    }



}
