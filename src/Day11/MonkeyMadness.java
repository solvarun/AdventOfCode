package Day11;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

class Monkey{
    List<Long> items = new ArrayList<>(); // items Monkey is holding
    char operator = '*'; // operation to be done on worry level. Defaulting operation to add.
    int operatingNumber = -1; // number used for operation
    int divisibilityNumber = -1; // divisibility test
    int divisibleMonkey = -1; // if divisibility test is positive, throwing item to this Monkey
    int notDivisibleMonkey = -1; // if divisibility test is negative, throwing item to this Monkey

    /*Getters and setters.*/
    public List<Long> getItems() {
        return items;
    }

    public void setItems(List<Long> items) {
        this.items = items;
    }

    public char getOperator() {
        return operator;
    }

    public void setOperator(char operator) {
        this.operator = operator;
    }

    public int getOperatingNumber() {
        return operatingNumber;
    }

    public void setOperatingNumber(int operatingNumber) {
        this.operatingNumber = operatingNumber;
    }

    public int getDivisibilityNumber() {
        return divisibilityNumber;
    }

    public void setDivisibilityNumber(int divisibilityNumber) {
        this.divisibilityNumber = divisibilityNumber;
    }

    public int getDivisibleMonkey() {
        return divisibleMonkey;
    }

    public void setDivisibleMonkey(int divisibleMonkey) {
        this.divisibleMonkey = divisibleMonkey;
    }

    public int getNotDivisibleMonkey() {
        return notDivisibleMonkey;
    }

    public void setNotDivisibleMonkey(int notDivisibleMonkey) {
        this.notDivisibleMonkey = notDivisibleMonkey;
    }
}

public class MonkeyMadness {
    static final String user_dir = System.getProperty("user.dir");

    /*This list is used to keep a record of the Monkeys with the index in list being the number of that specific monkey,*/
    static ArrayList<Monkey> monkeyList = new ArrayList<>();
    static long MODULO = 1;

