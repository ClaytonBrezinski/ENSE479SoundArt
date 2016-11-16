/*
 Author: Clayton Brezinski
 Student ID: 200220989
 Notes: This program will take what the microphone on the computer (or connected to the computer)is recording and write it to a file named record.wav that will be saved
 to the where the same directory as this source file.
 */
package soundtutorialpart2;

import java.io.IOException;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.io.File;

/**

 @author Dolan
 */
public class SoundTutorialPart2
{

    /**
     @param args the command line arguments
     */
    public static void main(String[] args)
    {
        System.out.println("Starting Sound Test");
        final File audioFile = new File("record.wav");
        int timeToRecord = 10000;  
        try
        {
            // PCM_Signed because we want to work with .wav file formats
            // 44100 for the sample rate because that is the highest rate .wav can achieve
            // 16 bit sample size
            // 2 for stereo output, 1 is for mono
            // 4 for frame size in bytes
            // 44100 frame rate
            // bigEndian format, we actually want littleEndian for .wav files
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            // check if the DataLine is supported with the audio system that we are working with on this computer
            if (AudioSystem.isLineSupported(info) == false)
            {
                System.err.println("Dataline is not supported");
                System.exit(0);
            }
            final TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(info);
            targetLine.open();  // prepare the system resources for recording

            System.out.println("Start recording");
            targetLine.start(); // begin getting data from the microphone

            // write the recording to a file. 
            Thread thread = new Thread()
            {
                @Override
                public void run()
                {
                    // make audioStream for the targetLine
                    AudioInputStream audioStream = new AudioInputStream(targetLine);
                    try
                    {
                        // write our recording to our audio file. as long as the target dataline gets info from the microphone.
                        AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);    
                    }
                    catch (IOException ioe)
                    {
                        ioe.printStackTrace();
                    }
                    System.out.println("Stopped Recording");
                }
            };
            // we need to thread in order to record because we need 1 thread actually recording and writing the data, and the other thread operating the 
            // .sleep() timeout and then eventually closing the program
            thread.start();
            thread.sleep(timeToRecord); // 500s
            targetLine.stop(); // stop the recording
            targetLine.close(); //close the targetLine
        }
        catch (LineUnavailableException lue)
        {
            lue.printStackTrace();
        }

        catch (InterruptedException ie)
        {
            ie.printStackTrace();
        }

    }

}

/*
 Purpose:
 Arguments:
 Outputs:
 Notes:
 */
