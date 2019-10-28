(function () {
    $("#submitId").on("click", function(e){
        e.preventDefault();
        let checkinStr = $('#checkin').val();
        let checkoutStr = $('#checkout').val();
        if (checkinStr === '' || checkoutStr === ''){
            alert("You haven't specified a date");
            return;
        }
        let checkin = new Date(checkinStr);
        let checkout = new Date(checkoutStr);
        if (checkin > checkout){
            alert("checkin date cannot be less that checkout");
        } else {
            $('#formId').submit();
        }
    });
})();