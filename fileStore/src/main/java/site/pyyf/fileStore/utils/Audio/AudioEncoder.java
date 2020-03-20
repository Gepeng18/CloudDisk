package site.pyyf.fileStore.utils.Audio;

import java.io.File;

import ws.schild.jave.MultimediaInfo;
import ws.schild.jave.MultimediaObject;

public class AudioEncoder {


	public void getAudioEncoder() {
		File file = new File("");
		MultimediaObject mSource = new MultimediaObject(file);
		try {
			MultimediaInfo mInfo = mSource.getInfo();


		} catch (Exception e) {
			// TODO: handle exception
		}


	}

}
