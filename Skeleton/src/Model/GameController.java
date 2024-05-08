package Model;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    private List<Room> rooms = new ArrayList<Room>();

    //Az aktuális kör száma
    private int currentRound = 0;
    //A maximális körök száma (tetszleges)
    private int maxRounds = 50;

    //A játék indítása
    public void StartGame() {
        //TODO
    }

    //A játék vége
    public void EndGame() {
        //TODO
    }

    //A játék léptetése
    public void Step(){
        //TestPrint
        System.out.print("Step()\n" +
                    "    ");
        SetCurrentRound();

        if(currentRound >= maxRounds){
            EndGame();
        }

        for(Room r : rooms){
            r.Step(currentRound);
        }
        AnyStudentsAlive();
    }

    //Az aktuális kör növelése
    public void SetCurrentRound(){
        currentRound += 1;
        //TestPrint
        System.out.print("SetCurrentRound(" + currentRound + ")-> GameController\n" +
                        "    ");

    }

    //Annak az ellenőrzése, hogy él-e még Student
    public void AnyStudentsAlive(){
        //TODO
        System.out.print("AnyStudentsAlive() -> GameController\n" +
                            "    ");
    }

    //Annak ellenőrzése, hogy valamelyik Student megszerezte-e már a Logarlecet
    public void StudentsHaveLogarlec(){
        //TODO, bár a labvez asszem jelezte, hogy ez nem fog működni így,
        //akkor TODO alternatív megoldás xD
    }
}
