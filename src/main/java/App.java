package main.java;

import main.java.model.Person;
import main.java.model.Product;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class App {

    public static void main(String[] args) {

        Person p1 = new Person(1, "Brenda", LocalDate.of(1991, 1, 21));
        Person p2 = new Person(2, "Mitto", LocalDate.of(1990, 2, 21));
        Person p3 = new Person(3, "Jaime", LocalDate.of(1980, 6, 23));
        Person p4 = new Person(4, "Carla", LocalDate.of(2019, 12, 15));
        Person p5 = new Person(5, "Lucas", LocalDate.of(2010, 11, 4));

        Product pr1 = new Product(1, "Ceviche", 15.0);
        Product pr2 = new Product(2, "Chilaquines", 25.50);
        Product pr3 = new Product(3, "Bandeja Paisa", 35.50);
        Product pr4 = new Product(4, "Ceviche", 15.0);

        List<Person> persons = Arrays.asList(p1, p2, p3, p4, p5);
        List<Product> products = Arrays.asList(pr1, pr2, pr3, pr4);

        //Lambda || referencia a método
        persons.forEach(System.out::println);
        System.out.println();
        products.forEach(System.out::println);

        //Filter (param:Predicate)
        List<Person> filteredList1 = persons.stream()
                .filter(p -> App.getAge(p.getBirthDate()) >= 18)
                .collect(Collectors.toList());
        System.out.println();
        App.printList(filteredList1);

        //Map (param: Function)
        Function<String, String> coderFunction = name -> "Coder " + name;
        List<String> filteredList2 = persons.stream()
                //.filter(p -> App.getAge(p.getBirthDate()) >= 18) //se puede aplicar un filtro [32,33,43]
                //.map(p -> App.getAge(p.getBirthDate()))//[32,33,43,3,13]
                //.map(p -> "Coder " + p.getName())
                .map(Person::getName)
                .map(coderFunction)
                .collect(Collectors.toList());
        System.out.println();
        App.printList(filteredList2);

        //Sorted (param:Comparator)
        Comparator<Person> byNameAsc = (Person o1, Person o2) -> o1.getName().compareTo(o2.getName());
        Comparator<Person> byNameDesc = (Person o1, Person o2) -> o2.getName().compareTo(o1.getName());
        Comparator<Person> byBirthDate = (Person o1, Person o2) -> o1.getBirthDate().compareTo(o2.getBirthDate());
        List<Person> filteredList3 = persons.stream()
                //.sorted(byNameAsc)
                //.sorted(byNameDesc)
                .sorted(byBirthDate)
                .collect(Collectors.toList());
        System.out.println();
        App.printList(filteredList3);

        //Match (param:Predicate)
        Predicate<Person> startsWithPredicate = p -> p.getName().startsWith("J");
        //anyMatch : No evalua todo el string, termina en la coincidencia
        boolean rpta1 = persons.stream()
                .anyMatch(startsWithPredicate);
        System.out.println();
        System.out.println(rpta1); //true

        //allMatch: Evalua todo el string bajo la condición
        boolean rpta2 = persons.stream()
                .allMatch(startsWithPredicate);
        System.out.println();
        System.out.println(rpta2); //false

        //noneMatch: Evalua todo el string bajo la condición
        boolean rpta3 = persons.stream()
                .noneMatch(startsWithPredicate);
        System.out.println();
        System.out.println(rpta3); //false

        //Limit || Skip
        int pageNumber = 0; // 0 1 2
        int pageSize = 2;
        List<Person> filteredlist4 = persons.stream()
                //.skip(2) Person{id=3, name='Jaime', birthDate=1980-06-23} Person{id=4, name='Carla',
                // birthDate=2019-12-15} Person{id=5, name='Lucas', birthDate=2010-11-04}
                //.limit(2) Person{id=1, name='Brenda', birthDate=1991-01-21} Person{id=2, name='Mitto',
                // birthDate=1990-02-21}
                .skip(pageNumber * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());

        System.out.println();
        App.printList(filteredlist4);

        //Collectors
        //GroupBy
        Map<Double, List<Product>> collect1 = products.stream()
                .filter(p -> p.getPrice() > 20)
                .collect(Collectors.groupingBy(Product::getPrice));
        System.out.println();
        System.out.println(collect1); //{35.5=[Product{id=3, name='Bandeja Paisa', price=35.5}], 25.5=[Product{id=2, name='Chilaquines', price=25.5}]}

        Map<String, List<Product>> collect2 = products.stream()
                .filter(p -> p.getPrice() > 20)
                .collect(Collectors.groupingBy(Product::getName));
        System.out.println();
        System.out.println(collect2); //{Chilaquines=[Product{id=2, name='Chilaquines', price=25.5}], Bandeja Paisa=[Product{id=3, name='Bandeja Paisa', price=35.5}]}

        //counting
        Map<String, Long> collect3 = products.stream()
                .collect(Collectors.groupingBy(Product::getName, Collectors.counting()));

        System.out.println();
        System.out.println(collect3); //{Ceviche=2, Chilaquines=1, Bandeja Paisa=1}

        //Agrupando por nombre producto y sumando
        Map<String, Double> collect4 = products.stream()
                .collect(Collectors.groupingBy(
                                Product::getName,
                                Collectors.summingDouble(Product::getPrice)
                        )
                );

        System.out.println();
        System.out.println(collect4); //{Ceviche=30.0, Chilaquines=25.5, Bandeja Paisa=35.5}

        //Obteniendo suma y resumen
        DoubleSummaryStatistics statistics = products.stream()
                .collect(Collectors.summarizingDouble(Product::getPrice));
        System.out.println();
        System.out.println(statistics); //DoubleSummaryStatistics{count=4, sum=91.000000, min=15.000000, average=22.750000, max=35.500000}

        //reduce
        Optional<Double> sum = products.stream()
                .map(Product::getPrice)
                .reduce(Double::sum);
        System.out.println();
        System.out.println(sum.get()); //91.0
    }


    public static int getAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public static void printList(List<?> list) {
        list.forEach(System.out::println);
    }
}
