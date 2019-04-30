package HW6;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class NLP{

    public static SortedSet<Map.Entry<String, Integer>> settt;
    public static TreeMap<String, Integer> corpus;

    public static SortedSet<Map.Entry<String,Double>> entriesSorted(Map<String,Double> corpus) {
        SortedSet<Map.Entry<String,Double>> sortedEntries = new TreeSet<Map.Entry<String,Double>>(
                        new Comparator<Map.Entry<String,Double>>() {
                            @Override 
                            public int compare(Map.Entry<String,Double> e1, Map.Entry<String,Double> e2) {
                                int res = e2.getValue().compareTo(e1.getValue());
                                return res != 0 ? res : 1;
                            }
                        }
                        );
        sortedEntries.addAll(corpus.entrySet());
        return sortedEntries;
    }

    public static SortedSet<Map.Entry<String,Integer>> entriesSortedByValues(Map<String,Integer> corpus) {
        SortedSet<Map.Entry<String,Integer>> sortedEntries = new TreeSet<Map.Entry<String,Integer>>(
                        new Comparator<Map.Entry<String,Integer>>() {
                            @Override 
                            public int compare(Map.Entry<String,Integer> e1, Map.Entry<String,Integer> e2) {
                                int res = e2.getValue().compareTo(e1.getValue());
                                return res != 0 ? res : 1;
                            }
                        }
                        );
        sortedEntries.addAll(corpus.entrySet());
        return sortedEntries;
    }

    public static double cosSimi(TreeMap<String, Double> v1, TreeMap<String, Double> v2) {
        double total = 0;
        double[] one = new double[v1.size()];
        double[] two = new double[v2.size()];
        int a = 0;
        for (Map.Entry<String, Double> entry : v1.entrySet()) {
            one[a]= entry.getValue(); a++;
        }

        a = 0;
        for (Map.Entry<String, Double> entry : v2.entrySet()) {
            two[a]= entry.getValue(); a++;
        }

        for (int i = 0; i < one.length; i++) {
            total += one[i] * two[i];
        }

        return total / (vecMag(v1) * vecMag(v2));
    }

    public static double vecMag (TreeMap<String, Double> map) {
        double count = 0;
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            count += Math.pow(entry.getValue(), 2);
        }
        return Math.sqrt(count);
    }

    public static TreeMap<String, Double> bagOfWords(File file, File folder) throws FileNotFoundException {
        int wordCount = 0;
        TreeMap<String, Double> words = new TreeMap<>();

        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            String token = scanner.next();
            if (!words.containsKey(token)) words.put(token, 1.0);
            else words.put(token, words.get(token) + 1);
            wordCount++;
        }

        for (Map.Entry<String, Double> entry : words.entrySet())
            words.put(entry.getKey(), entry.getValue() / wordCount);

        return words;
    }

    public static TreeMap<String, Double> tfidf (File file, File folder) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        TreeMap<String, Integer> ninetyEight = new TreeMap<>();
        TreeMap<String, Double> tf = new TreeMap<>();
        TreeMap<String, Double> idf = new TreeMap<>();
        TreeMap<String, Double> tf_idf = new TreeMap<>();

        while (scanner.hasNext()) {
            String token = scanner.next();
            if (!ninetyEight.containsKey(token)) ninetyEight.put(token.trim(), 1);
            else ninetyEight.put(token, ninetyEight.get(token) + 1);
        }
        scanner.close();

        double max = ninetyEight.get("the");
        for (Map.Entry<String, Integer> entry : ninetyEight.entrySet()) {
            tf.put(entry.getKey(), entry.getValue()/ max);
        }

        int count = 0;
        for (Map.Entry<String, Integer> entry : ninetyEight.entrySet()) {
            File[] listOfFiles = folder.listFiles();
            Scanner scr = null;

            for (File f : listOfFiles) {
                if (f.isFile()) {
                    scr = new Scanner(new File("D:\\Computer science java files\\CS540\\news\\"+f.getName()));
                    while (scr.hasNext()) {
                        String token = scr.next();
                        if (token.equals(entry.getKey())) {
                            count++;
                            break;
                        }
                    }
                    scr.close();
                }
            }

            idf.put(entry.getKey(), Math.log10(511.0 / count));
            count = 0;
        }

        for (Map.Entry<String, Double> entry : tf.entrySet()) {
            tf_idf.put(entry.getKey(), tf.get(entry.getKey()) * idf.get(entry.getKey()));
        }

        SortedSet<Map.Entry<String, Double>> result = entriesSorted(tf_idf);
//        int i = 1;
//        for (Map.Entry<String, Double> entry : result) {
//            System.out.printf("%d %s %f\n", i, entry.getKey(), tf.get(entry.getKey()) * idf.get(entry.getKey()));
//            i++;
//        }
        return tf_idf;
    }

    public static void main(String[] args) throws IOException {
        File folder = new File("D:\\Computer science java files\\CS540\\news");
        //        File[] listOfFiles = folder.listFiles();
        //        Scanner scanner = null;
        //        corpus = new TreeMap<>();
        //        String temp;
        //        int count = 0;
        //        
        //        for (File file : listOfFiles) {
        //            if (file.isFile()) {
        //                scanner = new Scanner(new File("D:\\Computer science java files\\CS540\\news\\"+file.getName()));
        //                while (scanner.hasNext()) {
        //                    count++;
        //                    temp = scanner.next();
        //                    if (!corpus.containsKey(temp)) corpus.put(temp.trim(), 1);
        //                    else corpus.put(temp, corpus.get(temp) + 1);
        //                }
        //                scanner.close();
        //            }
        //        }
        //        
        //        File file = new File("result.txt");
        //        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        //        BufferedWriter bw = new BufferedWriter(fw);
        //        
        //        settt = entriesSortedByValues(corpus);
        //        int i = 1;
        //        System.out.println("word tokens:" + count);
        //        System.out.println("word types:"+settt.size());
        //        for (Map.Entry<String, Integer> entry : settt) {
        //            //System.out.println(i+": "+entry.getKey()+" "+entry.getValue());
        //            bw.write(i+" "+entry.getKey()+" "+entry.getValue()+" \r\n");
        //            i++;
        //        }

        TreeMap<String, Double> bv1 = bagOfWords(new File("D:\\Computer science java files\\CS540\\news\\098.txt"), folder);
        TreeMap<String, Double> bv2 = bagOfWords(new File("D:\\Computer science java files\\CS540\\news\\297.txt"), folder);

        System.out.println(cosSimi(bv1, bv2));

        TreeMap<String, Double> nineeight = tfidf(new File("D:\\Computer science java files\\CS540\\news\\098.txt"), folder);
        TreeMap<String, Double> twoeightseven = tfidf(new File("D:\\Computer science java files\\CS540\\news\\297.txt"), folder);
        System.out.println(cosSimi(nineeight, twoeightseven));
    }
}