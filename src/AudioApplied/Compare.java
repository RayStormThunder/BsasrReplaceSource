package AudioApplied;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JOptionPane;

public class Compare {
public static void main(String[] args) {
JOptionPane.showMessageDialog(null, "Program Started");
    	  FileWriter writer = null;
          try {
              // Convert the "WZSound.brsar" file to a .txt file of its hex data
              File file = new File("WZSound.brsar");
              FileInputStream fis = new FileInputStream(file);
              byte[] data = new byte[(int) file.length()];
              fis.read(data);
              fis.close();

              File outputFile = new File("Output\\WZSound.brsar.txt");
              if (!outputFile.exists()) {
                  FileWriter hexWriter = new FileWriter(outputFile);
                  for (int i = 0; i < data.length; i++) {
                      hexWriter.write(String.format("%02X", data[i]));
                  }
                  hexWriter.close();
              }
              
            writer = new FileWriter("Output\\output.txt");

            for (int i = 1; i <= 200; i++) {
                // The two files to be compared
                File file2 = new File("Original Audio\\exported\\Audio[" + i + "]");
                File file1 = new File("Replacement Audio\\exported\\Audio[" + i + "]");

                // Check if the files exist
                if (!file1.exists() && file2.exists()) {
                    writer.write("An Original Audio[" + i + "] exists but a corresponding one in Replacements could not be found\n");
                }
                if (!file2.exists() && file1.exists()) {
                    writer.write("A Replacement Audio[" + i + "] exists but a corresponding one in Original could not be found\n");
                }
                if (file2.exists() && file1.exists()) {
                    writer.write("Both Audio[" + i + "] exist.\n");
                }
                if (!file2.exists() || !file1.exists()) {
                    continue;
                }

                // Compare the sizes of the files
                long size1 = file1.length();
                long size2 = file2.length();
                if (size1 <= size2) {
                    writer.write("The size of Replacement Audio[" + i + "] is correctly smaller than the size of Original Audio[" + i + "]\n");
                } else {
                    writer.write("The size of Replacement Audio[" + i + "] is incorrectly greater than the size of Original Audio[" + i + "]\n");
                }

                // Create a new text file for the audio file and write its hexadecimal representation
                File hexFile1 = new File("Replacement Audio\\txt\\Audio[" + i + "].txt");
                if (!hexFile1.exists()) {
                hexFile1.createNewFile();
                FileWriter hexWriter1 = new FileWriter(hexFile1);
                byte[] file1Bytes = Files.readAllBytes(file1.toPath());
                for (byte b : file1Bytes) {
                    hexWriter1.write(String.format("%02X", b));
                }
                hexWriter1.close();
                }

                File hexFile2 = new File("Original Audio\\txt\\Audio[" + i + "].txt");
                if (!hexFile2.exists()) {
                hexFile2.createNewFile();
                FileWriter hexWriter2 = new FileWriter(hexFile2);
                byte[] file2Bytes = Files.readAllBytes(file2.toPath());
                for (byte b : file2Bytes) {
                    hexWriter2.write(String.format("%02X", b));
                }
                hexWriter2.close();
            }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file");
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                System.out.println("An error occurred while closing the file");
                e.printStackTrace();
            }
            
        }
          Modify.main(null);
    }
}