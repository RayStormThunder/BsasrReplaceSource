package fast;

import java.io.*;
import javax.swing.JOptionPane;

public class trial {
    public static void main(String[] args) throws IOException {

        File output = new File("output.txt");
        File replaceLocations = new File("replaceLocations.txt");
        if (replaceLocations.exists()) {
            replaceLocations.delete();
        }
        replaceLocations.createNewFile();

        // Open the output file for writing
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(output), "UTF-8"))) {

        writer.write("Program Started\n\n");
    	
        JOptionPane.showMessageDialog(null, "Program Started");
        // Create the WZBackups folder
        File wzBackups = new File("WZModified");
        if (!wzBackups.exists()) {
            try {
                wzBackups.mkdir();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error creating the WZBackups folder: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                writer.write("Error creating the WZBackups folder: " + e.getMessage() + "\n");
                writer.close();
                System.exit(0);
                return;
            }
        }

        // Copy WZSound.bsasr to the WZBackups folder
        File wzSound = new File("WZSound.brsar");
        try (InputStream wzSoundStream = new FileInputStream(wzSound);
             OutputStream wzBackupStream = new FileOutputStream(new File(wzBackups, "WZSound.brsar"))) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = wzSoundStream.read(buffer)) > 0) {
                wzBackupStream.write(buffer, 0, length);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error copying WZSound.brsar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            writer.write("Error copying WZSound.brsar: " + e.getMessage() + "\n");
            writer.close();
            System.exit(0);
            return;
        }

        // Create the Original Audio and Replacement Audio folders
        File originalAudio = new File("Original Audio\\modified");
        if (!originalAudio.exists()) {
            try {
                originalAudio.mkdir();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error creating the Original Audio folder: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                writer.write("Error creating the Original Audio folder: " + e.getMessage() + "\n");
                writer.close();
                System.exit(0);
                return;
            }
        }
        File replacementAudio = new File("Replacement Audio\\modified");
        if (!replacementAudio.exists()) {
            try {
                replacementAudio.mkdir();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error creating the Replacement Audio folder: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                writer.write("Error creating the Replacement Audio folder: " + e.getMessage() + "\n");
                writer.close();
                System.exit(0);
                return;
            }
        }
            for (int i = 1; i <= 500; i++) {
                File audio = new File("Original Audio\\exported\\Audio[" + i + "]");
                File cudio = new File("Replacement Audio\\exported\\Audio[" + i + "]");

                // Check if Audio[i] and Cudio[i] exist
                if (!audio.exists()) {
                    if (cudio.exists()) {
                        // Skip to the end of the loop if Audio[i] does not exist
                        writer.write("Replacement Audio[" + i + "] exists but a corresponding Audio[" + i + "] does not exist in Original Audio\n");
                    }
                    continue;
                } else if (!cudio.exists() && audio.exists()) {                        // Skip to the end of the loop if Cudio[i] does not exist
                    writer.write("Original Audio[" + i + "] exists but a corresponding Audio[" + i + "] does not exist in Replacement Audio\n");
                    continue;     
            }

            File newAudio = new File(originalAudio, "Audio[" + i + "]");
            File newCudio = new File(replacementAudio, "Audio[" + i + "]");
            File newwzSound = new File(wzBackups, "WZSound.brsar");

            // Copy Audio[i] to the Original Audio folder
            try (InputStream audioStream = new FileInputStream(audio);
                 OutputStream newAudioStream = new FileOutputStream(newAudio)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = audioStream.read(buffer)) > 0) {
                    newAudioStream.write(buffer, 0, length);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error copying Replacement Audio[" + i + "] to the Original Audio folder: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                writer.write("Error copying Replacement Audio[" + i + "] to the Original Audio folder: " + e.getMessage() + "\n");
                writer.close();
                return;
            }

            // Copy Cudio[i] to the Replacement Audio folder
            try (InputStream cudioStream = new FileInputStream(cudio);
                 OutputStream newCudioStream = new FileOutputStream(newCudio)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = cudioStream.read(buffer)) > 0) {
                    newCudioStream.write(buffer, 0, length);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error copying Original Audio[" + i + "] to the Replacement Audio folder: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                writer.write("Error copying Original Audio[" + i + "] to the Replacement Audio folder: " + e.getMessage() + "\n");
                writer.close();
                return;
            }

            // Check the size of Audio[i] compared to Cudio[i]
            if (newAudio.length() > newCudio.length()) {
                // Open the files for reading in binary mode
                try (RandomAccessFile audioFile = new RandomAccessFile(newAudio, "r");
                     RandomAccessFile cudioFile = new RandomAccessFile(newCudio, "rw")) {
                    // Get the length of both files in binary
                    long audioLength = audioFile.length();

                    // Add zeros to the end of Cudio[i] to make the length of both files the same
                    cudioFile.setLength(audioLength);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error checking the size of Original Audio[" + i + "] compared to Replacement Audio[" + i + "]: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    writer.write("Error checking the size of Original Audio[" + i + "] compared to Replacement Audio[" + i + "]: " + e.getMessage() + "\n");
                    writer.close();
                    System.exit(0);
                    return;
                }
                writer.write("Original Audio[" + i + "] is correctly larger than Replacement Audio[" + i + "]\n");
            } else if (newAudio.length() < newCudio.length()) {
                writer.write("Original Audio[" + i + "] is incorrectly smaller than Replacement Audio[" + i + "]\n");
                continue;
            } else if (newAudio.length() == newCudio.length()) {
                writer.write("Original Audio[" + i + "] is correctly equal in size to Replacement Audio[" + i + "]\n");
            }

            // Open the WZSound.bsasr file for reading and writing in binary mode
            try (RandomAccessFile newwzSoundFile = new RandomAccessFile(newwzSound, "rw")) {
                // Read the contents of Audio[i] into a byte array
                byte[] audioData = new byte[(int) newAudio.length()];
                try (InputStream newAudioDataStream = new FileInputStream(newAudio)) {
                    newAudioDataStream.read(audioData);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error reading the contents of Original Audio[" + i + "] into a byte array: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    writer.write("Error reading the contents of Original Audio[" + i + "] into a byte array: " + e.getMessage() + "\n");
                    writer.close();
                    System.exit(0);
                    return;
                }

                // Read the contents of Cudio[i] into a byte array
                byte[] cudioData = new byte[(int) newCudio.length()];
                try (InputStream newCudioDataStream = new FileInputStream(newCudio)) {
                    newCudioDataStream.read(cudioData);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error reading the contents of Replacement Audio[" + i + "] into a byte array: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    writer.write("Error reading the contents of Replacement Audio[" + i + "] into a byte array: " + e.getMessage() + "\n");
                    writer.close();
                    System.exit(0);
                    return;
                }

                // Open the output file for writing
                try (PrintWriter writer2 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(output), "UTF-8"))) {
	
	                // Search the binary data of audio[i] in WZSound.bsasr
	                byte[] newwzSoundData = new byte[(int) newwzSound.length()];
	                newwzSoundFile.seek(0);
	                newwzSoundFile.read(newwzSoundData);
	                for (int j = 0; j <= newwzSoundData.length - audioData.length; j++) {
	                    boolean match = true;
	                    for (int k = 0; k < audioData.length; k++) {
	                        if (newwzSoundData[j + k] != audioData[k]) {
	                            match = false;
	                            break;
	                        }
	                    }
	
	                    // Replace the binary data of It audio[i] with the binary data of cudio[i]
	                    if (match) {
	                        writer2.write("Audio[" + i + "] found at location " + j + " \n");
	                        newwzSoundFile.seek(j);
	                        newwzSoundFile.write(cudioData);
	                    }
	                }writer2.write("\n");
                }
               
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error opening the WZSound.bsasr file for reading and writing in binary mode: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                writer.write("Error opening the WZSound.bsasr file for reading and writing in binary mode: " + e.getMessage() + "\n");
                writer.close();
                System.exit(0);
                return;
            }
        }writer.write("Program Finished");
         writer.close();
    }
    JOptionPane.showMessageDialog(null, "Program Finished");
    System.exit(0);
}
}
                