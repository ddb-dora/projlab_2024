package Skeleton;

import Model.Room;
import Model.Sponge;
import Model.Student;
import Model.Teacher;
import Model.TVSZ;
import Model.Beer;
import Model.Camembert;
import Model.GameController;
import Model.Logarlec;
import Model.FFP2;
import Model.Transistor;
import java.util.Scanner;


public class Main {
    public static void main(String args[]){
        int n = 80;
        Scanner sc = new Scanner(System.in);
        while(n != 0){
            System.out.println("\nWhich test would you like to run?\n" + 
                            "Give the number between 1 and 47!\n" +
                            "Choose 0 if you want to exit!\n");
            sc = new Scanner(System.in);
            n = sc.nextInt();

            switch(n){
                case 0:
                    break;
                case 1:
                    StudentMoves();
                    break;
                case 2:
                    StudentTriesToMoveIntoFullRoom();
                    break;

                case 3:
                    TeacherStepsIntoARoom();
                    break;
                case 4:
                    TeacherTriesToMoveIntoFullRoom();
                    break;
                case 5:
                    StudentPicksUpTransistor();
                    break;
                case 6:
                    StudentTriesToPickUpAnAlreadyConnectedTransistor();
                    break;
                case 7:
                    StudentPutsDownTransistorIfNotActivated();
                    break;
                case 8:
                    StudentPutsDownFirstTransistorIfActivated();
                    break;
                case 9:
                    StudentPutsDownSecondTransistorIfActivated();
                    break;
                case 10:
                    StudentTeleportsToFullRoom();
                    break;
                case 11:
                    StudentTeleports();
                    break;
                case 12:
                    StudentUsesTransistor();
                    break;
                case 13:
                    TeacherPicksUpTransistor();
                    break;
                case 14:
                    TeacherPutsDownTransistor();
                    break;
                
                case 15:
                    StudentPickTVSZUp();
                    break;
                case 16:
                    StudentPutsTVSZDown();
                    break;
                case 17:
                    StudentUsesTVSZ();
                    break;
                case 18: 
                    TeacherPicksTVSZUp();
                    break;
                case 19:    
                    TeacherPutsTVSZDown();
                    break;      
                case 20:
                    StudentPicksBeerUp();
                    break;
                case 21:
                    StudentPutsBeerDown();
                    break;
                case 22:
                    StudentUsesBeer();
                    break;
                case 23:
                    TeacherPicksBeerUp();
                    break;
                case 24:
                    TeacherPutsBeerDown();
                    break;
                case 25:
                    StudentPicksSpongeUp();
                    break;
                case 26:
                    StudentPutsSpongeDown();
                    break;
                case 27:
                    TeacherGetsStunnedWithSponge();
                    break;
                case 28:
                    StudentPicksCamembertUp();
                    break;
                case 29:
                    StudentPutsCamembertDown();
                    break;
                case 30:
                    StudentFaints();
                    break;
                case 31:
                    TeacherFaints();
                    break;
                case 32:
                    TeacherPicksCamembertUp();
                    break;
                case 33:
                    TeacherPutsCamembertDown();
                    break;
                case 34:
                    StudentPicksFFP2Up();
                    break;
                case 35:
                    StudentPutsFFP2Down();
                    break;
                case 36:
                    StudentUsesFFP2();
                    break;
                case 37:
                    TeacherPicksFFP2Up();
                    break;
                case 38: 
                    TeacherPutsFFP2Down();
                    break;
                case 39:
                    StudentPicksUpLogarlec();
                    break;
                case 40: 
                    TeacherPicksUpLogarlec();
                    break;
                case 41:
                    studentDies();
                    break;
                case 42:
                    studentTriesToMoveOutOfCursedRoom();
                    break;
                case 43:
                    gameSteps();
                    break;
                case 44: 
                    mergeRooms();
                    break;
                case 45:
                    splitRooms();
                    break;
                case 46: 
                    roomSteps();
                   // StudentTriesToMoveIntoFullRoom();
                    break;
                case 47: 
                   // StudentMoves(); 
                    teacherSteps();
                    break;

                default:
                    System.out.println("There is no test with such a number like that \n(remember that there are only 49 test).");
            }
        }
        sc.close();
        
    }
        //1. test: Student moves
    private static void StudentMoves(){
        Room r1 = new Room();
        Room r2 = new Room();
        r1.SetNeighbor(r2);
        r2.SetNeighbor(r1);

        Student s = new Student(r1);

        s.Move(r2);
    }
        //2. test: Student tries to move into full room
    private static void StudentTriesToMoveIntoFullRoom(){
        Room r1 = new Room();
        Room r2 = new Room();
        r1.SetNeighbor(r2);
        r2.SetNeighbor(r1);

        Student s = new Student(r1);
        Student s1 = new Student(r2);
        Student s2 = new Student(r2);
        Student s3 = new Student(r2);
        Student s4 = new Student(r2);
        Student s5 = new Student(r2);

        s.Move(r2);
    }
        //3. test: Teacher steps into a room
    private static void TeacherStepsIntoARoom(){
        Room room1 = new Room();
        Room room2 = new Room();
        Teacher teacher = new Teacher(room1);

        teacher.Step();
        teacher.Move(room2);
    }
        //4. test: Teacher tries to move into a full room
    private static void TeacherTriesToMoveIntoFullRoom(){
        Room room1 = new Room();
        Room room2 = new Room();
        Teacher teacher = new Teacher(room1);

        Student st1 = new Student(room2);
        Student st2 = new Student(room2);
        Student st3 = new Student(room2);
        Student st4 = new Student(room2);
        Student st5 = new Student(room2);

        teacher.Move(room2);
       
    }
        
