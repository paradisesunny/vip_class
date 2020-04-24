
function countDown(time,id){
    var day_elem = $(id).find('.day');
    var hour_elem = $(id).find('.hour');
    var minute_elem = $(id).find('.minute');
    var second_elem = $(id).find('.second');
    var end_time = new Date(time).getTime(),//月份是实际月份-1
        sys_second = (end_time-new Date().getTime())/1000;
    var timer = setInterval(function(){
        if (sys_second > 1) {
            sys_second -= 1;
            var day = Math.floor((sys_second / 3600) / 24);
            var hour = Math.floor((sys_second / 3600) % 24);
            var minute = Math.floor((sys_second / 60) % 60);
            var second = Math.floor(sys_second % 60);
            day_elem && $(day_elem).text(day);
            //$(hour_elem).text(hour<10?"0"+hour:hour);//计算小时，有天数显示时。
            if(day>0){
                //$(hour_elem).text(hour+day*24);//计算小时 没有天数显示时。
				$(hour_elem).text(hour<10?"0"+hour:hour);//计算小时 没有天数显示时。
            } else {
                $(hour_elem).text(hour<10?"0"+hour:hour);//计算小时 没有天数显示时。
            }

            $(minute_elem).text(minute<10?"0"+minute:minute);//计算分钟
            $(second_elem).text(second<10?"0"+second:second);//计算秒杀
        } else {
            clearInterval(timer);
        }
    }, 1000);
}
