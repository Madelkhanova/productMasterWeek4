package easy;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        StringPrinter stringPrinter = new StringPrinter();
        Scanner scanner = new Scanner(System.in);
        String word = scanner.nextLine();
        stringPrinter.print(word);
    }
}
