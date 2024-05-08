package Model;

import java.util.Random;

public class Beer extends Item{

    //Azt méri, hogy az adott Student még mennyi ideig védett a Teacher-ektől
    private int time;
    private boolean activated;

    //Konstruktor a Beer tárgyhoz.
    public Beer(){
        activated = false;
    }

//teszteléshez kell proto
@Override
public String getName() {
    return "beer";
}

    public Beer(int time){
        this.time = time;
        activated = false;
    }

    //Fogadja az ItemVisitor interface-t megvalósító karaktert és meghívja a Visit() függvényét saját magával,
    //a pickUp paraméter értékétől függően tárgy felvételre, vagy tárgy letevésre fog a Visit() hívódni
    @Override
    public void Accept(ItemVisitor ch, boolean pickUp) {
        ch.Visit(this, pickUp);
    }

    //A sör használata
    @Override
    public void Use() {
        SetActivation(true);
        //SetTime(5);
        ProtectStudent(GetCharacter());
        HandleAttack();
        Step(0);
    }

    //Megvédi a sört használó Studentet a Teacher-ektől
    public void ProtectStudent(Character s){
        s.SetProtectedAgainstTeacher(1);
    }

    //Aktivált Beer léptetését valósítja meg, minden körben eggyel csökkenti az időt ameddig a tárgy védelmet nyújt
    @Override
    public void Step(int round) {
        if(activated == true){
            if(time > 0){
                DecreaseTime();
            }
            if(time == 0){
                SetActivation(false);
                this.GetCharacter().SetProtectedAgainstTeacher(0);
                this.GetCharacter().RemoveItemFromInventory(this);
            }
        } 
    }

    //Eggyel csökkenti azt az időt amíg a tárgy védelmet nyújt
    public void DecreaseTime(){
        time -= 1;
    }

    //Beállítja a tárgy activated attribútumát true-ra
    public void SetActivation(boolean isActivated){
        activated = isActivated;
    }

    //Random visszaad egy tárgyat a karakter inventory-jából,
    //a véletlenszerűen történő eseményeknél lesz szerepe
    private Item getRandomItemFromInventory(Random random){
        int i = random.nextInt(this.GetCharacter().GetItems().size()); // Random int between 1 and 2
        return this.GetCharacter().GetItems().get(i);
    }

    //Egy oktatóval való találkozásnál a támadás lekezelését valósítja meg
    //Eldob véletlenszerűen egy tárgyat a karakter inventory-jából
    @Override
    public void HandleAttack(){
        Random random = new Random();
        Item itemToPutDown;                     
                                              //A letevésre szánt tárgy
        //itemToPutDown = this.GetCharacter().getRandomItemFromInventory(random);   
        if(this.GetCharacter().GetItems().size() >= 1){
            itemToPutDown = this.GetCharacter().GetItems().get(0);      //Kiválasztjuk a letevésre szánt tárgyat és eltároljuk egy változóban.
        //this.GetCharacter().GetRoom().AddItemToRoom(itemToPutDown);                     //Hozzáadjuk a szobához a tárgyat, amit leteszünk.
            this.GetCharacter().PutDown(itemToPutDown);                                     //Letesszük a tárgyat
        }

    }

    //Beállítja a tárgy time attribútumát a paraméterül kapott értékre
    public void SetTime(int n){
        time = n;
    }

    public int getTime(){
        return time;
    }

    public boolean getActivated(){
        return activated;
    }
}
