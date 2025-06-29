package homework_2;

import homework_2.entity.Employee;
import homework_2.exceptions.EmptyArrayException;
import homework_2.exceptions.EmptyTextException;
import homework_2.exceptions.NotEnoughNumbersException;
import homework_2.exceptions.NotEnoughUniqueNumbersException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HomeWork2App {

    private static final String JOB_POSITION = "Инженер";

    public static void main(String[] args) {
        List<Integer> integers = List.of(3, 6, 4, 8, 9, 8);
        Optional<Integer> thirdMaxNumber = getThirdMaxNumber(integers);
        System.out.println(thirdMaxNumber.orElseThrow(NoSuchElementException::new));

        Optional<Integer> thirdUniqueMaxNumber = getThirdUniqueMaxNumber(integers);
        System.out.println(thirdUniqueMaxNumber.orElseThrow(NoSuchElementException::new));

        List<Employee> employees = createListOfEmployees();
        List<String> namesOfEngineers = sortedEngineersNamesByAge(employees);
        System.out.println(namesOfEngineers);

        double engineersAverageAge = getEngineersAverageAge(employees);
        System.out.println(engineersAverageAge);

        String inputText = "java python java kotlin python java";
        String longestWord = findLongestWordInText(inputText);
        System.out.println(longestWord);

        Map<String, Integer> wordCounts = countWords(inputText);
        wordCounts.forEach((word, count) ->
                System.out.printf("%-7s: %d%n", word, count));

        printSortedWords(List.of("java", "python", "kotlin", "c++", "c#"));

        String[] lines = {
                "яблоко груша апельсин банан киви",
                "помидор огурец перец морковь капуста"
        };
        String longestWord2 = findLongestWordInArray(lines);
        System.out.println(longestWord2);

    }

    /**
     * Находит самое длинное слово в массиве строк, где каждая строка содержит 5 слов, разделенных пробелами.
     * Если найдено несколько слов с максимальной длиной, возвращает любое из них.
     *
     * @param lines массив строк, где каждая строка содержит ровно 5 слов, разделенных пробелами
     * @return самое длинное слово
     * @throws EmptyArrayException если входной массив null, пуст или содержит некорректные строки
     * @throws EmptyTextException если входная строка null или пустая
     */
    public static String findLongestWordInArray(String[] lines) {
        return Optional.ofNullable(lines)
                .map(arr -> Arrays.stream(arr)
                        .flatMap(line -> Arrays.stream(line.split("\\s+")))
                        .filter(word -> !word.isEmpty())
                        .max(Comparator.comparingInt(String::length))
                        .orElseThrow(() -> new EmptyTextException("Не найдено ни одного слова")))
                .orElseThrow(() -> new EmptyArrayException("Входной массив не может быть null"));
    }

    /**
     * Сортирует и печатает слова в порядке возрастания длины,
     * при равной длине - в алфавитном порядке
     *
     * @param words список слов для сортировки
     * @throws EmptyArrayException если список слов null или пуст
     */
    public static void printSortedWords(List<String> words) {
        Optional.ofNullable(words)
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new EmptyArrayException(
                        "Список слов не может быть null или пустым"))
                .stream()
                .filter(word -> !word.isBlank())
                .sorted(Comparator.comparingInt(String::length)
                        .thenComparing(Comparator.naturalOrder()))
                .forEach(System.out::println);
    }

    /**
     * Подсчитывает количество вхождений каждого слова в строке.
     *
     * @param input строка со словами в нижнем регистре, разделенными пробелами
     * @return Map, где ключ - слово, значение - количество его вхождений
     * @throws EmptyTextException если входная строка null или пустая
     */
    public static Map<String, Integer> countWords(String input) {
        return Optional.ofNullable(input)
                .filter(s -> !s.isBlank())
                .map(s -> Arrays.stream(s.split("\\s+")))
                .orElseThrow(() -> new EmptyTextException("Входная строка не может быть null или пустой"))
                .filter(word -> !word.isEmpty())
                .collect(Collectors.toMap(
                        word -> word,
                        word -> 1,
                        Integer::sum,
                        LinkedHashMap::new
                ));
    }

    /**
     * Находит самое длинное слово в тексте. Если текст пуст или в тексте только символы -
     * будет выброшено исключение.
     *
     * @param text текст
     * @return самое длинное слово
     * @throws EmptyTextException если входная строка null или пустая
     */
    public static String findLongestWordInText(String text) {
        return Stream.ofNullable(text)
                .filter(s -> !s.isEmpty())
                .flatMap(s -> Arrays.stream(s.split("[^a-zA-Zа-яА-Я]+")))
                .filter(word -> !word.isEmpty())
                .max(Comparator.comparingInt(String::length))
                .orElseThrow(() -> new EmptyTextException("Нет слов для обработки"));
    }

    /**
     * Вычисляет средний возраст сотрудников с должностью "Инженер".
     *
     * @param employees список всех сотрудников
     * @return значение среднего возраста
     * @throws EmptyArrayException если массив null или пустой
     */
    public static double getEngineersAverageAge(List<Employee> employees) {
        return employees.stream()
                .filter(employee -> JOB_POSITION.equals(employee.getPosition()))
                .mapToInt(Employee::getAge)
                .average()
                .orElseThrow(() -> new EmptyArrayException("Массив не должен быть пустым"));
    }

    /**
     * Формирует список имен инженеров из общего списка сотрудников.
     *
     * @param employees список всех сотрудников.
     * @return список имен инженеров.
     * @throws EmptyArrayException если массив null или пустой
     */
    public static List<String> sortedEngineersNamesByAge(List<Employee> employees) {
        if (employees.isEmpty()) {
            throw new EmptyArrayException("Массив не может быть пустым");
        }

        return employees
                .stream()
                .filter(employee -> employee.getPosition().equals(JOB_POSITION))
                .sorted(Comparator.comparingInt(Employee::getAge).reversed())
                .map(Employee::getName)
                .toList();
    }

    /**
     * Создает общий список сотрудников.
     *
     * @return список сотрудников.
     */
    public static List<Employee> createListOfEmployees() {
        Employee employee1 = new Employee("Иван", 30, "Инженер");
        Employee employee2 = new Employee("Сергей", 25, "Инженер");
        Employee employee3 = new Employee("Александр", 35, "Инженер");
        Employee employee4 = new Employee("Глеб", 27, "Архитектор");
        Employee employee5 = new Employee("Ольга", 32, "Архитектор");
        Employee employee6 = new Employee("Анна", 37, "Архитектор");
        return Arrays.asList(employee1, employee2, employee3, employee4, employee5, employee6);

    }

    /**
     * Вычисляет третье наибольшее уникальное число в списке целых чисел.
     *
     * @param integers список целых чисел
     * @return третье наибольшее уникальное число из списка
     * @throws EmptyArrayException             если массив null или пустой
     * @throws NotEnoughUniqueNumbersException если в массиве меньше трех элементов
     */
    public static Optional<Integer> getThirdUniqueMaxNumber(List<Integer> integers) {
        if (integers == null || integers.isEmpty()) {
            throw new EmptyArrayException("Пустой массив элементов");
        }

        long uniqueCount = integers.stream().distinct().count();
        if (uniqueCount < 3) {
            throw new NotEnoughUniqueNumbersException("В массиве меньше трех уникальных элементов");
        }
        return integers.stream()
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(2)
                .findFirst();
    }

    /**
     * Вычисляет третье наибольшее число в списке целых чисел.
     *
     * @param integers список целых чисел
     * @return третье наибольшее число из списка
     * @throws EmptyArrayException             если массив null или пустой
     * @throws NotEnoughUniqueNumbersException если в массиве меньше трех элементов
     */
    public static Optional<Integer> getThirdMaxNumber(List<Integer> integers) {
        if (integers == null || integers.isEmpty()) {
            throw new EmptyArrayException("Массив не должен быть пустым");
        }
        if (integers.size() < 3) {
            throw new NotEnoughNumbersException("В массиве меньше трех элементов");
        }

        return integers.stream()
                .sorted(Comparator.reverseOrder())
                .skip(2)
                .findFirst();
    }
}