$(document).ready(function () {

    $('#changePasswordForm').on("submit", function (event) {
        event.preventDefault();

        var userId = $("#user-id").val();
        var password = $("#password").val();
        var rePassword = $('#re-password').val();

        if (!password) {
            alert("새 비밀번호를 입력해주세요");
            return;
        }
        if (!validate_password(password)) {
            alert("비밀번호 형식이 잘못되었습니다.");
            return;
        }
        if (!rePassword) {
            alert("새 비밀번호 확인을 입력해수제요")
            return;
        }
        if (password !== rePassword) {
            alert("비밀번호와 비밀번호 확인이 다릅니다")
            return;
        }

        var formData = {
            'userId': userId,
            'password': password,
            'rePassword': rePassword
        }

        $.ajax({
            url: '/api/login/changePassword',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function (response) {
                alert(response.data);
                location.href = "/page/login";
            },
            error: function(xhr, status, error) {
                commonErrorCallBack(xhr, status, error);
            }
        })
    })
})