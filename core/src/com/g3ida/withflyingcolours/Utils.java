package com.g3ida.withflyingcolours;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Utils {
    public static float epsilon = 0.001f;
    public static float PI = 3.14159265359f; //use this as Math.PI is double
    public static float PI2 = 1.57079632679f;

    public static float modPI2(float angle) {
        while (angle < -PI)
            angle += 2.0 * PI;
        while (angle > PI)
            angle -= 2.0 * PI;
        return angle;
    }

    public static ShaderProgram loadShader(String baseFileName) {
        return new ShaderProgram(
                Gdx.files.internal(Constants.SHADERS_PATH + baseFileName + ".vert"),
                Gdx.files.internal(Constants.SHADERS_PATH + baseFileName + ".frag"));
    }
}
