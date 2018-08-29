package com.luoriusheng.study;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class Steam {

    List<Dish> menu = null;

    @Before
    public void before() {

        menu = Arrays.asList(new Dish("pork", false, 800, Dish.Type.MEAT),
                        new Dish("beef", false, 700, Dish.Type.MEAT),
                        new Dish("chicken", false, 400, Dish.Type.MEAT),
                        new Dish("french fries", true, 530, Dish.Type.OTHER),
                        new Dish("rice", true, 350, Dish.Type.OTHER),
                        new Dish("season fruit", true, 120, Dish.Type.OTHER),
                        new Dish("pizza", true, 550, Dish.Type.OTHER),
                        new Dish("prawns", false, 300, Dish.Type.FISH),
                        new Dish("salmon", false, 450, Dish.Type.FISH));
    }

    public void demo() {

        List<String> threeHighCaloricDishNames = menu.stream()
                        .filter(d -> d.getCalories() > 300)
                        .map(Dish::getName) // Function<T, R> T -> R
                        .limit(3)
                        .collect(toList());
    }

    @Test
    public void demo2(){
//        流让你从外部迭代转向内部迭代。

        List<String> names =
                        menu.stream()
                                        .filter(d -> {
                                            System.out.println("filtering" + d.getName());
                                            return d.getCalories() > 300;
                                        })
                                        .map(d -> {
                                            System.out.println("mapping" + d.getName());
                                            return d.getName();
                                        })// filte map 在一次遍历中， 循环合并
                                        .limit(3) // limit 短路操作
                                        .collect(toList());
                //filteringpork
                //mappingpork
                //filteringbeef
                //mappingbeef
                //filteringchicken
                //mappingchicken
                //[pork, beef, chicken]
        System.out.println(names);

        long count = menu.stream()
                        .filter(d -> d.getCalories() > 500) //Predicate<T>  T -> boolean
                        .distinct()
                        .sorted() //Comparator<T> (T, T) -> int
                        .limit(3) // transform
                        .skip(2)
                        .count(); // action
        System.out.println(count);

        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(3, 4);
        List<int[]> pairs = numbers1.stream().flatMap(i -> numbers2.stream()
                                                        .map(j -> new int[]{i, j})
                                        ).collect(toList());

    }

    public void demo3() {
        if(menu.stream().anyMatch(Dish::isVegetarian)){
            System.out.println("The menu is (somewhat) vegetarian friendly!!");
        }
        boolean isHealthy = menu.stream()
                        .allMatch(d -> d.getCalories() < 1000);
        boolean isHealthys = menu.stream()
                        .noneMatch(d -> d.getCalories() >= 1000);
        menu.stream().filter(Dish::isVegetarian)
                                        .findAny() // Optional
                                 .ifPresent(d -> System.out.println(d.getName()));

        List<Integer> someNumbers = Arrays.asList(1, 2, 3, 4, 5);
        Optional<Integer> firstSquareDivisibleByThree =
                        someNumbers.stream()
                                        .map(x -> x * x)
                                        .filter(x -> x % 3 == 0)
                                        .findFirst(); // 9
    }

    public void demo4(){
//        int sum = numbers.stream().reduce(0, Integer::sum);
//        Optional<Integer> sum = numbers.stream().reduce((a, b) -> (a + b));
//        Optional<Integer> max = numbers.stream().reduce(Integer::max);
        int calories = menu.stream()
                        .mapToInt(Dish::getCalories)  //IntStream
                        .sum();
        IntStream intStream = menu.stream().mapToInt(Dish::getCalories);
        Stream<Integer> stream = intStream.boxed();//转换回对象流

        OptionalInt maxCalories = menu.stream()
                        .mapToInt(Dish::getCalories)
                        .max();
        int max = maxCalories.orElse(1);
    }


    public void demo5(){
        Stream<String> stream = Stream.of("Java 8 ", "Lambdas ", "In ", "Action");
        stream.map(String::toUpperCase).forEach(System.out::println);

        int[] numbers = {2, 3, 5, 7, 11, 13};
        int sum = Arrays.stream(numbers).sum();

        long uniqueWords = 0;
        try(Stream<String> lines =
                        Files.lines(Paths.get("data.txt"), Charset.defaultCharset())){
            uniqueWords = lines.flatMap(line -> Arrays.stream(line.split(" ")))
                            .distinct()
                            .count();
        }
        catch(IOException e){
        }

        Stream.iterate(0, n -> n + 2)
                        .limit(10)
                        .forEach(System.out::println);

        Stream.generate(Math::random)
                        .limit(5)
                        .forEach(System.out::println);
    }

    public void demo6(){
        //-------------收集器
//        Map<Currency, List<Transaction>> transactionsByCurrencies =
//                        transactions.stream().collect(groupingBy(Transaction::getCurrency));
    }
}

class Dish {

    private final String name;
    private final boolean vegetarian;
    private final int calories;
    private final Type type;

    public Dish(String name, boolean vegetarian, int calories, Type type) {

        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }

    public String getName() {

        return name;
    }

    public boolean isVegetarian() {

        return vegetarian;
    }

    public int getCalories() {

        return calories;
    }

    public Type getType() {

        return type;
    }

    @Override
    public String toString() {

        return name;
    }

    public enum Type {MEAT, FISH, OTHER}
}
