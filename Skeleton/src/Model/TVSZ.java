package Model;

public class TVSZ extends Item{

    //Azt méri, hogy hányszor használható még a TVSZ
    private int lives = 3;

    //Fogadja az ItemVisitor interface-t megvalósító karaktert és meghívja a Visit() függvényét saját magával,
    //a pickUp paraméter értékétől függően tárgy felvételre, vagy tárgy letevésre fog a Visit() hívódni
    @Override
    public void Accept(ItemVisitor ch, boolean pickUp) {
        ch.Visit(this, pickUp);
        //TestPrint
        System.out.println("Accept(ch, " + pickUp + ") -> TVSZ");
    }

    //A TVSZ használata, használatkor az élete csökken eggyel
    @Override
    public void Use() {
        //Testprint
        System.out.print("TVSZ\n" +
                    "        ");
        if(lives > 0){
            //TestPrint
            System.out.print("ProtectStudent(student) -> TVSZ\n" + 
                            "    ");
            ProtectStudent(GetCharacter());
            //Testprint
            System.out.println("lives -= 1\n");
            lives -= 1;
        }
    }

    //Student megvédése Teacher-rel szemben
    public void ProtectStudent(Character s){
        //TestPrint
        System.out.print("Student <- GetProtectedAgainstTeacher()\n" +
                        "        ");
        s.GetProtectedAgainstTeacher();
    }
}
