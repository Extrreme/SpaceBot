package dev.extrreme.spacebot.dto;

import com.google.gson.JsonObject;
import dev.extrreme.spacebot.base.miscellaneous.DiscordEmbeddable;
import dev.extrreme.spacebot.simulation.Constants;
import dev.extrreme.spacebot.utils.HTTPClient;
import dev.extrreme.spacebot.utils.JSONUtility;
import dev.extrreme.spacebot.utils.MatrixUtility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.ejml.simple.SimpleMatrix;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

@SuppressWarnings("unused")
public class Satellite implements DiscordEmbeddable {
    // Identification
    private final int id;                                   // NORAD Satellite Catalog Number
    private final String name;                              // Satellite Name

    // Orbital Elements
    private final double inclination;                       // i - Inclination
    private final double raOfAscNode;                       // W - Right Ascension of the Ascending Node (RAAN)
    private final double argOfPerigee;                      // w - Argument of Perigee
    private final double eccentricity;                      // e - Eccentricity
    private final double meanAnomaly;                       // M - Mean Anomaly
    private final double meanMotion;                        // n - Mean Motion
    private final double semiMajAxis;                       // a - Semi-major Axis
    private final double semilatusRectum;                   // P - Semilatus Rectum
    private final double trueAnomaly;                       // O - True Anomaly
    private final double eccentricAnomaly;                  // E - Eccentric Anomaly

    private final double position;
    private final double velocity;

    private final SimpleMatrix C_IP;

    private String lastUpdated;

    public Satellite(int id, String name, double inclination, double raOfAscNode, double argOfPerigee,
                     double eccentricity, double meanAnomaly, double meanMotion) {
        this.id = id;
        this.name = name;
        this.inclination = inclination;
        this.raOfAscNode = raOfAscNode;
        this.argOfPerigee = argOfPerigee;
        this.eccentricity = eccentricity;
        this.meanAnomaly = meanAnomaly;
        this.meanMotion = meanMotion;

        this.semiMajAxis = Math.cbrt(Constants.U_EARTH /Math.pow(meanMotion*Constants.W_EARTH, 2));
        this.semilatusRectum = semiMajAxis*(1-Math.pow(eccentricity,2));

        // Eccentric Anomaly by Iterations
        double E = meanAnomaly;
        for (int i = 0; i < 10; i++) {
            E = E - (E-eccentricity* Math.sin(E)- meanAnomaly)/(1-eccentricity* Math.cos(E));
        }
        this.eccentricAnomaly = E;

        this.trueAnomaly = 2*Math.atan(Math.tan(E/2)*Math.sqrt((1+eccentricity)/(1-eccentricity)));

        this.position = semilatusRectum/(1+eccentricity*Math.cos(trueAnomaly));
        this.velocity = Math.sqrt(Constants.U_EARTH*((2/getPosition())-(1/semiMajAxis)));

        this.C_IP = MatrixUtility.rotZ(argOfPerigee)
                .mult(MatrixUtility.rotX(inclination).mult(MatrixUtility.rotZ(raOfAscNode)));
    }

