package project.Experiment.newGenarators;

import java.util.List;
import java.util.Random;

public class RandomIntGen {
    private Random random;
    
    public RandomIntGen(long seed){
        random = new Random(seed);
    }

    // Generar un número aleatorio entre min y max (ambos incluidos)
    public int getRandomNumberBetween(List<Integer> list) {
        int min = (int) list.get(0);
        if(list.size() > 1){
            int max = (int) list.get(1);
            return random.nextInt((max - min) + 1) + min;
        } else {
            return min;
        }
    }
}
