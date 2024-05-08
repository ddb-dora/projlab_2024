package Model;

import java.util.ArrayList;
import java.util.List;

public abstract class Character implements ItemVisitor {

    private Room room;
    private List<Item> items = new ArrayList<>();
    private Transistor lastTransistor;

    private int itemCapacity = 5;
    private boolean unconscious = false;

    public Character(Room r){
        room = r;
        r.AddCharacter(this);
    }

    public void PickUp(Item i){
        if(items.size() < itemCapacity){
            //Test
            System.out.print("Tester -> PickUp(item)\n" +
                               "    " );
            room.GiveItemTo(this, i);
            
        }
    }

    public void PutDown(Item i){
        //Testprint
        System.out.print("PutDown(item)\n" +
                               "        ");
        i.Accept(this, false);
    }

    public void Move(Room r){
        System.out.println("Tester: Move(Room2) -> Student\n");
        if(r.GetCapacity() < r.GetMaxCapacity() && !unconscious){
            this.room.Remove(this);
            r.Accept(this);
            System.out.println( 
                                "    Remove(ch) -> Room1\n" + 
                                "    Accept(student) -> Room2\n");
            return;
        }
        System.out.println("Couldn't move, capacity full! or The character is unconscious!");
    }

    public void Faint(){
        this.unconscious = true;
        for(Item item : items) PutDown(item);
    }

    public void AddItemToInventory(Item i){
        if(items.size() < itemCapacity){
            items.add(i);
            i.SetCharacter(this);
            i.OnPickedUpBy(this);
        }
    }

    public void RemoveItemFromInventory(Item i){
        items.remove(i);
        i.SetCharacter(null);
    }

    public void SetLastTransistor(Transistor t){
        this.lastTransistor = t;
    }

    ///Absztrakt függvények, amiket vagy a Student, vagy a Teacher vagy mindkettő implementál
    public abstract void UseItem(Item i); //Student
    public abstract void Step(); //Teacher
    public abstract void Notify(); //Student
    public abstract void GetParalyzed(); //Teacher
    public abstract void GetProtectedAgainstTeacher(); //Student
    public abstract void GetProtectedAgainstGas(); //Student
    public abstract boolean Teleport(); //Student
    public abstract void SetLogarlec(); //Student
    public abstract void handleOnPickedUpBy(Item item); //Student és Teacher

    @Override
    public void Visit(TVSZ i, boolean pickUp) {
        if(pickUp){
            this.AddItemToInventory(i);
            this.GetRoom().RemoveItemFromRoom(i);
           // System.out.println("TVSZ felvéve!\n");
        }
        else{
            this.GetRoom().AddItemToRoom(i);
            this.RemoveItemFromInventory(i);
           // System.out.println("TVSZ letéve!\n");
        }
    }

    @Override
    public void Visit(Beer i, boolean pickUp) {
        if(pickUp){
            this.AddItemToInventory(i);
            this.GetRoom().RemoveItemFromRoom(i);
        }
        else{
            this.GetRoom().AddItemToRoom(i);
            this.RemoveItemFromInventory(i);
        }
    }

    @Override
    public void Visit(Sponge i, boolean pickUp) {
        if(pickUp){
            this.AddItemToInventory(i);
            this.GetRoom().RemoveItemFromRoom(i);
        }
        else{
            this.GetRoom().AddItemToRoom(i);
            this.RemoveItemFromInventory(i);
        }
    }

    @Override
    public void Visit(FFP2 i, boolean pickUp) {
        if(pickUp){
            this.AddItemToInventory(i);
            this.GetRoom().RemoveItemFromRoom(i);
        }
        else{
            this.GetRoom().AddItemToRoom(i);
            this.RemoveItemFromInventory(i);
        }
    }

    @Override
    public void Visit(Camembert i, boolean pickUp) {
        if(pickUp){
            this.AddItemToInventory(i);
            this.GetRoom().RemoveItemFromRoom(i);
        }
        else{
            this.GetRoom().AddItemToRoom(i);
            this.RemoveItemFromInventory(i);
        }
    }

    @Override
    public void Visit(Transistor i, boolean pickUp) {
        if(pickUp && !i.GetActivation()){
            this.AddItemToInventory(i);
            this.GetRoom().RemoveItemFromRoom(i);
        }
        else if(!pickUp && !i.GetActivation()){
            this.GetRoom().AddItemToRoom(i);
            this.RemoveItemFromInventory(i);
        }
        else if(pickUp && i.GetActivation()){
            if(this.lastTransistor != null){
                i.ConnectTo(lastTransistor);
            }
            else{
                this.SetLastTransistor(i);
            }
            this.GetRoom().PlaceTransistor(i, this);
        }
    }

    @Override
    public void Visit(Logarlec i, boolean pickUp) {
        if(pickUp){
            this.AddItemToInventory(i);
            this.GetRoom().RemoveItemFromRoom(i);
        }
        else{
            this.GetRoom().AddItemToRoom(i);
            this.RemoveItemFromInventory(i);
        }
    }

    public Room GetRoom(){
        return room;
    }

    public void SetRoom(Room r){
        room = r;
    }

    public Transistor GetLastTransistor(){
        return lastTransistor;
    }

    public boolean GetUnconscious(){
        return unconscious;
    }
    public void SetUnconscious(){
        this.unconscious = true;
    }
}
