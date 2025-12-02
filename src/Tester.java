package src;
import java.io.File;
import java.io.IOException;
import static java.lang.System.out;
import java.util.*;
import javax.imageio.ImageIO;

public class Tester {
    private static ArrayList<Bird> birds = new ArrayList<Bird>();
    private static Map<String, ArrayList<String>> bonusMap = new HashMap<String, ArrayList<String>>();
    private static String[] bonuses = {"Anatomist", "Cartographer", "Historian", "Photographer", "Backyard Birder", "Bird Bander", "Bird Counter", "Bird Feeder", "Diet Specialist", "Enclosure Builder", "Species Protector", "Falconer", "Fishery Manager", "Food Web Expert", "Forester", "Large Bird Specialist", "Nest Box Builder", "Omnivore Expert", "Passerine Specialist", "Platform Builder", "Prairie Manager", "Rodentologist", "Small Clutch Specialist", "Viticulturalist", "Wetland Scientist", "Wildlife Gardener"};
    public static void main(String[] args) {
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
                    ArrayList<String> supportSplit = new ArrayList<String>();
                    supportSplit.addAll(Arrays.asList(quoteSplit[0].split(",")));
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
                    String[] foox = new String[foo.size()];
                    for (int i=0;i<foo.size();i++) foox[i] = foo.get(i);
                    //out.println(Arrays.toString(foox));
                    foodArr.add(foox);
                }

                // Ability type stuff
                //out.println("Got to abilityType stuff");
                String abilityType;
                if (items[3].equals("X")) abilityType = "predator";
                else if (items[4].equals("X")) abilityType = "flocking";
                else if (items[5].equals("X")) abilityType = "bonus";
                else abilityType = "";

                //ablityActivate stuff
                //out.println("Got to abilityActivate stuff");
                String abilityActivate;
                abilityActivate = switch (items[1]) {
                    case "brown" -> "OA";
                    case "white" -> "WP";
                    case "pink" -> "OBT";
                    default -> "N";
                };

                //habitat stuff
                //out.println("Got to Habitat stuff");
                ArrayList<String> habitats = new ArrayList<String>();
                if (items[10].equals("X")) habitats.add("f");
                if (items[11].equals("X")) habitats.add("p");
                if (items[12].equals("X")) habitats.add("w");
                //out.println("Items: " + items[11]+items[12]+items[13]);
                //out.println("Habitats: "+habitats);

                //out.println("Got to birdmaking stuff");
                
                ArrayList<String[]> foodLists = new ArrayList<String[]>();
                for (String[] foodOption : foodArr) {
                    String[] foodTypes = new String[foodOption.length];
                    for (int i = 0; i < foodOption.length; i++) {
                        foodTypes[i] = foodOption[i];
                    }
                    foodLists.add(foodTypes);
                }
                b = Bird.create(items[0], abilityActivate, items[2], abilityType, Integer.parseInt(items[6]), items[7], Integer.parseInt(items[8]), Integer.parseInt(items[9]), habitats, foodLists);
                birds.add(b);
                //out.println("Finished birdmaking stuff");

                //22 start bonuses

                

                int i=22;
                while (i<22+bonuses.length && i<items.length){
                    if (items[i].equals("X")){
                        bonusMap.get(bonuses[i-22]).add(items[0]);
                    }
                    i++;
                }
            }
        } catch (Exception e) {
            out.println("Exception: " + e + "\ncsv reading ran into issue");
        }
        

        
    }


    public static void setUpBirdPics(){
        try{
            for (Bird b: birds){
                String name = b.getName().toLowerCase().replace("-","_").replace("'","").replace(" ","_");
                //out.println(name);
                b.setImage(ImageIO.read(Tester.class.getResource("/assets/birds/"+name+".png")));
                
            }
        }catch(Exception e){
            out.println("Exception: "+e);
            out.println("Images couldn't be loaded.");
        }
    }
}