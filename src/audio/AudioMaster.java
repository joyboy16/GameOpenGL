package audio;

// Make all the imports
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.LWJGLException;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.WaveData;

// Master of the audio
public class AudioMaster
{
	// Initiate the variables
	private static List<Integer> audioBuffers;
	
	// init-method
	public static void init()
	{
		// Initiate the audio buffers
		audioBuffers = new ArrayList<Integer>();
		try
		{
			// Try to create the audio library
			AL.create();
		} catch (LWJGLException e)
		{
			// Catch error and print it
			e.printStackTrace();
		}
	}
	
	// set all listener datas
	public static void setListenerData(Vector3f position)
	{
		// position
		AL10.alListener3f(AL10.AL_POSITION, position.x, position.y, position.z);
		
		// velocity
		AL10.alListener3f(AL10.AL_VELOCITY, 0,0,0);
	}
	
	// load a sound
	public static int loadSound(String soundFile)
	{
		// generate a new buffer
		int soundBuffer = AL10.alGenBuffers();
		// and add it to the buffer list
		audioBuffers.add(soundBuffer);
		
		// create the waveData,
		WaveData waveSoundFile = WaveData.create(soundFile);
		// fill the buffer
		AL10.alBufferData(soundBuffer, waveSoundFile.format, waveSoundFile.data, waveSoundFile.samplerate);
		// and dispose the waveData
		waveSoundFile.dispose();
		
		// return the buffer
		return soundBuffer;
	}
	
	// clean all buffers
	public static void cleanUP()
	{
		// go through every buffer and delete it
		for(int audioBuffer:audioBuffers) AL10.alDeleteBuffers(audioBuffer);
		
		// get rid of the audio library
		AL.destroy();
	}
}
