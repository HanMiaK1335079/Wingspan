import java.io.File;
import java.io.IOException;
import static java.lang.System.out;
import java.util.*;
import javax.imageio.ImageIO;

public class Tester {
    private static ArrayList<Bird> birds = new ArrayList<Bird>();
    private static Map<String, ArrayList<String>> bonusMap = new HashMap<String, ArrayList<String>>();
    private static String[] bonuses = {"Anatomist", "Cartographer", "Historian", "Photographer", "Backyard Birder", "Bird Bander", "Bird Counter", "Bird Feeder", "Diet Specialist", "Enclosure Builder", "Species Protector", "Falconer", "Fishery Manager", "Food Web Expert", "Forester", "Large Bird Specialist", "Nest Box Builder", "Omnivore Expert", "Passerine Specialist", "Platform Builder", "Prairie Manager", "Rodentologist", "Small Clutch Specialist", "Viticulturalist", "Wetland Scientist", "Wildlife Gardener"};
    public static void main(String[] ar gs) {
            
            
            
             

        File f = new File("assets/birdInfo.csv");
        

        readCSV(f);
        setUpBirdPics();
        //out.println(bonusMap.get("Wildlife Gardener"));
        // for (int i=0;i<170;i++) out.println(birds.get(i));
    }
    
    public static void readCSV(File f){
        for (String b: bonuses) bonusMap.put(b, new ArrayList<String>());

        try {
            //out.println("Will activate scanner");
            Scanner scan = new Scanner(f);
            //out.println("Read the scanner");
            Bird b;
            String[] items;
            while (scan.hasNextLine()){
                String l = scan.nextLine();
                //out.println("line: " + l);
                if (l.contains("\"")){
                    l = l.replace("\"\"", "");
                    String[] quoteSplit = l.split("\"");
                    //out.println("quotesplit: "+Arrays.toString(quoteSplit));
                    ArrayL

    st<String> suppor
         supportSplit.addAll(Arra
      supportSplit.add(quoteSplit[1]);
                    supportSplit.addAll(Arrays.asList(quoteSplit[2].split(",")));
                    supportSplit.remove(3);
                    items = new String[supportSplit.size()];
                    //out.println("Support: "+supportSplit);
                    for (int i=0;i<items.length;i++) items[i] = supportSplit.get(i);
                    
                    // for (int i=0;i<items.length;i++) out.print(i+": "+items[i]+"        ");
                }else{
                    items = l.split(",");
                }
    
                


                // Doing all the food parsing yey
                Map<Integer, String> foodMap = new HashMap<Integer, String>();
                String[] foodtypes = {"i", "s", "f", "b", "r", "","a"};
                for (int i=13;i<20;i++) foodMap.put(i, foodtypes[i-13]);
                ArrayList<String[]> foodArr = new ArrayList<String[]>();
                ArrayList<String> foods = new ArrayList<String>();
                //out.println("Food stuff instantiated");
                if (items[20].equals("/")){
                    //out.println("Activated splitfoods");
                    for (int i=13;i<18;i++)
                        if (!items[i].equals("")) foods.add(foodMap.get(i));

                    for (String fo: foods){
                        String[] foo = {fo};
        foodArr.add(foo);
                    }
                    
                }else{
                    //out.println("Activated setfoods");
                    ArrayList<String> foo = new ArrayList<String>();
                    for (int i=13;i<20;i++){
                        if (!items[i].equals("")){
                            //out.println("Adding");
                            for (int j=0;j<Integer.parseInt(items[i]);j++){
                                foo.add(foodMap.get(i));
                                //out.println("Added food to foodMap");
                            }
                        }
                    }
     foox = new String[foo.size()];

    ;i<foo.size();i++) foox[i] = foo.get(i);
                    //out.println(Arrays.toString(foox));
                    foodArr.a

    bility type stufft.pi
    ntln( " Got to abilityType tuff");
    ng  abilityType;
    s[3].e
     if (items[4].equale

    
    // ablityActivate stuff
    //out.println("Got tSting abilityActivate;abilityctivate = switch case "brown" -> "OA";    cas "white" -> "WP";case "pink"- "OBT";    default -> ""

    // out.println("Got to 
    ArrayList<String> habitats = new ArrayList<Stri
    if (items[10].equals("X"if (items[11].euls("X") habitatsaitem[12].eqal("X")t.prntln("Iem: " +t.prntln("abtats: //

    Ar rayList<Strin
    for (String[] foodOption : foodArr) {
        String[] foodTpes = ne tring[foodOption.length]   for (int i = 0; i < fodOption.length; i+       foodTypes[i] = foo}   foodLists.add(foodType
    b  = Bird.create(items[0],
    s.add(b);
    //out.println("Finished birdmaking stu

    // 22 start bonuses

    
    
    i=22;e (<
    22+bonuseslnth && i<items.lengh){
    
        items[i].equals("X")){
     } }

    pr intln("Exception: " + e + "\ncsv reading 

     

    
    
    
          
    i(       /
    /out.println
    n
            b.setImage(ImageIO.read(Tester.class.getResource("/assets/bi
     

    }
    }
}

    
