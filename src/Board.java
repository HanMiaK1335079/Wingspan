package src;
import java.util.*;

public class Board {
    //replace arraylist of each habitat with 2D array as follows
    //0 - forest, 1 - plains, 2 - wetlands
    // will reference each row and not individual arraylists.
    private Bird[][] board = new Bird[3][5];
    
    
    public Board() {
        for (Bird[] row : board) {
            Arrays.fill(row, null);
        }
    }
    
    public Bird[][] getBoard() {
        return board;
    }
    
    public void setBoard(Bird[][] b) {
        this.board = b;
    }

    public int getFirstEmptySlotIndex(String habitat) {
        int h = getHabitatIndex(habitat);
        if (h == -1) return -1;

        for (int i = 0; i < board[h].length; i++) {
            if (board[h][i] == null) {
                return i;
            }
        }
        return -1;
    }
    
    public boolean playBird(Bird bird, String habitat) {
        return playBird(bird, habitat, -1); 
    }
    
    public boolean playBird(Bird bird, String habitat, int position) {
        int h = getHabitatIndex(habitat);
        if (h == -1 || !bird.canLiveInHabitat(habitat)) {
            return false;
        }

        int placementIndex = position;
        if (placementIndex < 0) {
            placementIndex = getFirstEmptySlotIndex(habitat);
        }

        if (placementIndex < 0 || placementIndex >= board[h].length) {
            return false;
        }

        if (board[h][placementIndex] == null) {
            board[h][placementIndex] = bird;
            bird.setHabitat(habitat);
            return true;
        }

        return false;
    }
    
    public boolean canPlayBird(String habitat) {
        return canPlayBird(habitat, -1); 
    }
    
    public boolean canPlayBird(String habitat, int position) {
        int h = getHabitatIndex(habitat);
        if (h == -1) {
            return false;
        }

        if (position >= 0) {
            return position < board[h].length && board[h][position] == null;
        }

        return getFirstEmptySlotIndex(habitat) != -1;
    }
    
    public int getHabitatSize(String habitat) {
        int h = getHabitatIndex(habitat);
        if (h == -1) return 0;
        
        int count = 0;
        for (Bird bird : board[h]) {
            if (bird != null) {
                count++;
            }
        }
        return count;
    }
    
    public Bird getBirdAtPosition(String habitat, int position) {
        int h = getHabitatIndex(habitat);
        if (h == -1 || position < 0 || position >= board[h].length) return null;
        return board[h][position];
    }
    
    public boolean isHabitatFull(String habitat) {
        int h = getHabitatIndex(habitat);
        if (h == -1) return true;
        
        for (Bird bird : board[h]) {
            if (bird == null) {
                return false;
            }
        }
        return true;
    }

    public int getEggCount() {
        int totalEggs = 0;
        for (Bird[] row : board) {
            for (Bird b : row) {
                if (b != null) {
                    totalEggs += b.getEggCount();
                }
            }
        }
        return totalEggs;
    }

    public String getHabitatName(int index) {
        switch (index) {
            case 0: return "forest";
            case 1: return "plains";
            case 2: return "wetlands";
            default: return "";
        }
    }