        //5. test: Student picks up Transistor
    private static void StudentPicksUpTransistor(){
        Room room = new Room();
        Student student = new Student(room);
        Transistor tr = new Transistor();
        room.AddItemToRoom(tr);
        

        //pick up
        student.PickUp(tr);

    }
        //6. test: Student tries to pick up an already connected transistor
    private static void StudentTriesToPickUpAnAlreadyConnectedTransistor(){
        Room room1 = new Room();
        Room room2 = new Room();
        Transistor tr1 = new Transistor();
        Transistor tr2 = new Transistor();
        Student student = new Student(room1);
        tr1.ConnectTo(tr2);
        room2.AddItemToRoom(tr2);
        room1.AddItemToRoom(tr1);

        student.PickUp(tr1);
        System.out.println("Picking up has failed due to already existing connection!");
    }
        //7. test: Student puts down transistor if not activated
    private static void StudentPutsDownTransistorIfNotActivated(){
        Room room = new Room();
        Student student = new Student(room);
        room.Accept(student);
        room.AddCharacter(student);
        Transistor tr1 = new Transistor();
        student.AddItemToInventory(tr1);

        System.out.print("Tester -> ");
        student.PutDown(tr1);
    }
        //8. test: Student puts down first transistor if activated
    private static void StudentPutsDownFirstTransistorIfActivated(){
        Room room = new Room();
        Student student = new Student(room);
        room.AddCharacter(student);
        Transistor tr1 = new Transistor();
        Transistor tr2 = new Transistor();
        student.AddItemToInventory(tr1);
        student.AddItemToInventory(tr2);
        tr1.OnPickedUpBy(student);
        tr2.OnPickedUpBy(student);
        tr1.ConnectTo(tr2);

        student.UseItem(tr1);
        System.out.print("Tester -> ");
        student.PutDown(tr1);
    }
        //9. test: Student puts down second transistor if activated
    private static void StudentPutsDownSecondTransistorIfActivated(){
        Room room1 = new Room();
        Room room2 = new Room();
        Transistor tr1 = new Transistor();
        Transistor tr2 = new Transistor();
        Student student = new Student(room1);
        student.AddItemToInventory(tr1);
        student.AddItemToInventory(tr2);
        tr1.OnPickedUpBy(student);
        tr2.OnPickedUpBy(student);
        tr1.ConnectTo(tr2);
        student.PutDown(tr1);

        student.Move(room2);
        room2.Accept(student);
        room2.AddCharacter(student);
        
        System.out.print("Tester -> ");
        student.PutDown(tr2);

    }
        //10. test: Student teleports to full room
    private static void StudentTeleportsToFullRoom(){
        Room r1 = new Room();
        Room r2 = new Room();
        Student s = new Student(r1);

        Student s1 = new Student(r2);
        Student s2 = new Student(r2);
        Student s3 = new Student(r2);
        Student s4 = new Student(r2);
        Student s5 = new Student(r2);
        Transistor last = new Transistor();
        s.AddItemToInventory(last);
        System.out.print("Part of creation of the test, ignore: ");
        last.SetActivation(true);
        s.SetLastTransistor(last);
        last.SetRoom(r2);


        Transistor t = new Transistor();
        s.AddItemToInventory(t);
        System.out.print("Part of creation of the test, ignore: ");
        t.SetActivation(true);
        t.ConnectTo(s.GetLastTransistor());
        //r1.PlaceTransistor(t, s);
        s.Move(r2);
        
    }

        
        //11. test: Student teleports
    private static void StudentTeleports(){
        Room r1 = new Room();
        Room r2 = new Room();
        Student s = new Student(r1);

        Transistor last = new Transistor();
        s.AddItemToInventory(last);
        last.SetActivation(true);
        s.SetLastTransistor(last);
        r2.PlaceTransistor(last, s);

        Transistor t = new Transistor();
        s.AddItemToInventory(t);
        t.SetActivation(true);
        t.ConnectTo(s.GetLastTransistor());
        r1.PlaceTransistor(t, s);
    }
        //12. test: Student uses transistor
    private static void StudentUsesTransistor(){
        Room room = new Room();
        Student student = new Student(room);
        Transistor transistor = new Transistor();
        student.AddItemToInventory(transistor);
       
        student.UseItem(transistor);
        
    }
        //13. test: Teacher picks up Transistor
    private static void TeacherPicksUpTransistor(){
        Room room = new Room();
        Teacher teacher = new Teacher(room);
        Transistor transistor = new Transistor();
        teacher.AddItemToInventory(transistor);
        room.AddItemToRoom(transistor);
        

        //pick up
        teacher.PickUp(transistor);
    }
        //14. test Teacher puts down Transistor
    private static void TeacherPutsDownTransistor(){
        Room room = new Room();
        Teacher teacher = new Teacher(room);
        Transistor transistor = new Transistor();
        teacher.AddItemToInventory(transistor);        

        //puts down
        System.out.print("Tester -> ");
        teacher.PutDown(transistor);       

    }
        
