$(function() {
    if (!window.$attach) {
        window.$attach = {
            /**
             * 업로드 객체 초기화
             */
            init: function (option) {
                option = Object.assign({
                    refType: null,     // (필수) 참조타입
                    refKey: null,      // (수정시 필수) 참조키
                    mapCodes: null    // (필수) 상세구분코드
                }, option);

                /*
                    option 예시
                    {
                        refType: 'exampleBoard',    // 예제 게시판 참조타입
                        refKey: 1,                  // 예제 게시판 참조키(=글번호. 수정 시만 입력)
                        mapCodes: [
                            {
                                mapCode: 'thumb',   // 예제 게시판 > 썸네일 이미지 상세구분코드
                                size: 3,            // 등록 할 수 있는 첨부파일 개수
                                exts: ['jpg','png'] // 첨부파일 확장자 제한(배열로 여러개 입력 가능)
                            },
                            {
                                mapCode: 'image',   // 예제 게시판 > 대표 이미지 상세구분코드
                                size: 3,            // 등록 할 수 있는 첨부파일 개수
                                exts: ['jpg']       // 첨부파일 확장자 제한(배열로 여러개 입력 가능)
                            }
                        ]
                    }
                 */

                if(!option || !option.refType || !option.mapCodes) return false;

                if (!window.$attach[option.refType]) {
                    window.$attach[option.refType] = {
                        '_deleted': []
                    };
                }

                option.mapCodes.forEach(function(mapCodeItem, mapCodeIndex) {
                    var mapCode = option.mapCodes[mapCodeIndex].mapCode;
                    if (!window.$attach[option.refType][mapCode]) window.$attach[option.refType][mapCode] = [];
                    var fileInputSize = option.mapCodes[mapCodeIndex].size;
                    if(!fileInputSize) fileInputSize = 1;

                    for(var fileIdx=0;fileIdx<fileInputSize;fileIdx++) {
                        // upload event 생성
                        getFileInput(option.refType, mapCode, fileIdx).fileupload({
                            url: '/api/attach/tmp',
                            formData: {
                                refType: option.refType,
                                mapCode: mapCode
                            },
                            contentType: false,
                            dataType: 'json',
                            paramName: 'file',
                            add: function (e, data) {
                                var file = data.files[0];
                                var mapCode = $(e.target).closest('.file_upload_wrap').data('map-code');
                                var fileObj = window.$attach[option.refType][mapCode][$(e.target).data('idx')];
                                if(option.mapCodes[mapCodeIndex].exts) {
                                    var extAllow = false;
                                    option.mapCodes[mapCodeIndex].exts.forEach(function(ext) {
                                        if(file.name.lastIndexOf(ext) == file.name.length - ext.length) {
                                            extAllow = true;
                                        }
                                    });
                                    if(extAllow == false) {
                                        alert(option.mapCodes[mapCodeIndex].exts.join(',') + ' 확장자만 등록 가능 합니다.');
                                        return false;
                                    }
                                }
                                toggleDelete(true, option.refType, mapCode, $(e.target).data('idx'));
                                fileObj.displayName = file.name;
                                fileObj.fileSize = file.size;
                                fileObj.fileType = file.type;
                                fileObj.status = 'loading';
                                toggleDelete(false, option.refType, mapCode, $(e.target).data('idx'));
                                data.process().done(function () {
                                    data.submit();
                                });
                            },
                            done: function (e, data) {
                                var mapCode = $(e.target).closest('.file_upload_wrap').data('map-code');
                                var fileObj = window.$attach[option.refType][mapCode][$(e.target).data('idx')];
                                fileObj.savedName = data.result.savedName;
                                fileObj.status = 'complete';
                                fileObj.url = '/api/attach/temp/view/' + data.result.savedName;
                                toggleDelete(false, option.refType, mapCode, $(e.target).data('idx'));
                                var $file_input = getFileInput(option.refType, mapCode, $(e.target).data('idx'));
                                var $file_upload_item = $file_input.closest('.file_upload_item');
                                var $file_delete = $file_upload_item.find('.file_delete');
                                $file_delete.show().on('click',function(){
                                    toggleDelete(true, option.refType, mapCode, $(e.target).data('idx'));
                                });
                            },
                            error: function (e) {
                                var mapCode = $(e.target).closest('.file_upload_wrap').data('map-code');
                                toggleDelete(true, option.refType, mapCode, $(e.target).data('idx'));
                                alert('파일 업로드 중 오류가 발생했습니다. 다시 시도해 주세요.');
                            }
                        });
                        window.$attach[option.refType][mapCode].push({});
                    }
                });

                // refKey가 있는 경우 기존 파일 가져오기
                if(option.refKey) {
                    $.ajax('/api/attach/' + option.refType + '/' + option.refKey, {
                        method: 'GET',
                        success: function (data) {
                            option.mapCodes.forEach(function(mapCodeItem) {
                                var mapCode = mapCodeItem.mapCode;
                                if(data[mapCode]) {
                                    data[mapCode].forEach(function(fileItem, fileIdx) {
                                        var fileObj = window.$attach[option.refType][mapCode][fileItem.order-1];
                                        if(fileObj) {
                                            fileObj.idx = fileItem.idx;
                                            fileObj.orgDisplayName = fileItem.displayName;
                                            fileObj.fileType = fileItem.fileType;
                                            fileObj.url = '/api/attach/view/'+option.refType+'/'+option.refKey+'/'+mapCode+'/'+fileItem.order;
                                            toggleDelete(false, option.refType, mapCode, fileItem.order-1);
                                            var $file_input = getFileInput(option.refType, mapCode, fileItem.order-1);
                                            var $file_upload_item = $file_input.closest('.file_upload_item');
                                            var $file_delete = $file_upload_item.find('.file_delete');
                                            $file_delete.show().on('click',function(){
                                                toggleDelete(true, option.refType, mapCode, fileItem.order-1);
                                            });
                                        }
                                    });
                                }
                            });
                        },
                        error: function (xhr, err) {
                            alert('첨부파일 조회 중 오류가 발생했습니다.');
                        }
                    });
                }

                function toggleDelete(isDelete, refType, mapCode, index) {
                    var fileObj = window.$attach[refType][mapCode][index];
                    var $file_input = getFileInput(refType, mapCode, index);
                    var $file_upload_item = $file_input.closest('.file_upload_item');
                    var $file_txt = $file_upload_item.find('.file_txt');
                    if(isDelete == true) {  // 삭제 요청 하기
                        $file_txt.text('');
                        if(fileObj.orgDisplayName) {    // 기존 파일이 있는 경우
                            // mapCode == '_deleted'로 이동
                            window.$attach[refType]['_deleted'].push({
                                idx: fileObj.idx
                            });
                            // 기존 파일 제거
                            delete fileObj.idx;
                            delete fileObj.orgDisplayName;
                        }
                        delete fileObj.fileType;
                        delete fileObj.displayName;
                        delete fileObj.savedName;
                        delete fileObj.fileSize;
                        delete fileObj.status;
                        delete fileObj.url;
                        $file_upload_item.find('.file_delete').hide();
                    } else {  // 삭제 아닌 상태
                        if(fileObj.orgDisplayName) {    // 기존 파일이 있는 경우
                            // 기존 파일 표시
                            if(fileObj.fileType.indexOf('image') >= 0) {
                                $file_txt.html(
                                    '<a href="'+fileObj.url+'" title="클릭 시 이미지 보기" target="_blank" style="text-decoration: none;">' +
                                    fileObj.orgDisplayName + ' <small style="color: #999;">(현재 파일)</small>' +
                                    '</a>'
                                );
                            } else {
                                $file_txt.html(fileObj.orgDisplayName + ' <small style="color: #999;">(현재 파일)</small>');
                            }
                            // 새로운 파일 제거
                            delete fileObj.displayName;
                            delete fileObj.savedName;
                            delete fileObj.fileSize;
                            delete fileObj.fileType;
                        } else if(fileObj.displayName) {    // 새로운 파일이 있는 경우
                            if(fileObj.status == 'complete') {
                                $file_txt.html(
                                    '<a href="'+fileObj.url+'" title="클릭 시 이미지 보기" target="_blank" style="text-decoration: none;">' +
                                    fileObj.displayName + ' <small style="color: green;">(임시 등록됨)</small>' +
                                    '</a>'
                                );
                            } else {
                                $file_txt.html(fileObj.displayName + ' <small style="color: red;">(업로드 중...)</small>');
                            }
                        }
                    }
                }
            },

            /**
             * 업로드 객체 제거 (처음 상태로 되돌림)
             * @param refType  삭제할 업로드 객체의 refType
             */
            destroy: function(refType) {
                var attachObj = window.$attach[refType];
                if(attachObj) {
                    Object.keys(attachObj).forEach(function(mapCode){
                        if(mapCode !='_deleted') {
                            attachObj[mapCode].forEach(function(item, index) {
                                var $file_input = getFileInput(refType, mapCode, index);
                                var $file_upload_item = $file_input.closest('.file_upload_item');
                                $file_upload_item.find('.file_txt').text('');
                                $file_upload_item.find('.file_delete').hide();
                                $file_input.fileupload('destroy');
                            });
                        }
                    });
                    delete window.$attach[refType];
                }
            }
        };
    }

    function getFileInput(refType, mapCode, index) {
        return $('.file_upload_wrap[data-ref-type='+refType+'][data-map-code='+mapCode+'] input[type=file][data-idx='+index+']');
    }
});