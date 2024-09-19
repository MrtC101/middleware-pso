package project.Experiment;

import java.util.ArrayList;
import java.util.Random;

import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.hosts.HostSimple;
import org.cloudsimplus.resources.Pe;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.schedulers.vm.VmSchedulerTimeShared;
import org.cloudsimplus.schedulers.vm.VmSchedulerSpaceShared;


public class HostGenerator extends GeneratorAbstract<Host> {

    private int minHostMips;
    private int maxHostMips;

    private int minHostRam;
    private int maxHostRam;

    private int minHostBw;
    private int maxHostBw;

    private int minHostStorage;
    private int maxHostStorage;

    private int minPesNumber;
    private int maxPesNumber;

    private int vmScheduler;

    private boolean heterogeneous;

    private Random random;

    public static final int VM_SCHEDULER_TIMESHARED = 0;
    public static final int VM_SCHEDULER_SPACESHARED = 1;

    int H_STORAGE_MULT = 1000;
    int H_MIPS_MULT = 100;
    int H_RAM_MULT = 512;
    int H_BW_MULT = 100;
    int H_PES_MULT = 1;

    public HostGenerator(int minHosts, int maxHosts, int minHostMips, int maxHostMips, int minHostRam, int maxHostRam,
            int minHostBw, int maxHostBw, int minHostStorage, int maxHostStorage, int minPesNumber, int maxPesNumber,
            boolean heterogeneous, int seed, int vmScheduler) {
        super(minHosts, maxHosts);
        this.minHostMips = minHostMips;
        this.maxHostMips = maxHostMips;
        this.minHostRam = minHostRam;
        this.maxHostRam = maxHostRam;
        this.minHostBw = minHostBw;
        this.maxHostBw = maxHostBw;
        this.minHostStorage = minHostStorage;
        this.maxHostStorage = maxHostStorage;
        this.minPesNumber = minPesNumber;
        this.maxPesNumber = maxPesNumber;
        this.heterogeneous = heterogeneous;
        this.vmScheduler = vmScheduler; // Fijo
        this.random = new Random(seed);
        
    }

    public HostGenerator(int minHosts, int maxHosts, int minHostMips, int maxHostMips, int minHostRam, int maxHostRam,
            int minHostBw, int maxHostBw, int minHostStorage, int maxHostStorage, int minPesNumber, int maxPesNumber,
            boolean heterogeneous, int vmScheduler) {
        super(minHosts, maxHosts);
        this.minHostMips = minHostMips;
        this.maxHostMips = maxHostMips;
        this.minHostRam = minHostRam;
        this.maxHostRam = maxHostRam;
        this.minHostBw = minHostBw;
        this.maxHostBw = maxHostBw;
        this.minHostStorage = minHostStorage;
        this.maxHostStorage = maxHostStorage;
        this.minPesNumber = minPesNumber;
        this.maxPesNumber = maxPesNumber;
        // !Distinguir heterogeneidad entre hosts y cpus
        this.heterogeneous = heterogeneous;
        this.vmScheduler = vmScheduler; // Fijo
        this.random = new Random();

    }

    private Host createHost(int mips, int ram, ArrayList<Pe> peList, int bw, int storage) {
        Host host = new HostSimple(ram, bw, storage, peList);
        if (vmScheduler == VM_SCHEDULER_TIMESHARED) host.setVmScheduler(new VmSchedulerTimeShared());
        if (vmScheduler == VM_SCHEDULER_SPACESHARED) host.setVmScheduler(new VmSchedulerSpaceShared());
        return host;
    }

    public ArrayList<Host> generate() {
        int hosts = this.random.nextInt((this.max - this.min) + 1) + this.min;
        int mips;
        int ram;
        int pesNumber;
        int bw;
        int storage;
        ArrayList<Pe> peList;
        ArrayList<Host> hostList = new ArrayList<>(hosts);
        if (this.heterogeneous) {
            mips = Utils.randomIntMultiple(this.minHostMips, this.maxHostMips, H_MIPS_MULT);
            ram = Utils.randomIntMultiple(this.minHostRam, this.maxHostRam, H_RAM_MULT);
            pesNumber = this.random.nextInt((this.maxPesNumber - this.minPesNumber) + 1) + this.minPesNumber;
            peList = new ArrayList<Pe>(pesNumber);
            for (int i = 0; i < pesNumber; i++) {
                peList.add(new PeSimple(mips));
            }
            bw = Utils.randomIntMultiple(this.minHostBw, this.maxHostBw, H_BW_MULT);
            storage = Utils.randomIntMultiple(this.minHostStorage, this.maxHostStorage, H_STORAGE_MULT);
            for (int i = 0; i < hosts; i++) {
                hostList.add(createHost(mips, ram, peList, bw, storage));
            }
        } else {
            for (int i = 0; i < hosts; i++) {
                mips = Utils.randomIntMultiple(this.minHostMips, this.maxHostMips, H_MIPS_MULT);
                ram = Utils.randomIntMultiple(this.minHostRam, this.maxHostRam, H_RAM_MULT);
                pesNumber = this.random.nextInt((this.maxPesNumber - this.minPesNumber) + 1) + this.minPesNumber;
                peList = new ArrayList<Pe>(pesNumber);
                for (int j = 0; j < pesNumber; j++) {
                    peList.add(new PeSimple(mips));
                }
                bw = Utils.randomIntMultiple(this.minHostBw, this.maxHostBw, H_BW_MULT);
                storage = Utils.randomIntMultiple(this.minHostStorage, this.maxHostStorage, H_STORAGE_MULT);
                hostList.add(createHost(mips, ram, peList, bw, storage));
            }
        }
        return hostList;
    }
}
