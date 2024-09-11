package project.Experiment;

import java.util.ArrayList;


import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.brokers.DatacenterBroker;
import org.cloudsimplus.datacenters.Datacenter;
import org.cloudsimplus.datacenters.DatacenterSimple;
import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.vms.Vm;

import project.Experiment.TaskGenerator;
import project.Experiment.VMGenerator;
import project.PSO.DatacenterBrokerPSO;
import project.Experiment.HostGenerator;


public class SimGenerator {
    private TaskGenerator taskGenerator;
    private VMGenerator vmGenerator;
    private HostGenerator hostGenerator;
    
    private ArrayList<DatacenterBroker> brokers;
    
    public SimGenerator(TaskGenerator taskGenerator, VMGenerator vmGenerator, HostGenerator hostGenerator, ArrayList<DatacenterBroker> brokers) {
        this.taskGenerator = taskGenerator;
        this.vmGenerator = vmGenerator;
        this.hostGenerator = hostGenerator;
        this.brokers = brokers;
    }


    public void generateSimulation() {
        CloudSimPlus simulation;
        simulation = new CloudSimPlus();
        ArrayList<Host> hostList = hostGenerator.generate();
        Datacenter datacenter = new DatacenterSimple(simulation, hostList);
        ArrayList<Cloudlet> taskList =  taskGenerator.generate(); // !Generar cloudlets por usuario (broker)
        ArrayList<Vm> vmList = vmGenerator.generate();
        for (DatacenterBroker broker : brokers) {
            broker.submitVmList(vmList);
            broker.submitCloudletList(taskList);
        }
    
        
        simulation.start();
    }
}
