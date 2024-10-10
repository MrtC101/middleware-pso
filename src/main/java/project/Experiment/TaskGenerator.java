package project.Experiment;

import java.util.ArrayList;

import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;

import project.Utils.RandomUtils;

public class TaskGenerator implements Generator<Cloudlet> {

    private DatacenterConfig.TasksConfig tasksConfig;

    private int seed;

    int T_FILESIZE_MULT = 1000;
    int T_OUTSIZE_MULT = 1000;
    int T_LENGTH_MULT = 512;
    int T_PES_MULT = 1;

    public TaskGenerator(DatacenterConfig.TasksConfig tasksConfig, int seed) {
        this.tasksConfig = tasksConfig;
        this.seed = seed;
    }

    public TaskGenerator(DatacenterConfig.TasksConfig tasksConfig) {
        this.tasksConfig = tasksConfig;
    }

    private Cloudlet createCloudlet(int fileSize, int outputSize, int pesNumber, int length){
        CloudletSimple cloudlet;
        cloudlet = new CloudletSimple(length, pesNumber);
        cloudlet.setFileSize(fileSize);
        cloudlet.setOutputSize(outputSize);
        return cloudlet;
    }

    @Override
    public ArrayList<Cloudlet> generate(){
        int tasks = RandomUtils.randomIntMultiple(tasksConfig.size, 1);
        int fileSize;
        int outputSize;
        int pesNumber;
        int length;
        ArrayList<Cloudlet> taskList = new ArrayList<>(tasks);
        if (tasksConfig.heterogeneous){
            fileSize = RandomUtils.randomIntMultiple(tasksConfig.fileSize, T_FILESIZE_MULT);
            outputSize = RandomUtils.randomIntMultiple(tasksConfig.outputSize, T_OUTSIZE_MULT);
            pesNumber = RandomUtils.randomIntMultiple(tasksConfig.pesNumber, T_PES_MULT);
            length = RandomUtils.randomIntMultiple(tasksConfig.length, T_LENGTH_MULT);
            for (int i = 0; i < tasks; i++) {
                taskList.add(createCloudlet(fileSize, outputSize, pesNumber, length));
            }
        } else {
            for (int i = 0; i < tasks; i++) {
                fileSize = RandomUtils.randomIntMultiple(tasksConfig.fileSize, T_FILESIZE_MULT);
                outputSize = RandomUtils.randomIntMultiple(tasksConfig.outputSize, T_OUTSIZE_MULT);
                pesNumber = RandomUtils.randomIntMultiple(tasksConfig.pesNumber, T_PES_MULT);
                length = RandomUtils.randomIntMultiple(tasksConfig.length, T_LENGTH_MULT);
                taskList.add(createCloudlet(fileSize, outputSize, pesNumber, length));
            }
        }
        return taskList;    
    }
}
