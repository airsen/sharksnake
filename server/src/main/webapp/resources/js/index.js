$(function () {

    var testUrl = '/test';
    var startGameUrl = '/start';

    template.openTag = "{{";
    template.closeTag = "}}";

    // 动画的句柄
    var handler;

    // 画图属性
    var canvas = $('#canvas')[0];
    var canvasContext = canvas && canvas.getContext ? canvas.getContext('2d') : {};
    var map = {};
    map.size = 40;
    map.speed = 400;
    map.playerMaxCount = 5;
    var color = {
        0: '#fff',
        1: '#59EE4B',// 食物
        10: '#89D7E6',
        20: '#DCEB58',
        30: 'pink',
        40: '#F22D0A',
        50: 'grey'
    };

    // 模版
    function getPlayerTemplate(index) {
        return template.render('player', {
            color: color[index * 10 + '']
        });
    }

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

    // 可以参加
    function toggleJoinin($dom) {
        var $parent = $dom.parent();
        if ($dom.hasClass('btn-success')) {
            $dom.html('已参与');
            $parent.find('.btn-group button').attr('disabled', 'disabled');
            $parent.find('.address:input[type="text"]').attr('disabled', 'disabled');
            $parent.find('.type[type="hidden"]').val($parent.find('.active').val());
            $parent.find('.address:input[type="hidden"]').val($parent.find('.address:input[type="text"]').val());
//            $parent.find('.type[type="hidden"]').attr('name', 'type').val($parent.find('.active').val());
//            $parent.find('.address:input[type="hidden"]').attr('name', 'address').val($parent.find('.address:input[type="text"]').val());
        } else {
            $dom.html('未参与');
            $parent.find('.btn-group button').removeAttr('disabled');
            $parent.find('.address:input[type="text"]').removeAttr('disabled');
            $parent.find('.type[type="hidden"]').val('');
            $parent.find('.address:input[type="hidden"]').val('');
//            $parent.find('.type[type="hidden"]').removeAttr('name');
//            $parent.find('.address:input[type="hidden"]').removeAttr('name');
        }
    }

    // 增加一位玩家
    $('#add-player').click(function () {
        if ($('.container form .clearfix').length >= map.playerMaxCount) {
            notice('warnning', '当前最多支持5位');
            return;
        }
        $('.container form').append(getPlayerTemplate($('.container form .clearfix').length + 1));
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

    // 调整速度
    $('#speed').change(function () {
        map.speed = $(this).val(); //TODO检查是否是数字
    })

    // 参与
    $('.joinin').live('click', function () {
        if ($(this).parent().find('.test').hasClass('btn-success')) {
            $(this).toggleClass('btn-success');
            toggleJoinin($(this));
        } else {
            notice('warnning', '请先测试通过');
        }
    });

    // 测试连接
    $('.test').live('click', function () {
        var $dom = $(this);
        var type = $dom.parent().find('.active').val();
        var address = $dom.parent().find('.address[type="text"]').val();
        $dom.attr('class', 'btn test');
        if (type == undefined || address == undefined || type == "" || address == "") {
            notice('error', '格式不对');
            $dom.addClass('btn-warning');
            toggleJoinin($dom.parent().find('.join'));
        } else {
            $.get(testUrl, {
                type: type,
                address: address
            }, function (e) {
                if (e.success == true) {
                    notice('success', e.message);
                    $dom.addClass('btn-success');
                }
                else {
                    notice('error', e.message);
                    $dom.addClass('btn-danger');
                    toggleJoinin($dom.parent().find('.join'));
                }
            }, 'json')
        }
    });

    // 开始游戏
    $('#start-game').click(function () {
        if ($(this).hasClass('disabled')) {
            notice('warnning', '表急，还在计算中');
            return;
        } else if ($('.joinin.btn-success').length == 0) {
            return notice('warnning', '要有人参加才行啊…');
            return;
        }
        notice('success', '游戏计算中…');
        $(this).button('loading');
        $.post(startGameUrl, $('#config').serialize(), function (game) {

            // 去掉已有地图
            canvasContext.clearRect(0, 0, canvas.width, canvas.height);
            canvas.width = game.width * map.size;
            canvas.height = game.height * map.size;
//            $(canvas).css({
//                width: '80%',
//                height: '60%'
//            })

            // 初始化地图哦耶
            console.log('游戏的宽度高度:' + game.width + '-' + game.height);
            console.log('现实场地的宽度宽度:' + canvas.width + '-' + canvas.height);
            console.log('横纵坐标单位:' + map.size);

            //开始播放
            var i = 0;
            clearInterval(handler);
            handler = setInterval(function () { // 每帧
                if (i < game.round) {
                    for (var j in game.frames[i]) { // 每增量变化单元格
                        var unit = game.frames[i][j];
                        var value = unit.value;
                        if (value >= 10)
                            value = parseInt(value / 10) * 10;
                        canvasContext.fillStyle = color[value];
                        canvasContext.fillRect(unit.x * map.size, unit.y * map.size, map.size, map.size);
                    }
                }
                i++;
            }, map.speed);
            $('#start-game').button('reset');
        }, 'json');
    });


// 初始化
    $('.container form').append(getPlayerTemplate(1));

})
;