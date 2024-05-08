package Model;

public class FFP2 extends Item{

    //Azt tartja nyilván, hogy hányszor használható még a maszk
    private int lives = 3;
    //Azt mére, hogy a maszk életenként mennyi ideig használható,
    //minél kevesebb a maszk élete, egy élet alatt annál rövidebb ideig véd
    private int time;

    //Fogadja az ItemVisitor interface-t megvalósító karaktert és meghívja a Visit() függvényét saját magával,
    //a pickUp paraméter értékétől függően tárgy felvételre, vagy tárgy letevésre fog a Visit() hívódni
    @Override
    public void Accept(ItemVisitor ch, boolean pickUp) {
        ch.Visit(this, pickUp);
          //TestPrint
          System.out.println("Accept(ch, " + pickUp + ") -> FFP2");
    }

    //Az FFP2-es maszk használata, használatkor egyel csökken az élete
    @Override
    public void Use() {
         //Testprint
         System.out.print("FFP2\n" +
         "        ");
        if(lives > 0){
            //TestPrint
            System.out.print("ProtectStudent(student) -> FFP2\n" + 
            "    ");
            ProtectStudent(GetCharacter());
            //Testprint
            System.out.println("lives -= 1\n");
            lives -= 1;
        }
    }

    //Megvédi az őt használót gázos szobában
    public void ProtectStudent(Character s){
        s.GetProtectedAgainstGas();
    }
}
