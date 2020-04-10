function doShare() {
    $("#submit").html('<i class="fa fa-spinner fa-pulse"></i>&nbsp; 转存中\n');
    var fileFolderId = $('#fileFolderId').val();
    var pwd = $('#pwd').val();
    var domain = $("#domain").html();
    console.log("logggg" + fileFolderId + "  " + pwd);

    $.ajax({
        url: domain + '/share',
        type: 'post',
        data: {
            fileFolderId: $('#fileFolderId').val(),
            pwd: $('#pwd').val()
        },
        datatype: "json",
        success: function (result) {

            var data = JSON.parse(result);
            // 在提示框中显示返回消息

            $('#btngoupdateElasticframe').hide();
            alert(data.msg);
            $("#submit").html('<i class="fa fa-save"></i>&nbsp; 保存');
            // 2秒后,自动隐藏提示框
            setTimeout(function () {
                // 刷新页面
                if (data.code == 200) {
                    window.location.reload();
                }
            }, 100);
        },
        error: function (result) {
            var data = JSON.parse(result);
            // 在提示框中显示返回消息
            console.log(data.msg);
            $("#btngoupdateElasticframe").hide();
            // 2秒后,自动隐藏提示框
            setTimeout(function () {
                // 刷新页面
                if (data.code == 200) {
                    window.location.reload();
                }
            }, 1000);

        }
    });
}


$(function () {
    $('.folders').dblclick(function () {
        let id = $(this).children("td").children("span").html();
        console.log(id);
        var location = window.location.href;
        let strings = location.split("?");
        if (id != "") {
            window.location.href = strings[0] + "?fId=" + id;
        } else {
            return;
        }
    });
    $('.files').dblclick(function () {
        let id = $(this).children("td").children("span").html();
        var location = window.location.href;
        let strings = location.split("cloudDisk");
        var domain = $("#domain").html();
        if (id != "") {
            window.location.href = strings[0] + domain + "/downloadFile?time=" + new Date().getTime() + "&tip=" + Math.random() * 1000000 + "&fId=" + id;
        } else {
            return;
        }
    });
    $('.folders').hover(function () {
        let id = $(this).children("td").children("span").html();
        $('#tarFolder').html(id);
        $(this).siblings().removeClass('flag');
        $(this).addClass('flag');
    }, function () {
    });
    $('.files').hover(function () {
        let id = $(this).children("td").children("span").html();
        $('#tarFile').html(id);
        $(this).siblings().removeClass('flag');
        $(this).addClass('flag');

        let name = $('.flag td a').html();
        var supImg = ["png", "jpg", "jpeg"];

        if (supImg.indexOf(name.substring(name.lastIndexOf(".") + 1)) > -1) {
            //定义X初始坐标量
            var x = 10;
            //定义Y初始坐标量
            var y = 20;
            $(".files").mouseover(function (e) {
                //封装图片TITLE
                var showPath = $(".flag td #showPath").html();
                //声明层对象
                var tooltip = "<div id='thumbnail_img' style='z-index:10;position:absolute'><img src='" + showPath + "' alt='预览图' style='max-width: 400px;max-height: 300px' />" + "</div>";
                //将层追加到文档中
                $("body").append(tooltip);
                //设置层样式
                $("#thumbnail_img").css({
                    "top": (e.pageY + y) + "px",
                    "left": (e.pageX + x) + "px"
                });
            }).mouseout(function () {
                this.title = this.myTitle;
                //移除层
                $("#thumbnail_img").remove();
            }).mousemove(function (e) {
                $("#thumbnail_img").css({
                    "top": (e.pageY + y) + "px",
                    "left": (e.pageX + x) + "px"
                });
            });
        }
    }, function () {
    });
});


function closeAndPause() {
    console.log("属性修改开始");
    var video = document.getElementById('videoPlayer');
    if (video.play) { //如果已暂停则播放
        video.pause(); //播放控制
    }
    // if(video.paused){ //如果已暂停则播放
    //     video.play(); //播放控制
    // }else{ // 已播放点击则暂停
    //     video.pause(); //暂停控制
    // }
    $('#preMediaId').html("");
    $('#preMediaId').attr("id", "");

    $("#videoElasticframe").slideToggle(300);
};

function suoxiao() {
    var postion = $("#audioPlayerPosition").text();
    if (postion == 250) {
        $("#audioPlayerTool").animate({
            left: "5px"
        }, "slow");
        $('#audioPlayerPosition').html(5);
        $('#audioPlayerSrc').attr("src", "https://pyyf.oss-cn-hangzhou.aliyuncs.com/community/icons/右.png");
    } else {
        $("#audioPlayerTool").animate({
            left: "250px"
        }, "slow");
        $('#audioPlayerPosition').html(250);
        $('#audioPlayerSrc').attr("src", "https://pyyf.oss-cn-hangzhou.aliyuncs.com/community/icons/左.png");
    }

}

function closeAndRefresh() {
    window.onbeforeunload = function () {
    };
    window.location.reload();
}