    public Satellite(int id, String name, double inclination, double raOfAscNode, double argOfPerigee,
                     double eccentricity, double semiMajAxis, int timeSincePerigee) {
        this.id = id;
        this.name = name;
        this.inclination = inclination;
        this.raOfAscNode = raOfAscNode;
        this.eccentricity = eccentricity;
        this.argOfPerigee = argOfPerigee;
        this.semiMajAxis = semiMajAxis;

        this.semilatusRectum = semiMajAxis*(1-Math.pow(eccentricity,2));
        this.meanMotion = Math.sqrt(Constants.U_EARTH/Math.pow(semiMajAxis, 3));
        this.meanAnomaly = this.meanMotion*timeSincePerigee;

        // Eccentric Anomaly by Iterations
        double E = meanAnomaly;
        for (int i = 0; i < 10; i++) {
            E = E - (E-eccentricity* Math.sin(E)-meanAnomaly)/(1-eccentricity*Math.cos(E));
        }
        this.eccentricAnomaly = E;

        this.trueAnomaly = 2*Math.atan(Math.tan(E/2)*Math.sqrt((1+eccentricity)/(1-eccentricity)));

        this.position = semilatusRectum/(1+eccentricity*Math.cos(trueAnomaly));
        this.velocity = Math.sqrt(Constants.U_EARTH*((2/getPosition())-(1/semiMajAxis)));

        // C3(w) * C1(i) * C3(W)
        this.C_IP = MatrixUtility.chain(MatrixUtility.rotZ(raOfAscNode), MatrixUtility.rotX(inclination), MatrixUtility.rotZ(argOfPerigee));
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getInclination() {
        return inclination;
    }

    public double getRaOfAscNode() {
        return raOfAscNode;
    }

    public double getEccentricity() {
        return eccentricity;
    }

    public double getArgumentOfPerigee() {
        return argOfPerigee;
    }

    public double getMeanAnomaly() {
        return meanAnomaly;
    }

    public double getMeanMotion() {
        return meanMotion;
    }

    public double getSemiMajorAxis() {
        return semiMajAxis;
    }

    public double getSemilatusRectum() {
        return semilatusRectum;
    }

    public double getTrueAnomaly() {
        return trueAnomaly;
    }

    public double getEccentricAnomaly() {
        return eccentricAnomaly;
    }

    public double getPosition() {
        return position;
    }

    public double getVelocity() {
        return velocity;
    }

    public SimpleMatrix getPerifocalRotationMatrix() {
        return this.C_IP;
    }

    public SimpleMatrix getPositionECI() {
        // Satellite position in Perifocal ref. frame
        SimpleMatrix r_P = new SimpleMatrix(new double[][] {
                { position*Math.cos(trueAnomaly) },
                { position*Math.sin(trueAnomaly) },
                {               0                }
        });

        // Satellite position in ECI ref. frame
        return C_IP.mult(r_P);
    }

    public SimpleMatrix getVelocityECI() {
        // Satellite velocity in Perifocal ref. frame
        SimpleMatrix v_P = new SimpleMatrix(new double[][] {
                {      -1*Math.sqrt(Constants.U_EARTH/semilatusRectum)* Math.sin(trueAnomaly)       },
                { Math.sqrt(Constants.U_EARTH/semilatusRectum)*(eccentricity+Math.cos(trueAnomaly)) },
                {                                          0                                         }
        });

        // Satellite position in ECI ref. frame
        return C_IP.mult(v_P);
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public MessageEmbed toEmbed() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n**Inclination (i):** ").append(inclination).append(" degrees");
        sb.append("\n**Right Ascension of the Ascending Node (W):** ").append(raOfAscNode).append(" degrees");
        sb.append("\n**Argument of Perigee (w):** ").append(argOfPerigee).append(" degrees");
        sb.append("\n**Eccentricity (e):** ").append(eccentricity);
        sb.append("\n**Semi-Major Axis (a):** ").append(semiMajAxis).append(" km");
        sb.append("\n**Semilatus Rectum (P):** ").append(semilatusRectum).append(" km");

        sb.append("\n\n**Mean Motion (n):** ").append(meanMotion).append(" revolutions/day");
        sb.append("\n**Mean Anomaly (M):** ").append(meanAnomaly).append(" degrees");

        sb.append("\n\n**Altitude:** ").append(position-Constants.R_EARTH).append(" km");
        sb.append("\n**Velocity:** ").append(velocity).append(" km/s");

        return new EmbedBuilder().setColor(Color.ORANGE)
                .setTitle(name)
                .setDescription(sb)
                .setThumbnail("http://clipart-library.com/img/1817029.png")
                .setFooter("ID: " + id + " / Last Updated: " + lastUpdated)
                .build();
    }

    @Override
    public String toString() {
        return "Satellite{" +
                "id=" + id + ", name='" + name + "'" + ", inclination=" + inclination +
                ", rightAscensionOfAscendingNode=" + raOfAscNode +
                ", argOfPerigee=" + argOfPerigee + ", eccentricity=" + eccentricity +
                ", meanAnomaly=" + meanAnomaly + ", meanMotion=" + meanMotion +
                ", semiMajAxis=" + semiMajAxis + ", semilatusRectum=" + semilatusRectum +
                ", trueAnomaly=" + trueAnomaly + ", eccentricAnomaly=" + eccentricAnomaly +
                ", position=" + position + ", velocity=" + velocity +
                '}';
    }

    public static Satellite getData(int id) {
        String endpoint = "https://celestrak.org/NORAD/elements/gp.php?CATNR=" + id + "&FORMAT=json";

        try (InputStream is = Objects.requireNonNull(HTTPClient.executeGet(endpoint, new HashMap<>()))) {
            JsonObject json = (JsonObject) JSONUtility.read(is).getAsJsonArray().get(0);

            if (json == null) {
                return null;
            }

            String name = json.get("OBJECT_NAME").getAsString();

            if (name == null || name.equals("")) {
                return null;
            }

            double inclination = Double.parseDouble(json.get("INCLINATION").getAsString());
            double raOfAscNode = Double.parseDouble(json.get("RA_OF_ASC_NODE").getAsString());
            double argOfPerigee = Double.parseDouble(json.get("ARG_OF_PERICENTER").getAsString());
            double eccentricity = Double.parseDouble(json.get("ECCENTRICITY").getAsString());
            double meanAnomaly = Double.parseDouble(json.get("MEAN_ANOMALY").getAsString());
            double meanMotion = Double.parseDouble(json.get("MEAN_MOTION").getAsString());

            Satellite sat = new Satellite(id, name, inclination, raOfAscNode, argOfPerigee,
                    eccentricity, meanAnomaly, meanMotion);

            sat.setLastUpdated(json.get("EPOCH").getAsString());

            return sat;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}