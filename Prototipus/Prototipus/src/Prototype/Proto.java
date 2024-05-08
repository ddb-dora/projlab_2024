package Prototype;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import Model.Beer;
import Model.Camembert;
import Model.Cleaner;
import Model.FFP2;
import Model.IStudent;
import Model.Item;
import Model.Logarlec;
import Model.Room;
import Model.Sponge;
import Model.Spray;
import Model.Student;
import Model.TVSZ;
import Model.Teacher;
import Model.Transistor;

public class Proto {

    //Az egyes használt objektumok és neveik azonos idvel
    List<Object> objects = new ArrayList<>();
    List<String> objectnames = new ArrayList<>();
    public List<Object> getObjects(){
        return objects;
    }

    //A parancs stringje
    String[] command;
    //Használt random objektum
    Random random = null;
    //Buffer tárolja az eddigi kimenetet
    StringBuilder sb = new StringBuilder(300);

    //A játék vége feltétel ellenőrzésre
    IStudent ProtoStudent = null;
    
    //Futtatja a prototípust
    public void Run(){
        //Addig olvassa a sorokat, amíg exit parancsot nem kap
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        command = line.split(" ");
        while(!command[0].equals("exit")){
            //Végrehajtja a kapott parancsot
            evaluateCommand(false);
            command = sc.nextLine().split(" ");
        }

    }

    //Meghívja a megfelelő függvényt, ami végrehajtja a command tagváltozóban levő parancsot
    private void evaluateCommand(boolean isFileInput){
        switch(command[0]){
            case "clear":
                //Törli a prototípus adatait
                Clear();
                break;
            case "save":
                //A megadott fájlnév mentén fájlba menti a buffert
                if(command.length > 1){
                    try {
                        Save(command[1]);
                    } catch (FileNotFoundException e) {
                        // File could not be opened
                        e.printStackTrace();
                    }
                }else{
                    System.out.println("Adj meg fajlnevet is");
                }
                break;
            case "load":
                //A végtelen betöltések elkerülése végett
                if(!isFileInput){
                    //Betölti és végrehajtja az adott nevű fájlból a parancsokat
                    if(command.length > 1){
                        Load(command[1]);
                    }else{
                        System.out.println("Adj meg fajlnevet is");
                    }
                }
                break;
            case "setRandom":
                //Beállítja a program random működését a paraméternek megfelelőenf
                if(command.length > 1){
                    setRandom(command[1]);
                }
                break;
            case "create":
                //Átlép a létrehizó függvénybe
                if(command.length > 1){
                    Create();
                }
                break;
            case "ls":
                //Listázza az eddigi parancs kimeneteket
                List();
                break;
            case "tick":
                //lépteti a játékot
                if(command.length > 1){
                    tick();
                }
                break;
            case "cmd":
                //Command végrehajtás
                if(command.length > 2){
                    cmd();
                }
                break;
            default:
                System.out.println("Invalid bemenet");
                break;
        }
    }

    public void Clear(){
        //stringbuilder és objectek ürítése
        sb = new StringBuilder(300);
        objects.clear();
        objectnames.clear();
    }
    //Átállítja a random objektumot
    public void setRandom(String bool){
        //Ha igaz új random példány létrehozása, ha hamis nullázás
        if(bool.equals("true")){
            random = new Random();
        }else if(bool.equals("false")){
            random = null;
        }
    }
    //Létrehoz egy szobát a parancsnak megfelelően
    private void CreateRoom(){
        // ha vannak opcionális paraméterek TODO cursed
        if(command.length >= 6){
            Room newRoom = new Room(Integer.parseInt(command[2]), Boolean.parseBoolean(command[3]), Boolean.parseBoolean(command[4]), Boolean.parseBoolean(command[5]));
            objects.add(newRoom);
            //Név tárolása egyedi, csak szobákhoz tartozó karakterrel
            objectnames.add("r"+objects.size());
            //folyamat naplózása bufferbe
            sb.append("#" + objects.size() + " room created: cap: " + newRoom.GetMaxCapacity() +
                        " p: " + newRoom.GetPoisonous() + " c: " + newRoom.GetCursed() + " s: " + newRoom.GetSticky() +"\n");
            //System.out.println(sb.toString());
        }else{
            objects.add(new Room());
            objectnames.add("r"+objects.size());
            sb.append("#" + objects.size() + " room created: cap: 5 p: false c: false s: false\n");
            //System.out.println(sb.toString());
        }
    }
    //Létrehoz egy tanulót a parancsnak megfelelően
    private void CreateStudent(){
        if(command.length >= 5){
            //Number of students to be added
            int num = Integer.parseInt(command[2]);
            for(int i = 0; i < num; i++){
                //Creating and adding a student
                Student newStudent = new Student((Room)objects.get(Integer.parseInt(command[3]) - 1), intValue(Boolean.parseBoolean(command[4])));
                objects.add(newStudent);
                if(ProtoStudent == null)
                    ProtoStudent = newStudent;
                //Név tárolása egyedi, csak tanulókhoz tartozó karakterrel
                objectnames.add("s"+objects.size());
                //folyamat naplózása bufferbe
                if (newStudent.GetUnconscious() == 0){
                    sb.append("#" + objects.size() + " student created: n: " + command[2] +
                        " r: " + command[3] + " u: false\n");
                }else{
                    sb.append("#" + objects.size() + " student created: n: " + command[2] +
                        " r: " + command[3] + " u: true\n");
                }
            }
        }else{
            //default argumentumokkal
            Student newStudent = new Student((Room)objects.get(0));
            objects.add(newStudent);
            objectnames.add("s"+objects.size());
            if(ProtoStudent == null)
                    ProtoStudent = newStudent;
            //folyamat naplózása bufferbe
            sb.append("#" + objects.size() + " student created: n: 1 r: 1 u: false\n");
        }
    }
    private int intValue(boolean b) {
        return b ? 1 : 0;
    }

