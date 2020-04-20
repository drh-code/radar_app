package horton.radar.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Digitizer {
    public static final double C = 299792458.0; // m/s
    public static final double C_inv = 1.0 / C;
    public static final double C_inv_x2 = 2.0 / C;

    public double powerPerMeter = 5e16;
    public double noisePerMeter = 0.0005;
    public double dutyCycle = 0.10;
    public int numRangeGates = 100;
    public double rangeLimit = 15000;

    public Random RNG;

    public Digitizer() {
        RNG = new Random();
    }

    double[] calculatePulseReturn(RadarWorld world)
    {
        double pulseWidth = rangeLimit * dutyCycle;  // pulse width in terms of range
        //numRangeGates = (int) (2.0 * rangeLimit / pulseWidth); // is this a nyquist thingy?
        double rgWidth = rangeLimit / numRangeGates; // range gate width

        double [] out = new double[numRangeGates];

        int off_until = (int)(numRangeGates * dutyCycle); // transmitter on / receiver off time

        // RANDOM NOISE
        for (int i = 0; i < numRangeGates; ++i)
        {
            if (i > off_until)
                out[i] = RNG.nextDouble() * rgWidth * noisePerMeter;
            else
                out[i] = 0.0;
        }

        // OBJECT RETURNS
        for (int i = 0; i < world.objects.size(); ++i)
        {
            RadarObject o = world.objects.get(i);
            double range = o.position.magnitude();
            while (range > rangeLimit) range -= rangeLimit; // wrap around (range ambiguity)
            //double flightTime = range * C_inv_x2; // round trip time

            double pulseRangeEnd = range + pulseWidth;
            int currBin = (int) ( range / rgWidth );
            //int endBin = (int) ( pulseRangeEnd / rgWidth );

            double binStart = range;
            double binEnd;// = (currBin+1) * rgWidth;
            while (binStart < pulseRangeEnd)
            {
                binEnd = Math.min(pulseRangeEnd, binStart + rgWidth);
                if (currBin > off_until) // only receive when TX off
                {
                    double loss = Math.pow(range, 4.0);
                    //double overlap = (binEnd - binStart) / rgWidth;
                    //out[currBin] += overlap * 0.3;

                    double overlap = (binEnd - binStart);
                    out[currBin] += powerPerMeter * overlap / loss;
                }

                binStart += rgWidth;
                currBin = (currBin+1) % numRangeGates;
            }
        }
        return out;
    }

}
