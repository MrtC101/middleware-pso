package project.Examples;

import project.Experiment.DatacenterConfig;
import project.Experiment.SimGenerator;
import project.Utils.YamlReader;


public class Test {
    public static void main(String[] args) {
        DatacenterConfig datacenterConfig = YamlReader.readConfig("./src/main/java/project/Examples/y_test.yaml");
        SimGenerator simGenerator = new SimGenerator(datacenterConfig);
        simGenerator.generateSimulation();
    }
}