    //Létrehoz egy oktatót a parancsnak megfelelően
    private void CreateTeacher(){
        if(command.length >= 6){
            //Number of teachers to be added
            int num = Integer.parseInt(command[2]);
            for(int i = 0; i < num; i++){
                //Creating and adding a teacher
                Teacher newTeacher = new Teacher((Room)objects.get(Integer.parseInt(command[3]) - 1), intValue(Boolean.parseBoolean(command[4])), intValue(Boolean.parseBoolean(command[5])));
                objects.add(newTeacher);
                //Név tárolása egyedi, csak oktatókhoz tartozó karakterrel
                objectnames.add("t"+objects.size());
                //folyamat naplózása bufferbe
                if (newTeacher.GetUnconscious() == 0 && newTeacher.IsParalyzed() == 0){
                    sb.append("#" + objects.size() + " teacher created: n: " + command[2] +
                    " r: " + command[3] + " u: false p: false\n");
                }else if (newTeacher.GetUnconscious() == 0 && newTeacher.IsParalyzed() != 0){   
                    sb.append("#" + objects.size() + " teacher created: n: " + command[2] +
                    " r: " + command[3] + " u: false p: true\n");
                }else if (newTeacher.GetUnconscious() != 0 && newTeacher.IsParalyzed() == 0){
                    sb.append("#" + objects.size() + " teacher created: n: " + command[2] +
                    " r: " + command[3] + " u: true p: false\n");
                }else{
                    sb.append("#" + objects.size() + " teacher created: n: " + command[2] +
                    " r: " + command[3] + " u: true p: true\n");
                }
            }
        }else{
            //default argumentumokkal
            objects.add(new Teacher((Room)objects.get(0)));
            objectnames.add("t"+objects.size());
            sb.append("#" + objects.size() + " teacher created: n: 1 r: 1 u: false p: false\n");
        }
    }
    private void CreateCleaner() {
        if(command.length >= 4){
            //Number of cleaners to be added
            int num = Integer.parseInt(command[2]);
            for(int i = 0; i < num; i++){
                //Creating and adding a student
                Cleaner newCleaner = new Cleaner((Room)objects.get(Integer.parseInt(command[3]) - 1));
                objects.add(newCleaner);
                //Név tárolása egyedi, csak tanulókhoz tartozó karakterrel
                objectnames.add("cl"+ objects.size());
                //folyamat naplózása bufferbe
                sb.append("#" + objects.size() + " cleaner created: n: " + command[2] +
                        " r: " + command[3] + "\n");
            }
        }else{
            //default argumentumokkal
            objects.add(new Cleaner((Room)objects.get(0)));
            objectnames.add("cl"+objects.size());
            //folyamat naplózása bufferbe
            sb.append("#" + objects.size() + " cleaner created: n: 1 r: 1\n");
        }
    }

    //Ez a függvény hívódik az evaluateCommand-ból létrehozó parancs esetén
    //Ez hívja az egyes létrehozó függvényeket
    public void Create(){
        //A parancs 2. paramétere alapján create vagy default vagy egyedi argumentumokkal
        switch (command[1]) {
            case "room":
                CreateRoom();
                break;
            case "student":
                CreateStudent();
                break;
            case "teacher":
                CreateTeacher();
                break;
            case "cleaner":
                CreateCleaner();
                break;
            case "transistor":
                //Ha vannak opcionális argumentumok
                if(command.length >= 4){
                    //Létrejön az arg. nak megfelelő tranzisztor
                    Transistor tr = new Transistor(Boolean.parseBoolean(command[2]),Boolean.parseBoolean(command[3]));
                    //Hozzáadjuk, majd naplózzuk
                    objects.add(tr);
                    objectnames.add("tr"+objects.size());
                    sb.append("#" + objects.size() + " transistor created: c: " + tr.GetConnected() +
                        " a: " + tr.GetActivation() +"\n");
                }else{
                    //Default tranzisztor jön létre, majd naplózzuk
                    objects.add(new Transistor());
                    objectnames.add("tr"+objects.size());
                    sb.append("#" + objects.size() + " transistor created: c: false a: false" +"\n");
                }
                break;
            case "sponge":
                //Ha vannak opcionális argumentumok
                if(command.length >= 3){
                    //Létrejön az arg. nak megfelelő sponge
                    //Hiányzik a Sponge time attr.
                    Sponge s = new Sponge(Integer.parseInt(command[2]));
                    //Hozzáadjuk, majd naplózzuk
                    objects.add(s);
                    objectnames.add("sp"+ objects.size());
                    sb.append("#" + objects.size() + " sponge created: l: " + s.GetTime() +"\n");
                }else{
                    //Default sponge jön létre, majd naplózzuk
                    objects.add(new Sponge());
                    objectnames.add("sp"+objects.size());
                    sb.append("#" + objects.size() + " sponge created: l: 3" +"\n");
                }
                break;
            case "camembert":
                //Ha vannak opcionális argumentumok
                if(command.length >= 3){
                    //Létrejön az arg. nak megfelelő camembert
                    //Hiányzik a parancsok szerinti opened attribútum
                    Camembert c = new Camembert(Boolean.parseBoolean(command[2]));
                    //Hozzáadjuk, majd naplózzuk
                    objects.add(c);
                    objectnames.add("c"+ objects.size());
                    sb.append("#" + objects.size() + " camembert created: o: " + c.GetOpened() +"\n");
                }else{
                    //Default camembert jön létre, majd naplózzuk
                    objects.add(new Camembert());
                    objectnames.add("c"+objects.size());
                    sb.append("#" + objects.size() + " camembert created: o: false" +"\n");
                }
                break;
            case "mask":
                //Ha vannak opcionális argumentumok
                if(command.length >= 5){
                    //Létrejön az arg. nak megfelelő mask
                    FFP2 m = new FFP2(Integer.parseInt(command[2]), Integer.parseInt(command[3]), Boolean.parseBoolean(command[4]));
                    //Hozzáadjuk, majd naplózzuk
                    objects.add(m);
                    objectnames.add("m"+ objects.size());
                    sb.append("#" + objects.size() + " mask created: l: " + command[2] + " t: " + command[3] + " d: " + command[4] +"\n");
                }else{
                    //Default mask jön létre, majd naplózzuk
                    objects.add(new FFP2(3, 3, false));
                    objectnames.add("m"+objects.size());
                    sb.append("#" + objects.size() + " mask created: l: 3 t: 3 d: false" +"\n");
                }
                break;
            case "beer":
                //Ha vannak opcionális argumentumok
                if(command.length >= 3){
                    //Létrejön az arg. nak megfelelő beer
                    Beer b = new Beer(Integer.parseInt(command[2]));
                    //Hozzáadjuk, majd naplózzuk
                    objects.add(b);
                    objectnames.add("b"+ objects.size());
                    sb.append("#" + objects.size() + " beer created: t: " + command[2] +"\n");
                }else{
                    //Default beer jön létre, majd naplózzuk
                    objects.add(new Beer(3));
                    objectnames.add("b"+objects.size());
                    sb.append("#" + objects.size() + " beer created: t: 3" +"\n");
                }
                break;
            case "spray":
                //Default spray jön létre, majd naplózzuk
                objects.add(new Spray());
                objectnames.add("spr"+objects.size());
                sb.append("#" + objects.size() + " spray created" +"\n");
                break;
            case "tvsz":
                //Ha vannak opcionális argumentumok
                if(command.length >= 4){
                    //Létrejön az arg. nak megfelelő tvsz
                    TVSZ t = new TVSZ(Integer.parseInt(command[2]), Boolean.parseBoolean(command[3]));
                    //Hozzáadjuk, majd naplózzuk
                    objects.add(t);
                    objectnames.add("v"+ objects.size());
                    sb.append("#" + objects.size() + " tvsz created: t: " + command[2] + " d: " + command[3] +"\n");
                }else{
                    //Default TVSZ jön létre, majd naplózzuk
                    objects.add(new TVSZ(false));
                    objectnames.add("v"+objects.size());
                    sb.append("#" + objects.size() + " tvsz created: t: 3 d: false" +"\n");
                }
                break;
            case "logarlec":
                //Ha vannak opcionális argumentumok
                if(command.length >= 3){
                    //Létrejön az arg. nak megfelelő logarlec
                    Logarlec l = new Logarlec(Boolean.parseBoolean(command[2]));
                    //Hozzáadjuk, majd naplózzuk
                    objects.add(l);
                    objectnames.add("l"+ objects.size());
                    sb.append("#" + objects.size() + " logarlec created: d: " + command[2] +"\n");
                }else{
                    //Default logarlec jön létre, majd naplózzuk
                    objects.add(new Logarlec(false));
                    objectnames.add("l"+objects.size());
                    sb.append("#" + objects.size() + " logarlec created: d: false" +"\n");
                }
                break;
            default:
                System.out.println("Invalid argumentum");
                break;
        }
    }

