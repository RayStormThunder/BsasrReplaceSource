package AudioApplied;

import java.io.*;
import java.nio.file.*;

public class Replace {
    public static void main(String[] args) {
        // Loop through numbers 1 to 200
        for (int i = 1; i <= 200; i++) {
            String originalFileName = "Original Audio\\txt\\Audio[" + i + "].txt";
            String replacementFileName = "Replacement Audio\\txt\\Audio[" + i + "].txt";
            String targetFileName = "Output\\WZSound.brsar.txt";
            
            // Get the length of the original file
            long originalLength = 0;
            try {
                originalLength = Files.size(Paths.get(originalFileName));
            } catch (IOException e) {
                System.out.println("Error reading file: " + originalFileName);
                continue;
            }

            // Get the length of the replacement file
            long replacementLength = 0;
            try {
                replacementLength = Files.size(Paths.get(replacementFileName));
            } catch (IOException e) {
                System.out.println("Error reading file: " + replacementFileName);
                continue;
            }

            // Check if the replacement file is bigger in size
            if (replacementLength > originalLength) {
                try {
                    Files.write(Paths.get("Output\\output2.txt"), ("Audio[" + i + "].txt was too big\n").getBytes());
                } catch (IOException e) {
                    System.out.println("Error writing file: output2.txt");
                }
                continue;
            }

            // Read the contents of the original file
            String originalText = "";
            try {
                originalText = new String(Files.readAllBytes(Paths.get(originalFileName)));
            } catch (IOException e) {
                System.out.println("Error reading file: " + originalFileName);
                continue;
            }

            // Read the contents of the replacement file
            String replacementText = "";
            try {
                replacementText = new String(Files.readAllBytes(Paths.get(replacementFileName)));
            } catch (IOException e) {
                System.out.println("Error reading file: " + replacementFileName);
                continue;
            }

            // Read the contents of the target file
            String targetText = "";
            try {
                targetText = new String(Files.readAllBytes(Paths.get(targetFileName)));
            } catch (IOException e) {
                System.out.println("Error reading file: " + targetFileName);
                continue;
            }

            // Replace all instances of the original text with the replacement text
            targetText = targetText.replace(originalText, replacementText);

            // Write the modified text back to the target file
            try {
                Files.write(Paths.get(targetFileName), targetText.getBytes());
            } catch (IOException e) {
                System.out.println("Error writing file: " + targetFileName);
            }
        }
        Final.main(null);
    }
}