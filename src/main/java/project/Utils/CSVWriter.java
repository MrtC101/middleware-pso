package project.Utils;

import java.util.List;
import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter {
    public static void writeCSV(String filename, List<String[]> data) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (String[] row : data) {
                writer.append(String.join(",", row));
                writer.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}