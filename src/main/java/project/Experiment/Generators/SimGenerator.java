package project.Experiment.Generators;

import java.util.ArrayList;
import java.util.List;

import org.cloudsimplus.brokers.DatacenterBroker;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.datacenters.Datacenter;
import org.cloudsimplus.datacenters.DatacenterSimple;
import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.vms.Vm;
import project.Experiment.Configurations.DatacenterConfig;
import project.PSO.DatacenterBrokerPSO;

/**
 * SimGenerator class handles the creation and execution of the CloudSimPlus simulation, including
 * generating tasks, VMs, hosts.
 */

public class SimGenerator {
    // Task, VM, and Host generators for creating the necessary simulation components
    private TaskGenerator taskGenerator;
    private VMGenerator vmGenerator;
    private HostGenerator hostGenerator;

    // List of brokers participating in the simulation
    private ArrayList<DatacenterBroker> brokers;

    /**
     * Constructor initializes the generators using the configuration settings passed via the
     * DatacenterConfig object.
     * 
     * @param datacenterConfig Configuration object containing tasks, VMs, and hosts definitions
     */
    public SimGenerator(DatacenterConfig datacenterConfig) {
        this.taskGenerator = new TaskGenerator(datacenterConfig.tasks);
        this.vmGenerator = new VMGenerator(datacenterConfig.vms);
        this.hostGenerator = new HostGenerator(datacenterConfig.hosts);
    }

    /**
     * It sets up and runs the cloud simulation.
     */
    public void generateSimulation() {
        // Create a new CloudSimPlus simulation instance
        CloudSimPlus simulation;
        simulation = new CloudSimPlus();

        // Initialize brokers list and create a PSO broker
        ArrayList<DatacenterBroker> brokers = new ArrayList<>();
        final DatacenterBrokerPSO broker0 = new DatacenterBrokerPSO(simulation);
        brokers.add(broker0); // Add broker to the list

        // Generate hosts and create a simple datacenter
        ArrayList<Host> hostList = hostGenerator.generate();
        Datacenter datacenter = new DatacenterSimple(simulation, hostList);

        // Generate tasks (cloudlets) and VMs for the simulation
        ArrayList<Cloudlet> taskList = taskGenerator.generate();
        ArrayList<Vm> vmList = vmGenerator.generate();

        // Submit VMs and tasks to each broker
        for (DatacenterBroker broker : brokers) {
            broker.submitVmList(vmList); // Submit VM list to the broker
            broker.submitCloudletList(taskList); // Submit task (cloudlet) list to the broker
            // If the broker is an instance of DatacenterBrokerPSO, run the PSO algorithm
            if (broker instanceof DatacenterBrokerPSO) {
                ((DatacenterBrokerPSO) (broker)).runPSO(100, 1000, 0.9, 2.0, 2.0,false);
            }
        }

        // Start the simulation
        simulation.start();

        // Retrieve and print the list of finished cloudlets
        List cloudletFinishedList;
        for (DatacenterBroker broker : brokers) {
            cloudletFinishedList = broker.getCloudletFinishedList(); // Get completed cloudlets
            System.out.println(broker); // Print broker information
            new CloudletsTableBuilder(cloudletFinishedList).build(); // Display finished cloudlets
                                                                     // in a table
        }
    }
}
