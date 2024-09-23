package project.Experiment;

import java.util.ArrayList;
import java.util.Random;

import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;

public class TaskGenerator extends GeneratorAbstract<Cloudlet> {

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

    int T_FILESIZE_MULT = 1000;
    int T_OUTSIZE_MULT = 1000;
    int T_LENGTH_MULT = 512;
    int T_PES_MULT = 1;

    public TaskGenerator(int minTasks, int maxTasks, int minFileSize, int maxFileSize, int minOutputSize,
            int maxOutputSize, int minPesNumber, int maxPesNumber, int minLength, int maxLength, boolean heterogeneous, int seed) {
        super(minTasks, maxTasks);
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
        super(minTasks, maxTasks);
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
        int tasks = this.random.nextInt((this.max - this.min) + 1) + this.min;
        int fileSize;
        int outputSize;
        int pesNumber;
        int length;
        ArrayList<Cloudlet> taskList = new ArrayList<Cloudlet>(tasks);
        if (this.heterogeneous){
            fileSize = Utils.randomIntMultiple(this.minFileSize, this.maxFileSize, T_FILESIZE_MULT);
            outputSize = Utils.randomIntMultiple(this.minOutputSize, this.maxOutputSize, T_OUTSIZE_MULT);
            pesNumber = this.random.nextInt((this.maxPesNumber - this.minPesNumber) + 1) + this.minPesNumber;
            length = Utils.randomIntMultiple(this.minLength, this.maxLength, T_LENGTH_MULT);
            for (int i = 0; i < tasks; i++) {
                taskList.add(createCloudlet(fileSize, outputSize, pesNumber, length));
            }
        } else {
            for (int i = 0; i < tasks; i++) {
                fileSize = Utils.randomIntMultiple(this.minFileSize, this.maxFileSize, T_FILESIZE_MULT);
                outputSize = Utils.randomIntMultiple(this.minOutputSize, this.maxOutputSize, T_OUTSIZE_MULT);
                pesNumber = this.random.nextInt((this.maxPesNumber - this.minPesNumber) + 1) + this.minPesNumber;
                length = Utils.randomIntMultiple(this.minLength, this.maxLength, T_LENGTH_MULT);
                taskList.add(createCloudlet(fileSize, outputSize, pesNumber, length));
            }
        }
        return taskList;    
    }
}
