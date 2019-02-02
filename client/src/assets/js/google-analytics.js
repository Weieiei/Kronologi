// NOTES: add this to webpack, gulp or whatever, don't include in dev unless you want to track values in dev as well, or create a different UA id for dev etc -->
// Google Analytics


loadScript("https://www.google-analytics.com/analytics.js", () => {
  window.ga=window.ga||function(){(ga.q=ga.q||[]).push(arguments)};ga.l=+new Date;

    ga('create', 'UA-133707510-1', 'auto');
   // ga('send', 'pageview');
});


function loadScript(url, callback){

  var script = document.createElement("script")
  script.type = "text/javascript";

  if (script.readyState){  //IE
      script.onreadystatechange = function(){
          if (script.readyState == "loaded" ||
                  script.readyState == "complete"){
              script.onreadystatechange = null;
              callback();
          }
      };
  } else {  //Others
      script.onload = function(){
          callback();
      };
  }

  script.src = url;
  document.getElementsByTagName("head")[0].appendChild(script);
}