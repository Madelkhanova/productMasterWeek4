package Homework.src.six.easy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Numbers {
    private List<Integer> numbers;

    public Numbers(List<Integer> numbers) {
        this.numbers = numbers;
    }

    public int findMin() {
        return Collections.min(numbers);
    }

    public int findMax() {
        return Collections.max(numbers);
    }

    public List<Integer> sortAscending() {
        List<Integer> sortedList = new ArrayList<>(numbers);
        Collections.sort(sortedList);
        return sortedList;
    }

    // Сортировка по убыванию
    public List<Integer> sortDescending() {
        List<Integer> sortedList = new ArrayList<>(numbers);
        sortedList.sort(Collections.reverseOrder());
        return sortedList;
    }

    // Поиск элемента
    public boolean searchElement(int value) {
        return numbers.contains(value);
    }

    // Фильтрация элементов, больше заданного значения
    public List<Integer> filterElements(int threshold) {
        List<Integer> filteredList = new ArrayList<>();
        for (int num : numbers) {
            if (num > threshold) {
                filteredList.add(num);
            }
        }
        return filteredList;
    }

    // Сумма всех чисел
    public int sumAll() {
        int sum = 0;
        for (int num : numbers) {
            sum += num;
        }
        return sum;
    }
}
