var ue;
$(document).ready(function () {
    // 实例化编辑器
    ue = UE.getEditor('container', {
        // topOffset: 50,//浮动时工具栏距离浏览器顶部的高度，用于某些具有固定头部的页面
        initialFrameHeight: 400
    });
});

layui.use(['form', 'laydate', 'upload'], function () {
    var $ = layui.jquery,
        form = layui.form
        , layer = layui.layer
        , laydate = layui.laydate
        , upload = layui.upload;
    getExperts();
    getCategory();
    //发布日期选择
    laydate.render({
        elem: '#nePublishDateStr'
    });
    //监听是否有效显示开关
    form.on('switch(isValid)', function (data) {
        $('#neIsValid').val(this.checked ? 1 : 0);
    });
    //监听轮播图显示开关
    form.on('switch(banner)', function (data) {
        $('#neIsCarousel').val(this.checked ? 1 : 0);
    });
    //监听来源选择按钮
    form.on('radio(cs)', function (data) {
        if ($('#checkFrom input[name="neFrom"]:checked').val() == '0') {
            $(".count").hide();
        } else {
            $(".count").show();
        }
        form.render();
    });
    //缩略图图片上传
    var uploadneThumbnailImg = upload.render({
        elem: '#uploadThumbnail' //绑定元素
        , url: '/admin/upload/uploadPic'
        , data: {
            type: $("#imgType").val()
        }
        , accept: 'images'
        , auto: false
        , choose: function (obj) {
            obj.preview(function (index, file, result) {
                var img = new Image();
                img.onload = function () {
                    console.log('choose poster', img.width, img.height)
                    if (img.width / img.height <= 2 && img.width / img.height > 1) {
                        obj.upload(index, file);
                    } else {
                        layer.msg('您上传的图片像素宽高比必须是:2:1');
                    }
                };
                img.src = result;
            })
        }
        , done: function (res) {
            var msg = res.msg;
            if (res.success == true) {
                msg = "上传成功";
                $("#imgThumbnail").attr("src", res.data);
                $("#neThumbnailPath").attr("value", res.data);
            }
            layer.alert(msg);
            return false;
        }
        , error: function () {
            //演示失败状态，并实现重传
            var msgText = $('.msgText');
            msgText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
            msgText.find('.demo-reload').on('click', function () {
                // uploadneThumbnailImg.upload();
            });
        }
    });
    //轮播图上传
    var uploadneCarouselImg = upload.render({
        elem: '#uploadCarousel' //绑定元素
        , url: '/admin/upload/uploadPic' //上传接口
        , data: {
            type: $("#imgType").val()
        }
        , accept: 'images'
        , auto: false
        , choose: function (obj) {
            obj.preview(function (index, file, result) {
                var img = new Image();
                img.onload = function () {
                    console.log('choose poster', img.width, img.height)
                    if (img.width / img.height <= 2 && img.width / img.height > 1) {
                        obj.upload(index, file);
                    } else {
                        layer.msg('您上传的图片像素宽高比必须是:2:1');
                    }
                };
                img.src = result;
            })
        }
        , done: function (res) {
            var msg = res.msg;
            if (res.success == true) {
                msg = "上传成功";
                $("#imgCarouselPath").attr("src", res.data);
                $("#neCarouselPath").attr("value", res.data);
            }
            layer.alert(msg);
        }
        , error: function () {
            //演示失败状态，并实现重传
            var msgText = $('.msgText');
            msgText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
            msgText.find('.demo-reload').on('click', function () {
                // uploadneCarouselImg.upload();
            });
        }
    });

});

//发送查询专家列表异步请求
function getExperts() {
    $.ajax({
        url: '/admin/front/news/getExpertList',
        success: function (res) {
            if (res.success) {
                console.log(res);
                for (var i = 0; i < res.data.length; i++) {
                    var expertId = res.data[i].peId;
                    if(expertId == $("#expertId").val()){
                        $("#neExpertId").append('<option value="' + expertId + '" selected>' + res.data[i].peName + '</option>');
                    }else{
                        $("#neExpertId").append('<option value="' + expertId + '">' + res.data[i].peName + '</option>');
                    }
                }
                //重新渲染
                layui.form.render("select");
            } else {
                layer.msg(res.message);
            }
        }
    });
}


//发送查询专题列表异步请求
function getCategory() {
    $.ajax({
        url: '/admin/front/news/getCategory',
        success: function (res) {
            if (res.success) {
                console.log(res);
                var ncIdsStr = $("#neNcIds").val();
                var ncIds;
                if(ncIdsStr){
                    if(ncIdsStr.indexOf(",")>=0){
                        ncIds = ncIdsStr.split(",");
                    }else{
                        ncIds = ncIdsStr;
                    }
                }
                for (var i = 0; i < res.data.length; i++) {
                    var ncId = res.data[i].ncId;
                    var ncName = res.data[i].ncName;
                    $("select[name='neNcIds']").append('<option value="' + ncId + '">' + ncName + '</option>');
                }
                if(ncIds && ncIds.length >0){
                    for(var j = 0;j < ncIds.length; j++){
                        if(ncIds[j]){
                            $("select[name='neNcIds']:eq("+j+")").val(ncIds[j]);
                        }
                    }
                }
                //重新渲染
                layui.form.render("select");
            } else {
                layer.msg(res.message);
            }
        }
    });
}