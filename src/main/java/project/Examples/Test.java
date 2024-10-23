package project.Examples;

import project.Experiment.DatacenterConfig;
import project.Experiment.SimGenerator;
import project.Utils.YamlReader;


public class Test {
    public static void main(String[] args) {
        /*
        DatacenterConfig datacenterConfig = YamlReader.readConfig("./src/main/java/project/Examples/y_test.yaml");
        SimGenerator simGenerator = new SimGenerator(datacenterConfig);
        simGenerator.generateSimulation();
         */
        

       
        String costsFile = "./src/main/java/project/Examples/costs.yaml";
        String hostsFile = "./src/main/java/project/Examples/hostsConfig.yaml";
        String vmsFile = "./src/main/java/project/Examples/vmsConfig.yaml";
        String tasksFile = "./src/main/java/project/Examples/tasksConfig.yaml";
        DatacenterConfig datacenterConfig2 = YamlReader.readConfig(costsFile, hostsFile, vmsFile, tasksFile);
        SimGenerator simGenerator2 = new SimGenerator(datacenterConfig2);
        simGenerator2.generateSimulation();
    }
}
