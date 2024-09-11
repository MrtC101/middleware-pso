package project.Experiment;

import java.util.ArrayList;
import java.util.Random;

import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;

public class TaskGenerator {
    private int minTasks;
    private int maxTasks;

    private int minFileSize;
    private int maxFileSize;

    private int minOutputSize;
    private int maxOutputSize;

    private int minPesNumber;
    private int maxPesNumber;

    private int minLength;
    private int maxLength;
    
    private boolean heterogeneous;

    private Random random;

    public TaskGenerator(int minTasks, int maxTasks, int minFileSize, int maxFileSize, int minOutputSize,
            int maxOutputSize, int minPesNumber, int maxPesNumber, int minLength, int maxLength, boolean heterogeneous, int seed) {
        this.minTasks = minTasks;
        this.maxTasks = maxTasks;
        this.minFileSize = minFileSize;
        this.maxFileSize = maxFileSize;
        this.minOutputSize = minOutputSize;
        this.maxOutputSize = maxOutputSize;
        this.minPesNumber = minPesNumber;
        this.maxPesNumber = maxPesNumber;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.heterogeneous = heterogeneous;
        this.random = new Random(seed);
    }

    public TaskGenerator(int minTasks, int maxTasks, int minFileSize, int maxFileSize, int minOutputSize,
            int maxOutputSize, int minPesNumber, int maxPesNumber, int minLength, int maxLength, boolean heterogeneous) {
        this.minTasks = minTasks;
        this.maxTasks = maxTasks;
        this.minFileSize = minFileSize;
        this.maxFileSize = maxFileSize;
        this.minOutputSize = minOutputSize;
        this.maxOutputSize = maxOutputSize;
        this.minPesNumber = minPesNumber;
        this.maxPesNumber = maxPesNumber;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.heterogeneous = heterogeneous;
        this.random = new Random();
    }

    private Cloudlet createCloudlet(int fileSize, int outputSize, int pesNumber, int length){
        CloudletSimple cloudlet;
        cloudlet = new CloudletSimple(length, pesNumber);
        cloudlet.setFileSize(fileSize);
        cloudlet.setOutputSize(outputSize);
        return cloudlet;
    }

    public ArrayList<Cloudlet> generate(){
        int tasks = this.random.nextInt((this.maxTasks - this.minTasks) + 1) + this.minTasks;
        int fileSize;
        int outputSize;
        int pesNumber;
        int length;
        ArrayList<Cloudlet> taskList = new ArrayList<Cloudlet>(tasks);
        if (this.heterogeneous){
            fileSize = this.random.nextInt((this.maxFileSize - this.minFileSize) + 1) + this.minFileSize;
            outputSize = this.random.nextInt((this.maxOutputSize - this.minOutputSize) + 1) + this.minOutputSize;
            pesNumber = this.random.nextInt((this.maxPesNumber - this.minPesNumber) + 1) + this.minPesNumber;
            length = this.random.nextInt((this.maxLength - this.minLength) + 1) + this.minLength;
            for (int i = 0; i < tasks; i++) {
                taskList.add(createCloudlet(fileSize, outputSize, pesNumber, length));
            }
        } else {
            for (int i = 0; i < tasks; i++) {
                fileSize = this.random.nextInt((this.maxFileSize - this.minFileSize) + 1) + this.minFileSize;
                outputSize = this.random.nextInt((this.maxOutputSize - this.minOutputSize) + 1) + this.minOutputSize;
                pesNumber = this.random.nextInt((this.maxPesNumber - this.minPesNumber) + 1) + this.minPesNumber;
                length = this.random.nextInt((this.maxLength - this.minLength) + 1) + this.minLength;
                taskList.add(createCloudlet(fileSize, outputSize, pesNumber, length));
            }
        }
        return taskList;    
    }
}
