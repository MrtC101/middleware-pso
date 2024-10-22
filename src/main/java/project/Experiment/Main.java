package project.Experiment;

import project.Utils.YamlReader;

public class Main {
    public static void main(String[] args) {
        // Ruta del archivo de configuración YAML
        String configFilePath = "src/main/java/resources/config.yaml";

        // Leer configuración desde archivo YAML usando YamlReader
        DatacenterConfig datacenterConfig = YamlReader.readConfig(configFilePath);

        // Instanciar la simulación con la configuración del datacenter
        Simulation simulation = new Simulation(datacenterConfig);

        // Ejecutar la simulación
        simulation.runSimulation();
    }
}
