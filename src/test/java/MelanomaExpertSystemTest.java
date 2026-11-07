
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
    void evaluateShouldReturnCompleteForValidPatient() {

        Patient patient = new Patient(40001);

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

        EvaluationResult result = system.evaluate(patient);

        assertEquals(EvaluationStatus.COMPLETE, result.getStatus());
        assertEquals(TCategory.T2A, result.getClassification().getTCategory());
        assertEquals(NCategory.N0, result.getClassification().getNCategory());
        assertEquals(MCategory.M0, result.getClassification().getMCategory());
        assertEquals(MelanomaStage.IB, result.getClassification().getStage());
        assertTrue(result.getMissingData().isEmpty());
    }


    @Test
    void evaluateShouldIdentifyMissingBreslow() {

        Patient patient = new Patient(40002);

        patient.setPrimaryTumor(
                new PrimaryTumor((Double) null, Boolean.FALSE)
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

        EvaluationResult result = system.evaluate(patient);

        assertEquals(EvaluationStatus.INCOMPLETE, result.getStatus());

        assertNull(result.getClassification().getTCategory());
        assertEquals(NCategory.N0, result.getClassification().getNCategory());
        assertEquals(MCategory.M0, result.getClassification().getMCategory());

        assertTrue(
                result.getMissingData().stream().anyMatch(
                        missing -> missing.getCategory().equals("T")
                                && missing.getField().equals("breslowThicknessMm")
                )
        );

        assertTrue(result.getRecommendations().isEmpty());
    }



    @Test
    void evaluateShouldIncludeClinicalReasoning() {

        Patient patient = new Patient(40003);

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

        patient.getClinicalContext()
                .setSentinelNodeBiopsyStatus(
                        SentinelNodeBiopsyStatus.NOT_PERFORMED
                );

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        EvaluationResult result = system.evaluate(patient);

        // TNM classification must be complete
        assertEquals(EvaluationStatus.COMPLETE, result.getStatus());
        assertEquals(MelanomaStage.IB,
                result.getClassification().getStage());

        // INF-02 must generate the first clinical finding
        assertTrue(
                result.getExplanations().stream()
                        .anyMatch(e -> e.startsWith("INF-02:"))
        );

        // INF-03 must use that finding to generate another
        assertTrue(
                result.getExplanations().stream()
                        .anyMatch(e -> e.startsWith("INF-03:"))
        );

        // Recommendations are not implemented yet
        assertTrue(
                result.getRecommendations().stream()
                        .anyMatch(r -> r.startsWith("REC-01:"))
        );
    }



    @Test
    void evaluateShouldNotRecommendSLNBWhenAlreadyPerformed() {

        Patient patient = new Patient(40004);

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

        patient.getClinicalContext()
                .setSentinelNodeBiopsyStatus(
                        SentinelNodeBiopsyStatus.NEGATIVE
                );

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        EvaluationResult result = system.evaluate(patient);

        // TNM classification must be complete
        assertEquals(
                EvaluationStatus.COMPLETE,
                result.getStatus()
        );

        assertEquals(
                MelanomaStage.IB,
                result.getClassification().getStage()
        );

        // INF-02 should still identify clinically negative nodes
        assertTrue(
                result.getExplanations().stream()
                        .anyMatch(e -> e.startsWith("INF-02:"))
        );

        // INF-03 must NOT activate
        assertFalse(
                result.getExplanations().stream()
                        .anyMatch(e -> e.startsWith("INF-03:"))
        );

        // REC-01 must NOT be generated
        assertFalse(
                result.getRecommendations().stream()
                        .anyMatch(r -> r.startsWith("REC-01:"))
        );
    }


    @Test
    void evaluateShouldBlockRecommendationsWhenTNMIncomplete() {

        Patient patient = new Patient(40005);

        // Missing Breslow thickness
        patient.setPrimaryTumor(
                new PrimaryTumor(null, false)
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

        patient.getClinicalContext()
                .setSentinelNodeBiopsyStatus(
                        SentinelNodeBiopsyStatus.NOT_PERFORMED
                );

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        EvaluationResult result = system.evaluate(patient);

        // TNM must be incomplete
        assertEquals(
                EvaluationStatus.INCOMPLETE,
                result.getStatus()
        );

        // Recommendations must remain blocked
        assertTrue(result.getRecommendations().isEmpty());

        // The system must explain why
        assertTrue(
                result.getExplanations().stream()
                        .anyMatch(e -> e.contains(
                                "Clinical recommendations are blocked"
                        ))
        );
    }


    @Test
    void evaluateShouldRecommendBaselineImagingForStageIIB() {

        Patient patient = new Patient(40006);

        // Breslow 3.0 mm with ulceration -> T3b
        patient.setPrimaryTumor(
                new PrimaryTumor(3.0, true)
        );

        // No regional nodal involvement -> N0
        patient.setRegionalDisease(
                new RegionalDisease(
                        0, false, false,
                        false, false, false
                )
        );

        // No distant metastasis -> M0
        patient.setDistantDisease(
                new DistantDisease(
                        false, false, false,
                        false, false,
                        LdhStatus.NOT_RECORDED
                )
        );

        patient.getClinicalContext()
                .setSuspiciousRegionalNode(false);

        patient.getClinicalContext()
                .setBaselineImagingStatus(
                        BaselineImagingStatus.NOT_PERFORMED
                );

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        EvaluationResult result = system.evaluate(patient);

        // Expected classification: T3b N0 M0 -> Stage IIB
        assertEquals(
                EvaluationStatus.COMPLETE,
                result.getStatus()
        );

        assertEquals(
                MelanomaStage.IIB,
                result.getClassification().getStage()
        );

        // INF-04 must identify staging assessment need
        assertTrue(
                result.getExplanations().stream()
                        .anyMatch(e -> e.startsWith("INF-04:"))
        );

        // REC-02C must recommend specific imaging
        assertTrue(
                result.getRecommendations().stream()
                        .anyMatch(r ->
                                r.startsWith("REC-02C:")
                                        && r.contains("PET-CT")
                                        && r.contains("brain MRI")
                        )
        );
    }


    @Test
    void evaluateShouldNotRecommendBaselineImagingWhenComplete() {

        Patient patient = new Patient(40007);

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

        patient.getClinicalContext()
                .setSuspiciousRegionalNode(false);

        patient.getClinicalContext()
                .setBaselineImagingStatus(
                        BaselineImagingStatus.COMPLETE
                );

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        EvaluationResult result = system.evaluate(patient);

        // Classification must remain complete
        assertEquals(
                EvaluationStatus.COMPLETE,
                result.getStatus()
        );

        assertEquals(
                MelanomaStage.IIB,
                result.getClassification().getStage()
        );

        // INF-04 must still activate
        assertTrue(
                result.getExplanations().stream()
                        .anyMatch(e -> e.startsWith("INF-04:"))
        );

        // REC-02C must NOT activate
        assertFalse(
                result.getRecommendations().stream()
                        .anyMatch(r -> r.startsWith("REC-02C:"))
        );
    }


    @Test
    void evaluateShouldRequestImagingInformationWhenUnknown() {

        Patient patient = new Patient(40008);

        // T3b
        patient.setPrimaryTumor(
                new PrimaryTumor(3.0, true)
        );

        // N0
        patient.setRegionalDisease(
                new RegionalDisease(
                        0, false, false,
                        false, false, false
                )
        );

        // M0
        patient.setDistantDisease(
                new DistantDisease(
                        false, false, false,
                        false, false,
                        LdhStatus.NOT_RECORDED
                )
        );

        patient.getClinicalContext()
                .setSuspiciousRegionalNode(false);

        patient.getClinicalContext()
                .setBaselineImagingStatus(
                        BaselineImagingStatus.UNKNOWN
                );

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        EvaluationResult result = system.evaluate(patient);

        // TNM must be complete
        assertEquals(
                EvaluationStatus.COMPLETE,
                result.getStatus()
        );

        assertEquals(
                MelanomaStage.IIB,
                result.getClassification().getStage()
        );

        // INF-04 must activate
        assertTrue(
                result.getExplanations().stream()
                        .anyMatch(e -> e.startsWith("INF-04:"))
        );

        // REC-02F must request missing imaging information
        assertTrue(
                result.getRecommendations().stream()
                        .anyMatch(r ->
                                r.startsWith("REC-02F:")
                                        && r.contains("unknown")
                        )
        );

        // REC-02C must NOT activate
        assertFalse(
                result.getRecommendations().stream()
                        .anyMatch(r -> r.startsWith("REC-02C:"))
        );
    }


    @Test
    void evaluateShouldRecommendRegionalUltrasoundForStageIB() {

        Patient patient = new Patient(40009);

        // T2a: Breslow 1.5 mm, without ulceration
        patient.setPrimaryTumor(
                new PrimaryTumor(1.5, false)
        );

        // N0
        patient.setRegionalDisease(
                new RegionalDisease(
                        0, false, false,
                        false, false, false
                )
        );

        // M0
        patient.setDistantDisease(
                new DistantDisease(
                        false, false, false,
                        false, false,
                        LdhStatus.NOT_RECORDED
                )
        );

        patient.getClinicalContext()
                .setSuspiciousRegionalNode(false);

        patient.getClinicalContext()
                .setRegionalNodeUltrasoundStatus(
                        BaselineImagingStatus.NOT_PERFORMED
                );

        MelanomaExpertSystem system = new MelanomaExpertSystem();

        EvaluationResult result = system.evaluate(patient);

        // Expected classification: T2a N0 M0 -> IB
        assertEquals(
                EvaluationStatus.COMPLETE,
                result.getStatus()
        );

        assertEquals(
                MelanomaStage.IB,
                result.getClassification().getStage()
        );

        // INF-02 identifies clinically node-negative patient
        assertTrue(
                result.getExplanations().stream()
                        .anyMatch(e -> e.startsWith("INF-02:"))
        );

        // REC-02B must recommend regional ultrasound
        assertTrue(
                result.getRecommendations().stream()
                        .anyMatch(r ->
                                r.startsWith("REC-02B:")
                                        && r.contains("regional lymph node ultrasound")
                        )
        );
    }


    @Test
    void evaluateShouldRequestUltrasoundInformationWhenUnknown() {

        Patient patient = new Patient(40011);

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

        patient.getClinicalContext()
                .setRegionalNodeUltrasoundStatus(
                        BaselineImagingStatus.UNKNOWN
                );

        EvaluationResult result =
                new MelanomaExpertSystem().evaluate(patient);

        assertEquals(
                EvaluationStatus.COMPLETE,
                result.getStatus()
        );

        assertEquals(
                MelanomaStage.IB,
                result.getClassification().getStage()
        );

        // Unknown status -> request information
        assertTrue(
                result.getRecommendations().stream()
                        .anyMatch(r -> r.startsWith("REC-02D:"))
        );

        // Must not recommend an unperformed ultrasound
        assertFalse(
                result.getRecommendations().stream()
                        .anyMatch(r -> r.startsWith("REC-02B:"))
        );
    }


    @Test
    void evaluateShouldRecommendBaselineImagingForStageIIIA() {

        Patient patient = new Patient(40012);

        // T2a: Breslow 1.5 mm, no ulceration
        patient.setPrimaryTumor(
                new PrimaryTumor(1.5, false)
        );

        // N1a: one clinically occult positive regional node
        patient.setRegionalDisease(
                new RegionalDisease(
                        1, false, false,
                        false, false, false
                )
        );

        // M0: no distant metastases
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

        EvaluationResult result =
                new MelanomaExpertSystem().evaluate(patient);

        // Verify complete classification
        assertEquals(
                EvaluationStatus.COMPLETE,
                result.getStatus()
        );

        assertEquals(
                MelanomaStage.IIIA,
                result.getClassification().getStage()
        );

        // Verify REC-02G activates
        assertTrue(
                result.getRecommendations().stream()
                        .anyMatch(r ->
                                r.startsWith("REC-02G:")
                        )
        );

        // REC-02C is only for IIB-IIC
        assertFalse(
                result.getRecommendations().stream()
                        .anyMatch(r ->
                                r.startsWith("REC-02C:")
                        )
        );
    }


    @Test
    void evaluateShouldNotRecommendBaselineImagingForStageIIIAWhenComplete() {

        Patient patient = new Patient(40013);

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

        patient.getClinicalContext()
                .setBaselineImagingStatus(
                        BaselineImagingStatus.COMPLETE
                );

        EvaluationResult result =
                new MelanomaExpertSystem().evaluate(patient);

        assertEquals(
                EvaluationStatus.COMPLETE,
                result.getStatus()
        );

        assertEquals(
                MelanomaStage.IIIA,
                result.getClassification().getStage()
        );

        // REC-02G must not activate
        assertFalse(
                result.getRecommendations().stream()
                        .anyMatch(r -> r.startsWith("REC-02G:"))
        );

        // REC-02F must not activate either
        assertFalse(
                result.getRecommendations().stream()
                        .anyMatch(r -> r.startsWith("REC-02F:"))
        );
    }


    @Test
    void evaluateShouldRecommendRoutineFollowUp() {

        Patient patient = new Patient(40014);

        // T2a: Breslow 1.5 mm, no ulceration
        patient.setPrimaryTumor(
                new PrimaryTumor(1.5, false)
        );

        // N0
        patient.setRegionalDisease(
                new RegionalDisease(
                        0, false, false,
                        false, false, false
                )
        );

        // M0
        patient.setDistantDisease(
                new DistantDisease(
                        false, false, false,
                        false, false,
                        LdhStatus.NOT_RECORDED
                )
        );

        // Clinical context: routine follow-up
        patient.getClinicalContext().setSurgeryStatus(
                SurgeryStatus.COMPLETE
        );

        patient.getClinicalContext().setDiseaseStatus(
                DiseaseStatus.NO_EVIDENCE_OF_DISEASE
        );

        patient.getClinicalContext()
                .setNewSymptoms(false);

        patient.getClinicalContext()
                .setSuspiciousRegionalNode(false);

        patient.getClinicalContext()
                .setCurrentlyUnderTreatment(false);

        EvaluationResult result =
                new MelanomaExpertSystem().evaluate(patient);

        // Classification
        assertEquals(
                EvaluationStatus.COMPLETE,
                result.getStatus()
        );

        assertEquals(
                MelanomaStage.IB,
                result.getClassification().getStage()
        );

        // INF-05: routine follow-up scenario
        assertTrue(
                result.getExplanations().stream()
                        .anyMatch(e -> e.startsWith("INF-05:"))
        );

        // REC-03A: routine follow-up recommendation
        assertTrue(
                result.getRecommendations().stream()
                        .anyMatch(r -> r.startsWith("REC-03A:"))
        );
    }



    @Test
    void evaluateShouldNotRecommendRoutineFollowUpWhenNewSymptoms() {

        Patient patient = new Patient(40015);

        // Same melanoma classification as patient 40014
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

        // Clinical context
        patient.getClinicalContext().setSurgeryStatus(
                SurgeryStatus.COMPLETE
        );

        patient.getClinicalContext().setDiseaseStatus(
                DiseaseStatus.NO_EVIDENCE_OF_DISEASE
        );

        patient.getClinicalContext().setNewSymptoms(true);

        patient.getClinicalContext()
                .setSuspiciousRegionalNode(false);

        patient.getClinicalContext()
                .setCurrentlyUnderTreatment(false);

        EvaluationResult result =
                new MelanomaExpertSystem().evaluate(patient);

        // TNM classification remains complete
        assertEquals(
                EvaluationStatus.COMPLETE,
                result.getStatus()
        );

        assertEquals(
                MelanomaStage.IB,
                result.getClassification().getStage()
        );

        // INF-05 must NOT activate
        assertFalse(
                result.getExplanations().stream()
                        .anyMatch(e -> e.startsWith("INF-05:"))
        );

        // REC-03A must NOT activate
        assertFalse(
                result.getRecommendations().stream()
                        .anyMatch(r -> r.startsWith("REC-03A:"))
        );


        // New symptoms must trigger REC-03B
        assertTrue(
                result.getRecommendations().stream()
                        .anyMatch(r -> r.startsWith("REC-03B:"))
        );


        // New symptoms must trigger REC-03B
        assertTrue(
                result.getRecommendations().stream()
                        .anyMatch(r -> r.startsWith("REC-03B:"))
        );

    }


    @Test
    void evaluateShouldRecommendAssessmentForSuspiciousRegionalNodes() {

        Patient patient = new Patient(40016);

        // T2a
        patient.setPrimaryTumor(
                new PrimaryTumor(1.5, false)
        );

        // N0 based on currently recorded staging data
        patient.setRegionalDisease(
                new RegionalDisease(
                        0, false, false,
                        false, false, false
                )
        );

        // M0
        patient.setDistantDisease(
                new DistantDisease(
                        false, false, false,
                        false, false,
                        LdhStatus.NOT_RECORDED
                )
        );

        // Follow-up clinical context
        patient.getClinicalContext().setSurgeryStatus(
                SurgeryStatus.COMPLETE
        );

        patient.getClinicalContext().setDiseaseStatus(
                DiseaseStatus.NO_EVIDENCE_OF_DISEASE
        );

        patient.getClinicalContext().setNewSymptoms(false);

        patient.getClinicalContext()
                .setSuspiciousRegionalNode(true);

        patient.getClinicalContext()
                .setCurrentlyUnderTreatment(false);

        EvaluationResult result =
                new MelanomaExpertSystem().evaluate(patient);

        // Existing TNM classification
        assertEquals(
                EvaluationStatus.COMPLETE,
                result.getStatus()
        );

        assertEquals(
                MelanomaStage.IB,
                result.getClassification().getStage()
        );

        // INF-01: suspected nodal disease
        assertTrue(
                result.getExplanations().stream()
                        .anyMatch(e -> e.startsWith("INF-01:"))
        );

        // REC-03C: targeted nodal assessment
        assertTrue(
                result.getRecommendations().stream()
                        .anyMatch(r -> r.startsWith("REC-03C:"))
        );

        // Routine follow-up must not activate
        assertFalse(
                result.getRecommendations().stream()
                        .anyMatch(r -> r.startsWith("REC-03A:"))
        );
    }


    @Test
    void evaluateShouldRecommendAdjuvantAssessmentForStageIIB() {

        Patient patient = new Patient(40017);

        // T3b: Breslow 3.0 mm, ulcerated
        patient.setPrimaryTumor(
                new PrimaryTumor(3.0, true)
        );

        // N0
        patient.setRegionalDisease(
                new RegionalDisease(
                        0, false, false,
                        false, false, false
                )
        );

        // M0
        patient.setDistantDisease(
                new DistantDisease(
                        false, false, false,
                        false, false,
                        LdhStatus.NOT_RECORDED
                )
        );

        // Clinical context
        patient.getClinicalContext().setSurgeryStatus(
                SurgeryStatus.COMPLETE
        );

        patient.getClinicalContext().setDiseaseStatus(
                DiseaseStatus.NO_EVIDENCE_OF_DISEASE
        );

        patient.getClinicalContext()
                .setCurrentlyUnderTreatment(false);

        patient.getClinicalContext()
                .setAdjuvantEligibilityAssessed(false);

        EvaluationResult result =
                new MelanomaExpertSystem().evaluate(patient);

        // Verify classification
        assertEquals(
                EvaluationStatus.COMPLETE,
                result.getStatus()
        );

        assertEquals(
                MelanomaStage.IIB,
                result.getClassification().getStage()
        );

        // REC-04A must activate
        assertTrue(
                result.getRecommendations().stream()
                        .anyMatch(r -> r.startsWith("REC-04A:"))
        );
    }

    @Test
    void evaluateShouldNotRecommendAdjuvantAssessmentWhenAlreadyAssessed() {

        Patient patient = new Patient(40018);

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

        // Clinical context
        patient.getClinicalContext().setSurgeryStatus(
                SurgeryStatus.COMPLETE
        );

        patient.getClinicalContext().setDiseaseStatus(
                DiseaseStatus.NO_EVIDENCE_OF_DISEASE
        );

        patient.getClinicalContext()
                .setCurrentlyUnderTreatment(false);

        // Eligibility assessment already completed
        patient.getClinicalContext()
                .setAdjuvantEligibilityAssessed(true);

        EvaluationResult result =
                new MelanomaExpertSystem().evaluate(patient);

        assertEquals(
                EvaluationStatus.COMPLETE,
                result.getStatus()
        );

        assertEquals(
                MelanomaStage.IIB,
                result.getClassification().getStage()
        );

        // REC-04A must NOT activate
        assertFalse(
                result.getRecommendations().stream()
                        .anyMatch(r -> r.startsWith("REC-04A:"))
        );
    }


    @Test
    void evaluateShouldRequestAdjuvantAssessmentInformationWhenUnknown() {

        Patient patient = new Patient(40019);

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

        // Clinical context
        patient.getClinicalContext().setSurgeryStatus(
                SurgeryStatus.COMPLETE
        );

        patient.getClinicalContext().setDiseaseStatus(
                DiseaseStatus.NO_EVIDENCE_OF_DISEASE
        );

        patient.getClinicalContext()
                .setCurrentlyUnderTreatment(false);

        // Do not set adjuvantEligibilityAssessed.
        // Its default value is null (unknown).

        EvaluationResult result =
                new MelanomaExpertSystem().evaluate(patient);

        assertEquals(
                EvaluationStatus.COMPLETE,
                result.getStatus()
        );

        assertEquals(
                MelanomaStage.IIB,
                result.getClassification().getStage()
        );

        // Unknown assessment status -> REC-04B
        assertTrue(
                result.getRecommendations().stream()
                        .anyMatch(r -> r.startsWith("REC-04B:"))
        );

        // Must not assume assessment has not been performed
        assertFalse(
                result.getRecommendations().stream()
                        .anyMatch(r -> r.startsWith("REC-04A:"))
        );
    }


    @Test
    void evaluateShouldGenerateMultipleRecommendations() {

        Patient patient = new Patient(40020);

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

        ClinicalContext context = patient.getClinicalContext();

        context.setSurgeryStatus(SurgeryStatus.COMPLETE);
        context.setDiseaseStatus(
                DiseaseStatus.NO_EVIDENCE_OF_DISEASE
        );

        context.setNewSymptoms(false);
        context.setSuspiciousRegionalNode(false);
        context.setCurrentlyUnderTreatment(false);

        context.setBaselineImagingStatus(
                BaselineImagingStatus.NOT_PERFORMED
        );

        context.setAdjuvantEligibilityAssessed(false);

        EvaluationResult result =
                new MelanomaExpertSystem().evaluate(patient);

        // Classification
        assertEquals(
                EvaluationStatus.COMPLETE,
                result.getStatus()
        );

        assertEquals(
                MelanomaStage.IIB,
                result.getClassification().getStage()
        );

        // Baseline imaging recommendation
        assertTrue(
                result.getRecommendations().stream()
                        .anyMatch(r -> r.startsWith("REC-02C:"))
        );

        // Routine follow-up recommendation
        assertTrue(
                result.getRecommendations().stream()
                        .anyMatch(r -> r.startsWith("REC-03A:"))
        );

        // Adjuvant eligibility assessment
        assertTrue(
                result.getRecommendations().stream()
                        .anyMatch(r -> r.startsWith("REC-04A:"))
        );

        // Must not activate stage III-IV imaging rule
        assertFalse(
                result.getRecommendations().stream()
                        .anyMatch(r -> r.startsWith("REC-02G:"))
        );
    }


    @Test
    void evaluateShouldBlockRecommendationsWhenTnmIncomplete() {

        Patient patient = new Patient(40021);

        // Missing Breslow thickness
        patient.setPrimaryTumor(
                new PrimaryTumor(null, true)
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

        ClinicalContext context = patient.getClinicalContext();

        context.setSurgeryStatus(SurgeryStatus.COMPLETE);
        context.setDiseaseStatus(
                DiseaseStatus.NO_EVIDENCE_OF_DISEASE
        );

        context.setNewSymptoms(false);
        context.setSuspiciousRegionalNode(false);
        context.setCurrentlyUnderTreatment(false);

        context.setBaselineImagingStatus(
                BaselineImagingStatus.NOT_PERFORMED
        );

        context.setAdjuvantEligibilityAssessed(false);

        EvaluationResult result =
                new MelanomaExpertSystem().evaluate(patient);

        // TNM must remain incomplete
        assertEquals(
                EvaluationStatus.INCOMPLETE,
                result.getStatus()
        );

        assertNull(
                result.getClassification().getTCategory()
        );

        // No clinical recommendations may be generated
        assertTrue(
                result.getRecommendations().isEmpty()
        );

        // The system must explain why recommendations are blocked
        assertTrue(
                result.getExplanations().stream()
                        .anyMatch(e ->
                                e.contains(
                                        "Clinical recommendations are blocked"
                                )
                        )
        );
    }


















}
