package project.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import project.Experiment.DatacenterConfig;

/**
 * Utility class for reading YAML configuration files and mapping the contents
 * to a DatacenterConfig object.
 */
public class YamlReader {

    /**
     * Reads a YAML file and maps it to a DatacenterConfig object using SnakeYAML.
     *
     * @param fileName the path to the YAML file to be read.
     * @return a DatacenterConfig object containing the parsed configuration data,
     *         or null if the file is not found or an error occurs during loading.
     */
    public static DatacenterConfig readConfig(String fileName) {
        // Loader options allow customization of YAML parsing behavior
        LoaderOptions loaderOptions = new LoaderOptions();
        
        // Constructor is used to create instances of DatacenterConfig based on YAML input
        Constructor constructor = new Constructor(DatacenterConfig.class, loaderOptions);
        
        // Yaml object that performs the actual YAML parsing
        Yaml yaml = new Yaml(constructor);
        
        InputStream inputStream;
        try {
            // Attempt to open the specified file as an InputStream
            inputStream = new FileInputStream(new File(fileName));
            
            // Load and parse the YAML file, mapping it to the DatacenterConfig object
            DatacenterConfig datacenterConfig = yaml.load(inputStream);
            
            // Return the parsed configuration
            return datacenterConfig;
        } catch (FileNotFoundException e) {
            // Print stack trace in case the file is not found and return null
            e.printStackTrace();
            return null;
        }
    }
}
