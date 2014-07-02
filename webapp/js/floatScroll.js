(function( $ ) {
$.floatScroll = {
    defaults: {
    	direction: "top",
        distance: 0,
        css: 'fixed'
    }
}
$.fn.floatScroll = function(map){
    var opts = $.extend({},
        $.floatScroll.defaults,
        map || {}
    );
    return this.each(function() {
    	var $this = $(this),
    		$window = $(window),
	    	windowHeight = $window.height(),
	    	objTop = $this.offset().top,
	    	objHeight = $this.height();
    	$window.scroll(function() {
    		scrollTop = $(document).scrollTop();
    		console.log("scollTop:"+scrollTop);
    		console.log("objTop  :"+objTop);
    		console.log("distance:"+opts.distance);
    		console.log("height :"+windowHeight);
    		if(opts.direction=="top"){
            	var scoll = opts.distance + scrollTop - objTop > 0;
            	console.log("top:"+scoll);
        		if(scoll) {
        			$this.addClass(opts.css);
        		}else {
        			$this.removeClass(opts.css);
        			objTop = $this.offset().top;
        		}
    		}else{
	        	var scroll = windowHeight + scrollTop - objHeight;
	    		if(scroll < objTop) {
	    			$this.addClass(opts.css);
	    		}else {
	    			$this.removeClass(opts.css);
	    			windowHeight = $window.height();
	    			objTop = $this.offset().top;
	    		}
    		}
    	});
    });
};
})( jQuery );