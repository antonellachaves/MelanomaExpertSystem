
package model;

public class MelanomaClassification {

    private final int patientId;

    private TCategory tCategory;
    private NCategory nCategory;
    private MCategory mCategory;
    private MelanomaStage stage;

    // Indicates whether the classification is finalized
    private boolean locked = false;

    public MelanomaClassification(int patientId) {
        this.patientId = patientId;
    }

    public int getPatientId() {
        return patientId;
    }

    public TCategory getTCategory() {
        return tCategory;
    }

    public NCategory getNCategory() {
        return nCategory;
    }

    public MCategory getMCategory() {
        return mCategory;
    }

    public MelanomaStage getStage() {
        return stage;
    }

    public boolean isLocked() {
        return locked;
    }

    private void checkNotLocked() {
        if (locked) {
            throw new IllegalStateException(
                    "Classification for patient " + patientId
                            + " is finalized and cannot be modified."
            );
        }
    }

    public void setTCategory(TCategory tCategory) {
        checkNotLocked();
        this.tCategory = tCategory;
    }

    public void setNCategory(NCategory nCategory) {
        checkNotLocked();
        this.nCategory = nCategory;
    }

    public void setMCategory(MCategory mCategory) {
        checkNotLocked();
        this.mCategory = mCategory;
    }

    public void setStage(MelanomaStage stage) {
        checkNotLocked();
        this.stage = stage;
    }

    // Finalize the classification after Drools execution
    public void lock() {
        if (tCategory == null ||
                nCategory == null ||
                mCategory == null ||
                stage == null) {

            throw new IllegalStateException(
                    "Cannot finalize an incomplete classification for patient "
                            + patientId
            );
        }

        locked = true;
    }
}

