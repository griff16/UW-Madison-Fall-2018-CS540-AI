

import java.util.*;
import java.io.*;

public class Chatbot{
    private static int n = 228548;
    private static int v = 4700;
    private static String filename = "./corpus.txt";
    
    
    private static ArrayList<Integer> readCorpus(){
        ArrayList<Integer> corpus = new ArrayList<Integer>();
        try{
            File f = new File(filename);
            Scanner sc = new Scanner(f);
            while(sc.hasNext()){
                if(sc.hasNextInt()){
                    int i = sc.nextInt();
                    corpus.add(i);
                }
                else{
                    sc.next();
                }
            }
        }
        catch(FileNotFoundException ex){
            System.out.println("File Not Found.");
        }
        return corpus;
    }
    
    public static double calcp (int count) {
        return (count + 1.0) / (n+v); 
    }
    
    public static double[] two (ArrayList<Integer> corpus, double r) {
        double[] p = new double[4700];
        double[] ary = new double[3];
        double prev = 0;
        double end = 0;
        
        for (int i = 0; i < 4700; i++) {
            int count = 0;
            for (Integer index : corpus) if (index == i) count++;
            p[i] = calcp(count);
            
            prev = end;
            end += p[i];
            if ((r <= end && i == 0) || (r <= end && r > prev)) {
                ary[0] = i; ary[1] = prev; ary[2] = end;
                return ary; 
            }
        }
        
        return null;
    }
    
    public static double[] four (ArrayList<Integer> corpus, double r, int h) {
        int count = 0;
        double[] ary = new double[3];
        double[] prob = new double[4700];
        double prev = 0;
        double end = prob[0];
        
        ArrayList<Integer> words_after_h = new ArrayList<Integer>();
        for (int i = 0; i < corpus.size() - 1; i++) {
            if (corpus.get(i) == h ) {
                words_after_h.add(corpus.get(i+1));
            }
        }
        
        
        for (int i = 0; i < 4700; i++, count = 0) {
            for (Integer num : words_after_h) {
                if (num == i) count++;
            }
            prob[i] = (count + 1.0) / (words_after_h.size() + v);
            
            prev = end;
            end += prob[i];
            if ((r <= end && r > prev) || (r <= end && r >= 0 && i == 0)) {
                ary[0] = i; ary[1] = prev; ary[2] = end;
                return ary;
            }
        }
        
        System.out.println("undefined");
        return null;
    }
    
    public static double[] six (ArrayList<Integer> corpus, double r, int h1, int h2) {
        double[] prob = new double[4700];
        double[] ary = new double[3];
        double prev = 0;
        double end = 0;
        int count = 0;
        
        ArrayList<Integer> words_after_h1h2 = new ArrayList<Integer>();
        for (int i = 0; i < corpus.size() - 3; i++) {
            if (corpus.get(i) == h1 && corpus.get(i+1) == h2) {
                words_after_h1h2.add(corpus.get(i+2));
            }
        }
        
        for (int i = 0; i < 4700; i++, count = 0) {
            for (Integer num : words_after_h1h2) {
                if (num == i) count++;
            }
            prob[i] = (count + 1.0) / (words_after_h1h2.size() + v);
            
            prev = end;
            end += prob[i];
            if ((r <= end && r > prev) || (r <= end && r >= 0 && i == 0)) {
                ary[0] = i; ary[1] = prev; ary[2] = end;
                return ary;
            }
        }
        
        System.out.println("Undefined");
        return null;
    }
    
