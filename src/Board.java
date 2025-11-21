package src;
import java.util.*;

public class Board {
    //replace arraylist of each habitat with 2D array as follows
    //0 - forest, 1 - plains, 2 - wetlands
    // will reference each row and not individual arraylists.
    private Bird[][] board = new Bird[3][4];
    
    
    public Board() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = null;
            }
        }
    }
    
    public Bird[][] getBoard() {
        return board;
    }
    
    public void setBoard(Bird[][] b) {
        this.board = b;
    }
    
    public boolean playBird(Bird bird, String habitat) {
        int h = getHabitatIndex(habitat);
        if (h == -1) return false;
        
        if (!bird.canLiveInHabitat(habitat)) return false;
        
        for (int i = 0; i < board[h].length; i++) {
            if (board[h][i] == null) {
                board[h][i] = bird;
                return true;
            }
        }
        return false;
    }
    
    public boolean canPlayBird(String habitat) {
        int h = getHabitatIndex(habitat);
        if (h == -1) return false;
        
        for (int i = 0; i < board[h].length; i++) {
            if (board[h][i] == null) {
                return true;
            }
        }
        return false;
    }
    
    public int getHabitatSize(String habitat) {
        int h = getHabitatIndex(habitat);
        if (h == -1) return 0;
        
        int count = 0;
        for (int i = 0; i < board[h].length; i++) {
            if (board[h][i] != null) {
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
        
        for (int i = 0; i < board[h].length; i++) {
            if (board[h][i] == null) {
                return false;
            }
        }
        return true;
    }
    
    public int calculateScore() {
        int total = 0;
        
        //bird points
        for (int h = 0; h < 3; h++) {
            for (int i = 0; i < board[h].length; i++) {
                if (board[h][i] != null) {
                    total += board[h][i].getPoints();
                }
            }
        }
        
        // egg points
        for (int h = 0; h < 3; h++) {
            for (int i = 0; i < board[h].length; i++) {
                if (board[h][i] != null) {
                    total += board[h][i].getStoredEggs();
                }
            }
        }
        
        //add cached food pts
        for (int h = 0; h < 3; h++) {
            for (int i = 0; i < board[h].length; i++) {
                if (board[h][i] != null) {
                    total += board[h][i].getCachedFood();
                }
            }
        }
        
        for (int h = 0; h < 3; h++) {
            for (int i = 0; i < board[h].length; i++) {
                if (board[h][i] != null) {
                    total += board[h][i].getTuckedCards().size();
                }
            }
        }
        
        return total;
    }
    
    public int getEggCount() {
        int total = 0;
        for (int h = 0; h < 3; h++) {
            for (int i = 0; i < board[h].length; i++) {
                if (board[h][i] != null) {
                    total += board[h][i].getStoredEggs();
                }
            }
        }
        return total;
    }
    
    public int getBirdCount() {
        int total = 0;
        for (int h = 0; h < 3; h++) {
            for (int i = 0; i < board[h].length; i++) {
                if (board[h][i] != null) {
                    total++;
                }
            }
        }
        return total;
    }
    
    public ArrayList<Bird> getBirdsInHabitat(String habitat) {
        int h = getHabitatIndex(habitat);
        if (h == -1) return new ArrayList<>();
        
        ArrayList<Bird> birds = new ArrayList<>();
        for (int i = 0; i < board[h].length; i++) {
            if (board[h][i] != null) {
                birds.add(board[h][i]);
            }
        }
        return birds;
    }
    
    public ArrayList<Bird> getAllBirds() {
        ArrayList<Bird> allBirds = new ArrayList<>();
        for (int h = 0; h < 3; h++) {
            for (int i = 0; i < board[h].length; i++) {
                if (board[h][i] != null) {
                    allBirds.add(board[h][i]);
                }
            }
        }
        return allBirds;
    }
    
    public int getCachedFoodCount() {
        int total = 0;
        for (int h = 0; h < 3; h++) {
            for (int i = 0; i < board[h].length; i++) {
                if (board[h][i] != null) {
                    total += board[h][i].getCachedFood();
                }
            }
        }
        return total;
    }
    
    public int getLongestRow() {
        int maxLength = 0;
        
        for (int h = 0; h < 3; h++) {
            int currentLength = 0;
            int maxCurrentLength = 0;
            
            for (int i = 0; i < board[h].length; i++) {
                if (board[h][i] != null) {
                    currentLength++;
                    if (currentLength > maxCurrentLength) {
                        maxCurrentLength = currentLength;
                    }
                } else {
                    currentLength = 0;
                }
            }
            
            if (maxCurrentLength > maxLength) {
                maxLength = maxCurrentLength;
            }
        }
        
        return maxLength;
    }
    
    public boolean hasBird(Bird bird) {
        for (int h = 0; h < 3; h++) {
            for (int i = 0; i < board[h].length; i++) {
                if (board[h][i] == bird) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public ArrayList<Bird> getBirdsWithEggs() {
        ArrayList<Bird> birdsWithEggs = new ArrayList<>();
        for (int h = 0; h < 3; h++) {
            for (int i = 0; i < board[h].length; i++) {
                if (board[h][i] != null && board[h][i].getStoredEggs() > 0) {
                    birdsWithEggs.add(board[h][i]);
                }
            }
        }
        return birdsWithEggs;
    }
    
    public ArrayList<Bird> getBirdsWithoutEggs() {
        ArrayList<Bird> birdsWithoutEggs = new ArrayList<>();
        for (int h = 0; h < 3; h++) {
            for (int i = 0; i < board[h].length; i++) {
                if (board[h][i] != null && board[h][i].getStoredEggs() == 0) {
                    birdsWithoutEggs.add(board[h][i]);
                }
            }
        }
        return birdsWithoutEggs;
    }
    
    public ArrayList<Bird> getBirdsByNestType(String nestType) {
        ArrayList<Bird> birdsWithNest = new ArrayList<>();
        for (int h = 0; h < 3; h++) {
            for (int i = 0; i < board[h].length; i++) {
                if (board[h][i] != null && board[h][i].getNest().equals(nestType)) {
                    birdsWithNest.add(board[h][i]);
                }
            }
        }
        return birdsWithNest;
    }
    
    public int getTuckedCardCount() {
        int total = 0;
        for (int h = 0; h < 3; h++) {
            for (int i = 0; i < board[h].length; i++) {
                if (board[h][i] != null) {
                    total += board[h][i].getTuckedCards().size();
                }
            }
        }
        return total;
    }
    
    private int getHabitatIndex(String habitat) {
        switch (habitat.toLowerCase()) {
            case "forest": return 0;
            case "plains": return 1;
            case "wetlands": return 2;
            default: return -1;
        }
    }
}