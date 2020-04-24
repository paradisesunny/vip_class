//自定义alert弹框，title不显示域名
(function(){
    window.alert = function(name){
        var iframe = document.createElement("IFRAME");
        iframe.style.display="none";
        iframe.setAttribute("src", 'data:text/plain');
        document.documentElement.appendChild(iframe);
        window.frames[0].window.alert(name);
        iframe.parentNode.removeChild(iframe);
    }
})();
$(document).ready(function () {
    //键盘遮挡下弹框的问题
    $('body').on('touchend', function(el) {
        objBlurFun("input",null);
    })
    //弹出小键盘页面没有回退的问题
    let inputs = document.getElementsByTagName('input');
    let timer = null;
    for (let input of inputs) {
        input.addEventListener('blur', function() {
            timer = setTimeout(() => {
                window.scrollTo(0, 0);
            timer = null;
        }, 0);
        }, false);
        input.addEventListener('focus', function() {
            timer && clearTimeout(timer);
        }, false);
    }
});
//如果不是当前触摸点不在input上,那么都失去焦点
function objBlurFun(sDom,time){
    var time = time||300;
    var browser={
        versions:function(){
            var u = navigator.userAgent, app = navigator.appVersion;
            return {
                ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
            };
        }(),
        language:(navigator.browserLanguage || navigator.language).toLowerCase()
    }
    if(browser.versions.ios){
        var obj = document.querySelectorAll(sDom);
        for(var i=0;i<obj.length;i++){
            objBlur(obj[i],time);
        }
    }
}
// 元素失去焦点隐藏键盘
function objBlur(sdom,time){
    var time = time||300;
    if(sdom){
        sdom.addEventListener("focus", function(){
            document.addEventListener("touchend", docTouchend,false);
        },false);

    }else{
        throw new Error("objBlur()没有找到元素");
    }
    var docTouchend = function(event){
        if(event.target!= sdom){
            // setTimeout(function(){
            sdom.blur();
            document.removeEventListener('touchend', docTouchend,false);
            // },time);
        }
    };

}