package project.Experiment.newGenarators;

import java.util.ArrayList;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import project.Experiment.Configurations.DatacenterConfig;
import project.Utils.RandomUtils;

public class RandomTaskGenerator{

    // Task configuration object that holds various task parameters
    private DatacenterConfig.TasksConfig tasksConfig;
    private int seed;
    /**
     * Constructor that takes the task configuration and a random seed.
     * 
     * @param tasksConfig Task configuration settings
     * @param seed Random seed for generating random numbers
     */
    public RandomTaskGenerator(DatacenterConfig.TasksConfig tasksConfig, int seed) {
        this.tasksConfig = tasksConfig;
        this.seed = seed;
    }

    private Cloudlet createCloudlet(int fileSize, int outputSize, int pesNumber, int length) {
        CloudletSimple cloudlet;
        cloudlet = new CloudletSimple(length, pesNumber);
        cloudlet.setFileSize(fileSize);
        cloudlet.setOutputSize(outputSize);
        return cloudlet;
    }
    private Cloudlet createCloudlet(long fileSize, long outputSize, long pesNumber, long length) {
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
    public ArrayList<Cloudlet> generate() {

        int tasks = tasksConfig.number;
        ArrayList<Cloudlet> taskList = new ArrayList<>(tasks);
        RandomIntGen rand = new RandomIntGen(seed);
        for (int i = 0; i < tasks; i++) {
            int fileSize = rand.getRandomNumberBetween(tasksConfig.fileSize);
            int outputSize = rand.getRandomNumberBetween(tasksConfig.outputSize);
            int pesNumber = rand.getRandomNumberBetween(tasksConfig.pesNumber);
            int length = rand.getRandomNumberBetween(tasksConfig.length);
            taskList.add(createCloudlet(fileSize, outputSize, pesNumber, length));
        }
        return taskList;
    }

    public ArrayList<Cloudlet> cloneDeep(ArrayList<Cloudlet> tasks) {
        ArrayList<Cloudlet> clonedTasks = new ArrayList<>(tasks.size());
        for (Cloudlet task : tasks) {
            clonedTasks.add(createCloudlet(task.getFileSize(), task.getOutputSize(), task.getPesNumber(), task.getLength()));
        }
        return clonedTasks;
    }
}
