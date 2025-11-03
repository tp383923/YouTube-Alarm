import javax.sound.sampled.*;
import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

public class YouTubeAlarm {
    public static void main(String[] args) {
	        Scanner scanner = new Scanner(System.in);

	        System.out.println("Enter YouTube video URL:");
	        String url = scanner.nextLine();

	        System.out.println("Enter alarm time in seconds from now:");
	        int delaySeconds = scanner.nextInt();

	        // Download audio
	        String outputFile = "alarm_audio.wav";

	        boolean success = downloadAudio(url, outputFile);

	        if (success) {
	            System.out.println("Audio downloaded. Waiting for alarm time...");

	            try {
	                Thread.sleep(delaySeconds * 1000L);
	                playAudio(outputFile);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        } else {
	            System.out.println("Audio download failed.");
	        }
	    }

    /**
	     * Downloads audio from YouTube using yt-dlp
	     */
	    private static boolean downloadAudio(String url, String outputFilename) {
	        try {
	            ProcessBuilder pb = new ProcessBuilder(
	                    "yt-dlp",
	                    "-x",                            // extract audio
	                    "--audio-format", "wav",         // convert to WAV
	                    "-o", outputFilename,            // output filename
	                    url
	            );
	            pb.inheritIO(); // Optional: shows yt-dlp output in console
	            Process process = pb.start();
	            int exitCode = process.waitFor();

	            return exitCode == 0 && Files.exists(Paths.get(outputFilename));
	        } catch (IOException | InterruptedException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

     /**
	     * Plays a WAV audio file
	     */
	    private static void playAudio(String filepath) {
	        try {
	            File audioFile = new File(filepath);
	            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

	            Clip clip = AudioSystem.getClip();
	            clip.open(audioStream);
	            clip.start();

	            // Keep program running until audio finishes
	            Thread.sleep(clip.getMicrosecondLength() / 1000);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	
} 
