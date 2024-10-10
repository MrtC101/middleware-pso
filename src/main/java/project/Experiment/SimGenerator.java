package project.Experiment;

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

import project.PSO.DatacenterBrokerPSO;

public class SimGenerator {
    private TaskGenerator taskGenerator;
    private VMGenerator vmGenerator;
    private HostGenerator hostGenerator;
    
    private ArrayList<DatacenterBroker> brokers;
    
    public SimGenerator(DatacenterConfig datacenterConfig) {
        this.taskGenerator = new TaskGenerator(datacenterConfig.tasks);
        this.vmGenerator = new VMGenerator(datacenterConfig.vms);
        this.hostGenerator = new HostGenerator(datacenterConfig.hosts);
    }

    public void generateSimulation() {
        CloudSimPlus simulation;
        simulation = new CloudSimPlus();
        ArrayList<DatacenterBroker> brokers = new ArrayList<>();
        final DatacenterBrokerPSO broker0 = new DatacenterBrokerPSO(simulation, "");
        brokers.add(broker0);

        ArrayList<Host> hostList = hostGenerator.generate();
        Datacenter datacenter = new DatacenterSimple(simulation, hostList);
        ArrayList<Cloudlet> taskList =  taskGenerator.generate(); // !Generar cloudlets por usuario (broker)
        ArrayList<Vm> vmList = vmGenerator.generate();
        
        for (DatacenterBroker broker : brokers) {
            broker.submitVmList(vmList);
            broker.submitCloudletList(taskList);
            if (broker instanceof DatacenterBrokerPSO) {
                ((DatacenterBrokerPSO) (broker)).runPSO(10, 50, 1, 1, 1);
            }
        }
        simulation.start();
        List cloudletFinishedList;
        for (DatacenterBroker broker : brokers) {
            cloudletFinishedList = broker.getCloudletFinishedList();
            System.out.println(broker);
            new CloudletsTableBuilder(cloudletFinishedList).build();
        }
    }
}
