$(document).ready(function () {
    activeFlowStep(1);
})

function go_userInfo() {
    var isAgree = $('#agree').is(":checked");

    if (!isAgree) {
        alert("약관에 동의해주세요.");
    } else {
        location.href = "/signup/userInfo";
    }
}