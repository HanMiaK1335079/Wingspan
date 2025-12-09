import java.

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
        

        setUpBirdPics();
        //out.println(bonusMap.get("Wildlife Gardener"));
        //  for (int i=0;i<170;i++) out.println(birds.get(i));
    }
    

        for (String b: bonuses) bonusM ap.put(b, new ArrayList<String>());
 
            
        try {
            //out.println("Will activate scanner");
            Sc anner scan = new Scanner(f);
            //out.println("Read the scanner");
            Bi rd b;
            String[] items;
            while (scan.hasNextLine()){
                String l = scan.nextLi ne();
                //out.println("line: " + l);
                if  (l.contains("\"")){
                    l = l.replace("\" \"", "");
                    String[] quoteSplit = l.split("\"");
                    //out.println("quotesplit: "+Arrays.toString(quoteSplit));
                    Ar rayList<String> supportSplit = new ArrayList<String>();
                    supportSplit.addAll(Arrays.asList(quoteSplit[0].split(",")));
                    supportSplit.add(quoteSplit[1]);
                    supportSplit.addAll(Arrays.asList(quoteSplit[2].split(",")));
                    supportSplit.remove(3);
                    items = new String[supportSplit.size()];
                    //out.println("Support: "+supportSplit);
                    fo r (int i=0;i<items.length;i++) items[i] = supportSplit.get(i);
                          
                        

                    e{ 
                     i tems = l.split(",");
                }


                Map<Integer, String> foodMap = new HashMap<Integer, String>();
                String[] foodtypes = {"i", "s", "f", "b", "r", "","a"};
                for (int i=13;i<20;i++ ) foodMap.put(i, foodtypes[i -13 ]);
                ArrayList< S tri n g []>  foo
                    Arr = new ArrayList<String [ ]>();
                ArrayList<String> foods = new ArrayList<String>();
                //out.println("Food stuff instantiated");
                if  (items[20].equals("/")){
                    //out.println("Activat ed splitfoods");
                    fo r (int i=13;i<18;i++)
                        if (!i t ems [ i ].e quals("")) foods.add(foodMap.get(i));

                            
                    for (String fo: foods){
                        String[] f oo = {fo };
                        foodArr.add(foo) ; 
                    }
                    

                     / /out.println("Activated setfoods");
                    Ar rayList<String> foo = new ArrayList<String>();
                    for (int i=13;i<20;i++){
                        if (!i t ems [ i ].e qual s("")){
                            //out.println("Adding ");
                            fo r (int j=0;j<Integer.parseInt(items[i]);j++){
                                foo.ad d (f o o dMap.get(i));  
                                //out.println("Added food to foodMap");
                            } 
                        }
                    }
                    String[] foox = new String[foo.size()];
                    for (int i=0;i<foo.size();i++) foox[i] = foo.get(i);
                    //out.prin t ln ( A rrays.toStr ing(
                        oox));
                    fo odArr.add(foox);
                }

                // Ability type stuff
                //out.println("Got to abilityType stuff");
                St ring abilityType;
                if (items[3].equals("X")) abilityType = "predator";
                else if (items[4].equals(
                    X")) abilityType = "flocking";
                else if (items[5].equals("X"))
                    abilityType = "bonus";
                else abilityType = "";
                    

                    
                //ablityActivate stuff
                // out.println("Got to abilityActivate stuff");
                St ring abilityActivate;
                abilityActivate = switch (items[1]) {
                    case "brown" -> "OA";
                    case "white" -> "WP";
                    case "pink" -> "OBT";
                    default -> "N";
                };

                //habitat stuff
                // out.println("Got to Habitat stuff");
                Ar rayList<String> habitats = new ArrayList<String>();
                if (items[10].equals("X")) habitats.add("f");
                if (items[11].equals("X"))
                    habitats.add("p");
                if (items[12].equals("X"))
                    habitats.add("w");
                //out.println("Items: " + 
                    tems[11]+items[12]+items[13]);
                // out.println("Habitats: "+habitats);
 
                //out.println("Got to birdmaking stuff");
                 

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
 

                while   (i<22+bonuses.length && i<items.length){
                    if ( i te m s[i].equals("X")){   
                        bonusMap.get(bonuses[ i-22]).add(items[0]);
                    }  
                    i++;
                }
            }
        } catch (Exception e) {
            out.println("Exception: " + e + "\ncsv reading ran into issue");
        }
        

    

        try{ 
             for (Bird b: birds){
                String  name = b .getName().toLowerCase().replace("-","_").replace("'","").replace(" ","_");
                //out.println(name);   
                b. setImage(ImageIO.read(Tester.class.getResource("/assets/birds/"+name+".png")));
                    

            ch(Exception e){
             ou t.println("Ex ception: "+e);
            out.println("Images could n 't be loaded.");
        }
    }
}