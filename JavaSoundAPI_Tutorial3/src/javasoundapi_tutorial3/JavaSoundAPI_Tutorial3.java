/*
 To change this license header, choose License Headers in Project Properties.
 To change this template file, choose Tools | Templates
 and open the template in the editor.
 */
package JavaSoundAPI_Tutorial3;

import java.io.ByteArrayOutputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

/**

 @author Dolan
 */
public class JavaSoundAPI_Tutorial3
{
    /**
     @param args the command line arguments
     */
    public static void main(String[] args)
    {
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
        try
        {
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            final SourceDataLine sourceLine = (SourceDataLine)AudioSystem.getLine(info);
            sourceLine.open();

            info = new DataLine.Info(TargetDataLine.class, format);
            final TargetDataLine targetLine = (TargetDataLine)AudioSystem.getLine(info);
            targetLine.open();

            final ByteArrayOutputStream out = new ByteArrayOutputStream();

            Thread sourceThread = new Thread()
            {
                @Override
                public void run()
                {
                    sourceLine.start(); // play whateve is in the source's buffer.
                    while (true)
                    {
                        sourceLine.write(out.toByteArray(), 0, out.size()); // or out.size()
                    }
                }
            };
            // input stuff from the microphone
            Thread targetThread = new Thread()
            {
                @Override
                public void run()
                {
                    targetLine.start();
                    byte[] data = new byte[targetLine.getBufferSize() / 5];
                    int readBytes;
                    while (true)
                    {
                        // deposit information into the data buffer and hold the number of bytes written to the buffer.
                        readBytes = targetLine.read(data, 0, data.length);
                        out.write(data, 0, readBytes);
                    }
                }
            };

            targetThread.start();
            System.out.println("Start recording");
            Thread.sleep(5000);
            targetLine.stop();
            targetLine.close();

            System.out.println("End recording");
            System.out.println("Start playback");
            sourceThread.start();
            Thread.sleep(5000);
            sourceLine.stop();
            sourceLine.close();
            System.out.println("End playback");
//
        }
        catch (LineUnavailableException lue)
        {
            lue.printStackTrace();
        }
        catch (InterruptedException ie)
        {
            ie.printStackTrace();
        }
        System.exit(0);
    }

}
