package horton.radar.app;

import mikera.vectorz.Vector2;

public class RadarHelper {
    public static Vector2 polarToVec2(double r, double angle)
    {
        return new Vector2(r * Math.cos(angle), r * Math.sin(angle));
    }
}
