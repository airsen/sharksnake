$(function () {

    var testUrl = '/test';
    var startGameUrl = '/start';
    template.openTag = "{{";
    template.closeTag = "}}";

    // 模版
    var playerTemplate = template.render('player');

    // 通知
    function notice(type, message) {
        var noticeTemplate = template.render('notice', {
            type: type,
            message: message
        });
        $('.container').append(noticeTemplate);
        $('.alert').alert();
        setTimeout(function () {
            $('.alert').alert('close');
            $('.alert').remove();
        }, 2000);
    }


    // 增加一位玩家
    $('#add-player').click(function () {
        $('.container form').append(playerTemplate);
    });

    // 去掉一位玩家
    $('#delete-player').click(function () {
        if ($('.container form .clearfix').length < 2)
            return;
        $('.container form .clearfix:last-child').remove();
    });

    // 打开关闭对话框
    $('#config-game').click(function () {
        $('#config-board').modal('show');
    });
    $('.modal-footer .btn').click(function () {
        $('#config-board').modal('hide');
    });

    // 测试连接
    $('.test').live('click', function () {
        var $dom = $(this);
        var type = $dom.parent().find('.active').val();
        var address = $dom.parent().find('.address').val();
        $dom.attr('class', 'btn');
        $dom.val('测试');
        if (type == undefined || address == undefined || type == "" || address == "") {
            notice('error', '格式不对');
            $dom.addClass('btn-warning');
        } else {
            $.get(testUrl, {
                type: type,
                address: address
            }, function (e) {
                if (e.success == true) {
                    notice('error', e.message);
                    $dom.addClass('btn-success');
                }
                else {
                    notice('success', e.message);
                    $dom.addClass('btn-danger');
                }
            }, 'json')
        }
    });

    // 开始游戏
    $('#start-game').click(function () {
        $('.test btn-success')
        $.post(startGameUrl, $('#config').serialize(), function (data) {
            alert(data);
        }, 'json');
    });

    // canvas画画
    (function () {
        var canvas = $('#canvas')[0];
        var context = canvas && canvas.getContext ? canvas.getContext('2d') : {};
        var width = canvas.width, height = canvas.height, size = 10;

        context.fillStyle = '#eee';
        context.fillRect(0, 0, width, height);
    })();

    // 初始化
    $('.container form').append(playerTemplate);


});