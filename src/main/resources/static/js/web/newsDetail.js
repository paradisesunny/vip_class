$(document).ready(function(){
    //阅读加分
    updateScoreByRule($("#news_read").val());

    //收藏
    $('.collect_box .collect_block').click(function(){
        var thisDiv = $(this);
        addOrRemoveBookRecord($("#newsId").val()).then(function (value) {
            console.log(value)
            if(value){
                if (!thisDiv.hasClass("active")) {
                    thisDiv.addClass("active");
                } else{
                    thisDiv.removeClass("active");
                }
            }
        });
    });

    //点赞 不可取消
    $('.collect_box .like_block').click(function(){
        var thisDiv = $(this);
        addLikeRecord($("#newsId").val(),null).then(function (value) {
            if(value){
                var oldNum = thisDiv.find(".num").html();
                if (!thisDiv.hasClass("active")) {
                    thisDiv.addClass("active");
                    oldNum++;
                    thisDiv.find(".num").html(oldNum);
                }
            }
        });
    });

    //评论点赞 不可取消
    $('.comment_box .like_block .like').click(function(){
        var thisDiv = $(this);
        addLikeRecord($("#newsId").val(),thisDiv.attr("id")).then(function (value) {
            if(value){
                var oldNum = thisDiv.html();
                if (!thisDiv.hasClass("active")) {
                    thisDiv.addClass("active");
                    oldNum++;
                    thisDiv.html(oldNum);
                }
            }
        });
    });

    //评论
    $('#sendCommentBtn').click(function(){
        if(!$("#pcContent").val()){
            alert("请输入内容！");
            return false;
        }
        $(this).attr("disabled");
        isComment().then(function(data){
            console.log(data);
            if(data){
                saveComment();
                $(this).removeAttr("disabled");
            }
        });
    });
});

//点赞 不可取消
function addLikeRecord(newsId,commentId) {
    return new Promise(function (resolve, reject) {
        $.ajax({
            type: "GET",
            cache: false,
            async: false,
            dataType: "json",
            url: '/wechat/news/addLikeRecord',
            data: {
                newsId: newsId,
                commentId:commentId,
            },
            success: function (json) {
                if (json.success) {
                    //点赞加积分
                    updateScoreByRule($("#news_like").val());
                    resolve(true);
                }else{
                    alert(json.msg);
                    resolve(false);
                }
            },
        });
    })
}

// 收藏/取消收藏
function addOrRemoveBookRecord(newsId) {
    return new Promise(function (resolve, reject) {
        $.ajax({
            type: "GET",
            cache: false,
            async: false,
            dataType: "json",
            url: '/wechat/news/addOrRemoveBookRecord',
            data: {
                newsId: newsId,
                url:window.location.href,
            },
            success: function (json) {
                if (json.success) {
                    resolve(true);
                }else{
                    alert(json.msg);
                    resolve(false);
                }
            }
        });
    })
}

//是否评论过
function isComment() {
    return new Promise(function (resolve, reject) {
        $.ajax({
            type: "GET",
            cache: false,
            async: false,
            dataType: "json",
            url: '/wechat/news/getCommentById',
            data: {
                newsId: $("#newsId").val(),
            },
            success: function (json) {
                if (json.success) {
                    resolve(true);
                }else{
                    alert(json.msg);
                    resolve(false);
                }
            }
        });
    })
}

//保存评论
function saveComment(){
    $.ajax({
        type: "GET",
        cache: false,
        async: false,
        dataType: "json",
        url: '/wechat/news/saveComment',
        data: {
            pcNewsId: $("#newsId").val(),
            pcContent:$("#pcContent").val(),
        },
        success: function (json) {
            if (json.success) {
                location.reload();
            }else{
                alert(json.msg);
            }
        }
    })
}