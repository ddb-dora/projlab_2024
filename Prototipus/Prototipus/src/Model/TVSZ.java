package Model;

public class TVSZ extends Item{

    //Azt méri, hogy hányszor használható még a TVSZ
    private int lives = 3;
    //Igazi vagy hamis a tárgy
    private boolean dummy;

    public TVSZ(boolean isDummy){
        dummy = isDummy;
    }

    //teszteléshez kell proto
    @Override
    public String getName() {
        return "tvsz";
    }

    public TVSZ(int lives, boolean isDummy){
        this.lives = lives;
        dummy = isDummy;
    }

    //Fogadja az ItemVisitor interface-t megvalósító karaktert és meghívja a Visit() függvényét saját magával,
    //a pickUp paraméter értékétől függően tárgy felvételre, vagy tárgy letevésre fog a Visit() hívódni
    @Override
    public void Accept(ItemVisitor ch, boolean pickUp) {
        ch.Visit(this, pickUp);
    }

    //A TVSZ használata
    @Override
    public void Use() {
        if(!dummy && this.GetCharacter().GetProtectedAgainstTecher() == 0){
            this.GetCharacter().SetProtectiveItemAgainstTeacher(this); 
            ProtectStudent(GetCharacter());
            
        }
    }

    //Student megvédése Teacher-rel szemben
    public void ProtectStudent(Character s){
        s.SetProtectedAgainstTeacher(1);
        //3 teszt
        HandleAttack();
    }

    //Egy oktatóval való találkozás lekezelése
    @Override
    public void HandleAttack(){
        if(lives > 0){
            DecreaseLives();
        }
        if(lives == 0){
            this.GetCharacter().SetProtectiveItemAgainstTeacher(null);
            this.GetCharacter().SetProtectedAgainstTeacher(0);
            this.GetCharacter().RemoveItemFromInventory(this);
        }
    }

    //A tárgy életeinek csökkentése eggyel
    public void DecreaseLives(){
        lives -=1;
    }

    @Override
    public void Step(int round) {
        
    }

    public boolean IsDummy() {
        return dummy;
    }

    public int getLives() {
        return lives;
    }
}
