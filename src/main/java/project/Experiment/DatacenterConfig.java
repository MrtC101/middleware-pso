package project.Experiment;

import java.util.ArrayList;

/**
 * DatacenterConfig class holds the configuration of a datacenter, including cost settings and lists
 * of hosts, VMs, and tasks. It also contains nested classes for HostConfig, VmConfig, and
 * TasksConfig.
 */
public class DatacenterConfig {
    // Cost per second of computation resources in the datacenter
    public double costPerSec;

    // Cost per memory unit (e.g., per MB or GB)
    public double costPerMem;

    // Cost per storage unit (e.g., per MB or GB)
    public double costPerStorage;

    // Cost per bandwidth unit (e.g., per MB or GB of bandwidth)
    public double costPerBw;

    // List of host configurations for the datacenter
    public ArrayList<HostConfig> hosts;

    // List of VM (Virtual Machine) configurations for the datacenter
    public ArrayList<VmConfig> vms;

    // Task configuration for the datacenter
    public TasksConfig tasks;

    /**
     * Nested class representing the configuration of a host machine. Extends MachineConfigAbstract
     * and includes the VM scheduler type.
     */
    public static class HostConfig extends MachineConfigAbstract {
        // VM Scheduler options: TIMESHARED = 0, SPACESHARED = 1
        public int vmScheduler;
    }

    /**
     * Nested class representing the configuration of a virtual machine (VM). Extends
     * MachineConfigAbstract and includes the task scheduler type.
     */
    public static class VmConfig extends MachineConfigAbstract {
        // Task Scheduler options: TIMESHARED = 0, SPACESHARED = 1, COMPLETELYFAIR = 2
        public int taskScheduler;
    }

    /**
     * Nested class representing the configuration for tasks (Cloudlets). Extends ConfigAbstract and
     * includes parameters like file size, output size, PEs, and length.
     */
    public static class TasksConfig extends ConfigAbstract {
        // List of input file sizes for the tasks
        public ArrayList<Integer> fileSize;

        // List of output file sizes for the tasks
        public ArrayList<Integer> outputSize;

        // Number of PEs (Processing Elements) required by the tasks
        public ArrayList<Integer> pesNumber;

        // List of task lengths (instruction counts)
        public ArrayList<Integer> length;
    }
}
