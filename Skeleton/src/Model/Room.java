package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Room {

    private List<Room> neighbors = new ArrayList<Room>();
    private List<Character> characters = new ArrayList<Character>();
    private List<Item> items = new ArrayList<Item>();

    //A szoba max kapacitása
    private int maxCapacity = 5;
    //A szoba jelenlegi kapacitása
    private int capacity;
    //Ha a szoba el van gázosítva, akkor true, egyébként false
    private boolean poisonous = false;
    //Ha a szoba el van átkozva, akkor true, egyébként false
    private boolean cursed = false;

    

    //A szoba beengedi magába a karaktert, ha van szabad hely
    public void Accept(Character ch){
        if(capacity < maxCapacity){
            this.characters.add(ch);
            this.capacity += 1;
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
        this.characters.add(ch);
        capacity += 1;
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
    private boolean getRandom(Random random){
        int i = random.nextInt(2) + 1; // Random int between 1 and 2
        if(i == 1){
            return true;
        }
        else{
            return false;
        }
    }

    //A szoba léptetése
    public void Step(int round){
        Random random = new Random();
        //Random merge, separate, cursed
        if(getRandom(random)){
            int randomroom_num = random.nextInt(neighbors.size());
            Room r = neighbors.get(randomroom_num);
            MergeRooms(r);
        }
        if(getRandom(random)){
            SeparateRoom();
        }

        if(getRandom(random)){
            SetCursed();
        }

        if(poisonous == true){
            //If the room is poisonous the all characters faint
            for(Character ch : characters){
                ch.Faint();
            }
        }
        //Each character takes its turn
        for(Character ch : characters){
            ch.Step();
        }
    }

    //Szoba kettéválása
    public void SeparateRoom(){
        System.out.print("SeparateRoom()");

        //Létrejön az új szoba, ami szomszédos lesz ezzel
        Room r = new Room();

        //A létrejövő szoba rendelkezni fog az eredeti tulajdonságaival
        if(poisonous)
            r.poisonous = true;

        if(cursed)
            r.cursed = true;
        r.maxCapacity = capacity;


        Random random = new Random();
        //Véletlenszerűen kap az új szoba szomszédokat
        for(Room r1 : neighbors){
            if(getRandom(random)){
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
        //Megkapja a másik szobát mint szomszéd
        r.neighbors.add(this);
        neighbors.add(r);
    }

    //Szobák egyesülése
    //Ezt a szobát egyesíti egy kapott másikkal
    public void MergeRooms(Room r){
        System.out.println("MergeRooms(room)\n" +
                        "    ");
        //Ha nem férnének el a karakterek, akkor nem egyesül a két szoba
        if(r.capacity + capacity > Math.max(r.maxCapacity, maxCapacity)){
            return;
        }
        //A nagyobb befogadóképességgel fog rendelkezni
        maxCapacity = Math.max(r.maxCapacity, maxCapacity);

        //Iterálás a másik szoba szomszédain
        for(Room r1 : r.GetNeighbors()){
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
        }
        items.addAll(r.GetItems());

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
    public void GetPoisonous(){
        poisonous = true;
    }

    //A szoba elátkozott attribútumának állítása
    public void SetCursed(){
        if(cursed == true) cursed = false;
        else cursed = true;
    }

    //Egy tárgy karakternek való átadásának kezdeményezése,
    //a szoba hívja meg
    public void GiveItemTo(ItemVisitor v, Item i){
        //TestPrint
        System.out.print("GiveItemTo(ch) -> Room\n" + 
                           "         ");
        int it = items.indexOf(i);
        items.get(it).Accept(v,true);
        RemoveItemFromRoom(i);

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
    public void PlaceTransistor(Transistor t, Character ch){
        this.AddItemToRoom(t);
        ch.RemoveItemFromInventory(t);
        boolean succesful = false;
        if(t.GetConnected()){
            succesful = ch.Teleport();
            System.out.println(succesful);
        }
        if(succesful){
            t.SetActivation(false);
            t.SetConnection(false);
        }
        
    }

}
