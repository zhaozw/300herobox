function urlGet() {
	var aQuery = window.location.href.split("?");//取得Get参数
	var aGET = new Array();
	if(aQuery.length > 1) {
        var aBuf = aQuery[1].split("&");
		for(var i=0, iLoop = aBuf.length; i<iLoop; i++) {
			var aTmp = aBuf[i].split("=");//分离key与Value
			aGET[aTmp[0]] = decodeURI(aTmp[1]);
		}
	}
	return aGET;
}


function Fighting_capacity(sc,zc){
	var dwhsf,jcf,sl,scf,jscf,sljcf,zf;
	sl = parseInt(sc / zc * 100);
	jcf = 1500;
	scf = parseInt(2 * Math.pow(sc,0.9));
	if(scf >= 2500){
		scf = 2500;
	}
	if(sl > 50){
		jscf =(sc - (zc - sc)) * (1.6 + (sl / 100 - 0.5) * 10);
		if(jscf>1000){
			jscf = 1000;
		}
		scf = scf + jscf;
	}
	if(sl > 70){
		sl = 70;
	}
	if(sl > 50){
		sljcf = 750 + 100 * Math.pow(sl - 50,0.7);
	}else{
		sljcf = 750 - 100 * Math.pow(50 - sl,0.7);
	}
	sljcf = parseInt(sljcf);
	if(sc > 1000){
		sljcf = sljcf * 1.5;
	}else if(sc > 500){
		sljcf = sljcf * 1.4;
	}else if(sc > 300){
		sljcf = sljcf * 1.3;
	}else if(sc > 100){
		sljcf = sljcf * 1.2;
	}else if(sc > 50){
		sljcf = sljcf * 1.1;
	}else if(sc < 40){
		sljcf = sljcf * sc / 40;
	}
	sljc = parseInt(sljcf);
	sljcf = parseInt(sljcf * 1.2);
	zf = parseInt(jcf + scf + sljcf);
	return zf;
}







