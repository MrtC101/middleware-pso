package project.Datacenter;

import org.cloudsimplus.brokers.DatacenterBroker;
import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.datacenters.Datacenter;
import org.cloudsimplus.vms.Vm;

/**
 * This class based on a {@link DatacenterBroker} an implementation of a PSO based algorithm for
 * cloudlet assignment.
 *
 * @author Cogo Martín
 */
public class DatacenterBrokerPSO extends DatacenterBrokerSimple {

    public DatacenterBrokerPSO(final CloudSimPlus simulation){
        super(simulation);
    }

    public static void main(String[] args){
        final CloudSimPlus simulation = new CloudSimPlus();
        new DatacenterBrokerPSO(simulation);
    }
}
