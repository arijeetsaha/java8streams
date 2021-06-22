import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StreamFunctions {

    private List<Employee> employeeList = new ArrayList<>();

    public StreamFunctions() {
        Arrays.asList(1,2,3,4,5,6,7,8,9,10,10,20,22,23,24,25,23,21,20,5,4,3,2,1)
                .stream()
                .forEach(i -> {
                    Employee e = new Employee("name"+i, i+"", i);
                    employeeList.add(e);
                });
    }

    private void print(List<Employee> employeeList) {
        employeeList
                .stream()
                .filter(Objects::nonNull)
                .forEach(employee -> System.out.println(employee.getName()));
    }

    public static void main(String[] args) {
        StreamFunctions streamFunctions = new StreamFunctions();
        List<String> employeeList = streamFunctions.employeeNamesWithAgeGreater(20);

        employeeList.forEach(employee -> System.out.println(employee));

        System.out.println(streamFunctions.employeeCountWithAgeEquals(10));

        streamFunctions.findEmployeeWithName("name21");

        streamFunctions.findMaxAge();

        streamFunctions.sortEmployeesByAge();

        streamFunctions.groupByName();

        streamFunctions.sortEmployeesByAgeNullSafe();

        Map<Boolean, List<Employee>> map=streamFunctions.partitionEmployeesByAge();
        map.forEach((k,v) -> {
            System.out.println("Is age greater than 10: "+ k);
            System.out.println(v.stream().map(Employee::getName).collect(Collectors.toList()));
        });

        System.out.println(streamFunctions.sumOfLengthEmployeeNames());

        System.out.println(streamFunctions.totalNumberOfEmployees());

        streamFunctions.printAllNamesAsString();
    }


    private List<String> employeeNamesWithAgeGreater(int age) {

        Predicate<Employee> employeeAgeGreaterThanPredicate = employee -> employee.getAge() > age;

        return employeeList.stream()
                .filter(employeeAgeGreaterThanPredicate)
                .map(Employee::getName)
                .collect(Collectors.toList());
    }

    private long employeeCountWithAgeEquals(int age) {
        Predicate<Employee> employeeAgeGreaterThanPredicate = employee -> employee.getAge() == age;

        return employeeList.stream()
                .filter(employeeAgeGreaterThanPredicate)
                .count();
    }

    private void findMaxAge() {

        employeeList.stream()
                .mapToInt(Employee::getAge)
                .max().ifPresent(age -> {
                    System.out.println("Max age: "+age);
                });

    }

    private void findEmployeeWithName(String name) {
        Predicate<Employee> employeeWithName = employee -> name.equalsIgnoreCase(employee.getName());

        employeeList.stream()
                .filter(employeeWithName)
                .findAny().ifPresent(employee -> {
                    System.out.println(employee.getName());
                    System.out.println(employee.getId());
                });
    }

    private void sortEmployeesByAge() {

        //employeeList.stream().sorted((e1,e2) -> e1.getAge()- e2.getAge());

        List<Employee> sortedEmployees = employeeList.stream()
                .sorted(Comparator.comparing(Employee::getAge).reversed())
                .collect(Collectors.toList());
        print(sortedEmployees);
    }

    //Null safe sorting
    private void sortEmployeesByAgeNullSafe() {

        employeeList.add(null);
        List<Employee> sortedEmployees = employeeList.stream()
                .sorted(Comparator.nullsLast(Comparator.comparing(Employee::getAge)).reversed())
                .collect(Collectors.toList());
        print(sortedEmployees);
    }

    private void groupByName() {
        Map<String, List<Employee>> map = employeeList.stream()
                .collect(Collectors.groupingBy(employee -> employee.getName(), Collectors.toList()));
        map.forEach((e,k) -> {
            System.out.println("Employee Name: "+e);
            System.out.println("Employee ids "+ k.stream().map(em -> em.getId()).collect(Collectors.toList()));
        });

    }


    private void removeDuplicateEmployees() {
        employeeList.stream().collect(Collectors.toSet());
    }

    //Partition by Employee Age
    private Map<Boolean, List<Employee>> partitionEmployeesByAge() {
        Map<Boolean, List<Employee>> booleanListMap = employeeList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.partitioningBy(employee -> employee.getAge()>10));
        return booleanListMap;
    }

    // Sum of Employee Name lengths
    private Double sumOfLengthEmployeeNames() {
        employeeList.remove(employeeList.size()-1);
        return employeeList.stream()
                .map(Employee::getName)
                .collect(Collectors.summarizingDouble(String::length)).getSum();
    }

    // Sum of total number of employees
    private long totalNumberOfEmployees() {
        return employeeList.stream()
                .collect(Collectors.counting());
    }

    private void printAllNamesAsString() {
        System.out.println(employeeList.stream()
                .map(Employee::getName)
                .collect(Collectors.joining(",")));
    }

}
