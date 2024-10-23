package project.Examples;

import java.util.ArrayList;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.datacenters.DatacenterSimple;
import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.hosts.HostSimple;
import org.cloudsimplus.resources.Pe;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.schedulers.cloudlet.CloudletSchedulerSpaceShared;
import org.cloudsimplus.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudsimplus.schedulers.vm.VmSchedulerSpaceShared;
import org.cloudsimplus.schedulers.vm.VmSchedulerTimeShared;
import org.cloudsimplus.vms.Vm;
import org.cloudsimplus.vms.VmSimple;
import project.Experiment.HostGenerator;
import project.Experiment.VMGenerator;
import project.PSO.DatacenterBrokerPSO;

/**
 * This class implements all methods required to run a CloudSim-plus simulation with the
 * DatacenterBrokerPSO.
 */
public class RunPSOExample {

        public static void main(String[] args) {
                final int Tasks, FileSize, OutputSize, TaskPesNumber, Length;
                final int Hosts, HostMips, HostRam, HostBw, HostStorage, HostPesNumber;
                final int VMs, VMMips, VMRam, VMBw, VMStorage, VMPesNumber;

                // Task atributes
                Tasks = 10;
                FileSize = 1000;
                OutputSize = 1000;
                TaskPesNumber = 1;
                Length = 10_000;

                // Host atributes
                Hosts = 100;
                HostMips = 10_000;
                HostRam = 10_000;
                HostBw = 10_000;
                HostStorage = 1_000_000;
                HostPesNumber = 1000;
                int vmScheduler = HostGenerator.VM_SCHEDULER_TIMESHARED;

                // VM atributes
                VMs = 10;
                VMMips = 3000;
                VMRam = 1024 * 2;
                VMBw = 1000;
                VMStorage = 1000;
                VMPesNumber = 7;
                int taskScheduler = VMGenerator.T_SCHEDULER_TIMESHARED;

                ArrayList<Cloudlet> cloudlets = RunPSOExample.createCloudlets(Tasks, FileSize,
                                OutputSize, TaskPesNumber, Length);
                ArrayList<Host> hosts = RunPSOExample.createHosts(Hosts, HostMips, HostRam, HostBw,
                                HostStorage, HostPesNumber, vmScheduler);
                ArrayList<Vm> vms = RunPSOExample.createVMs(VMs, VMRam, VMBw, VMStorage, VMMips,
                                VMPesNumber, taskScheduler);

                RunPSOExample.runSearch(cloudlets, hosts, vms);
        }


        /**
         * Method used to create CloudSim cloudlets.
         * 
         * @param tasks
         * @param fileSize
         * @param outputSize
         * @param pesNumber
         * @param length
         * @return
         */
        private static ArrayList<Cloudlet> createCloudlets(int tasks, int fileSize, int outputSize,
                        int pesNumber, int length) {
                ArrayList<Cloudlet> taskList = new ArrayList<Cloudlet>(tasks);
                for (int i = 0; i < tasks; i++) {
                        CloudletSimple cloudlet = new CloudletSimple(length, pesNumber);
                        cloudlet.setFileSize(fileSize);
                        cloudlet.setOutputSize(outputSize);
                        taskList.add(cloudlet);
                }
                return taskList;
        }

        private static final int T_SCHEDULER_TIMESHARED = 0;
        private static final int T_SCHEDULER_SPACESHARED = 1;

        /**
         * Method used to create CloudSim VM's
         * 
         * @param vms
         * @param ram
         * @param bw
         * @param storage
         * @param mips
         * @param pesNumber
         * @param taskScheduler
         * @return
         */
        private static ArrayList<Vm> createVMs(int vms, int ram, int bw, int storage, int mips,
                        int pesNumber, int taskScheduler) {
                ArrayList<Vm> vmList = new ArrayList<Vm>(vms);
                for (int i = 0; i < vms; i++) {
                        Vm vm;
                        vm = new VmSimple(mips, pesNumber);
                        vm.setRam(ram).setBw(bw).setSize(storage);
                        if (taskScheduler == T_SCHEDULER_TIMESHARED)
                                vm.setCloudletScheduler(new CloudletSchedulerTimeShared());
                        if (taskScheduler == T_SCHEDULER_SPACESHARED)
                                vm.setCloudletScheduler(new CloudletSchedulerSpaceShared());
                        vmList.add(vm);
                }
                return vmList;
        }

        private static final int VM_SCHEDULER_TIMESHARED = 0;
        private static final int VM_SCHEDULER_SPACESHARED = 1;

        /**
         * Method used to create CloudSim Hosts
         * 
         * @param hosts
         * @param mips
         * @param ram
         * @param bw
         * @param storage
         * @param pesNumber
         * @param VmScheduler
         * @return
         */
        private static ArrayList<Host> createHosts(int hosts, int mips, int ram, int bw,
                        int storage, int pesNumber, int VmScheduler) {
                ArrayList<Pe> peList;
                ArrayList<Host> hostList = new ArrayList<>(hosts);
                for (int i = 0; i < hosts; i++) {
                        peList = new ArrayList<Pe>(pesNumber);
                        for (int j = 0; j < pesNumber; j++) {
                                peList.add(new PeSimple(mips));
                        }
                        Host host = new HostSimple(ram, bw, storage, peList);
                        if (VmScheduler == VM_SCHEDULER_TIMESHARED)
                                host.setVmScheduler(new VmSchedulerTimeShared());
                        if (VmScheduler == VM_SCHEDULER_SPACESHARED)
                                host.setVmScheduler(new VmSchedulerSpaceShared());
                        hostList.add(host);
                }
                return hostList;
        }

        /**
         * The method to start the simulation.
         * 
         * @param cloudlets
         * @param hosts
         * @param vms
         */
        private static void runSearch(ArrayList<Cloudlet> cloudlets, ArrayList<Host> hosts,
                        ArrayList<Vm> vms) {
                CloudSimPlus simulation = new CloudSimPlus();
                final DatacenterBrokerPSO broker = new DatacenterBrokerPSO(simulation);
                new DatacenterSimple(simulation, hosts);
                broker.submitVmList(vms);
                broker.submitCloudletList(cloudlets);
                // Parameters set based on
                // https://ewh.ieee.org/conf/wcci/2016/document/tutorials/cec4.pdf
                broker.runPSO(100, 1000, 0.9, 2.0, 2.0);
        }
}
