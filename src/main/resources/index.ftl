
<html lang="zh_CN">
<head>
    <!-- Info meta tags, important for social media + SEO -->
    <title>大荒神迹</title>
    <link rel="shortcut icon" href="favicon.png">

    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="utf-8">
    <link rel="stylesheet" href="static/css/stylesheet.css">
    <link rel="stylesheet" href="static/css/login.css">
    <link href="https://fonts.googleapis.com/css?family=Open+Sans:400,700&display=swap" rel="stylesheet">
</head>
<body>
<div class="container">
    <div class="logo">
        <img src="static/img/banner.png" alt="MyServer logo">
    </div>

    <div class="items">
        <!-- Replace # with your forum URL-->
        <a href="#" class="item forums">
            <div>
                <img src="static/img/forums.png" alt="Minecraft forums icon" class="img">
                <p class="subtitle">在我们的论坛上讨论</p>
                <p class="title">论坛</p>
            </div>
        </a>

        <!-- Replace # with your store URL -->
        <a href="#" class="item store">
            <div onclick="document.getElementById('id01').style.display='block'">
                <img src="static/img/store.png" alt="Minecraft store icon" class="img">
                <p class="subtitle">捐赠我们的服务器</p>
                <p class="title">商店</p>
            </div>
        </a>

        <!-- Replace # with your vote URL -->
        <a href="#" class="item vote">
            <div>
                <img src="static/img/vote.png" alt="Minecraft voting icon" class="img">
                <p class="subtitle">支持我们，给我们顶帖</p>
                <p class="title">顶帖</p>
            </div>
        </a>


    </div>

    <div class="playercount">
        <p>现有 ${config.online} 人在线 <span class="ip">${config.serverIp}</span></p>
    </div>
    <div id="id01" class="modal">

        <form class="modal-content animate" action="login" method="post">
            <div class="imgcontainer">
                <span onclick="document.getElementById('id01').style.display='none'" class="close" title="Close Modal">&times;</span>
                <img src="static/img/avatar.png" alt="Avatar" style="width: 150px; height: 150px;" class="avatar">
            </div>

            <div class="container">
                <label>
                    <input type="text" placeholder="请输入用户名" name="user" required>
                </label>

                <label>
                    <input type="password" placeholder="请输入密码" name="password" required>
                </label>

                <button type="submit">登录</button>
            </div>
            <div class="container" style="background-color:#f1f1f1">
                <button type="button" onclick="document.getElementById('id01').style.display='none'" class="cancelbtn">
                    注册账户
                </button>
                <span class="psw"><a href="#">忘记密码?</a></span>
            </div>
        </form>
    </div>
</div>

<script>
    // Get the modal
    var modal = document.getElementById('id01');

    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }
</script>

<script src="https://cdn.bootcdn.net/ajax/libs/jquery/1.11.2/jquery.min.js" type="text/javascript"></script>
<script src="static/js/main.js" type="text/javascript"></script>
</body>
</html>
