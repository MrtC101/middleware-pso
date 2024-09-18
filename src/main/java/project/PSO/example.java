package project.PSO;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.cloudsimplus.brokers.DatacenterBroker;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.utilizationmodels.UtilizationModelDynamic;
import org.cloudsimplus.utilizationmodels.UtilizationModelFull;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.datacenters.Datacenter;
import org.cloudsimplus.datacenters.DatacenterSimple;
import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.hosts.HostSimple;
import org.cloudsimplus.provisioners.ResourceProvisionerSimple;
import org.cloudsimplus.resources.Pe;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudsimplus.schedulers.vm.VmSchedulerTimeShared;
import org.cloudsimplus.vms.Vm;
import org.cloudsimplus.vms.VmSimple;

public class example {
    private static final int HOSTS_TO_CREATE = 100;
    private static final int VMS_TO_CREATE = 50;
    private static final int CLOUDLETS_TO_CREATE = 100;

    /**
     * Simulated Annealing (SA) parameters.
     */
    public static final double SA_INITIAL_TEMPERATURE = 1.0;
    public static final double SA_COLD_TEMPERATURE = 0.0001;
    public static final double SA_COOLING_RATE = 0.003;
    public static final int SA_NUMBER_OF_NEIGHBORHOOD_SEARCHES = 50;

    private final CloudSimPlus simulation;
    private final List<Cloudlet> cloudletList;
    private List<Vm> vmList;

    /**
     * Number of cloudlets created so far.
     */
    private int createdCloudlets = 0;
    /**
     * Number of VMs created so far.
     */
    private int createdVms = 0;
    /**
     * Number of hosts created so far.
     */
    private int createdHosts = 0;

    public static void main(String[] args) {
        new example();
    }

    /**
     * Default constructor where the simulation is built.
     */
    private example() {
        System.out.println("Starting " + getClass().getSimpleName());
        this.vmList = new ArrayList<>();
        this.cloudletList = new ArrayList<>();

        simulation = new CloudSimPlus();

        final Datacenter datacenter0 = createDatacenter();
        final DatacenterBrokerPSO broker0 = new DatacenterBrokerPSO(simulation, "");

        createAndSubmitVms(broker0);
        createAndSubmitCloudlets(broker0);
        broker0.runPSO(10, 50, 1, 1, 1);
        simulation.start();

        final List cloudletFinishedList = broker0.getCloudletFinishedList();
        System.out.println(broker0);
    }

    private void createAndSubmitCloudlets(final DatacenterBroker broker0) {
        for (int i = 0; i < CLOUDLETS_TO_CREATE; i++) {
            cloudletList.add(createCloudlet(broker0, getRandomPesNumber(4)));
        }
        broker0.submitCloudletList(cloudletList);
    }

    private void createAndSubmitVms(final DatacenterBroker broker0) {
        vmList = new ArrayList<>(VMS_TO_CREATE);
        for (int i = 0; i < VMS_TO_CREATE; i++) {
            vmList.add(createVm(broker0, getRandomPesNumber(4)));
        }
        broker0.submitVmList(vmList);
    }

    private int getRandomPesNumber(int i) {
        Random r = new Random();
        return r.nextInt(12) + 1;
    }

    private DatacenterSimple createDatacenter() {
        final List hostList = new ArrayList<Host>();
        for (int i = 0; i < HOSTS_TO_CREATE; i++) {
            hostList.add(createHost());
        }

        return new DatacenterSimple(simulation, hostList);
    }

    private Host createHost() {
        final long mips = 1000; // capacity of each CPU core (in Million Instructions per Second)
        final int ram = 2048; // host memory (Megabyte)
        final long storage = 1000000; // host storage
        final long bw = 10000;

        final List peList = new ArrayList<Pe>();
        /*
         * Creates the Host's CPU cores and defines the provisioner used to allocate each core for
         * requesting VMs.
         */
        for (int i = 0; i < 8; i++)
            peList.add(new PeSimple(mips));

        return new HostSimple(ram, bw, storage, peList)
                .setRamProvisioner(new ResourceProvisionerSimple())
                .setBwProvisioner(new ResourceProvisionerSimple())
                .setVmScheduler(new VmSchedulerTimeShared());
    }

    private Vm createVm(final DatacenterBroker broker, final int pesNumber) {
        final long mips = 1000;
        final long storage = 10000; // vm image size (Megabyte)
        final int ram = 512; // vm memory (Megabyte)
        final long bw = 1000; // vm bandwidth

        return new VmSimple(createdVms++, mips, pesNumber).setRam(ram).setBw(bw).setSize(storage)
                .setCloudletScheduler(new CloudletSchedulerTimeShared());
    }

    private Cloudlet createCloudlet(final DatacenterBroker broker, final int pesNumber) {
        final long length = 400000; // in Million Instructions (MI)
        final long fileSize = 300; // Size (in bytes) before execution
        final long outputSize = 300; // Size (in bytes) after execution

        final UtilizationModelFull utilizationFull = new UtilizationModelFull();
        final UtilizationModelDynamic utilizationDynamic = new UtilizationModelDynamic(0.1);

        return new CloudletSimple(createdCloudlets++, length, pesNumber).setFileSize(fileSize)
                .setOutputSize(outputSize).setUtilizationModelCpu(utilizationFull)
                .setUtilizationModelRam(utilizationDynamic)
                .setUtilizationModelBw(utilizationDynamic);
    }

}
