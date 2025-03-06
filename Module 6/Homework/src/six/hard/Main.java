package Homework.src.six.hard;

import java.util.ArrayList;
import java.util.HashSet;

public class Main {

    public static void main(String[] args) {
        SafeList<String> list = new SafeList<>();
        list.add("apple");
        list.add("banana");
        list.add(null);
        list.add("apple");

        System.out.println(list.get(0));  // apple
        System.out.println(list.get(5));  // null (не ошибка!)
        System.out.println("Размер списка: " + list.size());  // Размер списка: 2
    }

    public static ArrayList<Integer> removeDuplicates(ArrayList<Integer> list) {
        HashSet<Integer> uniqueSet = new HashSet<>(list);
        return new ArrayList<>(uniqueSet);
    }
}
