/*
This program will accept a .wave file that has a name corresponding to the soundFile String within this program. It will play it and then close it immediately after. 
 */
package ense479soundartproject;
import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
/**

 @author Dolan
 */

public class ENSE479SoundArtProject
{
    public static Mixer mixer;
    public static Clip clip;
    public static void main(String[] args) throws UnsupportedAudioFileException
    {
        
        // this api accepts .WAV files nativly. 
        Mixer.Info[] mixInfoArray = AudioSystem.getMixerInfo();
        for (Mixer.Info info : mixInfoArray)
        {
            System.out.println(info.getName() + "--" + info.getDescription());
        }
        // setup the mixer
        mixer = AudioSystem.getMixer(mixInfoArray[0]);
        
        // create a dataline in the format of a clip
        AudioFormat format = null;
        DataLine.Info dataInfo = new DataLine.Info(Clip.class, format);  
        try
        {
            clip = (Clip)mixer.getLine(dataInfo);
        }
        catch(LineUnavailableException e)
        {
            e.printStackTrace();
        }
        
        // put a sound file in the clip
        try 
        {
            String soundFile = "Lunar Intro.wav";
            URL soundURL = ENSE479SoundArtProject.class.getResource("/resources/" + soundFile);
           AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
           clip.open(); //clip fully loads the file before playing it
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
        catch(LineUnavailableException lue)
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
            catch(InterruptedException ie)
            {
                ie.printStackTrace();
            }
        }
        while(clip.isActive() == true);
        
    }
    
}
