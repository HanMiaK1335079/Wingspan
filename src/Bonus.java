package src;
import java.awt.image.BufferedImage;
import java.util.*;

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

    public Bonus(String n, ArrayList<String> b){
        name = n;
        birds = b;
        determineBonusType();
    }
    
    private void determineBonusType() {
        switch(name) {
            case "Anatomist": type = BonusType.ANATOMIST; break;
            case "Cartographer": type = BonusType.CARTOGRAPHER; break;
            case "Historian": type = BonusType.HISTORIAN; break;
            case "Photographer": type = BonusType.PHOTOGRAPHER; break;
            case "Backyard Birder": type = BonusType.BACKYARD_BIRDER; break;
            case "Bird Bander": type = BonusType.BIRD_BANDER; break;
            case "Bird Counter": type = BonusType.BIRD_COUNTER; break;
            case "Bird Feeder": type = BonusType.BIRD_FEEDER; break;
            case "Diet Specialist": type = BonusType.DIET_SPECIALIST; break;
            case "Enclosure Builder": type = BonusType.ENCLOSURE_BUILDER; break;
            case "Species Protector": type = BonusType.SPECIES_PROTECTOR; break;
            case "Falconer": type = BonusType.FALCONER; break;
            case "Fishery Manager": type = BonusType.FISHERY_MANAGER; break;
            case "Food Web Expert": type = BonusType.FOOD_WEB_EXPERT; break;
            case "Forester": type = BonusType.FORESTER; break;
            case "Large Bird Specialist": type = BonusType.LARGE_BIRD_SPECIALIST; break;
            case "Nest Box Builder": type = BonusType.NEST_BOX_BUILDER; break;
            case "Omnivore Expert": type = BonusType.OMNIVORE_EXPERT; break;
            case "Passerine Specialist": type = BonusType.PASSERINE_SPECIALIST; break;
            case "Platform Builder": type = BonusType.PLATFORM_BUILDER; break;
            case "Prairie Manager": type = BonusType.PRAIRIE_MANAGER; break;
            case "Rodentologist": type = BonusType.RODENTOLOGIST; break;
            case "Small Clutch Specialist": type = BonusType.SMALL_CLUTCH_SPECIALIST; break;
            case "Viticulturalist": type = BonusType.VITICULTURALIST; break;
            case "Wetland Scientist": type = BonusType.WETLAND_SCIENTIST; break;
            case "Wildlife Gardener": type = BonusType.WILDLIFE_GARDENER; break;
            default: type = BonusType.BACKYARD_BIRDER; break;
        }
    }

    public String getName() {return name;}
    public ArrayList<String> getBirds() {return birds;}
    public void setImage(BufferedImage i){image = i;}
    public BufferedImage getImage() {return image;}
    public BonusType getType() {return type;}

    public boolean hasBird(Bird b) {return birds.contains(b.getName());}

    public int calculateBonus(Player p){
        int bonus = 0;
        
        switch(type) {
            case ANATOMIST:
                for (Bird b : p.getCardsInHand()) {
                    if (b.getWingspan() >= 65) bonus++;
                }
                for (Bird b : p.getAllBirdsOnBoard()) {
                    if (b.getWingspan() >= 65) bonus++;
                }
                break;
                
            case CARTOGRAPHER:
                for (Bird b : p.getCardsInHand()) {
                    if (b.getMaxEggs() >= 4) bonus++;
                }
                for (Bird b : p.getAllBirdsOnBoard()) {
                    if (b.getMaxEggs() >= 4) bonus++;
                }
                break;
                
            case HISTORIAN:
                for (Bird b : p.getCardsInHand()) {
                    if (b.getPoints() >= 5) bonus++;
                }
                for (Bird b : p.getAllBirdsOnBoard()) {
                    if (b.getPoints() >= 5) bonus++;
                }
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
                for (Bird b : p.getCardsInHand()) {
                    if (hasBird(b)) bonus++;
                }
                for (Bird b : p.getAllBirdsOnBoard()) {
                    if (hasBird(b)) bonus++;
                }
                break;
                
            case DIET_SPECIALIST:
                for (Bird b : p.getCardsInHand()) {
                    if (hasBird(b)) bonus++;
                }
                for (Bird b : p.getAllBirdsOnBoard()) {
                    if (hasBird(b)) bonus++;
                }
                break;
                
            case ENCLOSURE_BUILDER:
                for (Bird b : p.getCardsInHand()) {
                    if (hasBird(b)) bonus++;
                }
                for (Bird b : p.getAllBirdsOnBoard()) {
                    if (hasBird(b)) bonus++;
                }
                break;
                
            case SPECIES_PROTECTOR:
                for (Bird b : p.getCardsInHand()) {
                    if (hasBird(b)) bonus++;
                }
                for (Bird b : p.getAllBirdsOnBoard()) {
                    if (hasBird(b)) bonus++;
                }
                break;
                
            case FALCONER:
                for (Bird b : p.getCardsInHand()) {
                    if (b.getabilityType().equals("predator")) {
                        if (hasBird(b)) bonus++;
                    }
                }
                for (Bird b : p.getAllBirdsOnBoard()) {
                    if (b.getabilityType().equals("predator")) {
                        if (hasBird(b)) bonus++;
                    }
                }
                break;
                
            case FISHERY_MANAGER:
                for (Bird b : p.getCardsInHand()) {
                    if (b.canLiveInHabitat("wetlands")) {
                        if (hasBird(b)) bonus++;
                    }
                }
                for (Bird b : p.getAllBirdsOnBoard()) {
                    if (b.canLiveInHabitat("wetlands")) {
                        if (hasBird(b)) bonus++;
                    }
                }
                break;
                
            case FOOD_WEB_EXPERT:
                for (Bird b : p.getCardsInHand()) {
                    if (hasBird(b)) bonus++;
                }
                for (Bird b : p.getAllBirdsOnBoard()) {
                    if (hasBird(b)) bonus++;
                }
                break;
                
            case FORESTER:
                for (Bird b : p.getCardsInHand()) {
                    if (b.canLiveInHabitat("forest")) {
                        if (hasBird(b)) bonus++;
                    }
                }
                for (Bird b : p.getAllBirdsOnBoard()) {
                    if (b.canLiveInHabitat("forest")) {
                        if (hasBird(b)) bonus++;
                    }
                }
                break;
                
            case LARGE_BIRD_SPECIALIST:
                for (Bird b : p.getCardsInHand()) {
                    if (b.getWingspan() >= 100) {
                        if (hasBird(b)) bonus++;
                    }
                }
                for (Bird b : p.getAllBirdsOnBoard()) {
                    if (b.getWingspan() >= 100) {
                        if (hasBird(b)) bonus++;
                    }
                }
                break;
                
            case NEST_BOX_BUILDER:
                for (Bird b : p.getCardsInHand()) {
                    if (b.getNest().equals("cavity")) {
                        if (hasBird(b)) bonus++;
                    }
                }
                for (Bird b : p.getAllBirdsOnBoard()) {
                    if (b.getNest().equals("cavity")) {
                        if (hasBird(b)) bonus++;
                    }
                }
                break;
                
            case OMNIVORE_EXPERT:
                for (Bird b : p.getCardsInHand()) {
                    if (hasBird(b)) bonus++;
                }
                for (Bird b : p.getAllBirdsOnBoard()) {
                    if (hasBird(b)) bonus++;
                }
                break;
                
            case PASSERINE_SPECIALIST:
                for (Bird b : p.getCardsInHand()) {
                    if (hasBird(b)) bonus++;
                }
                for (Bird b : p.getAllBirdsOnBoard()) {
                    if (hasBird(b)) bonus++;
                }
                break;
                
            case PLATFORM_BUILDER:
                for (Bird b : p.getCardsInHand()) {
                    if (b.getNest().equals("platform")) {
                        if (hasBird(b)) bonus++;
                    }
                }
                for (Bird b : p.getAllBirdsOnBoard()) {
                    if (b.getNest().equals("platform")) {
                        if (hasBird(b)) bonus++;
                    }
                }
                break;
                
            case PRAIRIE_MANAGER:
                for (Bird b : p.getCardsInHand()) {
                    if (b.canLiveInHabitat("plains")) {
                        if (hasBird(b)) bonus++;
                    }
                }
                for (Bird b : p.getAllBirdsOnBoard()) {
                    if (b.canLiveInHabitat("plains")) {
                        if (hasBird(b)) bonus++;
                    }
                }
                break;
                
            case RODENTOLOGIST:
                for (Bird b : p.getCardsInHand()) {
                    ArrayList<String[]> foods = b.getFoods();
                    boolean hasRat = false;
                    for (String[] foodSet : foods) {
                        for (String food : foodSet) {
                            if (food.equals("r")) {
                                hasRat = true;
                                break;
                            }
                        }
                        if (hasRat) break;
                    }
                    if (hasRat && hasBird(b)) bonus++;
                }
                for (Bird b : p.getAllBirdsOnBoard()) {
                    ArrayList<String[]> foods = b.getFoods();
                    boolean hasRat = false;
                    for (String[] foodSet : foods) {
                        for (String food : foodSet) {
                            if (food.equals("r")) {
                                hasRat = true;
                                break;
                            }
                        }
                        if (hasRat) break;
                    }
                    if (hasRat && hasBird(b)) bonus++;
                }
                break;
                
            case SMALL_CLUTCH_SPECIALIST:
                for (Bird b : p.getCardsInHand()) {
                    if (b.getMaxEggs() <= 2) {
                        if (hasBird(b)) bonus++;
                    }
                }
                for (Bird b : p.getAllBirdsOnBoard()) {
                    if (b.getMaxEggs() <= 2) {
                        if (hasBird(b)) bonus++;
                    }
                }
                break;
                
            case VITICULTURALIST:
                for (Bird b : p.getCardsInHand()) {
                    ArrayList<String[]> foods = b.getFoods();
                    boolean hasBerry = false;
                    for (String[] foodSet : foods) {
                        for (String food : foodSet) {
                            if (food.equals("b")) {
                                hasBerry = true;
                                break;
                            }
                        }
                        if (hasBerry) break;
                    }
                    if (hasBerry && hasBird(b)) bonus++;
                }
                for (Bird b : p.getAllBirdsOnBoard()) {
                    ArrayList<String[]> foods = b.getFoods();
                    boolean hasBerry = false;
                    for (String[] foodSet : foods) {
                        for (String food : foodSet) {
                            if (food.equals("b")) {
                                hasBerry = true;
                                break;
                            }
                        }
                        if (hasBerry) break;
                    }
                    if (hasBerry && hasBird(b)) bonus++;
                }
                break;
                
            case WETLAND_SCIENTIST:
                for (Bird b : p.getCardsInHand()) {
                    ArrayList<String> habitats = b.getHabitats();
                    if (habitats.size() == 1 && habitats.contains("w")) {
                        if (hasBird(b)) bonus++;
                    }
                }
                for (Bird b : p.getAllBirdsOnBoard()) {
                    ArrayList<String> habitats = b.getHabitats();
                    if (habitats.size() == 1 && habitats.contains("w")) {
                        if (hasBird(b)) bonus++;
                    }
                }
                break;
                
            case WILDLIFE_GARDENER:
                for (Bird b : p.getCardsInHand()) {
                    if (b.getNest().equals("bowl")) {
                        if (hasBird(b)) bonus++;
                    }
                }
                for (Bird b : p.getAllBirdsOnBoard()) {
                    if (b.getNest().equals("bowl")) {
                        if (hasBird(b)) bonus++;
                    }
                }
                break;
        }
        
        return bonus;
    }
    
    public int getBonusPoints(Player p) {
        int count = calculateBonus(p);
        return count * 5; 
    }
}