        //15. test: Student picks tvsz up
    private static void StudentPickTVSZUp(){
            Room room = new Room();
            Student student = new Student(room);
            TVSZ tvsz = new TVSZ();
            
            room.AddItemToRoom(tvsz);
    
            student.PickUp(tvsz);
        }
        //16. test: Student puts tvsz down
    private static void StudentPutsTVSZDown(){
        Room room = new Room();
        Student student = new Student(room);
        TVSZ tvsz = new TVSZ();
        
        student.AddItemToInventory(tvsz);
        
        System.out.print("Tester -> ");
        student.PutDown(tvsz);
    }
        //17. Student uses TVSZ
    private static void StudentUsesTVSZ(){
        Room room = new Room();
        Student student = new Student(room);
        TVSZ tvsz = new TVSZ();
        
        student.AddItemToInventory(tvsz);

        student.UseItem(tvsz);
    }
        //18. test: Teacher picks TVSZ up
    private static void TeacherPicksTVSZUp(){
        Room room = new Room();
            Teacher teacher = new Teacher(room);
            TVSZ tvsz = new TVSZ();
            room.AddItemToRoom(tvsz);
    
            teacher.PickUp(tvsz);
    }
        //19. test: Teacher puts down TVSZ
    private static void TeacherPutsTVSZDown(){
        Room room = new Room();
        Teacher teacher = new Teacher(room);
        TVSZ tvsz = new TVSZ();
        
        teacher.AddItemToInventory(tvsz);
        
        System.out.print("Tester -> ");
        teacher.PutDown(tvsz);
    }
        //20. test: Student picks beer up
    private static void StudentPicksBeerUp(){
        Room room = new Room();
        Student student = new Student(room);
        Beer beer = new Beer();
        
        room.AddItemToRoom(beer);

        student.PickUp(beer);
    }
        //21. test: Student puts beer down
    private static void StudentPutsBeerDown(){
        Room room = new Room();
        Student student = new Student(room);
        Beer beer = new Beer();
        
        student.AddItemToInventory(beer);

        System.out.print("Tester -> ");
        student.PutDown(beer);
    }
        //22. test: Student uses beer
    private static void StudentUsesBeer(){
        Room room = new Room();
        Student student = new Student(room);
        Teacher teacher = new Teacher(room);
        Beer beer = new Beer();
        
        student.AddItemToInventory(beer);

        student.UseItem(beer);
    }
        //23. test: Teacher picks beer up
    private static void TeacherPicksBeerUp(){
        Room room = new Room();
        Teacher teacher = new Teacher(room);
        Beer beer = new Beer();
        room.AddItemToRoom(beer);

        teacher.PickUp(beer);
    }
        //24. test: Teacher puts beer down
    private static void TeacherPutsBeerDown(){
        Room room = new Room();
        Teacher teacher = new Teacher(room);
        Beer beer = new Beer();
        teacher.AddItemToInventory(beer);

        System.out.print("Tester -> ");
        teacher.PutDown(beer);
    }
        //25. test: Student picks Sponge up
    private static void StudentPicksSpongeUp(){
        Room room = new Room();
        Student student = new Student(room);
        Sponge sponge = new Sponge();
        room.AddItemToRoom(sponge);

        student.PickUp(sponge);
    }
        //26. test: Student puts Sponge down
    private static void StudentPutsSpongeDown(){
        Room room = new Room();
        Student student = new Student(room);
        Sponge sponge = new Sponge();
        student.AddItemToInventory(sponge);

        System.out.print("Tester -> ");
        student.PutDown(sponge);
    }
        //27. test: Teacher gets stunned with sponge
    private static void TeacherGetsStunnedWithSponge(){
        Room room = new Room();
        Student student = new Student(room);
        Sponge sponge = new Sponge();

        student.AddItemToInventory(sponge);
        sponge.SetRoom(room);
        Teacher teacher = new Teacher(room);

        student.UseItem(sponge);
    }
        //28. test: Student picks camembert up
    private static void StudentPicksCamembertUp(){
        Room room = new Room();
        Student student = new Student(room);
        Camembert camembert = new Camembert();
        room.AddItemToRoom(camembert);
        
        student.PickUp(camembert);
    }
        //29. test: Student puts camembert down
    private static void StudentPutsCamembertDown(){
        Room room = new Room();
        Student student = new Student(room);
        Camembert camembert = new Camembert();
        student.AddItemToInventory(camembert);
        
        System.out.print("Tester -> ");
        student.PutDown(camembert);
    }
        //30. test: Student faints
    private static void StudentFaints(){
        Room room = new Room();
        Student student = new Student(room);
        Beer beer = new Beer();
        Sponge sponge = new Sponge();

        student.AddItemToInventory(beer);
        student.AddItemToInventory(sponge);
        room.GetPoisonous();

        //GameController step fv-ének hatására elejt
        student.Faint();
    }
        //31.test: Teacher faints
    private static void TeacherFaints(){
        Room room = new Room();
        Teacher teacher = new Teacher(room);
        Beer beer = new Beer();
        Sponge sponge = new Sponge();

        teacher.AddItemToInventory(beer);
        teacher.AddItemToInventory(sponge);
        room.GetPoisonous();

        //GameController step fv-ének hatására elejt
        teacher.Faint();
    }
        //32. test: Teacher picks Camembert up
    private static void TeacherPicksCamembertUp(){
        Room room = new Room();
        Teacher teacher = new Teacher(room);
        Camembert camembert = new Camembert();
        room.AddItemToRoom(camembert);

        teacher.PickUp(camembert);
    }
        //33. test: Teacher puts Camembert down
    private static void TeacherPutsCamembertDown(){
        Room room = new Room();
        Teacher teacher = new Teacher(room);
        Camembert camembert = new Camembert();
        teacher.AddItemToInventory(camembert);

        System.out.print("Tester -> ");
        teacher.PutDown(camembert);
    }
        //34. test: Student picks FFP2 up
    private static void StudentPicksFFP2Up(){
        Room room = new Room();
        Student student = new Student(room);
        FFP2 ffp2 = new FFP2();
        room.AddItemToRoom(ffp2);

        //pick up
        student.PickUp(ffp2);
    }
        //35. test: Student puts FFP2 down
    private static void StudentPutsFFP2Down(){
        Room room = new Room();
        Student student = new Student(room);
        FFP2 ffp2 = new FFP2();

        student.AddItemToInventory(ffp2);
        System.out.print("Tester -> ");
        student.PutDown(ffp2);
    }
        //36. test: Student uses FFP2
    private static void StudentUsesFFP2(){
        Room room = new Room();
        Student student = new Student(room);
        FFP2 ffp2 = new FFP2();

        student.AddItemToInventory(ffp2);

        student.UseItem(ffp2);

    }
        //37. test: Teacher Picks FFP2 Up
    private static void TeacherPicksFFP2Up(){
        Room room = new Room();
        Teacher teacher = new Teacher(room);
        FFP2 ffp2 = new FFP2();
        room.AddItemToRoom(ffp2);

        teacher.PickUp(ffp2);

    }
        //38. test: Teacher puts FFP2 down
    private static void TeacherPutsFFP2Down(){
        Room room = new Room();
        Teacher teacher = new Teacher(room);
        FFP2 ffp2 = new FFP2();
        teacher.AddItemToInventory(ffp2);

        System.out.print("Tester -> ");
        teacher.PutDown(ffp2);

    }
        //39. test: Student Pick Up Logarléc
    private static void StudentPicksUpLogarlec(){
        Room room = new Room();
        Student student = new Student(room);
        Logarlec log = new Logarlec();
        room.AddItemToRoom(log);
        student.PickUp(log); 
    }
        //40. test: Teacher Picks up Logarléc
    private static void TeacherPicksUpLogarlec(){
        Room room = new Room();
        Teacher teacher = new Teacher(room);
        Logarlec log = new Logarlec();

        room.AddItemToRoom(log);
        teacher.PickUp(log); 

    }
        //41. test: Student dies
    private static void studentDies(){
        Room room = new Room();
        Student student = new Student(room);
        Teacher teacher = new Teacher(room);
        System.out.print("Tester -> Step(round) -> Teacher\n" +
                        "    ");
        student.Notify();

    }

