package project.PSO;

import net.sourceforge.jswarm_pso.FitnessFunction;
import java.util.ArrayList;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.vms.Vm;

public class PSOFitnessFunction extends FitnessFunction {

    private double[][] costMatrix;

    public PSOFitnessFunction(ArrayList<Cloudlet> cloudletList, ArrayList<Vm> vmList) {
        super(false);
        costMatrix = buildCostMatrix(cloudletList, vmList);
    }

    @Override
    public double evaluate(double[] position) {
        double totalCost = 0;
        for (int tskId = 0; tskId < position.length; tskId++) {
            int vmId = (int) position[tskId];
            totalCost += costMatrix[tskId][vmId];
        }
        return totalCost;
    }

    private double[][] buildCostMatrix(ArrayList<Cloudlet> cloudlets, ArrayList<Vm> vms) {
        int numCloudlets = cloudlets.size();
        int numVms = vms.size();
        double[][] matrix = new double[numCloudlets][numVms];
        // TODO: develop how to fill the cost matrix.
        return matrix;
    }

}
