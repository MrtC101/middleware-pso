package project.Utils;

import java.util.Random;



public class RandomUtils {
    
    private static Random random = new Random();
    public static int randomIntMultiple(int minValue, int maxValue, int multiple) {
        int min = (minValue + multiple-1) / multiple;
        int max = maxValue / multiple;
        int randomValue = random.nextInt(max - min + 1) + min;
        return randomValue * multiple;
    }


}
