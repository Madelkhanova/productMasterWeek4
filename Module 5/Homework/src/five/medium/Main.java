package five.medium;

import java.util.ArrayList;
import java.util.Collections;

public class Main {

    public static void main(String[] args) {
        ArrayList<DayOfWeek> daysOfWeek = new ArrayList<>();
        Collections.addAll(daysOfWeek, DayOfWeek.values());

        for (DayOfWeek day : daysOfWeek) {
            System.out.println(day);
        }

        DayOfWeek dayToCheck = DayOfWeek.SUNDAY;
        System.out.println(dayToCheck + " является выходным? " + isWeekend(dayToCheck));

    }

    public static boolean isWeekend(DayOfWeek day) {
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

}
