$(document).ready(function(){
    changeQuantity();
});

function changeQuantity() {
    $(".quantity-button").click(function(){
        var id =$(this).attr('id');
        var inputField = $("#quantity-input-"+id);
        var quantity = inputField.val();
        inputField.attr("value", quantity);
        var price = $("#product-price-" + id).text();
        $("#product-sumprice-" + id).text(parseFloat(price * quantity).toFixed(2));

        $.post("/changeQuantity", {
            id:id,
            quantity:quantity
        });
        /*    functon(data,status)
        {
            //todo
        });*/
    });


}