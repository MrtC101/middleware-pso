package project.Experiment;

import java.util.ArrayList;


/**
 * Abstract class representing a general configuration. Includes common properties such as size and
 * whether the configuration is heterogeneous.
 */
public abstract class ConfigAbstract {
    // Size of the resources/configurations, represented as a list of integers
    public ArrayList<Integer> size;

    // Indicates if the configuration should support heterogeneity (variation in parameters)
    public boolean heterogeneous;
}
