package co.kr.citus;

import java.util.ArrayList;
import java.util.List;

public class DayCluster implements Cloneable {

    public List<ITourPlace> points;
    public ITourPlace centroid;
    public int day;

    //Creates a new DayCluster
    public DayCluster(int day) {
        this.day = day;
        this.points = new ArrayList<>();
        this.centroid = null;
    }

    public List<ITourPlace> getTourPlaces() {
        return points;
    }

    public void addTourPlace(ITourPlace place) {
        points.add(place);
    }

    public void setTourPlaces(List places) {
        this.points = places;
    }

    public ITourPlace getCentroid() {
        return centroid;
    }

    public void setCentroid(ITourPlace centroid) {
        this.centroid = centroid;
    }

    public int getDay() {
        return day;
    }

    public double getTotalCost() {
        if (points.size() == 0) {
            return (double)Integer.MAX_VALUE;
        }
        double costTotal = 0.0;
        for (ITourPlace place: points) {
            costTotal += place.costTo(centroid);
        }
        return costTotal;
    }

    public void clear() {
        points.clear();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder()
            .append("[Day: ")
                .append(day + 1)
                .append("]\n")
            .append("[Centroid: ")
                .append(centroid)
                .append("]\n")
            .append("[Places: \n");

        for (ITourPlace p : points) {
            builder.append(p.toString());
        }

        builder.append("\n]\n");
        return builder.toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        super.clone();
        DayCluster cloneCluster = new DayCluster(getDay());
        cloneCluster.centroid = centroid;
        for (ITourPlace place: points) {
            cloneCluster.points.add((ITourPlace)place.clone());
        }
        return cloneCluster;
    }

}