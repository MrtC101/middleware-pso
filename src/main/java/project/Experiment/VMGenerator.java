package project.Experiment;


import java.util.ArrayList;

import org.cloudsimplus.schedulers.cloudlet.CloudletSchedulerCompletelyFair;
import org.cloudsimplus.schedulers.cloudlet.CloudletSchedulerSpaceShared;
import org.cloudsimplus.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudsimplus.vms.Vm;
import org.cloudsimplus.vms.VmSimple;

import project.Utils.RandomUtils;

public class VMGenerator implements  Generator<Vm>{

    private ArrayList<DatacenterConfig.VmConfig> vmConfigs;
    private int seed;

    public static final int T_SCHEDULER_TIMESHARED = 0;
    public static final int T_SCHEDULER_SPACESHARED = 1;
    public static final int T_SCHEDULER_COMPLETELYFAIR = 1;

    int VM_STORAGE_MULT = 1000;
    int VM_MIPS_MULT = 100;
    int VM_RAM_MULT = 512;
    int VM_BW_MULT = 100;
    int VM_PES_MULT = 1;

    public VMGenerator(ArrayList<DatacenterConfig.VmConfig> vmConfigs, int seed) {
        this.vmConfigs = vmConfigs;
        this.seed = seed; 
    }

    public VMGenerator(ArrayList<DatacenterConfig.VmConfig> vmConfigs) {
        this.vmConfigs = vmConfigs;
    }

    private Vm createVM(int mips, int ram, int pesNumber, int bw, int storage, int taskScheduler){
        Vm vm;
        vm = new VmSimple(mips, pesNumber);
        vm.setRam(ram).setBw(bw).setSize(storage);
        if (taskScheduler == T_SCHEDULER_TIMESHARED) 
            vm.setCloudletScheduler(new CloudletSchedulerTimeShared());
        if (taskScheduler == T_SCHEDULER_SPACESHARED) 
            vm.setCloudletScheduler(new CloudletSchedulerSpaceShared());
        if (taskScheduler == T_SCHEDULER_COMPLETELYFAIR)
            vm.setCloudletScheduler(new CloudletSchedulerCompletelyFair());
        return vm;
    }
    
    @Override
    public ArrayList<Vm> generate(){
        int vms, mips, ram, pesNumber, bw, storage;
        ArrayList<Vm> vmList = new ArrayList<>();
        for (DatacenterConfig.VmConfig vmConfig : vmConfigs) {
            vms = RandomUtils.randomIntMultiple(vmConfig.size, 1);
            if (vmConfig.heterogeneous){
                mips = RandomUtils.randomIntMultiple(vmConfig.mips, VM_MIPS_MULT);
                ram = RandomUtils.randomIntMultiple(vmConfig.ram, VM_RAM_MULT);
                pesNumber = RandomUtils.randomIntMultiple(vmConfig.pesNumber, VM_PES_MULT);
                bw = RandomUtils.randomIntMultiple(vmConfig.bw, VM_BW_MULT);
                storage = RandomUtils.randomIntMultiple(vmConfig.storage, VM_STORAGE_MULT);
                for (int i = 0; i < vms; i++) {
                    vmList.add(createVM(mips, ram, pesNumber, bw, storage, vmConfig.taskScheduler));
                }
            } else {
                for (int i = 0; i < vms; i++) {
                    mips = RandomUtils.randomIntMultiple(vmConfig.mips, VM_MIPS_MULT);
                    ram = RandomUtils.randomIntMultiple(vmConfig.ram, VM_RAM_MULT);
                    pesNumber = RandomUtils.randomIntMultiple(vmConfig.pesNumber, VM_PES_MULT);
                    bw = RandomUtils.randomIntMultiple(vmConfig.bw, VM_BW_MULT);
                    storage = RandomUtils.randomIntMultiple(vmConfig.storage, VM_STORAGE_MULT);
                    vmList.add(createVM(mips, ram, pesNumber, bw, storage, vmConfig.taskScheduler));
                }
            }
        }       
        return vmList;    
    }
}
