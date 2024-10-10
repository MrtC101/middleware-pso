package project.Experiment;
@Deprecated
public abstract class GeneratorAbstract<T> implements Generator<T> {
    protected int min;
    protected int max;

    public GeneratorAbstract(int min, int max) {
        this.min = min;
        this.max = max;
    }

}
