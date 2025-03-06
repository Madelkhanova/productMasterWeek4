package Homework.src.six.easy;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Integer> nums = new ArrayList<>();
        nums.add(3);
        nums.add(5);
        nums.add(7);

        Numbers numberList = new Numbers(nums);

        System.out.println("Минимум: " + numberList.findMin());
        System.out.println("Максимум: " + numberList.findMax());
        System.out.println("Сортировка по возрастанию: " + numberList.sortAscending());
        System.out.println("Сортировка по убыванию: " + numberList.sortDescending());
        System.out.println("Поиск элемента (7): " + numberList.searchElement(7));
        System.out.println("Фильтрация элементов больше 5: " + numberList.filterElements(5));
        System.out.println("Сумма всех чисел: " + numberList.sumAll());
    }
}
