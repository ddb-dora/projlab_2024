package Model;

public class Beer extends Item{

    //Azt méri, hogy az adott Student még mennyi ideig védett a Teacher-ektől
    private int time;

    //Fogadja az ItemVisitor interface-t megvalósító karaktert és meghívja a Visit() függvényét saját magával,
    //a pickUp paraméter értékétől függően tárgy felvételre, vagy tárgy letevésre fog a Visit() hívódni
    @Override
    public void Accept(ItemVisitor ch, boolean pickUp) {
        //TestPrint
        System.out.println("Accept(ch, " + pickUp + ") -> Beer");
        ch.Visit(this, pickUp);
    }

    //A sör használata
    @Override
    public void Use() {
        //Testprint
        System.out.print("Beer\n" +
                    "        " +
                    "ProtectStudent(student) -> Beer\n" + 
                    "    ");
        ProtectStudent(GetCharacter());
    }

    //Megvédi a sört használó Studentet a Teacher-ektől
    public void ProtectStudent(Character s){
        //TestPrint
        System.out.print("Student <- GetProtectedAgainstTeacher()\n" +
                        "        ");
        s.GetProtectedAgainstTeacher();
    }
}
