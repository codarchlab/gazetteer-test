$(document).ready(function(){

	// Toggle extended search
	$("#extendedSearchBtn").click(function() {
		$("#extendedSearchDiv").slideToggle();
		$("#extendedSearchBtn i").toggleClass("icon-circle-arrow-down icon-circle-arrow-up");
		$("#extendedSearchBtn").toggleClass("active");
	});
	
	$('input.lnglat').locationPicker();
	
});
$(document).scroll(function(){

	// Fix navbar to top when scrolling
	if($('.subnav').length > 0) {
		
	    // If has not activated (has no attribute "data-top"
	    if (!$('.subnav').attr('data-top')) {
	        // If already fixed, then do nothing
	        if ($('.subnav').hasClass('subnav-fixed')) return;
	        // Remember top position
	        var offset = $('.subnav').offset();
	        $('.subnav').attr('data-top', offset.top);
	    }

	    if ($('.subnav').attr('data-top') - $('.subnav').outerHeight() <= $(this).scrollTop())
	        $('.subnav').addClass('subnav-fixed');
	    else
	        $('.subnav').removeClass('subnav-fixed');
	    
	}
	
	// Fix map to top when scrolling
	if($('.map-top').length > 0) {
		
	    // If has not activated (has no attribute "data-top"
	    if (!$('.map-top').attr('data-top')) {
	        // If already fixed, then do nothing
	        if ($('.map-top').hasClass('map-top-fixed')) return;
	        // Remember top position
	        var offset = $('.map-top').offset();
	        $('.map-top').attr('data-top', offset.top);
	    }

	    if ($('.map-top').attr('data-top') - $('.subnav').outerHeight() <= $(this).scrollTop())
	        $('.map-top').addClass('map-top-fixed');
	    else
	        $('.map-top').removeClass('map-top-fixed');
	    
	}
	
});

var mapsApiCallback;

function requireGoogleMaps(callback, apiKey) {
	
	/******** Load google maps api if not present *********/
	if (typeof window.google === "undefined") {
		mapsApiCallback = callback;
	    var script_tag = document.createElement('script');
	    script_tag.setAttribute("type","text/javascript");
	    var src = "https://maps.google.com/maps/api/js?sensor=false&callback=mapsApiCallback&key=" + apiKey;
	    script_tag.setAttribute("src", src);
	    // Try to find the head, otherwise default to the documentElement
	    (document.getElementsByTagName("head")[0] || document.documentElement).appendChild(script_tag);
	} else {
	    // called if google maps api already present
	    callback();
	}
	
}