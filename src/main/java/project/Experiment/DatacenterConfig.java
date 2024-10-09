package project.Experiment;

import java.util.ArrayList;

public class DatacenterConfig {
    
    public double costPerSec;
    public double costPerMem;
    public double costPerStorage;
    public double costPerBw;
    public ArrayList<HostConfig> hosts;
    public ArrayList<VmConfig> vms;
    public ArrayList<TasksConfig> tasks;

    public static class HostConfig {
        public int id;
        public ArrayList<Integer> ram;
        public ArrayList<Integer> storage;
        public ArrayList<Integer> bw;
        public ArrayList<Integer> pesNumber;
        public ArrayList<Integer> mips;
        public String vmScheduler;
    }

    public static class VmConfig {
        public int id;
        public ArrayList<Integer> ram;
        public ArrayList<Integer> storage;
        public ArrayList<Integer> bw;
        public ArrayList<Integer> pesNumber;
        public ArrayList<Integer> mips;
        public String taskScheduler;        
    }

    public static class TasksConfig {
        public int size;
        public ArrayList<Integer> fileSize;
        public ArrayList<Integer> outputSize;
        public ArrayList<Integer> pesNumber;
        public ArrayList<Integer> length;
        public String taskScheduler;

    }
    
}
