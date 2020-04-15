#Granular Synth

This is a grain-sampler build on the GrainBuf class in supercollider.
It contains the modulators i would usually use. It will only read
mono files.

##Parameters are:

*buf,          
*freq,
*rFreq,
*dense,
*rDense,
*rDenseSpeed,
*size,
*rSize,
*speed,
*rSpeed,
*rSpeedFreq,
*start,
*end,
*rPos,
*pan,
*rPan,
*amp,
*rAmp,
*rAmpSpeed,
*win;

You will need the SC3plugins package for supercollider.

Copy FloGrain2.sc to your supercollider system applicaton folder and recompile
class library. To find your directory use

Platform.systemExtensionDir


Demo:

~buf = SoundFile.collectIntoBuffers("yourPathHere/*",s);

(
SynthDef(\grains,
	{
		arg gate = 1, buf,freq,rFreq,dense, rDense, rDenseSpeed,
		size,rSize,speed,rSpeed,rSpeedFreq,start,end, rPos,pan,rPan,
		amp,rAmp,rAmpSpeed,win,hall,delay,fft,att,dec,sus,rel,hpf,posOffset;

		var rPosM,player,env, mix;

		player = FloGrain2.ar(buf,freq,rFreq,dense,rDense,rDenseSpeed,size,
			  rSize,speed,rSpeed,rSpeedFreq,start,end,rPos,pan,rPan,amp,rAmp,
			   rAmpSpeed,win,posOffset);

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
		\rFreq,Pseg([0,0,8],[4,8],\lin,1),

		\legato,1,

		\dense,Pseg([40,0.1,1],[4,8],\lin,1),
		\rDense,Pseg([0,0,12],[4,8],\lin,1),
		\rDenseSpeed,4,

		\size,Pkey(\dense).reciprocal*Pseg([0.001,2,1],[4,8],\exp,1),
		\rSize,0,

		\speed,Pseg([1,1,0.0],[4,8],\lin,1),
		\start,Pseries(0,0.05)%1,
		\end,1,
		\rPos,Pseg([0,0,0.2],[4,8],\lin,1),

		\dur,0.25,

		\hpf,120,

		\legato,1.3,
		\att,0.1,
		\dec,0.01,
		\sus,1,
		\rel,0.1,

		\amp,0.5,
		\rAmp,0,
		\rAmpSpeed,0,
		
		\pan,0,
		\rPan,Pseg([0,0,1],[4,8],\lin,1),
	)
).play;
)

lf0
2020
