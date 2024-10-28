$(document).ready(function () {
    activeFlowStep(1);
})

function clickText(checkboxSelector) {
    const checkbox = $(checkboxSelector);
    checkbox.prop('checked', !checkbox.prop('checked'));
}

function go_userInfo() {

    var isAgreeUse = $('#agreeUse').is(":checked");
    var isAgreePrivacy = $('#agreePrivacy').is(":checked");

    if (!isAgreeUse || !isAgreePrivacy) {
        alert("약관에 동의해주세요.");
    } else {
        location.href = "/page/signup/userInfo";
    }
}