package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Character implements ItemVisitor {

    private Room room;
    private List<Item> items = new ArrayList<>();
    private Transistor lastTransistor;
    private Item protectiveItemAgainstGas;
    private Item protectiveItemAgainstTeacher;

    private int itemCapacity = 5;
    private int unconscious = 0;

    public Character(Room r){
        room = r;
        r.AddCharacter(this);
    }
    public Character(Room r, int unc){
        room = r;
        r.AddCharacter(this);
        unconscious = unc;
    }

    public void PickUp(Item i){
        if(i != null && i.GetRoom() != null && items.size() < itemCapacity && !i.GetRoom().GetSticky()){ //Sticky
                //room.GiveItemTo(this, i); 
                room.RemoveItemFromRoom(i);
                i.Accept(this, true); //Nem volt eredetileg
        }
    }

    public void PutDown(Item i){
        if(i != null){
            i.Accept(this, false);
        }
        
    }

    public void Move(Room r, boolean transistorMove){
        if((r.GetCapacity() < r.GetMaxCapacity() && (GetRoom().HasNeigbor(r) || transistorMove))){ //csak szomszedos szobaba lehet atmenni
            this.room.Remove(this);
            r.Accept(this);
            this.SetRoom(r);
        }
    }

    public void AddItemToInventory(Item i){
        if(items.size() < itemCapacity){// && !(i.GetRoom().GetSticky())){ //Ha a szoba ragacsos, nem veheti fel
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

    public void SetProtectiveItemAgainstGas(Item i){
        protectiveItemAgainstGas = i;
    }

    public void SetProtectiveItemAgainstTeacher(Item i){
        protectiveItemAgainstTeacher = i;
    }

    public Item GetProtectiveItemAgainstGas(){
        return protectiveItemAgainstGas;
    }

    public Item GetProtectiveItemAgainstTeacher(){
        return protectiveItemAgainstTeacher;
    }

    public List<Item> GetItems(){
        return items;
    }

//tesztelés, inventory kiírása proto
    public String getFormattedItems() {
        StringBuilder formattedItems = new StringBuilder();
        List<Item> items = GetItems();
        for (Item item : items) {
            formattedItems.append(item.getName()).append(",");
        }
        // vessző, space eltávolítása a végéről
        if (formattedItems.length() > 0) {
            formattedItems.setLength(formattedItems.length() - 1);
        }
        return formattedItems.toString();
    }





    ///Absztrakt függvények, amiket vagy a Student, vagy a Teacher vagy mindkettő implementál
    public abstract void UseItem(Item i); //Student
    public abstract void Step(int round); //Teacher
    public abstract void Kill(); //Student
    public abstract void SetParalyzed(int time); //Teacher
    public abstract void SetProtectedAgainstTeacher(int n); //Student
    public abstract void SetProtectedAgainstGas(int n); //Student
    public abstract boolean Teleport(); //Student
    public abstract void SetLogarlec(); //Student
    public abstract void handleOnPickedUpBy(Item item); //Student és Teacher
    public abstract int GetProtectedAgainstTecher();
    public abstract int GetProtectedAgainstGas();
    public abstract void Faint();

    @Override
    public void Visit(TVSZ i, boolean pickUp) {
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
    public void Visit(Beer i, boolean pickUp) {
        if(pickUp){
            this.AddItemToInventory(i);
            this.GetRoom().RemoveItemFromRoom(i);
        }
        else{
            i.SetActivation(false);
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
            i.SetActivation(false);
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
    public void Visit(Spray i, boolean pickUp) {
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
        //Ha a karakter fel akar fenni egy transistort, megteheti, ha az nincs 
        //sem aktivalva, sem osszekotve masikkal, ekkor bekerul a karakter inventorijaba es kikerul a room-bol
        if(pickUp && !i.GetActivation()){
            this.AddItemToInventory(i);
            this.GetRoom().RemoveItemFromRoom(i);
        }
        //Ha a tr. aktivalasra kerul felveve, akkor osszekotodik a masik tranzisztorral, ha van masik tranzisztor a karakternel
        else if(pickUp && i.GetActivation()){
            if(this.lastTransistor != null){
                i.ConnectTo(lastTransistor);
            }
            else{
                this.SetLastTransistor(i);
            }
            this.GetRoom().PlaceTransistor(i, this);
        }
        else if(!pickUp && !i.GetActivation()){
            this.GetRoom().AddItemToRoom(i);
            this.RemoveItemFromInventory(i);
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

    public int GetUnconscious(){
        return unconscious;
    }
    public void SetUnconscious(){
        this.unconscious = 2;
    }

    //Eggyel csökkenti azt az időt amíg a karakter le van bénulva
    public void DecreaseUnconsciousTime(){
        unconscious--;
    }

    public void Leave(){}

    //Random számot generál,
    //a véletlenszerűen történő eseményeknél lesz szerepe
    protected boolean getRandom(Random random){
        int i = random.nextInt(2) + 1; // Random int between 1 and 2
        if(i == 1){
            return true;
        }
        else{
            return false;
        }
    }

    //Random visszaad egy szomszédos szobát,
    //a véletlenszerűen történő eseményeknél lesz szerepe
    protected Room getRandomRoom(Random random){
        int i = random.nextInt(this.GetRoom().GetNeighbors().size()); // Random int between 1 and 2
        return this.GetRoom().GetNeighbors().get(i);
    }

    //Random visszaad egy tárgyat a karakter inventory-jából,
    //a véletlenszerűen történő eseményeknél lesz szerepe
    protected Item getRandomItemFromInventory(Random random){
        if(this.items.size() > 0){
            int i = random.nextInt(this.items.size()); // Random int between 1 and 2
            return this.items.get(i);
        }
        return null;
    }

    //Random visszaad egy tárgyat a szoba inventory-jából,
    //a véletlenszerűen történő eseményeknél lesz szerepe
    protected Item getRandomItemFromRoom(Random random){
        if(this.GetRoom().GetItems().size() > 0){
            int i = random.nextInt(this.GetRoom().GetItems().size()); // Random int between 1 and 2
            return this.GetRoom().GetItems().get(i);
        }
        return null;
        
    }
}
