
package model;

public class MissingClinicalData {

    private final int patientId;
    private final String category;
    private final String field;
    private final String explanation;

    public MissingClinicalData(
            int patientId,
            String category,
            String field,
            String explanation) {

        this.patientId = patientId;
        this.category = category;
        this.field = field;
        this.explanation = explanation;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getCategory() {
        return category;
    }

    public String getField() {
        return field;
    }

    public String getExplanation() {
        return explanation;
    }

    @Override
    public String toString() {
        return "MissingClinicalData{" +
                "patientId=" + patientId +
                ", category='" + category + '\'' +
                ", field='" + field + '\'' +
                ", explanation='" + explanation + '\'' +
                '}';
    }
}

