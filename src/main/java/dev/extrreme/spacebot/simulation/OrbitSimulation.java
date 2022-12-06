package dev.extrreme.spacebot.simulation;

import dev.extrreme.spacebot.dto.Satellite;
import org.ejml.simple.SimpleMatrix;
import org.knowm.xchart.*;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.awt.*;

public class OrbitSimulation {
    private final Satellite satellite;

    private final int orbits;
    private final int resolution;

    private SimpleMatrix[] r_P = null;
    private SimpleMatrix[] v_P = null;

    private SimpleMatrix[] r_I = null;
    private SimpleMatrix[] v_I = null;

    public OrbitSimulation(Satellite satellite, int orbits, int resolution) {
        this.satellite = satellite;
        this.orbits = orbits;
        this.resolution = resolution;
    }

    public OrbitSimulation simulate() {
        r_P = new SimpleMatrix[resolution];
        v_P = new SimpleMatrix[resolution];
        r_I = new SimpleMatrix[resolution];
        v_I = new SimpleMatrix[resolution];

        double maxTano = 2*Math.PI*orbits;

        double P = satellite.getSemilatusRectum();
        double e = satellite.getEccentricity();

        SimpleMatrix C_IP = satellite.getPerifocalRotationMatrix();

        int i = 0;
        double tano = satellite.getTrueAnomaly();

        while (i < resolution) {
            double r = Equations.orbitEqn(P, e, tano);

            SimpleMatrix r_P = new SimpleMatrix(new double[][] {
                    { r * Math.cos(tano) },
                    { r * Math.sin(tano) },
                    {          0         }
            });

            SimpleMatrix v_P = new SimpleMatrix(new double[][] {
                    { -1*Math.sqrt(Constants.U_EARTH/P)* Math.sin(tano) },
                    { Math.sqrt(Constants.U_EARTH/P)*(e+Math.cos(tano)) },
                    {                         0                         }
            });

            SimpleMatrix r_I = C_IP.mult(r_P);
            SimpleMatrix v_I = C_IP.mult(v_P);

            this.r_P[i] = r_P;
            this.v_P[i] = v_P;
            this.r_I[i] = r_I;
            this.v_I[i] = v_I;

            i += 1;
            tano += maxTano/resolution;
        }

        return this;
    }

    public Satellite getSatellite() {
        return satellite;
    }

    public SimpleMatrix[] getPositionPerifocal() {
        return r_P;
    }

    public SimpleMatrix[] getVelocityPerifocal() {
        return v_P;
    }

    public SimpleMatrix[] getPositionECI() {
        return r_I;
    }

    public SimpleMatrix[] getVelocityECI() {
        return v_I;
    }

    public XYChart getPerifocalPlot() {
        double[] x = new double[resolution];
        double[] y = new double[resolution];

        for (int i = 0; i < resolution; i++) {
            x[i] = r_P[i].get(0,0);
            y[i] = r_P[i].get(1,0);
        }

        double[] xEarth = new double[resolution];
        double[] yEarth = new double[resolution];

        int i = 0;
        double tano = 0;

        while (i < resolution) {
            xEarth[i] = Constants.R_EARTH*Math.cos(tano);
            yEarth[i] = Constants.R_EARTH*Math.sin(tano);

            i += 1;
            tano += 2*Math.PI/resolution;
        }


        XYChart chart = new XYChartBuilder().width(500).height(500)
                .title("Orbit of " + satellite.getName() + " in Orbital Plane")
                .xAxisTitle("X Position").yAxisTitle("Y Position")
                .build();

        chart.getStyler().setLegendVisible(false);
        chart.addSeries("Orbit", x, y).setMarker(SeriesMarkers.NONE).setLineColor(Color.RED);
        chart.addSeries("Earth", xEarth, yEarth).setMarker(SeriesMarkers.NONE).setLineColor(Color.GREEN);

        return chart;
    }

}
