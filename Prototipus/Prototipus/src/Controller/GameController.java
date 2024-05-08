package Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Model.Beer;
import Model.Camembert;
import Model.Cleaner;
import Model.FFP2;
import Model.IRoom;
import Model.IStudent;
import Model.Logarlec;
import Model.Room;
import Model.Sponge;
import Model.Spray;
import Model.Student;
import Model.TVSZ;
import Model.Teacher;
import Model.Transistor;

public class GameController {

    //A szobák listája interfészekkel
    private List<IRoom> rooms = new ArrayList<>();
    //Az 1. játékos, amin keresztül eléri a statikus változókat
    private IStudent student;

    //Az aktuális kör száma
    private int currentRound = 0;
    //A maximális körök száma (tetszleges)
    private int maxRounds = 50;

    //Random objektum, melyet a Room step fgv-e is használ
    Random random = new Random();

    //A játék indítása
    public void StartGame() {
        CreateStandard();
        while(currentRound <= maxRounds && AnyStudentsAlive() && !StudentsHaveLogarlec()){
            Step();
        }
        EndGame();
    }

    //Standard pálya/objektumok létrehozása
    private void CreateStandard() {
        Room r = new Room();
        //Az első játékos az 1. szobában + egy maszk és transistor
        student = new Student(r);
        r.AddItemToRoom(new FFP2(false));
        r.AddItemToRoom(new Transistor());

        //Vele szomszédos szobában egy teacher és egy hamis TVSZ
        Room r1 = new Room();
        setNeighbours(r1, r);
        new Teacher(r1);
        r1.AddItemToRoom(new TVSZ(true));

        //A teacherrel szomszédosan egy cleaner és egy beer, sponge
        Room r2 = new Room();
        setNeighbours(r1, r2);
        new Cleaner(r2);
        r2.AddItemToRoom(new Beer());
        r2.AddItemToRoom(new Sponge());

        //Három az elsővel szomszédos szoba különböző itemekkel
        for(int i = 0; i < 3; i++){
            Room temp = new Room();
            if(i == 0){
                temp.AddItemToRoom(new Camembert());
                temp.AddItemToRoom(new Transistor());
            }else if(i == 1){
                temp.AddItemToRoom(new Spray());
            }else//Az utolsó szobában logarléc
            if(i == 2){
                temp.AddItemToRoom(new Logarlec(false));
            }
            setNeighbours(r, temp);
            rooms.add(temp);
        }

        Room r3 = new Room();
        new Student(r3);
        //Két egyirányú szomszéd
        r1.SetNeighbor(r3);
        r3.SetNeighbor(r2);
        //+ egy hamis logarlec
        r3.AddItemToRoom(new Logarlec(true));

        rooms.add(r);
        rooms.add(r1);
        rooms.add(r2);
        rooms.add(r3);
    }

    //Két szobának a szomszédságát állítja
    private void setNeighbours(Room r1, Room r2){
        r1.SetNeighbor(r2);
        r2.SetNeighbor(r1);
    }

    //A játék vége
    public void EndGame() {
        //Ha nincs életben maradt hallgató
        if(!AnyStudentsAlive() && currentRound != 0){
            System.out.println("Students lost");
        //Ha a hallgatók megszerezték a logarlécet
        }else if(StudentsHaveLogarlec()){
            System.out.println("Students win!");
        }
        //(Ha lejárt a körök száma) a játék vége
        System.out.println(currentRound);
        System.out.println("Game ended");
    }

    //A játék léptetése
    public void Step(){
        
        SetCurrentRound();

        for(IRoom r : rooms){
            r.Step(currentRound, random);
        }
    }

    //Az aktuális kör növelése
    public void SetCurrentRound(){
        currentRound += 1;
    }

    //Annak az ellenőrzése, hogy él-e még Student
    public boolean AnyStudentsAlive(){
        
        return student.GetnumberOfStudents() > 0;
    }

    //Annak ellenőrzése, hogy valamelyik Student megszerezte-e már a Logarlecet
    public boolean StudentsHaveLogarlec(){
        return student.DoesStudentsHaveLogarlec();
    }

    public int GetCurrentRound(){
        return currentRound;
    }

    public int GetMaxRounds(){
        return maxRounds;
    }

    public void SetMaxRounds(int n){
        maxRounds = n;
    }
}