package project.Experiment.Generators;

import java.util.ArrayList;

import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import project.Experiment.Configurations.DatacenterConfig;
import project.Utils.RandomUtils;

/**
 * TaskGenerator class generates Cloudlets (tasks) based on the provided configuration. It supports
 * both homogeneous and heterogeneous task configurations, with random variations in certain
 * parameters.
 */
public class TaskGenerator implements Generator<Cloudlet> {

    // Task configuration object that holds various task parameters
    private DatacenterConfig.TasksConfig tasksConfig;

    private int seed;

    // Multipliers for generating random values for various task parameters
    int T_FILESIZE_MULT = 1000;
    int T_OUTSIZE_MULT = 1000;
    int T_LENGTH_MULT = 512;
    int T_PES_MULT = 1;

    /**
     * Constructor that takes the task configuration and a random seed.
     * 
     * @param tasksConfig Task configuration settings
     * @param seed Random seed for generating random numbers
     */
    public TaskGenerator(DatacenterConfig.TasksConfig tasksConfig, int seed) {
        this.tasksConfig = tasksConfig;
        this.seed = seed;
    }

    /**
     * Constructor without seed, only taking the task configuration.
     * 
     * @param tasksConfig Task configuration settings
     */
    public TaskGenerator(DatacenterConfig.TasksConfig tasksConfig) {
        this.tasksConfig = tasksConfig;
    }


    /**
     * Creates a Cloudlet (task) with the specified parameters.
     * 
     * @param fileSize Size of the input file required by the task
     * @param outputSize Size of the output file generated by the task
     * @param pesNumber Number of PEs (cores) assigned to the task
     * @param length The length or number of instructions for the task
     * @return A new Cloudlet object with the specified configuration
     */
    private Cloudlet createCloudlet(int fileSize, int outputSize, int pesNumber, int length) {
        CloudletSimple cloudlet;
        cloudlet = new CloudletSimple(length, pesNumber);
        cloudlet.setFileSize(fileSize);
        cloudlet.setOutputSize(outputSize);
        return cloudlet;
    }

    /**
     * Generates a list of Cloudlets (tasks) based on the provided configuration settings, using
     * random values for certain parameters based on predefined multipliers.
     * 
     * @return A list of generated Cloudlets
     */
    @Override
    public ArrayList<Cloudlet> generate() {

        // Generate a random (or not) number of tasks
        int tasks = RandomUtils.randomIntMultiple(tasksConfig.size, 1);
        int fileSize, outputSize, pesNumber, length;

        // Initialize the task list with the specified size
        ArrayList<Cloudlet> taskList = new ArrayList<>(tasks);

        // If the task configuration is heterogeneous, generate random (or not) values for each
        // parameter
        if (tasksConfig.heterogeneous) {
            fileSize = RandomUtils.randomIntMultiple(tasksConfig.fileSize, T_FILESIZE_MULT);
            outputSize = RandomUtils.randomIntMultiple(tasksConfig.outputSize, T_OUTSIZE_MULT);
            pesNumber = RandomUtils.randomIntMultiple(tasksConfig.pesNumber, T_PES_MULT);
            length = RandomUtils.randomIntMultiple(tasksConfig.length, T_LENGTH_MULT);

            // If the task configuration is heterogeneous, generate random (or not) values for each
            // parameter
            for (int i = 0; i < tasks; i++) {
                taskList.add(createCloudlet(fileSize, outputSize, pesNumber, length));
            }
        } else {
            // If the configuration is homogeneous, use fixed random (or not) values for each task
            for (int i = 0; i < tasks; i++) {
                fileSize = RandomUtils.randomIntMultiple(tasksConfig.fileSize, T_FILESIZE_MULT);
                outputSize = RandomUtils.randomIntMultiple(tasksConfig.outputSize, T_OUTSIZE_MULT);
                pesNumber = RandomUtils.randomIntMultiple(tasksConfig.pesNumber, T_PES_MULT);
                length = RandomUtils.randomIntMultiple(tasksConfig.length, T_LENGTH_MULT);

                // Add the created cloudlet to the task list
                taskList.add(createCloudlet(fileSize, outputSize, pesNumber, length));
            }
        }
        return taskList; // Return the generated task list
    }
}