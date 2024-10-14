package project.Experiment;

import java.util.ArrayList;

/**
 * Abstract class extending ConfigAbstract, representing a machine configuration.
 * It defines the resource configurations common to machines like RAM, storage, bandwidth, PEs, and MIPS.
 */
public abstract class MachineConfigAbstract extends ConfigAbstract {
    // RAM sizes in MB for the machines
    public ArrayList<Integer> ram;

    // Storage sizes in MB for the machines
    public ArrayList<Integer> storage;

    // Bandwidth capacities in MB for the machines
    public ArrayList<Integer> bw;

    // Number of processing elements (PEs) for the machines
    public ArrayList<Integer> pesNumber;

    // MIPS (Million Instructions Per Second) rating for the machines
    public ArrayList<Integer> mips;
}