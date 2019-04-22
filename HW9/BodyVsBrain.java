import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

class BodyVsBrain {
    
    public static ArrayList<Double> body = new ArrayList<>();
    public static ArrayList<Double> brain = new ArrayList<>();

    public static void process () throws IOException {
        BufferedReader bReader = new BufferedReader(new FileReader("data.csv"));
        String line = bReader.readLine();
        
        while ((line = bReader.readLine()) != null) {
            String[] tokens = line.split(",");
            body.add(Double.parseDouble(tokens[0]));
            brain.add(Double.parseDouble(tokens[1]));
        }
    }
    
    public static double[] sd () {
        double[] result = new double[4];
        
        double torsol = 0, head = 0;
        for (int i = 0; i < body.size(); i++) {
            torsol += body.get(i);
            head += brain.get(i);
        }
        
        result[0] = torsol / body.size(); result[2] = head / body.size();
        
        double total = 0, sum = 0;
        for (int i = 0; i < body.size(); i++) {
            total += Math.pow(body.get(i) - result[0], 2);
            sum += Math.pow(brain.get(i) - result[2], 2);
        }
        
        result[1] = Math.sqrt(total / (body.size() - 1)); result[3] = Math.sqrt(sum / (body.size() - 1)); 
        return result;
    }
    
    public static double mse (double beta0, double beta1) {
        double tol = 0;
        for (int i = 0; i < body.size(); i++) {
            tol += Math.pow(beta0 + beta1 * body.get(i) - brain.get(i), 2);
        }
        return tol / body.size();
    }
    
    public static double[] gd (double beta0, double beta1) {
        double[] result = new double[2]; 
        double tol = 0, sum = 0;
        for (int i = 0; i < body.size(); i++) {
            tol += beta0 + beta1 * body.get(i) - brain.get(i);
            sum += (beta0 + beta1 * body.get(i) - brain.get(i)) * body.get(i);
        }
        result[0] = 2 * tol / body.size(); result[1] = 2 * sum / body.size(); 
        return result;
    }
    
    public static void gdi (double yita, double T) {
        double beta0 = 0, beta1 = 0;
        double temp0 = 0, temp1 = 0;
        for (int i = 0; i < T; i++) {
            temp0 = beta0 - (yita * gd(beta0, beta1)[0]);
            temp1 = beta1 - (yita * gd(beta0, beta1)[1]);
            beta0 = temp0; beta1 = temp1;
            System.out.printf("%d %.4f %.4f %.4f\n", i+1, beta0, beta1, mse(beta0, beta1));
        }
    }
    
    public static double[] closedForm () {
        double[] result = new double[3];
        double xbar = 0, ybar = 0;
        for (int i =0; i < body.size(); i++) {
            xbar += body.get(i);
            ybar += brain.get(i);
        }
        xbar = xbar / body.size(); ybar = ybar /brain.size();
        
        double tol = 0, sum = 0;
        for (int i = 0; i < body.size(); i++) {
            tol += (body.get(i) - xbar) * (brain.get(i) - ybar);
            sum += Math.pow(body.get(i) - xbar, 2);
        }
        result[1] = tol / sum; result[0] = ybar - result[1] * xbar;result[2] = mse(result[0], result[1]);
        return result;
    }
    
    public static void normalize () {
        double[] param = sd();
        for (int i = 0; i < body.size(); i++)
            body.set(i, (body.get(i) - param[0]) / param[1]);
    }
    
    public static void sgd (double yita, double T) {
        double beta0 = 0, beta1 = 0;
        double temp0 = 0, temp1 = 0;
        
        for (int i = 0; i < T; i++) {
            temp0 = beta0 - (yita * helper(beta0, beta1)[0]);
            temp1 = beta1 - (yita * helper(beta0, beta1)[1]);
            beta0 = temp0; beta1 = temp1;
            System.out.printf("%d %.4f %.4f %.4f\n", i+1, beta0, beta1, mse(beta0, beta1));
        }
    }
    
    public static double[] helper (double beta0, double beta1) {
        int rnd = (int)(Math.random() * body.size());
        double[] reuslt = new double[2];
        
        reuslt[0] = 2 * (beta0 + beta1 * body.get(rnd) - brain.get(rnd));
        reuslt[1] = 2 * (beta0 + beta1 * body.get(rnd) - brain.get(rnd)) * body.get(rnd);
        
        return reuslt;
    }
    
    public static void main(String[] args) throws IOException {
        int flag = Integer.valueOf(args[0]);
        process();  // assign values to global fields 
        
        if (flag == 100) {
            double[] result = sd();
            System.out.println(body.size());
            System.out.printf("%.4f %.4f\n", result[0], result[1]);
            System.out.printf("%.4f %.4f\n", result[2], result[3]);
            
        } else if (flag == 200) {
            double beta0 = Double.parseDouble(args[1]), beta1 = Double.parseDouble(args[2]);
            System.out.printf("%.4f\n", mse(beta0, beta1));
            
        } else if (flag == 300) {
            double beta0 = Double.parseDouble(args[1]), beta1 = Double.parseDouble(args[2]);
            double[] result = gd(beta0, beta1);
            System.out.printf("%.4f\n", result[0]);
            System.out.printf("%.4f\n", result[1]);
            
        } else if (flag == 400) {
            double yita = Double.parseDouble(args[1]), T = Double.parseDouble(args[2]);
            gdi (yita, T);
            
        } else if (flag == 500) {
            double[] result = closedForm();
            System.out.printf("%.4f %.4f %.4f", result[0], result[1], result[2]);
            
        } else if (flag == 600) {
            double body = Double.parseDouble(args[1]);
            double[] result = closedForm();
            
            // fix me
            
        } else if (flag == 700) {
            double yita = Double.parseDouble(args[1]), T = Double.parseDouble(args[2]);
            normalize();
            gdi (yita, T);
            
        } else if (flag == 800) {
            double yita = Double.parseDouble(args[1]), T = Double.parseDouble(args[2]);
            normalize();
            sgd(yita, T);
        }
    }
    

}