    public String getHabitatOfBird(Bird b) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == b) {
                    return getHabitatName(i);
                }
            }
        }
        return null;
    }

    public ArrayList<Bird> getBirdsInHabitat(String habitat) {
        int h = getHabitatIndex(habitat);
        if (h == -1) return new ArrayList<>();
        
        ArrayList<Bird> birds = new ArrayList<>();
        for (Bird bird : board[h]) {
            if (bird != null) {
                birds.add(bird);
            }
        }
        return birds;
    }
    
    public ArrayList<Bird> getAllBirds() {
        ArrayList<Bird> allBirds = new ArrayList<>();
        forEachBird(allBirds::add);
        return allBirds;
    }

    public int getBirdCount() {
        return getAllBirds().size();
    }
    
    public int getCachedFoodCount() {
        final int[] total = {0};
        forEachBird(bird -> total[0] += bird.getCachedFood());
        return total[0];
    }
    
    public int getLongestRow() {
        int maxLength = 0;
        
        for (int h = 0; h < 3; h++) {
            int currentLength = 0;
            for (Bird b : board[h]) {
                if (b != null) {
                    currentLength++;
                }
            }
            if (currentLength > maxLength) {
                maxLength = currentLength;
            }
        }
        return maxLength;
    }

    private void forEachBird(java.util.function.Consumer<Bird> action) {
        for (Bird[] row : board) {
            for (Bird b : row) {
                if (b != null) {
                    action.accept(b);
                }
            }
        }
    }

    public void layEggsInNestType(String nestType) {
        System.out.println("Laying eggs on : " + nestType);
        for (Bird[] row: board){
            for (Bird b: row){
                if (!(b==null)){
                    if (b.getNest().equals(nestType)){
                        b.addEggs(1);
                        System.out.println("Added Eggz");
                    }
                }
            }
        }
    }

    public boolean layEggsOnAnyBird() {
        ArrayList<Bird> birds = getAllBirds();
        birds.sort(Comparator.comparingInt(b -> (b.getStoredEggs() >= b.getMaxEggs()) ? 1 : 0));
        for (Bird bird : birds) {
            if (bird.getStoredEggs() < bird.getMaxEggs()) {
                bird.addEggs(1);
                return true;
            }
        }
        return false;
    }
    
    public int getHabitatIndex(String habitat) {
        switch (habitat.toLowerCase()) {
            case "forest":
                return 0;
            case "plains":
                return 1;
            case "grasslands": return 1;
            case "wetlands":
            case "wetland":
                return 2;
            default:
                return -1;
        }
    }

    public void moveBird(Bird bird, String newHabitat) {
        String oldHabitat = bird.getHabitat();
        int oldHabitatIndex = getHabitatIndex(oldHabitat);
        int newHabitatIndex = getHabitatIndex(newHabitat);

        if (oldHabitatIndex != -1 && newHabitatIndex != -1) {
            for (int i = 0; i < board[oldHabitatIndex].length; i++) {
                if (board[oldHabitatIndex][i] == bird) {
                    board[oldHabitatIndex][i] = null;
                    break; 
                }
            }

            // Find first empty slot; if none available, don't move
            int newPosition = getFirstEmptySlotIndex(newHabitat);
            if (newPosition != -1) {
                board[newHabitatIndex][newPosition] = bird;
                bird.setHabitat(newHabitat);
            } else {
                // Habitat full—place bird back in old habitat
                int oldPosition = getFirstEmptySlotIndex(oldHabitat);
                if (oldPosition != -1) {
                    board[oldHabitatIndex][oldPosition] = bird;
                }
            }
        }
    }

    public ScoreBreakdown calculateScore() {
        int points = 0;
        int eggs = 0;
        int cached = 0;
        int tucked = 0;
        for (Bird[] row : board) {
            for (Bird b : row) {
                if (b != null) {
                    points += b.getPoints();
                    eggs += b.getEggCount();
                    cached += b.getCachedFood();
                    tucked += b.getTuckedCards();
                }
            }
        }
        // Use 6-arg constructor: printed, eggs, cached, tucked, bonus, flocked
        return new ScoreBreakdown(points, eggs, cached, tucked, 0, 0);
    }

    public ArrayList<Bird> getBirdsByNestType(String nestType) {
        ArrayList<Bird> birds = new ArrayList<>();
        forEachBird(b -> {
            if (b.getNest().equalsIgnoreCase(nestType)) {
                birds.add(b);
            }
        });
        return birds;
    }

    public Bird getRightmostBirdInHabitat(String habitat) {
        int h = getHabitatIndex(habitat);
        if (h == -1) return null;

        for (int i = board[h].length - 1; i >= 0; i--) {
            if (board[h][i] != null) {
                return board[h][i];
            }
        }
        return null;
    }

    public boolean isBirdRightmostInHabitat(Bird b, String habitat) {
        int habitatIndex = getHabitatIndex(habitat);

        if (habitatIndex != -1) {
            for (int i = 0; i < board[habitatIndex].length; i++) {
                if (board[habitatIndex][i] == b) {
                    for (int j = i + 1; j < board[habitatIndex].length; j++) {
                        if (board[habitatIndex][j] != null) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }
}