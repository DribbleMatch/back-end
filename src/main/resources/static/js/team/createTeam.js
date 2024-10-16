$(document).ready(function () {
    activeMenu('team-menu');
})

const selectedTags = [];

function showImage() {

    const imageFile = $('#image')[0].files[0];

    if (imageFile) {
        var reader = new FileReader();
        reader.onload = function (e) {
            $("#preview").attr("src", e.target.result).show();
        }
        reader.readAsDataURL(imageFile)
    }
}

function selectTag(tagElement) {

    const tagName = $(tagElement).attr('data-name');
    const tagText = $(tagElement).text();
    const tagColor = $(tagElement).attr('data-color');

    // 선택된 태그가 이미 3개라면 더 이상 선택하지 않도록 함
    if (selectedTags.length >= 3) {
        alert("최대 3개의 태그만 선택할 수 있습니다.");
        return;
    }

    // 이미 선택된 태그가 있는지 확인
    if (selectedTags.includes(tagName)) {
        alert("이미 선택된 태그입니다.");
        return;
    }

    // 선택된 태그에 추가
    selectedTags.push(tagName);

    // 태그를 선택된 리스트에 추가
    $('.select-tag-div').append(
        `<div class="selected-tag" style="background-color:#${tagColor};">
            ${tagText} <span class="remove-tag" onclick="removeTag('${tagName}', this)">X</span>
        </div>`
    );

    // 선택된 태그를 클릭 불가능하게 설정
    $(tagElement).addClass('disabled-tag');
}

function removeTag(tagName, removeBtn) {
    // 태그 삭제
    $(removeBtn).parent().remove();

    // 선택된 태그 리스트에서 제거
    const index = selectedTags.indexOf(tagName);
    if (index > -1) {
        selectedTags.splice(index, 1);
    }

    // 다시 태그를 클릭 가능하게 설정
    $(`[data-name=${tagName}]`).removeClass('disabled-tag');
}

function checkTeamName() {

    const name = $('#name').val();

    if (!name) {
        alert("팀 이름을 입력해주세요");
        return;
    }

    $.ajax({
        url: '/api/team/checkTeamName',
        type: 'POST',
        data: {
            name: name
        },
        success: function (response) {
            alert("사용 가능한 팀이름입니다.");
            $('#name').prop('readonly', true);
            $('#check-team-name-btn').prop('disabled', true).text('사용 가능');
        },
        error: function (xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}

function createTeam() {

    const name = $('#name').val();
    const max_num = $('#max-num').val();
    const info = $('#info').val();
    var siDo = $('#siDo').val();
    var siGunGu = $('#siGunGu').val();
    var image = $("#image")[0].files[0];
    var tags = selectedTags.join(",");

    if (!name) {
        alert("팀 이름을 입력해주세요");
        return;
    }

    if (!max_num) {
        alert("팀 최대 인원을 입력해주세요");
        return;
    }

    if (!siDo) {
        alert("시/도를 선택해주세요");
        return;
    }

    var formData = new FormData();
    formData.append("name", name);
    formData.append("maxNum", max_num);
    formData.append("info", info);
    formData.append("regionString", siDo + " " + siGunGu);
    formData.append("image", image);
    formData.append("tags", tags);

    $.ajax({
        url: '/api/team/createTeam',
        type: 'POST',
        data: formData,
        contentType: false,
        processData: false,
        success: function (response) {
            alert("팀 생성에 성공하였습니다.\n팀원들을 모집해보세요!");
            location.href = "/";
        },
        error: function (xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    })
}