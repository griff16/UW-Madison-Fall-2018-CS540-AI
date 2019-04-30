package HW6;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class PCA {
    
    public static double retail = 0, hopow = 0;

    public static double calcVec (String[] ary) {
        int count = 0;
        for (int i = ary.length - 1; ary.length - 1 - i < 11; i--) {
            if (ary.length - 1 - i == 6) hopow += Double.parseDouble(ary[i]);
            if (ary.length - 1 - i == 10) retail += Double.parseDouble(ary[i]);
            count += Math.pow(Double.parseDouble(ary[i]), 2);
        }
        return Math.sqrt(count);
    }
    
    public static double calcMu (ArrayList<Double> vectors) {
        double total = 0;
        for (Double aDouble : vectors) total += aDouble;
        return total / 356;
    }
    
    public static double calcStanDe (ArrayList<Double> vectors, double mu) {
        double total = 0;
        for (Double aDouble : vectors) total += Math.pow(aDouble - mu, 2);
        return Math.sqrt(total / 355);
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<Double> vectors = new ArrayList<>();
        Scanner scr = new Scanner(new File("cardata.csv"));
        scr.nextLine();
        while (scr.hasNextLine()) {
            String line = scr.nextLine();
            vectors.add(calcVec(line.split(",")));
        }
        
        double mu = calcMu(vectors);
        double deviation = calcStanDe(vectors, mu);
        System.out.println(deviation);
    }

}
