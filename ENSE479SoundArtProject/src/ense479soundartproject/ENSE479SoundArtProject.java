/*
 To change this license header, choose License Headers in Project Properties.
 To change this template file, choose Tools | Templates
 and open the template in the editor.
 */
package ense479soundartproject;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
/**

 @author Dolan
 */
public class ENSE479SoundArtProject
{

    public static Mixer mixer;
    public static Clip clip;
    public static void main(String[] args)
    {
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
    }
    
}
