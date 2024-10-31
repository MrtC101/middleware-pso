package project.Examples;

import project.Experiment.ComparativeSimulation;
import project.Experiment.Configurations.DatacenterConfig;
import project.Experiment.Configurations.DatacenterConfig.VmConfig;
import project.Utils.YamlReader;

public class RunScenarios {

    public static void main(String[] args) {
        // @TODO Trabajo en progreso, al diea es plantear la ejecución de todos los scenarios aca.
        // pero tengo que determinar si las maquinas vana ser aleatorias o vana ser preestablecidas.

        // Ruta del archivo de configuración YAML
        String confPath = "src/main/java/resources/configs/config.yaml";
        String resPath = "src/main/java/resources/results";
        //int seed = 10;
        int[] vmsNumbers = {1, 3, 6, 9, 15};
        int[] cloudletsNumber = {25, 50, 100, 150, 200};
        DatacenterConfig datacenterConfig = YamlReader.readConfig(confPath);
        for (int vmsN : vmsNumbers) {
            for (int cloudN : cloudletsNumber) {
                //@TODO modificar para que la cantidad d ecloudlets y de maquinas virtuales este separada
                // de la configuración.
                datacenterConfig.tasks.number = cloudN;
                for(VmConfig vmconf :datacenterConfig.vms){
                    vmconf.number = vmsN;
                }
                ComparativeSimulation sim =
                        new ComparativeSimulation(datacenterConfig);
                String resultPath = resPath.concat(String.format("/scenario-%d-%d", vmsN, cloudN));
                sim.runSimulation(resultPath);
            }
        }
    }
}
