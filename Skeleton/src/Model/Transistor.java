package Model;

public class Transistor extends Item{

    private Transistor connectedTo;

    //Ha a tranzisztor össze van kapcsolva egy másikkal akkor true az értéke, egyébként false
    private boolean connected;
    //Ha a tranzisztor használatban van, vagyis a közeljövőben teleportálásra akarjuk használni,
    //akkor true az értéke, egyébként false
    private boolean activated;

    //Fogadja az ItemVisitor interface-t megvalósító karaktert és meghívja a Visit() függvényét,
    //a pickUp paraméter értékétől függően tárgy felvételre, vagy tárgy letevésre fog a Visit() hívódni
    @Override
    public void Accept(ItemVisitor ch, boolean pickUp) {
        if(this.connected == false && pickUp == true){
            ch.Visit(this, pickUp);

            //Testprint
            System.out.println("Accept(ch, true) -> Transistor");
        }
        else
            System.out.println("Accept(ch, false) -> Transistor\n");
           
    }

    //A tranzisztor használata, beállítja az activated attribútumát true-ra
    @Override
    public void Use() {
        //Testprint
        System.out.print("Transistor\n" +
                "        ");
        SetActivation(true);
    }

    //Beállítja az összekacsolandó tranzisztork connected attribútumait true-ra
    public void ConnectTo(Transistor t){
        connectedTo = t;
        this.SetConnection(true);
        t.SetConnection(true);
    }

    //Setter az activated attribútumhoz
    public void SetActivation(boolean isActivated){
        activated = isActivated;
        //Testprint
        System.out.println("SetActivation(" + activated + ") -> Transistor");

    }

    //Setter a connected attribútumhoz
    public void SetConnection(boolean isConnected){
        connected = isConnected;
        //Testprint
        System.out.println("SetConnection(" + connected + ") -> Transistor");
    }

    //Getter az activated attribútumhoz
    public boolean GetActivation(){
        return activated;
    }

    //Getter a connected attribútumhoz
    public boolean GetConnected(){
        return connected;
    }


}
