package audio;

import org.lwjgl.util.vector.Vector3f;

import org.lwjgl.openal.AL10;

public class Source
{
	private int sourceId;
	
	public Source()
	{
		sourceId = AL10.alGenSources();
		setVolume(1);
		setPitch(1);
		setPosition(new Vector3f(0,0,0));
	}
	
	public void play(int buffer)
	{
		stop();
		AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
		resume();
	}
	
	public void delete()
	{
		stop();
		AL10.alDeleteSources(sourceId);
	}
	
	public void pause()
	{
		AL10.alSourcePause(sourceId);
	}
	
	public void resume()
	{
		AL10.alSourcePlay(sourceId);
	}
	
	public void stop()
	{
		AL10.alSourceStop(sourceId);
	}
	
	public void setRolloff(float value)
	{
		AL10.alSourcef(sourceId, AL10.AL_ROLLOFF_FACTOR, value);
	}
	
	public void setReferenceDistance(float value)
	{
		AL10.alSourcef(sourceId, AL10.AL_REFERENCE_DISTANCE, value);
	}
	
	public void setMaxDistance(float value)
	{
		AL10.alSourcef(sourceId, AL10.AL_MAX_DISTANCE, value);
	}
	
	public void setVelocity(Vector3f vel)
	{
		AL10.alSource3f(sourceId, AL10.AL_VELOCITY, vel.x, vel.y, vel.z);
	}
	
	public void setLoop(boolean loopean)
	{
		AL10.alSourcef(sourceId, AL10.AL_LOOPING, loopean ? AL10.AL_TRUE : AL10.AL_FALSE);
	}
	
	public boolean isPlaying()
	{
		return AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}
	
	public void setVolume(float value)
	{
		AL10.alSourcef(sourceId, AL10.AL_GAIN, value);
	}
	
	public void setPitch(float value)
	{
		AL10.alSourcef(sourceId, AL10.AL_PITCH, value);
	}
	
	public void setPosition(Vector3f position)
	{
		AL10.alSource3f(sourceId, AL10.AL_POSITION, position.x, position.y, position.z);
	}
}
