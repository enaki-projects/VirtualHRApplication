$('.custom_button:not(.disabled)').click(function (e) {
    e.preventDefault();
    const url = "grant-action";
    const uid = $(this).attr('value');
    const action = $(this).attr('id');

    console.log(uid);
    console.log(action);

    const data = {
        uid: uid,
        action: action,
    }

    $.ajax({
        async: false,
        type: "POST",
        url: url,
        data: data,
        success: function (result) {
            window.location.reload();
        },
        error: function (e) {
            console.log(e.status);
            alert(e.status);
        }
    });
});
