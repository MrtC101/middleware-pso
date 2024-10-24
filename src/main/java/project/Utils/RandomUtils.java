package project.Utils;

import java.util.ArrayList;
import java.util.Random;
/**
 * Utility class that provides methods for generating random integers 
 * based on a list of values and a specified multiple.
 */
public class RandomUtils {
    
    // Random number generator instance
    private static Random random = new Random();

    /**
     * Generates a random integer that is a multiple of the specified value
     * and falls within the range of values provided in the numberList.
     * 
     * @param numberList an ArrayList of integers, where the first element represents the minimum value
     *                   and the second element represents the maximum value for the random number generation.
     *                   If only one number is present in the list, that number is returned.
     * @param multiple the integer value that the generated random number should be a multiple of.
     * @return a random integer that is a multiple of the specified value.
     * @throws IllegalArgumentException if the list doesn't contain 1 or 2 numbers.
     */
    public static int randomIntMultiple(ArrayList<Integer> numberList, int multiple) {
        // Case when the list has only one element, simply return that element
        if (numberList.size() == 1) {
            return numberList.get(0);
        } 
        // Case when the list has two elements, generate a random number between min and max, both being multiples of 'multiple'
        else if (numberList.size() == 2) {
            // Calculate the minimum and maximum bounds that are multiples of the specified 'multiple'
            int min = (numberList.get(0) + multiple - 1) / multiple; // Ensures the minimum is rounded up
            int max = numberList.get(1) / multiple; // Ensures the maximum is a multiple of 'multiple'
            
            // Generate a random number between min and max (both inclusive), multiply by 'multiple' to get the final result
            return (random.nextInt(max - min + 1) + min) * multiple;
        } 
        // If the list contains neither 1 nor 2 elements, throw an exception
        else {
            throw new IllegalArgumentException("The list must contain either 1 or 2 numbers");
        }
    }

    // Generar un número aleatorio entre min y max (ambos incluidos)
    public static int getRandomNumberBetween(ArrayList list) {
        int min = (int) list.get(0);
        int max = (int) list.get(1);
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
}