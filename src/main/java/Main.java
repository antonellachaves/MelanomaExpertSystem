
import model.*;

public class Main {

    public static void main(String[] args) {

        MelanomaExpertSystem expertSystem =
                new MelanomaExpertSystem();

        // ----------------------------------
        // 1. First evaluation: Patient 1002
        // ----------------------------------

        Patient patientOriginal = new Patient(1002);

        patientOriginal.setPrimaryTumor(
                new PrimaryTumor(1.5, false)
        );

        patientOriginal.setRegionalDisease(
                new RegionalDisease(
                        0, false, false,
                        false, false, false
                )
        );

        patientOriginal.setDistantDisease(
                new DistantDisease(
                        false, false, false,
                        false, false,
                        LdhStatus.NOT_RECORDED
                )
        );

        System.out.println("=== FIRST EVALUATION ===");

        MelanomaClassification resultOriginal =
                expertSystem.classify(patientOriginal);

        printResult(resultOriginal);


        // ----------------------------------
        // 2. Updated clinical information
        //    Same patient ID, new data
        // ----------------------------------

        Patient patientUpdated = new Patient(1002);

        // Breslow changed from 1.5 mm to 4.5 mm
        patientUpdated.setPrimaryTumor(
                new PrimaryTumor(4.5, false)
        );

        patientUpdated.setRegionalDisease(
                new RegionalDisease(
                        0, false, false,
                        false, false, false
                )
        );

        patientUpdated.setDistantDisease(
                new DistantDisease(
                        false, false, false,
                        false, false,
                        LdhStatus.NOT_RECORDED
                )
        );

        System.out.println();
        System.out.println("=== SECOND EVALUATION ===");

        MelanomaClassification resultUpdated =
                expertSystem.classify(patientUpdated);

        printResult(resultUpdated);


        // ----------------------------------
        // 3. Compare both evaluations
        // ----------------------------------

        System.out.println();
        System.out.println("=== COMPARISON ===");

        System.out.println(
                "Original stage = " + resultOriginal.getStage()
        );

        System.out.println(
                "Updated stage = " + resultUpdated.getStage()
        );

        System.out.println(
                "Same classification object? " +
                        (resultOriginal == resultUpdated)
        );
    }


    // ----------------------------------
    // Helper method to display results
    // ----------------------------------

    private static void printResult(
            MelanomaClassification classification) {

        System.out.println(
                "Patient ID = " + classification.getPatientId()
        );

        System.out.println(
                "T category = " + classification.getTCategory()
        );

        System.out.println(
                "N category = " + classification.getNCategory()
        );

        System.out.println(
                "M category = " + classification.getMCategory()
        );

        System.out.println(
                "Stage = " + classification.getStage()
        );
    }
}
