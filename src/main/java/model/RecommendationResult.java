
package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RecommendationResult {

    private final int patientId;
    private final List<ClinicalRecommendation> recommendations;

    public RecommendationResult(int patientId) {
        this.patientId = patientId;
        this.recommendations = new ArrayList<>();
    }

    public int getPatientId() {
        return patientId;
    }

    public void addRecommendation(ClinicalRecommendation recommendation) {

        if (recommendation == null) {
            throw new IllegalArgumentException(
                    "Recommendation cannot be null."
            );
        }

        if (recommendation.getPatientId() != patientId) {
            throw new IllegalArgumentException(
                    "Recommendation belongs to a different patient."
            );
        }

        for (ClinicalRecommendation existing : recommendations) {
            if (existing.getRuleId().equals(recommendation.getRuleId())) {
                return;
            }
        }

        recommendations.add(recommendation);
    }

    public List<ClinicalRecommendation> getRecommendations() {

        List<ClinicalRecommendation> sorted =
                new ArrayList<>(recommendations);

        sorted.sort(
                Comparator.comparingInt(
                        ClinicalRecommendation::getPriority
                ).reversed()
        );

        return sorted;
    }

    public boolean hasRecommendations() {
        return !recommendations.isEmpty();
    }

    public int getRecommendationCount() {
        return recommendations.size();
    }

}
