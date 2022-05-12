import java.util.ArrayList;

public class Spring {

    private double k;

    private ArrayList<Double> coordinates(int t1, int t2, int dt, int x, int v, int m){
        ArrayList<Double> coordinates = null;
        double res = Math.sqrt(this.k / m);
        for (int i = t1; i <= t2; i += dt) {
            double singleCoord = x * Math.cos(res * i) + (v / res) * Math.sin(res * i);
            coordinates.add(singleCoord);
        }
        return coordinates;
    }

    public Spring(){
        this.k = 1;
    }

    public Spring(int number){
        this.k = number;
    }

    private void setStiffness(int number){
        this.k = number;
    }

    public double getStiffness(){
        return this.k;
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
}
