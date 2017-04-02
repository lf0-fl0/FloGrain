/*
Classic Grainsynth with randomizable pitch,amp,dense,size.
Size is reciprocal to dense.You have the size arg as a multiplier.
e.g. size = 2 will overlap 50%.
*/

FloGrain2 {

	*ar {

		arg buf = 0,freq = 1,rFreq = 0,dense = 1, rDense = 0, rDenseSpeed = 1,
		size=0.1, rSize =0, speed=1,rSpeed,rSpeedFreq,start=0,end=1, rPos=0,pan=0,rPan=0,
		amp=1, rAmp=0,rAmpSpeed=1,win= -1;

		var info, impulse, freqmod, posrand, densemod, sizemod, ampmod, panmod, speedmod, head, sound;


		densemod = LFNoise2.ar(rDenseSpeed,dense).abs*rDense; //random walk: max. Grainsize * float


		info = BufFrames.kr(buf); //Get framesize of the audiobuffer

		impulse = Impulse.ar(dense+densemod); //trigger for GrainBuf generating a grain + densemod


		freqmod = TGaussRand.ar(rFreq.neg.min(0), rFreq,impulse); //frequency modulation triggered by impulse

		ampmod = LFNoise2.ar(rAmpSpeed).abs*rAmp; //amplitude modulation withrandom walk (better triggered ?)

		sizemod = TGaussRand.ar(0, size*rSize,impulse); //sizemod is relative to size: good/bad?

		posrand = TGaussRand.ar(rPos.neg, rPos,impulse); //random read-position for Grainbufs "head" or readpoint

		panmod = TGaussRand.ar(rPan.neg,rPan,impulse); //pan is -1/1 so this adds random values arround the actual "pan" value. 0 in pan and 1 in rPan is max stereo

		speedmod = LFNoise2.ar(rSpeedFreq,rSpeed);

		head = Phasor.ar(1, ((BufRateScale.kr(buf)*speed) / info) + speedmod, start, (start+end)); //readhead for Grainbufs pos argument. (bufSize*speed)/bufFrames is  "realtime"


		sound = GrainBuf.ar(2, impulse, (dense+densemod).reciprocal*(size+sizemod), buf, //get it in the class
			freq+freqmod, head + posrand ,4,pan+panmod,mul:1-ampmod,envbufnum:win);

		^sound*amp //get it out

	}
}


// * (dense+densemod).reciprocal)

