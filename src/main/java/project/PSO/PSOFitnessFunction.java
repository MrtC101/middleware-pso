package project.PSO;

import net.sourceforge.jswarm_pso.FitnessFunction;
import java.util.ArrayList;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.vms.Vm;

public class PSOFitnessFunction extends FitnessFunction {

    private ArrayList<Cloudlet> cloudletList;
    private ArrayList<Vm> vmList;


    public PSOFitnessFunction(ArrayList<Cloudlet> cloudletList, ArrayList<Vm> vmList) {
        super(false);
        this.cloudletList = cloudletList;
        this.vmList = vmList;
    }

    /**
     * Objective function taken from BungaMapetu[2019]
     */
    @Override
    public double evaluate(double[] position) {
        double[] vmsCT = calcCompletionTime(position);
        double totalCost = calcMaxCtDifference(vmsCT);
        return totalCost;
    }

    private double calcMaxCtDifference(double[] vmsCT) {
        double maxCT = 0;
        for(int i = 0; i < vmsCT.length; i++){
            for(int j = 0; j < vmsCT.length; j++){
                if(i != j){
                    double currCT = vmsCT[i] - vmsCT[j];
                    if(currCT > maxCT){
                        maxCT = currCT;
                    }
                }
            }
        }
        return maxCT;
    }

    private double[] calcCompletionTime(double[] position) {
        double[] CTs = new double[vmList.size()];
        
        for (int taskId = 0; taskId < position.length; taskId++) {
            int vmId = (int) position[taskId];
            
            if (vmId < 0 || vmId >= vmList.size()) {
                throw new IllegalArgumentException("VM ID out of bounds: " + vmId);
            }
            if (taskId < 0 || taskId >= cloudletList.size()) {
                throw new IllegalArgumentException("Task ID out of bounds: " + taskId);
            }
    
            Vm currVm = vmList.get(vmId);
            Cloudlet currTask = cloudletList.get(taskId);
            
            if (currVm.getPesNumber() == 0) {
                throw new IllegalArgumentException(vmId + " VM have 0 PEs.");
            }
    
            CTs[vmId] += currTask.getLength() / (currVm.getPesNumber() * currVm.getMips());
        }
        
        return CTs;
    }
}
