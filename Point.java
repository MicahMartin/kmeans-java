package main.recommender;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Point {


    private double[] point_vector;
    private int cluster_number = 0;
    private int vector_size;

    public Point(double[] vector){
      this.point_vector = vector;
      this.vector_size = vector.length;
    }

    public void setComponentValue(int component_index, double x){
      //Might have to specify point_vector in the setDouble function. NOTE: Shit might break
        this.point_vector[component_index] = x;
    }

    public double getComponentValue(int component_index){
        return this.point_vector[component_index];
    }

    public void setCluster(int n){
        this.cluster_number = n;
    }

    public int getCluster(){
        return this.cluster_number;
    }

    public void setVector(double[] vector){
      this.point_vector = vector;

    }
    public double[] getVector(){
      return this.point_vector;
    }
    public int vectorSize(){
      return this.vector_size;
    }

    protected static double distance(Point a, Point b){
        double sumOfComponents = 0;
        for(int i = 0; i < a.vector_size; i++){
          sumOfComponents += Math.pow(( b.getComponentValue(i) - a.getComponentValue(i) ), 2);
        }
        return Math.sqrt(sumOfComponents);
    }

    // protected static Point createRandomPoint(int min, int max, int size){
    //     Random r = new Random();
    //     double[] vector = new double[size];
    //     for(int i = 0; i < size; i++){
    //       vector[i] = min + (max-min) * r.nextDouble();
    //     }
    //     return new Point(vector);
    // }
    //
    // protected static List createRandomPoints(int min, int max, int number, int size){
    //     List<Point> points = new ArrayList(number);
    //     for(int i = 0; i < number; i++) {
    //         Point pointToAdd = createRandomPoint(min,max,size);
    //         points.add(pointToAdd);
    //     }
    //     return points;
    // }

    public String toString() {
        StringBuilder vectorToString = new StringBuilder("#( ");
        for(int i=0; i < vector_size; i++){
          String appendMe = String.valueOf(this.point_vector[i]) + ", ";
          vectorToString.append(appendMe);
        }
        vectorToString.append(" )#");
        return vectorToString.toString();
    }
}
