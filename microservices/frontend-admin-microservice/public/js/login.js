console.log("Hello from login.js")

function getFormDataAsJSON(form){
    const unindexed_array = form.serializeArray();
    const indexed_array = {};

    unindexed_array.map(function(item){
        indexed_array[item['name']] = item['value'];
    });
    return indexed_array;
}

bootstrap_alert = function() {}
bootstrap_alert.warning = function(message) {
    $('#alert_placeholder').html('<div class="alert"><a class="close" data-dismiss="alert">×</a><span>'+message+'</span></div>')
}



$('#login_form').submit(function (e) {
    e.preventDefault(); // avoid to execute the actual submit of the form.
    //$("#password").val(CryptoJS.SHA256($("#password").val()).toString());
    const url = $(this).attr('action');
    let data = getFormDataAsJSON($(this));
    // data["password"] = CryptoJS.SHA256(data["password"]).toString();
    console.log(data);
    $.ajax({
        async: false,
        type: "POST",
        url: url,
        data: data, // serializes the form's elements.
        success: function (result) {
            //if the submit was successful, you redirect
            console.log(result.status);
            window.location.href = "/";
        },
        error: function (e) {
            console.log(e.status);
            if (e.status === 401){
                alert("401. Invalid Credentials");
            } else if (e.status === 403){
                alert("403. Forbidden");
            } else {
                alert("400. Bad Request");
            }
            //window.location.href = "/login";
        }
    });
});
