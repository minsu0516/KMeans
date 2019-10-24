package co.kr.citus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DaySchedule {

    //Number of day clusters. This metric should be related to the number of points
    private int numDays;
    //Min and Max X and Y
    private double minLatitude = -90;
    private double maxLatitude = 90;
    private double minLongitude = -180;
    private double maxLongitude = 180;
    private int maxIteration;
    private int iteration;
    private double cohesion = Double.MAX_VALUE;

    private List<ITourPlace> points;
    private List<DayCluster> dayClusters;

    public DaySchedule(int numDays, int iteration, List<ITourPlace> places) throws CloneNotSupportedException {
        this.numDays = numDays;
        this.maxIteration = iteration;
        this.points = new ArrayList<>();
        for (ITourPlace place: places) {
            this.points.add((ITourPlace)place.clone());
        }
        this.dayClusters = new ArrayList<>();
    }

    public double getCohesion() {
        return cohesion;
    }

    public int getIteration() {
        return iteration;
    }

    public List<DayCluster> getDayClusters() {
        return dayClusters;
    }

    // Creates random point
    private static Point createRandomPoint(double minX, double maxX, double minY, double maxY) {
        Random rx = new Random();
        Random ry = new Random();
        double x = minX + (maxX - minX) * rx.nextDouble();
        double y = minY + (maxY - minY) * ry.nextDouble();
        return new Point(x, y);
    }

    private void init() {
        // find min/max of coordinate
        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        for (ITourPlace point: points) {
            if (point.getLon() < minX) {
                minX = point.getLon();
            } else if (point.getLon() > maxX) {
                maxX = point.getLon();
            }
            if (point.getLat() < minY) {
                minY = point.getLat();
            } else if (point.getLat() > maxY) {
                maxY = point.getLat();
            }
        }
        minLongitude = minX;
        maxLongitude = maxX;
        minLatitude = minY;
        maxLatitude = maxY;

        //Create days & set random centroids
        for (int i = 0; i < numDays; i++) {
            DayCluster dayCluster = new DayCluster(i);
            ITourPlace centroid = createRandomPoint(minLongitude, maxLongitude, minLatitude, maxLatitude);
            dayCluster.setCentroid(centroid);
            dayClusters.add(dayCluster);
        }
    }

    public DaySchedule printClusters() {
        for (int i = 0; i < numDays; i++) {
            DayCluster c = dayClusters.get(i);
            System.out.println(c.toString());
        }

        return this;
    }

    public DaySchedule build() {
        calc();
        double costTotal = 0.0;
        for (DayCluster cluster: dayClusters) {
            costTotal += cluster.getTotalCost();
        }
        cohesion = costTotal / points.size();
        return this;
    }

    //The process to calculate the K Means, with iterating method.
    private void calc() {
        boolean finish = false;
        iteration = 0;

        init();

        // Add in new data, one at a time, recalculating centroids with each new one.
        while (!finish) {
            iteration++;

            List<ITourPlace> lastCentroids = getCentroids();

            clearDayClusters();
            assignDayCluster();
            calculateCentroids();

            List<ITourPlace> currentCentroids = getCentroids();

            //Calculates total distance between new and old Centroids
            double distance = 0;
            for (int i = 0; i < lastCentroids.size(); i++) {
                distance += lastCentroids.get(i).costTo(currentCentroids.get(i));
            }

            if (distance == 0 || iteration >= maxIteration) {
                finish = true;
            }
        }
    }

    private void clearDayClusters() {
        for(DayCluster dayCluster : dayClusters) {
            dayCluster.clear();
        }
    }

    private List<ITourPlace> getCentroids() {
        List<ITourPlace> centroids = new ArrayList<>(numDays);
        for (DayCluster dayCluster : dayClusters) {
            ITourPlace aux = dayCluster.getCentroid();
            ITourPlace point = new Point(aux.getLon(), aux.getLat());
            centroids.add(point);
        }
        return centroids;
    }

    private void assignDayCluster() {
        double max = Double.MAX_VALUE;
        double min;
        int day = 0;
        double distance;

        for (ITourPlace point : points) {
            min = max;
            for (int i = 0; i < numDays; i++) {
                DayCluster c = dayClusters.get(i);
                distance = point.costTo(c.getCentroid());
                if(distance < min){
                    min = distance;
                    day = i;
                }
            }
            point.setDay(day);
            dayClusters.get(day).addTourPlace(point);
        }
    }

    private void calculateCentroids() {
        for (DayCluster dayCluster : dayClusters) {
            double sumX = 0;
            double sumY = 0;
            List<ITourPlace> list = dayCluster.getTourPlaces();
            int numPoints = list.size();

            for (ITourPlace point : list) {
                sumX += point.getLon();
                sumY += point.getLat();
            }

            if (numPoints > 0) {
                ITourPlace centroid = dayCluster.getCentroid();
                double newX = sumX / numPoints;
                double newY = sumY / numPoints;
                centroid.setLon(newX);
                centroid.setLat(newY);
            }
        }
    }
}