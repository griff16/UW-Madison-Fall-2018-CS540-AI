import java.util.*;
import java.lang.Math;

class State {
    static double T = 100;
    char[] board;

    public State(char[] arr) {
        this.board = Arrays.copyOf(arr, arr.length);
    }

    public void printState(int option, int iteration, int seed) {
        if (option == 1) {
            System.out.println(heuCost());
        } else if (option == 2) {
            ArrayList<String> list = heuNext(0);
            if (list.size() > 1)
                for (int i = 0; i < list.size(); i++) System.out.println(list.get(i));
        } else if (option == 3) {
            Random rng = new Random();
            if (seed != -1) rng.setSeed(seed);
            System.out.printf("%d:%s %s\n", 0, new String(board), heuCost());
            if (heuCost() == 0) {System.out.println("solved"); return;}
            for (int i = 0; i < iteration; i++) {
                ArrayList<String> list = heuNext(0);
                int r = rng.nextInt(list.size() - 1);
                System.out.printf("%d:%s %s\n", i+1, list.get(r), list.get(list.size() - 1));
                if (list.get(list.size() - 1).equals("0")) {System.out.println("solved"); return;}
                board = list.get(r).toCharArray();
            }
        } else if (option == 4) {
            System.out.printf("%d:%s %s\n", 0, new String(board), heuCost());
            if (heuCost() == 0) {System.out.println("solved"); return;}
            for (int i = 0; i < iteration; i++) {
                ArrayList<String> list = heuNext(1);
                if (list == null) {System.out.println("Local optimum"); return;}
                System.out.printf("%d:%s %s\n", i+1, list.get(0), list.get(1));
                if (list.get(list.size() - 1).equals("0")) {System.out.println("solved"); return;}
                board = list.get(0).toCharArray();
            }
        } else {  // SA
            Random rng = new Random();
            if (seed != -1) rng.setSeed(seed);
            System.out.printf("%d:%s %s\n", 0, new String(board), heuCost());
            if (heuCost() == 0) {System.out.println("solved"); return;}
            
            for (int i = 0; i < iteration; i++) {
                ArrayList<String> list = sa(rng);
                if(list == null) continue;
                System.out.printf("%d:%s %s\n", i+1, list.get(0), list.get(list.size() - 1));
                if (list.get(list.size() - 1).equals("0")) {System.out.println("solved"); return;}
                board = list.get(0).toCharArray();
            }
        }
    }

    public int heuCost() {
        int cost = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = i + 1; j < board.length; j++) {
                if (board[i] == board[j]) cost++;
                if (Math.abs(board[i] - board[j]) == j - i) cost++;
            }
        }
        return cost;
    }

    public ArrayList<String> heuNext (int option) {
        ArrayList<String> result = new ArrayList<>();
        ArrayList<Integer> heuRec = new ArrayList<>();
        int lowest = heuCost();

        for (int i = 0; i < board.length; i++) {
            char orig = board[i];
            for (int j = 0; j < board.length; j++) {
                if (orig == (char)(j+'0')) continue;  // if it is the same, then skip
                board[i] = (char)(j+'0');
                int temp = heuCost();
                if (option == 0) {
                    if (temp <= lowest) {
                        result.add(new String(board)); 
                        heuRec.add(temp);
                        lowest = temp;
                    }
                } else {  // hill climbing
                    if (temp < lowest) {
                        result.add(new String(board)); 
                        result.add(temp+"");
                        lowest = temp;
                        return result;
                    } 
                }
            }
            board[i] = orig;
        }
        if(option == 1) return null;

        for (int i = 0; i < heuRec.size(); i++) {
            if (heuRec.get(i) != lowest) {
                heuRec.remove(i);
                result.remove(i);
                i--;
            }
        }
        result.add(lowest+"");
        return result;
    }
    
    public ArrayList<String> sa (Random rng) {
        ArrayList<String> result = new ArrayList<>();
        
        for (int i = 0; i < board.length; i++, T--) {
            char orig = board[i];
            int index = rng.nextInt(7); int value = rng.nextInt(7); double prob = rng.nextDouble();
            int cur = heuCost();
            board[index] = (char)(value + '0');
            int nextState = heuCost();
            
            if (nextState < cur) {
                result.add(new String(board)); result.add(nextState+"");
                return result;
            } else {
                if (Math.exp(-Math.abs(nextState - cur) / T) >= prob) {
                    result.add(new String(board)); result.add(nextState+"");
                    return result;
                }
            }
        }
        return null;
    }
}

public class EightQueen {
    public static void main(String args[]) {
        if (args.length != 2 && args.length != 3) {
            System.out.println("Invalid Number of Input Arguments");
            return;
        }

        int flag = Integer.valueOf(args[0]);
        int option = flag / 100;
        int iteration = flag % 100;
        char[] board = new char[8];
        int seed = -1;
        int board_index = -1;

        if (args.length == 2 && (option == 1 || option == 2 || option == 4)) {
            board_index = 1;
        } else if (args.length == 3 && (option == 3 || option == 5)) {
            seed = Integer.valueOf(args[1]);
            board_index = 2;
        } else {
            System.out.println("Invalid Number of Input Arguments");
            return;
        }

        if (board_index == -1) return;
        for (int i = 0; i < 8; i++) {
            board[i] = args[board_index].charAt(i);
            int pos = board[i] - '0';
            if (pos < 0 || pos > 7) {
                System.out.println("Invalid input: queen position(s)");
                return;
            }
        }

        State init = new State(board);
        init.printState(option, iteration, seed);
    }
}
