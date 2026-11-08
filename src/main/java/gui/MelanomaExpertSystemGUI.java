
package gui;

import javax.swing.*;
import java.awt.*;
import model.LdhStatus;
import model.PrimaryTumor;
import model.RegionalDisease;
import model.DistantDisease;
import model.Patient;
import model.SurgeryStatus;
import model.DiseaseStatus;
import model.SentinelNodeBiopsyStatus;
import model.BaselineImagingStatus;
import model.BrafStatus;
import model.ResectabilityStatus;
import model.ClinicalContext;
import engine.MelanomaExpertSystem;
import model.EvaluationResult;
import model.MissingClinicalData;

import java.awt.Font;
import java.awt.Insets;


public class MelanomaExpertSystemGUI extends JFrame {


    private JTextField patientIdField;
    private JTextField breslowField;
    private JComboBox<String> ulcerationBox;
    private JTextField involvedNodesField;
    private JComboBox<String> clinicallyDetectedNodeBox;

    private JComboBox<String> inTransitBox;
    private JComboBox<String> satelliteBox;
    private JComboBox<String> microsatelliteBox;
    private JComboBox<String> mattedNodesBox;

    private JComboBox<String> distantMetastasisBox;
    private JComboBox<String> distantSkinBox;
    private JComboBox<String> lungMetastasisBox;
    private JComboBox<String> visceralMetastasisBox;
    private JComboBox<String> cnsMetastasisBox;
    private JComboBox<String> ldhStatusBox;

    private JComboBox<String> surgeryStatusBox;
    private JComboBox<String> diseaseStatusBox;
    private JComboBox<String> underTreatmentBox;

    private JComboBox<String> slnbStatusBox;
    private JComboBox<String> baselineImagingBox;
    private JComboBox<String> regionalUltrasoundBox;
    private JComboBox<String> brafStatusBox;
    private JComboBox<String> resectabilityBox;
    private JComboBox<String> suspiciousNodeBox;
    private JComboBox<String> newSymptomsBox;
    private JComboBox<String> adjuvantEligibilityBox;

    public MelanomaExpertSystemGUI() {

        setTitle("Melanoma Expert System");

        setSize(900, 650);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JLabel title = new JLabel(
                "Melanoma Clinical Expert System",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 24));

        add(title, BorderLayout.NORTH);


        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 15));

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        );


        JLabel patientSection = new JLabel("PATIENT AND PRIMARY TUMOR (T)");
        patientSection.setFont(new Font("Arial", Font.BOLD, 15));

        formPanel.add(patientSection);
        formPanel.add(new JLabel(""));


// Patient ID
        formPanel.add(new JLabel("Patient ID:"));
        patientIdField = new JTextField();
        formPanel.add(patientIdField);

// Breslow thickness
        formPanel.add(new JLabel("Breslow thickness (mm):"));
        breslowField = new JTextField();
        formPanel.add(breslowField);

// Ulceration
        formPanel.add(new JLabel("Ulceration:"));

        ulcerationBox = new JComboBox<>(
                new String[]{"Unknown", "Yes", "No"}
        );

        formPanel.add(ulcerationBox);


        JLabel regionalSection = new JLabel("REGIONAL DISEASE (N)");
        regionalSection.setFont(new Font("Arial", Font.BOLD, 15));

        formPanel.add(regionalSection);
        formPanel.add(new JLabel(""));

// Number of involved regional nodes
        formPanel.add(new JLabel("Involved regional nodes:"));

        involvedNodesField = new JTextField();

        formPanel.add(involvedNodesField);

// Clinically detected node
        formPanel.add(new JLabel("Clinically detected node:"));

        clinicallyDetectedNodeBox =
                new JComboBox<>(
                        new String[]{"Unknown", "Yes", "No"}
                );

        formPanel.add(clinicallyDetectedNodeBox);

// In-transit metastasis
        formPanel.add(new JLabel("In-transit metastasis:"));

        inTransitBox = new JComboBox<>(
                new String[]{"Unknown", "Yes", "No"}
        );

        formPanel.add(inTransitBox);

// Satellite metastasis
        formPanel.add(new JLabel("Satellite metastasis:"));

        satelliteBox = new JComboBox<>(
                new String[]{"Unknown", "Yes", "No"}
        );
        formPanel.add(satelliteBox);

// Microsatellite metastasis
        formPanel.add(new JLabel("Microsatellite metastasis:"));

        microsatelliteBox = new JComboBox<>(
                new String[]{"Unknown", "Yes", "No"}
        );
        formPanel.add(microsatelliteBox);

