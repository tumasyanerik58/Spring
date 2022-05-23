import java.lang.reflect.Array;
import java.util.ArrayList;

public class Spring {

    private double k;

    public Spring(){
        this.k = 1;
    }

    public Spring(double number){
        this.k = number;
    }

    private void setStiffness(double number){
        this.k = number;
    }

    public double getStiffness(){
        return this.k;
    }

    private ArrayList<Double> coordinates(int t1, int t2, int dt, int x, int v, int m){
        ArrayList<Double> coordinates = null;
        double res = Math.sqrt(this.k / m);
        for (int i = t1; i <= t2; i += dt) {
            double singleCoord = x * Math.cos(res * i) + (v / res) * Math.sin(res * i);
            coordinates.add(singleCoord);
        }
        return coordinates;
    }

    public Spring inParallel(Spring that){
        double newK = this.k + that.getStiffness();
        return new Spring(newK);
    }

    public Spring inSeries(Spring that){
        double newK = 1 / (1/this.k + 1/that.getStiffness());
        return new Spring(newK);
    }

    public ArrayList<Double> firstMove(int t, int dt, int x, int v){
        return this.coordinates(0,t,dt,x,v,1);
    }

    public ArrayList<Double> secondMove(int t, int dt, int x){
        return this.coordinates(0,t,dt,x,0,1);
    }

    public ArrayList<Double> thirdMove(int t1,int t2, int dt, int x, int v){
        return this.coordinates(t1,t2,dt,x,v,1);
    }

    public ArrayList<Double> fourthMove(int t1,int t2, int dt, int x, int v, int m){
        return this.coordinates(t1,t2,dt,x,v,m);
    }

    class SpringArray{
        public static Double getInSeries(int index, String expression,ArrayList<Double> stiffnessArr) {
            Double defaultK = Double.valueOf(0);
            int i = index + 1;
            while (true){
                if(String.valueOf(expression.charAt(i)) == "]" || String.valueOf(expression.charAt(i)) == "}"){
                    i++;
                    break;
                } else if(String.valueOf(expression.charAt(i)) == "["){
                    if(String.valueOf(expression.charAt(i+1)) == "]"){
                        Double firstStiffness = stiffnessArr.get(0);
                        stiffnessArr.remove(0);
                        if(firstStiffness != null){
                            defaultK = defaultK + 1/firstStiffness;
                        } else {
                            defaultK = defaultK + 1;
                        }
                        Double newK = getInParallel(i,expression,stiffnessArr);
                        if(newK != null){
                            defaultK = defaultK + 1/newK;
                        }
                    }
                }  else if(String.valueOf(expression.charAt(i+1)) == "{"){
                    Double newK = getInSeries(i, expression, stiffnessArr);
                    if(newK != null){
                        defaultK = defaultK + 1/newK;
                    }
                }
            }
            return 1/defaultK;
        }

        public static Double getInParallel(int index, String expression,ArrayList<Double> stiffnessArr) {
            Double defaultK = Double.valueOf(0);
            int i = index + 1;
            while (true){
                if(String.valueOf(expression.charAt(i)) == "]" || String.valueOf(expression.charAt(i)) == "}"){
                    i++;
                    break;
                } else if(String.valueOf(expression.charAt(i)) == "["){
                    if(String.valueOf(expression.charAt(i+1)) == "]"){
                        Double firstStiffness = stiffnessArr.get(0);
                        stiffnessArr.remove(0);
                        if(firstStiffness != null){
                            defaultK = defaultK + firstStiffness;
                        } else {
                            defaultK = defaultK + 1;
                        }
                        Double newK = getInParallel(i,expression,stiffnessArr);
                        defaultK = defaultK + newK;
                    }
                }  else if(String.valueOf(expression.charAt(i+1)) == "{"){
                    Double newK = getInSeries(i, expression, stiffnessArr);
                    defaultK = defaultK + newK;
                }
            }
            return defaultK;
        }

        public static Spring equivalentSpring(String springExpr){
            Double k = null;
            if(String.valueOf(springExpr.charAt(0)) == "["){
                k = getInParallel(0 ,springExpr, null);
            } else {
                k = getInSeries(0, springExpr, null);
            }
            return new Spring(k);
        }

        public static Spring equivalentSpringWithSpring(String springExpr, ArrayList<Spring> springs){
            Double k = null;
            ArrayList<Double> stiffnessArr = null;
            for(Spring spring: springs){
                stiffnessArr.add(spring.getStiffness());
            }
            if(String.valueOf(springExpr.charAt(0)) == "["){
                k = getInParallel(0 ,springExpr, stiffnessArr);
            } else {
                k = getInSeries(0, springExpr, stiffnessArr);
            }
            return new Spring(k);
        }

        public static Double fftMock(double num){
            return Math.pow(num,2);
        }

        public static Number bitToNumber(String bit_num){
            ArrayList<Spring> bit_strings = null;
            for(int i = 1; i < 9; i++){
                for(int j = 0; j < 8;j++){
                    if(bit_num.charAt(j) != '0'){
                        bit_strings.add(new Spring(i));
                    }
                }
            }
            ArrayList<Double> transformedList = null;
            for(Spring spring: bit_strings){
                transformedList.add(fftMock(spring.getStiffness()));
            }
            Double result = Double.valueOf(0);
            for(Double number: transformedList){
                result = result + Math.pow(2,number);
            }
            return result;
        }
    }
}