        //42. test: Student tries to move out of cursed Room
    private static void studentTriesToMoveOutOfCursedRoom(){
        Room room1 = new Room();
        Room room2 = new Room();
        room1.SetNeighbor(room2);
        room2.SetNeighbor(room1);
        Student student = new Student(room1);
       
        room1.GetPoisonous();  
        student.SetUnconscious();    
        student.Move(room2);

    }

        //43. test: Game Steps 
    private static void gameSteps(){
        System.out.print("Tester -> ");
        GameController gc = new GameController();
        gc.Step();

    }

        //44. test: mergeRooms
    private static void mergeRooms(){
        Room room1 = new Room();
        Room room2 = new Room();

        Room roomN = new Room(); //szomszédos szoba
        TVSZ tvsz = new TVSZ();
        Student student = new Student(room1);
        
        room1.AddItemToRoom(tvsz);
        roomN.SetNeighbor(room1);
        room1.SetNeighbor(roomN);

        System.out.print("Tester -> ");
        room1.MergeRooms(room2);
    }

        //45. test: Split Rooms
    private static void splitRooms(){
        Room room = new Room();
        Camembert camembert = new Camembert();
        Teacher teacher = new Teacher(room);
        room.AddItemToRoom(camembert);
        System.out.print("Tester -> ");
        room.SeparateRoom();
    }

