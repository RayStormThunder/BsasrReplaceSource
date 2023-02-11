package AudioApplied;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Modify {
    public static void main(String[] args) {
        for (int i = 1; i <= 200; i++) {
            // The two files to be compared
            File originalFile = new File("Original Audio\\txt\\Audio[" + i + "].txt");
            File replacementFile = new File("Replacement Audio\\txt\\Audio[" + i + "].txt");

            // Check if the files exist
            if (!originalFile.exists() || !replacementFile.exists()) {
                System.out.println("One or both of the files do not exist");
                continue;
            }

            // Make the size of the replacement file equal to the size of the original file
            try {
                FileWriter fileWriter = new FileWriter(replacementFile, true);
                long difference = originalFile.length() - replacementFile.length();
                for (int j = 0; j < difference; j++) {
                    fileWriter.write("0");
                }
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file");
                e.printStackTrace();
            }
        }
        Replace.main(null);
    }
}