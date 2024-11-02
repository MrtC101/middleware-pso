package project.Experiment;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.qos.logback.classic.Level;
import org.cloudsimplus.brokers.DatacenterBroker;
import org.cloudsimplus.brokers.DatacenterBrokerHeuristic;
import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.distributions.UniformDistr;
import org.cloudsimplus.datacenters.DatacenterSimple;
import org.cloudsimplus.heuristics.CloudletToVmMappingSimulatedAnnealing;
import org.cloudsimplus.util.Log;
import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.vms.Vm;
import project.Experiment.Configurations.DatacenterConfig;
import project.Experiment.Generators.HostGenerator;
import project.Experiment.newGenarators.RandomTaskGenerator;
import project.Experiment.newGenarators.RandomVmGenerator;
import project.PSO.DatacenterBrokerPSO;
import project.Utils.CSVWriter;

/**
 * Simulation to compare three balancers policies.
 */
public class ComparativeSimulation {
    private CloudletToVmMappingSimulatedAnnealing heuristic;

    private RandomTaskGenerator taskGenerator;
    private RandomVmGenerator vmGenerator;
    private HostGenerator hostGenerator;

    private List<String[]> cloudletData = new ArrayList<>();
    private List<String[]> vmData = new ArrayList<>();
    private ArrayList<String[]> MetricsData = new ArrayList<>();
    private ArrayList<String[]> CloudleCharstData = new ArrayList<>();
    private ArrayList<String[]> VmCharsData = new ArrayList<>();

    public ComparativeSimulation(DatacenterConfig datacenterConfig) {
        this.taskGenerator = new RandomTaskGenerator(datacenterConfig.tasks, 10);
        this.vmGenerator = new RandomVmGenerator(datacenterConfig.vms, 10);
        this.hostGenerator = new HostGenerator(datacenterConfig.hosts, 10);

        // Header for cloudlets
        cloudletData.add(new String[] { "Broker", "Cloudlet ID", "VM ID", "Execution Time",
                "Start Time", "Finish Time", "CPU Utilization Start", "CPU Utilization Finish" });
        // Header for VMs
        vmData.add(new String[] { "Broker", "VM ID", "VM Load", "Total Execution Time" });
        // Header for Metrics
        MetricsData.add(new String[] { "Broker", "Maskespan", "Cpu Utilization", "Throughput" });
        CloudleCharstData.add(new String[] { "Cloudlet", "Pes", "Lenght" });
        VmCharsData.add(new String[] { "Vm", "Pes", "Mips" });
    }

    /**
     * Runs a simulation for each of each of the three cloudlet load balancer
     * policies RoundRobin,
     * Simulated Annealing an PSO.
     * 
     * @return Two csv files stored in results
     */
    public void runSimulation(String savePath) {
        Log.setLevel(Level.INFO);

        final DatacenterBrokerPSO brokerPSO = new DatacenterBrokerPSO(new CloudSimPlus());
        final DatacenterBrokerSimple brokerSimple = new DatacenterBrokerSimple(new CloudSimPlus());
        final DatacenterBrokerHeuristic brokerHeuristic = new DatacenterBrokerHeuristic(new CloudSimPlus());

        createSimulatedAnnealingHeuristic();
        brokerHeuristic.setHeuristic(heuristic);

        ArrayList<Cloudlet> taskList = taskGenerator.generate();
        ArrayList<Vm> vmList = vmGenerator.generate();

        ArrayList<Cloudlet> taskList2 = taskGenerator.cloneDeep(taskList);
        ArrayList<Vm> vmList2 = vmGenerator.cloneDeep(vmList);

        ArrayList<Cloudlet> taskList3 = taskGenerator.cloneDeep(taskList);
        ArrayList<Vm> vmList3 = vmGenerator.cloneDeep(vmList);

        // Ejecutar simulaciones secuenciales
        executeBrokerSimulation(brokerSimple, taskList, vmList);
        executeBrokerSimulation(brokerPSO, taskList2, vmList2);
        executeBrokerSimulation(brokerHeuristic, taskList3, vmList3);

        for (Cloudlet cloudlet : taskList) {
            CloudleCharstData.add(new String[] {
                String.valueOf(cloudlet.getId()),
                String.valueOf(cloudlet.getPesNumber()),
                String.valueOf(cloudlet.getLength()),
            });
        }

        for (Vm vm : vmList) {
            VmCharsData.add(new String[] {
                String.valueOf(vm.getId()),
                String.valueOf(vm.getPesNumber()),
                String.valueOf(vm.getMips()),
            });
        }


        // Save csvs
        CSVWriter.writeCSV(savePath.concat("/cloudlets_results.csv"), cloudletData);
        CSVWriter.writeCSV(savePath.concat("/vms_results.csv"), vmData);
        CSVWriter.writeCSV(savePath.concat("/brokers_results.csv"), MetricsData);
        CSVWriter.writeCSV(savePath.concat("/vms_chars.csv"), VmCharsData);
        CSVWriter.writeCSV(savePath.concat("/cloudlet_chars.csv"), CloudleCharstData);
    }
    
