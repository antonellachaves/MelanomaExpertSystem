package model;

public class Patient {

    private int patientId;
    private PrimaryTumor primaryTumor;
    private RegionalDisease regionalDisease;
    private DistantDisease distantDisease;

    public Patient(int patientId) {
        this.patientId = patientId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
    public PrimaryTumor getPrimaryTumor() {
        return primaryTumor;
    }

    public void setPrimaryTumor(PrimaryTumor primaryTumor) {
        this.primaryTumor = primaryTumor;
    }

    public RegionalDisease getRegionalDisease() {
        return regionalDisease;
    }

    public void setRegionalDisease(RegionalDisease regionalDisease) {
        this.regionalDisease = regionalDisease;
    }

    public DistantDisease getDistantDisease() {
        return distantDisease;
    }

    public void setDistantDisease(DistantDisease distantDisease) {
        this.distantDisease = distantDisease;
    }
}