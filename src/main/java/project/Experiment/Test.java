package project.Experiment;

import project.Utils.YamlReader;


public class Test {

    
    public static void main(String[] args) {

        DatacenterConfig datacenterConfig = YamlReader.readConfig("./src/main/java/project/Experiment/y_test.yaml");
        SimGenerator simGenerator = new SimGenerator(datacenterConfig);
        simGenerator.generateSimulation();
    }
}
