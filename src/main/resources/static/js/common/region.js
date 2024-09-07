function getSiGunGuList() {
    var siDo = $('#siDo').val();
    var siGunGuSelect = $('#siGunGu');

    if(siDo === "") {
        siGunGuSelect.disabled = true;
        siGunGuSelect.empty();
        siGunGuSelect.append($('<option>').val("").text("시/군/구"));
    } else {
        $.ajax({
            url: '/region/getSiGunGuList',
            type: 'POST',
            data: { siDo: siDo},
            success: function (data) {
                siGunGuSelect.empty();
                siGunGuSelect.append($('<option>').val("").text("시/군/구"));

                data.forEach(function (siGunGu) {
                    siGunGuSelect.append($('<option>').val(siGunGu).text(siGunGu));
                });

                siGunGuSelect.prop('disabled', false);
            },
            error: function (xhr, status, error) {
                siGunGuSelect.empty();
                siGunGuSelect.append($('<option>').val("").text("시/군/구"));
                commonErrorCallBack(xhr, status, error);
            }
        })
    }
}