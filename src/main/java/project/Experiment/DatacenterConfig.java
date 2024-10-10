package project.Experiment;

import java.util.ArrayList;

public class DatacenterConfig {
    public double costPerSec;
    public double costPerMem;
    public double costPerStorage;
    public double costPerBw;
    public ArrayList<HostConfig> hosts;
    public ArrayList<VmConfig> vms;
    public TasksConfig tasks;

    public static class HostConfig extends MachineConfigAbstract {
        // TIMESHARED = 0; SPACESHARED = 1;
        public int vmScheduler;
    }

    public static class VmConfig extends MachineConfigAbstract {
        // TIMESHARED = 0; SPACESHARED = 1; COMPLETELYFAIR = 2
        public int taskScheduler;        
    }

    public static class TasksConfig extends ConfigAbstract {
        public ArrayList<Integer> fileSize;
        public ArrayList<Integer> outputSize;
        public ArrayList<Integer> pesNumber;
        public ArrayList<Integer> length;
    }
    
}
