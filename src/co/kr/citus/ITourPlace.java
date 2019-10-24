package co.kr.citus;

public interface ITourPlace {
    // Gets place's x coordinate
    double getLon();

    void setLon(double lon);

    // Gets place's y coordinate
    double getLat();

    void setLat(double lat);

    // Gets place's average duration time in seconds
    int getDuration();

    void setDay(int c);

    int getDay();

    // Gets the distance to given place
    double costTo(ITourPlace place);

    String toString();

    Object clone() throws CloneNotSupportedException;

}
