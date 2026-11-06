
import model.*;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieSession;

import static org.junit.jupiter.api.Assertions.*;

public class ClinicalInferenceTest {

    @Test
    void suspiciousNodeShouldGenerateClinicalFinding() {

        Patient patient = new Patient(1001);

        patient.getClinicalContext()
                .setSuspiciousRegionalNode(true);

        KieSession session = KieServices.Factory.get()
                .getKieClasspathContainer()
                .newKieSession();

        try {
            session.insert(patient);
            session.fireAllRules();

            long findings = session.getObjects().stream()
                    .filter(obj -> obj instanceof ClinicalFinding)
                    .map(obj -> (ClinicalFinding) obj)
                    .filter(f -> f.getPatientId() == 1001)
                    .filter(f -> f.getType().equals("SUSPECTED_NODAL_DISEASE"))
                    .count();

            assertEquals(1, findings);

        } finally {
            session.dispose();
        }
    }


    @Test
    void clinicallyNodeNegativeShouldGenerateFinding() {

        Patient patient = new Patient(1002);

        patient.setRegionalDisease(
                new RegionalDisease(
                        0, false, false,
                        false, false, false
                )
        );

        patient.setDistantDisease(
                new DistantDisease(
                        false, false, false,
                        false, false,
                        LdhStatus.NOT_RECORDED
                )
        );

        patient.getClinicalContext()
                .setSuspiciousRegionalNode(false);

        KieSession session = KieServices.Factory.get()
                .getKieClasspathContainer()
                .newKieSession();

        try {
            session.insert(patient);
            session.fireAllRules();

            long findings = session.getObjects().stream()
                    .filter(obj -> obj instanceof ClinicalFinding)
                    .map(obj -> (ClinicalFinding) obj)
                    .filter(f -> f.getPatientId() == 1002)
                    .filter(f -> f.getType().equals("CLINICALLY_NODE_NEGATIVE"))
                    .count();

            assertEquals(1, findings);

        } finally {
            session.dispose();
        }
    }


    @Test
    void nodeNegativePatientShouldMeetSLNBCriteria() {

        Patient patient = new Patient(1003);

        patient.setPrimaryTumor(
                new PrimaryTumor(1.5, false)
        );

        patient.setRegionalDisease(
                new RegionalDisease(
                        0, false, false,
                        false, false, false
                )
        );

        patient.setDistantDisease(
                new DistantDisease(
                        false, false, false,
                        false, false,
                        LdhStatus.NOT_RECORDED
                )
        );

        patient.getClinicalContext()
                .setSuspiciousRegionalNode(false);

        KieSession session = KieServices.Factory.get()
                .getKieClasspathContainer()
                .newKieSession();

        try {
            session.insert(patient);
            session.fireAllRules();

            long nodeNegative = session.getObjects().stream()
                    .filter(obj -> obj instanceof ClinicalFinding)
                    .map(obj -> (ClinicalFinding) obj)
                    .filter(f -> f.getPatientId() == 1003)
                    .filter(f -> f.getType().equals("CLINICALLY_NODE_NEGATIVE"))
                    .count();

            long slnbCriteria = session.getObjects().stream()
                    .filter(obj -> obj instanceof ClinicalFinding)
                    .map(obj -> (ClinicalFinding) obj)
                    .filter(f -> f.getPatientId() == 1003)
                    .filter(f -> f.getType().equals("SLNB_CRITERIA_MET"))
                    .count();

            assertEquals(1, nodeNegative);
            assertEquals(1, slnbCriteria);

        } finally {
            session.dispose();
        }
    }


    @Test
    void stageIIBShouldGenerateStagingAssessmentFinding() {

        Patient patient = new Patient(1004);

        patient.setPrimaryTumor(
                new PrimaryTumor(3.0, true)
        );

        patient.setRegionalDisease(
                new RegionalDisease(
                        0, false, false,
                        false, false, false
                )
        );

        patient.setDistantDisease(
                new DistantDisease(
                        false, false, false,
                        false, false,
                        LdhStatus.NOT_RECORDED
                )
        );

        MelanomaExpertSystem system = new MelanomaExpertSystem();
        MelanomaClassification classification = system.classify(patient);

        assertEquals(MelanomaStage.IIB, classification.getStage());

        KieSession session = KieServices.Factory.get()
                .getKieClasspathContainer()
                .newKieSession();

        try {
            session.insert(patient);
            session.insert(classification);
            session.fireAllRules();

            long findings = session.getObjects().stream()
                    .filter(obj -> obj instanceof ClinicalFinding)
                    .map(obj -> (ClinicalFinding) obj)
                    .filter(f -> f.getPatientId() == 1004)
                    .filter(f -> f.getType().equals(
                            "ADVANCED_STAGING_ASSESSMENT_NEEDED"))
                    .count();

            assertEquals(1, findings);

        } finally {
            session.dispose();
        }
    }


    @Test
    void patientWithoutEvidenceOfDiseaseShouldGenerateFollowUpFinding() {

        Patient patient = new Patient(1005);

        patient.getClinicalContext()
                .setSurgeryStatus(SurgeryStatus.COMPLETE);

        patient.getClinicalContext()
                .setDiseaseStatus(DiseaseStatus.NO_EVIDENCE_OF_DISEASE);

        patient.getClinicalContext()
                .setNewSymptoms(false);

        patient.getClinicalContext()
                .setSuspiciousRegionalNode(false);

        KieSession session = KieServices.Factory.get()
                .getKieClasspathContainer()
                .newKieSession();

        try {
            session.insert(patient);
            session.fireAllRules();

            long findings = session.getObjects().stream()
                    .filter(obj -> obj instanceof ClinicalFinding)
                    .map(obj -> (ClinicalFinding) obj)
                    .filter(f -> f.getPatientId() == 1005)
                    .filter(f -> f.getType().equals(
                            "ROUTINE_FOLLOW_UP_SCENARIO"))
                    .count();

            assertEquals(1, findings);

        } finally {
            session.dispose();
        }
    }



}
