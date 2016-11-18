/*
 Author: Clayton Brezinski
 Student ID: 200220989
 Notes:
 */
package ENSE479Project;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.Clip;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**

 @author Dolan
 */
public class ENSE479Project
{

    /**
     @param args the command line arguments
     */
    public static Mixer mixer;
    public static Clip clip;

    public static void main(String[] args)
    {
        // display the system info for the user
        // the file can be found within the project folder under: build/classes/resources
        Mixer.Info[] mixInfoArray = AudioSystem.getMixerInfo();
        for (Mixer.Info info : mixInfoArray)
        {
            System.out.println(info.getName() + "--" + info.getDescription());
        }
        System.out.println("Starting Sound Test");
        String soundFile = "record.wav";
        URL soundURL = ENSE479Project.class.getResource("/resources/" + soundFile);
        int timeToRecord = 3000;
        try
        {
            final File audioFile = new File(soundURL.toURI());
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

                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                final SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);
                sourceLine.open();

                info = new DataLine.Info(TargetDataLine.class, format);
                final TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(info);
                targetLine.open();

                final ByteArrayOutputStream out = new ByteArrayOutputStream();

                // check if the DataLine is supported with the audio system that we are working with on this computer
                if (AudioSystem.isLineSupported(info) == false)
                {
                    System.err.println("Dataline is not supported");
                    System.exit(0);
                }

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
                System.out.println("Start recording");
                Thread.sleep(timeToRecord);
                targetLine.stop();
                targetLine.close();
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
        catch (URISyntaxException urise)
        {
            urise.printStackTrace();
            System.exit(0);
        }

// ------------------------------------------------------------------------------------------
        System.out.println("Begin playback from the file");
        // setup the mixer
        mixer = AudioSystem.getMixer(mixInfoArray[0]);

        // create a dataline in the format of a clip
        AudioFormat format = null;
        DataLine.Info dataInfo = new DataLine.Info(Clip.class, format);
        try
        {
            clip = (Clip) mixer.getLine(dataInfo);
        }
        catch (LineUnavailableException e)
        {
            e.printStackTrace();
        }

        // put a sound file in the clip
        try
        {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
            clip.open(audioStream); //clip fully loads the file before playing it
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        catch (LineUnavailableException lue)
        {
            lue.printStackTrace();
        }
        catch (UnsupportedAudioFileException uafe)
        {
            uafe.printStackTrace();
        }

        // play the clip
        // new thread is opened with this call. The main thread terminates with this new thread opened.
        clip.start();

        // to counteract the main thread terminating we perform a dowhile loop to work around this. 
        // clipstart takes a few mS to start 
        do
        {
            try
            {
                Thread.sleep(50);
            } // have this thread sleep for 50ms
            catch (InterruptedException ie)
            {
                ie.printStackTrace();
            }
        }
        while (clip.isActive() == true);
    }
}

/*
 Purpose:
 Arguments:
 Outputs:
 Notes:
 */
