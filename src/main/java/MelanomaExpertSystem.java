
import model.Patient;
import model.MelanomaClassification;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
 //MelanomaExpertSystem.java se encarga de ejecutar las reglas.
public class MelanomaExpertSystem {

    private final KieContainer kieContainer;

    public MelanomaExpertSystem() {
        KieServices kieServices = KieServices.Factory.get();
        kieContainer = kieServices.getKieClasspathContainer();
    }

    public MelanomaClassification classify(Patient patient) {

        if (patient == null) {
            throw new IllegalArgumentException(
                    "Patient cannot be null"
            );
        }

        if (patient.getPrimaryTumor() == null ||
                patient.getRegionalDisease() == null ||
                patient.getDistantDisease() == null) {

            throw new IllegalArgumentException(
                    "Incomplete clinical data for patient "
                            + patient.getPatientId()
            );
        }

        // Create a NEW classification for this evaluation
        MelanomaClassification classification =
                new MelanomaClassification(
                        patient.getPatientId()
                );

        // Create a NEW Drools session
        KieSession kieSession =
                kieContainer.newKieSession();

        //Cada llamada a classify() comienza con una clasificación vacía y una sesión nueva. Así evitamos que queden resultados anteriores dentro de la sesión de reglas.

        try {

            kieSession.insert(patient);
            kieSession.insert(classification);

            int firedRules = kieSession.fireAllRules();

            System.out.println(
                    "Patient " + patient.getPatientId()
                            + " -> Rules fired: " + firedRules
            );

            if (classification.getTCategory() == null ||
                    classification.getNCategory() == null ||
                    classification.getMCategory() == null ||
                    classification.getStage() == null) {

                throw new IllegalStateException(
                        "Incomplete classification for patient "
                                + patient.getPatientId()
                );
            }

            return classification;

        } finally {

            // Always close the session
            kieSession.dispose();
        }
    }
}

