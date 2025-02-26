$(document).ready(function () {
    $("#badge-info-btn, #badge-info-balloon").hover(
        function () {
            $("#badge-info-balloon").show();
        },
        function () {
            if (!$("#badge-info-btn").is(":hover") && !$("#badge-info-balloon").is(":hover")) {
                $("#badge-info-balloon").hide();
            }
        }
    );

    $("#manner-info-btn, #manner-info-balloon").hover(
        function () {
            $("#manner-info-balloon").show();
        },
        function () {
            if (!$("#manner-info-btn").is(":hover") && !$("#manner-info-balloon").is(":hover")) {
                $("#manner-info-balloon").hide();
            }
        }
    );
});

function goToReservedMatchingList(gameKind) {
    location.href = "/page/matching/reservedMatchingList/" + gameKind;
}

function goToEndedMatchingList(gameKind) {
    location.href = "/page/matching/endedMatchingList/" + gameKind;
}