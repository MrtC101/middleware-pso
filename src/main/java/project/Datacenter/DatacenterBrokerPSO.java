package project.Datacenter;

import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.core.*;
import org.cloudsimplus.datacenters.Datacenter;
import org.cloudsimplus.vms.Vm;
import project.Optimization.Mapping;


/**
 * This class based on a {@link DatacenterBrokerAbstract} an implementation of a PSO based algorithm
 * for cloudlet assignment.
 *
 * @author Cogo Martín
 */
public class DatacenterBrokerPSO extends DatacenterBrokerSimple {

    private Mapping preComputedMapping;

    /**
     * {@inheritDoc} Creates a DatacenterBroker that uses PSO for CloudletAllocation.
     * 
     * @param simulation {@inheritDoc}
     * @param name {@inheritDoc}
     */
    public DatacenterBrokerPSO(CloudSimPlus simulation, String name) {
        super(simulation, name);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * <b>It applies a default policy to select the next {@link Vm} from the {@link #getVmWaitingList() list
     * of waiting VMs} to assign the given cloudlet.</b>
     * </p>
     *
     * @param cloudlet {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Vm dapsoVmMapper(Cloudlet cloudlet) {
        return null;
    }

    /**
     * 
     */
    public void setPreComputedMapping(Mapping psoMapping){
        this.preComputedMapping = psoMapping
    }

    /**
     * Returns the corresponding {@link Vm} for the given {@link Cloudlet} that
     * was assigned previously with a PSO algorithm and 
     * @param cloudlet
     * @return
     */
    public Vm psoVmMapper(Cloudlet cloudlet) {
        return preComputedMapping.getVM(cloudlet.getId());
    }

    /**
     * For debuggin purpose.
     * 
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Running...");
        CloudSimPlus simulation = new CloudSimPlus();
        new DatacenterBrokerPSO(simulation, "");
    }
}

