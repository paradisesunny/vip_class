var layer,tree,util;
layui.use(['tree','util'], function () {
    layer = layui.layer, tree = layui.tree, util = layui.util;
    //开启节点操作图标
    tree.render({
        elem: '#dataTree'
        ,data: getTag()
        ,id: 'treeId'
        ,showCheckbox: true
        ,onlyIconControl: true//是否仅允许节点左侧图标控制展开收缩
        ,click: function(obj){
            layer.msg(JSON.stringify(obj.data));
        }
    });

});

function getTag() {
    var data;
    $.ajax({
        type: "get",
        url: "/admin/newsCate/listTagAjax",
        async: false,
        data:{newsId:$("#newsId").val()},
        success: function (res) {
            data = res.data;
        }
    });
    return data;
}

function checkNewsTag() {
    // alert($("#layerDemo input[type='checkbox']:checked").val());
    // var checkedData = tree.getChecked('treeId');
    // layer.alert(JSON.stringify(checkedData), {shade:0});
    var str ="";
    $("#layerDemo input[type='checkbox']:checked").each(function (index, item) {
        str += $(this).val() + ",";
    });
    // alert(str);
    $("#neTagIds").val(str);
}