    static public void main(String[] args){
        ArrayList<Integer> corpus = readCorpus();
		int flag = Integer.valueOf(args[0]);
        
        if(flag == 100){
			int w = Integer.valueOf(args[1]);
            int count = 0;
            
            //TODO count occurence of w
            for (Integer index : corpus) if (index == w) count++;
            System.out.println(count);
            System.out.printf("%.7f\n", calcp(count));
            
        }
        else if(flag == 200){
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            
            //TODO generate
            double[]ary = two(corpus, (double)n1 / n2);
            System.out.println((int)ary[0]);
            System.out.printf("%.7f\n", ary[1]);
            System.out.printf("%.7f\n", ary[2]);
        }
        else if(flag == 300){
            int h = Integer.valueOf(args[1]);
            int w = Integer.valueOf(args[2]);
            int count = 0;
            ArrayList<Integer> words_after_h = new ArrayList<Integer>();

            //TODO
            for (int i = 0; i < corpus.size() - 1; i++) {
                if (corpus.get(i) == h) {
                    words_after_h.add(corpus.get(i+1));
                    if (corpus.get(i+1) == w) count++;
                }
            }

            System.out.println(count);
            System.out.println(words_after_h.size());
            System.out.printf("%.7f",(count + 1.0) / (words_after_h.size() + v));
        }
        else if(flag == 400){
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            int h = Integer.valueOf(args[3]);
            
            //TODO
            double[]ary = four(corpus, (double)n1 / n2, h);
            System.out.println((int)ary[0]);
            System.out.printf("%.7f\n", ary[1]);
            System.out.printf("%.7f\n", ary[2]);
        }
        else if(flag == 500){
            int h1 = Integer.valueOf(args[1]);
            int h2 = Integer.valueOf(args[2]);
            int w = Integer.valueOf(args[3]);
            int count = 0;
            ArrayList<Integer> words_after_h1h2 = new ArrayList<Integer>();
            
            //TODO
            for (int i = 0; i < corpus.size() - 3; i++) {
                if (corpus.get(i) == h1 && corpus.get(i + 1) == h2) {
                    words_after_h1h2.add(corpus.get(i + 2));
                    if (corpus.get(i + 2) == w) count++;
                }
            }
            
            System.out.println(count);
            System.out.println(words_after_h1h2.size());
            System.out.printf("%.7f",(count + 1.0) / (words_after_h1h2.size() + v));
        }
        else if(flag == 600){
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            int h1 = Integer.valueOf(args[3]);
            int h2 = Integer.valueOf(args[4]);
            
            //TODO
            double[]ary = six(corpus, (double)n1 / n2, h1, h2);
            System.out.println((int)ary[0]);
            System.out.printf("%.7f\n", ary[1]);
            System.out.printf("%.7f\n", ary[2]);
        }
        else if(flag == 700){
            int seed = Integer.valueOf(args[1]);
            int t = Integer.valueOf(args[2]);
            int h1=0,h2=0;

            Random rng = new Random();
            if (seed != -1) rng.setSeed(seed);

            if(t == 0){
                // TODO Generate first word using r
                double r = rng.nextDouble();
                double[] ary = two(corpus, r);
                h1 = (int)ary[0];
                System.out.println(h1);
                if(h1 == 9 || h1 == 10 || h1 == 12){
                    return;
                }

                // TODO Generate second word using r
                r = rng.nextDouble();
                double[] ary1 = four(corpus, r, h1);
                h2 = (int)ary1[0];
                System.out.println(h2);
            }
            else if(t == 1){
                h1 = Integer.valueOf(args[3]);
                
                // TODO Generate second word using r
                double r = rng.nextDouble();
                double[] ary = four(corpus, r, h1);
                h2 = (int)ary[0];
                System.out.println(h2);
            }
            else if(t == 2){
                h1 = Integer.valueOf(args[3]);
                h2 = Integer.valueOf(args[4]);
            }

            while(h2 != 9 && h2 != 10 && h2 != 12){
                double r = rng.nextDouble();
                int w  = 0;

                // TODO Generate new word using h1,h2
                double[] ary = six(corpus, r, h1, h2);
                w = (int)ary[0];
                System.out.println(w);
                h1 = h2;
                h2 = w;
            }
        }

        return;
    }
}
