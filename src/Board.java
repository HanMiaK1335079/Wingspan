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
        
        for (int i = 0; i < board[h].length; i++) {
            if (board[h][i] == null) {
                board[h][i] = bird;
                return true;
            }
        }
        return false;
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
    
    public int calculateScore() {
        int total = 0;
        
        for (int h = 0; h < 3; h++) {
            for (int i = 0; i < board[h].length; i++) {
                if (board[h][i] != null) {
                    total += board[h][i].getPoints();
                }
            }
        }
        
        for (int h = 0; h < 3; h++) {
            for (int i = 0; i < board[h].length; i++) {
                if (board[h][i] != null) {
                    total += board[h][i].getStoredEggs();
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