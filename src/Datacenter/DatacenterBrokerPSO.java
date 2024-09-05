package project.Datacenter;

import org.cloudsimplus.brokers.DatacenterBroker;
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
public class DatacenterBrokerPSO extends DatacenterBroker {
     /**
     * Creates a new DatacenterBroker.
     *
     * @param simulation the CloudSimPlus instance that represents the simulation the Entity is
     *        related to
     */
    public DatacenterBrokerPSO(final CloudSimPlus simulation) {
        this(simulation, "");
    }

    /**
     * Creates a DatacenterBroker giving a specific name.
     *
     * @param simulation the CloudSimPlus instance that represents the simulation the Entity is
     *        related to
     * @param name the DatacenterBroker name
     */
    public DatacenterBrokerPSO(final CloudSimPlus simulation, final String name) {
        super(simulation, name);
        this.lastSelectedVmIndex = -1;
        this.lastSelectedDcIndex = -1;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * <b>It applies a Round-Robin policy to cyclically select the next Datacenter from the list.
     * However, it just moves to the next Datacenter when the previous one was not able to create
     * all {@link #getVmWaitingList() waiting VMs}.
     * </p>
     *
     * <p>
     * This policy is just used if the selection of the closest Datacenter is not enabled.
     * Otherwise, the {@link #closestDatacenterMapper(Datacenter, Vm)} is used instead.
     * </p>
     *
     * @param lastDatacenter {@inheritDoc}
     * @param vm {@inheritDoc}
     * @return {@inheritDoc}
     * @see DatacenterBroker#setDatacenterMapper(java.util.function.BiFunction)
     * @see #setSelectClosestDatacenter(boolean)
     */
    @Override
    protected Datacenter defaultDatacenterMapper(final Datacenter lastDatacenter, final Vm vm) {
        if (getDatacenterList().isEmpty()) {
            throw new IllegalStateException("You don't have any Datacenter created.");
        }

        if (getDatacenterList().size() == 1)
            return getDatacenterList().get(0);

        if (lastDatacenter != Datacenter.NULL) {
            return nextDatacenter(lastDatacenter);
        }

        /*
         * If all Datacenter were tried already, return Datacenter.NULL to indicate there isn't a
         * suitable Datacenter to place waiting VMs.
         */
        if (triedDatacenters >= getDatacenterList().size()) {
            return Datacenter.NULL;
        }

        triedDatacenters++;
        // Selects the next datacenter in a circular (round-robin) way.
        return getDatacenterList().get(++lastSelectedDcIndex % getDatacenterList().size());
    }

    private Datacenter nextDatacenter(final Datacenter lastDatacenter) {
        if (lastSelectedDcIndex == -1)
            lastSelectedDcIndex = getDatacenterList().indexOf(lastDatacenter);

        return lastDatacenter;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * <b>It applies a Round-Robin policy to cyclically select the next Vm from the
     * {@link #getVmWaitingList() list of waiting VMs}.
     * </p>
     *
     * @param cloudlet {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    protected Vm defaultVmMapper(final Cloudlet cloudlet) {
        if (cloudlet.isBoundToVm()) {
            return cloudlet.getVm();
        }

        if (getVmExecList().isEmpty()) {
            return Vm.NULL;
        }

        /*
         * If the cloudlet isn't bound to a specific VM or the bound VM was not created, cyclically
         * selects the next VM on the list of created VMs.
         */
        lastSelectedVmIndex = ++lastSelectedVmIndex % getVmExecList().size();
        return getVmFromCreatedList(lastSelectedVmIndex);
    }

    public static void main(){
        
    }
}
