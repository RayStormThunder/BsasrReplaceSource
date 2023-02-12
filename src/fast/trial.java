package fast;

import java.io.*;
import javax.swing.JOptionPane;

public class trial {
    public static void main(String[] args) throws IOException {
    	JOptionPane.showMessageDialog(null, "Program Started");
        // Create the WZBackups folder
        File wzBackups = new File("WZModified");
        if (!wzBackups.exists()) {
            wzBackups.mkdir();
        }

        // Copy WZSound.bsasr to the WZBackups folder
        File wzSound = new File("WZSound.brsar");
        InputStream wzSoundStream = new FileInputStream(wzSound);
        OutputStream wzBackupStream = new FileOutputStream(new File(wzBackups, "WZSound.brsar"));
        byte[] buffer = new byte[1024];
        int length;
        while ((length = wzSoundStream.read(buffer)) > 0) {
            wzBackupStream.write(buffer, 0, length);
        }
        wzSoundStream.close();
        wzBackupStream.close();

        // Create the Original Audio and Replacement Audio folders
        File originalAudio = new File("Original Audio\\modified");
        if (!originalAudio.exists()) {
            originalAudio.mkdir();
        }
        File replacementAudio = new File("Replacement Audio\\modified");
        if (!replacementAudio.exists()) {
            replacementAudio.mkdir();
        }
    	
        File output = new File("output.txt");

        // Open the output file for writing
        PrintWriter writer = new PrintWriter(new FileWriter(output));

        for (int i = 1; i <= 500; i++) {
        
        	
            File audio = new File("Original Audio\\exported\\Audio[" + i + "]");
            File cudio = new File("Replacement Audio\\exported\\Audio[" + i + "]");
	       
            // Check if Audio[i] and Cudio[i] exist
            if (!audio.exists() || !cudio.exists()) {
                // Skip to the end of the loop if Audio[i] or Cudio[i] do not exist
                continue;
            }
            
            File newAudio = new File(originalAudio, "Audio[" + i + "]");
            File newCudio = new File(replacementAudio, "Audio[" + i + "]");
            File newwzSound = new File(wzBackups, "WZSound.brsar");

            // Copy Audio[i] to the Original Audio folder
            InputStream audioStream = new FileInputStream(audio);
            OutputStream newAudioStream = new FileOutputStream(newAudio);
            while ((length = audioStream.read(buffer)) > 0) {
                newAudioStream.write(buffer, 0, length);
            }
            audioStream.close();
            newAudioStream.close();

            // Copy Cudio[i] to the Replacement Audio folder
            InputStream cudioStream = new FileInputStream(cudio);
            OutputStream newCudioStream = new FileOutputStream(newCudio);
            while ((length = cudioStream.read(buffer)) > 0) {
                newCudioStream.write(buffer, 0, length);
            }
            cudioStream.close();
            newCudioStream.close();

            // Check the size of Audio[i] compared to Cudio[i]
            if (newAudio.length() > newCudio.length()) {
                // Open the files for reading in binary mode
                RandomAccessFile audioFile = new RandomAccessFile(newAudio, "r");
                RandomAccessFile cudioFile = new RandomAccessFile(newCudio, "rw");

                // Get the length of both files in binary
                long audioLength = audioFile.length();

                // Add zeros to the end of Cudio[i] to make the length of both files the same
                cudioFile.setLength(audioLength);

                // Close the files
                audioFile.close();
                cudioFile.close();
                writer.write("Original Audio[" + i + "] is correctly larger than Replacement Audio[" + i + "]\n");
            } else if (newAudio.length() < newCudio.length()) {
                // Skip to the end of the loop if Audio[i] is smaller than Cudio[i]
                writer.write("Original Audio[" + i + "] is incorrectly smaller than Replacement Audio[" + i + "]\n");
                continue;
            }else if(newAudio.length() == newCudio.length()) {
                writer.write("Original Audio[" + i + "] is correctly equal in size to Replacement Audio[" + i + "]\n");
            }

            // Open the WZSound.bsasr file for reading and writing in binary mode
            RandomAccessFile newwzSoundFile = new RandomAccessFile(newwzSound, "rw");

            // Read the contents of Audio[i] into a byte array
            byte[] audioData = new byte[(int) newAudio.length()];
            InputStream newAudioDataStream = new FileInputStream(newAudio);
            newAudioDataStream.read(audioData);
            newAudioDataStream.close();

            // Read the contents of Cudio[i] into a byte array
            byte[] cudioData = new byte[(int) newCudio.length()];
            InputStream newCudioDataStream = new FileInputStream(newCudio);
            newCudioDataStream.read(cudioData);
            newCudioDataStream.close();
            
            // Search the binary data of audio[i] in WZSound.bsasr
            byte[] newwzSoundData = new byte[(int) newwzSound.length()];
            newwzSoundFile.read(newwzSoundData);
            for (int j = 0; j <= newwzSoundData.length - audioData.length; j++) {
                boolean match = true;
                for (int k = 0; k < audioData.length; k++) {
                    if (newwzSoundData[j + k] != audioData[k]) {
                        match = false;
                        break;
                    }
                }

                // Replace the binary data of audio[i] with the binary data of cudio[i]
                if (match) {
                	newwzSoundFile.seek(j);
                	newwzSoundFile.write(cudioData);
                }
            }

            // Close the file
            newwzSoundFile.close();
        }
    	JOptionPane.showMessageDialog(null, "Program Finished");
        writer.close();
    }
}
