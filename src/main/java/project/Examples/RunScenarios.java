package project.Examples;

import project.Experiment.ComparativeSimulation;
import project.Experiment.Configurations.DatacenterConfig;
import project.Utils.YamlReader;

public class RunScenarios {

    public static void main(String[] args){
        // Ruta del archivo de configuración YAML
        String confPath = "src/main/java/resources/configs"; 
        String resPath = "src/main/java/resources/results";

        runScencario(confPath.concat("/sequential.yaml"), resPath.concat("/sequential"));
        runScencario(confPath.concat("/scenario1.yaml"), resPath.concat("/scenario1"));
        runScencario(confPath.concat("/scenario2.yaml"), resPath.concat("/scenario2"));
        runScencario(confPath.concat("/scenario3.yaml"), resPath.concat("/scenario3"));
        runScencario(confPath.concat("/scenario4.yaml"), resPath.concat("/scenario4"));

    }

    private static void runScencario(String configFilePath, String resultPath) {
        DatacenterConfig datacenterConfig = YamlReader.readConfig(configFilePath);
        ComparativeSimulation sim = new ComparativeSimulation(datacenterConfig);
        sim.runSimulation(resultPath);
    }
}