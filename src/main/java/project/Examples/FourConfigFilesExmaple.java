package project.Examples;

import project.Experiment.Simulation;
import project.Experiment.Configurations.DatacenterConfig;
import project.Experiment.Generators.SimGenerator;
import project.Utils.YamlReader;

/**
 * Class to test the cofiguration reading of all different yaml files to create a Datacenter
 * configuration.
 */
public class FourConfigFilesExmaple {
    public static void main(String[] args) {
        /*
         * DatacenterConfig datacenterConfig =
         * YamlReader.readConfig("./src/main/java/project/Examples/y_test.yaml"); SimGenerator
         * simGenerator = new SimGenerator(datacenterConfig); simGenerator.generateSimulation();
         */


        String configFolderPath = "src/main/java/resources/configs/example1";
        String costsFile = configFolderPath.concat("/costs.yaml");
        String hostsFile = configFolderPath.concat("/hostsConfig.yaml");
        String vmsFile = configFolderPath.concat("/vmsConfig.yaml");
        String tasksFile = configFolderPath.concat("/tasksConfig.yaml");
        DatacenterConfig datacenterConfig = YamlReader.readConfig(costsFile, hostsFile, vmsFile, tasksFile);
        (new SimGenerator(datacenterConfig)).generateSimulation();
    }
}
