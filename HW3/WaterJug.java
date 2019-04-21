import java.util.*;

class State {
    int cap_jug1;
    int cap_jug2;
    int curr_jug1;
    int curr_jug2;
    int goal;
    int depth;
    State parentPt;

    public State(int cap_jug1, int cap_jug2, int curr_jug1, int curr_jug2, int goal, int depth, State pt) {
        this.cap_jug1 = cap_jug1;
        this.cap_jug2 = cap_jug2;
        this.curr_jug1 = curr_jug1;
        this.curr_jug2 = curr_jug2;
        this.goal = goal;
        this.depth = depth;
        this.parentPt = pt;
    }

    public ArrayList<State> getSuccessors() {  // get all successors and return them in proper order
        ArrayList<State> results = new ArrayList<>();
        State pt = new State(cap_jug1, cap_jug2, curr_jug1, curr_jug2, goal, depth, parentPt);
        if (curr_jug1 != cap_jug1) results.add(new State(cap_jug1, cap_jug2, cap_jug1, curr_jug2, goal, depth + 1, pt)); 
        if (curr_jug2 != cap_jug2) results.add(new State(cap_jug1, cap_jug2, curr_jug1, cap_jug2, goal, depth + 1, pt));
        if (curr_jug1 != 0) results.add(new State(cap_jug1, cap_jug2, 0, curr_jug2, goal, depth+ 1, pt));
        if (curr_jug2 != 0) results.add(new State(cap_jug1, cap_jug2, curr_jug1, 0, goal, depth+ 1, pt));
        if (curr_jug1 != 0 && curr_jug2 != cap_jug2) results.add(pour(curr_jug1, cap_jug1, curr_jug2, cap_jug2, goal, depth + 1, 0, pt));
        if (curr_jug2 != 0 && curr_jug1 != cap_jug1) results.add(pour(curr_jug2, cap_jug2, curr_jug1, cap_jug1, goal, depth + 1, 1, pt));
        Collections.sort(results, new OrderComparator());
        return results;
    }

    public boolean isGoalState() {  // determine if the state is a goal node or not and return boolean
        return (curr_jug1 == goal || curr_jug2 == goal) ? true : false;
    }

    public State pour(int src, int capSrc, int dest, int capDest, int goal, int depth, int flag, State pt) {
        if (src + dest <= capDest) {
            dest = src + dest; src = 0;
        } else {
            src = src - (capDest - dest); dest = capDest; 
        }
        return (flag == 0) ? new State(capSrc, capDest, src, dest, goal, depth, pt) : new State(capDest, capSrc, dest, src, goal, depth, pt);
    }

