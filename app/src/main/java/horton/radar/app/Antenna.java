package horton.radar.app;

public class Antenna {

    public double power = 1.0;
    public boolean isIsometric = true;

    public Antenna() {
    }

    public double getPowerForAngle(double theta)
    {
        return power;
    }
}
