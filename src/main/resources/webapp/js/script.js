var isMobile = function() {
    return (
        navigator.userAgent.match(/Windows Phone/i) ||
        navigator.userAgent.match(/Android/i) ||
        navigator.userAgent.match(/webOS/i) ||
        navigator.userAgent.match(/iPhone/i) ||
        navigator.userAgent.match(/iPad/i) ||
        navigator.userAgent.match(/iPod/i) ||
        navigator.userAgent.match(/BlackBerry/)
    );
};

var randomizepartners = function(e) {
    if(e.size() === 0) return;

    var replace = $('<div>');
    var size = e.size();
    while (size >= 1) {
       var rand = Math.floor(Math.random() * size);
       var temp = e.get(rand);
       replace.append(temp);
       e = e.not(temp);
       size--;
    }
    $('#partners').html(replace.html());
};

var daysUntilJavazoneInBinary = function() {
    msPerDay = 24 * 60 * 60 * 1000 ;
    today = new Date();
    javazone = new Date("September 12, 2012");
    
    timeLeft = (javazone.getTime() - today.getTime());
    daysLeft = Math.floor(timeLeft / msPerDay) + 1;
    if(daysLeft < 0) {
        daysLeft = 0;
    }
    
    daysLeftBinary = parseInt(daysLeft, 10).toString(2);
    return daysLeftBinary;
};

var changeHeaderColors = function() {
    daysLeftBinary = daysUntilJavazoneInBinary();

    var menuElementToModify = 7;
    for (var i = daysLeftBinary.length - 1; i >= 0; i--) {
        if(daysLeftBinary[i] == 1) {
            $("#menuelement-" + menuElementToModify).css({backgroundColor: "#00aced"},{queue:false,duration:100});
        } 
        menuElementToModify--;
    }
};

var resizeIframe = function() {
    $("iframe").each(function() {
        $(this).css("width", $(this).parent().width() - 35);
    });
};

$(function() {
    $("html").removeClass("no-js");

    // Hyphenation of paragraphs.
    Hyphenator.run();

    // Animate menu.
    if(!isMobile()) {
        var original = "-10px";
        $("#menu div").hover(function() {
            original = $(this).css("margin-top");
            $(this).animate({opacity:1},{queue:false,duration:100});
        }, function() {        
            $(this).animate({opacity:0.9},{queue:false,duration:100});
        });    
    }
    
    // Parallax headers.
    if(!isMobile()) {
        (function () {
            var next, part;
            $(window).scroll(function(){
                next = - parseInt($(this).scrollTop(), 10) * 0.5;
                part = next * 0.5;
                $("#splash").css("background-position", "50% " + next + "px");
                $("#sidesplash").css("background-position", "" + part + "px " + next + "px");
                $("#side").css("background-position", "" + part + "px " + next + "px");
            });
        }());
    }

    // Fade in content.
    if(isMobile()) {
        $("#main, #side, #sidesplash").show();
    } else {
        $("#main, #side, #sidesplash").fadeIn("slow");
    }

    changeHeaderColors();
    randomizepartners($("#partners a"));
    resizeIframe();
    $(window).resize(resizeIframe);
});