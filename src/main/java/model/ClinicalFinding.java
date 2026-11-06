
package model;

public class ClinicalFinding {

    private final int patientId;
    private final String type;
    private final String explanation;
    private final String sourceRule;

    public ClinicalFinding(int patientId, String type,
                           String explanation, String sourceRule) {
        this.patientId = patientId;
        this.type = type;
        this.explanation = explanation;
        this.sourceRule = sourceRule;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getType() {
        return type;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getSourceRule() {
        return sourceRule;
    }
}
