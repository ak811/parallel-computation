import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;

public class SubProcess {

    public static void main(String[] args) throws Exception {
        String fileName = args[0];
        int processIndex = Integer.parseInt(args[1]);
        int processesCount = Integer.parseInt(args[2]);
        String outputFilePath = args[3];

        int lineCount = countFileLines(fileName);

        ArrayList<Integer> processIndicesOfFile = new ArrayList<>();
        int tempProcessIndex = processIndex;

        do {
            processIndicesOfFile.add(tempProcessIndex);
            tempProcessIndex += processesCount;
        } while (tempProcessIndex < lineCount);

        Long[] numbers = new Long[lineCount];
        readFileAndBindArray(fileName, numbers);

        long startTime = System.currentTimeMillis();
        long processFinalCode = 0;

        for (int i = 0; i < processIndicesOfFile.size(); i++)
            processFinalCode ^= numbers[processIndicesOfFile.get(i)];

        long endTime = System.currentTimeMillis();

        // writing the final code of each process into its corresponding file
        writeResultToFile(processIndex, startTime, endTime, processFinalCode, outputFilePath);
    }

    public static void readFileAndBindArray(String fileName, Long[] array) throws Exception {
        Scanner scanner = new Scanner(new File(fileName));
        for (int i = 0; scanner.hasNextLine(); i++)
            array[i] = Long.valueOf(scanner.nextLine());
    }

    public static int countFileLines(String fileName) throws Exception {
        int linesCount = 0;
        Scanner scanner = new Scanner(new File(fileName));
        while (scanner.hasNextLine()) {
            scanner.nextLine();
            linesCount++;
        }
        return linesCount;
    }

    private static void writeResultToFile(int processIndex, long startTime, long endTime, long processFinalCode, String outputFilePath)
            throws FileNotFoundException {
        Formatter formatter = new Formatter(new File(outputFilePath, "p" + processIndex + ".txt"));
        formatter.format(processFinalCode + "\n" + (endTime - startTime));
        formatter.flush();
        formatter.close();
    }
}