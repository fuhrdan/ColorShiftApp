import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ColorShiftApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ColorShiftApp::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("RGB Color Shift Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel imageLabel = new JLabel("Select an image to view it here.", JLabel.CENTER);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        JButton openButton = new JButton("Open Image");
        JButton transformButton = new JButton("Transform Colors");
        transformButton.setEnabled(false);

        JFileChooser fileChooser = new JFileChooser();

        openButton.addActionListener(e -> {
            int returnValue = fileChooser.showOpenDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    BufferedImage image = ImageIO.read(selectedFile);
                    if (image != null) {
                        imageLabel.setIcon(new ImageIcon(image));
                        imageLabel.setText("");
                        imageLabel.putClientProperty("originalImage", image);
                        transformButton.setEnabled(true);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Failed to load image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        transformButton.addActionListener(e -> {
            BufferedImage originalImage = (BufferedImage) imageLabel.getClientProperty("originalImage");
            if (originalImage != null) {
                BufferedImage transformedImage = transformImageColors(originalImage);
                imageLabel.setIcon(new ImageIcon(transformedImage));
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openButton);
        buttonPanel.add(transformButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(imageLabel, BorderLayout.CENTER);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static BufferedImage transformImageColors(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);

                // Extract color components
                int alpha = (rgb >> 24) & 0xFF;
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                // Swap R -> G, G -> B, B -> R
                int newRed = green;
                int newGreen = blue;
                int newBlue = red;

                // Maintain alpha channel
                int newRGB = (alpha << 24) | (newRed << 16) | (newGreen << 8) | newBlue;
                result.setRGB(x, y, newRGB);
            }
        }

        return result;
    }
}
