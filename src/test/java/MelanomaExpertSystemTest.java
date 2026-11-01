
import model.*;
import org.junit.jupiter.api.Test;
import model.RecommendationResult;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

public class MelanomaExpertSystemTest {

    @Test
    void invasiveMelanomaShouldBeStageIB() {

        Patient patient = new Patient(1001);

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

        MelanomaExpertSystem system = new MelanomaExpertSystem();
        MelanomaClassification result = system.classify(patient);

        assertEquals(TCategory.T2A, result.getTCategory());
        assertEquals(NCategory.N0, result.getNCategory());
        assertEquals(MCategory.M0, result.getMCategory());
        assertEquals(MelanomaStage.IB, result.getStage());
    }

    @Test
    void melanomaInSituShouldBeStageZero() {

        Patient patient = new Patient(1002);

        patient.setPrimaryTumor(
                PrimaryTumor.inSitu()
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
        MelanomaClassification result = system.classify(patient);

        assertEquals(TCategory.TIS, result.getTCategory());
        assertEquals(NCategory.N0, result.getNCategory());
        assertEquals(MCategory.M0, result.getMCategory());
        assertEquals(MelanomaStage.ZERO, result.getStage());
    }


    @Test
    void invasiveMelanomaShouldRejectZeroBreslow() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PrimaryTumor(0.0, false)
        );
    }

    @Test
    void breslowBoundariesShouldBeClassifiedCorrectly() {

        double[] breslowValues = {
                0.79, 0.80, 1.00, 1.01,
                2.00, 2.01, 4.00, 4.01
        };

        TCategory[] expectedCategories = {
                TCategory.T1A,
                TCategory.T1B,
                TCategory.T1B,
                TCategory.T2A,
                TCategory.T2A,
                TCategory.T3A,
                TCategory.T3A,
                TCategory.T4A
        };

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        for (int i = 0; i < breslowValues.length; i++) {

            Patient patient = new Patient(2000 + i);

            patient.setPrimaryTumor(
                    new PrimaryTumor(breslowValues[i], false)
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

            MelanomaClassification result = system.classify(patient);

            assertEquals(
                    expectedCategories[i],
                    result.getTCategory(),
                    "Incorrect T category for Breslow = "
                            + breslowValues[i] + " mm"
            );
        }
    }


    @Test
    void breslowBoundariesWithUlcerationShouldBeClassifiedCorrectly() {

        double[] breslowValues = {
                0.79, 0.80, 1.00, 1.01,
                2.00, 2.01, 4.00, 4.01
        };

        TCategory[] expectedCategories = {
                TCategory.T1B,
                TCategory.T1B,
                TCategory.T1B,
                TCategory.T2B,
                TCategory.T2B,
                TCategory.T3B,
                TCategory.T3B,
                TCategory.T4B
        };

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        for (int i = 0; i < breslowValues.length; i++) {

            Patient patient = new Patient(3000 + i);

            patient.setPrimaryTumor(
                    new PrimaryTumor(breslowValues[i], true)
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

            MelanomaClassification result = system.classify(patient);

            assertEquals(
                    expectedCategories[i],
                    result.getTCategory(),
                    "Incorrect T category for ulcerated melanoma with Breslow = "
                            + breslowValues[i] + " mm"
            );
        }
    }

    @Test
    void oneClinicallyOccultNodeShouldBeN1A() {

        Patient patient = new Patient(4001);

        patient.setPrimaryTumor(
                new PrimaryTumor(1.5, false)
        );

        patient.setRegionalDisease(
                new RegionalDisease(
                        1, false, false,
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
        MelanomaClassification result = system.classify(patient);

        assertEquals(NCategory.N1A, result.getNCategory());
        assertEquals(MelanomaStage.IIIA, result.getStage());
    }


    @Test
    void oneClinicallyDetectedNodeShouldBeN1B() {

        Patient patient = new Patient(4002);

        patient.setPrimaryTumor(
                new PrimaryTumor(1.5, false)
        );

        patient.setRegionalDisease(
                new RegionalDisease(
                        1, true, false,
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
        MelanomaClassification result = system.classify(patient);

        assertEquals(TCategory.T2A, result.getTCategory());
        assertEquals(NCategory.N1B, result.getNCategory());
        assertEquals(MCategory.M0, result.getMCategory());
        assertEquals(MelanomaStage.IIIB, result.getStage());
    }


    @ParameterizedTest(name = "Regional disease case {index}: expected {7}")
    @CsvSource({
            // nodes, clinicallyDetected, inTransit, satellite,
            // microsatellite, mattedNodes, expectedN, expectedStage

            "0, false, false, false, false, false, N0,  IB",
            "1, false, false, false, false, false, N1A, IIIA",
            "1, true,  false, false, false, false, N1B, IIIB",
            "0, false, true,  false, false, false, N1C, IIIB",
            "2, false, false, false, false, false, N2A, IIIA",
            "2, true,  false, false, false, false, N2B, IIIB",
            "1, false, true,  false, false, false, N2C, IIIC",
            "4, false, false, false, false, false, N3A, IIIC",
            "4, true,  false, false, false, false, N3B, IIIC",
            "1, true,  false, false, false, true,  N3B, IIIC",
            "2, false, true,  false, false, false, N3C, IIIC",
            "2, true, true, false, false, true, N3C, IIIC"

    })
    void regionalDiseaseShouldBeClassifiedCorrectly(
            int nodes,
            boolean clinicallyDetected,
            boolean inTransit,
            boolean satellite,
            boolean microsatellite,
            boolean mattedNodes,
            NCategory expectedN,
            MelanomaStage expectedStage) {

        Patient patient = new Patient(5000 + nodes);

        patient.setPrimaryTumor(
                new PrimaryTumor(1.5, false)
        );

        patient.setRegionalDisease(
                new RegionalDisease(
                        nodes,
                        clinicallyDetected,
                        inTransit,
                        satellite,
                        microsatellite,
                        mattedNodes
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
        MelanomaClassification result = system.classify(patient);

        assertEquals(
                TCategory.T2A,
                result.getTCategory()
        );

        assertEquals(
                expectedN,
                result.getNCategory(),
                "Incorrect N category"
        );

        assertEquals(
                MCategory.M0,
                result.getMCategory()
        );

        assertEquals(
                expectedStage,
                result.getStage(),
                "Incorrect pathological stage for " + expectedN
        );
    }

    @Test
    void distantSkinMetastasisShouldBeM1A() {

        Patient patient = new Patient(6001);

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
                        true,  // Distant metastasis present
                        true,  // Skin, soft tissue or nonregional nodes
                        false, // Lung
                        false, // Non-CNS visceral
                        false, // CNS
                        LdhStatus.NOT_RECORDED
                )
        );

        MelanomaExpertSystem system = new MelanomaExpertSystem();
        MelanomaClassification result = system.classify(patient);

        assertEquals(TCategory.T2A, result.getTCategory());
        assertEquals(NCategory.N0, result.getNCategory());
        assertEquals(MCategory.M1A, result.getMCategory());
        assertEquals(MelanomaStage.IV, result.getStage());
    }


    @ParameterizedTest(name = "M category: {6}")
    @CsvSource({
            // Skin, Lung, Visceral, CNS, LDH, Expected M, Description

            "true, false, false, false, NOT_RECORDED, M1A, Skin",
            "true, false, false, false, NOT_ELEVATED, M1A_0, Skin",
            "true, false, false, false, ELEVATED, M1A_1, Skin",

            "false, true, false, false, NOT_RECORDED, M1B, Lung",
            "false, true, false, false, NOT_ELEVATED, M1B_0, Lung",
            "false, true, false, false, ELEVATED, M1B_1, Lung",

            "false, false, true, false, NOT_RECORDED, M1C, Visceral",
            "false, false, true, false, NOT_ELEVATED, M1C_0, Visceral",
            "false, false, true, false, ELEVATED, M1C_1, Visceral",

            "false, false, false, true, NOT_RECORDED, M1D, CNS",
            "false, false, false, true, NOT_ELEVATED, M1D_0, CNS",
            "false, false, false, true, ELEVATED, M1D_1, CNS"
    })
    void distantMetastasisShouldBeClassifiedCorrectly(
            boolean skin,
            boolean lung,
            boolean visceral,
            boolean cns,
            LdhStatus ldh,
            MCategory expectedM,
            String description) {

        Patient patient = new Patient(7001);

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
                        true,
                        skin,
                        lung,
                        visceral,
                        cns,
                        ldh
                )
        );

        MelanomaExpertSystem system = new MelanomaExpertSystem();
        MelanomaClassification result = system.classify(patient);

        assertEquals(
                expectedM,
                result.getMCategory(),
                "Incorrect M category for " + description
        );

        assertEquals(
                MelanomaStage.IV,
                result.getStage(),
                "Incorrect stage for " + description
        );
    }


    @ParameterizedTest(name = "Multiple metastases: {5}")
    @CsvSource({
            // Skin, Lung, Visceral, CNS, Expected M, Description
            "true, true, false, false, M1B_0, Skin and lung",
            "true, true, true, false, M1C_0, Skin lung and visceral",
            "false, true, true, false, M1C_0, Lung and visceral",
            "false, true, false, true, M1D_0, Lung and CNS",
            "true, true, true, true, M1D_0, All locations"
    })
    void multipleMetastaticSitesShouldUseHighestMCategory(
            boolean skin,
            boolean lung,
            boolean visceral,
            boolean cns,
            MCategory expectedM,
            String description) {

        Patient patient = new Patient(8001);

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
                        true,
                        skin,
                        lung,
                        visceral,
                        cns,
                        LdhStatus.NOT_ELEVATED
                )
        );

        MelanomaExpertSystem system = new MelanomaExpertSystem();
        MelanomaClassification result = system.classify(patient);

        assertEquals(
                expectedM,
                result.getMCategory(),
                "Incorrect M category for " + description
        );

        assertEquals(
                MelanomaStage.IV,
                result.getStage()
        );
    }

    @Test
    void contradictoryDistantMetastasisDataShouldBeRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new DistantDisease(
                        false, // No distant metastasis
                        false, // Skin
                        true,  // Lung metastasis: contradiction
                        false, // Visceral
                        false, // CNS
                        LdhStatus.NOT_RECORDED
                )
        );
    }

    @Test
    void distantMetastasisWithoutLocationShouldNotBeClassified() {

        Patient patient = new Patient(9001);

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
                        true,  // Distant metastasis confirmed
                        false, // Skin
                        false, // Lung
                        false, // Non-CNS visceral
                        false, // CNS
                        LdhStatus.NOT_RECORDED
                )
        );

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        assertThrows(
                IllegalStateException.class,
                () -> system.classify(patient)
        );
    }

    @ParameterizedTest(name = "Breslow {0} mm, ulceration {1} -> Stage {2}")
    @CsvSource({
            "0.5, false, IA",
            "0.9, true, IA",
            "1.5, false, IB",
            "1.5, true, IIA",
            "3.0, false, IIA",
            "3.0, true, IIB",
            "5.0, false, IIB",
            "5.0, true, IIC"
    })
    void localizedMelanomaShouldHaveCorrectPathologicalStage(
            double breslow,
            boolean ulceration,
            MelanomaStage expectedStage) {

        Patient patient = new Patient(10001);

        patient.setPrimaryTumor(
                new PrimaryTumor(breslow, ulceration)
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
        MelanomaClassification result = system.classify(patient);

        assertEquals(expectedStage, result.getStage());
        assertEquals(NCategory.N0, result.getNCategory());
        assertEquals(MCategory.M0, result.getMCategory());
    }

    @ParameterizedTest(name = "Stage III case {index}: expected {8}")
    @CsvSource({
            // Breslow, Ulceration, Nodes, Clinically detected,
            // In-transit, Satellite, Microsatellite, Matted, Expected stage

            "1.5, false, 1, false, false, false, false, false, IIIA",
            "1.5, false, 1, true, false, false, false, false, IIIB",
            "3.0, true, 1, false, false, false, false, false, IIIC",
            "1.5, false, 2, false, true, false, false, false, IIIC",
            "5.0, true, 1, true, false, false, false, false, IIIC",
            "5.0, true, 4, true, false, false, false, false, IIID"
    })
    void stageIIIShouldBeClassifiedCorrectly(
            double breslow,
            boolean ulceration,
            int nodes,
            boolean clinicallyDetected,
            boolean inTransit,
            boolean satellite,
            boolean microsatellite,
            boolean matted,
            MelanomaStage expectedStage) {

        Patient patient = new Patient(11001);

        patient.setPrimaryTumor(
                new PrimaryTumor(breslow, ulceration)
        );

        patient.setRegionalDisease(
                new RegionalDisease(
                        nodes,
                        clinicallyDetected,
                        inTransit,
                        satellite,
                        microsatellite,
                        matted
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
        MelanomaClassification result = system.classify(patient);

        assertEquals(
                expectedStage,
                result.getStage(),
                "Incorrect stage for T=" + result.getTCategory()
                        + ", N=" + result.getNCategory()
        );

        assertEquals(MCategory.M0, result.getMCategory());
    }


    @ParameterizedTest(name = "T0 case {index}: expected {6}")
    @CsvSource({
            // Nodes, Clinically detected, In-transit,
            // Satellite, Microsatellite, Matted, Expected stage

            "1, true, false, false, false, false, IIIB",
            "0, false, true, false, false, false, IIIB",
            "2, true, false, false, false, false, IIIC",
            "1, false, true, false, false, false, IIIC",
            "4, true, false, false, false, false, IIIC"
    })
    void melanomaWithoutIdentifiablePrimaryShouldHaveCorrectStage(
            int nodes,
            boolean clinicallyDetected,
            boolean inTransit,
            boolean satellite,
            boolean microsatellite,
            boolean matted,
            MelanomaStage expectedStage) {

        Patient patient = new Patient(12001);

        patient.setPrimaryTumor(
                PrimaryTumor.noPrimaryTumor()
        );

        patient.setRegionalDisease(
                new RegionalDisease(
                        nodes,
                        clinicallyDetected,
                        inTransit,
                        satellite,
                        microsatellite,
                        matted
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
        MelanomaClassification result = system.classify(patient);

        assertEquals(TCategory.T0, result.getTCategory());
        assertEquals(expectedStage, result.getStage());
    }


    @Test
    void melanomaWithUnassessablePrimaryShouldNotReceiveStage() {

        Patient patient = new Patient(13001);

        patient.setPrimaryTumor(
                PrimaryTumor.cannotAssess()
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

        assertThrows(
                IllegalStateException.class,
                () -> system.classify(patient)
        );
    }


    @Test
    void allInvasiveTAndNCategoriesShouldHaveStage() {

        double[] breslowValues = {
                0.5, 0.5,
                1.5, 1.5,
                3.0, 3.0,
                5.0, 5.0
        };

        boolean[] ulcerations = {
                false, true,
                false, true,
                false, true,
                false, true
        };

        RegionalDisease[] regionalCases = {
                new RegionalDisease(0, false, false, false, false, false),
                new RegionalDisease(1, false, false, false, false, false),
                new RegionalDisease(1, true, false, false, false, false),
                new RegionalDisease(0, false, true, false, false, false),
                new RegionalDisease(2, false, false, false, false, false),
                new RegionalDisease(2, true, false, false, false, false),
                new RegionalDisease(1, false, true, false, false, false),
                new RegionalDisease(4, false, false, false, false, false),
                new RegionalDisease(4, true, false, false, false, false),
                new RegionalDisease(2, false, true, false, false, false)
        };

        MelanomaStage[][] expectedStages = {
                {MelanomaStage.IA, MelanomaStage.IIIA, MelanomaStage.IIIB,
                        MelanomaStage.IIIB, MelanomaStage.IIIA, MelanomaStage.IIIB,
                        MelanomaStage.IIIC, MelanomaStage.IIIC, MelanomaStage.IIIC,
                        MelanomaStage.IIIC},

                {MelanomaStage.IA, MelanomaStage.IIIA, MelanomaStage.IIIB,
                        MelanomaStage.IIIB, MelanomaStage.IIIA, MelanomaStage.IIIB,
                        MelanomaStage.IIIC, MelanomaStage.IIIC, MelanomaStage.IIIC,
                        MelanomaStage.IIIC},

                {MelanomaStage.IB, MelanomaStage.IIIA, MelanomaStage.IIIB,
                        MelanomaStage.IIIB, MelanomaStage.IIIA, MelanomaStage.IIIB,
                        MelanomaStage.IIIC, MelanomaStage.IIIC, MelanomaStage.IIIC,
                        MelanomaStage.IIIC},

                {MelanomaStage.IIA, MelanomaStage.IIIB, MelanomaStage.IIIB,
                        MelanomaStage.IIIB, MelanomaStage.IIIB, MelanomaStage.IIIB,
                        MelanomaStage.IIIC, MelanomaStage.IIIC, MelanomaStage.IIIC,
                        MelanomaStage.IIIC},

                {MelanomaStage.IIA, MelanomaStage.IIIB, MelanomaStage.IIIB,
                        MelanomaStage.IIIB, MelanomaStage.IIIB, MelanomaStage.IIIB,
                        MelanomaStage.IIIC, MelanomaStage.IIIC, MelanomaStage.IIIC,
                        MelanomaStage.IIIC},

                {MelanomaStage.IIB, MelanomaStage.IIIC, MelanomaStage.IIIC,
                        MelanomaStage.IIIC, MelanomaStage.IIIC, MelanomaStage.IIIC,
                        MelanomaStage.IIIC, MelanomaStage.IIIC, MelanomaStage.IIIC,
                        MelanomaStage.IIIC},

                {MelanomaStage.IIB, MelanomaStage.IIIC, MelanomaStage.IIIC,
                        MelanomaStage.IIIC, MelanomaStage.IIIC, MelanomaStage.IIIC,
                        MelanomaStage.IIIC, MelanomaStage.IIIC, MelanomaStage.IIIC,
                        MelanomaStage.IIIC},

                {MelanomaStage.IIC, MelanomaStage.IIIC, MelanomaStage.IIIC,
                        MelanomaStage.IIIC, MelanomaStage.IIIC, MelanomaStage.IIIC,
                        MelanomaStage.IIIC, MelanomaStage.IIID, MelanomaStage.IIID,
                        MelanomaStage.IIID}
        };

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        for (int t = 0; t < breslowValues.length; t++) {
            for (int n = 0; n < regionalCases.length; n++) {

                Patient patient = new Patient(20000 + t * 10 + n);

                patient.setPrimaryTumor(
                        new PrimaryTumor(
                                breslowValues[t],
                                ulcerations[t]
                        )
                );

                patient.setRegionalDisease(regionalCases[n]);

                patient.setDistantDisease(
                        new DistantDisease(
                                false, false, false,
                                false, false,
                                LdhStatus.NOT_RECORDED
                        )
                );

                MelanomaClassification result = system.classify(patient);

                assertEquals(
                        expectedStages[t][n],
                        result.getStage(),
                        "Incorrect AJCC8 stage for T="
                                + result.getTCategory()
                                + ", N=" + result.getNCategory()
                                + ", M=" + result.getMCategory()
                );
            }
        }
    }


    @ParameterizedTest(name = "AJCC8: T={0}, N={2} -> {3}")
    @CsvSource({
            // Breslow, Ulceration, Nodes, Clinically detected, Expected stage
            "1.5, false, 1, false, IIIA",
            "1.5, false, 1, true, IIIB",
            "3.0, false, 1, false, IIIB",
            "3.0, true, 1, false, IIIC",
            "5.0, false, 1, true, IIIC",
            "5.0, true, 4, true, IIID"
    })
    void pathologicalStageShouldMatchExpectedAJCC8Stage(
            double breslow,
            boolean ulceration,
            int nodes,
            boolean clinicallyDetected,
            MelanomaStage expectedStage) {

        Patient patient = new Patient(30001);

        patient.setPrimaryTumor(
                new PrimaryTumor(breslow, ulceration)
        );

        patient.setRegionalDisease(
                new RegionalDisease(
                        nodes,
                        clinicallyDetected,
                        false, false, false, false
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
        MelanomaClassification result = system.classify(patient);

        assertEquals(
                expectedStage,
                result.getStage(),
                "Incorrect AJCC8 pathological stage for "
                        + result.getTCategory()
                        + " " + result.getNCategory()
                        + " " + result.getMCategory()
        );
    }



    @Test
    void stageZeroRecommendationShouldOnlyApplyToStageZero() {

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        // ==========================================
        // PATIENT 1: MELANOMA IN SITU (STAGE 0)
        // ==========================================

        Patient patientZero = new Patient(30001);

        patientZero.setPrimaryTumor(PrimaryTumor.inSitu());

        patientZero.setRegionalDisease(
                new RegionalDisease(
                        0, false, false,
                        false, false, false
                )
        );

        patientZero.setDistantDisease(
                new DistantDisease(
                        false, false, false,
                        false, false,
                        LdhStatus.NOT_RECORDED
                )
        );

        // Surgery status is unknown
        patientZero.getClinicalContext()
                .setSurgeryStatus(SurgeryStatus.UNKNOWN);

        MelanomaClassification classificationZero =
                system.classify(patientZero);

        assertEquals(
                MelanomaStage.ZERO,
                classificationZero.getStage()
        );

        RecommendationResult recommendationsZero =
                system.generateRecommendations(
                        patientZero,
                        classificationZero
                );

        // Stage 0 + UNKNOWN surgery -> STAGE-00C
        assertTrue(
                recommendationsZero.getRecommendations()
                        .stream()
                        .anyMatch(r ->
                                "STAGE-00C".equals(r.getRuleId())
                        ),
                "STAGE-00C should be generated for stage 0 with unknown surgery status"
        );

        // Only one STAGE-00 recommendation should be generated
        long stageZeroCount =
                recommendationsZero.getRecommendations()
                        .stream()
                        .filter(r ->
                                r.getRuleId().startsWith("STAGE-00")
                        )
                        .count();

        assertEquals(
                1,
                stageZeroCount,
                "Exactly one STAGE-00 recommendation should be generated"
        );

        // ==========================================
        // PATIENT 2: INVASIVE MELANOMA (STAGE IA)
        // ==========================================

        Patient patientIA = new Patient(30002);

        patientIA.setPrimaryTumor(
                new PrimaryTumor(0.5, false)
        );

        patientIA.setRegionalDisease(
                new RegionalDisease(
                        0, false, false,
                        false, false, false
                )
        );

        patientIA.setDistantDisease(
                new DistantDisease(
                        false, false, false,
                        false, false,
                        LdhStatus.NOT_RECORDED
                )
        );

        patientIA.getClinicalContext()
                .setSurgeryStatus(SurgeryStatus.UNKNOWN);

        MelanomaClassification classificationIA =
                system.classify(patientIA);

        assertEquals(
                MelanomaStage.IA,
                classificationIA.getStage()
        );

        RecommendationResult recommendationsIA =
                system.generateRecommendations(
                        patientIA,
                        classificationIA
                );

        // No STAGE-00 rule should apply to stage IA
        assertFalse(
                recommendationsIA.getRecommendations()
                        .stream()
                        .anyMatch(r ->
                                r.getRuleId().startsWith("STAGE-00")
                        ),
                "STAGE-00 recommendations must not be generated for stage IA"
        );
    }


    @Test
    void stageIBShouldGenerateDiagnosticRecommendation() {

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        // Patient with stage IB melanoma
        Patient patientIB = new Patient(30003);

        patientIB.setPrimaryTumor(
                new PrimaryTumor(1.5, false)
        );

        patientIB.setRegionalDisease(
                new RegionalDisease(0, false, false, false, false, false)
        );

        patientIB.setDistantDisease(
                new DistantDisease(
                        false, false, false,
                        false, false,
                        LdhStatus.NOT_RECORDED
                )
        );

        // Configure clinical context BEFORE classification
        patientIB.getClinicalContext()
                .setRegionalNodeUltrasoundStatus(
                        BaselineImagingStatus.NOT_PERFORMED
                );

        patientIB.getClinicalContext()
                .setSuspiciousRegionalNode(Boolean.FALSE);

        MelanomaClassification classificationIB =
                system.classify(patientIB);

        assertEquals(
                MelanomaStage.IB,
                classificationIB.getStage()
        );

        RecommendationResult recommendationsIB =
                system.generateRecommendations(
                        patientIB,
                        classificationIB
                );

        assertTrue(
                recommendationsIB.getRecommendations()
                        .stream()
                        .anyMatch(r -> "DIAG-01".equals(r.getRuleId())),
                "DIAG-01 should be generated for stage IB"
        );

        // Patient with stage IA melanoma
        Patient patientIA = new Patient(30004);

        patientIA.setPrimaryTumor(
                new PrimaryTumor(0.5, false)
        );

        patientIA.setRegionalDisease(
                new RegionalDisease(0, false, false, false, false, false)
        );

        patientIA.setDistantDisease(
                new DistantDisease(
                        false, false, false,
                        false, false,
                        LdhStatus.NOT_RECORDED
                )
        );

        patientIA.getClinicalContext()
                .setSuspiciousRegionalNode(Boolean.FALSE);

        MelanomaClassification classificationIA =
                system.classify(patientIA);

        assertEquals(
                MelanomaStage.IA,
                classificationIA.getStage()
        );

        RecommendationResult recommendationsIA =
                system.generateRecommendations(
                        patientIA,
                        classificationIA
                );

        assertFalse(
                recommendationsIA.getRecommendations()
                        .stream()
                        .anyMatch(r -> "DIAG-01".equals(r.getRuleId())),
                "DIAG-01 must not be generated for stage IA"
        );
    }

    @Test
    void stageIBUltrasoundRecommendationShouldDependOnImagingStatus() {

        BaselineImagingStatus[] statuses = {
                BaselineImagingStatus.NOT_PERFORMED,
                BaselineImagingStatus.COMPLETE,
                BaselineImagingStatus.UNKNOWN
        };

        boolean[] expectedRecommendation = {
                true,
                false,
                false
        };

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        for (int i = 0; i < statuses.length; i++) {

            Patient patient = new Patient(31000 + i);

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
                    .setRegionalNodeUltrasoundStatus(statuses[i]);

            patient.getClinicalContext()
                    .setSuspiciousRegionalNode(Boolean.FALSE);

            MelanomaClassification classification =
                    system.classify(patient);


            assertEquals(
                    MelanomaStage.IB,
                    classification.getStage()
            );

            RecommendationResult recommendations =
                    system.generateRecommendations(
                            patient,
                            classification
                    );

            boolean hasDiag01 = recommendations.getRecommendations()
                    .stream()
                    .anyMatch(r -> "DIAG-01".equals(r.getRuleId()));

            assertEquals(
                    expectedRecommendation[i],
                    hasDiag01,
                    "Unexpected DIAG-01 result for ultrasound status "
                            + statuses[i]
            );
        }
    }

    @Test
    void stageIBShouldVerifyUnknownUltrasoundStatus() {

        BaselineImagingStatus[] statuses = {
                BaselineImagingStatus.UNKNOWN,
                BaselineImagingStatus.NOT_PERFORMED,
                BaselineImagingStatus.COMPLETE
        };

        boolean[] expectedData01 = {true, false, false};
        boolean[] expectedDiag01 = {false, true, false};

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        for (int i = 0; i < statuses.length; i++) {

            Patient patient = new Patient(32000 + i);

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
                    .setRegionalNodeUltrasoundStatus(statuses[i]);

            patient.getClinicalContext()
                    .setSuspiciousRegionalNode(Boolean.FALSE);

            MelanomaClassification classification =
                    system.classify(patient);


            assertEquals(
                    MelanomaStage.IB,
                    classification.getStage()
            );

            RecommendationResult recommendations =
                    system.generateRecommendations(
                            patient,
                            classification
                    );

            boolean hasData01 = recommendations.getRecommendations()
                    .stream()
                    .anyMatch(r -> "DATA-01".equals(r.getRuleId()));

            boolean hasDiag01 = recommendations.getRecommendations()
                    .stream()
                    .anyMatch(r -> "DIAG-01".equals(r.getRuleId()));

            assertEquals(
                    expectedData01[i],
                    hasData01,
                    "Unexpected DATA-01 for status " + statuses[i]
            );

            assertEquals(
                    expectedDiag01[i],
                    hasDiag01,
                    "Unexpected DIAG-01 for status " + statuses[i]
            );
        }
    }


    @Test
    void stageIIBShouldGenerateBaselineImagingRecommendation() {

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        BaselineImagingStatus[] imagingStatuses = {
                BaselineImagingStatus.NOT_PERFORMED,
                BaselineImagingStatus.COMPLETE,
                BaselineImagingStatus.NOT_PERFORMED
        };

        boolean[] expected = {true, false, false};

        for (int i = 0; i < imagingStatuses.length; i++) {

            Patient patient = new Patient(33000 + i);

            // First two patients: T3b N0 M0 -> Stage IIB
            // Third patient: T1a N0 M0 -> Stage IA
            if (i < 2) {
                patient.setPrimaryTumor(
                        new PrimaryTumor(3.0, true)
                );
            } else {
                patient.setPrimaryTumor(
                        new PrimaryTumor(0.5, false)
                );
            }

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

            // Set clinical data BEFORE classification
            patient.getClinicalContext()
                    .setBaselineImagingStatus(imagingStatuses[i]);

            MelanomaClassification classification =
                    system.classify(patient);

            assertEquals(
                    i < 2 ? MelanomaStage.IIB : MelanomaStage.IA,
                    classification.getStage()
            );

            RecommendationResult recommendations =
                    system.generateRecommendations(
                            patient,
                            classification
                    );

            boolean hasDiag02 = recommendations.getRecommendations()
                    .stream()
                    .anyMatch(r -> "DIAG-02".equals(r.getRuleId()));

            assertEquals(
                    expected[i],
                    hasDiag02,
                    "Unexpected DIAG-02 for stage "
                            + classification.getStage()
                            + " and imaging status "
                            + imagingStatuses[i]
            );
        }
    }


    @Test
    void higherRiskStagesShouldGenerateBaselineImagingRecommendation() {

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        double[] breslowValues = {
                5.0, 0.5, 3.0, 0.5, 1.5
        };

        boolean[] ulcerations = {
                true, false, true, false, true
        };

        RegionalDisease[] regionalCases = {
                new RegionalDisease(0, false, false, false, false, false),
                new RegionalDisease(1, false, false, false, false, false),
                new RegionalDisease(1, true, false, false, false, false),
                new RegionalDisease(0, false, false, false, false, false),
                new RegionalDisease(0, false, false, false, false, false)
        };

        MelanomaStage[] expectedStages = {
                MelanomaStage.IIC,
                MelanomaStage.IIIA,
                MelanomaStage.IIIC,
                MelanomaStage.IV,
                MelanomaStage.IIA
        };

        boolean[] expectedRecommendations = {
                true, true, true, true, false
        };

        for (int i = 0; i < expectedStages.length; i++) {

            Patient patient = new Patient(34000 + i);

            patient.setPrimaryTumor(
                    new PrimaryTumor(
                            breslowValues[i],
                            ulcerations[i]
                    )
            );

            patient.setRegionalDisease(regionalCases[i]);

            boolean hasDistantMetastasis = (i == 3);

            patient.setDistantDisease(
                    new DistantDisease(
                            hasDistantMetastasis,
                            false,
                            hasDistantMetastasis,
                            false,
                            false,
                            LdhStatus.NOT_RECORDED
                    )
            );

            patient.getClinicalContext()
                    .setBaselineImagingStatus(
                            BaselineImagingStatus.NOT_PERFORMED
                    );

            MelanomaClassification classification =
                    system.classify(patient);

            assertEquals(
                    expectedStages[i],
                    classification.getStage(),
                    "Unexpected stage for patient " + patient.getPatientId()
            );

            RecommendationResult recommendations =
                    system.generateRecommendations(
                            patient,
                            classification
                    );

            boolean hasDiag02 = recommendations.getRecommendations()
                    .stream()
                    .anyMatch(r -> "DIAG-02".equals(r.getRuleId()));

            assertEquals(
                    expectedRecommendations[i],
                    hasDiag02,
                    "Unexpected DIAG-02 for stage "
                            + classification.getStage()
            );
        }
    }


    @Test
    void brafRecommendationShouldDependOnStageAndMutationStatus() {

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        BrafStatus[] brafStatuses = {
                BrafStatus.UNKNOWN,
                BrafStatus.MUTATED,
                BrafStatus.NOT_MUTATED,
                BrafStatus.UNKNOWN,
                BrafStatus.UNKNOWN
        };

        MelanomaStage[] expectedStages = {
                MelanomaStage.IIIA,
                MelanomaStage.IIIA,
                MelanomaStage.IIIA,
                MelanomaStage.IV,
                MelanomaStage.IA
        };

        boolean[] expectedRecommendations = {
                true, false, false, true, false
        };

        for (int i = 0; i < brafStatuses.length; i++) {

            Patient patient = new Patient(35000 + i);

            patient.setPrimaryTumor(
                    new PrimaryTumor(0.5, false)
            );

            // One occult regional node produces stage IIIA
            boolean hasRegionalNode = (i < 3);

            patient.setRegionalDisease(
                    new RegionalDisease(
                            hasRegionalNode ? 1 : 0,
                            false,
                            false,
                            false,
                            false,
                            false
                    )
            );

            // Fourth patient has distant lung metastasis: stage IV
            boolean hasDistantMetastasis = (i == 3);

            patient.setDistantDisease(
                    new DistantDisease(
                            hasDistantMetastasis,
                            false,
                            hasDistantMetastasis,
                            false,
                            false,
                            LdhStatus.NOT_RECORDED
                    )
            );

            // Clinical data must be set BEFORE classification
            patient.getClinicalContext()
                    .setBrafStatus(brafStatuses[i]);

            MelanomaClassification classification =
                    system.classify(patient);

            assertEquals(
                    expectedStages[i],
                    classification.getStage(),
                    "Unexpected stage for patient " + i
            );

            RecommendationResult result =
                    system.generateRecommendations(
                            patient,
                            classification
                    );

            boolean hasMol01 = result.getRecommendations()
                    .stream()
                    .anyMatch(r -> "MOL-01".equals(r.getRuleId()));

            assertEquals(
                    expectedRecommendations[i],
                    hasMol01,
                    "Unexpected MOL-01 for stage "
                            + classification.getStage()
                            + " and BRAF status "
                            + brafStatuses[i]
            );
        }
    }




    @Test
    void surgicalRecommendationShouldDependOnStageAndSurgeryStatus() {

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        double[] breslowValues = {
                0.5, 1.5, 0.5, 1.5, 1.5
        };

        boolean[] ulcerations = {
                false, false, false, false, true
        };

        SurgeryStatus[] surgeryStatuses = {
                SurgeryStatus.NOT_PERFORMED,
                SurgeryStatus.INCOMPLETE,
                SurgeryStatus.COMPLETE,
                SurgeryStatus.UNKNOWN,
                SurgeryStatus.NOT_PERFORMED
        };

        MelanomaStage[] expectedStages = {
                MelanomaStage.IA,
                MelanomaStage.IB,
                MelanomaStage.IA,
                MelanomaStage.IB,
                MelanomaStage.IIA
        };

        String[] expectedSurgicalRules = {
                "SURG-01A",
                "SURG-01B",
                "NONE",
                "SURG-01C",
                "SURG-01A"
        };

        for (int i = 0; i < expectedStages.length; i++) {

            Patient patient = new Patient(36000 + i);

            patient.setPrimaryTumor(
                    new PrimaryTumor(
                            breslowValues[i],
                            ulcerations[i]
                    )
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
                    .setSurgeryStatus(surgeryStatuses[i]);

            MelanomaClassification classification =
                    system.classify(patient);

            assertEquals(
                    expectedStages[i],
                    classification.getStage(),
                    "Unexpected stage for patient " + i
            );

            RecommendationResult result =
                    system.generateRecommendations(
                            patient,
                            classification
                    );

            String expectedRule = expectedSurgicalRules[i];

            long surgicalRuleCount =
                    result.getRecommendations()
                            .stream()
                            .filter(r ->
                                    r.getRuleId().startsWith("SURG-01")
                            )
                            .count();

            if ("NONE".equals(expectedRule)) {

                assertEquals(
                        0,
                        surgicalRuleCount,
                        "No surgical recommendation expected for "
                                + surgeryStatuses[i]
                );

            } else {

                assertEquals(
                        1,
                        surgicalRuleCount,
                        "Exactly one surgical recommendation expected"
                );

                assertTrue(
                        result.getRecommendations()
                                .stream()
                                .anyMatch(r ->
                                        expectedRule.equals(r.getRuleId())
                                ),
                        "Expected " + expectedRule
                                + " for stage "
                                + classification.getStage()
                                + " and surgery status "
                                + surgeryStatuses[i]
                );
            }
        }
    }


    @Test
    void sentinelNodeBiopsyRecommendationShouldRespectClinicalCriteria() {

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        double[] breslowValues = {
                1.5, 0.7, 1.5, 1.5, 1.5
        };

        Boolean[] suspiciousNodes = {
                Boolean.FALSE,
                Boolean.FALSE,
                Boolean.TRUE,
                null,
                Boolean.FALSE
        };

        SentinelNodeBiopsyStatus[] biopsyStatuses = {
                SentinelNodeBiopsyStatus.NOT_PERFORMED,
                SentinelNodeBiopsyStatus.NOT_PERFORMED,
                SentinelNodeBiopsyStatus.NOT_PERFORMED,
                SentinelNodeBiopsyStatus.NOT_PERFORMED,
                SentinelNodeBiopsyStatus.NEGATIVE
        };

        boolean[] expectedRecommendations = {
                true, false, false, false, false
        };

        for (int i = 0; i < breslowValues.length; i++) {

            Patient patient = new Patient(37000 + i);

            patient.setPrimaryTumor(
                    new PrimaryTumor(breslowValues[i], false)
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

            // Set clinical information BEFORE classification
            if (suspiciousNodes[i] != null) {
                patient.getClinicalContext()
                        .setSuspiciousRegionalNode(suspiciousNodes[i]);
            }
            patient.getClinicalContext()
                    .setSentinelNodeBiopsyStatus(biopsyStatuses[i]);

            MelanomaClassification classification =
                    system.classify(patient);

            RecommendationResult result =
                    system.generateRecommendations(
                            patient,
                            classification
                    );

            boolean hasDiag03 = result.getRecommendations()
                    .stream()
                    .anyMatch(r -> "DIAG-03A".equals(r.getRuleId()));

            assertEquals(
                    expectedRecommendations[i],
                    hasDiag03,
                    "Unexpected DIAG-03 for Breslow "
                            + breslowValues[i]
                            + ", suspicious node "
                            + suspiciousNodes[i]
                            + ", SLNB status "
                            + biopsyStatuses[i]
            );
        }
    }


    @Test
    void sentinelNodeBiopsyShouldRespectBreslowBoundaries() {

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        double[] breslowValues = {
                0.79, 0.79, 0.80, 1.00, 1.01, 0.50
        };

        boolean[] ulcerations = {
                false, true, false, false, false, false
        };

        boolean[] expectedRecommendations = {
                false, false, false, true, true, false
        };

        for (int i = 0; i < breslowValues.length; i++) {

            Patient patient = new Patient(38000 + i);

            patient.setPrimaryTumor(
                    new PrimaryTumor(
                            breslowValues[i],
                            ulcerations[i]
                    )
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

            // Clinical information BEFORE classification
            patient.getClinicalContext()
                    .setSuspiciousRegionalNode(Boolean.FALSE);

            patient.getClinicalContext()
                    .setSentinelNodeBiopsyStatus(
                            SentinelNodeBiopsyStatus.NOT_PERFORMED
                    );

            MelanomaClassification classification =
                    system.classify(patient);

            RecommendationResult result =
                    system.generateRecommendations(
                            patient,
                            classification
                    );

            boolean hasDiag03 = result.getRecommendations()
                    .stream()
                    .anyMatch(r ->
                            "DIAG-03A".equals(r.getRuleId()) ||
                                    "DIAG-03B".equals(r.getRuleId())
                    );

            assertEquals(
                    expectedRecommendations[i],
                    hasDiag03,
                    "Unexpected SLNB recommendation for Breslow "
                            + breslowValues[i]
                            + " mm and ulceration "
                            + ulcerations[i]
            );
        }
    }


    @Test
    void multidisciplinaryRecommendationShouldApplyToStageIIIAndIV() {

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        MelanomaStage[] expectedStages = {
                MelanomaStage.IA,
                MelanomaStage.IIB,
                MelanomaStage.IIIA,
                MelanomaStage.IV
        };

        boolean[] expectedRecommendations = {
                false, false, true, true
        };

        for (int i = 0; i < expectedStages.length; i++) {

            Patient patient = new Patient(39000 + i);

            // IA and IIIA: thin, non-ulcerated tumor
            // IIB: 3 mm ulcerated tumor
            double breslow = (i == 1) ? 3.0 : 0.5;
            boolean ulceration = (i == 1);

            patient.setPrimaryTumor(
                    new PrimaryTumor(breslow, ulceration)
            );

            // IIIA: one occult positive regional node
            int involvedNodes = (i == 2) ? 1 : 0;

            patient.setRegionalDisease(
                    new RegionalDisease(
                            involvedNodes,
                            false,
                            false,
                            false,
                            false,
                            false
                    )
            );

            // IV: distant lung metastasis
            boolean distantMetastasis = (i == 3);

            patient.setDistantDisease(
                    new DistantDisease(
                            distantMetastasis,
                            false,
                            distantMetastasis,
                            false,
                            false,
                            LdhStatus.NOT_RECORDED
                    )
            );

            MelanomaClassification classification =
                    system.classify(patient);

            assertEquals(
                    expectedStages[i],
                    classification.getStage(),
                    "Unexpected stage for patient " + i
            );

            RecommendationResult result =
                    system.generateRecommendations(
                            patient,
                            classification
                    );

            boolean hasGen03 = result.getRecommendations()
                    .stream()
                    .anyMatch(r -> "GEN-03".equals(r.getRuleId()));

            assertEquals(
                    expectedRecommendations[i],
                    hasGen03,
                    "Unexpected GEN-03 for stage "
                            + classification.getStage()
            );
        }
    }


    @Test
    void followUpRecommendationShouldDependOnDiseaseStatus() {

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        DiseaseStatus[] diseaseStatuses = {
                DiseaseStatus.NO_EVIDENCE_OF_DISEASE,
                DiseaseStatus.NO_EVIDENCE_OF_DISEASE,
                DiseaseStatus.ACTIVE_DISEASE,
                DiseaseStatus.UNKNOWN
        };

        MelanomaStage[] expectedStages = {
                MelanomaStage.IA,
                MelanomaStage.IIB,
                MelanomaStage.IV,
                MelanomaStage.IA
        };

        boolean[] expectedRecommendations = {
                true, true, false, false
        };

        for (int i = 0; i < expectedStages.length; i++) {

            Patient patient = new Patient(40000 + i);

            // Stage IIB: T3b (3 mm, ulcerated)
            double breslow = (i == 1) ? 3.0 : 0.5;
            boolean ulceration = (i == 1);

            patient.setPrimaryTumor(
                    new PrimaryTumor(breslow, ulceration)
            );

            patient.setRegionalDisease(
                    new RegionalDisease(
                            0, false, false,
                            false, false, false
                    )
            );

            // Stage IV: distant lung metastasis
            boolean distantMetastasis = (i == 2);

            patient.setDistantDisease(
                    new DistantDisease(
                            distantMetastasis,
                            false,
                            distantMetastasis,
                            false,
                            false,
                            LdhStatus.NOT_RECORDED
                    )
            );

            // Set clinical context BEFORE classification
            patient.getClinicalContext()
                    .setDiseaseStatus(diseaseStatuses[i]);

            MelanomaClassification classification =
                    system.classify(patient);

            assertEquals(
                    expectedStages[i],
                    classification.getStage(),
                    "Unexpected stage for patient " + i
            );

            RecommendationResult result =
                    system.generateRecommendations(
                            patient,
                            classification
                    );

            boolean hasFollow01 = result.getRecommendations()
                    .stream()
                    .anyMatch(r -> "FOLLOW-01".equals(r.getRuleId()));

            assertEquals(
                    expectedRecommendations[i],
                    hasFollow01,
                    "Unexpected FOLLOW-01 for stage "
                            + classification.getStage()
                            + " and disease status "
                            + diseaseStatuses[i]
            );
        }
    }


    @Test
    void recommendationsShouldNotContainDuplicateRuleIds() {

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        Patient patient = new Patient(41001);

        // T3b N0 M0 -> Stage IIB
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

        // Set clinical information before classification
        patient.getClinicalContext()
                .setBaselineImagingStatus(
                        BaselineImagingStatus.NOT_PERFORMED
                );

        patient.getClinicalContext()
                .setSurgeryStatus(
                        SurgeryStatus.NOT_PERFORMED
                );

        patient.getClinicalContext()
                .setSuspiciousRegionalNode(Boolean.FALSE);

        patient.getClinicalContext()
                .setSentinelNodeBiopsyStatus(
                        SentinelNodeBiopsyStatus.NOT_PERFORMED
                );

        MelanomaClassification classification =
                system.classify(patient);

        assertEquals(
                MelanomaStage.IIB,
                classification.getStage()
        );

        RecommendationResult result =
                system.generateRecommendations(
                        patient,
                        classification
                );

        java.util.List<String> ruleIds =
                result.getRecommendations()
                        .stream()
                        .map(ClinicalRecommendation::getRuleId)
                        .toList();

        long uniqueRuleIds =
                ruleIds.stream()
                        .distinct()
                        .count();

        assertEquals(
                ruleIds.size(),
                uniqueRuleIds,
                "Duplicate recommendation IDs detected: " + ruleIds
        );

        assertTrue(ruleIds.contains("DIAG-02"));
        assertTrue(ruleIds.contains("DIAG-03A"));
        assertTrue(ruleIds.contains("SURG-01A"));
    }


    @ParameterizedTest(name = "GEN-01 case {index}: {0} -> {1}")
    @CsvSource({
            "NOT_PERFORMED, GEN-01A",
            "COMPLETE, GEN-01B",
            "UNKNOWN, GEN-01C"
    })
    void suspiciousRegionalNodeShouldGenerateCorrectRecommendation(
            BaselineImagingStatus ultrasoundStatus,
            String expectedRuleId) {

        Patient patient = new Patient(42001);

        patient.setPrimaryTumor(new PrimaryTumor(1.5, false));

        patient.setRegionalDisease(
                new RegionalDisease(0, false, false, false, false, false)
        );

        patient.setDistantDisease(
                new DistantDisease(
                        false, false, false, false, false,
                        LdhStatus.NOT_RECORDED
                )
        );

        patient.getClinicalContext()
                .setSuspiciousRegionalNode(Boolean.TRUE);

        patient.getClinicalContext()
                .setRegionalNodeUltrasoundStatus(ultrasoundStatus);

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        MelanomaClassification classification =
                system.classify(patient);

        RecommendationResult result =
                system.generateRecommendations(patient, classification);

        long matchingRules = result.getRecommendations()
                .stream()
                .filter(r -> r.getRuleId().startsWith("GEN-01"))
                .count();

        assertEquals(1, matchingRules,
                "Exactly one GEN-01 recommendation should be generated");

        assertTrue(
                result.getRecommendations().stream()
                        .anyMatch(r -> expectedRuleId.equals(r.getRuleId())),
                "Expected recommendation: " + expectedRuleId
        );
    }

    @Test
    void noSuspiciousNodeShouldNotGenerateGen01() {

        Patient patient = new Patient(42002);

        patient.setPrimaryTumor(new PrimaryTumor(1.5, false));

        patient.setRegionalDisease(
                new RegionalDisease(0, false, false, false, false, false)
        );

        patient.setDistantDisease(
                new DistantDisease(
                        false, false, false, false, false,
                        LdhStatus.NOT_RECORDED
                )
        );

        patient.getClinicalContext()
                .setSuspiciousRegionalNode(Boolean.FALSE);

        patient.getClinicalContext()
                .setRegionalNodeUltrasoundStatus(
                        BaselineImagingStatus.NOT_PERFORMED
                );

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        MelanomaClassification classification =
                system.classify(patient);

        RecommendationResult result =
                system.generateRecommendations(patient, classification);

        assertTrue(
                result.getRecommendations().stream()
                        .noneMatch(r -> r.getRuleId().startsWith("GEN-01")),
                "GEN-01 must not fire without a suspicious regional node"
        );
    }


    @Test
    void newSymptomsShouldTriggerGen02OnlyWhenTrue() {


        String[] symptoms = {
                "true",
                "false",
                null
        };

        boolean[] expected = {
                true,
                false,
                false
        };

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        for (int i = 0; i < symptoms.length; i++) {

            Patient patient = new Patient(43000 + i);

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
                    .setNewSymptoms(
                            symptoms[i] == null ? null : Boolean.valueOf(symptoms[i])
                    );

            MelanomaClassification classification =
                    system.classify(patient);

            RecommendationResult result =
                    system.generateRecommendations(
                            patient,
                            classification
                    );

            boolean hasGen02 = result.getRecommendations()
                    .stream()
                    .anyMatch(r -> "GEN-02".equals(r.getRuleId()));

            assertEquals(
                    expected[i],
                    hasGen02,
                    "Unexpected GEN-02 for newSymptoms = "
                            + symptoms[i]
            );
        }
    }


    @Test
    void stageZeroRecommendationShouldDependOnSurgeryStatus() {

        SurgeryStatus[] statuses = {
                SurgeryStatus.NOT_PERFORMED,
                SurgeryStatus.INCOMPLETE,
                SurgeryStatus.COMPLETE,
                SurgeryStatus.UNKNOWN
        };

        String[] expectedRules = {
                "STAGE-00A",
                "STAGE-00A",
                "STAGE-00B",
                "STAGE-00C"
        };

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        for (int i = 0; i < statuses.length; i++) {

            Patient patient = new Patient(44000 + i);

            patient.setPrimaryTumor(PrimaryTumor.inSitu());

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
                    .setSurgeryStatus(statuses[i]);

            MelanomaClassification classification =
                    system.classify(patient);

            assertEquals(
                    MelanomaStage.ZERO,
                    classification.getStage()
            );

            RecommendationResult result =
                    system.generateRecommendations(
                            patient, classification
                    );

            long stageZeroCount = result.getRecommendations()
                    .stream()
                    .filter(r -> r.getRuleId().startsWith("STAGE-00"))
                    .count();

            assertEquals(
                    1,
                    stageZeroCount,
                    "Exactly one STAGE-00 rule expected for " + statuses[i]
            );


            String expectedRule = expectedRules[i];

            assertTrue(
                    result.getRecommendations()
                            .stream()
                            .anyMatch(r ->
                                    expectedRule.equals(r.getRuleId())
                            ),
                    "Expected " + expectedRule
                            + " for surgery status " + statuses[i]
            );

        }
    }


    @Test
    void regionalUltrasoundShouldDependOnTCategory() {

        double[] thicknesses = {
                0.5, 0.9, 1.5, 2.5, 3.5, 4.5
        };

        boolean[] expected = {
                false, true, true, true, true, true
        };

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        for (int i = 0; i < thicknesses.length; i++) {

            Patient patient = new Patient(45000 + i);

            patient.setPrimaryTumor(
                    new PrimaryTumor(thicknesses[i], false)
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
                    .setRegionalNodeUltrasoundStatus(
                            BaselineImagingStatus.NOT_PERFORMED
                    );

            patient.getClinicalContext()
                    .setSuspiciousRegionalNode(Boolean.FALSE);

            MelanomaClassification classification =
                    system.classify(patient);

            RecommendationResult result =
                    system.generateRecommendations(
                            patient, classification
                    );

            boolean hasDiag01 = result.getRecommendations()
                    .stream()
                    .anyMatch(r -> "DIAG-01".equals(r.getRuleId()));

            assertEquals(
                    expected[i],
                    hasDiag01,
                    "Unexpected DIAG-01 for Breslow = "
                            + thicknesses[i] + " mm"
            );
        }
    }


    @Test
    void regionalUltrasoundRecommendationsShouldDependOnImagingStatus() {

        BaselineImagingStatus[] statuses = {
                BaselineImagingStatus.UNKNOWN,
                BaselineImagingStatus.NOT_PERFORMED,
                BaselineImagingStatus.COMPLETE
        };

        String[] expectedRules = {
                "DATA-01",
                "DIAG-01",
                "NONE"
        };

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        for (int i = 0; i < statuses.length; i++) {

            Patient patient = new Patient(46000 + i);

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
                    .setRegionalNodeUltrasoundStatus(statuses[i]);

            patient.getClinicalContext()
                    .setSuspiciousRegionalNode(Boolean.FALSE);

            MelanomaClassification classification =
                    system.classify(patient);

            RecommendationResult result =
                    system.generateRecommendations(
                            patient, classification
                    );

            long ultrasoundRecommendations =
                    result.getRecommendations()
                            .stream()
                            .filter(r ->
                                    "DATA-01".equals(r.getRuleId()) ||
                                            "DIAG-01".equals(r.getRuleId())
                            )
                            .count();

            String expectedRule = expectedRules[i];

            if ("NONE".equals(expectedRule)) {

                assertEquals(
                        0,
                        ultrasoundRecommendations,
                        "No ultrasound recommendation expected for "
                                + statuses[i]
                );

            } else {

                assertEquals(
                        1,
                        ultrasoundRecommendations,
                        "Exactly one ultrasound recommendation expected for "
                                + statuses[i]
                );

                assertTrue(
                        result.getRecommendations()
                                .stream()
                                .anyMatch(r ->
                                        expectedRule.equals(r.getRuleId())
                                ),
                        "Expected " + expectedRule
                                + " for " + statuses[i]
                );
            }
        }
    }


    @Test
    void baselineImagingRecommendationShouldDependOnStageAndStatus() {

        double[] thicknesses = {
                0.5, 1.5, 2.5, 2.5, 4.5
        };

        boolean[] ulcerations = {
                false, false, false, true, true
        };

        boolean[] expected = {
                false, false, false, true, true
        };

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        for (int i = 0; i < thicknesses.length; i++) {

            Patient patient = new Patient(47000 + i);

            patient.setPrimaryTumor(
                    new PrimaryTumor(thicknesses[i], ulcerations[i])
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
                    .setBaselineImagingStatus(
                            BaselineImagingStatus.NOT_PERFORMED
                    );

            MelanomaClassification classification =
                    system.classify(patient);

            RecommendationResult result =
                    system.generateRecommendations(
                            patient, classification
                    );

            boolean hasDiag02 = result.getRecommendations()
                    .stream()
                    .anyMatch(r ->
                            "DIAG-02".equals(r.getRuleId())
                    );

            assertEquals(
                    expected[i],
                    hasDiag02,
                    "Unexpected DIAG-02 for Breslow = "
                            + thicknesses[i]
                            + ", ulceration = " + ulcerations[i]
                            + ", stage = " + classification.getStage()
            );
        }
    }

    @Test
    void brafRecommendationShouldDependOnStage() {

        double[] thicknesses = {
                0.5, 1.5, 2.5, 2.5, 4.5
        };

        boolean[] ulcerations = {
                false, false, false, true, true
        };

        boolean[] expected = {
                false, false, false, true, true
        };

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        for (int i = 0; i < thicknesses.length; i++) {

            Patient patient = new Patient(48000 + i);

            patient.setPrimaryTumor(
                    new PrimaryTumor(thicknesses[i], ulcerations[i])
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
                    .setBrafStatus(BrafStatus.UNKNOWN);

            MelanomaClassification classification =
                    system.classify(patient);
            System.out.println(
                    "Patient " + patient.getPatientId()
                            + " -> Stage: " + classification.getStage()
                            + " | BRAF: " + patient.getClinicalContext().getBrafStatus()
            );

            RecommendationResult result =
                    system.generateRecommendations(
                            patient, classification
                    );

            boolean hasMol01 = result.getRecommendations()
                    .stream()
                    .anyMatch(r ->
                            "MOL-01".equals(r.getRuleId())
                    );

            assertEquals(
                    expected[i],
                    hasMol01,
                    "Unexpected MOL-01 for Breslow = "
                            + thicknesses[i]
                            + ", ulceration = " + ulcerations[i]
                            + ", stage = " + classification.getStage()
            );
        }
    }



    @Test
    void surgicalRecommendationShouldDependOnSurgeryStatus() {

        SurgeryStatus[] statuses = {
                SurgeryStatus.NOT_PERFORMED,
                SurgeryStatus.INCOMPLETE,
                SurgeryStatus.UNKNOWN,
                SurgeryStatus.COMPLETE
        };

        String[] expectedRules = {
                "SURG-01A",
                "SURG-01B",
                "SURG-01C",
                "NONE"
        };

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        for (int i = 0; i < statuses.length; i++) {

            Patient patient = new Patient(49000 + i);

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
                    .setSurgeryStatus(statuses[i]);

            MelanomaClassification classification =
                    system.classify(patient);

            assertEquals(
                    MelanomaStage.IB,
                    classification.getStage()
            );

            RecommendationResult result =
                    system.generateRecommendations(
                            patient, classification
                    );


            long surgicalRecommendations =
                    result.getRecommendations()
                            .stream()
                            .filter(r ->
                                    r.getRuleId().startsWith("SURG-01")
                            )
                            .count();

            String expectedRule = expectedRules[i];

            if ("NONE".equals(expectedRule)) {

                assertEquals(
                        0,
                        surgicalRecommendations,
                        "No SURG-01 recommendation expected for "
                                + statuses[i]
                );

            } else {

                assertEquals(
                        1,
                        surgicalRecommendations,
                        "Exactly one SURG-01 recommendation expected for "
                                + statuses[i]
                );

                assertTrue(
                        result.getRecommendations()
                                .stream()
                                .anyMatch(r ->
                                        expectedRule.equals(r.getRuleId())
                                ),
                        "Expected " + expectedRule
                                + " for " + statuses[i]
                );
            }
        }
    }


    @Test
    void thinMelanomaWithUlcerationShouldTriggerDiag03B() {

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        Patient patient = new Patient(43001);

        // Breslow 0.9 mm with ulceration
        patient.setPrimaryTumor(
                new PrimaryTumor(0.9, true)
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
                .setSuspiciousRegionalNode(Boolean.FALSE);

        patient.getClinicalContext()
                .setSentinelNodeBiopsyStatus(
                        SentinelNodeBiopsyStatus.NOT_PERFORMED
                );

        MelanomaClassification classification =
                system.classify(patient);

        RecommendationResult result =
                system.generateRecommendations(
                        patient,
                        classification
                );

        java.util.List<String> ruleIds =
                result.getRecommendations()
                        .stream()
                        .map(ClinicalRecommendation::getRuleId)
                        .toList();

        assertTrue(
                ruleIds.contains("DIAG-03B"),
                "Expected DIAG-03B for Breslow 0.9 mm with ulceration"
        );

        assertFalse(
                ruleIds.contains("DIAG-03A"),
                "DIAG-03A should not trigger below 1.0 mm"
        );
    }


    @Test
    void missingUlcerationShouldGenerateData02() {

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        Patient patient = new Patient(44001);

        // Breslow known, ulceration unknown
        patient.setPrimaryTumor(
                new PrimaryTumor(Double.valueOf(0.9), (Boolean) null)
        );

        assertNull(
                patient.getPrimaryTumor().getUlceration(),
                "Ulceration should remain unknown"
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
                .setSuspiciousRegionalNode(Boolean.FALSE);

        patient.getClinicalContext()
                .setSentinelNodeBiopsyStatus(
                        SentinelNodeBiopsyStatus.NOT_PERFORMED
                );

        MelanomaClassification classification =
                system.classify(patient);

        RecommendationResult result =
                system.generateRecommendations(
                        patient,
                        classification
                );

        java.util.List<String> ruleIds =
                result.getRecommendations()
                        .stream()
                        .map(ClinicalRecommendation::getRuleId)
                        .toList();

        assertTrue(
                ruleIds.contains("DATA-02"),
                "DATA-02 should request missing ulceration information"
        );

        assertFalse(
                ruleIds.contains("DIAG-03B"),
                "DIAG-03B must not assume that ulceration is present"
        );

        assertFalse(
                ruleIds.contains("DIAG-03A"),
                "DIAG-03A should not trigger for Breslow below 1.0 mm"
        );

        System.out.println(
                "Classification with unknown ulceration: "
                        + classification.getTCategory()
        );
    }











}
