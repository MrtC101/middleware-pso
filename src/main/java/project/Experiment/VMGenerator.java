package project.Experiment;

import java.util.ArrayList;
import java.util.Random;


import org.cloudsimplus.schedulers.cloudlet.CloudletSchedulerSpaceShared;
import org.cloudsimplus.schedulers.vm.VmSchedulerTimeShared;
import org.cloudsimplus.vms.Vm;
import org.cloudsimplus.vms.VmSimple;

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

    private Random random;

    

    public VMGenerator(int minVMs, int maxVMs, int minVMRam, int maxVMRam, int minVMBw, int maxVMBw, int minVMStorage,
            int maxVMStorage, int minVMMips, int maxVMMips, int minVMPes, int maxVMPes, boolean heterogeneous, int seed) {
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
        this.random = new Random(seed);
        
    }

    public VMGenerator(int minVMs, int maxVMs, int minVMRam, int maxVMRam, int minVMBw, int maxVMBw, int minVMStorage,
            int maxVMStorage, int minVMMips, int maxVMMips, int minVMPes, int maxVMPes, boolean heterogeneous) {
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
        this.random = new Random();

    }

    private Vm createVM(int mips, int ram, int pesNumber, int bw, int storage){
        Vm vm;
        vm = new VmSimple(mips, pesNumber);
        vm.setRam(ram).setBw(bw).setSize(storage);
        
        vm.setCloudletScheduler(new CloudletSchedulerSpaceShared()); // Parametrizar
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
            mips = this.random.nextInt((this.maxVMMips - this.minVMMips) + 1) + this.minVMMips;
            ram = this.random.nextInt((this.maxVMRam - this.minVMRam) + 1) + this.minVMRam;
            pesNumber = this.random.nextInt((this.maxVMPes - this.minVMPes) + 1) + this.minVMPes;
            bw = this.random.nextInt((this.maxVMBw - this.minVMBw) + 1) + this.minVMBw;
            storage = this.random.nextInt((this.maxVMStorage - this.minVMStorage) + 1) + this.minVMStorage;
            for (int i = 0; i < vms; i++) {
                vmList.add(createVM(mips, ram, pesNumber, bw, storage));
            }
        } else {
            for (int i = 0; i < vms; i++) {
                mips = this.random.nextInt((this.maxVMMips - this.minVMMips) + 1) + this.minVMMips;
                ram = this.random.nextInt((this.maxVMRam - this.minVMRam) + 1) + this.minVMRam;
                pesNumber = this.random.nextInt((this.maxVMPes - this.minVMPes) + 1) + this.minVMPes;
                bw = this.random.nextInt((this.maxVMBw - this.minVMBw) + 1) + this.minVMBw;
                storage = this.random.nextInt((this.maxVMStorage - this.minVMStorage) + 1) + this.minVMStorage;
                vmList.add(createVM(mips, ram, pesNumber, bw, storage));
            }
        }
        return vmList;    
    }

}
