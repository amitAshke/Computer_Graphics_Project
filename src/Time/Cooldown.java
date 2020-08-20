package Time;

public class Cooldown {
    private long lastTimeActivated;
    private double activationRate;

    public Cooldown(double activationRate) {
        lastTimeActivated = System.nanoTime();
        this.activationRate = activationRate;
    }

    public boolean canActivate() {
        long currentTimeActivation = System.nanoTime(), passedTime = currentTimeActivation - lastTimeActivated;
        return passedTime / 1000000000.0 > activationRate;
    }

    public void activated() {
        lastTimeActivated = System.nanoTime();
    }
}
