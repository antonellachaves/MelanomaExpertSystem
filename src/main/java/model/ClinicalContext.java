

package model;

public class ClinicalContext {

    // Clinical information
    private SurgeryStatus surgeryStatus = SurgeryStatus.UNKNOWN;

    private SentinelNodeBiopsyStatus sentinelNodeBiopsyStatus =
            SentinelNodeBiopsyStatus.UNKNOWN;

    private BaselineImagingStatus baselineImagingStatus =
            BaselineImagingStatus.UNKNOWN;

    private BaselineImagingStatus regionalNodeUltrasoundStatus =
            BaselineImagingStatus.UNKNOWN;

    private BrafStatus brafStatus = BrafStatus.UNKNOWN;

    private DiseaseStatus diseaseStatus = DiseaseStatus.UNKNOWN;

    private ResectabilityStatus resectabilityStatus =
            ResectabilityStatus.UNCERTAIN;

    private Boolean suspiciousRegionalNode = null;
    private Boolean newSymptoms = null;
    private Boolean adjuvantEligibilityAssessed = null;
    private Boolean currentlyUnderTreatment = null;

    // Lock mechanism
    private boolean locked = false;

    public void lock() {
        this.locked = true;
    }

    private void checkNotLocked() {
        if (locked) {
            throw new IllegalStateException(
                    "Clinical context is locked and cannot be modified."
            );
        }
    }

    // Getters

    public SurgeryStatus getSurgeryStatus() {
        return surgeryStatus;
    }

    public SentinelNodeBiopsyStatus getSentinelNodeBiopsyStatus() {
        return sentinelNodeBiopsyStatus;
    }

    public BaselineImagingStatus getBaselineImagingStatus() {
        return baselineImagingStatus;
    }

    public BrafStatus getBrafStatus() {
        return brafStatus;
    }

    public DiseaseStatus getDiseaseStatus() {
        return diseaseStatus;
    }

    public ResectabilityStatus getResectabilityStatus() {
        return resectabilityStatus;
    }


    public Boolean getSuspiciousRegionalNode() {
        return suspiciousRegionalNode;
    }

    public Boolean getNewSymptoms() {
        return newSymptoms;
    }

    public BaselineImagingStatus getRegionalNodeUltrasoundStatus() {
        return regionalNodeUltrasoundStatus;
    }

    public Boolean getAdjuvantEligibilityAssessed() {
        return adjuvantEligibilityAssessed;
    }

    public Boolean getCurrentlyUnderTreatment() {
        return currentlyUnderTreatment;
    }

    // Setters

    public void setSurgeryStatus(SurgeryStatus surgeryStatus) {
        checkNotLocked();
        this.surgeryStatus = surgeryStatus;
    }

    public void setSentinelNodeBiopsyStatus(
            SentinelNodeBiopsyStatus sentinelNodeBiopsyStatus) {
        checkNotLocked();
        this.sentinelNodeBiopsyStatus = sentinelNodeBiopsyStatus;
    }

    public void setBaselineImagingStatus(
            BaselineImagingStatus baselineImagingStatus) {
        checkNotLocked();
        this.baselineImagingStatus = baselineImagingStatus;
    }

    public void setBrafStatus(BrafStatus brafStatus) {
        checkNotLocked();
        this.brafStatus = brafStatus;
    }

    public void setDiseaseStatus(DiseaseStatus diseaseStatus) {
        checkNotLocked();
        this.diseaseStatus = diseaseStatus;
    }

    public void setResectabilityStatus(
            ResectabilityStatus resectabilityStatus) {
        checkNotLocked();
        this.resectabilityStatus = resectabilityStatus;
    }

    public void setSuspiciousRegionalNode(boolean suspiciousRegionalNode) {
        checkNotLocked();
        this.suspiciousRegionalNode = suspiciousRegionalNode;
    }

    public void setNewSymptoms(Boolean newSymptoms) {
        checkNotLocked();
        this.newSymptoms = newSymptoms;
    }

    public void setAdjuvantEligibilityAssessed(
            boolean adjuvantEligibilityAssessed) {
        checkNotLocked();
        this.adjuvantEligibilityAssessed = adjuvantEligibilityAssessed;
    }

    public void setCurrentlyUnderTreatment(boolean currentlyUnderTreatment) {
        checkNotLocked();
        this.currentlyUnderTreatment = currentlyUnderTreatment;
    }

    public void setRegionalNodeUltrasoundStatus(
            BaselineImagingStatus regionalNodeUltrasoundStatus) {

        checkNotLocked();

        if (regionalNodeUltrasoundStatus == null) {
            throw new IllegalArgumentException(
                    "Regional node ultrasound status cannot be null"
            );
        }

        this.regionalNodeUltrasoundStatus = regionalNodeUltrasoundStatus;
    }
}
