package project.Experiment;

public class Test {

    
    public static void main(String[] args) {
        int minTasks = 6;
        int maxTasks = 8;
        int minFileSize = 1000;
        int maxFileSize = minFileSize * 10;
        int minOutputSize = 1000;
        int maxOutputSize = minOutputSize * 10;
        int minTaskPesNumber = 1;
        int maxTaskPesNumber = 2;
        int minLength = 10000;
        int maxLength = 50000;
        boolean heterogeneous = true;
        TaskGenerator taskGen = new TaskGenerator(minTasks, maxTasks, minFileSize, maxFileSize, minOutputSize, maxOutputSize, minTaskPesNumber, maxTaskPesNumber, minLength, maxLength, heterogeneous);

        int minHosts = 6;
        int maxHosts = 8;
        int minHostMips = 5000;
        int maxHostMips = 10000;
        int minHostRam = 4098;
        int maxHostRam = minHostRam * 8;
        int minHostBw = 5000;
        int maxHostBw = 15000;
        int minHostStorage = 1_000_000;
        int maxHostStorage = minHostStorage*5;
        int minHostPesNumber = 4;
        int maxHostPesNumber = 16;
        int vmScheduler = HostGenerator.VM_SCHEDULER_TIMESHARED;
        HostGenerator hostGen = new HostGenerator(minHosts, maxHosts, minHostMips, maxHostMips, minHostRam, maxHostRam, minHostBw, maxHostBw, minHostStorage, maxHostStorage, minHostPesNumber, maxHostPesNumber, heterogeneous, vmScheduler);

        int minVMs = 6;
        int maxVMs = 20;
        int minVMMips = 1000;
        int maxVMMips = 5000;
        int minVMRam = 2048;
        int maxVMRam = minVMRam * 4;
        int minVMBw = 200;
        int maxVMBw = 2000;
        int minVMStorage = 10_000;
        int maxVMStorage = minVMStorage*10;
        int minVMPesNumber = 2;
        int maxVMPesNumber = 8;
        int taskScheduler = VMGenerator.T_SCHEDULER_TIMESHARED;
        VMGenerator vmGen = new VMGenerator(minVMs, maxVMs, minVMRam, maxVMRam, minVMBw, maxVMBw, minVMStorage, maxVMStorage, minVMMips, maxVMMips, minVMPesNumber, maxVMPesNumber, heterogeneous, taskScheduler);

        SimGenerator simGen = new SimGenerator(taskGen, vmGen, hostGen);
        simGen.generateSimulation();
    }
}
