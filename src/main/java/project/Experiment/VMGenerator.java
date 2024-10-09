package project.Experiment;


import java.util.ArrayList;
import java.util.Random;

import org.cloudsimplus.schedulers.cloudlet.CloudletSchedulerCompletelyFair;
import org.cloudsimplus.schedulers.cloudlet.CloudletSchedulerSpaceShared;
import org.cloudsimplus.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudsimplus.vms.Vm;
import org.cloudsimplus.vms.VmSimple;

import project.Utils.RandomUtils;

public class VMGenerator extends GeneratorAbstract<Vm>{

    private int minVMRam;
    private int maxVMRam;

    private int minVMBw;
    private int maxVMBw;

    private int minVMMips;
    private int maxVMMips;

    private int minVMPes;
    private int maxVMPes;

    private int minVMStorage;
    private int maxVMStorage;
    
    private boolean heterogeneous;

    private int taskScheduler;

    public static final int T_SCHEDULER_TIMESHARED = 0;
    public static final int T_SCHEDULER_SPACESHARED = 1;
    public static final int T_SCHEDULER_COMPLETELYFAIR = 1;

    private Random random;

    int VM_STORAGE_MULT = 1000;
    int VM_MIPS_MULT = 100;
    int VM_RAM_MULT = 512;
    int VM_BW_MULT = 100;
    int VM_PES_MULT = 1;

    public VMGenerator(int minVMs, int maxVMs, int minVMRam, int maxVMRam, int minVMBw, int maxVMBw, int minVMStorage, int maxVMStorage, int minVMMips, int maxVMMips, int minVMPes, int maxVMPes, boolean heterogeneous, int taskScheduler, int seed) {
        super(minVMs, maxVMs);
        this.minVMRam = minVMRam;
        this.maxVMRam = maxVMRam;
        this.minVMMips = minVMMips;
        this.maxVMMips = maxVMMips;
        this.minVMBw = minVMBw;
        this.maxVMBw = maxVMBw;
        this.minVMStorage = minVMStorage;
        this.maxVMStorage = maxVMStorage;
        this.minVMPes = minVMPes;
        this.maxVMPes = maxVMPes;
        this.heterogeneous = heterogeneous;
        this.taskScheduler = taskScheduler;
        this.random = new Random(seed);
        
    }

    public VMGenerator(int minVMs, int maxVMs, int minVMRam, int maxVMRam, int minVMBw, int maxVMBw, int minVMStorage,
            int maxVMStorage, int minVMMips, int maxVMMips, int minVMPes, int maxVMPes, boolean heterogeneous, int taskScheduler) {
        super(minVMs, maxVMs);
        this.minVMRam = minVMRam;
        this.maxVMRam = maxVMRam;
        this.minVMBw = minVMBw;
        this.maxVMBw = maxVMBw;
        this.minVMMips = minVMMips;
        this.maxVMMips = maxVMMips;
        this.minVMStorage = minVMStorage;
        this.maxVMStorage = maxVMStorage;
        this.minVMPes = minVMPes;
        this.maxVMPes = maxVMPes;
        this.heterogeneous = heterogeneous;
        this.taskScheduler = taskScheduler;
        this.random = new Random();

    }

    private Vm createVM(int mips, int ram, int pesNumber, int bw, int storage){
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

    
    public ArrayList<Vm> generate(){
        int vms = this.random.nextInt((this.max - this.min) + 1) + this.min;
        int mips;
        int ram;
        int pesNumber;
        int bw;
        int storage;
        ArrayList<Vm> vmList = new ArrayList<Vm>(vms);
        if (this.heterogeneous){
            mips = RandomUtils.randomIntMultiple(this.minVMMips, this.maxVMMips, VM_MIPS_MULT);
            ram = RandomUtils.randomIntMultiple(this.minVMRam, this.maxVMRam, VM_RAM_MULT);
            pesNumber = this.random.nextInt((this.maxVMPes - this.minVMPes) + 1);
            bw = RandomUtils.randomIntMultiple(this.minVMBw, this.maxVMBw, VM_BW_MULT);
            storage = RandomUtils.randomIntMultiple(this.minVMStorage, this.maxVMStorage, VM_STORAGE_MULT);
            for (int i = 0; i < vms; i++) {
                vmList.add(createVM(mips, ram, pesNumber, bw, storage));
            }
        } else {
            for (int i = 0; i < vms; i++) {
                mips = RandomUtils.randomIntMultiple(this.minVMMips, this.maxVMMips, VM_MIPS_MULT);
                ram = RandomUtils.randomIntMultiple(this.minVMRam, this.maxVMRam, VM_RAM_MULT);
                pesNumber = this.random.nextInt((this.maxVMPes - this.minVMPes) + 1);
                bw = RandomUtils.randomIntMultiple(this.minVMBw, this.maxVMBw, VM_BW_MULT);
                storage = RandomUtils.randomIntMultiple(this.minVMStorage, this.maxVMStorage, VM_STORAGE_MULT);
                vmList.add(createVM(mips, ram, pesNumber, bw, storage));
            }
        }
        return vmList;    
    }

}
