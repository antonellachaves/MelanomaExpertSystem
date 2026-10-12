
package model;

public class MelanomaClassification {

    private int patientId;

    private TCategory tCategory;
    private NCategory nCategory;
    private MCategory mCategory;
    private MelanomaStage stage;

    public MelanomaClassification(int patientId) {
        this.patientId = patientId;
    }

    public int getPatientId() {
        return patientId;
    }

    public TCategory getTCategory() {
        return tCategory;
    }

    public void setTCategory(TCategory tCategory) {
        this.tCategory = tCategory;
    }

    public NCategory getNCategory() {
        return nCategory;
    }

    public void setNCategory(NCategory nCategory) {
        this.nCategory = nCategory;
    }

    public MCategory getMCategory() {
        return mCategory;
    }

    public void setMCategory(MCategory mCategory) {
        this.mCategory = mCategory;
    }

    public MelanomaStage getStage() {
        return stage;
    }

    public void setStage(MelanomaStage stage) {
        this.stage = stage;
    }
}
