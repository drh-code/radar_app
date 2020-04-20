package horton.radar.app;

import mikera.vectorz.Vector2;

public class RadarObject {

    public Vector2 position;
    public Vector2 velocity;
    public double RCS; // radar cross section

    public RadarObject()
    {
        position = new Vector2(0,0);
        velocity = new Vector2(0,0);
        RCS = 1.0;
    }
    public RadarObject(Vector2 pos, Vector2 vel, double rcs)
    {
        position = pos;
        velocity = vel;
        RCS = rcs;
    }
}
