import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DarkSmartAttendanceSystem {
    private static final String QR_CODE_IMAGE_PATH = "MyQRCode.png";

    // Dark theme colors
    private static final Color DARK_BG = new Color(18, 18, 18);
    private static final Color DARK_SECONDARY = new Color(30, 30, 30);
    private static final Color ACCENT_COLOR = new Color(75, 0, 130);  // Deep purple
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    private static final Color HIGHLIGHT_COLOR = new Color(128, 128, 255);

    // Main frame
    private static JFrame mainFrame;
    private static CardLayout cardLayout;
    private static JPanel mainPanel;

    // Logged in user info
    private static String currentUser = "";

    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Customize UI elements for dark theme
            setDarkLookAndFeel();

            // Create and show main frame
            createAndShowGUI();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setDarkLookAndFeel() {
        UIManager.put("Panel.background", DARK_BG);
        UIManager.put("OptionPane.background", DARK_BG);
        UIManager.put("OptionPane.messageForeground", TEXT_COLOR);
        UIManager.put("TextField.background", DARK_SECONDARY);
        UIManager.put("TextField.foreground", TEXT_COLOR);
        UIManager.put("TextField.caretForeground", TEXT_COLOR);
        UIManager.put("PasswordField.background", DARK_SECONDARY);
        UIManager.put("PasswordField.foreground", TEXT_COLOR);
        UIManager.put("Button.background", ACCENT_COLOR);
        UIManager.put("Button.foreground", TEXT_COLOR);
        UIManager.put("Label.foreground", TEXT_COLOR);
        UIManager.put("ComboBox.background", DARK_SECONDARY);
        UIManager.put("ComboBox.foreground", TEXT_COLOR);
        UIManager.put("ComboBox.selectionBackground", ACCENT_COLOR);
        UIManager.put("ComboBox.selectionForeground", TEXT_COLOR);
    }

    private static void createAndShowGUI() {
        // Create main frame
        mainFrame = new JFrame("Smart Attendance System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(600, 500);
        mainFrame.setLocationRelativeTo(null);

        // Use CardLayout for switching between screens
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(DARK_BG);

        // Create different screens
        createLoginPanel();
        createMainMenuPanel();
        createGenerateQRPanel();
        createMarkAttendancePanel();

        // Add main panel to frame
        mainFrame.add(mainPanel);

        // Show the login screen first
        cardLayout.show(mainPanel, "login");
        mainFrame.setVisible(true);
    }

    private static void createLoginPanel() {
        JPanel loginPanel = new JPanel(null);
        loginPanel.setBackground(DARK_BG);

        // Title label
        JLabel titleLabel = new JLabel("SMART ATTENDANCE SYSTEM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(HIGHLIGHT_COLOR);
        titleLabel.setBounds(100, 40, 400, 40);

        // Logo/image
        JLabel logoLabel = new JLabel(new ImageIcon(createAppLogo(100, 100)));
        logoLabel.setBounds(250, 90, 100, 100);

        // Username field
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(TEXT_COLOR);
        userLabel.setBounds(150, 210, 100, 25);

        JTextField userField = new JTextField();
        userField.setBounds(250, 210, 200, 25);
        userField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)));

        // Password field
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(TEXT_COLOR);
        passLabel.setBounds(150, 250, 100, 25);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(250, 250, 200, 25);
        passField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)));

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(250, 300, 100, 35);
        loginButton.setBackground(ACCENT_COLOR);
        loginButton.setForeground(TEXT_COLOR);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(new RoundedBorder(10));

        loginButton.addActionListener(e -> {
            if (validateLogin(userField.getText(), new String(passField.getPassword()))) {
                currentUser = userField.getText();
                cardLayout.show(mainPanel, "mainMenu");
            } else {
                JOptionPane.showMessageDialog(mainFrame,
                        "Invalid username or password!",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add components to panel
        loginPanel.add(titleLabel);
        loginPanel.add(logoLabel);
        loginPanel.add(userLabel);
        loginPanel.add(userField);
        loginPanel.add(passLabel);
        loginPanel.add(passField);
        loginPanel.add(loginButton);

        // Add panel to main panel
        mainPanel.add(loginPanel, "login");
    }

    private static void createMainMenuPanel() {
        JPanel menuPanel = new JPanel(null);
        menuPanel.setBackground(DARK_BG);

        // Title at top
        JLabel titleLabel = new JLabel("SMART ATTENDANCE SYSTEM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(HIGHLIGHT_COLOR);
        titleLabel.setBounds(100, 30, 400, 30);

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome to the system", SwingConstants.CENTER);
        welcomeLabel.setForeground(TEXT_COLOR);
        welcomeLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        welcomeLabel.setBounds(150, 70, 300, 20);

        // Create option buttons
        JButton generateQRButton = createMenuButton("Generate QR Code", 200, 130);
        generateQRButton.addActionListener(e -> cardLayout.show(mainPanel, "generateQR"));

        JButton markAttendanceButton = createMenuButton("Mark Attendance", 200, 200);
        markAttendanceButton.addActionListener(e -> cardLayout.show(mainPanel, "markAttendance"));

        JButton logoutButton = createMenuButton("Logout", 200, 270);
        logoutButton.addActionListener(e -> {
            currentUser = "";
            cardLayout.show(mainPanel, "login");
        });

        JButton exitButton = createMenuButton("Exit System", 200, 340);
        exitButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(mainFrame,
                    "Are you sure you want to exit?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // Status bar at bottom
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(DARK_SECONDARY);
        statusPanel.setBounds(0, 440, 600, 30);

        JLabel statusLabel = new JLabel("  Ready");
        statusLabel.setForeground(TEXT_COLOR);
        statusPanel.add(statusLabel);

        // Add components
        menuPanel.add(titleLabel);
        menuPanel.add(welcomeLabel);
        menuPanel.add(generateQRButton);
        menuPanel.add(markAttendanceButton);
        menuPanel.add(logoutButton);
        menuPanel.add(exitButton);
        menuPanel.add(statusPanel);

        // Add panel to main panel
        mainPanel.add(menuPanel, "mainMenu");
    }

    private static void createGenerateQRPanel() {
        JPanel qrPanel = new JPanel(null);
        qrPanel.setBackground(DARK_BG);

        // Title
        JLabel titleLabel = new JLabel("Generate QR Code", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(HIGHLIGHT_COLOR);
        titleLabel.setBounds(150, 30, 300, 30);

        // Input field for QR content
        JLabel contentLabel = new JLabel("Content for QR Code:");
        contentLabel.setForeground(TEXT_COLOR);
        contentLabel.setBounds(100, 80, 150, 25);

        JTextField contentField = new JTextField();
        contentField.setBounds(250, 80, 250, 25);
        contentField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)));

        // Add placeholder for QR code display
        JPanel qrDisplayPanel = new JPanel();
        qrDisplayPanel.setBackground(DARK_SECONDARY);
        qrDisplayPanel.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR));
        qrDisplayPanel.setBounds(175, 120, 250, 250);

        JLabel qrImageLabel = new JLabel("QR code will appear here", SwingConstants.CENTER);
        qrImageLabel.setForeground(TEXT_COLOR);
        qrDisplayPanel.add(qrImageLabel);

        // Buttons
        JButton generateButton = new JButton("Generate QR");
        generateButton.setBounds(150, 390, 150, 35);
        generateButton.setBackground(ACCENT_COLOR);
        generateButton.setForeground(TEXT_COLOR);
        generateButton.setFocusPainted(false);
        generateButton.setBorder(new RoundedBorder(10));

        generateButton.addActionListener(e -> {
            String content = contentField.getText().trim();
            if (!content.isEmpty()) {
                generateQRCode(content, QR_CODE_IMAGE_PATH);

                try {
                    // Clear panel and add new QR code image
                    qrDisplayPanel.removeAll();
                    BufferedImage qrImage = ImageIO.read(new File(QR_CODE_IMAGE_PATH));
                    JLabel imageLabel = new JLabel(new ImageIcon(qrImage));
                    qrDisplayPanel.add(imageLabel);
                    qrDisplayPanel.revalidate();
                    qrDisplayPanel.repaint();

                    JOptionPane.showMessageDialog(mainFrame,
                            "QR Code Generated Successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame,
                        "Please enter content for the QR Code",
                        "Input Required",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton backButton = new JButton("Back to Menu");
        backButton.setBounds(310, 390, 150, 35);
        backButton.setBackground(DARK_SECONDARY);
        backButton.setForeground(TEXT_COLOR);
        backButton.setFocusPainted(false);
        backButton.setBorder(new RoundedBorder(10));

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "mainMenu"));

        // Add components
        qrPanel.add(titleLabel);
        qrPanel.add(contentLabel);
        qrPanel.add(contentField);
        qrPanel.add(qrDisplayPanel);
        qrPanel.add(generateButton);
        qrPanel.add(backButton);

        // Add panel to main panel
        mainPanel.add(qrPanel, "generateQR");
    }

    private static void createMarkAttendancePanel() {
        JPanel attendancePanel = new JPanel(null);
        attendancePanel.setBackground(DARK_BG);

        // Title
        JLabel titleLabel = new JLabel("Mark Attendance", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(HIGHLIGHT_COLOR);
        titleLabel.setBounds(150, 30, 300, 30);

        // Location field
        JLabel locationLabel = new JLabel("Your Location:");
        locationLabel.setForeground(TEXT_COLOR);
        locationLabel.setBounds(100, 80, 120, 25);

        String[] locationOptions = {"Select Location", "Campus", "Outside"};
        JComboBox<String> locationComboBox = new JComboBox<>(locationOptions);
        locationComboBox.setBounds(220, 80, 280, 25);
        locationComboBox.setBackground(DARK_SECONDARY);
        locationComboBox.setForeground(TEXT_COLOR);

        // Student ID field
        JLabel studentIdLabel = new JLabel("Student ID:");
        studentIdLabel.setForeground(TEXT_COLOR);
        studentIdLabel.setBounds(100, 120, 120, 25);

        JTextField studentIdField = new JTextField();
        studentIdField.setBounds(220, 120, 280, 25);
        studentIdField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)));

        // Course field
        JLabel courseLabel = new JLabel("Course Code:");
        courseLabel.setForeground(TEXT_COLOR);
        courseLabel.setBounds(100, 160, 120, 25);

        JTextField courseField = new JTextField();
        courseField.setBounds(220, 160, 280, 25);
        courseField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)));

        // Status area
        JPanel statusArea = new JPanel();
        statusArea.setLayout(new BorderLayout());
        statusArea.setBackground(DARK_SECONDARY);
        statusArea.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR));
        statusArea.setBounds(100, 200, 400, 150);

        JTextArea statusTextArea = new JTextArea();
        statusTextArea.setEditable(false);
        statusTextArea.setBackground(DARK_SECONDARY);
        statusTextArea.setForeground(TEXT_COLOR);
        statusTextArea.setLineWrap(true);
        statusTextArea.setWrapStyleWord(true);
        statusTextArea.setMargin(new Insets(10, 10, 10, 10));
        statusTextArea.setText("Status: Ready to mark attendance.\nPlease fill in the details above and click 'Scan QR & Mark Attendance'.");

        JScrollPane scrollPane = new JScrollPane(statusTextArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(DARK_SECONDARY);
        statusArea.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JButton scanButton = new JButton("Scan QR & Mark Attendance");
        scanButton.setBounds(150, 370, 300, 35);
        scanButton.setBackground(ACCENT_COLOR);
        scanButton.setForeground(TEXT_COLOR);
        scanButton.setFocusPainted(false);
        scanButton.setBorder(new RoundedBorder(10));

        scanButton.addActionListener(e -> {
            String location = (String) locationComboBox.getSelectedItem();
            String studentId = studentIdField.getText().trim();
            String courseCode = courseField.getText().trim();

            if (location.equals("Select Location")) {
                JOptionPane.showMessageDialog(mainFrame,
                        "Please select your location",
                        "Input Required",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (studentId.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame,
                        "Please enter your Student ID",
                        "Input Required",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (location.equals("Campus")) {
                // If on campus, proceed with attendance marking
                String scannedData = "";

                // Try to scan QR code (in real app, this would use camera)
                if (courseCode.isEmpty()) {
                    // If course code not manually entered, try to scan from QR
                    try {
                        scannedData = scanQRCode(QR_CODE_IMAGE_PATH);
                        if (scannedData == null) {
                            JOptionPane.showMessageDialog(mainFrame,
                                    "Failed to scan QR Code. Please try again or enter course code manually.",
                                    "Scan Failed",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(mainFrame,
                                "Error scanning QR Code: " + ex.getMessage(),
                                "Scan Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    // Use manually entered course code
                    scannedData = courseCode;
                }

                // Mark attendance with current timestamp
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String timestamp = now.format(formatter);

                // Show success message
                statusTextArea.setText("✅ ATTENDANCE MARKED SUCCESSFULLY\n\n" +
                        "Student ID: " + studentId + "\n" +
                        "Course/Class: " + scannedData + "\n" +
                        "Timestamp: " + timestamp + "\n" +
                        "Location: On Campus");

                // In a real app, this would save to database
            } else {
                // If not on campus, show error
                statusTextArea.setText("❌ ATTENDANCE MARKING FAILED\n\n" +
                        "You appear to be outside campus.\n" +
                        "Please make sure you are physically present\n" +
                        "on campus to mark attendance.");
            }
        });

        JButton backButton = new JButton("Back to Menu");
        backButton.setBounds(225, 420, 150, 35);
        backButton.setBackground(DARK_SECONDARY);
        backButton.setForeground(TEXT_COLOR);
        backButton.setFocusPainted(false);
        backButton.setBorder(new RoundedBorder(10));

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "mainMenu"));

        // Add components
        attendancePanel.add(titleLabel);
        attendancePanel.add(locationLabel);
        attendancePanel.add(locationComboBox);
        attendancePanel.add(studentIdLabel);
        attendancePanel.add(studentIdField);
        attendancePanel.add(courseLabel);
        attendancePanel.add(courseField);
        attendancePanel.add(statusArea);
        attendancePanel.add(scanButton);
        attendancePanel.add(backButton);

        // Add to main panel
        mainPanel.add(attendancePanel, "markAttendance");
    }

    private static JButton createMenuButton(String text, int y, int height) {
        JButton button = new JButton(text);
        button.setBounds(150, y, 300, height);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(15));

        return button;
    }

    private static boolean validateLogin(String username, String password) {
        // Hardcoded login (same as original)
        return username.equals("admin") && password.equals("1234");
    }

    private static void generateQRCode(String text, String filePath) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);

            Path path = new File(filePath).toPath();
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String scanQRCode(String filePath) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(filePath));
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static BufferedImage createAppLogo(int width, int height) {
        BufferedImage logo = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = logo.createGraphics();

        // Set rendering hints
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background circle
        g2d.setColor(ACCENT_COLOR);
        g2d.fillOval(0, 0, width, height);

        // Draw QR code icon
        g2d.setColor(TEXT_COLOR);
        int padding = width / 4;
        g2d.fillRect(padding, padding, width - (2 * padding), height - (2 * padding));

        // Draw QR code-like squares
        int sqSize = width / 10;
        g2d.setColor(ACCENT_COLOR);

        // Draw some squares to make it look like a QR code
        g2d.fillRect(padding + sqSize, padding + sqSize, sqSize, sqSize);
        g2d.fillRect(width - padding - (2 * sqSize), padding + sqSize, sqSize, sqSize);
        g2d.fillRect(padding + sqSize, height - padding - (2 * sqSize), sqSize, sqSize);
        g2d.fillRect(width - padding - (2 * sqSize), height - padding - (2 * sqSize), sqSize, sqSize);

        g2d.dispose();
        return logo;
    }

    // Custom rounded border
    private static class RoundedBorder implements Border {
        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(c.getBackground());
            ((Graphics2D) g).setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g.fillRoundRect(x, y, width-1, height-1, radius, radius);
        }
    }
}