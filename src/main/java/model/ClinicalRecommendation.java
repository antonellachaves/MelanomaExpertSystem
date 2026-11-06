
package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClinicalRecommendation {

    private final int patientId;
    private final String ruleId;
    private final String message;
    private final int priority;
    private final String explanation;
    private final List<String> supportingRules;

    public ClinicalRecommendation(
            int patientId,
            String ruleId,
            String message,
            int priority,
            String explanation,
            List<String> supportingRules) {

        this.patientId = patientId;
        this.ruleId = ruleId;
        this.message = message;
        this.priority = priority;
        this.explanation = explanation;
        this.supportingRules = Collections.unmodifiableList(
                new ArrayList<>(supportingRules)
        );
    }

    public int getPatientId() {
        return patientId;
    }

    public String getRuleId() {
        return ruleId;
    }

    public String getMessage() {
        return message;
    }

    public int getPriority() {
        return priority;
    }

    public String getExplanation() {
        return explanation;
    }

    public List<String> getSupportingRules() {
        return supportingRules;
    }
}
