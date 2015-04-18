function update(){
	mui.ajax('http://wz.tohkalove.com/api/android/update.php',{
		dataType:'json',
		type:'GET',
		timeout:10000,
		success:function(json){
			if(compareVersion(plus.runtime.version,json.version)){
				plus.ui.confirm(json.note,
					function(i){
						if(!i){
							plus.runtime.openURL(json.url);
						}
					},
					json.title,
					[
						"立即更新",
						"取消更新"
					]
				);
			}
		},
		error:function(xhr,type,errorThrown){
			mui.toast('网络连接超时！请稍后再试！\n错误响应：' + xhr.status);
		}
	});
}

function compareVersion( ov, nv ){
	if ( !ov || !nv || ov=="" || nv=="" ){
		return false;
	}
	var b=false,
	ova = ov.split(".",4),
	nva = nv.split(".",4);
	for ( var i=0; i<ova.length&&i<nva.length; i++ ) {
		var so=ova[i],no=parseInt(so),sn=nva[i],nn=parseInt(sn);
		if ( nn>no || sn.length>so.length  ) {
			return true;
		} else if ( nn<no ) {
			return false;
		}
	}
	if ( nva.length>ova.length && 0==nv.indexOf(ov) ) {
		return true;
	}
}