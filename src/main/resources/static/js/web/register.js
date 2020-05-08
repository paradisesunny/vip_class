$(document).ready(function () {
    //发送邮箱验证码
    $("#getCodeBtn").click(function () {
        var btn = $(this);
        if(!btn.attr("disabled")){
            btn.attr("disabled",true);
            $.ajax({
                url: "/common/sendEmailCode",
                type: "post",
                cache: false,
                dataType: "json",
                data: {
                    email: $("#L_email").val(),
                    type: "email_register"
                },
                success: function (json) {
                    if (json.success) {
                        layer.open({title:'提示',content:'验证码发送成功'});
                        btn.attr("disabled",true);
                        var time = 60;
                        var set=setInterval(function(){
                            btn.text(--time+" s");
                        }, 1000);

                        setTimeout(function(){
                            btn.attr("disabled",false).html("重新获取");
                            clearInterval(set);
                        }, 60000);
                    } else {
                        layer.open({title:'出错了',content:json.msg});
                    }
                }
            })
        }
    })
});
