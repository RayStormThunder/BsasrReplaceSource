package AudioApplied;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.JOptionPane;

public class Final {
    public static void main(String[] args) {
        try {
            // Read the entire content of the input file
            String hexContent = new String(Files.readAllBytes(Paths.get("Output\\WZSound.brsar.txt")), StandardCharsets.UTF_8);

            // Convert the hex string to bytes
            byte[] bytes = hexStringToByteArray(hexContent);

            // Write the bytes to the output file
            Files.write(Paths.get("Output\\WZSound.brsar"), bytes);
            JOptionPane.showMessageDialog(null, "Program Finished");
        } catch (IOException e) {
            System.out.println("Error reading or writing the input/output file: " + e.getMessage());
        } finally {
            System.exit(0);
        }
    }

    private static byte[] hexStringToByteArray(String hexString) {
        int length = hexString.length();
        byte[] bytes = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return bytes;
    }
}