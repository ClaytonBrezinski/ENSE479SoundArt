/*
 Author: Clayton Brezinski
 Student ID: 200220989
 Notes: This program will receive microphone input from the user and will play it back for them. It will prompt them asking if they would like to save the audio
        clip as is, or if they would like to slow the clip down by 50%. If they chose to slow it down, it will play it for them again and then save the file 
        with the name that the user requests. 
 */
package ENSE479Project;

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
public class ENSE479Project
{

    /**
    Ask the user how long they would like to record for -- set a hard cap to 10s
    Ask do the recording, ask if they would like to record a different one. 
    Give a dialogue to listen to the recording again or to save it, or to slow it down by 50%
        Save - prompt the user to input a name to save the file as. 
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
