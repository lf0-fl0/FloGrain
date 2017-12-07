#Granular Synth

This is a grain-sampler build on the GrainBuf class in supercollider.
It contains the modulators i would usually use. 

Parameters are:

buf,          
freq,
rFreq,
dense,
rDense,
rDenseSpeed,
size,
rSize,
speed,
rSpeed,
rSpeedFreq,
start,
end,
rPos,
pan,
rPan,
amp,
rAmp,
rAmpSpeed,
win;

You will need the SC3plugins package for supercollider.

Copy FloGrain2.sc to your supercollider system applicaton folder and recompile
class library. To find your directory use

Platform.systemExtensionDir


Demo:

~buf = SoundFile.collectIntoBuffers("/youreFile(s)Here/*",s);

(
SynthDef(\grains,
	{
		arg gate = 1, buf,freq,rFreq,dense, rDense, rDenseSpeed,
		size, rSize, speed,rSpeed,rSpeedFreq,start,end, rPos,pan,rPan,
		amp, rAmp,rAmpSpeed,win,hall,delay,fft,att,dec,sus,rel,hpf,rHall;

		var rPosM,player,env, mix;

		player = FloGrain2.ar(buf,freq,rFreq,dense,rDense,rDenseSpeed,size,
			rSize,speed,rSpeed,rSpeedFreq,start,end,rPos,pan,rPan,amp,rAmp,rAmpSpeed,win:win)*amp;

		env = EnvGen.ar(Env.adsr(att,dec,sus,rel,1,1),gate:gate,doneAction:2);

		mix = player*env;

		mix = HPF.ar(mix,hpf);

		OffsetOut.ar(0,mix);
	}
).add;
)

(
Pdef(\oldskool,
	Pbind(
		\instrument,\grains,
		\gate,1,
		\buf,~buf[0],
		\win, -1,
		
		\freq,1,
		\rFreq,2.2,

		\legato,1,

		\dense,2,
		\rDense,123,
		\rDenseSpeed,0.1,

		\size,Pkey(\dense).reciprocal*0.2,
		\rSize,1.2,

		\speed,0.1,
		\start,0,
		\end,1,
		\rPos,0.00015,

		\dur,4,

		\hpf,120,

		\legato,1.3,
		\att,4,
		\dec,0.01,
		\sus,1,
		\rel,4,

		\amp,0.5,
		\rAmp,1,
		\rAmpSpeed,0.1,
		
		\pan,0,
		\rPan,1,
	)
).play;
)

lf0
2017
