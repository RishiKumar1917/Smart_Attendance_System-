This code implements a **dark-themed Smart Attendance System** desktop application using Java Swing and the ZXing library for QR code generation and scanning. Here’s a summary of its features and unique aspects:

### What the Code Does
- **Login System:** Simple login screen with hardcoded credentials (`admin`/`1234`).
- **Dark Theme UI:** Custom dark color palette for all UI components, with rounded buttons and modern fonts for better visibility.
- **Main Menu:** Navigation between generating QR codes, marking attendance, logging out, and exiting.
- **QR Code Generation:** Users can generate a QR code from any text input, which is saved as an image and displayed in the app.
- **Attendance Marking:** Users can mark attendance by scanning a QR code (for course code) or entering it manually, with location selection (Campus/Outside) and status feedback.
- **Custom Logo:** The app displays a custom-drawn logo resembling a QR code.
- **Reusable UI Components:** Rounded borders, hover effects, and status panels for a modern look.

### Technologies & Libraries Used
- **Java Swing:** For building the GUI.
- **ZXing (com.google.zxing):** For QR code generation and scanning.
- **Java 2D API:** For custom logo drawing.
- **Maven:** For dependency management (implied by your project setup).

### What Makes It Unique
- **Modern Dark Theme:** Unlike most basic Swing apps, this uses a consistent, visually appealing dark theme with custom colors and rounded UI elements.
- **Integrated QR Code Workflow:** Both QR code generation and scanning are built-in, making it a self-contained attendance solution.
- **Flexible Attendance Marking:** Allows both QR scanning and manual course code entry, with location-based logic.
- **Custom Graphics:** The app logo is programmatically drawn, not just an image file.
- **User Experience:** Hover effects, status messages, and error dialogs enhance usability.

This combination of a modern dark UI, QR code integration, and flexible attendance logic makes your project stand out from typical Java Swing applications. It’s a good candidate for showcasing on GitHub as a practical, visually appealing desktop app.