        //46. test: Room Steps 
    private static void roomSteps(){
        //Szobák inicializálása tanulókkal, tárgyakkal együtt.
        Room room1 = new Room();
        Room neighborRoom = new Room();
        Room mainRoom = new Room();
        Student student = new Student(neighborRoom);
        TVSZ tvsz = new TVSZ();

        neighborRoom.Accept(student);
        neighborRoom.AddCharacter(student);
        neighborRoom.SetNeighbor(room1);
        neighborRoom.AddItemToRoom(tvsz);

        neighborRoom.Step(1);
        System.out.println("neighborRoom.Step(round1)");
    }

        //47. test: Teacher Steps
    private static void teacherSteps(){
        //Létrehozunk 2 szobát, ahol az oktató mozoghat, illetve tárgyakat hozunk létre, hogy elhelyezhessük majd ezeket bennük
        Room room1 = new Room();
        Room room2 = new Room();
        TVSZ tvsz = new TVSZ();
        Camembert camembert = new Camembert();
        Beer beer = new Beer();

        //Elhelyezünk egy oktatót a szobában, akit mozgatni fogunk, illetve a szobákban a tárgyakat.
        //Az oktatónál már van egy camembert, azt betesszük az inventory-jába
        Teacher teacher = new Teacher(room1);
        room1.Accept(teacher);
        room1.AddCharacter(teacher);
        room1.AddItemToRoom(tvsz);
        room2.AddItemToRoom(beer);
        teacher.AddItemToInventory(camembert);
                
        //Move
        System.out.println("Do you want the teacher to move? (Yes/No)");    //Kérdés, válasz
        Scanner scan = new Scanner(System.in);                                //Input bekérés
        String answer = scan.nextLine();
        
        if(answer.equals("Yes")){                                    //Az az eset, amikor az oktató mozogni kezd
            System.out.println("teacher.Move(room2)");
            teacher.Move(room2);
        }
        else if(answer.equals("No")){                               //Az az eset, amikor az oktató marad a szobában
            System.out.println("The teacher stays in room1");
        }
        else System.out.println("That is not a correct option to answer."); //Hibás input
    }

}