package com.bezeq.locator.draw;

public class PitchAzimuthCalculator {
    private static final Vector looking = new Vector();
    private static final float[] lookingArray = new float[3];

    private static volatile float azimuth = 0;

    private static volatile float pitch = 0;

    private PitchAzimuthCalculator() {};

    public static synchronized float getAzimuth() {
        return PitchAzimuthCalculator.azimuth;
    }
    public static synchronized float getPitch() {
        return PitchAzimuthCalculator.pitch;
    }

    public static synchronized void calcPitchBearing(Matrix rotationM) {
        if (rotationM==null) return;

        looking.set(0, 0, 0);
        rotationM.transpose();
        looking.set(1, 0, 0);
        looking.prod(rotationM);
        looking.get(lookingArray);
        PitchAzimuthCalculator.azimuth = ((getAngle(0, 0, lookingArray[0], lookingArray[2])  + 360 ) % 360);

        rotationM.transpose();
        looking.set(0, 1, 0);
        looking.prod(rotationM);
        looking.get(lookingArray);
        PitchAzimuthCalculator.pitch = -getAngle(0, 0, lookingArray[1], lookingArray[2]);
    }
    
    public static final float getAngle(float center_x, float center_y, float post_x, float post_y) {
        float tmpv_x = post_x - center_x;
        float tmpv_y = post_y - center_y;
        float d = (float) Math.sqrt(tmpv_x * tmpv_x + tmpv_y * tmpv_y);
        float cos = tmpv_x / d;
        float angle = (float) Math.toDegrees(Math.acos(cos));

        angle = (tmpv_y < 0) ? angle * -1 : angle;

        return angle;
    }
}
