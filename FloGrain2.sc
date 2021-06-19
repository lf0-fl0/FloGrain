FloGrain2 {
	*ar {
		arg buf = 0,freq = 1,rFreq = 0, dense = 1, rDense = 0, rDenseSpeed = 1,
		size=0.1, rSize =0, speed=1,rSpeed=0,rSpeedFreq=0,start=0,end=1, rPos=0,
		pan=0,rPan=0,amp=1, rAmp=0,rAmpSpeed=1,win= -1,posOffset=0;

		var info, impulse, freqmod, posrand, densemod, sizemod, ampmod, panmod, speedmod, head, sound;


		densemod = LFDNoise1.ar(rDenseSpeed).range(0,2)**rDense; //random walk

		info = BufFrames.kr(buf); //Get framesize of the audiobuffer

		impulse = Impulse.ar((dense+densemod).clip(0,550)); //trigger for GrainBuf generating a grain + densemod

		freqmod = (TRand.ar(0,rFreq,impulse)**3)/4; //frequency modulation triggered by impulse

		ampmod = LFDNoise1.ar(rAmpSpeed).abs * rAmp; //amplitude modulation with random walk (better triggered ?)

		sizemod = TGaussRand.ar(0, size*rSize,impulse); //sizemod is relative to size: good/bad?

		posrand = TRand.ar(rPos.neg, rPos,impulse); //random read-position for Grainbufs "head" or readpoint

		panmod = LFDNoise1.kr(0.1)*rPan; //pan is -1/1 so this adds random values
		//arround the actual "pan" value. 0 in pan and 1 in rPan is max stereo

		speedmod = LFNoise2.ar(rSpeedFreq,rSpeed/SampleRate.ir); //speed modulation of readhead

		head = Phasor.ar(1, (speed*(BufRateScale.kr(buf)/info)) - speedmod, start, end, start) % BufFrames.kr(buf); //readhead points to audiobuffer

		sound = GrainBufJ.ar(2, impulse, size+sizemod, buf, //standart grainbuf object
			freq+freqmod, (head + posrand + posOffset).max(0) , 4, pan + panmod, mul: 1-ampmod, envbufnum:win);

		^sound*amp //get it out
	}
}
