package project.Experiment;

import java.util.ArrayList;
import java.util.List;

import ch.qos.logback.classic.Level;
import org.cloudsimplus.brokers.DatacenterBroker;
import org.cloudsimplus.brokers.DatacenterBrokerHeuristic;
import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.distributions.UniformDistr;
import org.cloudsimplus.datacenters.DatacenterSimple;
import org.cloudsimplus.heuristics.CloudletToVmMappingSimulatedAnnealing;
import org.cloudsimplus.util.Log;
import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.vms.Vm;
import org.cloudsimplus.vms.VmSimple;


import project.PSO.DatacenterBrokerPSO;
import project.Utils.CSVWriter;


public class Simulation {
    private CloudletToVmMappingSimulatedAnnealing heuristic;

    private TaskGenerator taskGenerator;
    private VMGenerator vmGenerator;
    private HostGenerator hostGenerator;

    public Simulation(DatacenterConfig datacenterConfig) {
        this.taskGenerator = new TaskGenerator(datacenterConfig.tasks);
        this.vmGenerator = new VMGenerator(datacenterConfig.vms);
        this.hostGenerator = new HostGenerator(datacenterConfig.hosts);
    }

    // Lista de brokers
    private ArrayList<DatacenterBroker> brokers;

    public void runSimulation() {
        Log.setLevel(Level.INFO);

        // Crear brokers para las diferentes políticas
        final DatacenterBrokerPSO brokerPSO = new DatacenterBrokerPSO(new CloudSimPlus(), "");
        final DatacenterBrokerSimple brokerSimple = new DatacenterBrokerSimple(new CloudSimPlus());
        final DatacenterBrokerHeuristic brokerHeuristic =
                new DatacenterBrokerHeuristic(new CloudSimPlus());

        // Crea la heurística de SimulatedAnnealing y se la setea al broker
        createSimulatedAnnealingHeuristic();
        brokerHeuristic.setHeuristic(heuristic);

        // Generar las tareas (cloudlets) y VMs
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
    }

    private void executeBrokerSimulation(DatacenterBroker broker, ArrayList<Cloudlet> taskList,
            ArrayList<Vm> vmList) {
        // Generar hosts y crear un datacenter simple
        ArrayList<Host> hostList = hostGenerator.generate();
        new DatacenterSimple(broker.getSimulation(), hostList);

        // Asignar VMs y Cloudlets al broker
        broker.submitVmList(vmList);
        broker.submitCloudletList(taskList);

        if (broker instanceof DatacenterBrokerPSO) {
            ((DatacenterBrokerPSO) (broker)).runPSO(20, 500, 1, 2, 2);
        }

        // Ejecutar simulación
        long startTime = System.currentTimeMillis();
        broker.getSimulation().start();
        long endTime = System.currentTimeMillis();

        // Recoger resultados
        List<Cloudlet> cloudletFinishedList = broker.getCloudletFinishedList();
        long executionTime = endTime - startTime;
        System.out.println("Broker: " + broker.getClass().getSimpleName() + " Execution time: "
                + executionTime);

        // Mostrar resultados
        new CloudletsTableBuilder(cloudletFinishedList).build();

        // Guardar en CSV
        exportResultsToCSV(broker.getClass().getSimpleName(), cloudletFinishedList, executionTime);
    }

    private void createSimulatedAnnealingHeuristic() {
        heuristic = new CloudletToVmMappingSimulatedAnnealing(1.0, new UniformDistr(0, 1));
        heuristic.setColdTemperature(0.0001);
        heuristic.setCoolingRate(0.003);
        heuristic.setSearchesByIteration(20);
    }

    public void exportResultsToCSV(String brokerName, List<Cloudlet> cloudletFinishedList,
            long executionTime) {
        List<String[]> data = new ArrayList<>();

        // Agregar cabeceras
        data.add(new String[] {"Broker", "Cloudlet ID", "Execution Time", "Start Time",
                "Finish Time"});

        // Agregar resultados de cada Cloudlet
        for (Cloudlet cloudlet : cloudletFinishedList) {
            data.add(new String[] {brokerName, String.valueOf(cloudlet.getId()),
                    String.valueOf(cloudlet.getActualCpuTime()),
                    String.valueOf(cloudlet.getExecStartTime()),
                    String.valueOf(cloudlet.getFinishTime())});
        }

        // Agregar tiempo total de ejecución
        data.add(new String[] {"Total Execution Time", String.valueOf(executionTime)});

        // Exportar a CSV
        CSVWriter.writeCSV("src/main/java/resources/results/DatacenterBrokesResults.csv", data);
    }
}
