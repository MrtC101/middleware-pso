package project.PSO;

import java.util.ArrayList;
import java.util.Map;
import org.cloudsimplus.brokers.DatacenterBrokerAbstract;
import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.vms.Vm;
import me.tongfei.progressbar.ProgressBar;
import net.sourceforge.jswarm_pso.Swarm;

/**
 * This class based on a {@link DatacenterBrokerAbstract} an implementation of a PSO based algorithm
 * for cloudlet assignment.
 *
 * @author Cogo Martín
 */
public class DatacenterBrokerPSO extends DatacenterBrokerSimple {

    private Map<Cloudlet, Vm> precomputedMapping;

    /**
     * {@inheritDoc} Creates a DatacenterBroker that uses PSO for CloudletAllocation.
     * 
     * @param simulation {@inheritDoc}
     * @param name {@inheritDoc}
     */
    public DatacenterBrokerPSO(CloudSimPlus simulation, String name) {
        super(simulation, name);
        this.setVmMapper(this::psoVmMapper);
    }

    /**
     * Returns the corresponding {@link Vm} for the given {@link Cloudlet} that was assigned
     * previously with a PSO algorithm and
     * 
     * @param cloudlet
     * @return
     */
    private Vm psoVmMapper(Cloudlet cloudlet) {
        return precomputedMapping.get(cloudlet);
    }

    /**
     * Runs a Particle Swarm Optimization (PSO) algorithm to optimize the mapping of cloudlets to
     * VMs.
     * 
     * The PSO algorithm evolves particles representing cloudlet-to-VM mappings to find the optimal
     * solution that minimizes the fitness function defined by the cloudlet and VM performance.
     * 
     * @param popSize Number of particles (population size) in the PSO algorithm.
     * @param iterations Number of iterations (epochs) to evolve the swarm.
     * @param W Inertia weight, which controls the influence of a particle's previous velocity.
     * @param C1 Cognitive coefficient, which represents the particle's tendency to move towards its
     *        own best-known position.
     * @param C2 Social coefficient, which represents the particle's tendency to move towards the
     *        swarm's best-known position.
     */
    public void runPSO(int popSize, int iterations, double W, double C1, double C2) {

        // Variable Declaration
        ArrayList<Cloudlet> cloudlets = (ArrayList<Cloudlet>) this.getCloudletWaitingList();
        ArrayList<Vm> vms = (ArrayList<Vm>) this.getVmWaitingList();
        PSOFitnessFunction ff = new PSOFitnessFunction(cloudlets, vms);
        PSOParticle.CLOUDLET_SIZE = cloudlets.size();
        PSOParticle exampleParticle = new PSOParticle();
        PSOParticleUpdate velocityFunction = new PSOParticleUpdate(exampleParticle, W, C1, C2);
        Swarm swarm = new Swarm(popSize, exampleParticle, ff);

        swarm.setMinPosition(0);
        swarm.setMaxPosition(vms.size() - 1);
        swarm.setParticleUpdate(velocityFunction);

        // Iterations
        ProgressBar pb = new ProgressBar("PSO progress", iterations);
        for (int i = 0; i < iterations; i++) {
            swarm.evolve();
            pb.setExtraMessage(
                    String.format("Global best at iteration (%d): %f", i, swarm.getBestFitness()));
            pb.step();
        }
        PSOParticle bestParticle = (PSOParticle) swarm.getBestParticle();

        System.out.println("The best fitness value: " + swarm.getBestFitness());
        System.out.println("The best solution is: ");
        System.out.println(bestParticle);

        this.precomputedMapping = bestParticle.mapCloudletsToVms(cloudlets, vms);
    }

    /**
     * For debuggin purpose.
     * 
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Running...");
        CloudSimPlus simulation = new CloudSimPlus();
        DatacenterBrokerPSO a = new DatacenterBrokerPSO(simulation, "");
        a.submitVmList(new ArrayList<Vm>());
        a.submitCloudletList(new ArrayList<Cloudlet>());
        a.runPSO(20, 500, 1, 2, 2);
    }
    
}

