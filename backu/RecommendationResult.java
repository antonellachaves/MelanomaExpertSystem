
package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RecommendationResult {

    private final List<ClinicalRecommendation> recommendations =
            new ArrayList<>();

    // Add a recommendation, avoiding duplicate rule IDs
    public void addRecommendation(ClinicalRecommendation recommendation) {

        for (ClinicalRecommendation existing : recommendations) {
            if (existing.getRuleId().equals(recommendation.getRuleId())) {
                return;
            }
        }

        recommendations.add(recommendation);
    }

    // Return recommendations ordered by priority
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
