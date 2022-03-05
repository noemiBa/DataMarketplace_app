$(document).ready(function() {
	$(".minusButton").on("click", function(evt) {
		evt.preventDefault(); 
		productId = $(this).attr("pid");
		quantityInput = $("#quantity" + productId);
		
		newQuantity = parseInt(quantityInput.val())-1;
		if (newQuantity > 0) quantityInput.val(newQuantity);
	});

	$(".plusButton").on("click", function(evt) {
		evt.preventDefault(); 
		productId = $(this).attr("pid");
		quantityInput = $("#quantity" + productId);
		
		newQuantity = parseInt(quantityInput.val())+1;
		if (newQuantity < 100) quantityInput.val(newQuantity);
	});
});