    //Kiírja az eddig végzett műveletekhez tartozó buffer tartalmát
    public void List(){
    //módosítottam, csak a #-el kezdődőket kell h kiírja
        String[] lines = sb.toString().split("\n");
        for (String line : lines) {
            if (line.startsWith("#")) {
                System.out.println(line);
            }
        }
    }
    //A kapott fájlnévre elmenti a buffer tartalmát, kivételt dob, ha sikertelen
    public void Save(String filename) throws FileNotFoundException{
        try(PrintWriter out = new PrintWriter(filename)){
            out.write(sb.toString());
        }
    }
    //A kapott fájlnévről betölti a fájlban levő parancsokat és futtatja
    public void Load(String filename){
        //Új file objektum a fájlnévből
        File file = new File(filename);
        try {
            //Megnyitja egy scanner
            Scanner sc = new Scanner(file);
            //Amíg van új sor a fileban addig olvas, ha nincs exit parancs
            while(sc.hasNextLine()){
                command = sc.nextLine().split(" ");
                if(!command[0].equals("exit")){
                    //Kiértékeli a parancsot(futtatja)
                    evaluateCommand(true);
                }else{
                    return;
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            // File cannot be opened
            e.printStackTrace();
        }

    }
    //A cmd parancs kezeléséért felelől függvény
    public void cmd(){
        int idx = Integer.parseInt(command[1]);
        String id = objectnames.get(idx - 1);
        //A parancs 2. paramétere alapján típus castolás és művelet
        //Azok az objectumok, melyek neve r-el kezdődik, azokról tudjuk, hogy szobák t az oktató... stb.
        //A Create fgv. alapján
        //A parancs 3. paramétere command[3] az elvégzendő művelet
        if(id.startsWith("r")){
            //A kiválasztott szoba objektum a parancs id alapján
            Room teacher = (Room)objects.get(idx - 1);
            //Szomszédos szobák id-jának listázása
            if(command[2].equals("neighbours")){
                //A választott szobának szomszédainak listája
                List<Room> neighbours = teacher.GetNeighbors();
                int ix;
                Room currentNeighbour;
                //Ha van szomszédos
                //if(!neighbours.isEmpty()){
                /*for(int i = 0; i < neighbours.size(); i++){
                    //A keresett id megtalálása és kiírása
                    currentNeighbour = neighbours.get(i);
                    ix = objects.indexOf(currentNeighbour);
        
                    //System.out.print(ix+1);
                }*/
                //while(!neighbours.isEmpty()){
                for(int i = 0; i < neighbours.size(); i++){
                    //A keresett id megtalálása és kiírása
                    currentNeighbour = neighbours.get(i);
                    ix = objects.indexOf(currentNeighbour);
                    sb.append(ix+1 + " ");
                    System.out.print(ix+1 + " ");
                }
                //Starting New lines
                sb.append("\n");
                System.out.print("\n");
            }else if(command[2].equals("setNeighbours")){
                //A parancsban megadott id-vel rendelkező szobák hozzáadása a szomszédokhoz
                for(int i = 3; i < command.length; i++){
                    //Room newNeighbour = (Room)objects.get(Integer.parseInt(command[i]) - 1);
                    //teacher.SetNeighbor(newNeighbour);
                    teacher.SetNeighbor((Room)objects.get(Integer.parseInt(command[i]) - 1));
                }

            }else if(command[2].equals("inventory")){
                //A szoba tárgyainak listázása
                String roomItems = teacher.getFormattedItems();
                if (!roomItems.isEmpty()) {
                    sb.append(roomItems + "\n");
                    System.out.println(roomItems);
                } else {
                    System.out.println("Room has no items.");
                }

            } else if(command[2].equals("status")){
                sb.append("room: cap: " + teacher.GetMaxCapacity() + " p: " + teacher.GetPoisonous() + " c: " + teacher.GetCursed() +" s: " + teacher.GetSticky() + "\n");
                System.out.println("room: cap: " + teacher.GetMaxCapacity() + " p: " + teacher.GetPoisonous() + " c: " + teacher.GetCursed() +" s: " + teacher.GetSticky()+ "\n");
            } 
            
            else if(command[2].equals("merge")){
                //A megadott extra szoba mergelése a választottal
                Room tobeMerged = (Room)objects.get(Integer.parseInt(command[3]) - 1);
                teacher.MergeRooms(tobeMerged);
            }else if(command[2].equals("split")){
                //Split a kiválasztott randomitással
                /*Room newRoom = teacher.SeparateRoom(random);

                objects.add(newRoom);
                objectnames.add("r"+objects.size());*/
                Room newRoom = teacher.SeparateRoom(random);
                //teszthez 18
                objects.add(newRoom);
                //Név tárolása egyedi, csak szobákhoz tartozó karakterrel
                objectnames.add("r"+objects.size());
                //folyamat naplózása bufferbe
                sb.append("#" + objects.size() + " room created: cap: " + newRoom.GetMaxCapacity() +
                        " p: " + newRoom.GetPoisonous() + " c: " + newRoom.GetCursed() + " s: " + newRoom.GetSticky() +"\n");

                
            }
            
        }
        else if(id.startsWith("t")){
            if(id.startsWith("tr")){
                //Tranzisztor parancsok
                if(command.length >= 3) {
                    int transistorId = Integer.parseInt(command[1]);
                    Transistor transistor = (Transistor) objects.get(transistorId - 1);

                    switch(command[2]) {
                        case "connect":
                            if(command.length >= 4) {
                                // mivel kapcsoljuk össze a tranzisztort
                                int targetTransistorId = Integer.parseInt(command[3]);
                                Transistor targetTransistor = (Transistor) objects.get(targetTransistorId - 1);
                                // Összekapcsoljuk a tranzisztorokat
                                transistor.ConnectTo(targetTransistor);
                            } else {
                                System.out.println("Invalid command usage. Usage: connect <transistorId>");
                            }
                            break;
                        case "activate":
                            // Aktiváljuk a tranzisztort
                            transistor.Use();
                            break;
                        case "add":
                            if(command.length >= 4) {
                                // character vagy room id
                                int itemId = Integer.parseInt(command[3]);
                                Object addToWhat = objects.get(itemId - 1);

                                    if (addToWhat instanceof Student) {
                                        Student student = (Student) addToWhat;
                                        student.AddItemToInventory(transistor);
                                    } else if (addToWhat instanceof Room) {
                                        Room room = (Room) addToWhat;
                                        room.AddItemToRoom(transistor);
                                    } else if (addToWhat instanceof Teacher) {
                                        Teacher teacher = (Teacher) addToWhat;
                                        //teacher.AddItemToInventory(transistor);
                                        teacher.PickUp(transistor);
                                    } else {
                                        // Invalid object type
                                        System.out.println("Invalid object type. Can only add character or room.");
                                    }

                            } else {
                                System.out.println("Invalid command usage. Usage: add <characterId>|<roomId>");
                            }
                            break;
                        case "status":
                            sb.append("transistor: c: " + transistor.GetConnected() + " a: " + transistor.GetActivation() + "\n");
                            System.out.println("transistor: c: " + transistor.GetConnected() + " a: " + transistor.GetActivation()+ "\n");

                            break;
                        default:
                            System.out.println("Invalid command for transistor");
                            break;
                    }
                } else {
                    System.out.println("Invalid command usage. Usage: transistor <transistorId> <command>");
                }

            }else{
                //Oktató parancsok
                Teacher teacher = (Teacher) objects.get(idx - 1);
                int ix;
                switch(command[2]) {
                    case "move":
                        if(command.length >= 4) {
                            int roomId = Integer.parseInt(command[3]);
                            Room target = (Room) objects.get(roomId - 1);
                            teacher.Move(target, false);
                            //Ha a mozgatás sikeres volt
                            if(teacher.GetRoom().equals(target)){
                                //System.out.println("Teacher moved to room " + roomId);
                                //Értesítjük a karaktereket
                                List<Model.Character> temp = new ArrayList<>(target.GetCharacters());
                                for (Model.Character ch : temp) {
                                    //3. teszt

                                    ch.Kill();
                                }
                            }
                            //tick();
                            //Ha sikertelenül mozog az oktató, akkor nincs kiírás
                        } else {
                            System.out.println("Invalid command usage. Usage: move <roomId>");
                        }

                        break;
                    case "pickup":
                        if (command.length >= 4) {
                            int itemId = Integer.parseInt(command[3]);
                            if (itemId >= 1 && itemId <= objects.size()) {
                                Item itemToPickUp = (Item) objects.get(itemId - 1);
                                Room teacherRoom = teacher.GetRoom();
                                if (teacherRoom != null && teacherRoom.GetItems().contains(itemToPickUp)) {
                                    teacher.PickUp(itemToPickUp);
                                    System.out.println("Picking up item: " + itemId + " - " + itemToPickUp.getName());
                                } else {
                                    System.out.println("The specified item is not in the room.");
                                }
                            } else {
                                System.out.println("Invalid item ID.");
                            }
                        } else {
                            System.out.println("Invalid command usage. Usage: pickup <itemId>");
                        }
                        break;
                    case "inventory":
                        String teacherItems = teacher.getFormattedItems();
                        if (!teacherItems.isEmpty()) {
                            sb.append(teacherItems + "\n");
                            System.out.println(teacherItems);
                        } else {
                            System.out.println("Teacher has no items.");
                        }
                        break;
                    case "room":
                        ix = objects.indexOf(teacher.GetRoom());
                        sb.append(ix+1 + "\n");
                        System.out.println(ix+1);
                        break;
                    case "status":
                        ix = objects.indexOf(teacher.GetRoom());
                        int help = ix+1;
                        if(teacher.GetUnconscious() == 0 && teacher.IsParalyzed()==0){
                            sb.append("teacher: r: "+ help + " u: false p: false" + "\n");
                            System.out.println("teacher: r: "+ help + " u: false p: false" + "\n");
                        } else if(teacher.GetUnconscious() == 0 && teacher.IsParalyzed()!=0){
                            sb.append("teacher: r: "+ help + " u: false p: true" + "\n");
                            System.out.println("teacher: r: "+ help + " u: false p: true" + "\n");
                        } else if (teacher.GetUnconscious() != 0 && teacher.IsParalyzed()==0){
                            sb.append("teacher: r: "+ help + " u: true p: false" + "\n");
                            System.out.println("teacher: r: "+ help + " u: true p: false" + "\n");
                        } else {
                            sb.append("teacher: r: "+ help + " u: true p: true" + "\n");
                            System.out.println("teacher: r: "+ help + " u: true p: true" + "\n");
                        }
                        break;
                    case "remove":
                        if(command.length >= 4) {
                            //ha több elemet is akarunk törölni
                            List<Integer> itemIds = new ArrayList<>();
                            for(int i = 3; i < command.length; i++) {
                                itemIds.add(Integer.parseInt(command[i])-1);
                            }
                            for(int i = 0; i < itemIds.size(); i++) {
                                Item item = (Item) objects.get(itemIds.get(i));
                                teacher.RemoveItemFromInventory(item);
                            }
                        } else {
                            System.out.println("Invalid command usage. Usage: remove <itemId> <itemId> ...");
                        }
                        break;
                    default:
                        System.out.println("Invalid command for teacher");
                        break;
                }
            }
        }
        else if(id.startsWith("s")){
            if(id.startsWith("sp")){
                if(id.startsWith("spr")){
                    //Spray parancsok
                    if(command.length >= 3) {
                        int sprayId = Integer.parseInt(command[1]);
                        Spray spray = (Spray) objects.get(sprayId - 1);
                        switch(command[2]) {
                            case "add":
                                if(command.length >= 4) {
                                    // mihez adjuk hozzá a spray-t
                                    int itemId = Integer.parseInt(command[3]);
                                    // character vagy room id
                                    Object addToWhat = objects.get(itemId - 1);

                                    if (addToWhat instanceof Student) {
                                        Student student = (Student) addToWhat;
                                        //student.AddItemToInventory(spray);
                                        student.PickUp(spray);
                                    } else if (addToWhat instanceof Room) {
                                        Room room = (Room) addToWhat;
                                        room.AddItemToRoom(spray);
                                    } else if (addToWhat instanceof Teacher) {
                                        Teacher teacher = (Teacher) addToWhat;
                                        teacher.AddItemToInventory(spray);
                                    } else {
                                        // Invalid object type
                                        System.out.println("Invalid object type. Can only add character or room.");
                                    }
                                } else {
                                    System.out.println("Invalid command usage. Usage: add <characterId|roomId>");
                                }
                                break;
                            case "clean":
                                if(command.length >= 4) {
                                    int roomId = Integer.parseInt(command[3]);
                                    Room room = (Room) objects.get(roomId - 1);
                                    // Kitakarítja a szobát
                                    spray.Use(room);
                                } else {
                                    System.out.println("Invalid command usage. Usage: clean <roomId>");
                                }
                                break;
                            default:
                                System.out.println("Invalid command for spray");
                                break;
                        }
                    } else {
                        System.out.println("Invalid command usage. Usage: spray <command>");
                    }

                }
                else{
                    //Sponge parancsok
                    if (command.length >= 3) {
                        int spongeId = Integer.parseInt(command[1]);
                        Sponge sponge = (Sponge) objects.get(spongeId - 1);
        
                        switch (command[2]) {
                            case "paralyze":
                                if (command.length >= 4) {
                                // Itt a Paralyze() metódusnak nem kell a teachert megadni paraméterként, tuti jó úgy?
                                    int teacherId = Integer.parseInt(command[3]);
                                    Teacher teacher = (Teacher) objects.get(teacherId - 1);
                                    teacher.SetParalyzed(3);
                                    //sponge.Paralyze();
                                } else {
                                    System.out.println("Invalid command usage. Usage: paralyze <teacherId>");
                                }
                                break;
                            case "add":
                                if (command.length >= 4) {
                                    // character vagy room id
                                    int itemId = Integer.parseInt(command[3]);
                                    // character vagy room id
                                    Object addToWhat = objects.get(itemId - 1);
    
                                    if (addToWhat instanceof Student) {
                                        Student student = (Student) addToWhat;
                                        student.AddItemToInventory(sponge);
                                    } else if (addToWhat instanceof Room) {
                                        Room room = (Room) addToWhat;
                                        room.AddItemToRoom(sponge);
                                    } else if (addToWhat instanceof Teacher) {
                                        Teacher teacher = (Teacher) addToWhat;
                                        teacher.AddItemToInventory(sponge);
                                    } else {
                                        // Invalid object type
                                        System.out.println("Invalid object type. Can only add character or room.");
                                    }
                                } else {
                                    System.out.println("Invalid command usage. Usage: add <characterId|roomId>");
                                }
                                break;
                            case "status":
                                sb.append("sponge: l: " + sponge.GetTime() + "\n");
                                System.out.println("sponge: l: " + sponge.GetTime() + "\n");
                                break;
                            case "use":
                                sponge.Use();
                                break;
                            default:
                                System.out.println("Invalid command for Sponge");
                                break;
                        }
                    } else {
                        System.out.println("Invalid command usage. Usage: sponge <command>");
                    }
                }
            }else{
                //Student parancsok
                if (command.length >= 3) {
                    int studentId = Integer.parseInt(command[1]);
                    Student student = (Student) objects.get(studentId - 1);
                    int ix;
               
                    switch (command[2]) {
                        case "move":
                            if (command.length >= 4) {                        
                                int roomId = Integer.parseInt(command[3]);
                                Room target = (Room) objects.get(roomId - 1); 
                                student.Move(target, false);
                                //Ha teli szoba miatt nem tud mozogni
                                if(target.GetCapacity() == target.GetMaxCapacity()){
                                    System.out.println("Room is full!"); 
                                }

                                //Ha sikeres a mozgás Room Id-nek lekérdezése
                                // if(student.GetRoom().equals(target)){
                                //     System.out.println("Student moved to room " + (objects.indexOf(target) + 1));
                                // }
                                if (student.GetRoom().equals(target)) {
                                    System.out.println("Student moved to room " + roomId);
                                } else {
                                    System.out.println("Failed to move to room " + roomId);
                                }



                            } else {
                                System.out.println("Invalid command usage. Usage: move <roomId>");
                            }
                            break;

                        case "pickup":
                            if (command.length >= 4) {
                                int itemId = Integer.parseInt(command[3]);
                                if (itemId >= 1 && itemId <= objects.size()) {
                                    Item itemToPickUp = (Item) objects.get(itemId - 1);
                                    Room studentRoom = student.GetRoom();
                                    if (studentRoom != null && studentRoom.GetItems().contains(itemToPickUp)) {
                                        student.PickUp(itemToPickUp);
                                        System.out.println("Picking up item: " + itemId + " - " + itemToPickUp.getName());
                                    } else {
                                        System.out.println("The specified item is not in the room.");
                                    }
                                } else {
                                    System.out.println("Invalid item ID.");
                                }
                            } else {
                                System.out.println("Invalid command usage. Usage: pickup <itemId>");
                            }
                            break;
                        case "room":
                            // kiírja hol van a tanuló
                                ix = objects.indexOf(student.GetRoom());
                                sb.append(ix+1 + "\n");
                                System.out.println(ix+1);
                            break;
                        case "paralyze":
                            if (command.length >= 4) {
                                int teacherId = Integer.parseInt(command[3]);
                                Teacher teacher = (Teacher) objects.get(teacherId - 1);
                                // Paralyze teacher, de nem tudom melyik metódus állítja ezt (studentben/teacherben?)
                                teacher.SetParalyzed(3);
                            } else {
                                System.out.println("Invalid command usage. Usage: paralyze <teacherId>");
                            }
                            break;
                        case "inventory":
                            // kiírja a tanuló tárgyait
                            String studentItems = student.getFormattedItems();
                            if (!studentItems.isEmpty()) {
                                sb.append(studentItems + "\n");
                                System.out.println(studentItems);
                            } else {
                                System.out.println("Student has no items.");
                            }
                            break;
                        case "status":
                            // student állapotának kiírása
                            ix = objects.indexOf(student.GetRoom());
                            int help = ix+1;
                            if(student.GetUnconscious() == 0){

                                sb.append("student: r: "+ help + " u: false");
                                System.out.println("student: r: "+ help + " u: false");
                            } else {

                                sb.append("student: r: "+ help + " u: true");
                                System.out.println("student: r: "+ help + " u: true");
                            }
                            break;
                        case "place":
                            if (command.length >= 4) {
                                int transistorId = Integer.parseInt(command[3]);
                                Transistor transistor = (Transistor) objects.get(transistorId - 1);
                                // A tranzisztort a jelenlegi szobába helyezi
                                student.GetRoom().PlaceTransistor(transistor, student);

                                // Deaktiváljuk a tranzisztort
                                transistor.SetActivation(false);
                            } else {
                                System.out.println("Invalid command usage. Usage: place <transistorId>");
                            }
                            break;
                        case "remove":
                            if(command.length >= 4) {
                                //ha több elemet is akarunk törölni
                                List<Integer> itemIds = new ArrayList<>();
                                for(int i = 3; i < command.length; i++) {
                                    itemIds.add(Integer.parseInt(command[i])-1);
                                }
                                for(int i = 0; i < itemIds.size(); i++) {
                                    Item item = (Item) objects.get(itemIds.get(i));
                                    student.RemoveItemFromInventory(item);
                                }
                            } else {
                                System.out.println("Invalid command usage. Usage: remove <itemId> <itemId> ...");
                            }
                            break;
                        default:
                            System.out.println("Invalid command for Student");
                            break;
                    }
                } else {
                    System.out.println("Invalid command usage. Usage: student <studentId> <command>");
                }
            }
        }

        else if(id.startsWith("c")){
            if(id.startsWith("cl")){
                //Cleaner parancsok
                if (command.length >= 3) {
                    int cleanerId = Integer.parseInt(command[1]);
                    Cleaner cleaner = (Cleaner) objects.get(cleanerId - 1);

                    switch (command[2]) {
                        case "move":
                            if (command.length >= 4) {
                                int roomId = Integer.parseInt(command[3]);
                                Room room = (Room) objects.get(roomId - 1);

                                cleaner.Move(room, false);
                                //Ha a mozgás sikeres volt
                                if(cleaner.GetRoom().equals(room)){
                                    
                                    List<Model.Character> tempCharacterList = new ArrayList<>(room.GetCharacters());

                                    for(Model.Character ch : tempCharacterList){
                                        ch.Leave();
                                    }
                                }
                            } else {
                                System.out.println("Invalid command usage. Usage: move <roomId>");
                            }
                            break;
                        case "clean":
                            if (command.length >= 4) {
                                // Kitakarítja a szobát
                                cleaner.CleanRoom(cleaner.GetRoom());
                            } else {
                                System.out.println("Invalid command usage. Usage: clean <roomId>");
                            }
                            break;
                        case "room":
                            cleaner.GetRoom();
                            break;
                        default:
                            System.out.println("Invalid command for Cleaner");
                            break;
                    }
                } else {
                    System.out.println("Invalid command usage. Usage: cleaner <command>");
                }


            }else{
                //Camembert parancsok
                if (command.length >= 3) {
                    int camembertId = Integer.parseInt(command[1]);
                    Camembert camembert = (Camembert) objects.get(camembertId - 1);

                    switch (command[2]) {
                        case "gassing":
                            if (command.length >= 4) {
                                int roomId = Integer.parseInt(command[3]);
                                Room room = (Room) objects.get(roomId - 1);
                                camembert.SetRoom(room);
                                // elgázosítja a szobát
                                camembert.Use();

                                List<Model.Character> characters = new ArrayList<>(room.GetCharacters());
                                //If the room is poisonous the all characters faint
                                for(Model.Character ch : characters){
                                    //if(!camembert.GetCharacter().equals(ch)){
                                        ch.Faint();
                                    //}
                                }
                            } else {
                                System.out.println("Invalid command usage. Usage: gassing <roomId>");
                            }
                            
                            break;
                        case "add":
                            // Check if the command has enough parameters
                            if (command.length >= 4) {
                                // character vagy room id
                                int itemId = Integer.parseInt(command[3]);
                                // character vagy room id
                                Object addToWhat = objects.get(itemId - 1);
                                if (addToWhat instanceof Student) {
                                    Student student = (Student) addToWhat;
                                    student.AddItemToInventory(camembert);
                        
                                } else if (addToWhat instanceof Room) {
                                    Room room = (Room) addToWhat;
                                    room.AddItemToRoom(camembert);
                                } else if (addToWhat instanceof Teacher) {
                                    Teacher teacher = (Teacher) addToWhat;
                                    teacher.AddItemToInventory(camembert);
                                } else {
                                    // Invalid object type
                                    System.out.println("Invalid object type. Can only add character or room.");
                                }
                            } else {
                                System.out.println("Invalid command usage. Usage: add <characterId|roomId>");
                            }
                            break;
                        case "status":
                            // kiírja a camembert állapotát
                            sb.append("camembert: o: " + camembert.GetOpened() + "\n");
                            System.out.println("camembert: o: " + camembert.GetOpened() + "\n");
                            break;
                        default:
                            System.out.println("Invalid command for Camembert");
                            break;
                    }
                } else {
                    System.out.println("Invalid command usage. Usage: camembert <command>");
                }

            }
        }

        else if(id.startsWith("l")){
            //Logarlec parancsok
            if(command.length >= 3) {
                int logarlecId = Integer.parseInt(command[1]);
                Logarlec logarlec = (Logarlec) objects.get(logarlecId - 1);
  
                switch(command[2]) {
                    case "add":
                        if(command.length >= 4) {
                            // mihez adjuk hozzá a logarlec-et
                            int itemId = Integer.parseInt(command[3]);
                            // character vagy room id
                            Object addToWhat = objects.get(itemId - 1);

                            if (addToWhat instanceof Student) {
                                Student student = (Student) addToWhat;
                                student.AddItemToInventory(logarlec);
                                if(logarlec.getStatus()==false ){
                                    //&& student.GetRoom().GetSticky() == false , mert ha sticky, nem lehet felvenni

                                    sb.append("WIN");
                                    System.out.println("WIN");
                                    //kéne valamit értesíteni?
                                }

                            } else if (addToWhat instanceof Room) {
                                Room room = (Room) addToWhat;
                                room.AddItemToRoom(logarlec);
                            } else if (addToWhat instanceof Teacher) {
                                Teacher teacher = (Teacher) addToWhat;
                                //teacher.AddItemToInventory(logarlec);
                                teacher.PickUp(logarlec);
                            } else {
                                // Invalid object type
                                System.out.println("Invalid object type. Can only add character or room.");
                            }
                        } else {
                            System.out.println("Invalid command usage. Usage: add <roomId|characterId>");
                        }
                        break;
                    case "status":
                        // metódust megcsináltam
                        sb.append("logarlec: d: " + logarlec.getStatus() + "\n");
                        System.out.println("logarlec: d: " + logarlec.getStatus() + "\n");
                        break;
                    default:
                        System.out.println("Invalid command for Logarlec");
                        break;
                }
            } else {
                System.out.println("Invalid command usage. Usage: logarlec <command>");
            }
        }

        else if(id.startsWith("v")){
            //TVSZ parancsok
            if(command.length >= 3) {
                int tvszId = Integer.parseInt(command[1]);
                TVSZ tvsz = (TVSZ) objects.get(tvszId - 1);
//                Student student = null;

                switch(command[2]) {
                    case "add":
                        if(command.length >= 4) {
                            // mihez adjuk hozzá a tvsz-t
                            int itemId = Integer.parseInt(command[3]);
                            // character vagy room id
                            Object addToWhat = objects.get(itemId - 1);

                            if (addToWhat instanceof Student) {
                                Student student = (Student) addToWhat;
                                student.AddItemToInventory(tvsz);

                            } else if (addToWhat instanceof Room) {
                                Room room = (Room) addToWhat;
                                room.AddItemToRoom(tvsz);
                            } else if (addToWhat instanceof Teacher) {
                                Teacher teacher = (Teacher) addToWhat;
                                teacher.AddItemToInventory(tvsz);
                            } else {
                                // Invalid object type
                                System.out.println("Invalid object type. Can only add character or room.");
                            }
                        } else {
                            System.out.println("Invalid command usage. Usage: add <characterId|roomId>");
                        }
                        break;
                    case "status":
                        sb.append("tvsz: t: " + tvsz.getLives() + " d: " + tvsz.IsDummy() + "\n");
                        System.out.println("tvsz: t: " + tvsz.getLives() + " d: " + tvsz.IsDummy() + "\n");
                        break;
                    case "use":
                        //TODO csak akkor lehet használni, ha az inventorynkban van, mert teacher nem tudja használni                        
 //                       if (student.getFormattedItems().contains("tvsz")) {
    
                            tvsz.Use();

 //                       } else {
 //                           System.out.println("Cannot use TVSZ. It is not in your inventory.");
 //                       }
                        break;
                    default:
                        System.out.println("Invalid command for TVSZ");
                        break;
                }
            } else {
                System.out.println("Invalid command usage. Usage: tvsz <command>");
            }
        }

        else if(id.startsWith("b")){
            //Beer parancsok
            if(command.length >= 3) {
                int beerId = Integer.parseInt(command[1]);
                Beer beer = (Beer) objects.get(beerId - 1);

                switch(command[2]) {
                    case "add":
                        if(command.length >= 4) {
                            // mihez adjuk hozzá a sört
                            int itemId = Integer.parseInt(command[3]);
                            // character vagy room id
                            Object addToWhat = objects.get(itemId - 1);

                            if (addToWhat instanceof Student) {
                                Student student = (Student) addToWhat;
                                student.AddItemToInventory(beer);
     
                            } else if (addToWhat instanceof Room) {
                                Room room = (Room) addToWhat;
                                room.AddItemToRoom(beer);
                            } else if (addToWhat instanceof Teacher) {
                                Teacher teacher = (Teacher) addToWhat;
                                teacher.AddItemToInventory(beer);
                            } else {
                                // Invalid object type
                                System.out.println("Invalid object type. Can only add character or room.");
                            }
                        } else {
                            System.out.println("Invalid command usage. Usage: add <characterId|roomId>");
                        }
                        break;
                    case "status":
                        sb.append("beer: t: " + beer.getTime() + " a: " + beer.getActivated() + "\n");
                        System.out.println("beer: t: " + beer.getTime() + " a: " + beer.getActivated() + "\n");
                        break;
                    case "drop":
                        //beer.Use();
                        //Ha nem véletlenszerű
                        if(random == null){
                            Model.Character ch = beer.GetCharacter();
                            ch.PutDown(ch.GetItems().get(0));

                        }else{
                            //Manuális léptetés(így random tárgy elejtése)
                            command[1] = "0";
                            tick();

                        }
                        break;
                    case "use": 
                        beer.Use();
                        command[2] = "drop";

                        if(random == null){
                            //Model.Character ch = beer.GetCharacter();
                            //ch.PutDown(ch.GetItems().get(0));

                        }else{
                            Random random = new Random();

                            //Manuális léptetés(így random tárgy elejtése)
                            //command[1] = "0";
                            //tick();
                        }

                        break;
                    default:
                        System.out.println("Invalid command for beer");
                        break;
                }
            } else {
                System.out.println("Invalid command usage. Usage: beer <command>");
            }

        }
        else if(id.startsWith("m")){
            //FFP2 parancsok
            if(command.length >= 3) {
                int maskId = Integer.parseInt(command[1]);
                FFP2 mask = (FFP2) objects.get(maskId - 1);

                switch(command[2]) {
                    case "add":
                        if(command.length >= 4) {
                            // mihez adjuk hozzá a maszkot
                            int itemId = Integer.parseInt(command[3]);
                            // character vagy room id
                            Object addToWhat = objects.get(itemId - 1);

                            if (addToWhat instanceof Student) {
                                Student student = (Student) addToWhat;
                                student.AddItemToInventory(mask);
                            } else if (addToWhat instanceof Room) {
                                Room room = (Room) addToWhat;
                                room.AddItemToRoom(mask);
                            } else if (addToWhat instanceof Teacher) {
                                Teacher teacher = (Teacher) addToWhat;
                                teacher.AddItemToInventory(mask);
                            } else {
                                // Invalid object type
                                System.out.println("Invalid object type. Can only add character or room.");
                            }
                        } else {
                            System.out.println("Invalid command usage. Usage: add <characterId|roomId>");
                        }
                        break;
                    case "status":
                        sb.append("mask: l: " + mask.getLives() + " t: " + mask.getTime() + " a: "+ mask.isActivated() +" d: " + mask.isDummy() + "\n");
                        System.out.println("mask: l: " + mask.getLives() + " t: " + mask.getTime() + " a: "+ mask.isActivated() +" d: " + mask.isDummy() + "\n");
                        break;
                    case "use":
                        mask.Use();
                        break;
                    default:
                        System.out.println("Invalid command for mask");
                        break;
                }
            } else {
                System.out.println("Invalid command usage. Usage: mask <command>");
            }
        }
    }
    //Futtat egy kört a játékon a paraméternek megfelelően
    public void tick(){
        //A második paraméter alapján futtat
        int selection = Integer.parseInt(command[1]);
        //ha nulla, vagyis mindent kell futtatni, akkor lépteti a szobanevekhez tartozó objektumok körét
        if(selection == 0){
            for(int i = 0; i < objectnames.size(); i++){
                //Azok az objectumok, melyek neve r-el kezdődik, azokról tudjuk, hogy szobák
                if(objectnames.get(i).startsWith("r")){
                    Room r = (Room)objects.get(i);
                    
                    //Megadjuk a kör számát?? (mennyi?)
                    r.Step(1, random);
                }
            }
        }else{
            Room r = (Room)objects.get(selection - 1);
                    
            //Megadjuk a kör számát?? (mennyi?)
            r.Step(1, random);
        }
        ProtoStudent.GetnumberOfStudents();
        //A feltétel ellenőrzés ha nincsen élő student
        //Valamiért a numberofstudents változó elég random működésű, az okozhatja a game end hibákat
        if(ProtoStudent != null && ProtoStudent.GetnumberOfStudents() == 0){
            sb.append("LOST\n");
            System.out.println("LOST");
        }
        
    }


}
