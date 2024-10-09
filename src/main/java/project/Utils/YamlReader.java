package project.Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import project.Experiment.DatacenterConfig;

public class YamlReader {
    public static DatacenterConfig readConfig(String fileName) {
        LoaderOptions loaderOptions = new LoaderOptions();
        Constructor constructor = new Constructor(DatacenterConfig.class, loaderOptions);
        Yaml yaml = new Yaml(constructor);
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(new File(fileName));
            DatacenterConfig datacenterConfig = yaml.load(inputStream);
            return datacenterConfig;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
