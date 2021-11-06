package com.g3ida.withflyingcolours;

public class Utils {
    public static float epsilon = 0.001f;
    public static float PI = 3.14159265359f; //use this as Math.PI is double
    public static float PI2 = 1.57079632679f;

    private static int _idCounter = 0;
    private static final String _ID_PREFIX = "ID_";

    public static float modPI2(float angle) {
        while (angle < -PI)
            angle += 2.0 * PI;
        while (angle > PI)
            angle -= 2.0 * PI;
        return angle;
    }
}
