$(document).ready(function() {

	$(".minusButton").on("click", function(evt) {
		evt.preventDefault(); 
		decreaseQuantity($(this));
	});

	$(".plusButton").on("click", function(evt) {
		evt.preventDefault(); 
		increaseQuantity($(this));
	});

	updateTotal(); 
});

function decreaseQuantity(link) {
	productId = link.attr("pid");
	quantityInput = $("#quantity" + productId);
		
	newQuantity = parseInt(quantityInput.val())-1;
	if (newQuantity > 0) {
		quantityInput.val(newQuantity);
	}	
}

function increaseQuantity(link) {
	productId = link.attr("pid");
	quantityInput = $("#quantity" + productId);
		
	newQuantity = parseInt(quantityInput.val())+1;
	
	quantityInput.val(newQuantity);

}


function updateTotal() {
	total = 0.0; 
	$(".productSubtotal").each(function(index, element) {
		total = total + parseFloat(element.innerHTML);
	});
	total = total.toFixed(2);
	$("#totalAmount").text("€" + total);
}