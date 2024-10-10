package project.Utils;

import java.util.ArrayList;
import java.util.Random;



public class RandomUtils {
    
    private static Random random = new Random();


    public static int randomIntMultiple(ArrayList<Integer> numberList, int multiple) {
        if (numberList.size() == 1) {
            return numberList.get(0);
        } else if (numberList.size() == 2) {
            int min = (numberList.get(0) + multiple-1) / multiple;
            int max = numberList.get(1) / multiple;
            return (random.nextInt(max - min + 1) + min)*multiple;
        } else {
            throw new IllegalArgumentException("The list must contain either 1 or 2 numbers");
        }
    }


}
