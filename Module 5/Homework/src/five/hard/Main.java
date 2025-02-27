package five.hard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        ArrayList<Integer> numbers = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            numbers.add(scanner.nextInt());
        }

        ArrayList<Integer> uniqueNumbers = removeDuplicates(numbers);
        for (Integer number : uniqueNumbers) {
            System.out.print(number + " ");
        }
    }

    public static ArrayList<Integer> removeDuplicates(ArrayList<Integer> list) {

        ArrayList<Integer> result = new ArrayList<>();
        HashSet<Integer> seen = new HashSet<>();

        for (Integer number : list) {
            if (!seen.contains(number)) {
                result.add(number);
                seen.add(number);
            }
        }

        return result;

    }
}
