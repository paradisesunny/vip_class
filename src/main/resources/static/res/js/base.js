(function (doc, win) {
    var docEl = doc.documentElement,
        resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
        recalc = function () {
            var clientWidth = docEl.clientWidth;
            if (!clientWidth) return;
            if(clientWidth>=750){
                docEl.style.fontSize = '100px';
            }else{
                docEl.style.fontSize = 100*(clientWidth / 750) + 'px';
            }
        };

    if (!doc.addEventListener) return;
    win.addEventListener(resizeEvt, recalc, false);
    doc.addEventListener('DOMContentLoaded', recalc, false);
})(document, window);
$(document).ready(function () {
	
    const winH=$(window).height();    
    $('.bg_box').css("min-height",winH);
	$('.bg2_box').css("min-height",winH);
	
	$("input").on("blur", function () {
		$("body").removeClass("mui-focusin")
	});
	
	$(".growDet-item").on('click','.growSign',function(){
		if($(this).children().hasClass('close-sign')){
			$(this).children().removeClass('close-sign').parents('.growDet-item').next().show();
		}else{
			$(this).children().addClass('close-sign');
			$(this).parents('.growDet-item').next().hide();
		}

	});
    


});
