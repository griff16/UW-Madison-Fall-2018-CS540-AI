import java.util.LinkedList;
import java.util.Queue;

public class Number {

    public static String getStep(int x, int y) {
        int depth = 0;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(x);
        while (!queue.isEmpty()) {
            depth++;
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int pop = queue.poll();
                if (pop + 1 == y || pop - 1 == y || pop * 3 == y || pop * pop == y) return depth + "";
                if (pop + 1 > 0 && pop + 1 < 100) queue.add(pop + 1);
                if (pop - 1 > 0 && pop - 1 < 100) queue.add(pop - 1); 
                if (pop * 3 > 0 && pop * 3 < 100) queue.add(pop * 3); 
                if (pop * 1 > 0 && pop * pop < 100) queue.add(pop * pop);
            }
        }
        return depth+"";
    }

    public static void main(String[] args) {
        if (args.length != 2) return;
        System.out.println(getStep(Integer.parseInt(args[0]), Integer.parseInt(args[1])));
    }
}
