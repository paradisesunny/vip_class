function updateScoreByRule(type) {
    $.ajax({
        type: "GET",
        cache: false,
        async: false,
        dataType: "json",
        url: 'wechat/score/updateScoreByRule',
        data: {
            ruleType: type,
            newsId :$("#newsId").val()
        },
        success: function (json) {
            if (json.success) {
                if(type == "watch_video"){
                    $("#hasAddWatchScore").val(true);
                }
            } else {
                // alert(json.msg);
            }
        }
    });
}