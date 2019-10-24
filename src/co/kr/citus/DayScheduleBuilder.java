package co.kr.citus;

import java.util.ArrayList;
import java.util.List;

public class DayScheduleBuilder {
    private DaySchedule bestDaySchedule;
    private double bestCohesion;
    private int iteration = 10;
    private List<ITourPlace> places = new ArrayList<>();
    private int numDays = 2;

    public DayScheduleBuilder setMaxIteration(int iteration) {
        this.iteration = iteration;
        return this;
    }

    public DayScheduleBuilder setTourPlaces(List<ITourPlace> places) {
        this.places = places;
        return this;
    }

    public DayScheduleBuilder setDays(int days) {
        numDays = days;
        return this;
    }

    public DayScheduleBuilder build() {
        try {
            bestCohesion = Double.MAX_VALUE;
            DaySchedule daySchedule;
            for (int i = 0; i < iteration; i++) {
                daySchedule = new DaySchedule(numDays, iteration, places)
                    .build()
                    .printClusters();

                System.err.println(i+"================="+daySchedule.getCohesion());


                if (bestCohesion > daySchedule.getCohesion()) {
                    bestCohesion = daySchedule.getCohesion();
                    bestDaySchedule = daySchedule;
                }

            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public DaySchedule getDaySchedule() {
        return bestDaySchedule;
    }

    public double getBestCohesion() {
        return bestCohesion;
    }

    public static void test() {
        DayScheduleBuilder dayScheduleBuilder = new DayScheduleBuilder();

        long start = System.currentTimeMillis();

        dayScheduleBuilder
                .setMaxIteration(10)
                .setTourPlaces(createTestPoints())
                .setDays(2)
                .build();

        System.out.println("Finished");
        System.out.println("####################################");

        dayScheduleBuilder.getDaySchedule().printClusters();
        System.out.println("Best Cohesion: " + dayScheduleBuilder.getBestCohesion());

        System.out.println("Finished calc in " + (System.currentTimeMillis() - start) + "ms");
    }

    protected static List<ITourPlace> createTestPoints() {
        List<ITourPlace> points = new ArrayList<>();
        points.add(new Point(37.581151, 126.979038));
        points.add(new Point(37.580325,126.984649));
        points.add(new Point(37.582797,126.991206));
        points.add(new Point(37.574632,126.994105));
        points.add(new Point(37.548983, 127.081285));
        points.add(new Point(37.560622, 127.130304));
     //   points.add(new Point(12, 8.5));
//

        return points;
    }
}
