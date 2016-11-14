/*
 To change this license header, choose License Headers in Project Properties.
 To change this template file, choose Tools | Templates
 and open the template in the editor.
 */
package ense479soundartproject;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
/**

 @author Dolan
 */
public class ENSE479SoundArtProject
{

    public static Mixer mixer;
    
    public static void main(String[] args)
    {
        Mixer.Info[] mixInfo = AudioSystem.getMixerInfo();
        for (Mixer.Info info : mixInfo)
        {
            System.out.println(info.getName() + "--" + info.getDescription());
        }
    }
    
}
