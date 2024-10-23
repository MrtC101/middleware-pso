package project.Experiment.Generators;

import java.util.ArrayList;

import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.hosts.HostSimple;
import org.cloudsimplus.resources.Pe;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.schedulers.vm.VmSchedulerSpaceShared;
import org.cloudsimplus.schedulers.vm.VmSchedulerTimeShared;
import project.Experiment.Configurations.DatacenterConfig;
import project.Utils.RandomUtils;
import static project.Utils.RandomUtils.randomIntMultiple;


/**
 * HostGenerator class generates hosts based on the configuration provided. It supports both
 * homogeneous and heterogeneous hosts with random variations.
 */
public class HostGenerator implements Generator<Host> {
    // Configuration settings for hosts from the datacenter
    private ArrayList<DatacenterConfig.HostConfig> hostsConfig;
    private int seed;

    // Constants for VM scheduler types
    public static final int VM_SCHEDULER_TIMESHARED = 0;
    public static final int VM_SCHEDULER_SPACESHARED = 1;

    // Multipliers for generating random values for various host parameters
    int H_STORAGE_MULT = 1000;
    int H_MIPS_MULT = 100;
    int H_RAM_MULT = 512;
    int H_BW_MULT = 100;
    int H_PES_MULT = 1;

    /**
     * Constructor that takes the host configuration and a random seed.
     * 
     * @param hostsConfig List of host configuration settings
     * @param seed Random seed for generating random numbers
     */
    public HostGenerator(ArrayList<DatacenterConfig.HostConfig> hostsConfig, int seed) {
        this.hostsConfig = hostsConfig;
        this.seed = seed;
    }

    /**
     * Constructor without seed, only taking the host configuration.
     * 
     * @param hostsConfig List of host configuration settings
     */
    public HostGenerator(ArrayList<DatacenterConfig.HostConfig> hostsConfig) {
        this.hostsConfig = hostsConfig;
    }

    /**
     * Creates a host with the specified parameters.
     * 
     * @param mips MIPS rating for the processing elements (PEs)
     * @param ram Amount of RAM for the host
     * @param peList List of PEs for the host
     * @param bw Bandwidth for the host
     * @param storage Storage capacity for the host
     * @param vmScheduler The type of VM scheduler to use
     * @return A new Host object with the specified configuration
     */
    private Host createHost(int mips, int ram, ArrayList<Pe> peList, int bw, int storage,
            int vmScheduler) {
        Host host = new HostSimple(ram, bw, storage, peList);
        if (vmScheduler == VM_SCHEDULER_TIMESHARED)
            host.setVmScheduler(new VmSchedulerTimeShared());
        if (vmScheduler == VM_SCHEDULER_SPACESHARED)
            host.setVmScheduler(new VmSchedulerSpaceShared());
        return host;
    }

    /**
     * Generates a list of hosts based on the configuration settings, using random values for
     * certain parameters based on multipliers.
     * 
     * @return A list of generated hosts
     */

    @Override
    public ArrayList<Host> generate() {
        int mips, ram, pesNumber, bw, storage;
        ArrayList<Host> hostList = new ArrayList<>();

        // Loop through each host configuration
        for (DatacenterConfig.HostConfig hostConfig : hostsConfig) {
            // Generate a random (or not) number of hosts based on the configuration
            int hosts = randomIntMultiple(hostConfig.size, 1);
            ArrayList<Pe> peList;

            // If the configuration is for heterogeneous hosts, generate random (or not) values
            if (hostConfig.heterogeneous) {
                // Generate random values for MIPS, RAM, PEs, bandwidth, and storage
                mips = RandomUtils.randomIntMultiple(hostConfig.mips, H_MIPS_MULT);
                ram = RandomUtils.randomIntMultiple(hostConfig.ram, H_RAM_MULT);
                pesNumber = RandomUtils.randomIntMultiple(hostConfig.pesNumber, H_PES_MULT);
                peList = new ArrayList<>(pesNumber);

                // Create PEs with random MIPS
                for (int i = 0; i < pesNumber; i++) {
                    peList.add(new PeSimple(mips));
                }
                bw = RandomUtils.randomIntMultiple(hostConfig.bw, H_BW_MULT);
                storage = RandomUtils.randomIntMultiple(hostConfig.storage, H_STORAGE_MULT);

                // Add the generated hosts to the list
                for (int i = 0; i < hosts; i++) {
                    hostList.add(
                            createHost(mips, ram, peList, bw, storage, hostConfig.vmScheduler));
                }
            } else {
                // For homogeneous hosts, generate fixed random (or not) values for each host
                for (int i = 0; i < hosts; i++) {
                    mips = RandomUtils.randomIntMultiple(hostConfig.mips, H_MIPS_MULT);
                    ram = RandomUtils.randomIntMultiple(hostConfig.ram, H_RAM_MULT);
                    pesNumber = RandomUtils.randomIntMultiple(hostConfig.pesNumber, H_PES_MULT);
                    peList = new ArrayList<>(pesNumber);
                    // Create PEs with the same MIPS for each host
                    for (int j = 0; j < pesNumber; j++) {
                        peList.add(new PeSimple(mips));
                    }
                    bw = RandomUtils.randomIntMultiple(hostConfig.bw, H_BW_MULT);
                    storage = RandomUtils.randomIntMultiple(hostConfig.storage, H_STORAGE_MULT);

                    // Add the created host to the list
                    hostList.add(
                            createHost(mips, ram, peList, bw, storage, hostConfig.vmScheduler));
                }
            }
        }
        return hostList; // Return the generated host list
    }
}
