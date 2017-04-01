/*
Classic Grainsynth with randomizable pitch,amp,dense,size.
Size is reciprocal to dense.You have the size arg as a multiplier.
e.g. size = 2 will overlap 50%.
*/

FloGrain2 {

	*ar {

		arg buf=0,freq=1,rFreq=0,dense=1, rDense = 0, rDenseSpeed = 1,
		size=0.1, rSize = 0, speed=1,start=0,end=1, rPos=0,
		amp=1, rAmp = 0,rAmpSpeed=1,win = -1;

		var info, impulse, freqmod, posrand, densemod, sizemod, ampmod, head, sound;


		densemod = LFNoise2.ar(rDenseSpeed,dense).abs*rDense;


		info = BufFrames.kr(buf);

		impulse = Impulse.ar(dense+densemod);


		freqmod = TGaussRand.ar(rFreq.neg.min(0), rFreq,impulse);

		ampmod = LFNoise2.ar(rAmpSpeed).abs*rAmp;

		sizemod = TGaussRand.ar(0, size*rSize,impulse);

		posrand = TGaussRand.ar(rPos.neg, rPos,impulse);


		head = Phasor.ar(1, (BufRateScale.kr(buf)*speed) / info, start, (start+end));


		sound = GrainBuf.ar(1, impulse, (dense+densemod).reciprocal*(size+sizemod), buf,
			freq+freqmod, head + posrand, 4,mul:1-ampmod,envbufnum:win);

		^sound*amp

	}
}


// * (dense+densemod).reciprocal)

