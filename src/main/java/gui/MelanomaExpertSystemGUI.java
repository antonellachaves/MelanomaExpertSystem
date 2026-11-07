
package gui;

import javax.swing.*;
import java.awt.*;

public class MelanomaExpertSystemGUI extends JFrame {

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


        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 15));

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        );

// Patient ID
        formPanel.add(new JLabel("Patient ID:"));
        JTextField patientIdField = new JTextField();
        formPanel.add(patientIdField);

// Breslow thickness
        formPanel.add(new JLabel("Breslow thickness (mm):"));
        JTextField breslowField = new JTextField();
        formPanel.add(breslowField);

// Ulceration
        formPanel.add(new JLabel("Ulceration:"));

        JComboBox<String> ulcerationBox = new JComboBox<>(
                new String[]{"Unknown", "Yes", "No"}
        );

        formPanel.add(ulcerationBox);

        // Number of involved regional nodes
        formPanel.add(new JLabel("Involved regional nodes:"));

        JTextField involvedNodesField = new JTextField();

        formPanel.add(involvedNodesField);

// Clinically detected node
        formPanel.add(new JLabel("Clinically detected node:"));

        JComboBox<String> clinicallyDetectedNodeBox =
                new JComboBox<>(
                        new String[]{"Unknown", "Yes", "No"}
                );

        formPanel.add(clinicallyDetectedNodeBox);

        // In-transit metastasis
        formPanel.add(new JLabel("In-transit metastasis:"));

        JComboBox<String> inTransitBox = new JComboBox<>(
                new String[]{"Unknown", "Yes", "No"}
        );
        formPanel.add(inTransitBox);

// Satellite metastasis
        formPanel.add(new JLabel("Satellite metastasis:"));

        JComboBox<String> satelliteBox = new JComboBox<>(
                new String[]{"Unknown", "Yes", "No"}
        );
        formPanel.add(satelliteBox);

// Microsatellite metastasis
        formPanel.add(new JLabel("Microsatellite metastasis:"));

        JComboBox<String> microsatelliteBox = new JComboBox<>(
                new String[]{"Unknown", "Yes", "No"}
        );
        formPanel.add(microsatelliteBox);

// Matted nodes
        formPanel.add(new JLabel("Matted nodes:"));

        JComboBox<String> mattedNodesBox = new JComboBox<>(
                new String[]{"Unknown", "Yes", "No"}
        );
        formPanel.add(mattedNodesBox);



// Add form to window

        JPanel containerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        formPanel.setPreferredSize(new Dimension(650, 460));


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



    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            MelanomaExpertSystemGUI window =
                    new MelanomaExpertSystemGUI();

            window.setVisible(true);
        });
    }
}
