/**
 * 使用ajax提交数据
 */
function ajax(the_url,the_param,succ_callback){
	$.ajax({
		type:'POST',
		dataType:'JSON',
		url:the_url,
		data:the_param,
		success:function(msg){
			if(msg.unlogin) {
				location.href = "/login";
				return ;
			}
			succ_callback(msg);
		},
		error:function(html){
			alert("提交数据失败，请稍候再试");
		}
	});
}

function ltrim(str){
    var whitespace = new String(" \t\n\r");
    var s = new String(str);
    if (whitespace.indexOf(s.charAt(0)) != -1){
        var j=0, i = s.length;
        while (j < i && whitespace.indexOf(s.charAt(j)) != -1){
            j++;
        }
        s = s.substring(j, i);
    }
    return s;
}

function rtrim(str){
    var whitespace = new String(" \t\n\r");
    var s = new String(str);
    if (whitespace.indexOf(s.charAt(s.length-1)) != -1){
        var i = s.length - 1;
        while (i >= 0 && whitespace.indexOf(s.charAt(i)) != -1){
            i--;
        }
        s = s.substring(0, i+1);
    }
    return s;
}
 
function trim(str){
    return rtrim(ltrim(str));
}

function is_empty(str){
    return trim(str)=="";
}

function is_email(email)
{
	// a very simple email validation checking. 
	// you can add more complex email checking if it helps 
    var splitted = email.match("^(.+)@(.+)$");
    if(splitted == null) return false;
    if(splitted[1] != null )
    {
      var regexp_user=/^\"?[\w-_\.]*\"?$/;
      if(splitted[1].match(regexp_user) == null) 
      	return false;
    }
    if(splitted[2] != null)
    {
      var regexp_domain=/^[\w-\.]*\.[A-Za-z]{2,4}$/;
      if(splitted[2].match(regexp_domain) == null) 
      {
	    var regexp_ip =/^\[\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}\]$/;
	    if(splitted[2].match(regexp_ip) == null) 
	    	return false;
      }// if
      return true;
    }
	return false;
}
function html_encode(str)   
{   
  var s = "";   
  if (str.length == 0) return "";   
  s = str.replace(/&/g, "&amp;");   
  s = s.replace(/</g, "&lt;");   
  s = s.replace(/>/g, "&gt;");   
  //s = s.replace(/ /g, "&nbsp;");   
  s = s.replace(/\'/g, "&#39;");   
  s = s.replace(/\"/g, "&quot;");   
  //s = s.replace(/\n/g, "<br>");   
  return s;   
}   

function html_decode(str)   
{   
  var s = "";   
  if (str.length == 0) return "";   
  s = str.replace(/&amp;/g, "&");   
  s = s.replace(/&lt;/g, "<");   
  s = s.replace(/&gt;/g, ">");   
  s = s.replace(/&nbsp;/g, " ");   
  s = s.replace(/&#39;/g, "\'");   
  s = s.replace(/&quot;/g, "\"");   
  s = s.replace(/<br>/g, "\n");   
  return s;   
}
(function($) {
	  jQuery.fn.scrollFix = function(height, dir) {
	    height = height || 0;
	    height = height == "top" ? 0 : height;
	    return this.each(function() {
	      if (height == "bottom") {
	        height = document.documentElement.clientHeight - this.scrollHeight;
	      } else if (height < 0) {
	        height = document.documentElement.clientHeight - this.scrollHeight + height;
	      }
	      var that = $(this),
	        oldHeight = false,
	        p, r, l = that.offset().left;
	      dir = dir == "bottom" ? dir : "top"; //默认滚动方向向下
	      if (window.XMLHttpRequest) { //非ie6用fixed


	        function getHeight() { //>=0表示上面的滚动高度大于等于目标高度
	          return (document.documentElement.scrollTop || document.body.scrollTop) + height - that.offset().top;
	        }
	        $(window).scroll(function() {
	          if (oldHeight === false) {
	            if ((getHeight() >= 0 && dir == "top") || (getHeight() <= 0 && dir == "bottom")) {
	              oldHeight = that.offset().top - height;
	              that.css({
	                position: "fixed",
	                top: height,
	                left: l
	              });
	            }
	          } else {
	            if (dir == "top" && (document.documentElement.scrollTop || document.body.scrollTop) < oldHeight) {
	              that.css({
	                position: "static"
	              });
	              oldHeight = false;
	            } else if (dir == "bottom" && (document.documentElement.scrollTop || document.body.scrollTop) > oldHeight) {
	              that.css({
	                position: "static"
	              });
	              oldHeight = false;
	            }
	          }
	        });
	      } else { //for ie6
	        $(window).scroll(function() {
	          if (oldHeight === false) { //恢复前只执行一次，减少reflow
	            if ((getHeight() >= 0 && dir == "top") || (getHeight() <= 0 && dir == "bottom")) {
	              oldHeight = that.offset().top - height;
	              r = document.createElement("span");
	              p = that[0].parentNode;
	              p.replaceChild(r, that[0]);
	              document.body.appendChild(that[0]);
	              that[0].style.position = "absolute";
	            }
	          } else if ((dir == "top" && (document.documentElement.scrollTop || document.body.scrollTop) < oldHeight) || (dir == "bottom" && (document.documentElement.scrollTop || document.body.scrollTop) > oldHeight)) { //结束
	            that[0].style.position = "static";
	            p.replaceChild(that[0], r);
	            r = null;
	            oldHeight = false;
	          } else { //滚动
	            that.css({
	              left: l,
	              top: height + document.documentElement.scrollTop
	            })
	          }
	        });
	      }
	    });
	  };
	})(jQuery);