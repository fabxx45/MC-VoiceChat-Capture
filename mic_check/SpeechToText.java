package mic_check;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpeechToText {
    private static final Logger logger = Logger.getLogger(SpeechToText.class.getName());

    public static void main(String[] args) {
        String modelPath = "C:\\x\\x\\x\\x\\MC-VoiceChat-Capture\\vosk-model-en-us-0.22";

        try (Model model = new Model(modelPath);
             Recognizer recognizer = new Recognizer(model, 16000);
             TargetDataLine line = getMicrophone()) {

            System.out.println("Listening... Speak into the microphone.");
            byte[] buffer = new byte[4096];
            line.start();

            // Handle graceful shutdown
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\nStopping recognition...");
                if (line.isOpen()) {
                    line.stop();
                    line.close();
                }
            }));

            while (true) {
                int bytesRead = line.read(buffer, 0, buffer.length);
                if (bytesRead > 0) {
                    if (recognizer.acceptWaveForm(buffer, bytesRead)) {
                        System.out.println("Text: " + recognizer.getResult());
                    } else {
                        System.out.println("Partial: " + recognizer.getPartialResult());
                    }
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred", e);
        }
    }

    private static TargetDataLine getMicrophone() throws LineUnavailableException {
        AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            throw new LineUnavailableException("Microphone not supported.");
        }
        TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
        line.open(format);
        return line;
    }
}