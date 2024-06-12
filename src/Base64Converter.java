import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.datatransfer.*;
import java.awt.Toolkit;

public class Base64Converter extends JFrame implements ActionListener {

    private final JTextArea inputTextArea;
    private final JTextArea outputTextArea;
    private final JButton encodeButton;
    private final JButton decodeButton;
    private final JButton pasteButton; // Added paste button
    private final JButton clearButton;
    private final JButton copyButton;

    public Base64Converter() {
        super("Base 64 Encoding"); // Set the title of the JFrame
        setSize(400, 400); // Increased height to accommodate the output box below the buttons
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        Font titleFont = new Font("Arial", Font.BOLD, 16); // Define a larger font for the title
        JLabel titleLabel = new JLabel("Base 64 Encoding/Decoding");
        titleLabel.setFont(titleFont); // Set the font for the title label
        titleLabel.setBounds(80, 5, 250, 30); // Position and size the title label
        add(titleLabel);

        Font labelFont = new Font("Arial", Font.BOLD, 14); // Define a larger font for the labels
        JLabel inputLabel = new JLabel("Input:");
        inputLabel.setFont(labelFont); // Set the font for the input label
        inputLabel.setBounds(20, 40, 100, 30); // Adjust Y position to accommodate the title
        add(inputLabel);

        inputTextArea = new JTextArea();
        inputTextArea.setBounds(20, 70, 350, 40);
        add(inputTextArea);

        encodeButton = new JButton("Encode");
        encodeButton.setBounds(20, 130, 100, 30); // Adjust Y position for the encode button
        encodeButton.addActionListener(this);
        encodeButton.setBackground(Color.GREEN); // Set background color to green
        add(encodeButton);

        decodeButton = new JButton("Decode");
        decodeButton.setBounds(130, 130, 100, 30); // Adjust Y position for the decode button
        decodeButton.addActionListener(this);
        decodeButton.setBackground(Color.GREEN); // Set foreground color to black
        add(decodeButton);

        pasteButton = new JButton("Paste"); // Paste button
        pasteButton.setBounds(240, 130, 100, 30); // Adjust Y position for the paste button
        pasteButton.addActionListener(this);
        pasteButton.setBackground(Color.GREEN);
        add(pasteButton);

        JLabel outputLabel = new JLabel("Result:");
        outputLabel.setFont(labelFont); // Set the font for the output label
        outputLabel.setBounds(20, 170, 100, 30); // Adjust Y position for the output label
        add(outputLabel);

        outputTextArea = new JTextArea();
        outputTextArea.setBounds(20, 200, 350, 80); // Adjust Y position for the output text area
        outputTextArea.setEditable(false);
        add(outputTextArea);

        clearButton = new JButton("Clear");
        clearButton.setBounds(20, 300, 100, 30); // Adjust Y position for the clear button
        clearButton.addActionListener(this);
        clearButton.setBackground(Color.GREEN);
        add(clearButton);

        copyButton = new JButton("Copy");
        copyButton.setBounds(130, 300, 100, 30); // Adjust Y position for the copy button
        copyButton.addActionListener(this);
        copyButton.setBackground(Color.GREEN);
        add(copyButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == encodeButton) {
            String plainText = inputTextArea.getText();
            if (plainText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an input!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String encodedText = encodeToBase64(plainText);
                outputTextArea.setText(encodedText);
            }
        } else if (e.getSource() == decodeButton) {
            String encodedText = inputTextArea.getText();
            if (encodedText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an input!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    String decodedText = decodeFromBase64(encodedText);
                    outputTextArea.setText(decodedText);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid Base64 input!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == pasteButton) { // Handle paste button
            String clipboardText = getClipboardText();
            if (clipboardText != null) {
                inputTextArea.setText(clipboardText);
            } else {
                JOptionPane.showMessageDialog(this, "Clipboard is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == clearButton) {
            inputTextArea.setText("");
            outputTextArea.setText("");
        } else if (e.getSource() == copyButton) {
             String inputText = inputTextArea.getText();
            if (inputText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Result is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            String outputText = outputTextArea.getText();
            if (!outputText.isEmpty()) {
                StringSelection stringSelection = new StringSelection(outputText);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
                JOptionPane.showMessageDialog(this, "Output copied to clipboard!", "Copy", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private String encodeToBase64(String input) {
        return java.util.Base64.getEncoder().encodeToString(input.getBytes());
    }

    private String decodeFromBase64(String input) {
        byte[] decodedBytes = java.util.Base64.getDecoder().decode(input);
        return new String(decodedBytes);
    }

    private String getClipboardText() { // Helper method to get text from clipboard
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = clipboard.getContents(null);
        if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                return (String) transferable.getTransferData(DataFlavor.stringFlavor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Base64Converter::new);
    }
}
