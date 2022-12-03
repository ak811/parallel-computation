import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class SynchronizeThreadProducer {

    static int threadsCount;
    static ArrayList<Long> numbers;
    static long finalCode;

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        String[] command = getCommand().split(" ");
        String inputFilePath = command[2];
        threadsCount = Integer.parseInt(command[4]);
        Scanner scanner = new Scanner(new File(inputFilePath));

        numbers = bindArrayFromFile(scanner);

        long startTime = System.currentTimeMillis();

        createThreads();

        System.out.println("\n>>>>>>>> Final code: " + finalCode);
        System.out.println(">>>>>>>> Total duration without sleeps: " + (System.currentTimeMillis() - startTime
                - MySynchronizeThread.counter * 1000L));
    }

    private static ArrayList<Long> bindArrayFromFile(Scanner scanner) {
        ArrayList<Long> numbers = new ArrayList<>();
        while (scanner.hasNext()) {
            String numberStr = scanner.nextLine();
            StringBuilder strWithoutSpecificCharacters = new StringBuilder();
            for (int i = 0; i < numberStr.length(); i++) {
                char currentChar = numberStr.charAt(i);

                if (currentChar >= '0' && currentChar <= '9')
                    strWithoutSpecificCharacters.append(currentChar);
            }

            numbers.add(Long.valueOf(strWithoutSpecificCharacters.toString()));
        }
        return numbers;
    }

    private static void createThreads() throws InterruptedException {
        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < threadsCount; i++) {
            Thread thread = new Thread(new MySynchronizeThread(i));
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads)
            thread.join();
    }

    public static String getCommand() {
        System.out.println(">>>>>>>> 1. Enter the command similar to the format below.");
        System.out.println(">>>>>>>> 2. inputFilePath: absolute path of input.txt file in your pc.");
        System.out.println(">>>>>>>> 3. command: compute -f inputFilePath -t threadCount");
        System.out.println(">>>>>>>> 4. example: compute -f D:\\project\\input.txt -t 10");
        System.out.print(">>>>>>>> ");

        Scanner scanner = new Scanner(System.in);

        String command;

        while (true) {
            command = scanner.nextLine();
            String[] commandInputArray = command.split(" ");
            if (commandInputArray.length != 5 || command.isEmpty())
                System.out.println("WRONG INPUT. TRY AGAIN.");
            else
                break;
        }

        return command;
    }
}












