package Model;


public abstract class Item {

    //Az a karakter, akinél az adott tárgy van, ha a tárgy egy szobában van, akkor null
    private Character character;
    //Az a  szoba, amiben az adott tárgy van, ha a tárgy egy karakternél van, akkor null
    private Room room;

    //Absztrakt függvény tárgyak felvételéhez és letevéséhez
    public abstract void Accept(ItemVisitor ch, boolean pickUp);
    //Absztrakt függvény, az egyes tárgyak használatakor hívódik meg
    public abstract void Use();

    ///Logarléc esetében fontos függvények
    // A Character típusától függően delegál
    public void OnPickedUpBy(Character ch){
        ch.handleOnPickedUpBy(this);
    }
    //Ez a metódus kerül meghívásra, ha a Teacher objektum veszi fel az Item-et
    public void OnPickedUpBy(Teacher t){
    }
    //Ez a metódus kerül meghívásra, ha a Student objektum veszi fel az Item-et
    public void OnPickedUpBy(Student s){
    }

    //Getter a character attribútumhoz
    public Character GetCharacter(){
        return character;
    }

    //Getter a room attribútumhoz
    public Room GetRoom(){
        return room;
    }

    //Setter a character attribútum beállítására
    public void SetCharacter(Character ch){
            character = ch;
    }

    //Setter a room attribútum beállítására
    public void SetRoom(Room r){
        room = r;
    }
}
