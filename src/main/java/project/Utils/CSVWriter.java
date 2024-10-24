package project.Utils;

import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter {
    public static void writeCSV(String filename, List<String[]> data) {
        File file = new File(filename);
        File parentDir = file.getParentFile(); // Obtiene el directorio padre
            
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs(); // Crea el directorio, incluidos los padres faltantes
        }

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