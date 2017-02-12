package audio;

//Make all the imports
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.openal.AL10;

// Class of every source
public class Source
{
	// declare all variables
	private int numberOfSource;
	
	// constructor
	public Source()
	{
		// generate new source
		numberOfSource = AL10.alGenSources();
		
		// set default volume, pitch and position
		int defaultValue = 1;
		setVolume(defaultValue);
		setPitch(defaultValue);
		Vector3f position = new Vector3f(0,0,0);
		setPosition(position);
	}
	
	// play the source
	public void play(int audioBuffer)
	{
		// first stop a playing sound
		stop();
		// fill the buffer
		AL10.alSourcei(numberOfSource, AL10.AL_BUFFER, audioBuffer);
		// play the sound
		resume();
	}
	
	// delete the source
	public void delete()
	{
		// first stop a playing sound
		stop();
		// delete the source
		AL10.alDeleteSources(numberOfSource);
	}
	
	// pause the playing
	public void pause()
	{
		// pause the playing
		AL10.alSourcePause(numberOfSource);
	}
	
	// resume the playing
	public void resume()
	{
		// resume the playing
		AL10.alSourcePlay(numberOfSource);
	}
	
	// stop the playing
	public void stop()
	{
		// stop the playing
		AL10.alSourceStop(numberOfSource);
	}
	
	// set the rollOff-factor for the distance model
	public void setRolloff(float v)
	{
		// set the rollOff-factor for the distance model
		AL10.alSourcef(numberOfSource, AL10.AL_ROLLOFF_FACTOR, v);
	}
	
	// set the reference for the distance model
	public void setReferenceDistance(float v)
	{
		// set the reference for the distance model
		AL10.alSourcef(numberOfSource, AL10.AL_REFERENCE_DISTANCE, v);
	}
	
	// set the maximum distance for the distance model
	public void setMaxDistance(float v)
	{
		// set the maximum distance for the distance model
		AL10.alSourcef(numberOfSource, AL10.AL_MAX_DISTANCE, v);
	}
	
	// set the velocity for the sound
	public void setVelocity(Vector3f v)
	{
		// set the velocity for the sound
		AL10.alSource3f(numberOfSource, AL10.AL_VELOCITY, v.x, v.y, v.z);
	}
	
	// set the loop for the sound
	public void setLoop(boolean b)
	{
		// set the loop for the sound
		if(b)
		{
			AL10.alSourcef(numberOfSource, AL10.AL_LOOPING, AL10.AL_TRUE);
		}
		else
		{
			AL10.alSourcef(numberOfSource, AL10.AL_LOOPING, AL10.AL_FALSE);			
		}
	}
	
	// check if the sound is playing
	public boolean isPlaying()
	{		
		// check if the sound is playing
		return AL10.alGetSourcei(numberOfSource, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}
	
	// set the volume of the sound
	public void setVolume(float v)
	{
		AL10.alSourcef(numberOfSource, AL10.AL_GAIN, v);
	}
	
	// set the volume of the sound
	public void setPitch(float v)
	{
		AL10.alSourcef(numberOfSource, AL10.AL_PITCH, v);
	}
	
	// set the volume of the sound
	public void setPosition(Vector3f p)
	{
		AL10.alSource3f(numberOfSource, AL10.AL_POSITION, p.x, p.y, p.z);
	}
}
