package me.earth.earthhack.impl.util.math;

import net.minecraft.util.math.Vec3d;

public class MathUtil
{
    public static int clamp(int num, int min, int max)
    {
        return (num < min) ? min : (Math.min(num, max));
    }

    public static float clamp(float num, float min, float max)
    {
        return (num < min) ? min : (Math.min(num, max));
    }

    public static double clamp(double num, double min, double max)
    {
        return (num < min) ? min : (Math.min(num, max));
    }

    public static int square(int i)
    {
        return i * i;
    }

    public static float square(float i)
    {
        return i * i;
    }

    public static double square(double i)
    {
        return i * i;
    }

    /**
     * A simple to Math.pow operation, taking
     * integers as power.
     *
     * @param number the number to exponentiate.
     * @param power the power.
     * @return number to the power of the power.
     */
    public static double simplePow(double number, int power)
    {
        if (power == 0)
        {
            return 1;
        }
        else if (power < 0)
        {
            return 1 / simplePow(number, power * -1);
        }

        double result = number;
        for (int i = 1; i < power; i++)
        {
            result *= number;
        }

        return result;
    }

    public static double round(double number, int places)
    {
        return Math.round(number * simplePow(10.0, places)) / simplePow(10.0, places);
    }

    public static float rad(float angle)
    {
        return (float) (angle * Math.PI / 180);
    }

    public static double degree(double angle)
    {
        return angle / Math.PI * 180;
    }

    public static double angle(Vec3d vec3d, Vec3d other)
    {
        double lengthSq = vec3d.length() * other.length();

        if (lengthSq < 1.0E-4D)
        {
            return 0.0;
        }

        double dot = vec3d.dotProduct(other);
        double arg = dot / lengthSq;

        if (arg > 1)
        {
            return 0.0;
        }
        else if (arg < -1)
        {
            return 180.0;
        }

        return Math.acos(arg) * 180.0f / Math.PI;
    }

    /**
     * Draws a Vec3d between 2 Vec3ds, from first
     * parameter to second.
     *
     * @param from the start vec3d.
     * @param to the end vec3d.
     * @return return a vec3d looking from "from" to "to".
     */
    public static Vec3d fromTo(Vec3d from, Vec3d to)
    {
        return fromTo(from.x, from.y, from.z, to);
    }

    /**
     * Convenience method.
     */
    public static Vec3d fromTo(Vec3d from, double x, double y, double z)
    {
        return fromTo(from.x, from.y, from.z, x, y, z);
    }

    /**
     * Convenience method.
     */
    public static Vec3d fromTo(double x, double y, double z, Vec3d to)
    {
        return fromTo(x, y, z, to.x, to.y, to.z);
    }

    /**
     * Convenience method.
     */
    public static Vec3d fromTo(double x, double y, double z, double x2, double y2, double z2)
    {
        return new Vec3d(x2 - x, y2 - y, z2 - z);
    }

}
