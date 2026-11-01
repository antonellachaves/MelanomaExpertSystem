
package model;

public class Patient {

    private final int patientId;

    private PrimaryTumor primaryTumor;
    private RegionalDisease regionalDisease;
    private DistantDisease distantDisease;

    private ClinicalContext clinicalContext = new ClinicalContext();

    private boolean locked = false;

    public Patient(int patientId) {
        this.patientId = patientId;
    }

    public int getPatientId() {
        return patientId;
    }

    public PrimaryTumor getPrimaryTumor() {
        return primaryTumor;
    }

    public RegionalDisease getRegionalDisease() {
        return regionalDisease;
    }

    public DistantDisease getDistantDisease() {
        return distantDisease;
    }

    public ClinicalContext getClinicalContext() {
        return clinicalContext;
    }

    private void checkNotLocked() {
        if (locked) {
            throw new IllegalStateException(
                    "Clinical data for patient " + patientId
                            + " is locked and cannot be modified."
            );
        }

    }

    public void setPrimaryTumor(PrimaryTumor primaryTumor) {
        checkNotLocked();
        this.primaryTumor = primaryTumor;
    }

    public void setRegionalDisease(RegionalDisease regionalDisease) {
        checkNotLocked();
        this.regionalDisease = regionalDisease;
    }

    public void setDistantDisease(DistantDisease distantDisease) {
        checkNotLocked();
        this.distantDisease = distantDisease;
    }

    public boolean isLocked() {
        return locked;
    }


    public void lock() {
        if (primaryTumor == null ||
                regionalDisease == null ||
                distantDisease == null) {

            throw new IllegalStateException(
                    "Cannot lock incomplete clinical data for patient "
                            + patientId
            );
        }

        clinicalContext.lock();
        locked = true;
    }

}
