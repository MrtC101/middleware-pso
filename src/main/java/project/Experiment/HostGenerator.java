package project.Experiment;

import java.util.ArrayList;
import java.util.Random;

import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.hosts.HostSimple;
import org.cloudsimplus.resources.Pe;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.schedulers.vm.VmSchedulerTimeShared;

public class HostGenerator {
    private int minHosts;
    private int maxHosts;

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

    private boolean heterogeneous;

    private Random random;

    public HostGenerator(int minHosts, int maxHosts, int minHostMips, int maxHostMips, int minHostRam, int maxHostRam,
            int minHostBw, int maxHostBw, int minHostStorage, int maxHostStorage, int minPesNumber, int maxPesNumber,
            boolean heterogeneous, int seed) {
        this.minHosts = minHosts;
        this.maxHosts = maxHosts;
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
        this.random = new Random(seed);
    }

    public HostGenerator(int minHosts, int maxHosts, int minHostMips, int maxHostMips, int minHostRam, int maxHostRam,
            int minHostBw, int maxHostBw, int minHostStorage, int maxHostStorage, int minPesNumber, int maxPesNumber,
            boolean heterogeneous) {
        this.minHosts = minHosts;
        this.maxHosts = maxHosts;
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
        this.random = new Random();
    }

    private Host createHost(int mips, int ram, ArrayList<Pe> peList, int bw, int storage) {
    
        Host host = new HostSimple(ram, bw, storage, peList);
        host.setVmScheduler(new VmSchedulerTimeShared()); // !Parametrizar
        return host;
    }

    public ArrayList<Host> generate() {
        int hosts = this.random.nextInt((this.maxHosts - this.minHosts) + 1) + this.minHosts;
        int mips;
        int ram;
        int pesNumber;
        int bw;
        int storage;
        ArrayList<Pe> peList;
        ArrayList<Host> hostList = new ArrayList<>(hosts);
        if (this.heterogeneous) {
            mips = this.random.nextInt((this.maxHostMips - this.minHostMips) + 1) + this.minHostMips;
            ram = this.random.nextInt((this.maxHostRam - this.minHostRam) + 1) + this.minHostRam;
            pesNumber = this.random.nextInt((this.maxPesNumber - this.minPesNumber) + 1) + this.minPesNumber;
            peList = new ArrayList<Pe>(pesNumber);
            for (int i = 0; i < pesNumber; i++) {
                peList.add(new PeSimple(mips));
            }
            bw = this.random.nextInt((this.maxHostBw - this.minHostBw) + 1) + this.minHostBw;
            storage = this.random.nextInt((this.maxHostStorage - this.minHostStorage) + 1) + this.minHostStorage;
            for (int i = 0; i < hosts; i++) {
                hostList.add(createHost(mips, ram, peList, bw, storage));
            }
        } else {
            for (int i = 0; i < hosts; i++) {
                mips = this.random.nextInt((this.maxHostMips - this.minHostMips) + 1) + this.minHostMips;
                ram = this.random.nextInt((this.maxHostRam - this.minHostRam) + 1) + this.minHostRam;
                pesNumber = this.random.nextInt((this.maxPesNumber - this.minPesNumber) + 1) + this.minPesNumber;
                peList = new ArrayList<Pe>(pesNumber);
                for (int j = 0; i < pesNumber; j++) {
                    peList.add(new PeSimple(mips));
                }
                bw = this.random.nextInt((this.maxHostBw - this.minHostBw) + 1) + this.minHostBw;
                storage = this.random.nextInt((this.maxHostStorage - this.minHostStorage) + 1) + this.minHostStorage;
                hostList.add(createHost(mips, ram, peList, bw, storage));
            }
        }
        return hostList;
    }
}
