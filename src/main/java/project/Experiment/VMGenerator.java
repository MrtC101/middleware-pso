package project.Experiment;


import java.util.ArrayList;

import org.cloudsimplus.schedulers.cloudlet.CloudletSchedulerCompletelyFair;
import org.cloudsimplus.schedulers.cloudlet.CloudletSchedulerSpaceShared;
import org.cloudsimplus.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudsimplus.vms.Vm;
import org.cloudsimplus.vms.VmSimple;

import project.Utils.RandomUtils;

/**
 * VMGenerator class generates Virtual Machines (VMs) based on the provided configuration. It
 * supports both homogeneous and heterogeneous VM configurations, with random variations in certain
 * parameters.
 */
public class VMGenerator implements Generator<Vm> {

    // List of VM configurations from the Datacenter
    private ArrayList<DatacenterConfig.VmConfig> vmConfigs;
    private int seed;

    // Constants for task scheduler types
    public static final int T_SCHEDULER_TIMESHARED = 0;
    public static final int T_SCHEDULER_SPACESHARED = 1;
    public static final int T_SCHEDULER_COMPLETELYFAIR = 2;

    // Multipliers for generating random values for various VM parameters
    int VM_STORAGE_MULT = 1000;
    int VM_MIPS_MULT = 100;
    int VM_RAM_MULT = 512;
    int VM_BW_MULT = 100;
    int VM_PES_MULT = 1;

    /**
     * Constructor that takes the VM configuration list and a random seed.
     * 
     * @param vmConfigs List of VM configuration settings
     * @param seed Random seed for generating random numbers
     */
    public VMGenerator(ArrayList<DatacenterConfig.VmConfig> vmConfigs, int seed) {
        this.vmConfigs = vmConfigs;
        this.seed = seed;
    }

    /**
     * Constructor without seed, only taking the VM configuration list.
     * 
     * @param vmConfigs List of VM configuration settings
     */
    public VMGenerator(ArrayList<DatacenterConfig.VmConfig> vmConfigs) {
        this.vmConfigs = vmConfigs;
    }

    /**
     * Creates a VM with the specified parameters.
     * 
     * @param mips MIPS rating for the VM's PEs
     * @param ram Amount of RAM for the VM
     * @param pesNumber Number of PEs (cores) assigned to the VM
     * @param bw Bandwidth allocated to the VM
     * @param storage Storage capacity for the VM
     * @param taskScheduler The type of task scheduler to use (TimeShared, SpaceShared, or
     *        CompletelyFair)
     * @return A new Vm object with the specified configuration
     */
    private Vm createVM(int mips, int ram, int pesNumber, int bw, int storage, int taskScheduler) {
        // Create a simple VM with the specified MIPS and PEs
        Vm vm = new VmSimple(mips, pesNumber);
        vm.setRam(ram).setBw(bw).setSize(storage); // Set RAM, bandwidth, and storage for the VM

        // Set the appropriate task scheduler for the VM
        if (taskScheduler == T_SCHEDULER_TIMESHARED)
            vm.setCloudletScheduler(new CloudletSchedulerTimeShared());
        if (taskScheduler == T_SCHEDULER_SPACESHARED)
            vm.setCloudletScheduler(new CloudletSchedulerSpaceShared());
        if (taskScheduler == T_SCHEDULER_COMPLETELYFAIR)
            vm.setCloudletScheduler(new CloudletSchedulerCompletelyFair());
        return vm;
    }

    /**
     * Generates a list of VMs based on the provided configuration settings, using random values for
     * certain parameters based on predefined multipliers.
     * 
     * @return A list of generated VMs
     */
    @Override
    public ArrayList<Vm> generate() {
        int vms, mips, ram, pesNumber, bw, storage;
        ArrayList<Vm> vmList = new ArrayList<>();
        // Loop through each VM configuration in the list
        for (DatacenterConfig.VmConfig vmConfig : vmConfigs) {
            // Generate a random (or not) number of VMs based on the configuration
            vms = RandomUtils.randomIntMultiple(vmConfig.size, 1);

            // If the configuration is for heterogeneous VMs, generate random (or not) values for
            // each parameter
            if (vmConfig.heterogeneous) {
                mips = RandomUtils.randomIntMultiple(vmConfig.mips, VM_MIPS_MULT);
                ram = RandomUtils.randomIntMultiple(vmConfig.ram, VM_RAM_MULT);
                pesNumber = RandomUtils.randomIntMultiple(vmConfig.pesNumber, VM_PES_MULT);
                bw = RandomUtils.randomIntMultiple(vmConfig.bw, VM_BW_MULT);
                storage = RandomUtils.randomIntMultiple(vmConfig.storage, VM_STORAGE_MULT);

                // Add the generated VMs to the list
                for (int i = 0; i < vms; i++) {
                    vmList.add(createVM(mips, ram, pesNumber, bw, storage, vmConfig.taskScheduler));
                }
            } else {
                // For homogeneous VMs, generate fixed random (or not) values for each VM
                for (int i = 0; i < vms; i++) {
                    mips = RandomUtils.randomIntMultiple(vmConfig.mips, VM_MIPS_MULT);
                    ram = RandomUtils.randomIntMultiple(vmConfig.ram, VM_RAM_MULT);
                    pesNumber = RandomUtils.randomIntMultiple(vmConfig.pesNumber, VM_PES_MULT);
                    bw = RandomUtils.randomIntMultiple(vmConfig.bw, VM_BW_MULT);
                    storage = RandomUtils.randomIntMultiple(vmConfig.storage, VM_STORAGE_MULT);

                    // Add the created VM to the list
                    vmList.add(createVM(mips, ram, pesNumber, bw, storage, vmConfig.taskScheduler));
                }
            }
        }
        return vmList; // Return the generated VM list
    }
}
