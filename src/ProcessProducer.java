import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class ProcessProducer {

    public static void main(String[] args) throws Exception {

        String[] command = getCommand().split(" ");
        String inputFilePath = command[2];
        String subProcessFilePath = command[3];

        int processCount = Integer.parseInt(command[5]);
        String subProcessFileParentDirectory = subProcessFilePath.substring(0, subProcessFilePath.lastIndexOf("\\"));
        String inputFileName = inputFilePath.substring(inputFilePath.lastIndexOf("\\") + 1);
        new File("\\" + subProcessFileParentDirectory + "\\output").mkdirs();
        String outputFilePath = subProcessFileParentDirectory + "\\output";

        deleteOutputDirectoryFiles(outputFilePath);

        Runtime.getRuntime().exec("javac " + subProcessFilePath).waitFor();

        ArrayList<Process> processArray = new ArrayList<>();
        for (int i = 0; i < processCount; i++)
            processArray.add(Runtime.getRuntime().exec("java -cp " + subProcessFileParentDirectory + " SubProcess "
                    + inputFileName + " " + i + " " + processCount + " " + outputFilePath));

        for (Process process : processArray)
            process.waitFor();

        File directory = new File(outputFilePath);
        File[] filesList = directory.listFiles();

        ArrayList<Long> finalCodesArrayList = new ArrayList<>();
        ArrayList<Long> durationsArrayList = new ArrayList<>();
        long processFinalCode = 0, processDuration = 0;

        if (filesList != null && filesList.length != 0) {
            for (File file : filesList) {

                Scanner scanner = new Scanner(file);

                while (scanner.hasNextLine()) {
                    processFinalCode = Long.parseLong(scanner.nextLine());
                    processDuration = Long.parseLong(scanner.nextLine());
                }

                finalCodesArrayList.add(processFinalCode);
                durationsArrayList.add(processDuration);

            }

            long finalCode = 0;
            for (Long code : finalCodesArrayList)
                finalCode ^= code;

            System.out.println("\n>>>>>>>> FinalCode: " + finalCode);
            System.out.println(">>>>>>>> Total Duration: " + Collections.max(durationsArrayList));
        } else
            System.out.println("there is not any file in the output directory to read.");
    }

    private static void deleteOutputDirectoryFiles(String outpuFilePath) {
        File directory = new File(outpuFilePath);
        File[] files = directory.listFiles();
        if (files != null)
            for (File f : files)
                f.delete();
    }

    private static String getCommand() {
        System.out.println(">>>>>>>> 1. Enter the command similar to the format below.");
        System.out.println(">>>>>>>> 2. inputFilePath: absolute path of input.txt file in your PC");
        System.out.println(">>>>>>>> 3. subProcessFilePath: absolute path of SubProcess.java file in your PC");
        System.out.println(">>>>>>>> 4. Note: When you run the program, it creates a directory named 'output' " +
                "beside the SubProcess.java and ProcessProducer.java files, so the files produced from these processes " +
                "will save in this directory.");
        System.out.println(">>>>>>>> 5. Note: The input.txt file must be in the same directory as other .java files.");
        System.out.println(">>>>>>>> 6. Command: compute -f inputFilePath subProcessFilePath -t processCount");
        System.out.println(">>>>>>>> 7. Example: compute -f D:\\project\\input.txt D:\\project\\SubProcess.java -t 10");
        System.out.print(">>>>>>>> ");

        Scanner scanner = new Scanner(System.in);

        String command;

        while (true) {
            command = scanner.nextLine();
            String[] commandInputArray = command.split(" ");

            if (commandInputArray.length != 6 || command.isEmpty()) {
                System.out.println(">>>>>>>> WRONG INPUT. TRY AGAIN.");
                System.out.print(">>>>>>>> ");
            } else
                break;
        }

        return command;
    }
}