package five.medium;

public class Main {

  public static void main(String[] args) {
    ArrayList<DayOfWeek> daysOfWeek = new ArrayList<>();
    for (DayOfWeek day : DayOfWeek.values()) {
      daysOfWeek.add(day);
    }

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
