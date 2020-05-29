// 判断用户是否登录
function jundgeLoginStatus() {
    // 表示用户已经登录
    if(!$.cookie('frontedUsername')){
        window.location.href = 'login.jsp?path=' + window.location.href;
    }
}

jundgeLoginStatus();