    public String getOrderedPair() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.curr_jug1); builder.append(this.curr_jug2);
        return builder.toString().trim();
    }

    public void uniformSearch (ArrayList<State> open, ArrayList<State> end, int flag) {
        System.out.println(getOrderedPair());
        while (!open.isEmpty()) {
            State cur = flag == 0 ? open.remove(0) :open.remove(open.size() - 1);  // 0 bfs, 1 dfs 
            end.add(cur);
            //System.out.println("depth of "+ cur.getOrderedPair()+": "+cur.depth);
            ArrayList<State> temp = cur.getSuccessors();
            for (int i = 0; i < temp.size(); i++)
                if (!end.contains(temp.get(i)) && !open.contains(temp.get(i))) open.add(temp.get(i));
            if (cur.isGoalState()) {
                System.out.println(cur.getOrderedPair() + " Goal");
                System.out.println("Path"+path(cur));
                break;
            }
            System.out.print(cur.getOrderedPair()); System.out.print(" "+open.toString()+" "); System.out.println(end.toString());            
        }
    }
    
    public void dldfs (ArrayList<State> origOpen, ArrayList<State> origEnd, int depth) {
        for (int i = 0; i <= depth; i++) {  // run depth time
            ArrayList<State> open = new ArrayList<>(origOpen);  // init 
            ArrayList<State> end = new ArrayList<>(origEnd);  // init
            System.out.println(i+":"+getOrderedPair());
            while (!open.isEmpty()) {
                State cur = open.remove(open.size() - 1);
                end.add(cur);
                if (cur.depth < i) {
                    ArrayList<State> temp = cur.getSuccessors();
                    for (int j = 0; j < temp.size(); j++)
                        if (!end.contains(temp.get(j)) && !open.contains(temp.get(j))) open.add(temp.get(j)); 
                }
                if (cur.isGoalState()) {
                    System.out.println(i + ":" +cur.getOrderedPair() + " Goal");
                    System.out.println("Path"+path(cur));
                    return;
                }
                System.out.print(i+"："+cur.getOrderedPair()); System.out.print(" "+open.toString()+" "); System.out.println(end.toString()); 
            }
        }
    }

    public String path (State state) {
        if (state == null) return "";
        else return state.path(state.parentPt) +" "+ state.getOrderedPair();
    }

    @Override
    public boolean equals (Object o) {
        return curr_jug1 == ((State)o).curr_jug1 && curr_jug2 == ((State)o).curr_jug2 ? true : false;
    }

    @Override
    public String toString () {
        return getOrderedPair();
    }

    public void printState(int option, int depth) {  // print a State based on option (flag)
        if (option == 1 || option == 2) {
            ArrayList<State> states = getSuccessors();
            if (option == 1) 
                for (int i = 0; i < states.size(); i++) System.out.println(states.get(i).getOrderedPair());
            else 
                for (int i = 0; i < states.size(); i++) System.out.println(states.get(i).isGoalState() ? 
                                states.get(i).getOrderedPair() + " " + true : states.get(i).getOrderedPair() + " " + false); 
        } else if (option == 3 || option == 4 || option == 5) {
            ArrayList<State> end = new ArrayList<>(); ArrayList<State> open = new ArrayList<>();
            open.add(new State(cap_jug1, cap_jug2, curr_jug1, curr_jug2, goal, 0, null));
            if (option == 3 ) uniformSearch(open, end, 0);
            else if (option == 4) uniformSearch(open, end, 1);
            else dldfs(open, end, depth);
        } 
    }
}

class OrderComparator implements Comparator<State> {
    @Override
    public int compare(State o1, State o2) {
        return Integer.parseInt(o1.getOrderedPair()) - Integer.parseInt(o2.getOrderedPair());    
    }
}

public class WaterJug {
    public static void main(String args[]) {
        if (args.length != 6) {
            System.out.println("Invalid Number of Input Arguments");
            return;
        }
        int flag = Integer.valueOf(args[0]);
        int cap_jug1 = Integer.valueOf(args[1]);
        int cap_jug2 = Integer.valueOf(args[2]);
        int curr_jug1 = Integer.valueOf(args[3]);
        int curr_jug2 = Integer.valueOf(args[4]);
        int goal = Integer.valueOf(args[5]);

        int option = flag / 100;
        int depth = flag % 100;

        if (option < 1 || option > 5) {
            System.out.println("Invalid flag input");
            return;
        }
        if (cap_jug1 > 9 || cap_jug2 > 9 || curr_jug1 > 9 || curr_jug2 > 9) {
            System.out.println("Invalid input: 2-digit jug volumes");
            return;
        }
        if (cap_jug1 < 0 || cap_jug2 < 0 || curr_jug1 < 0 || curr_jug2 < 0) {
            System.out.println("Invalid input: negative jug volumes");
            return;
        }
        if (cap_jug1 < curr_jug1 || cap_jug2 < curr_jug2) {
            System.out.println("Invalid input: jug volume exceeds its capacity");
            return;
        }
        State init = new State(cap_jug1, cap_jug2, curr_jug1, curr_jug2, goal, 0, null);
        init.printState(option, depth);
    }
}