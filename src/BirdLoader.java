package src;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BirdLoader {

    public static ArrayList<Bird> loadBirds(File f, Map<String, ArrayList<String>> bonusMap) {
        ArrayList<Bird> birds = new ArrayList<>();
        try {
            Scanner scan = new Scanner(f);
            String[] items;
            while (scan.hasNextLine()) {
                String l = scan.nextLine();
                if (l.contains("\"")) {
                    l = l.replace("\"\"", "");
                    String[] quoteSplit = l.split("\"");
                    ArrayList<String> supportSplit = new ArrayList<String>();
                    supportSplit.addAll(java.util.Arrays.asList(quoteSplit[0].split(",")));
                    supportSplit.add(quoteSplit[1]);
                    supportSplit.addAll(java.util.Arrays.asList(quoteSplit[2].split(",")));
                    supportSplit.remove(3);
                    items = new String[supportSplit.size()];
                    for (int i = 0; i < items.length; i++) items[i] = supportSplit.get(i);
                } else {
                    items = l.split(",");
                }

                Map<Integer, String> foodMap = new HashMap<>();
                String[] foodtypes = {"i", "s", "f", "b", "r", "", "a"};
                for (int i = 13; i < 20; i++) foodMap.put(i, foodtypes[i - 13]);
                ArrayList<String[]> foodArr = new ArrayList<>();
                ArrayList<String> foods = new ArrayList<>();
                if (items[20].equals("/")) {
                    for (int i = 13; i < 18; i++)
                        if (!items[i].equals("")) foods.add(foodMap.get(i));

                    for (String fo : foods) {
                        String[] foo = {fo};
                        foodArr.add(foo);
                    }

                } else {
                    ArrayList<String> foo = new ArrayList<>();
                    for (int i = 13; i < 20; i++) {
                        if (!items[i].equals("")) {
                            for (int j = 0; j < Integer.parseInt(items[i]); j++) {
                                foo.add(foodMap.get(i));
                            }
                        }
                    }
                    String[] foox = new String[foo.size()];
                    for (int i = 0; i < foo.size(); i++) foox[i] = foo.get(i);
                    foodArr.add(foox);
                }

                String abilityType;
                if (items[3].equals("X")) abilityType = "predator";
                else if (items[4].equals("X")) abilityType = "flocking";
                else if (items[5].equals("X")) abilityType = "bonus";
                else abilityType = "";

                String abilityActivate;
                abilityActivate = switch (items[1]) {
                    case "brown" -> "OA";
                    case "white" -> "WP";
                    case "pink" -> "OBT";
                    default -> "N";
                };

                ArrayList<String> habitats = new ArrayList<>();
                if (items[10].equals("X")) habitats.add("f");
                if (items[11].equals("X")) habitats.add("p");
                if (items[12].equals("X")) habitats.add("w");

                String[] food = items[10].split("/");
                ArrayList<Food.FoodType[]> foodList = new ArrayList<>();
                for (String s : food) {
                    String[] foodTypes = s.split("\\+");
                    Food.FoodType[] tempArr = new Food.FoodType[foodTypes.length];
                    for (int i = 0; i < foodTypes.length; i++) {
                        tempArr[i] = Food.FoodType.valueOf(foodTypes[i].toUpperCase());
                    }
                    foodList.add(tempArr);
                }

                Bird b = Bird.create(items[0], abilityActivate, items[2], abilityType, Integer.parseInt(items[6]), items[7], Integer.parseInt(items[8]), Integer.parseInt(items[9]), habitats, foodList);
                birds.add(b);

                int i = 22;
                while (i < 22 + bonusMap.size() && i < items.length) {
                    if (items[i].equals("X")) {
                        bonusMap.get(bonusMap.keySet().toArray()[i - 22]).add(items[0]);
                    }
                    i++;
                }
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e + "\ncsv reading ran into issue");
        }
        return birds;
    }
}