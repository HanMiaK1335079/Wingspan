package src;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.function.Predicate;

public class Bonus {
    final private String name;
    final private ArrayList<String> birds;
    BufferedImage image;
    
    public enum BonusType {
        ANATOMIST, CARTOGRAPHER, HISTORIAN, PHOTOGRAPHER, BACKYARD_BIRDER, 
        BIRD_BANDER, BIRD_COUNTER, BIRD_FEEDER, DIET_SPECIALIST, ENCLOSURE_BUILDER,
        SPECIES_PROTECTOR, FALCONER, FISHERY_MANAGER, FOOD_WEB_EXPERT, FORESTER,
        LARGE_BIRD_SPECIALIST, NEST_BOX_BUILDER, OMNIVORE_EXPERT, PASSERINE_SPECIALIST,
        PLATFORM_BUILDER, PRAIRIE_MANAGER, RODENTOLOGIST, SMALL_CLUTCH_SPECIALIST,
        VITICULTURALIST, WETLAND_SCIENTIST, WILDLIFE_GARDENER
    }
    
    private BonusType type;
    private static final Map<String, BonusType> bonusTypeMap = new HashMap<>();

    static {
        bonusTypeMap.put("Anatomist", BonusType.ANATOMIST);
        bonusTypeMap.put("Cartographer", BonusType.CARTOGRAPHER);
        bonusTypeMap.put("Historian", BonusType.HISTORIAN);
        bonusTypeMap.put("Photographer", BonusType.PHOTOGRAPHER);
        bonusTypeMap.put("Backyard Birder", BonusType.BACKYARD_BIRDER);
        bonusTypeMap.put("Bird Bander", BonusType.BIRD_BANDER);
        bonusTypeMap.put("Bird Counter", BonusType.BIRD_COUNTER);
        bonusTypeMap.put("Bird Feeder", BonusType.BIRD_FEEDER);
        bonusTypeMap.put("Diet Specialist", BonusType.DIET_SPECIALIST);
        bonusTypeMap.put("Enclosure Builder", BonusType.ENCLOSURE_BUILDER);
        bonusTypeMap.put("Species Protector", BonusType.SPECIES_PROTECTOR);
        bonusTypeMap.put("Falconer", BonusType.FALCONER);
        bonusTypeMap.put("Fishery Manager", BonusType.FISHERY_MANAGER);
        bonusTypeMap.put("Food Web Expert", BonusType.FOOD_WEB_EXPERT);
        bonusTypeMap.put("Forester", BonusType.FORESTER);
        bonusTypeMap.put("Large Bird Specialist", BonusType.LARGE_BIRD_SPECIALIST);
        bonusTypeMap.put("Nest Box Builder", BonusType.NEST_BOX_BUILDER);
        bonusTypeMap.put("Omnivore Expert", BonusType.OMNIVORE_EXPERT);
        bonusTypeMap.put("Passerine Specialist", BonusType.PASSERINE_SPECIALIST);
        bonusTypeMap.put("Platform Builder", BonusType.PLATFORM_BUILDER);
        bonusTypeMap.put("Prairie Manager", BonusType.PRAIRIE_MANAGER);
        bonusTypeMap.put("Rodentologist", BonusType.RODENTOLOGIST);
        bonusTypeMap.put("Small Clutch Specialist", BonusType.SMALL_CLUTCH_SPECIALIST);
        bonusTypeMap.put("Viticulturalist", BonusType.VITICULTURALIST);
        bonusTypeMap.put("Wetland Scientist", BonusType.WETLAND_SCIENTIST);
        bonusTypeMap.put("Wildlife Gardener", BonusType.WILDLIFE_GARDENER);
    }

    public Bonus(String n, ArrayList<String> b){
        name = n;
        birds = b;
        determineBonusType();
    }
    
    private void determineBonusType() {
        type = bonusTypeMap.getOrDefault(name, BonusType.BACKYARD_BIRDER);
    }

    
    public String getName() {return name;}
    public ArrayList<String> getBirds() {return birds;}
    public void setImage(BufferedImage i){image = i;}
    public BufferedImage getImage() {return image;}
    public BonusType getType() {return type;}

