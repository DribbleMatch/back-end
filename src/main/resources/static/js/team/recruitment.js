function openContentPopup(button) {
    const $card = $(button).closest('.recruitment-card');

    const $title = $card.find('.recruitment-info').find('#recruitment-title').text();
    const $content = $card.find('#recruitment-content').val();

    $("#recruitment-title-h2").text($title);
    $("#recruitment-content-textarea").val($content);

    document.getElementById("content-popup").style.display = "flex";
}

function closeContentPopup() {
    document.getElementById("content-popup").style.display = "none";
}

function viewTeamDetail(id) {
    location.href = "/team/page/teamDetail/" + id;
}

function openRequestPop(button) {
    const $card = $(button).closest('.recruitment-card');

    const $teamId = $card.find('#team-id').val();
    $("#team-id-popup").val($teamId);

    document.getElementById("request-popup").style.display = "flex";
}

function closeRequestPop() {
    document.getElementById("request-popup").style.display = "none";
}

function requestJoin() {

    var id = $('#team-id-popup').val();
    var introduce = $('#introduce').val();

    $.ajax({
        url: '/teamApplication/rest/requestJoin',
        type: 'POST',
        data: {
            id: id,
            introduce: introduce
        },
        success: function (response) {
            alert("요청이 완료되었습니다.");
            closeRequestPop();
        },
        error: function (xhr, status, error) {
            var response = xhr.responseJSON;

            if (response && (response.code === '1301' || response.code === '2300')) {
                alert(response.message);
            } else {
                commonErrorCallBack(xhr, status, error);
            }
        }
    })
}