package project.Examples;

import project.Experiment.ComparativeSimulation;
import project.Experiment.Configurations.DatacenterConfig;
import project.Utils.YamlReader;

/**
 * Implementation of the main method for running the experiments for the task scheduling policies
 * comparison.
 */
public class OneConfigFileExample {
    public static void main(String[] args) {
        // Ruta del archivo de configuración YAML
        String configFilePath = "src/main/java/resources/configs/config.yaml";
        String resultFolderPath = "src/main/java/resources/results/example1";

        // Leer configuración desde archivo YAML usando YamlReader
        DatacenterConfig datacenterConfig = YamlReader.readConfig(configFilePath);

        // Ejecutar la simulación
        (new ComparativeSimulation(datacenterConfig)).runSimulation(resultFolderPath);
    }
}
