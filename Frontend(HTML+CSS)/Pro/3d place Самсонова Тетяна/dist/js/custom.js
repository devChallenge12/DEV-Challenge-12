window.onload = function() {
    //preloader
    var body = document.getElementsByClassName("loaded")[0];
    body.classList.remove("loaded");
    //preloader
    
    
    
    
};

//show more items
var elemShowRow = document.getElementsByClassName("js-show-more")[0];
var elemRowItem = document.getElementsByClassName("hide-row")[0];
elemShowRow.addEventListener("click", function (e) {
    e.preventDefault();
    elemRowItem.classList.remove("hide-row");
    this.classList.add("hide");
});
//show more items

//head on scroll
var lastScrollTop = 0;
$(window).scroll(function (event) {
    var st = $(this).scrollTop();
    var fixHead = $('.js-sort-head').offset().top;
    if (st > lastScrollTop && st > fixHead) {
        $('.fixed-head').addClass("show");
    } else {
        $('.fixed-head').removeClass("show");
    }
    lastScrollTop = st;
});
//head on scroll

//focus from
function focusFromFunction() {
    var elem = document.getElementById("from");
    var parentsElem = elem.parentNode;
    parentsElem.classList.add("focused")
};
function focusOutFromFunction() {
    var elem = document.getElementById("from");
    var parentsElem = elem.parentNode;
    parentsElem.classList.remove("focused")
};
document.getElementById("from").addEventListener("focus", focusFromFunction, true);
document.getElementById("from").addEventListener("blur", focusOutFromFunction, true);
//focus from

//focus to
function focusToFunction() {
    var elem = document.getElementById("to");
    var parentsElem = elem.parentNode;
    parentsElem.classList.add("focused")
};
function focusOutToFunction() {
    var elem = document.getElementById("to");
    var parentsElem = elem.parentNode;
    parentsElem.classList.remove("focused")
};
document.getElementById("to").addEventListener("focus", focusToFunction, true);
document.getElementById("to").addEventListener("blur", focusOutToFunction, true);
//focus to


//popup add
var elemPopup = document.getElementsByTagName("body")[0];
var elemPopupOpen = document.getElementsByClassName("js-add")[0];
elemPopupOpen.addEventListener("click", function (e) {
    e.preventDefault();
    elemPopup.classList.add("popup-open");
});
//popup add

//popup add close   
var elemPopupCloseLink = document.getElementsByClassName("js-close-popup")[0];

elemPopupCloseLink.addEventListener("click", function (e) {
    e.preventDefault();
    elemPopup.classList.remove("popup-open");
});

var elemPopupCloseOverley = document.getElementsByClassName("popup-overlay")[0];
elemPopupCloseOverley.addEventListener("click", function (e) {
    e.preventDefault();
    elemPopup.classList.remove("popup-open");
});
//popup add close

//popup row
var elemPopupRow = document.getElementsByClassName("table__row")[0];
elemPopupRow.addEventListener("click", function (e) {
    e.preventDefault();
    elemPopup.classList.add("popup-open-row");
});
//popup row





$(function(){
    //select
    if($('.styled').length) {
		$('.styled').styler();
	};
    //select
    
	/* placeholder*/	   
	$('.form-control').focus(function(){
 		var placeholder = $(this).val();
 		$(this).parent().addClass('focused-field');
 	});
    $('.form-control').focusout(function(){
 		var placeholder = $(this).val().length;
        if (placeholder > 0){
            $(this).parent().addClass('focused-field');
        } else{
            $(this).parent().removeClass('focused-field');
        }
 		
 	});
	/* placeholder*/

	
	

});