    public static void main(String[] args) {
        String PATH = user_dir + "\\src\\Day11\\challenge11.txt";
        try {
            List<String> inputs = Files.readAllLines(Path.of(PATH));
            parseInput(inputs);

            /*Comment and uncomment one to get the answer for that specific part of problem.*/
            long solutionPart1 = solvePart1();
//            long solutionPart2 = solvePart2();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**Part 1 - Requirement is to find the product of investigations done by two monkeys having highest and second-highest investigations after 20 rounds.
     *
     * @return product of two highest number of investigations done by monkeys.
     */
    private static long solvePart1() {
        int rounds = 20; // number of rounds for which investigations are done.
        long[] inspections = new long[8]; // array to keep tack of investigations done by each monkey.

        while (rounds > 0) {
            /*For each monkey*/
            for (int i = 0; i < monkeyList.size(); i++) {
                Monkey monkey = monkeyList.get(i); // current monkey
                inspections[i] += monkey.getItems().size(); // adding size of items list to investigations done

                /*Investigating each item individually. Worry defines the worry level when monkey is investigating the item.*/
                for (long worry : monkey.getItems()) {
                    /*Performing operation on worry either(+ or *) with the operating number.*/
                    worry = ((monkey.getOperator() == '+') ? // after each investigation the worry level of that item is reduced by three times.
                            worry + monkey.getOperatingNumber() : (monkey.getOperatingNumber() != -1) ? worry * monkey.getOperatingNumber() : worry * worry) / 3;
                    /*Performing divisibility test to select a monkey to which the item will be thrown.*/
                    monkeyList.get(worry % monkey.getDivisibilityNumber() == 0 ?
                            monkey.getDivisibleMonkey() : monkey.getNotDivisibleMonkey()).getItems().add(worry);
                }
                monkey.setItems(new ArrayList<>()); // after all items are checked, current monkey won't have any items left in his itemsList.
            }
            rounds -= 1; // 1 round completed
        }

        Arrays.sort(inspections); // sorting the array
        return inspections[6] * inspections[7]; // returning result.

        /*Monkey Business after 20 rounds with reduction in worry levels yields 110264.*/
    }

    /**Part 2 - Requirement is to find the product of investigations done by two monkeys having highest and second-highest investigations after 10000 rounds.
     *
     * @return product of two highest number of investigations done by monkeys.
     */
    private static long solvePart2() {
        int rounds = 10000; // number of rounds for which investigations are done.
        long[] inspections = new long[8]; // array to keep tack of investigations done by each monkey.

        while (rounds > 0) {
            /*For each monkey*/
            for (int i = 0; i < monkeyList.size(); i++) {
                Monkey monkey = monkeyList.get(i); // current monkey
                inspections[i] += monkey.getItems().size(); // adding size of items list to investigations done

                /*Investigating each item individually. Worry defines the worry level when monkey is investigating the item.*/
                for (long worry : monkey.getItems()) {
                    /*Performing operation on worry either(+ or *) with the operating number.*/
                    worry = ((monkey.getOperator() == '+') ?
                            /*Worry level of item is not reduced during this part, so we need a MODULO to keep the item worry level from overflowing the long range.*/
                            worry + monkey.getOperatingNumber() : (monkey.getOperatingNumber() != -1) ? worry * monkey.getOperatingNumber() : worry * worry) % MODULO;
                    /*Performing divisibility test to select a monkey to which the item will be thrown.*/
                    monkeyList.get(worry % monkey.getDivisibilityNumber() == 0 ?
                            monkey.getDivisibleMonkey() : monkey.getNotDivisibleMonkey()).getItems().add(worry);
                }
                monkey.setItems(new ArrayList<>()); // after all items are checked, current monkey won't have any items left in his itemsList.
            }
            rounds -= 1; // 1 round completed
        }
        Arrays.sort(inspections); // sorting the array
        return inspections[6] * inspections[7]; // returning result.

        /*Monkey Business after 10,000 rounds without reduction in worry levels yields 23680498584.*/
    }


    /**This method is used to parse the given input.
     *
     * @param inputs - input represented by list of Strings
     */
        private static void parseInput (List < String > inputs) {
            int idx = 0; // keeping track of input index
            while (idx < inputs.size()) {
                String input = inputs.get(idx).trim();

                /*Creating a Monkey object when input string starts with Monkey.*/
                if (input.startsWith("Monkey"))
                    idx = createMonkey(inputs, idx);
                idx += 1;
            }
        }

    /**This method is used to create Monkey object with all the details for that specific Monkey.
     *
     * @param inputs - input represented by list of Strings
     * @param idx - current index of input string in list.
     * @return index after Monkey object is created successfully.
     */
        private static int createMonkey (List < String > inputs,int idx){
            Monkey monkey = new Monkey(); // creating object
            while (idx < inputs.size()) {
                String current = inputs.get(idx).trim(); // current input string
                if (current.isBlank()) break; // if there is a blank line, it means current monkey characteristics are done.

                /*Parsing the items held by monkey and adding them to Arraylist object named Items for current Monkey.*/
                if (current.contains("items"))
                    /*Splitting current string at ":" and taking second half, and again splitting at "," to get all the items with numeric values.*/
                    monkey.setItems(Arrays.stream(current.split(":")[1].split(","))
                        .map(item -> Long.parseLong(item.trim())).collect(Collectors.toList())); // Using streams to map each trimmed digit string to list and adding it to items.

                /*Parsing the operation that needs to be done on each item during check done by this monkey.*/
                if (current.contains("Operation")) {
                    /*Splitting the string at "=" to get the number with which operation will be performed*/
                    String num = current.split("=")[1].trim().split(" ")[2];
                    /*If the string is "old", it means the operation is square of current number. Otherwise, we are storing the operating number.*/
                    if (!num.equalsIgnoreCase("old"))
                        monkey.setOperatingNumber(Integer.parseInt(num));
                    /*If the operation is addition, updating the operation.*/
                    if (current.contains("+"))
                        monkey.setOperator('+');
                }

                /*Parsing the divisibility test for current monkey.*/
                if (current.contains("divisible")) {
                    /*Getting the divisor by splitting the string at "by".*/
                    int divisor = Integer.parseInt(current.split("by")[1].trim());
                    monkey.setDivisibilityNumber(divisor);
                    MODULO *= divisor; // updating modulo for second part
                }

                /*Parsing the Conditional monkey numbers based on divisibility test.*/
                if (current.contains("true")) // if current item is divisible, throw to this monkey.
                    monkey.setDivisibleMonkey(Integer.parseInt(current.split("monkey")[1].trim()));
                if (current.contains("false")) // if current item is divisible, throw to this monkey.
                    monkey.setNotDivisibleMonkey(Integer.parseInt(current.split("monkey")[1].trim()));
                idx += 1; // incrementing index
            }
            monkeyList.add(monkey); // adding monkey to MonkeyList.
            return idx; // returning index
        }
    }
