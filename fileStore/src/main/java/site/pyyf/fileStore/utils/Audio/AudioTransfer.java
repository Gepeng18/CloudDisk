package site.pyyf.fileStore.utils.Audio;

import java.io.File;

import ws.schild.jave.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.InputFormatException;
import ws.schild.jave.MultimediaObject;

public class AudioTransfer {

	public static boolean audioToMp3(File source,File target) {

		MultimediaObject msource = new MultimediaObject(source);
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("libmp3lame");
		audio.setBitRate(new Integer(128000));
		audio.setChannels(new Integer(2));
		audio.setSamplingRate(new Integer(44100));

		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("mp3");
		attrs.setAudioAttributes(audio);

		Encoder encoder = new Encoder();
		try {
			encoder.encode(msource, target, attrs);
			return true;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (InputFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;

		} catch (EncoderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;

		}
	}

}
