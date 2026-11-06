
package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EvaluationResult {

    private final int patientId;
    private final MelanomaClassification classification;

    private EvaluationStatus status;

    private final List<MissingClinicalData> missingData;
    private final List<String> explanations;
    private final List<String> recommendations;

    public EvaluationResult(
            int patientId,
            MelanomaClassification classification) {

        if (classification == null) {
            throw new IllegalArgumentException(
                    "Classification cannot be null.");
        }

        if (classification.getPatientId() != patientId) {
            throw new IllegalArgumentException(
                    "Patient ID does not match classification.");
        }

        this.patientId = patientId;
        this.classification = classification;
        this.status = EvaluationStatus.INCOMPLETE;

        this.missingData = new ArrayList<>();
        this.explanations = new ArrayList<>();
        this.recommendations = new ArrayList<>();
    }

    public int getPatientId() {
        return patientId;
    }

    public MelanomaClassification getClassification() {
        return classification;
    }

    public EvaluationStatus getStatus() {
        return status;
    }

    public void setStatus(EvaluationStatus status) {
        if (status == null) {
            throw new IllegalArgumentException(
                    "Evaluation status cannot be null.");
        }
        this.status = status;
    }

    public void addMissingData(MissingClinicalData data) {
        if (data == null) {
            throw new IllegalArgumentException(
                    "Missing clinical data cannot be null.");
        }

        if (data.getPatientId() != patientId) {
            throw new IllegalArgumentException(
                    "Patient ID does not match missing data.");
        }

        boolean alreadyExists = missingData.stream().anyMatch(
                existing ->
                        existing.getCategory().equals(data.getCategory())
                                && existing.getField().equals(data.getField())
        );

        if (!alreadyExists) {
            missingData.add(data);
        }
    }

    public void addExplanation(String explanation) {
        if (explanation != null && !explanation.isBlank()
                && !explanations.contains(explanation)) {
            explanations.add(explanation);
        }
    }

    public void addRecommendation(String recommendation) {
        if (recommendation != null && !recommendation.isBlank()
                && !recommendations.contains(recommendation)) {
            recommendations.add(recommendation);
        }
    }

    public List<MissingClinicalData> getMissingData() {
        return Collections.unmodifiableList(missingData);
    }

    public List<String> getExplanations() {
        return Collections.unmodifiableList(explanations);
    }

    public List<String> getRecommendations() {
        return Collections.unmodifiableList(recommendations);
    }

    public boolean isComplete() {
        return status == EvaluationStatus.COMPLETE;
    }
}
