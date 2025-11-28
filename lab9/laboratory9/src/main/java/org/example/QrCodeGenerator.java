import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

public class QrCodeGenerator extends JFrame {

    private JLabel       label;
    private JTextField   textField;
    private JButton      generateButton;
    private JLabel       qrCodeLabel;

    public QrCodeGenerator() {
        setTitle("Генератор QR Code");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        label         = new JLabel("Вставьте ссылку:");
        textField     = new JTextField(32);
        generateButton = new JButton("Сгенерировать QR Code");
        qrCodeLabel   = new JLabel();

        generateButton.addActionListener(e -> generateQRCode());

        JPanel panel = new JPanel(new FlowLayout());
        panel.add(label);
        panel.add(textField);
        panel.add(generateButton);
        panel.add(qrCodeLabel);

        add(panel);
        setVisible(true);
    }

    private void generateQRCode() {
        String text = textField.getText().trim();

        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Введите ссылку перед генерацией QR-кода.",
                    "Предупреждение",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            BitMatrix bitMatrix = generateBitMatrix(text);
            BufferedImage qrCodeImage = createImageFromBitMatrix(bitMatrix);
            qrCodeLabel.setIcon(new ImageIcon(qrCodeImage));
        } catch (WriterException ex) {
            JOptionPane.showMessageDialog(this,
                    "Ошибка при создании QR-кода: " + ex.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private BitMatrix generateBitMatrix(String text) throws WriterException {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        return qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 250, 250, hints);
    }

    private BufferedImage createImageFromBitMatrix(BitMatrix bitMatrix) {
        int width  = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB();
                image.setRGB(x, y, rgb);
            }
        }

        return image;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new QrCodeGenerator();
        });
    }
}