$(".files").contextMenu({
    width: 100, // width
    itemHeight: 30, // 菜单项height
    bgColor: "#fff", // 背景颜色
    color: "#333", // 字体颜色
    fontSize: 12, // 字体大小
    hoverBgColor: "#3498db", // hover背景颜色
    target: function (ele) { // 当前元素
        console.log(ele);
    },
    menu: [{ // 菜单项
        text: " 下载",
        callback: function () {
            let id = $('#tarFile').html();
            var location = window.location.href;
            var domain = $("#domain").html();
            let strings = location.split("/");
            if (id != "") {
                window.location.href = domain + "/downloadFile?time=" + new Date().getTime() + "&tip=" + Math.random() * 1000000 + "&fId=" + id;
            } else {
                return;
            }
        }
    },
        {
            text: " 分享",
            callback: function () {
                $.ajax({
                    url: "getShareUrl?type=file&fId=" + $('#tarFile').html(),
                    dataType: "json",
                    type: "post",
                    async: false,
                    success: function (data) {
                        alert(data.msg);
                    }
                });

            }
        },
        {
            text: "直链下载地址",
            callback: function () {
                var domain = $("#domain").html();
                var location = window.location.href;
                let strings = location.split("/");
                $.ajax({
                    url: "getQrCode/?id=" + $('#tarFile').html() + "&url=" + strings[2] + domain,
                    type: "get",
                    async: false,
                    success: function (data) {
                        var txt = "<img src='" + data['imgPath'] + "' style='width: 150px;height: 150px'/><br>" +
                            "<input style='width: 80%;font-size: 14px' value='" + data['url'] + "'/>";
                        var option = {
                            title: "分享你的文件",
                        };
                        window.wxc.xcConfirm(txt, "custom", option);
                    }
                });
            }
        },
        {
            text: " 在线预览",
            callback: function () {
                var maxShowSize = $('#maxShowSize').html();
                var showType = $('#showType').html();
                var showPath = $(".flag td #showPath").html();
                let id = $('#tarFile').html();
                let name = $('.flag td a').html();
                var domain = $("#domain").html();
                var supLang = ["md", "java", "css", "cpp", "py", "php", "html"];
                var supImg = ["png", "jpg", "jpeg", "bmp", "gif"];
                var supVideo = ["mp4", "wmv", "flv"];
                var supAudio = ["mp3", "wma", "flac"];
                if (supLang.indexOf(name.substring(name.lastIndexOf(".") + 1)) > -1) {
                    window.location.href = domain + "/code/preview?fId=" + id;
                } else if (supImg.indexOf(name.substring(name.lastIndexOf(".") + 1)) > -1) {
                    var image = new Image();
                    // var location = window.location.href;
                    // let strings = location.split("cloudDisk");

                    image.src = showPath;

                    var viewer = new Viewer(image, {
                        hidden: function () {
                            viewer.destroy();
                        },
                    });
                    viewer.show();
                } else if (supVideo.indexOf(name.substring(name.lastIndexOf(".") + 1)) > -1) {

                    let size1 = $('.flag #size1').html();   //mb
                    let size2 = $('.flag #size2').html();   //kb


                    if (size2 == null) {
                        if (parseInt(size1.toString().split("MB")[0]) > parseInt(maxShowSize)) {
                            console.log(size1.toString().split("MB")[0]);
                            alert("超过" + maxShowSize + "M，本云盘只支持" + maxShowSize + "M以下内容在线播放");
                            return;
                        }
                    }

                    var top = ($(window).height() - $(videoElasticframe).height()) / 2;
                    var left = ($(window).width() - $(videoElasticframe).width()) / 2;
                    var scrollTop = $(document).scrollTop();
                    var scrollLeft = $(document).scrollLeft();

                    console.log("在线播放打开");

                    //播放视频时，如果音乐开着在，就关闭
                    var audio = document.getElementById('audioPlayer');
                    if (audio.play()) {
                        audio.pause();
                    }

                    var postion = $("#audioPlayerPosition").text();
                    if (postion == 250) {
                        $("#audioPlayerTool").animate({
                            left: "5px"
                        }, "slow");
                        $('#audioPlayerPosition').html(5);
                        $('#audioPlayerSrc').attr("src", "https://pyyf.oss-cn-hangzhou.aliyuncs.com/community/icons/右.png");
                    }


                    let id = $('#tarFile').html();

                    $("#videoElasticframe").css({
                        position: 'absolute',
                        'top': top + scrollTop,
                        left: left + scrollLeft,
                        zIndex: 10
                    });

                    $('#preMediaId').html("");
                    $('#preMediaId').attr("id", "");

                    $('.flag td div').attr("id", "preMediaId");
                    $('.flag td div').html(" &nbsp; &nbsp; <i class=\"fa fa-refresh fa-spin\"></i>&nbsp; &nbsp;");

                    if (showType == "FTP") {
                        var location = window.location.href;
                        let strings = location.split("/");

                        $("#videoPlayer").attr("src", strings[0] + domain + "/video?fId=" + id);
                    } else
                        $("#videoPlayer").attr("src", showPath);

                    // if (showType == "FTP")
                    //     $("#videoPlayer").html("<source  src=\"" + strings[0] + "cloudDisk/video?fId=" + id + "\" type=\"video/mp4\">");
                    // else
                    //     $("#videoPlayer").html("<source  src=" + showPath + ">");

                    $("#videoElasticframe").slideToggle(300);
                    console.log("显示完成");
                } else if (supAudio.indexOf(name.substring(name.lastIndexOf(".") + 1)) > -1) {

                    let size1 = $('.flag #size1').html();   //mb
                    let size2 = $('.flag #size2').html();   //kb

                    if (size2 == null) {
                        if (parseInt(size1.toString().split("MB")[0]) > parseInt(maxShowSize)) {
                            console.log(size1.toString().split("MB")[0]);
                            alert("超过" + maxShowSize + "M，本云盘只支持" + maxShowSize + "M以下内容在线播放");
                            return;
                        }
                    }

                    var audio = document.getElementById('audioPlayer');
                    if (audio.play()) {
                        audio.pause();
                    }

                    //存在preAudioId，表明这次是切歌
                    $('#preMediaId').html("");
                    $('#preMediaId').attr("id", "");

                    $('.flag td div').attr("id", "preMediaId");
                    $('.flag td div').html(" &nbsp; &nbsp; <i class=\"fa fa-refresh fa-spin\"></i>&nbsp; &nbsp;");

                    if (showType == "FTP") {
                        var location = window.location.href;
                        let strings = location.split("/");
                        //注意是设置audio的属性，不是source的属性，这里我被折腾了两个小时，哎
                        $("#audioPlayer").attr("src", strings[0] + domain + "/audio?fId=" + id);
                    } else
                        $("#audioPlayer").attr("src", showPath);


                    // audio.play(); //播放控制
                    // audio.pause();
                    // $("#audioPlayer").html("<source  src=\"" + strings[0] + "cloudDisk/audio?fId=" + id + "\" type=\"audio/mp3\">");
                    // $("#audioPlayer-container").slideToggle(300);

                    $("#audioPlayerTool").animate({
                        left: "250px"
                    }, "slow");
                    $('#audioPlayerPosition').html(250);
                    $('#audioPlayerSrc').attr("src", "https://pyyf.oss-cn-hangzhou.aliyuncs.com/community/icons/左.png");
                    audio.play(); //播放控制
                    console.log("音乐置换完成");

                } else {
                    alert("不支持的格式");
                }
            }
        },
        {
            text: " 重命名",
            callback: function () {
                let id = $('.flag td  #fileId').html();
                let name = $('.flag td a').html();
                let html = $('.flag td').eq(1).html($('' +
                    "<form id='updateFileNameForm' action='updateFileName' method='post'>" +
                    "<input id='updateFileName' name='myFileName' autocomplete='off' type='text' onblur='checkUpdateFile()' value='" + name + "'>" +
                    "<input type='hidden' name='myFileId' value='" + id + "'>" +
                    "</form>" +
                    ''));
            }
        },
        {
            text: " 删除",
            callback: function () {
                let id = $('#tarFile').html();
                var location = window.location.href;
                var domain = $("#domain").html();
                let strings = location.split("/");
                if (id != "") {
                    window.location.href = strings[0] + domain + "/deleteFile?fId=" + id + "&folder=" + $('#nowF').html();
                } else {
                    return;
                }
            }
        }
    ]
});
$(".folders").contextMenu({
    width: 100, // width
    itemHeight: 30, // 菜单项height
    bgColor: "#fff", // 背景颜色
    color: "#333", // 字体颜色
    fontSize: 12, // 字体大小
    hoverBgColor: "#3498db", // hover背景颜色
    target: function (ele) { // 当前元素
        console.log(ele);
    },
    menu: [{ // 菜单项
        text: " 打开",
        callback: function () {
            let id = $('#tarFolder').html();
            var location = window.location.href;
            let strings = location.split("?");
            if (id != "") {
                window.location.href = strings[0] + "?fId=" + id;
            } else {
                return;
            }
        }
    },
        {
            text: " 分享",
            callback: function () {
                $.ajax({
                    url: "getShareUrl?type=folder&fId=" + $('#tarFolder').html(),
                    dataType: "json",
                    type: "post",
                    async: false,
                    success: function (data) {
                        alert(data.msg);
                    }
                });

            }
        },
        {
            text: " 返回上一级",
            callback: function () {
                toPreFolder();
            }
        },
        {
            text: " 重命名",
            callback: function () {
                let id = $('.flag td #fileId').html();
                let name = $('.flag td a').html();
                let html = $('.flag td').eq(1).html($('' +
                    "<form id='updateFolderForm' action='updateFolder' method='post'>" +
                    "<input id='updateFolderName' name='fileFolderName' autocomplete='off' type='text' onblur='checkUpdateFolder()' value='" + name + "'>" +
                    "<input type='hidden' name='fileFolderId' value='" + id + "'>" +
                    "</form>" +
                    ''));
            }
        },
        {
            text: " 新建文件夹",
            callback: function () {
                addFolderElement();
            }
        },
        {
            text: " 清空并删除",
            callback: function () {
                let id = $('#tarFolder').html();
                var location = window.location.href;
                var domain = $("#domain").html();
                let strings = location.split("/");
                if (id != "") {
                    window.location.href = strings[0] + domain + "/deleteFolder?fId=" + id;
                } else {
                    return;
                }
            }
        }
    ]

});
$(".empty-space").contextMenu({
    width: 100, // width
    itemHeight: 30, // 菜单项height
    bgColor: "#fff", // 背景颜色
    color: "#333", // 字体颜色
    fontSize: 12, // 字体大小
    hoverBgColor: "#3498db", // hover背景颜色
    target: function (ele) { // 当前元素
        console.log(ele);
    },
    menu: [
        {
            text: " 新建文件夹",
            callback: function () {
                addFolderElement();
            }
        },
        {
            text: " 返回上一级",
            callback: function () {
                toPreFolder();
            }
        }
    ]

});

