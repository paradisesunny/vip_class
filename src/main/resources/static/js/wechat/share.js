$(function () {
    $.ajax({
        url: "/wx/portal/getJsSdk",
        type: "POST",
        dataType: "json",
        data: {"url": location.href.split('#')[0]},
        success: function (o) {
            var jsApiList = [
                //所有要调用的 API 都要加到这个列表中
                "onMenuShareTimeline",//分享到朋友圈接口
                "onMenuShareAppMessage",//分享给朋友接口
                "onMenuShareQQ",//分享到qq接口
                "onMenuShareWeibo",//分享到腾讯微博
                "onMenuShareQZone"//分享到QQ空间
            ];
            wx.config({
                debug: false,
                appId: o.data.appId,
                timestamp: o.data.timestamp,
                nonceStr: o.data.nonceStr,
                signature: o.data.signature,
                jsApiList: jsApiList
            });

            wx.error(function (res) {
            });

        },
        error: function (xhr, e, t) {
            //alert(e);
        }
    });
    wx.ready(function(){
        var shareconfig = {
            // 标题不能为空，最多32个汉字（2个英文字符算1个汉字，2个英文状态下的标点符号算1个汉字）。
            title: $('title').html(),
            link: location.href.split('#')[0],
            //缩略图 120 x 120
            // .getScheme() + "://" + request.getServerName() + p + path + "/"
            //注意：图片在测试号不显示~~
            imgUrl: $('meta[name="img"]').attr('content') || 'http://prad-college.kydev.net/res/images/nav1.png',
            // 最多32个汉字。（2个英文字符算1个汉字，2个英文状态下的标点符号算1个汉字）。
            desc:$('meta[name="description"]').attr('content') || $('title').html(),
            success:function () {
                //用户点击了分享后执行的回调函数
                updateScoreByRule($("#news_share").val());
            }
        };
        //分享到朋友圈
        wx.onMenuShareTimeline(shareconfig);
        //分享给朋友
        wx.onMenuShareAppMessage(shareconfig);
        //分享到QQ
        wx.onMenuShareQQ(shareconfig);
        //分享到QQ空间
        wx.onMenuShareQZone(shareconfig);
    });
})