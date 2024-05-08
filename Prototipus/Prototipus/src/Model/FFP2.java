package Model;

public class FFP2 extends Item{

    //Azt tartja nyilván, hogy hányszor használható még a maszk
    private int lives = 3;
    //Azt mére, hogy a maszk életenként mennyi ideig használható,
    //minél kevesebb a maszk élete, egy élet alatt annál rövidebb ideig véd
    private int time;
    //Igazi vagy hamis
    private boolean dummy;
    //Aktiválva van-e a tárgy
    private boolean activated = false;
    
    public FFP2(boolean isDummy){
        dummy = isDummy;
    }

    //teszteléshez kell proto
    @Override
    public String getName() {
        return "mask";
    }

    public FFP2(int lives, int time, boolean isDummy){
        this.lives = lives;
        this.time = time;
        dummy = isDummy;
    }

    //Fogadja az ItemVisitor interface-t megvalósító karaktert és meghívja a Visit() függvényét saját magával,
    //a pickUp paraméter értékétől függően tárgy felvételre, vagy tárgy letevésre fog a Visit() hívódni
    @Override
    public void Accept(ItemVisitor ch, boolean pickUp) {
        ch.Visit(this, pickUp);
    }

    //Az FFP2-es maszk használata, használatkor egyel csökken az élete
    @Override
    public void Use() {
        if(!dummy && this.GetCharacter().GetProtectedAgainstGas() == 0){
            SetActivation(true);
            SetTime();

            this.GetCharacter().SetProtectiveItemAgainstGas(this);
            ProtectStudent(GetCharacter());   
        }
    }

    //Megvédi az őt használót gázos szobában
    public void ProtectStudent(Character s){
        s.SetProtectedAgainstGas(1);
    }
    //Beállítja az activated attribútumot
    public void SetActivation(boolean isActivated){
        activated = isActivated;
    }
    //Beállítja a time attribútumot
    public void SetTime(){
        if(lives > 3) lives = 3;
        if(lives == 3){
            time = 10;
        }
        if(lives == 2){
            time = 5; 
        }
        if(lives == 1){
            time = 2;
        }
    }

    @Override
    public void Step(int round) {
        if(activated){
            if(time > 0){
                DecreaseTime();
            }
        }
    }
    //Eggyel csökkenti a lives  értékét
    public void DecreaseLives(){
        lives--;
    }
    //Eggyel csökkenti a time értékét
    public void DecreaseTime(){
        time--;
    }

    //Kezeli azt amikor a karakter gázos szobába ér és védve van egy FFP2 által
    @Override
    public void HandleGas(){
        if(time == 0){
            DecreaseLives();
            SetTime();
        }
        if(lives == 0){
            SetActivation(false);
            this.GetCharacter().SetProtectiveItemAgainstGas(this);
            this.GetCharacter().SetProtectedAgainstGas(0);
            this.GetCharacter().RemoveItemFromInventory(this);
        }
    }


    public int getLives() {
        return lives;
    }
    public int getTime() {
        return time;
    }

    public boolean isDummy() {
        return dummy;
    }

    public boolean isActivated() {
        return activated;
    }
}