function toPreFolder() {
    var location = window.location.href;
    let strings = location.split("?");
    var pre = $('#preF').html();
    if (pre != "") {
        window.location.href = strings[0] + "?fId=" + pre;
    } else {
        return;
    }
}

function addFolderElement() {
    var now = $('#nowF').html();
    $("<tr class='files-items folders'><td><i style='font-size: 24px;color: orange' class='icon ion-android-folder'></i></td>" +
        "<td>" +
        "<form id='addFolderForm' action='addFolder' method='post'>" +
        "<input id='newFolder'  autocomplete='off' name='fileFolderName' type='text' onblur='checkAddFolder()'>" +
        "<input type='hidden' name='parentFolderId' value='" + now + "'>" +
        "</form>" +
        "</td>" +
        "<td style='font-weight: 300'>文件夹</td>" +
        "<td style='font-weight: 300'>-</td>" +
        "<td style='font-weight: 300'>-</td>" +
        "</tr>").insertAfter($('#files-table-title'));
    $('#newFolder').focus();
}

function checkAddFolder() {
    var name = $.trim($("#newFolder").val());
    var nameReg = /^[\u4E00-\u9FA5A-Za-z0-9]{1,20}$/;
    if (!nameReg.test(name)) {
        alert("文件夹格式错误！【汉字、字母、数字】");
        var location = window.location.href;
        window.location.href = location;
    } else {
        $('#addFolderForm').submit();
    }
}

function checkUpdateFolder() {
    var name = $.trim($("#updateFolderName").val());
    var nameReg = /^[\u4E00-\u9FA5A-Za-z0-9]{2,20}$/;
    if (!nameReg.test(name)) {
        alert("文件夹格式错误！【汉字、字母、数字】");
        var location = window.location.href;
        window.location.href = location;
    } else {
        $('#updateFolderForm').submit();
    }
}

function checkUpdateFile() {
    var name = $.trim($("#updateFileName").val());
    var nameReg = /^[^\u4E00-\u9FA5\uF900-\uFA2D\w-_]{2,20}$/;
    if (!nameReg.test(name)) {
        alert("文件夹格式错误！【汉字、字母、数字】");
        var location = window.location.href;
        window.location.href = location;
    } else {
        $('#updateFileNameForm').submit();
    }
}