    public boolean hasBird(Bird b) {return birds.contains(b.getName());}

    private int countBirds(Player p, Predicate<Bird> condition) {
        int count = 0;
        for (Bird b : p.getCardsInHand()) {
            if (condition.test(b)) {
                count++;
            }
        }
        for (Bird b : p.getAllBirdsOnBoard()) {
            if (condition.test(b)) {
                count++;
            }
        }
        return count;
    }

    public int calculateBonus(Player p){
        int bonus = 0;
        
        switch(type) {
            case ANATOMIST:
                bonus = countBirds(p, b -> b.getWingspan() >= 65);
                break;
                
            case CARTOGRAPHER:
                bonus = countBirds(p, b -> b.getMaxEggs() >= 4);
                break;
                
            case HISTORIAN:
                bonus = countBirds(p, b -> b.getPoints() >= 5);
                break;
                
            case PHOTOGRAPHER:
                bonus += p.getBirdsInHabitat("wetlands").size();
                break;
                
            case BACKYARD_BIRDER:
                bonus += p.getBirdsInHabitat("forest").size();
                break;
                
            case BIRD_BANDER:
                bonus += p.getBirdsInHabitat("plains").size();
                break;
                
            case BIRD_COUNTER:
                bonus += p.getBirdsInHabitat("forest").size();
                bonus += p.getBirdsInHabitat("plains").size();
                break;
                
            case BIRD_FEEDER:
            case DIET_SPECIALIST:
            case ENCLOSURE_BUILDER:
            case SPECIES_PROTECTOR:
            case OMNIVORE_EXPERT:
            case PASSERINE_SPECIALIST:
            case FOOD_WEB_EXPERT:
                bonus = countBirds(p, this::hasBird);
                break;
                
            case FALCONER:
                bonus = countBirds(p, b -> b.getabilityType().equals("predator") && hasBird(b));
                break;
                
            case FISHERY_MANAGER:
                bonus = countBirds(p, b -> b.canLiveInHabitat("wetlands") && hasBird(b));
                break;
                
            case FORESTER:
                bonus = countBirds(p, b -> b.canLiveInHabitat("forest") && hasBird(b));
                break;
                
            case LARGE_BIRD_SPECIALIST:
                bonus = countBirds(p, b -> b.getWingspan() >= 100 && hasBird(b));
                break;
                
            case NEST_BOX_BUILDER:
                bonus = countBirds(p, b -> b.getNest().equals("cavity") && hasBird(b));
                break;
                
            case PLATFORM_BUILDER:
                bonus = countBirds(p, b -> b.getNest().equals("platform") && hasBird(b));
                break;
                
            case PRAIRIE_MANAGER:
                bonus = countBirds(p, b -> b.canLiveInHabitat("plains") && hasBird(b));
                break;
                
            case RODENTOLOGIST:
                bonus = countBirds(p, b -> hasBird(b) && b.getFoods().stream().flatMap(Arrays::stream).anyMatch(food -> food.equals("r")));
                break;
                
            case SMALL_CLUTCH_SPECIALIST:
                bonus = countBirds(p, b -> b.getMaxEggs() <= 2 && hasBird(b));
                break;
                
            case VITICULTURALIST:
                bonus = countBirds(p, b -> hasBird(b) && b.getFoods().stream().flatMap(Arrays::stream).anyMatch(food -> food.equals("b")));
                break;
                
            case WETLAND_SCIENTIST:
                bonus = countBirds(p, b -> {
                    List<String> habitats = b.getHabitats();
                    return habitats.size() == 1 && habitats.contains("w") && hasBird(b);
                });
                break;
                
            case WILDLIFE_GARDENER:
                bonus = countBirds(p, b -> b.getNest().equals("bowl") && hasBird(b));
                break;
        }
        
        return bonus;
    }



    
    public int getBonusPoints(Player p) {
        int count = calculateBonus(p);
        return count * 5; 
    }
}