FloGrain2 {

	*ar {

		arg buf = 0,freq = 1,rFreq = 0,dense = 1, rDense = 0, rDenseSpeed = 1,
		size=0.1, rSize =0, speed=1,rSpeed,rSpeedFreq=0,start=0,end=1, rPos=0,pan=0,rPan=0,
		amp=1, rAmp=0,rAmpSpeed=1,win= -1;

		var info, impulse, freqmod, posrand, densemod, sizemod, ampmod, panmod, speedmod, head, sound;


		densemod = LFDNoise1.ar(rDenseSpeed/2,dense).abs*rDense; //random walk: max. Grainsize * float


		info = BufFrames.kr(buf); //Get framesize of the audiobuffer

		impulse = Impulse.ar(dense+densemod); //trigger for GrainBuf generating a grain + densemod


		freqmod = TGaussRand.ar(rFreq.neg.min(0), rFreq,impulse); //frequency modulation triggered by impulse

		ampmod = LFDNoise1.ar(rAmpSpeed).abs*rAmp; //amplitude modulation withrandom walk (better triggered ?)

		sizemod = TGaussRand.ar(0, size*rSize,impulse); //sizemod is relative to size: good/bad?

		posrand = TGaussRand.ar(rPos.neg, rPos,impulse); //random read-position for Grainbufs "head" or readpoint

		panmod = TGaussRand.ar(rPan.neg,rPan,impulse); //pan is -1/1 so this adds random values arround the actual "pan" value. 0 in pan and 1 in rPan is max stereo

		speedmod = 0;//LFNoise2.ar(rSpeedFreq,rSpeed/SampleRate.ir);


		head = Phasor.ar(0, speed/SampleRate.ir , start, (start+end) % 1,start); //readhead for Grainbufs pos argument. (bufSize*speed)/bufFrames is  "realtime"


		sound = GrainBuf.ar(2, impulse,size+sizemod, buf, //get it in the class
			freq+freqmod, (head + posrand).abs ,4, pan+panmod, mul:1-ampmod, envbufnum:win);


		^sound*amp //get it out

	}
}



