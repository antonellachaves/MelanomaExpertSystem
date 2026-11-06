
import model.Patient;
import model.MelanomaClassification;
import model.RecommendationResult;

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
                         "Cannot complete TNM classification for patient "
                                 + patient.getPatientId()
                                 + ". Required clinical information is missing."
                 );
             }



             boolean complete =
                     classification.getTCategory() != null &&
                             classification.getNCategory() != null &&
                             classification.getMCategory() != null &&
                             classification.getStage() != null;

             if (complete) {
                 classification.lock();
                 patient.lock();
             }

             return classification;



         } finally {

             // Always close the session
             kieSession.dispose();
         }
     }



     public RecommendationResult generateRecommendations(
             Patient patient,
             MelanomaClassification classification) {
         if (patient == null) {
             throw new IllegalArgumentException(
                     "Patient cannot be null"
             );
         }

         if (classification == null) {
             throw new IllegalArgumentException(
                     "Classification cannot be null"
             );
         }

         if (classification.getStage() == null) {
             throw new IllegalArgumentException(
                     "Patient must be classified before generating recommendations"
             );
         }

         if (classification.getPatientId() != patient.getPatientId()) {
             throw new IllegalArgumentException(
                     "Classification does not belong to this patient"
             );
         }


         RecommendationResult result =
                 new RecommendationResult(patient.getPatientId());

         KieSession kieSession = kieContainer.newKieSession();


         try {
             kieSession.insert(patient);
             kieSession.insert(classification);
             kieSession.insert(result);

             int firedRules = kieSession.fireAllRules();

             System.out.println(
                     "Patient " + patient.getPatientId()
                             + " -> Recommendation rules fired: "
                             + firedRules
             );

             return result;

         } finally {
             kieSession.dispose();
         }
     }



 }