// Matted nodes
        formPanel.add(new JLabel("Matted nodes:"));

        mattedNodesBox = new JComboBox<>(
                new String[]{"Unknown", "Yes", "No"}
        );
        formPanel.add(mattedNodesBox);


        JLabel distantSection = new JLabel("DISTANT DISEASE (M)");
        distantSection.setFont(new Font("Arial", Font.BOLD, 15));

        formPanel.add(distantSection);
        formPanel.add(new JLabel(""));


// Distant metastasis
        formPanel.add(new JLabel("Distant metastasis:"));

        distantMetastasisBox =
                new JComboBox<>(
                        new String[]{"Unknown", "Yes", "No"}
                );

        formPanel.add(distantMetastasisBox);

// Distant skin, soft tissue or non-regional nodes
        formPanel.add(new JLabel(
                "Distant skin / soft tissue / non-regional nodes:"
        ));

        distantSkinBox =
                new JComboBox<>(
                        new String[]{"Unknown", "Yes", "No"}
                );

        formPanel.add(distantSkinBox);

// Lung metastasis
        formPanel.add(new JLabel("Lung metastasis:"));

        lungMetastasisBox =
                new JComboBox<>(
                        new String[]{"Unknown", "Yes", "No"}
                );

        formPanel.add(lungMetastasisBox);

// Non-CNS visceral metastasis
        formPanel.add(new JLabel("Non-CNS visceral metastasis:"));

        visceralMetastasisBox =
                new JComboBox<>(
                        new String[]{"Unknown", "Yes", "No"}
                );

        formPanel.add(visceralMetastasisBox);

// CNS metastasis
        formPanel.add(new JLabel("CNS metastasis:"));

        cnsMetastasisBox =
                new JComboBox<>(
                        new String[]{"Unknown", "Yes", "No"}
                );

        formPanel.add(cnsMetastasisBox);

// LDH status
        formPanel.add(new JLabel("LDH status:"));


        ldhStatusBox =
                new JComboBox<>(
                        new String[]{
                                "Not recorded",
                                "Not elevated",
                                "Elevated"
                        }
                );

        formPanel.add(ldhStatusBox);


        formPanel.add(ldhStatusBox);

// Clinical context section
        JLabel clinicalSection = new JLabel("CLINICAL CONTEXT");

        clinicalSection.setFont(
                new Font("Arial", Font.BOLD, 15)
        );

        formPanel.add(clinicalSection);
        formPanel.add(new JLabel(""));

        // Surgery status
        formPanel.add(new JLabel("Surgery status:"));

        surgeryStatusBox = new JComboBox<>(
                new String[]{
                        "Unknown",
                        "Not performed",
                        "Incomplete",
                        "Complete"
                }
        );

        formPanel.add(surgeryStatusBox);

// Disease status
        formPanel.add(new JLabel("Disease status:"));

        diseaseStatusBox = new JComboBox<>(
                new String[]{
                        "Unknown",
                        "Active disease",
                        "No evidence of disease"
                }
        );

        formPanel.add(diseaseStatusBox);

// Currently under treatment
        formPanel.add(new JLabel("Currently under treatment:"));

        underTreatmentBox = new JComboBox<>(
                new String[]{"Unknown", "Yes", "No"}
        );

        formPanel.add(underTreatmentBox);


        // Sentinel lymph node biopsy
        formPanel.add(new JLabel("Sentinel node biopsy (SLNB):"));

        slnbStatusBox = new JComboBox<>(
                new String[]{
                        "Unknown",
                        "Not performed",
                        "Negative",
                        "Positive"
                }
        );

        formPanel.add(slnbStatusBox);

// Baseline imaging
        formPanel.add(new JLabel("Baseline imaging:"));

        baselineImagingBox = new JComboBox<>(
                new String[]{
                        "Unknown",
                        "Not performed",
                        "Complete"
                }
        );

        formPanel.add(baselineImagingBox);

// Regional node ultrasound
        formPanel.add(new JLabel("Regional node ultrasound:"));

        regionalUltrasoundBox = new JComboBox<>(
                new String[]{
                        "Unknown",
                        "Not performed",
                        "Complete"
                }
        );

        formPanel.add(regionalUltrasoundBox);


        // BRAF status
        formPanel.add(new JLabel("BRAF status:"));


        brafStatusBox = new JComboBox<>(
                new String[]{
                        "Unknown",
                        "Mutated",
                        "Not mutated"
                }
        );

        formPanel.add(brafStatusBox);

// Resectability status
        formPanel.add(new JLabel("Resectability status:"));


        resectabilityBox = new JComboBox<>(
                new String[]{
                        "Uncertain",
                        "Resectable",
                        "Unresectable",
                        "Not applicable"
                }
        );

        formPanel.add(resectabilityBox);

