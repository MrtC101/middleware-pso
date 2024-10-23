package project.Examples;

import project.Experiment.Simulation;
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

        // Leer configuración desde archivo YAML usando YamlReader
        DatacenterConfig datacenterConfig = YamlReader.readConfig(configFilePath);

        // Ejecutar la simulación
        (new Simulation(datacenterConfig)).runSimulation();
    }
}
