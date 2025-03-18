import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class LanguageDetectorGUI extends JFrame {
    private JTextArea inputTextArea;
    private JButton detectButton;
    private JLabel resultLabel;

    public LanguageDetectorGUI() {
        setTitle("Multilingual Language Detector");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Text Area
        inputTextArea = new JTextArea("Enter text here...");
        add(new JScrollPane(inputTextArea), BorderLayout.CENTER);

        // Button
        detectButton = new JButton("Detect Language");
        add(detectButton, BorderLayout.SOUTH);

        // Result Label
        resultLabel = new JLabel("Detected Language: ", SwingConstants.CENTER);
        add(resultLabel, BorderLayout.NORTH);

        // Button Action
        detectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = inputTextArea.getText();
                String detectedLang = detectLanguage(inputText);
                resultLabel.setText("Detected Language: " + detectedLang);
            }
        });

        setVisible(true);
    }

    private String detectLanguage(String text) {
        try {
            String apiUrl = "http://127.0.0.1:5000/predict";
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // JSON Request
            String jsonInput = "{\"text\": \"" + text + "\"}";

            // Send request
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Read response
            try (Scanner scanner = new Scanner(conn.getInputStream(), StandardCharsets.UTF_8.name())) {
                return scanner.useDelimiter("\\A").next().replace("{\"language\": \"", "").replace("\"}", "");
            }

        } catch (Exception e) {
            return "Error: Could not connect to server!";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LanguageDetectorGUI::new);
    }
}
