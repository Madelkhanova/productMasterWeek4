package src.main.java.org.example;

import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        List<Person> people = List.of(
                Person.builder().name("Aru").age(25).city("ALA").build(),
                Person.builder().name("Diana").age(30).city("NQZ").build(),
                Person.builder().name("Tamilla").age(35).city("TLD").build()
        );

        people.forEach(System.out::println);

        //оставить только людей старше 18 лет
        List<Person> adults = people.stream()
                .filter(person -> person.getAge() > 18)
                .collect(Collectors.toList());

        System.out.println("Взрослые:");
        adults.forEach(System.out::println);

        //Вычисление среднего возраста
        double averageAge = people.stream()
                .mapToInt(Person::getAge)
                .average()
                .orElse(0); // если список пуст, вернуть 0

        System.out.println("\nСредний возраст: " + averageAge);

        //Список людей из Алматы (вывести name)
        List<String> almatyPeople = people.stream()
                .filter(person -> "Алматы".equals(person.getCity()))
                .map(Person::getName)
                .collect(Collectors.toList());

        System.out.println("\nЛюди из Алматы: " + almatyPeople);

        //Преобразование списка в Map<String, Integer> (имя → возраст)
        Map<String, Integer> nameToAgeMap = people.stream()
                .collect(Collectors.toMap(Person::getName, Person::getAge));

        System.out.println("\nКарта (имя → возраст): " + nameToAgeMap);

        //Сортировка по возрасту (убыванию) и вывод ТОП-3 самых старших
        List<Person> top3Oldest = people.stream()
                .sorted(Comparator.comparingInt(Person::getAge).reversed())
                .limit(3)
                .collect(Collectors.toList());

        System.out.println("ТОП-3 самых старших:");
        top3Oldest.forEach(System.out::println);

        //Создание компаний
        Company company1 = Company.builder()
                .name("AruzhanComp")
                .employees(List.of(people.get(0), people.get(2), people.get(4), people.get(6)))
                .build();

        Company company2 = Company.builder()
                .name("DianaComp")
                .employees(List.of(people.get(1), people.get(3), people.get(5)))
                .build();

        List<Company> companies = List.of(company1, company2);

//Фильтрация сотрудников в каждой компании (оставляем только тех, кто старше 25)
        Map<String, List<Person>> filteredEmployeesByCompany = companies.stream()
                .collect(Collectors.toMap(
                        Company::getName,
                        company -> company.getEmployees().stream()
                                .filter(employee -> employee.getAge() > 25)
                                .collect(Collectors.toList())
                ));

        System.out.println("\nСотрудники старше 25 лет в компаниях:");
        filteredEmployeesByCompany.forEach((company, employees) ->
                System.out.println(company + ": " + employees));

        //Средний возраст сотрудников в каждой компании
        Map<String, Double> avgAgeByCompany = companies.stream()
                .collect(Collectors.toMap(
                        Company::getName,
                        company -> company.getEmployees().stream()
                                .mapToInt(Person::getAge)
                                .average()
                                .orElse(0)
                ));

        System.out.println("\nСредний возраст сотрудников в компаниях:");
        avgAgeByCompany.forEach((company, avgAge) ->
                System.out.println(company + ": " + avgAge));

    }
}