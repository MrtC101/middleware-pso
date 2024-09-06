package project.Datacenter;

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

    public DatacenterBrokerPSO(CloudSimPlus simulation,String name){
        super(simulation, name);
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented constructor");
    }

    public static void main(String[] args) {
        System.out.println("Running...");
        CloudSimPlus simulation = new CloudSimPlus();
        new DatacenterBrokerPSO(simulation,"");
    }
    
    @Override
    protected Datacenter defaultDatacenterMapper(Datacenter lastDatacenter, Vm vm) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'defaultDatacenterMapper'");
    }

    @Override
    protected Vm defaultVmMapper(Cloudlet cloudlet) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'defaultVmMapper'");
    }
}

