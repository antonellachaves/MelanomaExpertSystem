
package model;

public class ClinicalRecommendation {

    private final String ruleId;
    private final String recommendation;
    private final int priority;

    public ClinicalRecommendation(
            String ruleId,
            String recommendation,
            int priority) {

        this.ruleId = ruleId;
        this.recommendation = recommendation;
        this.priority = priority;
    }

    public String getRuleId() {
        return ruleId;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "[" + ruleId + "] "
                + recommendation
                + " (Priority: " + priority + ")";
    }

}
