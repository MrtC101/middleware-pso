package project.Datacenter;

import org.cloudsimplus.brokers.DatacenterBroker;
import org.cloudsimplus.brokers.DatacenterBrokerAbstract;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.core.*;
import org.cloudsimplus.datacenters.Datacenter;
import org.cloudsimplus.vms.Vm;


/**
 * This class based on a {@link DatacenterBrokerAbstract} an implementation of a PSO based
 * algorithm for cloudlet assignment.
 *
 * @author Cogo Martín
 */
public class DatacenterBrokerPSO extends DatacenterBrokerAbstract {

    /**
     * {@inheritDoc} 
     * Creates a DatacenterBroker that uses PSO for CloudletAllocation.
     * @param simulation {@inheritDoc}
     * @param name {@inheritDoc}
     */
    public DatacenterBrokerPSO(CloudSimPlus simulation,String name){
        super(simulation, name);
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented constructor");
    }    

    /**
     * {@inheritDoc}
     *
     * <p><b>???.</p>
     *
     * <p>This policy is just used if the selection of the closest Datacenter is not enabled.
     * Otherwise, the {@link #closestDatacenterMapper(Datacenter, Vm)} is used instead.</p>
     *
     * @param lastDatacenter {@inheritDoc}
     * @param vm {@inheritDoc}
     * @return {@inheritDoc}
     * @see DatacenterBroker#setDatacenterMapper(java.util.function.BiFunction)
     * @see #setSelectClosestDatacenter(boolean)
     */
    @Override
    protected Datacenter defaultDatacenterMapper(Datacenter lastDatacenter, Vm vm) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'defaultDatacenterMapper'");
    }

    /**
     * {@inheritDoc}
     *
     * <p><b>It applies a ??? policy to select
     * the next Vm from the {@link #getVmWaitingList() list of waiting VMs}.</p>
     *
     * @param cloudlet {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    protected Vm defaultVmMapper(Cloudlet cloudlet) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'defaultVmMapper'");
    }

    /**
     * For debuggin purpose.
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Running...");
        CloudSimPlus simulation = new CloudSimPlus();
        new DatacenterBrokerPSO(simulation,"");
    }
}

