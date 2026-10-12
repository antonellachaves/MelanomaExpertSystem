package model;

public class PrimaryTumor {

    private double breslowThicknessMm;
    private boolean ulceration;

    // Empty constructor
    public PrimaryTumor() {
    }

    // Constructor with tumor information
    public PrimaryTumor(double breslowThicknessMm, boolean ulceration) {
        this.breslowThicknessMm = breslowThicknessMm;
        this.ulceration = ulceration;
    }

    // Getter and setter for Breslow thickness
    public double getBreslowThicknessMm() {
        return breslowThicknessMm;
    }

    public void setBreslowThicknessMm(double breslowThicknessMm) {
        this.breslowThicknessMm = breslowThicknessMm;
    }

    // Getter and setter for ulceration
    public boolean isUlceration() {
        return ulceration;
    }

    public void setUlceration(boolean ulceration) {
        this.ulceration = ulceration;
    }
}