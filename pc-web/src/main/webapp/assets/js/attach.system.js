function attachInit() {
    var log = console.log;
    var $uploadInput = $('.upload-input');
    var attachOption = window.attachOption;

    // 기존 객체 정리 후 초기화
    if(attachOption) {
        window.$attach.destroy(attachOption.refType);
        window.$attach.init(attachOption);
    }

    // 수정모드
    var refKey = attachOption ? attachOption.refKey : '';

    if (refKey !== '') {
        $.ajax('/api/attach/' + attachOption.refType + '/' + attachOption.refKey, {
            method: 'GET',
            cache: false,
            success: function (data) {
                // log('data??', data)

                $.each(Object.keys(data), function (i, key) {
                    var $tgInput = $uploadInput.filter('[data-map-code=' + key + ']');
                    var savedLi = [];
                    var saved = [];

                    $.each(data[key], function (i, v) {
                        var li = '<li>' +
                            v.displayName +
                            '<a data-idx="' + i + '" data-order="' + v.order + '" data-real-idx="' + v.idx + '" class="btn-gray btn-small btn-del">삭제</a>' +
                            '</li>';

                        saved.push({
                            idx: v.idx,
                            orgDisplayName: v.displayName,
                            url: 'api/attach/view/' + attachOption.refType + '/' + v.refKey + '/' + key + '/' + v.order
                        })

                        savedLi.push(li);
                    });

                    $tgInput.closest('.attach-wrap').find('.attach-list').html(savedLi.join(''));

                    window.$attach[attachOption.refType][key] = saved;

                    // log(saved)
                });
            },
            error: function (xhr, err) {
                alert('첨부파일 조회 중 오류가 발생했습니다.');
            }
        });
    }

    // 파일등록 버튼 클릭
    $('.btn-attach').off('click').on('click', function () {
        $(this).closest('.attach-wrap').find('.upload-input').trigger('click');
    });

    // 파일 선택
    $uploadInput.off('click').on('click', function (e) {
        var tg = e.target
        var $tg = $(tg);
        var $savedContainer = $tg.closest('.attach-wrap').find('.attach-list');
        var filename = '';
        var mapCode = $tg.data('map-code');
        var refType = $tg.data('ref-type');

        // 단일업로드 모드
        var isSingleType = $tg.closest('.attach-wrap').attr('data-single') === "Y";

        // log('change');

        var uploadParam = {
            url: '/api/attach/tmp',
            formData: {
                refType: refType,
                mapCode: mapCode
            },
            contentType: false,
            dataType: 'json',
            paramName: 'file',
            add: function (e, data) {
                // log(data);
                // log(data.files);
                filename = data.files[0].name;
                data.submit();
            },
            done: function (e, data) {
                // log('업로드파일명', filename);
                // log('저장된파일명', data.result.savedName);

                var indexList = $.map($savedContainer.find('.btn-del'), function (el, i) {
                    return parseInt($(el).data('idx'));
                });

                if (indexList.length) {
                    indexList.sort();
                }

                var lastIndex = indexList.length > 0 ? indexList.pop() : -1;
                lastIndex++;

                if (isSingleType) {
                    lastIndex = 0;
                    $savedContainer.find('.btn-del').trigger('click');
                }

                if (!window.$attach[refType]) {
                    window.$attach[refType] = {
                        '_deleted': []
                    };
                }

                var thisfile = data.files[0];
                window.$attach[refType][mapCode][lastIndex] = {};
                var fileObj = window.$attach[refType][mapCode][lastIndex];
                fileObj.displayName = thisfile.name;
                fileObj.fileSize = thisfile.size;
                fileObj.fileType = thisfile.type;
                fileObj.mapCode = mapCode;
                fileObj.savedName = data.result.savedName;
                fileObj.url = '/api/attach/temp/view/' + data.result.savedName;
                fileObj.status = 'complete';
                fileObj.order = lastIndex;

                $savedContainer.append(
                    '<li>' +
                    filename +
                    '<a data-idx="' + lastIndex + '" class="btn-gray btn-small btn-del">삭제</a>' +
                    '</li>'
                );
            },
            error: function (e) {
                log(e)
            }
        };

        // log(uploadParam)

        window.$attach[refType][mapCode].push({});

        $tg.fileupload(uploadParam);
    });

    // 파일삭제 버튼 클릭
    $('.attach-list').off('click').on('click', '.btn-del', function (e) {
        var $tg = $(e.target);
        var $li = $tg.closest('li');
        var $currentUploadInput = $tg.closest('.attach-wrap').find('.upload-input');
        var mapCode = $currentUploadInput.data('map-code');
        var refType = $currentUploadInput.data('ref-type');
        var idx = $tg.data('idx');

        // console.log(mapCode, refType, idx)

        if ($tg.data('real-idx')) {
            idx = $tg.data('real-idx');
            window.$attach[refType]['_deleted'].push({
                idx: idx,
                refMapCode : mapCode
            });
        }

        var fileObj = window.$attach[refType][mapCode][idx];

        window.$attach[refType][mapCode][idx] = {};

        // log('삭제', idx, mapCode, refType)

        // 삭제된후
        $li.remove();
    });
}

// dom ready init
$(document).ready(attachInit);