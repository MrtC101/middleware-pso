package project.Experiment;

import java.util.ArrayList;

import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.hosts.HostSimple;
import org.cloudsimplus.resources.Pe;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.schedulers.vm.VmSchedulerSpaceShared;
import org.cloudsimplus.schedulers.vm.VmSchedulerTimeShared;

import project.Utils.RandomUtils;
import static project.Utils.RandomUtils.randomIntMultiple;

public class HostGenerator implements Generator<Host> {
    private ArrayList<DatacenterConfig.HostConfig> hostsConfig;
    private int seed;

    public static final int VM_SCHEDULER_TIMESHARED = 0;
    public static final int VM_SCHEDULER_SPACESHARED = 1;

    int H_STORAGE_MULT = 1000;
    int H_MIPS_MULT = 100;
    int H_RAM_MULT = 512;
    int H_BW_MULT = 100;
    int H_PES_MULT = 1;

    public HostGenerator(ArrayList<DatacenterConfig.HostConfig>  hostsConfig, int seed) {
        this.hostsConfig = hostsConfig;
        this.seed = seed;
    }

    public HostGenerator(ArrayList<DatacenterConfig.HostConfig> hostsConfig) {
        this.hostsConfig = hostsConfig;
    }

    private Host createHost(int mips, int ram, ArrayList<Pe> peList, int bw, int storage, int vmScheduler) {
        Host host = new HostSimple(ram, bw, storage, peList);
        if (vmScheduler == VM_SCHEDULER_TIMESHARED) host.setVmScheduler(new VmSchedulerTimeShared());
        if (vmScheduler == VM_SCHEDULER_SPACESHARED) host.setVmScheduler(new VmSchedulerSpaceShared());
        return host;
    }

    @Override
    public ArrayList<Host> generate() {
        int mips, ram, pesNumber, bw, storage;
        ArrayList<Host> hostList = new ArrayList<>();

        for (DatacenterConfig.HostConfig hostConfig : hostsConfig) {
            int hosts = randomIntMultiple(hostConfig.size, 1);
            ArrayList<Pe> peList;
            if (hostConfig.heterogeneous) {
                mips = RandomUtils.randomIntMultiple(hostConfig.mips, H_MIPS_MULT);
                ram = RandomUtils.randomIntMultiple(hostConfig.ram, H_RAM_MULT);
                pesNumber = RandomUtils.randomIntMultiple(hostConfig.pesNumber, H_PES_MULT);
                peList = new ArrayList<>(pesNumber);
                for (int i = 0; i < pesNumber; i++) {
                    peList.add(new PeSimple(mips));
                }
                bw = RandomUtils.randomIntMultiple(hostConfig.bw, H_BW_MULT);
                storage = RandomUtils.randomIntMultiple(hostConfig.storage, H_STORAGE_MULT);
                for (int i = 0; i < hosts; i++) {
                    hostList.add(createHost(mips, ram, peList, bw, storage, hostConfig.vmScheduler));
                }
            } else {
                for (int i = 0; i < hosts; i++) {
                    mips = RandomUtils.randomIntMultiple(hostConfig.mips, H_MIPS_MULT);
                    ram = RandomUtils.randomIntMultiple(hostConfig.ram, H_RAM_MULT);
                    pesNumber = RandomUtils.randomIntMultiple(hostConfig.pesNumber, H_PES_MULT);
                    peList = new ArrayList<>(pesNumber);
                    for (int j = 0; j < pesNumber; j++) {
                        peList.add(new PeSimple(mips));
                    }
                    bw = RandomUtils.randomIntMultiple(hostConfig.bw, H_BW_MULT);
                    storage = RandomUtils.randomIntMultiple(hostConfig.storage, H_STORAGE_MULT);
                    hostList.add(createHost(mips, ram, peList, bw, storage, hostConfig.vmScheduler));
                }
            }
        }
        return hostList;
    }
}
