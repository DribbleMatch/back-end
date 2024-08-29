function check_input() {

    var gender = $('#gender').val();
    var skill = $('#skill').val();
    var positionChecked = $('input[name="positionString"]:checked').length;
    var siDo = $('#siDo').val();
    var siGunGu = $('#siGunGu').val();

    if (!gender) {
        alert("성별을 선택해주세요.");
        return false;
    }

    if (!skill) {
        alert("실력을 선택해주세요.");
        return false;
    }

    if (positionChecked === 0) {
        alert("포지션을 한 개 이상 선택해주세요.");
        return false;
    }

    if (!siDo) {
        alert("시/도를 선택해주세요.");
        return false;
    }

    if (!siGunGu) {
        alert("시/군/구를 선택해주세요.");
        return false;
    }
}