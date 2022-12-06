package dev.extrreme.spacebot.simulation;

public class Equations {
    public static double orbitEqn(double P, double e, double tano) {
        return P/(1+e*Math.cos(tano));
    }
}