    /**
     * Executes the simulation for an specific DatacenterBroker
     * 
     * @param broker   DatacenterBroker that implements the corresponding load
     *                 balance policy.
     * @param taskList List of cloudlets to run in the simulation.
     * @param vmList   List of VMs to in the simulation.
     */
    private void executeBrokerSimulation(DatacenterBroker broker, ArrayList<Cloudlet> taskList,
            ArrayList<Vm> vmList) {
        ArrayList<Host> hostList = hostGenerator.generate();
        new DatacenterSimple(broker.getSimulation(), hostList);

        broker.submitVmList(vmList);
        broker.submitCloudletList(taskList);

        if (broker instanceof DatacenterBrokerPSO) {
            ((DatacenterBrokerPSO) (broker)).runPSO(100, 1000, 0.9, 2.0, 2.0, true);
            ;
        }

        long startTime = System.currentTimeMillis();
        broker.getSimulation().start();
        long endTime = System.currentTimeMillis();

        List<Cloudlet> cloudletFinishedList = broker.getCloudletFinishedList();
        long executionTime = endTime - startTime;
        System.out.println("Broker: " + broker.getClass().getSimpleName() + " Execution time: "
                + executionTime);
        computeMetricsData(broker, cloudletFinishedList, vmList);
    }

    /**
     * Configures the Simulated Annealing DatacenterBroker parameters.
     */
    private void createSimulatedAnnealingHeuristic() {
        heuristic = new CloudletToVmMappingSimulatedAnnealing(1.0, new UniformDistr(0, 1));
        heuristic.setColdTemperature(0.0001);
        heuristic.setCoolingRate(0.003);
        heuristic.setSearchesByIteration(20);
    }

    private void computeMetricsData(DatacenterBroker broker, List<Cloudlet> cloudletFinishedList,
            ArrayList<Vm> vmList) {

        String brokerName = broker.getClass().getSimpleName();

        for (Cloudlet cloudlet : cloudletFinishedList) {
            cloudletData.add(new String[] { brokerName, String.valueOf(cloudlet.getId()),
                    String.valueOf(cloudlet.getVm().getId()),
                    String.valueOf(Math.round(cloudlet.getActualCpuTime())),
                    String.valueOf(Math.round(cloudlet.getExecStartTime())),
                    String.valueOf(Math.round(cloudlet.getFinishTime())),
                    String.valueOf(Math.round(cloudlet.getUtilizationOfCpu(cloudlet.getExecStartTime()))),
                    String.valueOf(Math.round(cloudlet.getUtilizationOfCpu(cloudlet.getFinishTime()))),
            });
        }

        // Crear un objeto DecimalFormatSymbols y establecer el punto como separador
        // decimal
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.'); // Establecer el punto como separador decimal
        symbols.setGroupingSeparator(','); // Establecer la coma como separador de miles
        DecimalFormat df = new DecimalFormat("#.##", symbols);

        for (Vm vm : vmList) {
            double vmLoad = computeVMLoad(vm, cloudletFinishedList);
            vmData.add(new String[] { brokerName, String.valueOf(vm.getId()),
                    String.valueOf(df.format(vmLoad)),
                    String.valueOf(Math.round(vm.getTotalExecutionTime())),
            });
        }

        double makespan = computeMakespan(cloudletFinishedList);
        double cpuUtilization = computeCpuUsage(broker, vmList, cloudletFinishedList);
        double throughput = vmList.size() / (double) makespan;
        MetricsData.add(new String[] { brokerName, String.valueOf(df.format(makespan)),
                String.valueOf(df.format(cpuUtilization)), String.valueOf(df.format(throughput)) });
        
    }

    /**
     * 
     * @param vm
     * @param cloudletList
     * @return
     */
    private double computeVMLoad(Vm vm, List<Cloudlet> cloudletList) {
        Map<Long, List<Integer>> Vm2Cloudlet = matchVmsToCloudlets(cloudletList);
        List<Integer> cloudlets = Vm2Cloudlet.get(vm.getId());
        return cloudlets != null ? (double) cloudlets.size() / (double) cloudletList.size() : 0.0;
    }

    /**
     * 
     * @param broker
     * @param vmList
     * @param cloudletList
     * @return
     */
    private double computeCpuUsage(DatacenterBroker broker, List<Vm> vmList,
    List<Cloudlet> cloudletList) {
        Map<Long, List<Integer>> Vm2Cloudlet = matchVmsToCloudlets(cloudletList);
        double totalCpuUsage = 0.0;
        // TODO Auto-generated method stub
        for (Vm vm : vmList) {
            // Compute CPU utilization for one VM.
            double totalCpuTime = 0.0;
            double totalTime = broker.getSimulation().clock();
            List<Integer> cloudlets = Vm2Cloudlet.get(vm.getId());
            if (cloudlets != null){
                for (int cloudletId : cloudlets) {
                    Cloudlet current = cloudletList.get(cloudletId);
                    totalCpuTime += current.getActualCpuTime();
                }
            }
            totalCpuUsage += totalTime > 0 ? totalCpuTime / totalTime : 0;
        }
        return totalCpuUsage / vmList.size();
    }


    /**
     * 
     * @param cloudletList
     * @return
     */
    private double computeMakespan(List<Cloudlet> cloudletList) {
        double maxFinishTime = 0.0;
        double minStartTime = 0.0;
        for (Cloudlet cloudlet : cloudletList) {
            maxFinishTime = Math.max(maxFinishTime, cloudlet.getFinishTime());
            minStartTime = Math.min(minStartTime, cloudlet.getExecStartTime());
        }
        return maxFinishTime - minStartTime;
    }

    /**
     * 
     * @param cloudletsList
     * @return
     */
    private Map<Long, List<Integer>> matchVmsToCloudlets(List<Cloudlet> cloudletsList) {
        Map<Long, List<Integer>> vmToCloudletMap = new HashMap<>();

        for (int i = 0; i < cloudletsList.size(); i++) {
            Cloudlet cloudlet = cloudletsList.get(i);
            long vmId = cloudlet.getVm().getId();
            if (vmToCloudletMap.get(vmId) == null) {
                vmToCloudletMap.put(vmId, new ArrayList<>());
            }
            vmToCloudletMap.get(vmId).add(i);
        }
        return vmToCloudletMap;
    }
}
