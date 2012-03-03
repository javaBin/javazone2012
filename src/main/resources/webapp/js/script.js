var partners = [
    ["systek_2012.jpg", "http://www.systek.no/"],
    ["arktekk_2012.jpg", "http://www.arktekk.no/"],
    ["nets_2012.jpg", "http://www.nets.no/"],
    ["marcello_2012.jpg", "http://www.marcello.no/"],
    ["edb_int_2012.jpg", "http://www.edb.com/"],
    ["visma_2012.jpg", "http://www.visma.no/"],
    ["bekk_2012.jpg", "http://www.bekk.no/"],
    ["computas_2012.jpg", "http://www.computas.no/"],
    ["jpro_2012.jpg", "http://www.jpro.no/"],
    ["nith_2012.jpg", "http://nith.no/"],
    ["steria_2012.jpg", "http://www.steria.no/"],
    ["kantega_2012.jpg", "http://www.kantega.no/"],
    ["iterate_2012.jpg", "http://www.iterate.no/"],
    ["conax_2012.jpg", "http://www.conax.no/"],
    ["microsoft_2012.jpg", "http://www.microsoft.no/"],
    ["knowit_2012.jpg", "http://www.knowit.no/"],
    ["mesan_2012.jpg", "http://www.mesan.no/"],
    ["norgesgruppen_2012.jpg", "http://www.norgesgruppen.no/"],
    ["bouvet_2012.jpg", "http://www.bouvet.no/"],
    ["kodemaker_2012.jpg", "http://www.kodemaker.no/"],
    ["accenture_2012.jpg", "http://www.accenture.no/"],
    ["capgemini_2012.jpg", "http://www.capgemini.no/"],
    ["cisco_2012.jpg", "http://www.cisco.com/"],
    ["ciber_2012.jpg", "http://www.ciber.no/"],
    ["programutvikling_2012.jpg", "http://www.put.no/"],
    ["itera_2012.jpg", "http://www.iteraconsulting.no/"],
    ["wepstep_2012.jpg", "http://www.webstep.no/"],
    ["miles_2012.jpg", "http://www.miles.no/"],
    ["finn_2012.jpg", "http://www.finn.no"]
];

var shuffle = function(o) {
    for(var j, x, i = o.length; i; j = parseInt(Math.random() * i, 10), x = o[--i], o[i] = o[j], o[j] = x);
    return o;
};

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

var randomizepartners = function() {
    partners = shuffle(partners);
    $("#partners").html("");
    for(var i = 0; i < partners.length; i++) {
        $("#partners")
            .prepend($("<a href='#' />")
            .attr("href", partners[i][1])
            .attr("target", "_blank")
            .html($("<img />").attr("src", 
                "img/partners/" + partners[i][0])));
    }
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
    //console.log("Antall dager til Javazone: " + daysLeft + ". Binær: " + daysLeftBinary);
    return daysLeftBinary;
};

var changeHeaderColors = function() {
    daysLeftBinary = daysUntilJavazoneInBinary();

    var menuElementToModify = 7;
    for (var i = daysLeftBinary.length - 1; i >= 0; i--) {
        if(daysLeftBinary[i] == 1) {
            //console.log("Gjør menyelement " + menuElementToModify + " blått.");
            $("#menuelement-" + menuElementToModify).animate({backgroundColor: "#00aced"}, 2000);
        } else {
            //console.log("Gjør menyelement " + menuElementToModify + " grønt.");
        }
        menuElementToModify--;
    }
};

$(function() {

    // Hyphenation of paragraphs.
    Hyphenator.run();

    // Animate menu.
    var original = "-10px";
    $("#menu div").hover(function() {
        original = $(this).css("margin-top");
        $(this).animate({marginTop:"-20px", opacity:1},{queue:false,duration:100});
    }, function() {        
        $(this).animate({marginTop:original, opacity:0.9},{queue:false,duration:100});
    });

    // Parallax headers.
    (function () {
        if(isMobile()) {
            return;
        }
        var next, part;
        $(window).scroll(function(){
            next = - parseInt($(this).scrollTop(), 10) * 0.5;
            part = next * 0.5;
            $("#splash").css("background-position-y", "" + next + "px");
            $("#top").css("background-position-y", "" + next + "px");


            $("#sidesplash").css("background-position-y", "" + next + "px");
            $("#sidesplash").css("background-position-x", "" + part + "px");
            $("#side").css("background-position-x", "" + part + "px");
        });
    }());

    // Fade in content.
    $("html").removeClass("no-js");
    $("#menu").fadeIn("slow");
    $("#main").fadeIn("slow");
    $("#side").fadeIn("slow");
    $("#sidesplash").fadeIn("slow");

    changeHeaderColors();
    randomizepartners();
});