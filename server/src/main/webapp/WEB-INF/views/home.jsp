<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>多人贪食蛇哦耶</title>
    <meta name="description" content="天下第一武大会">
    <meta name="author" content="王俊森(jason61719@gmail.com)">

    <!-- TODO 路径绝对化 -->
    <link href="/resources/css/bootstrap.css" rel="stylesheet">
    <link href="/resources/css/bootstrap-responsive.css" rel="stylesheet">
    <link href="/resources/css/index.css" rel="stylesheet">
    <link rel="icon" href="/resources/favicon.ico" type="image/x-icon" />

</head>
<body>

<div class="navbar navbar-fixed-top">
<div class="navbar-inner">
    <div class="container-fluid">
        <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </a>
        <a class="brand" href="#">贪食蛇</a>

        <div class="nav-collapse">
            <ul class="nav">
                <li class="active"><a href="#">首页</a></li>
                <li><a href="#about">关于</a></li>
                <li><a href="#contact">联系</a></li>
            </ul>
            <p class="navbar-text pull-right">阿里主公</p>
        </div>
    </div>
</div>
</div>

<div class="container">
<form class="well form-horizontal" method="post" id="config">
    <div class="btn-group pull-right">
        <div class="btn" id="config-game" data-toggle="modal" data-target="#config-board">配置游戏</div>
        <div class="btn" id="start-game" data-loading-text="计算中…">开始游戏</div>
    </div>
    <div class="btn-group pull-right">
        <div class="btn" id="add-player">添加玩家</div>
        <div class="btn" id="delete-player">删除玩家</div>
    </div>
    <div id="config-board" class="modal .fade" data-show="false" data-backdrop="true" style="display: none">
        <div class="modal-header">
            <h3>游戏配置</h3>
        </div>
        <div class="modal-body">
            <div class="control-group">
                <label class="control-label" for="totalRound">回合</label>

                <div class="controls">
                    <input type="text" class="input-xlarge" name="totalRound" id="totalRound"
                           placeholder="最多轮数，默认是100">
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="width">长宽</label>

                <div class="controls">
                    <input type="text" class="input-medium" id="width" name="width" placeholder="宽度，默认是30">
                    <input type="text" class="input-medium" id="height" name="height" placeholder="高度，默认是15">
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="totalRound">播放速度</label>

                <div class="controls">
                    <input type="text" class="input-xlarge" id="speed"
                           placeholder="播放游戏的速度，单位毫秒，默认是400">
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button class="btn btn-primary" type="button">关闭</button>
        </div>
    </div>
</form>
<canvas id="canvas">不支持Canvas，请用以下浏览器：IE9+ FF3.6+ Chrome10+</canvas>
</div>

<script type="text/javascript" src="/resources/js/jquery.min.js"></script>
<script type="text/javascript" src="/resources/js/bootstrap.js"></script>
<script type="text/javascript" src="/resources/js/bootstrap-alert.js"></script>
<script type="text/javascript" src="/resources/js/bootstrap-modal.js"></script>
<script type="text/javascript" src="/resources/js/template.js"></script>
<script type="text/javascript" src="/resources/js/index.js"></script>
<script type="text/html" id="player">
<div class="clearfix">
    <div class="pull-left color" style="background-color:{{=color }}"></div>
    <input type="hidden" name="type" class="type">
    <input type="hidden" name="address" class="address">

    <div class="btn-group pull-left" data-toggle="buttons-radio">
        <button type="button" class="btn active" value="http">HTTP</button>
        <button type="button" class="btn" value="udp">UDP</button>
    </div>
    <input type="text" class="span4 input-large address" placeholder="输入你的访问地址和端口">
    <button class="btn test" type="button">测试</button>
    <button type="button" class="btn joinin">未参与</button>
</div>
</script>
<script type="text/html" id="notice">
<div class="noticeTemplate fade in alert alert-{{=type }}">
    <a class="close" data-dismiss="alert">x</a>
    {{=message }}
</div>
</script>
</body>
</html>
