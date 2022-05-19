<div class="attach-wrap " data-single="${param.isSingle}">
    <div class="file-attach-btn">
        <input
                type="file"
                id="file${param.index}"
                data-ref-type="${param.refType}"
                data-map-code="${param.mapCode}"
                class="upload-input hide"
        >
        <label for="file${param.index}" class="btn-light btn-medium">파일선택</label>
    </div>
    <ul class="file-name attach-list"></ul>
</div>