// Suspicious regional node
        formPanel.add(new JLabel("Suspicious regional node:"));

        suspiciousNodeBox = new JComboBox<>(
                new String[]{"Unknown", "Yes", "No"}
        );
        formPanel.add(suspiciousNodeBox);

// New symptoms
        formPanel.add(new JLabel("New symptoms:"));

        newSymptomsBox = new JComboBox<>(
                new String[]{"Unknown", "Yes", "No"}
        );
        formPanel.add(newSymptomsBox);

// Adjuvant eligibility assessed
        formPanel.add(new JLabel("Adjuvant eligibility assessed:"));

        adjuvantEligibilityBox = new JComboBox<>(
                new String[]{"Unknown", "Yes", "No"}
        );
        formPanel.add(adjuvantEligibilityBox);





// Add form to window

        JPanel containerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        formPanel.setPreferredSize(new Dimension(650, 1510));


        containerPanel.add(formPanel);

// Allow vertical scrolling when the form grows
        JScrollPane scrollPane = new JScrollPane(containerPanel);

        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        JButton evaluateButton = new JButton("Evaluate Patient");

        add(evaluateButton, BorderLayout.SOUTH);


        evaluateButton.addActionListener(e -> {

            try {

                Patient patient = buildPatient();

                // Execute the expert system
                MelanomaExpertSystem system = new MelanomaExpertSystem();

                EvaluationResult result = system.evaluate(patient);

                // 1. Prepare missing clinical information
                StringBuilder missingInfo = new StringBuilder();

                if (!result.getMissingData().isEmpty()) {

                    missingInfo.append("\n\nMissing clinical information:\n");

                    for (MissingClinicalData missing : result.getMissingData()) {
                        missingInfo.append("- ")
                                .append(missing.getCategory())
                                .append(": ")
                                .append(missing.getField())
                                .append("\n");
                    }
                }


                StringBuilder explanationsInfo = new StringBuilder();

                if (!result.getExplanations().isEmpty()) {

                    explanationsInfo.append("\n\nClinical explanations:\n");

                    for (String explanation : result.getExplanations()) {

                        explanationsInfo.append("- ")
                                .append(explanation)
                                .append("\n");
                    }
                }

                StringBuilder recommendationsInfo = new StringBuilder();

                if (result.isComplete()) {

                    recommendationsInfo.append("\n\nClinical recommendations:\n");

                    if (result.getRecommendations().isEmpty()) {

                        recommendationsInfo.append(
                                "No specific recommendations generated.\n"
                        );

                    } else {

                        for (String recommendation : result.getRecommendations()) {

                            recommendationsInfo.append("- ")
                                    .append(recommendation)
                                    .append("\n");
                        }
                    }
                }


                JTextArea resultsArea = new JTextArea(20, 65);

                resultsArea.setEditable(false);
                resultsArea.setLineWrap(true);
                resultsArea.setWrapStyleWord(true);

                resultsArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
                resultsArea.setMargin(new Insets(12, 15, 12, 15));

                JScrollPane resultsScrollPane = new JScrollPane(resultsArea);

                // 2. Display results


                String resultsText =
                        "PATIENT EVALUATION\n"
                                + "========================================\n"
                                + "Patient: " + patient.getPatientId()
                                + "\nEvaluation status: " + result.getStatus()

                                + "\n\nTNM CLASSIFICATION\n"
                                + "========================================\n"
                                + "T: " + result.getClassification().getTCategory()
                                + "\nN: " + result.getClassification().getNCategory()
                                + "\nM: " + result.getClassification().getMCategory()
                                + "\nStage: " + result.getClassification().getStage()

                                + missingInfo.toString()
                                + explanationsInfo.toString()
                                + recommendationsInfo.toString()

                                + (result.isComplete()
                                ? ""
                                : "\nClinical recommendations are blocked.");


                resultsArea.setText(resultsText);
                resultsArea.setCaretPosition(0);


                JOptionPane optionPane = new JOptionPane(
                        resultsScrollPane,
                        JOptionPane.INFORMATION_MESSAGE,
                        JOptionPane.DEFAULT_OPTION,
                        null,
                        new Object[]{"Close"},
                        "Close"
                );

                JDialog dialog = optionPane.createDialog(
                        this,
                        "Melanoma Expert System - Evaluation Results"
                );

                dialog.setVisible(true);








            } catch (IllegalArgumentException | IllegalStateException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Invalid Clinical Data",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });




    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            MelanomaExpertSystemGUI window =
                    new MelanomaExpertSystemGUI();

            window.setVisible(true);
        });
    }


    private int readPatientId() {
        String text = patientIdField.getText().trim();

        if (text.isEmpty()) {
            throw new IllegalArgumentException(
                    "Patient ID is required."
            );
        }

        try {
            int id = Integer.parseInt(text);

            if (id <= 0) {
                throw new IllegalArgumentException(
                        "Patient ID must be a positive number."
                );
            }

            return id;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Patient ID must be a valid integer."
            );
        }
    }


    private Double readBreslow() {
        String text = breslowField.getText().trim();

        if (text.isEmpty()) {
            return null;
        }

        try {
            double breslow = Double.parseDouble(text);

            if (!Double.isFinite(breslow) || breslow <= 0) {
                throw new IllegalArgumentException(
                        "Breslow thickness must be a positive number."
                );
            }

            return breslow;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Breslow thickness must be a valid number."
            );
        }

    }


    private Boolean readUlceration() {
        String value = (String) ulcerationBox.getSelectedItem();

        if ("Yes".equals(value)) {
            return true;
        }

        if ("No".equals(value)) {
            return false;
        }

        return null;
    }


    private Integer readInvolvedNodes() {
        String text = involvedNodesField.getText().trim();

        if (text.isEmpty()) {
            return null;
        }

        try {
            int nodes = Integer.parseInt(text);

            if (nodes < 0) {
                throw new IllegalArgumentException(
                        "Involved regional nodes cannot be negative."
                );
            }

            return nodes;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Involved regional nodes must be a valid integer."
            );
        }
    }


    private Boolean readClinicallyDetectedNode() {

        String value =
                (String) clinicallyDetectedNodeBox.getSelectedItem();

        if ("Yes".equals(value)) {
            return true;
        }

        if ("No".equals(value)) {
            return false;
        }

        return null;
    }


    private Boolean readYesNoUnknown(JComboBox<String> box) {

        String value = (String) box.getSelectedItem();

        if ("Yes".equals(value)) {
            return true;
        }

        if ("No".equals(value)) {
            return false;
        }

        return null;
    }


    private LdhStatus readLdhStatus() {

        String value = (String) ldhStatusBox.getSelectedItem();

        if ("Not elevated".equals(value)) {
            return LdhStatus.NOT_ELEVATED;
        }

        if ("Elevated".equals(value)) {
            return LdhStatus.ELEVATED;
        }

        return LdhStatus.NOT_RECORDED;
    }


    private PrimaryTumor buildPrimaryTumor() {

        Double breslow = readBreslow();

        Boolean ulceration = readUlceration();

        return new PrimaryTumor(breslow, ulceration);
    }


    private RegionalDisease buildRegionalDisease() {

        Integer involvedNodes = readInvolvedNodes();

        Boolean clinicallyDetected =
                readClinicallyDetectedNode();

        Boolean inTransit =
                readYesNoUnknown(inTransitBox);

        Boolean satellite =
                readYesNoUnknown(satelliteBox);

        Boolean microsatellite =
                readYesNoUnknown(microsatelliteBox);

        Boolean mattedNodes =
                readYesNoUnknown(mattedNodesBox);

        return new RegionalDisease(
                involvedNodes,
                clinicallyDetected,
                inTransit,
                satellite,
                microsatellite,
                mattedNodes
        );
    }


    private DistantDisease buildDistantDisease() {

        Boolean distantMetastasis =
                readYesNoUnknown(distantMetastasisBox);

        Boolean distantSkin =
                readYesNoUnknown(distantSkinBox);

        Boolean lung =
                readYesNoUnknown(lungMetastasisBox);

        Boolean visceral =
                readYesNoUnknown(visceralMetastasisBox);

        Boolean cns =
                readYesNoUnknown(cnsMetastasisBox);

        LdhStatus ldh = readLdhStatus();

        return new DistantDisease(
                distantMetastasis,
                distantSkin,
                lung,
                visceral,
                cns,
                ldh
        );
    }





    private Patient buildPatient() {

        int patientId = readPatientId();

        Patient patient = new Patient(patientId);

        // TNM classification data
        patient.setPrimaryTumor(buildPrimaryTumor());
        patient.setRegionalDisease(buildRegionalDisease());
        patient.setDistantDisease(buildDistantDisease());

        // Additional clinical information
        buildClinicalContext(patient.getClinicalContext());

        return patient;
    }





    private SurgeryStatus readSurgeryStatus() {

        String value = (String) surgeryStatusBox.getSelectedItem();

        if ("Not performed".equals(value)) {
            return SurgeryStatus.NOT_PERFORMED;
        }

        if ("Incomplete".equals(value)) {
            return SurgeryStatus.INCOMPLETE;
        }

        if ("Complete".equals(value)) {
            return SurgeryStatus.COMPLETE;
        }

        return SurgeryStatus.UNKNOWN;
    }

    private DiseaseStatus readDiseaseStatus() {

        String value = (String) diseaseStatusBox.getSelectedItem();

        if ("Active disease".equals(value)) {
            return DiseaseStatus.ACTIVE_DISEASE;
        }

        if ("No evidence of disease".equals(value)) {
            return DiseaseStatus.NO_EVIDENCE_OF_DISEASE;
        }

        return DiseaseStatus.UNKNOWN;
    }

    private Boolean readUnderTreatment() {
        return readYesNoUnknown(underTreatmentBox);
    }


    private SentinelNodeBiopsyStatus readSlnbStatus() {

        String value = (String) slnbStatusBox.getSelectedItem();

        if ("Not performed".equals(value)) {
            return SentinelNodeBiopsyStatus.NOT_PERFORMED;
        }

        if ("Negative".equals(value)) {
            return SentinelNodeBiopsyStatus.NEGATIVE;
        }

        if ("Positive".equals(value)) {
            return SentinelNodeBiopsyStatus.POSITIVE;
        }

        return SentinelNodeBiopsyStatus.UNKNOWN;
    }

    private BaselineImagingStatus readBaselineImaging() {

        String value = (String) baselineImagingBox.getSelectedItem();

        if ("Not performed".equals(value)) {
            return BaselineImagingStatus.NOT_PERFORMED;
        }

        if ("Complete".equals(value)) {
            return BaselineImagingStatus.COMPLETE;
        }

        return BaselineImagingStatus.UNKNOWN;
    }


    private BaselineImagingStatus readRegionalUltrasound() {

        String value = (String) regionalUltrasoundBox.getSelectedItem();

        if ("Not performed".equals(value)) {
            return BaselineImagingStatus.NOT_PERFORMED;
        }

        if ("Complete".equals(value)) {
            return BaselineImagingStatus.COMPLETE;
        }

        return BaselineImagingStatus.UNKNOWN;
    }

    private BrafStatus readBrafStatus() {

        String value = (String) brafStatusBox.getSelectedItem();

        if ("Mutated".equals(value)) {
            return BrafStatus.MUTATED;
        }

        if ("Not mutated".equals(value)) {
            return BrafStatus.NOT_MUTATED;
        }

        return BrafStatus.UNKNOWN;
    }


    private ResectabilityStatus readResectability() {

        String value = (String) resectabilityBox.getSelectedItem();

        if ("Resectable".equals(value)) {
            return ResectabilityStatus.RESECTABLE;
        }

        if ("Unresectable".equals(value)) {
            return ResectabilityStatus.UNRESECTABLE;
        }

        if ("Not applicable".equals(value)) {
            return ResectabilityStatus.NOT_APPLICABLE;
        }

        return ResectabilityStatus.UNCERTAIN;
    }

    private Boolean readSuspiciousNode() {
        return readYesNoUnknown(suspiciousNodeBox);
    }

    private Boolean readNewSymptoms() {
        return readYesNoUnknown(newSymptomsBox);
    }


    private Boolean readAdjuvantEligibility() {
        return readYesNoUnknown(adjuvantEligibilityBox);
    }


    private void buildClinicalContext(ClinicalContext context) {

        // Surgery and disease status
        context.setSurgeryStatus(readSurgeryStatus());
        context.setDiseaseStatus(readDiseaseStatus());

        // Sentinel lymph node biopsy and imaging
        context.setSentinelNodeBiopsyStatus(readSlnbStatus());
        context.setBaselineImagingStatus(readBaselineImaging());
        context.setRegionalNodeUltrasoundStatus(readRegionalUltrasound());

        // Molecular and clinical characteristics
        context.setBrafStatus(readBrafStatus());
        context.setResectabilityStatus(readResectability());

        // Suspicious regional node
        Boolean suspiciousNode = readSuspiciousNode();
        if (suspiciousNode != null) {
            context.setSuspiciousRegionalNode(suspiciousNode);
        }

        // New symptoms (nullable)
        context.setNewSymptoms(readNewSymptoms());

        // Adjuvant eligibility assessment
        Boolean adjuvantEligibility = readAdjuvantEligibility();
        if (adjuvantEligibility != null) {
            context.setAdjuvantEligibilityAssessed(adjuvantEligibility);
        }

        // Currently under treatment
        Boolean underTreatment = readUnderTreatment();
        if (underTreatment != null) {
            context.setCurrentlyUnderTreatment(underTreatment);
        }

    }













}
