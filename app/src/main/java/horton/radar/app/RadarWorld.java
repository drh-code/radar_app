package horton.radar.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RadarWorld {

    private Random RNG;
    public List<RadarObject> objects;
    public Antenna antenna;
    public Digitizer digitizer;

    public double MinRange = 2000.0;
    public double MaxRange = 100000.0;
//    public double SampleRateMHz = 1;
//    public double PRI_ms = 0.001;
//    public double numRangeBins = 100;
//    public double max

    public RadarWorld() {
        RNG = new Random();
        objects = new ArrayList<>();
        antenna = new Antenna();
        digitizer = new Digitizer();
    }

    public void addObject(RadarObject obj)
    {
        objects.add(obj);
    }

    public void addRandomObject()
    {
        RadarObject o = new RadarObject();
        double range = RNG.nextDouble() * (MaxRange - MinRange) + MinRange;
        double ang = RNG.nextDouble() * Math.PI * 2.0;

        o.position = RadarHelper.polarToVec2(range, ang);
        o.velocity = RadarHelper.polarToVec2(100.0, RNG.nextDouble() * Math.PI * 2.0);
        o.RCS = RNG.nextDouble() * 3.0 + 1.0;

        objects.add(o);
    }

}
