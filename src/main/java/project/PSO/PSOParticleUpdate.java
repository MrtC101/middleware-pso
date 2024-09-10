package project.PSO;

import net.sourceforge.jswarm_pso.Particle;
import net.sourceforge.jswarm_pso.ParticleUpdate;
import net.sourceforge.jswarm_pso.Swarm;

public class PSOParticleUpdate extends ParticleUpdate {
    private double W;
    private double C1;
    private double C2;

    /**
     * Constructs a Velocity Function for a particle in the Particle Swarm Optimization (PSO)
     * algorithm.
     * 
     * @param particle The particle whose velocity is being constructed.
     * @param W Inertia Weight. Controls the impact of the particle's previous velocity on its
     *        current velocity.
     * @param C1 Cognitive Learning Factor. Influences how much the particle is attracted to its own
     *        best-known position.
     * @param C2 Social Learning Factor. Influences how much the particle is attracted to the
     *        best-known position in the swarm.
     */
    public PSOParticleUpdate(Particle particle, double W, double C1, double C2) {
        super(particle);
        this.W = W;
        this.C1 = C1;
        this.C2 = C2;
    }

    @Override
    public void update(Swarm swarm, Particle particle) {
        double[] velocity = particle.getVelocity();
        double[] position = particle.getPosition();
        double[] pbest = particle.getBestPosition();
        double[] gbest = swarm.getBestPosition();

        for (int i = 0; i < velocity.length; ++i) {
            velocity[i] = W * velocity[i] + C1 * Math.random() * (pbest[i] - position[i])
                    + C2 * Math.random() * (gbest[i] - position[i]);
            position[i] = (int) (position[i] + velocity[i]);
        }
    }

    @Override
    public String toString() {
        return String.format("Used constants for velocity function:%n" + "W  = %#e%n" + "C1 = %#e%n"
                + "C2 = %#e", this.W, this.C1, this.C2);
    }
}
