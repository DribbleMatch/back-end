var editor;

$(document).ready(function () {
    editor = new toastui.Editor({
        el: document.querySelector('#content'),
        height: '500px',
        weight: '100%',
        initialEditType: 'markdown',
        initialValue: '내용을 입력해 주세요.',
        previewStyle: 'vertical',
        hooks: {
            addImageBlobHook: function (blob, callback) {
                const formData = new FormData();
                formData.append('file', blob);

                $.ajax({
                    url: '/api/image',
                    method: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (response) {

                        const filename = response.data;
                        const imageUrl = encodeURI(`/api/image?imagePath=${filename}`);
                        callback(imageUrl, 'alt text');
                    },
                    error: function () {
                        alert('이미지 업로드 업로드 도중 문제가 발생했습니다');
                    }
                });
            }
        }
    });
});

function createPost() {
    var title = $('#title').val();
    var markdown = editor.getMarkdown();
    var content = editor.getHTML();
    var postStatus = $('input[name="post-status"]:checked').val();
    var hasBanner = $('input[name="has-banner"]:checked').val();
    var bannerStartAt = $('#banner-start').val();
    var bannerEndAt = $('#banner-end').val();
    var bannerImage = $('#banner-image')[0].files[0];

    if (!title) {
        alert("공지 제목을 입력해주세요.");
        return;
    }

    if (!markdown) {
        alert("게시물 내용을 입력해주세요.");
        return;
    }

    if (!postStatus) {
        alert("공개 여부를 선택해주세요.");
        return;
    }

    if (!hasBanner) {
        alert("배너 유무를 선택해주세요.");
        return;
    }

    var formData = new FormData();
    formData.append('title', title);
    formData.append('content', content);
    formData.append('status', postStatus);
    formData.append('hasBanner', hasBanner);

    if (hasBanner === 'BANNER') {
        if (!bannerStartAt || !bannerEndAt) {
            alert("배너 노출 기간을 입력해주세요.");
            return;
        }

        var startDate = new Date(bannerStartAt);
        var endDate = new Date(bannerEndAt);

        if (startDate > endDate) {
            alert("배너 시작 날짜는 종료 날짜보다 이전이어야 합니다.");
            return;
        }

        formData.append('bannerStartAt', bannerStartAt);
        formData.append('bannerEndAt', bannerEndAt);

        if (bannerImage) {
            formData.append('bannerImage', bannerImage);
        } else {
            alert("배너 이미지를 선택해주세요.");
            return;
        }
    }

    $.ajax({
        url: '/api/post/create',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function (data) {
            alert('공지 생성 완료');
            location.href = "/page";
        },
        error: function (xhr, status, error) {
            commonErrorCallBack(xhr, status, error);
        }
    });
}

function toggleBannerInfo() {

    var hasBanner = $('input[name="has-banner"]:checked').val();

    if (hasBanner === 'BANNER') {
        $('#banner-div').show();
    } else {
        $('#banner-div').hide();
    }
}

function previewImage() {

    var imageFile = $('#banner-image')[0].files[0];

    if (imageFile) {
        var reader = new FileReader();
        reader.onload = function (e) {
            $("#preview").attr("src", e.target.result).show();
        }
        reader.readAsDataURL(imageFile)
    }
}