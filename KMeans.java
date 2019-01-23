package main.recommender;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.File;
import java.io.FileInputStream;
import org.apache.poi.xssf.usermodel.*;

public class KMeans {

    private List<Point> points;
    private List<Cluster> clusters;

    //number of clusters
    private int K;

    public KMeans(int K) {
        this.points = new ArrayList();
        this.clusters = new ArrayList();
        this.K = K;
    }

    public static void main(String[] args) {
      // args[0] = K or number of clusters
      // args[1] = xlsx file to run algorithm against. Each row becomes a Point
      // args[2] = sheet to grab from xlsx file. if no sheet name is specified, we look for data baby.

        KMeans kmeans = new KMeans(Integer.parseInt(args[0]));
        kmeans.seed();
        kmeans.run();
    }

    public void seed() {
        //load data file and grab the sheet we need
        File file = new File("/Users/martin/Work/globe/trunk/eomdb/SysConfig/WebPortal/BostonGlobe/Framework/sandbox/mmartin/com/data/data.xlsx");
        FileInputStream inputStream = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheet("data");

        int observations = sheet.getLastRowNum() + 1;
        int vectorSize = sheet.getRow(0).getLastCellNum();
        //Loop through data file, convert each observation to a point
        for(int i = 0; i < observations; i++){
          XSSFRow row = sheet.getRow(i);
          double[] vector = new double[vectorSize];
          for(int x = 0; x < vectorSize; x++){
            XSSFCell cell = row.getCell(x);
            double val = cell.toDouble();
            vector[x] = val;
          }
          Point dataPoint = new Point(vector);
          points.add(dataPoint);
        }

        for (int i = 0; i < K; i++) {
            Cluster cluster = new Cluster(i);

            //TODO: implement kmeans++
            // Point centroid = Point.createRandomPoint(MIN_COORDINATE,MAX_COORDINATE,VECTOR_SIZE);

            cluster.setCentroid(centroid);
            clusters.add(cluster);
        }

        plotClusters();
    }

    //kmeans plus plus implementation
    private List<Point> kmpp(List<Point> dp, int k, Random r){

        List<Point> pointSet = new ArrayList(dp);
        List<Point> resultSet = new ArrayList();

        // Choose one center uniformly at random from among the data points.
        Point firstPoint = pointSet.remove(random.nextInt(pointSet.size()));
        resultSet.add(firstPoint);

        double[] dx2 = new double[pointSet.size()];
        while (resultSet.size() < k) {
            // For each data point x, compute D(x), the distance between x and
            // the nearest center that has already been chosen.
            double sum = 0;
            for (int i = 0; i < pointSet.size(); i++) {
                Point p = pointSet.get(i);
                Point nearestCentroid = getNearestCenter(resultSet, p);
                double d = Point.distance(nearestCentroid,p);
                sum += distance;
                dx2[i] = sum;
            }

            // Add one new data point as a center. Each point x is chosen with
            // probability proportional to D(x)2
            final double r = random.nextDouble() * sum;
            for (int i = 0 ; i < dx2.length; i++) {
                if (dx2[i] >= r) {
                    Point p = pointSet.remove(i);
                    resultSet.add(p);
                    break;
                }
            }
        }

        return resultSet;

    }

    private Point getNearestCenter(List<Point> centerList, Point p){
      int indexToReturn = 0;
      double[] selectFromMe = new double[centerlist.size()];
      double min = selectFromMe[indexToReturn];
      for(int i = 0; i < centerList.size(); i++){
        selectFromMe[i] = Point.distance(centerList[i],p);
        if(selectFromMe[i] < min){
          min = selectFromMe[i];
          indexToReturn = i;
        }
      }
      return centerList.get(indexToReturn);
    }

    private void plotClusters() {
        for (int i = 0; i < K; i++) {
            Cluster c = (Cluster) clusters.get(i);
            c.plotCluster();
        }
    }

    //The process to calculate the K Means, with iterating method.
    public void run() {
        boolean finish = false;
        int iteration = 0;

        // Add in new data, one at a time, recalculating centroids with each new one.
        while(!finish) {
            //Clear cluster state
            clearClusters();

            List lastCentroids = getCentroids();

            //Assign points to the closer cluster
            assignCluster();

            //Calculate new centroids.
            calculateCentroids();

            iteration++;

            List currentCentroids = getCentroids();

            //Calculates total distance between new and old Centroids
            double distance = 0;
            for(int i = 0; i < lastCentroids.size(); i++) {
                distance += Point.distance((Point) lastCentroids.get(i), (Point) currentCentroids.get(i));
            }
            System.out.println("#################");
            System.out.println("Iteration: " + iteration);
            System.out.println("Centroid distances: " + distance);
            plotClusters();

            if(distance == 0) {
                finish = true;
            }
        }
    }

    private void clearClusters() {
        for(Cluster cluster : clusters) {
            cluster.clear();
        }
    }

    private List getCentroids() {
        List centroids = new ArrayList(K);
        for(Cluster cluster : clusters) {
            Point aux = cluster.getCentroid();
            Point point = new Point(aux.getVector());
            centroids.add(point);
        }
        return centroids;
    }

    private void assignCluster() {
        double max = Double.MAX_VALUE;
        double min = max;
        int cluster = 0;
        double distance = 0.0;

        for(Point point : points) {
            min = max;
            for(int i = 0; i < K; i++) {
                Cluster c = clusters.get(i);
                distance = point.distance(point, c.getCentroid());
                if(distance < min){
                    min = distance;
                    cluster = i;
                }
            }
            point.setCluster(cluster);
            clusters.get(cluster).addPoint(point);
        }
    }

    private void calculateCentroids() {
        for(Cluster cluster : clusters) {
            double[] sumVector = new double[VECTOR_SIZE];
            List<Point> list = cluster.getPoints();
            int n_points = list.size();
            for(Point point : list) {
              for(int i = 0; i < point.vectorSize(); i++){
                sumVector[i] += point.getComponentValue(i);
              }
            }
            Point centroid = cluster.getCentroid();
            if(n_points > 0) {
                for(int i = 0; i < sumVector.length; i++){
                  sumVector[i] = sumVector[i] / n_points;
                }
                centroid.setVector(sumVector);
            }
        }
    }
}
