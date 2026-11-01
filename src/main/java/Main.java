
import model.*;

public class Main {

    public static void main(String[] args) {

        MelanomaExpertSystem expertSystem =
                new MelanomaExpertSystem();

        // 1. Create a patient
        Patient patient = new Patient(1002);

        // 2. Primary tumor information
        PrimaryTumor primaryTumor = new PrimaryTumor(1.5, false);
        patient.setPrimaryTumor(primaryTumor);

        // 3. Regional disease information
        patient.setRegionalDisease(
                new RegionalDisease(
                        0, false, false,
                        false, false, false
                )
        );

        // 4. Distant disease information
        patient.setDistantDisease(
                new DistantDisease(
                        false, false, false,
                        false, false,
                        LdhStatus.NOT_RECORDED
                )
        );

        // 5. Additional clinical information
        ClinicalContext context = patient.getClinicalContext();

        context.setSuspiciousRegionalNode(true);
        context.setNewSymptoms(true);

        // 6. TNM classification
        System.out.println("=== MELANOMA CLASSIFICATION ===");

        MelanomaClassification classification =
                expertSystem.classify(patient);

        System.out.println("\n=== CLASSIFICATION RESULT ===");
        System.out.println("Patient ID: " + classification.getPatientId());
        System.out.println("T category: " + classification.getTCategory());
        System.out.println("N category: " + classification.getNCategory());
        System.out.println("M category: " + classification.getMCategory());
        System.out.println("Stage: " + classification.getStage());

        // 7. Clinical recommendations
        System.out.println("\n=== CLINICAL RECOMMENDATIONS ===");

        RecommendationResult recommendations =
                expertSystem.generateRecommendations(patient, classification);

        System.out.println(
                "Total recommendations: "
                        + recommendations.getRecommendationCount()
        );

        for (ClinicalRecommendation recommendation :
                recommendations.getRecommendations()) {

            System.out.println(recommendation);
        }
    }
}
