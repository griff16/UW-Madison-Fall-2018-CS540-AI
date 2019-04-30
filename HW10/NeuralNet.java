import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class NeuralNet {
    static double[] weights = new double[13];
    static double[] inputs = new double[5];
    static double[] hidden = new double[3];
    static double act = 0;
     
    public static void calcNet() {
        for (int i = 0, j = 0; i < weights.length - 3; i++, j++) {
            if (i < (weights.length - 3) / 2 ) hidden[1] += inputs[j % 5] * weights[i];
            else hidden[2] += inputs[j % 5] * weights[i];
        }
        hidden[1] = 1 / (1 + Math.exp(-hidden[1]));
        hidden[2] = 1 / (1 + Math.exp(-hidden[2]));
        
        double temp = hidden[0] * weights[10] + hidden[1] * weights[11] + hidden[2] * weights[12];
        act = 1 / (1 + Math.exp(-temp));
    }
    
    public static double calcSigmoid (double y) {
        return act * (act - y) * (1 - act);
    }
    
    public static double[] calcHidden (double y, double layer1) {
        double[] result = new double[2];
        result[0] = layer1 * weights[(weights.length  - 3) + 1] * hidden[1] * (1 - hidden[1]);
        result[1] = layer1 * weights[(weights.length  - 3) + 2] * hidden[2] * (1 - hidden[2]);
        return result;
    }
    
    public static ArrayList<ArrayList<Double>> gradient (double y) {
        ArrayList<ArrayList<Double>> list = new ArrayList<>();
        list.add(new ArrayList<>());list.add(new ArrayList<>());list.add(new ArrayList<>());
        double layer1 = calcSigmoid(y);
        double[] layer2 = calcHidden(y, layer1);
        
        for (int i = 0, j = 0; i < weights.length && j <inputs.length; i++) {
            if(i < 3) {
                list.get(0).add(layer1 * hidden[i]);
            } else {
                list.get(1).add(layer2[0] * inputs[j]);
                list.get(2).add(layer2[1] * inputs[j]);
                j++;
            }
        }
        return list;
    }
    
    private static double helper(double[] sinputs, double[] shidden) {
        shidden[0] = 1;
        for (int i = 0, j = 0; i < weights.length - 3; i++, j++) {
            if (i < (weights.length - 3) / 2 ) shidden[1] += sinputs[j % 5] * weights[i];
            else shidden[2] += sinputs[j % 5] * weights[i];
        }
        
        shidden[1] = 1 / (1 + Math.exp(-shidden[1]));
        shidden[2] = 1 / (1 + Math.exp(-shidden[2]));
        
        double temp = shidden[0] * weights[10] + shidden[1] * weights[11] + shidden[2] * weights[12];
        return 1 / (1 + Math.exp(-temp));
    }
    
    public static double calError (String file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        double E = 0;
        double[] sinputs = new double[5];
        double[] shidden = new double[3];
        sinputs[0] = 1; 
        
        while (line != null) {
            String[] temp = line.split(",");
            for (int i = 1; i < temp.length; i++) sinputs[i] = Double.parseDouble(temp[i - 1]);
            double y = Double.parseDouble(temp[temp.length - 1]);
            
            E += Math.pow(helper(sinputs, shidden) - y, 2) / 2;
            shidden[1] = 0; shidden[2] = 0;
            line = br.readLine();
        }
        return E;
    }
    
    public static void train (String file, double yita) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        
        while (line != null) {
            String[] tokens = line.split(",");
            for (int i = 1; i < tokens.length; i++) inputs[i] = Double.parseDouble(tokens[i - 1]);
            double y = Double.parseDouble(tokens[tokens.length - 1]);

            calcNet();
            ArrayList<ArrayList<Double>> list = gradient(y);
            
            int index = 0, cur = 0;
            for (int i = 0; i < weights.length; i++) {
                if (i < 5) 
                    weights[i] = weights[i] - yita * list.get(1).get(i);
                else if (i >= 5 && i < 10) 
                    weights[i] = weights[i] - yita * list.get(2).get(index++);
                else 
                    weights[i] = weights[i] - yita * list.get(0).get(cur++);
            }
            hidden[1] = 0; hidden[2] = 0;
  
            String reuslt = "";
            for (int i = 0; i < weights.length; i++) 
                reuslt += String.format("%.5f ",weights[i]);
                //System.out.printf("%.5f ",weights[i]);
            System.out.print(reuslt.trim());
            System.out.printf("\n%.5f\n", calError("eval.csv"));
            line = br.readLine();
        }
    }
    
    public static void last (String file, double yita) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();

        while (line != null) {
            String[] tokens = line.split(",");
            for (int i = 1; i < tokens.length; i++) inputs[i] = Double.parseDouble(tokens[i - 1]);
            double y = Double.parseDouble(tokens[tokens.length - 1]);

            calcNet();
            ArrayList<ArrayList<Double>> list = gradient(y);
            
            int index = 0, cur = 0;
            for (int i = 0; i < weights.length; i++) {
                if (i < 5) 
                    weights[i] = weights[i] - yita * list.get(1).get(i);
                else if (i >= 5 && i < 10) 
                    weights[i] = weights[i] - yita * list.get(2).get(index++);
                else 
                    weights[i] = weights[i] - yita * list.get(0).get(cur++);
            }
            hidden[1] = 0; hidden[2] = 0;
            line = br.readLine();
        }
    }
    
    public static void main(String[] args) throws IOException {
        
        // initializing data
        int flag = Integer.valueOf(args[0]);
        int index = 0;
        inputs[0] = 1; hidden[0] = 1;  // bias
        for (int i = 1; i < 14; i++) {
            weights[index] = Double.parseDouble(args[i]);
            index++;
        }
        if ((flag != 500 && flag != 600) || flag == 100) {
            int cur = 1;
            for (int i = 14; i < 18; i++) {
                inputs[cur] = Double.parseDouble(args[i]);
                cur++;
            }
        }
        
        if (flag == 100) {  // forward propagation
            calcNet();
            System.out.printf("%.5f %.5f\n%.5f\n", hidden[1], hidden[2], act);
            
        } else if (flag == 200) {  // backward propagation
            double y = Double.parseDouble(args[args.length - 1]);
            calcNet();
            System.out.printf("%.5f\n", calcSigmoid(y));
            
        } else if (flag == 300) {  
            double y = Double.parseDouble(args[args.length - 1]);
            calcNet();
            double[] result = calcHidden(y, calcSigmoid(y));
            System.out.printf("%.5f %.5f\n", result[0], result[1]);
            
        } else if (flag == 400) {
            String result = "";
            double y = Double.parseDouble(args[args.length - 1]);
            calcNet();
            ArrayList<ArrayList<Double>> list = gradient(y);
            for (int i = 0; i < list.size(); i++) {
                for (Double val : list.get(i)) result += String.format("%.5f ",val);
                System.out.print(result.trim());
                result = "";
                System.out.println();
            }
            
        } else if (flag == 500) {
            double yita = Double.parseDouble(args[args.length - 1]);
            train("train.csv", yita);
            
        } else if (flag == 600) {
            double correct = 0, total = 0;
            double yita = Double.parseDouble(args[args.length - 1]);
            last("train.csv", yita);
            
            BufferedReader br = new BufferedReader(new FileReader("test.csv"));
            String line = br.readLine();
            double[] sinputs = new double[5]; sinputs[0] = 1;
            double[] shidden = new double[3]; 
            while (line != null) {
                String[] tokens = line.split(",");
                for (int i = 1; i < tokens.length; i++) sinputs[i] = Double.parseDouble(tokens[i - 1]);
                double y = Double.parseDouble(tokens[tokens.length - 1]);
                
                double result = helper(sinputs, shidden);
                int predict = ((int)(result * 10)) >= 5 ? 1 : 0;
                correct = y == predict ? correct + 1 : correct; 
                System.out.printf("%d %d %.5f\n", (int)y, predict, result);
                
                shidden[1] = 0; shidden[2] = 0;
                total++; 
                line = br.readLine();
            }
            System.out.println(correct / total);
        }
        
        
    }

}
