package co.kr.citus;

public class Point implements ITourPlace {
    private double x = 0;
    private double y = 0;
    private int day = 0;

    public Point(double x, double y) {
        this.setX(x);
        this.setY(y);
    }

    private void setX(double x) {
        this.x = x;
    }

    private double getX()  {
        return this.x;
    }

    private void setY(double y) {
        this.y = y;
    }

    private double getY() {
        return this.y;
    }

    //Calculates the distance between two points.
    private double distance(ITourPlace centroid) {
        double xDistance = Math.abs(getLon() - centroid.getLon());
        double yDistance = Math.abs(getLat() - centroid.getLat());
        return Math.sqrt((xDistance * xDistance) + (yDistance * yDistance));
    }

    @Override
    public double getLat() {
        return getY();
    }

    @Override
    public void setLat(double lat) {
        setY(lat);
    }

    @Override
    public double getLon() {
        return getX();
    }

    @Override
    public void setLon(double lon) {
        setX(lon);
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public void setDay(int n) {
        this.day = n;
    }

    @Override
    public int getDay() {
        return this.day;
    }

    @Override
    public double costTo(ITourPlace place) {
        return distance(place);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ") ";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Point newPoint = new Point(getX(), getY());
        newPoint.setDay(getDay());
        return newPoint;
    }
}