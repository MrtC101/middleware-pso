package project.Experiment;

import java.util.ArrayList;
import java.util.List;

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
import project.Experiment.Generators.TaskGenerator;
import project.Experiment.Generators.VMGenerator;
import project.PSO.DatacenterBrokerPSO;
import project.Utils.CSVWriter;


public class Simulation {
    private CloudletToVmMappingSimulatedAnnealing heuristic;

    private TaskGenerator taskGenerator;
    private VMGenerator vmGenerator;
    private HostGenerator hostGenerator;

    private List<String[]> cloudletData = new ArrayList<>();
    private List<String[]> vmData = new ArrayList<>();

    public Simulation(DatacenterConfig datacenterConfig) {
        this.taskGenerator = new TaskGenerator(datacenterConfig.tasks);
        this.vmGenerator = new VMGenerator(datacenterConfig.vms);
        this.hostGenerator = new HostGenerator(datacenterConfig.hosts);

        // Agregar cabecera para cloudletData
        cloudletData.add(new String[]{"Broker", "Cloudlet ID", "Execution Time", "Start Time", "Finish Time", "VM ID", "CPU Utilization"});
        // Agregar cabecera para vmData
        vmData.add(new String[]{"Broker", "VM ID", "Total Execution Time"});
    }

    // Lista de brokers
    private ArrayList<DatacenterBroker> brokers;

    public void runSimulation() {
        Log.setLevel(Level.INFO);
    
        final DatacenterBrokerPSO brokerPSO = new DatacenterBrokerPSO(new CloudSimPlus(), "");
        final DatacenterBrokerSimple brokerSimple = new DatacenterBrokerSimple(new CloudSimPlus());
        final DatacenterBrokerHeuristic brokerHeuristic = new DatacenterBrokerHeuristic(new CloudSimPlus());
    
        createSimulatedAnnealingHeuristic();
        brokerHeuristic.setHeuristic(heuristic);

        ArrayList<Cloudlet> taskList1 = taskGenerator.generate();
        ArrayList<Vm> vmList1 = vmGenerator.generate();

        ArrayList<Cloudlet> taskList2 = taskGenerator.generate();
        ArrayList<Vm> vmList2 = vmGenerator.generate();

        ArrayList<Cloudlet> taskList3 = taskGenerator.generate();
        ArrayList<Vm> vmList3 = vmGenerator.generate();
    
        // Ejecutar simulaciones secuenciales
        executeBrokerSimulation(brokerSimple, taskList1, vmList1);
        executeBrokerSimulation(brokerPSO, taskList2, vmList2);
        executeBrokerSimulation(brokerHeuristic, taskList3, vmList3);
        
        // Exportar los resultados después de todas las simulaciones
        exportCloudletResultsToCSV("combined_cloudlets_results.csv");
        exportVMResultsToCSV("combined_vms_results.csv");
    }
    
    private void executeBrokerSimulation(DatacenterBroker broker, ArrayList<Cloudlet> taskList, ArrayList<Vm> vmList) {
        ArrayList<Host> hostList = hostGenerator.generate();
        new DatacenterSimple(broker.getSimulation(), hostList);
    
        broker.submitVmList(vmList);
        broker.submitCloudletList(taskList);
    
        if (broker instanceof DatacenterBrokerPSO) {
            ((DatacenterBrokerPSO) (broker)).runPSO(20, 500, 1, 2, 2, false);
        }
    
        long startTime = System.currentTimeMillis();
        broker.getSimulation().start();
        long endTime = System.currentTimeMillis();
    
        List<Cloudlet> cloudletFinishedList = broker.getCloudletFinishedList();
        long executionTime = endTime - startTime;
        System.out.println("Broker: " + broker.getClass().getSimpleName() + " Execution time: " + executionTime);
    
        collectCloudletData(broker, cloudletFinishedList);
        collectVMData(broker, vmList);
    }

    private void collectCloudletData(DatacenterBroker broker, List<Cloudlet> cloudletFinishedList) {
        for (Cloudlet cloudlet : cloudletFinishedList) {
            Vm vm = cloudlet.getVm();
            double cpuUtilization = vm.getCpuPercentUtilization(cloudlet.getActualCpuTime());
            cloudletData.add(new String[]{
                broker.getClass().getSimpleName(),
                String.valueOf(cloudlet.getId()),
                String.valueOf(cloudlet.getActualCpuTime()),
                String.valueOf(cloudlet.getExecStartTime()),
                String.valueOf(cloudlet.getFinishTime()),
                String.valueOf(vm.getId()),
                String.valueOf(cpuUtilization)
            });
        }
    }

    private void collectVMData(DatacenterBroker broker, ArrayList<Vm> vmList) {
        for (Vm vm : vmList) {
            vmData.add(new String[]{
                broker.getClass().getSimpleName(),
                String.valueOf(vm.getId()),
                String.valueOf(vm.getTotalExecutionTime())
            });
        }
    }

    private void createSimulatedAnnealingHeuristic() {
		heuristic = new CloudletToVmMappingSimulatedAnnealing(1.0, new UniformDistr(0, 1));
		heuristic.setColdTemperature(0.0001);
		heuristic.setCoolingRate(0.003);
        heuristic.setSearchesByIteration(20);
	}

    public void exportCloudletResultsToCSV(String fileName) {
        CSVWriter.writeCSV(fileName, cloudletData);
    }

    public void exportVMResultsToCSV(String fileName) {
        CSVWriter.writeCSV(fileName, vmData);
    }
}