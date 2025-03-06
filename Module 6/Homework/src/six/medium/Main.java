package Homework.src.six.medium;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {

  public static void main(String[] args) {
    String filePath = "/Users/aruzhanmadelkhanova/Desktop/idea project/productmastersjava/Module 6/Homework/src/six/medium/words.txt";

    Set<String> uniqueWords = new HashSet<>();
    Map<String, Integer> wordCountMap = new HashMap<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] words = line.split("\\W+");

        for (String word : words) {
          if (!word.isEmpty()) {
            word = word.toLowerCase();
            uniqueWords.add(word);

            wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
          }
        }
      }

      System.out.println("Уникальные слова и их частота:");
      for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
        System.out.println(entry.getKey() + ": " + entry.getValue());
      }
    } catch (IOException e) {
      System.err.println("Ошибка при чтении файла: " + e.getMessage());
    }
  }
}
