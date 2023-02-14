use std::fs;
use std::fs::File;
use std::io::prelude::*;
use std::io::SeekFrom;
use std::path::PathBuf;
use fs::OpenOptions;

fn main() {
    let output_file = PathBuf::from("output.txt");
    let replace_locations_file = PathBuf::from("replaceLocations.txt");

    // Delete replaceLocations.txt if it exists
    if replace_locations_file.exists() {
        fs::remove_file(&replace_locations_file).unwrap();
    }

    // Create replaceLocations.txt
    let _replace_locations = File::create(&replace_locations_file).unwrap();

    // Open the output file for writing
    let mut output_writer = File::create(&output_file).unwrap();
    writeln!(output_writer, "Program Started\n").unwrap();

    // Display message box
    println!("Program Started");

    // Create the WZBackups folder
    let wz_backups = PathBuf::from("WZModified");
    if !wz_backups.exists() {
        match fs::create_dir(&wz_backups) {
            Ok(_) => {}
            Err(e) => {
                writeln!(
                    output_writer,
                    "Error creating the WZBackups folder: {}\n",
                    e
                )
                .unwrap();
                println!("Error creating the WZBackups folder: {}", e);
                return;
            }
        }
    }

    // Copy WZSound.bsasr to the WZBackups folder
    let wz_sound = PathBuf::from("WZSound.brsar");
    match fs::copy(&wz_sound, wz_backups.join("WZSound.brsar")) {
        Ok(_) => {}
        Err(e) => {
            writeln!(
                output_writer,
                "Error copying WZSound.brsar: {}\n",
                e
            )
            .unwrap();
            println!("Error copying WZSound.brsar: {}", e);
            return;
        }
    }

    // Create the Original Audio and Replacement Audio folders
    let original_audio = PathBuf::from("Original Audio/modified");
    if !original_audio.exists() {
        match fs::create_dir_all(&original_audio) {
            Ok(_) => {}
            Err(e) => {
                writeln!(
                    output_writer,
                    "Error creating the Original Audio folder: {}\n",
                    e
                )
                .unwrap();
                println!("Error creating the Original Audio folder: {}", e);
                return;
            }
        }
    }

    let replacement_audio = PathBuf::from("Replacement Audio/modified");
    if !replacement_audio.exists() {
        match fs::create_dir_all(&replacement_audio) {
            Ok(_) => {}
            Err(e) => {
                writeln!(
                    output_writer,
                    "Error creating the Replacement Audio folder: {}\n",
                    e
                )
                .unwrap();
                println!("Error creating the Replacement Audio folder: {}", e);
                return;
            }
        }
    }

    for i in 1..=500 {
        let audio = PathBuf::from(format!("Original Audio/exported/Audio[{}]", i));
        let cudio = PathBuf::from(format!("Replacement Audio/exported/Audio[{}]", i));

        // Check if Audio[i] and Cudio[i] exist
        if !audio.exists() {
            if cudio.exists() {
                // Skip to the end of the loop if Audio[i] does not exist
                writeln!(
                    output_writer,
                    "Replacement Audio[{}] exists but a corresponding Audio[{}] does not exist in Original Audio\n",
                    i,
                    i
                );
                continue;
            }
        } else if !cudio.exists() && audio.exists() {
// Skip to the end of the loop if Cudio[i] does not exist
writeln!(
    output_writer,
    "Original Audio[{}] exists but a corresponding Audio[{}] does not exist in Replacement Audio\n",
    i,
    i
    );
    continue;
    }
let new_audio = original_audio.join(format!("Audio[{}]", i));
let new_cudio = replacement_audio.join(format!("Audio[{}]", i));
let new_wz_sound = wz_backups.join("WZSound.brsar");

// Copy Audio[i] to the Original Audio folder
match fs::copy(&audio, &new_audio) {
    Ok(_) => {}
    Err(e) => {
        writeln!(
            output_writer,
            "Error copying Replacement Audio[{}] to the Original Audio folder: {}\n",
            i,
            e
        )
        .unwrap();
        println!(
            "Error copying Replacement Audio[{}] to the Original Audio folder: {}",
            i, e
        );
        return;
    }
}

// Copy Cudio[i] to the Replacement Audio folder
match fs::copy(&cudio, &new_cudio) {
    Ok(_) => {}
    Err(e) => {
        writeln!(
            output_writer,
            "Error copying Original Audio[{}] to the Replacement Audio folder: {}\n",
            i,
            e
        )
        .unwrap();
        println!(
            "Error copying Original Audio[{}] to the Replacement Audio folder: {}",
            i, e
        );
        return;
    }
}

// Check the size of Audio[i] compared to Cudio[i]
if new_audio.metadata().unwrap().len() > new_cudio.metadata().unwrap().len() {
    // Open the files for reading in binary mode
    let audio_file = File::open(&new_audio).unwrap();
    let mut cudio_file = File::create(&new_cudio).unwrap();
    let audio_length = audio_file.metadata().unwrap().len();

    // Add zeros to the end of Cudio[i] to make the length of both files the same
    cudio_file.seek(SeekFrom::Start(audio_length)).unwrap();
    cudio_file.write_all(&[0; 1]).unwrap();

    writeln!(
        output_writer,
        "Original Audio[{}] is correctly larger than Replacement Audio[{}]\n",
        i,
        i
    )
    .unwrap();
} else if new_audio.metadata().unwrap().len() < new_cudio.metadata().unwrap().len() {
    writeln!(
        output_writer,
        "Original Audio[{}] is incorrectly smaller than Replacement Audio[{}]\n",
        i,
        i
    )
    .unwrap();
    continue;
} else {
    writeln!(
        output_writer,
        "Original Audio[{}] is correctly equal in size to Replacement Audio[{}]\n",
        i,
        i
    )
    .unwrap();
}

// Open the WZSound.bsasr file for reading and writing in binary mode
let mut new_wz_sound_file = OpenOptions::new().write(true).read(true).open(&new_wz_sound).unwrap();


// Read the contents of Audio[i] into a byte array
let mut audio_data = Vec::new();
let mut audio_data_file = File::open(&new_audio).unwrap();
audio_data_file.read_to_end(&mut audio_data).unwrap();

// Read the contents of Cudio[i] into a byte array
let mut cudio_data = Vec::new();
let mut _cudio_data_file = File::open(&new_cudio).unwrap();
_cudio_data_file.read_to_end(&mut cudio_data).unwrap();
let replace_locations = "replaceLocations.txt";
let mut replace_locations_file = OpenOptions::new()
.create(true)
.write(true)
.append(true)
.open(&replace_locations)
.unwrap();

// Search the binary data of audio[i] in WZSound.bsasr
let mut new_wz_sound_data = Vec::new();
new_wz_sound_file.read_to_end(&mut new_wz_sound_data).unwrap();
writeln!(
replace_locations_file,
"Length of WZSound: {} Bytes",
new_wz_sound_data.len()
)
.unwrap();
writeln!(
replace_locations_file,
"Length of Audio File: {} Bytes",
audio_data.len()
)
.unwrap();

for j in 0..=new_wz_sound_data.len() - audio_data.len() {
let match_found = (0..audio_data.len())
    .all(|k| new_wz_sound_data[j + k] == audio_data[k]);

if match_found {
    writeln!(
        replace_locations_file,
        "Audio[{}] found at location {} ",
        i, j
    )
    .unwrap();
    new_wz_sound_file.seek(SeekFrom::Start(j as u64)).unwrap();
    match new_wz_sound_file.write_all(&mut cudio_data) {
        Ok(_) => {}
        Err(e) => {
            writeln!(
                replace_locations_file,
                "Error writing data to WZSound.brsar: {}\n",
                e
            )
            .unwrap();
            println!("Error writing data to WZSound.brsar: {}", e);
            return;
        }
    }
}
}
writeln!(replace_locations_file, "").unwrap();
}

writeln!(output_writer, "Program Finished").unwrap();
std::process::exit(0);
}