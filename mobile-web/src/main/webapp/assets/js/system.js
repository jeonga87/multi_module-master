$.ajaxSetup({
    headers: {
        Accept: "application/json; charset=utf-8"
    },
    contentType: "application/json; charset=utf-8",
    async: true,
    timeout: 10000,
    cache: false,
    error: function (xhr, err) {
        if(xhr.status == '401') {
            alert('로그인이 필요합니다.');
            location.href="/";
        } else {
            alert('서버와 통신 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.');
        }
    }
});

$(function() {
    if (typeof Object.assign != 'function') {
        Object.assign = function(target, varArgs) {
            'use strict';
            if (target == null) { // TypeError if undefined or null
                throw new TypeError('Cannot convert undefined or null to object');
            }

            var to = Object(target);

            for (var index = 1; index < arguments.length; index++) {
                var nextSource = arguments[index];

                if (nextSource != null) { // Skip over if undefined or null
                    for (var nextKey in nextSource) {
                        // Avoid bugs when hasOwnProperty is shadowed
                        if (Object.prototype.hasOwnProperty.call(nextSource, nextKey)) {
                            to[nextKey] = nextSource[nextKey];
                        }
                    }
                }
            }
            return to;
        };
    }

    /**
     * onlyNumberInput class는 숫자만 입력하도록
     */
    $(document).on('keyup', '.onlyNumberInput', function(){
        $(this).val($(this).val().replace(/[^0-9]/g,""));
    });

    /**
     * numberSymbolInput class는 숫자,기호(.;\-) 입력하도록
     */
    $(document).on('keyup', '.numberSymbolInput', function(){
        $(this).val($(this).val().replace(/[^0-9.;:\-]/g,""));
    });

    /**
     * notKorInput class는 한글입력 안되도록
     */
    $(document).on('keyup', '.notKorInput', function(){
        $(this).val($(this).val().replace(/[^a-z0-9~!@#$%^.&*()_+|<>?:{}]/gi,""));
    });

    /**
     * meail2Input class는 한글안되고 특수문자 "."가능
     */
    $(document).on('keyup', '.meail2Input', function(){
        $(this).val($(this).val().replace(/[^a-z0-9~!@#$%^.&*()_+|<>?:{}]/gi,""));
    });

    /**
     * array 중복값 제거
     */
    Array.prototype.unique = function() {
        var a = this.concat();
        for(var i=0; i<a.length; ++i) {
            for(var j=i+1; j<a.length; ++j) {
                if(a[i] === a[j])
                    a.splice(j--, 1);
            }
        }
        return a;
    };

    /**
     * Json 형태로 ajax 호출
     */
    $.ajaxJson = function(url, options) {
        options = $.extend({
            type: "POST"
        }, options);

        // form 전송 시
        if (!options.data && typeof options.form == 'object') {
            options.data = $.formToJson(options.form);
        }

        // form 전송이 아닐시
        if (options.data && (options.form == 'undefined' || options.form == '' || options.form == null)) {
        	options.data = JSON.stringify(options.data);
        }

        // 첨부파일 전송 시
        if (options.data && options.attachRefType) {
            var attachBag = $.getAttachBag(options.attachRefType);
            if (attachBag) {
                var dataObj = JSON.parse(options.data);
                dataObj.attachBag = attachBag;
                options.data = JSON.stringify(dataObj);
            }
        }

        return $.ajax(url, options);
    };

    /**
     * Form 객체를 Json으로 변환
     * @param form
     * @param refType (선택) 첨부파일 refType
     */
    $.formToJson = function(form, refType) {
        var dataObj = {};
        $(form).serializeArray().forEach(function(item) {
            if(dataObj[item.name]) {
                if(Array.isArray(dataObj[item.name]) != true) {
                    dataObj[item.name] = [dataObj[item.name]];
                }
                dataObj[item.name].push(item.value);
            } else {
                dataObj[item.name] = item.value;
            }
        });
        if(refType) {
            var attachBag = $.getAttachBag(refType);
            if(attachBag) {
                dataObj.attachBag = dataObj;
            }
        }
        return JSON.stringify(dataObj);
    };

    /**
     * 첨부파일의 refType으로 window.$attach에 저장된 첨부파일 정보를 attachBag 객체로 가공
     * @param refType
     * @returns {*}
     */
    $.getAttachBag = function(refType) {
        var attachBag = null;
        // window.$attach[refType]에 적재된 첨부파일 업로드 정보를 attachBag으로 적재한다.
        if(refType && window.$attach[refType]) {
            var attachBag = {};
            for(var mapCode in window.$attach[refType]) {
                attachBag[mapCode] = [];
                window.$attach[refType][mapCode].forEach(function(file, order) {
                    if(file.idx || file.savedName) {
                        file.order = order+1;
                        attachBag[mapCode].push(file);
                    }
                });
            }
            attachBag = attachBag;
        }
        return attachBag;
    };

    /**
     * $.escapeHtml()에서 사용하는 데이터
     */
    var entityMap = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#39;',
        '/': '&#x2F;',
        '`': '&#x60;',
        '=': '&#x3D;'
    };

    /**
     * String의 HTML 태그 제거
     * @param string
     * @returns {string}
     */
    $.escapeHtml = function(string) {
        return String(string).replace(/[&<>"'`=\/]/g, function (s) {
            return entityMap[s];
        });
    };

    /**
     * 숫자 입력 체크 ("." 포함)
     * 작성자 : 이호성
     * 작성일 : 2018.03.16
     * @param object
     * @returns {*}
     */
    $.inpuOnlyDNumber = function(obj)
    {
        if ((event.keyCode >= 48 && event.keyCode <= 57) || event.keyCode == 46) { //숫자키만 입력
        	return true;
        } else {
        	 event.preventDefault();
        }
    }
    /**
     * 숫자 입력 체크 (숫자만)
     * 작성자 : 이호성
     * 작성일 : 2018.03.16
     * @param object
     * @returns {*}
     */
    $.inpuOnlyNumber = function(obj)
    {
    	//console.log(event.keyCode);

    	//숫자키만 입력, 36:home , 35:end , 37:Left, 39:Right, 8: backSpace, 46 : Delete
        if (event.keyCode >= 48 && event.keyCode <= 57 || event.keyCode >= 96 && event.keyCode <= 105 ||event.keyCode === 36 || event.keyCode === 35 || event.keyCode === 37 ||
        		event.keyCode === 39 || event.keyCode === 8 || event.keyCode === 9 || event.keyCode === 46) {
        	return true;
        } else {
        	 event.preventDefault();
        }
    }

    /**
     * 콤마찍기
     * 작성자 : 이호성
     * 작성일 : 2018.03.16
     * @param value
     * @returns txtNumber
     */
    $.setComma = function(value)
    {

    	var txtNumber = ''+value;
    	// 소숫점이 없는 경우
    	if(txtNumber.indexOf(".") == -1){
    		return comma($.uncomma(txtNumber));

    	}else{	//소숫점이 있는 경우
    		var tmp = txtNumber.split(".");
    		return comma($.uncomma(tmp[0]))+"."+tmp[1];
    	}

    	return txtNumber;
    }

    /**
     * 정수 콤마찍기
     * 작성자 : 이호성
     * 작성일 : 2018.03.16
     * @param str
     * @returns 콤마가 추가된 str
     */
    function comma(str) {
        str = String(str);
        return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
    }

    /**
     * 정수 콤마풀기
     * 작성자 : 이호성
     * 작성일 : 2018.03.16
     * @param str
     * @returns 콤마가 제거된 str
     */
    $.uncomma = function(str) {
        str = String(str);
        return str.replace(/,/g, '');
    }


    /**
     * 키다운(엔터키) 이벤트 발생시 파라미터값으로 들어온 function 실행
     * 작성자 : 이수영
     * 작성일 : 2018.04.27
     * @param fn
     */
    $.inputKeydownEvent = function(fn) {
        if (event.keyCode == 13) {
            fn();
        }
    };

    function appendContent($el, content) {
        if (!content) return;

        // Simple test for a jQuery element
        $el.append(content.jquery ? content.clone() : content);
    }

    function appendBody($body, $element, opt) {
        // Clone for safety and convenience
        // Calls clone(withDataAndEvents = true) to copy form values.
        var $content = $element.clone(opt.formValues);

        if (opt.formValues) {
            // Copy original select and textarea values to their cloned counterpart
            // Makes up for inability to clone select and textarea values with clone(true)
            copyValues($element, $content, 'select, textarea');
        }

        if (opt.removeScripts) {
            $content.find('script').remove();
        }

        if (opt.printContainer) {
            // grab $.selector as container
            $content.appendTo($body);
        } else {
            // otherwise just print interior elements of container
            $content.each(function() {
                $(this).children().appendTo($body)
            });
        }
    }

    // Copies values from origin to clone for passed in elementSelector
    function copyValues(origin, clone, elementSelector) {
        var $originalElements = origin.find(elementSelector);

        clone.find(elementSelector).each(function(index, item) {
            $(item).val($originalElements.eq(index).val());
        });
    }

    var opt;
    $.fn.printThis = function(options) {
        opt = $.extend({}, $.fn.printThis.defaults, options);
        var $element = this instanceof jQuery ? this : $(this);

        var strFrameName = "printThis-" + (new Date()).getTime();

        if (window.location.hostname !== document.domain && navigator.userAgent.match(/msie/i)) {
            // Ugly IE hacks due to IE not inheriting document.domain from parent
            // checks if document.domain is set by comparing the host name against document.domain
            var iframeSrc = "javascript:document.write(\"<head><script>document.domain=\\\"" + document.domain + "\\\";</s" + "cript></head><body></body>\")";
            var printI = document.createElement('iframe');
            printI.name = "printIframe";
            printI.id = strFrameName;
            printI.className = "MSIE";
            document.body.appendChild(printI);
            printI.src = iframeSrc;

        } else {
            // other browsers inherit document.domain, and IE works if document.domain is not explicitly set
            var $frame = $("<iframe id='" + strFrameName + "' name='printIframe' />");
            $frame.appendTo("body");
        }

        var $iframe = $("#" + strFrameName);

        // show frame if in debug mode
        if (!opt.debug) $iframe.css({
            position: "absolute",
            width: "0px",
            height: "0px",
            left: "-600px",
            top: "-600px"
        });

        // before print callback
        if (typeof opt.beforePrint === "function") {
            opt.beforePrint();
        }

        // $iframe.ready() and $iframe.load were inconsistent between browsers
        setTimeout(function() {

            // Add doctype to fix the style difference between printing and render
            function setDocType($iframe, doctype){
                var win, doc;
                win = $iframe.get(0);
                win = win.contentWindow || win.contentDocument || win;
                doc = win.document || win.contentDocument || win;
                doc.open();
                doc.write(doctype);
                doc.close();
            }

            if (opt.doctypeString){
                setDocType($iframe, opt.doctypeString);
            }

            var $doc = $iframe.contents(),
                $head = $doc.find("head"),
                $body = $doc.find("body"),
                $base = $('base'),
                baseURL;

            // add base tag to ensure elements use the parent domain
            if (opt.base === true && $base.length > 0) {
                // take the base tag from the original page
                baseURL = $base.attr('href');
            } else if (typeof opt.base === 'string') {
                // An exact base string is provided
                baseURL = opt.base;
            } else {
                // Use the page URL as the base
                baseURL = document.location.protocol + '//' + document.location.host;
            }

            $head.append('<base href="' + baseURL + '">');

            // import page stylesheets
            if (opt.importCSS) $("link[rel=stylesheet]").each(function() {
                var href = $(this).attr("href");
                if (href) {
                    var media = $(this).attr("media") || "all";
                    $head.append("<link type='text/css' rel='stylesheet' href='" + href + "' media='" + media + "'>");
                }
            });

            // import style tags
            if (opt.importStyle) $("style").each(function() {
                $head.append(this.outerHTML);
            });

            // add title of the page
            if (opt.pageTitle) $head.append("<title>" + opt.pageTitle + "</title>");

            // import additional stylesheet(s)
            if (opt.loadCSS) {
                if ($.isArray(opt.loadCSS)) {
                    jQuery.each(opt.loadCSS, function(index, value) {
                        $head.append("<link type='text/css' rel='stylesheet' href='" + this + "'>");
                    });
                } else {
                    $head.append("<link type='text/css' rel='stylesheet' href='" + opt.loadCSS + "'>");
                }
            }

            var pageHtml = $('html')[0];

            // CSS VAR in html tag when dynamic apply e.g.  document.documentElement.style.setProperty("--foo", bar);
            $doc.find('html').prop('style', pageHtml.style.cssText);

            // copy 'root' tag classes
            var tag = opt.copyTagClasses;
            if (tag) {
                tag = tag === true ? 'bh' : tag;
                if (tag.indexOf('b') !== -1) {
                    $body.addClass($('body')[0].className);
                }
                if (tag.indexOf('h') !== -1) {
                    $doc.find('html').addClass(pageHtml.className);
                }
            }

            // print header
            appendContent($body, opt.header);

            if (opt.canvas) {
                // add canvas data-ids for easy access after cloning.
                var canvasId = 0;
                // .addBack('canvas') adds the top-level element if it is a canvas.
                $element.find('canvas').addBack('canvas').each(function(){
                    $(this).attr('data-printthis', canvasId++);
                });
            }

            appendBody($body, $element, opt);

            if (opt.canvas) {
                // Re-draw new canvases by referencing the originals
                $body.find('canvas').each(function(){
                    var cid = $(this).data('printthis'),
                        $src = $('[data-printthis="' + cid + '"]');

                    this.getContext('2d').drawImage($src[0], 0, 0);

                    // Remove the markup from the original
                    if ($.isFunction($.fn.removeAttr)) {
                        $src.removeAttr('data-printthis');
                    } else {
                        $.each($src, function(i, el) {
                            el.removeAttribute('data-printthis');
                        });
                    }
                });
            }

            // remove inline styles
            if (opt.removeInline) {
                // Ensure there is a selector, even if it's been mistakenly removed
                var selector = opt.removeInlineSelector || '*';
                // $.removeAttr available jQuery 1.7+
                if ($.isFunction($.removeAttr)) {
                    $body.find(selector).removeAttr("style");
                } else {
                    $body.find(selector).attr("style", "");
                }
            }

            // print "footer"
            appendContent($body, opt.footer);

            // attach event handler function to beforePrint event
            function attachOnBeforePrintEvent($iframe, beforePrintHandler) {
                var win = $iframe.get(0);
                win = win.contentWindow || win.contentDocument || win;

                if (typeof beforePrintHandler === "function") {
                    if ('matchMedia' in win) {
                        win.matchMedia('print').addListener(function(mql) {
                            if(mql.matches)  beforePrintHandler();
                        });
                    } else {
                        win.onbeforeprint = beforePrintHandler;
                    }
                }
            }
            attachOnBeforePrintEvent($iframe, opt.beforePrintEvent);

            setTimeout(function() {
                if ($iframe.hasClass("MSIE")) {
                    // check if the iframe was created with the ugly hack
                    // and perform another ugly hack out of neccessity
                    window.frames["printIframe"].focus();
                    $head.append("<script>  window.print(); </s" + "cript>");
                } else {
                    // proper method
                    if (document.queryCommandSupported("print")) {
                        $iframe[0].contentWindow.document.execCommand("print", false, null);
                    } else {
                        $iframe[0].contentWindow.focus();
                        $iframe[0].contentWindow.print();
                    }
                }

                // remove iframe after print
                if (!opt.debug) {
                    setTimeout(function() {
                        $iframe.remove();

                    }, 1000);
                }

                // after print callback
                if (typeof opt.afterPrint === "function") {
                    opt.afterPrint();
                }

            }, opt.printDelay);

        }, 333);

    };

    // defaults
    $.fn.printThis.defaults = {
        debug: false,               // show the iframe for debugging
        importCSS: true,            // import parent page css
        importStyle: false,         // import style tags
        printContainer: true,       // print outer container/$.selector
        loadCSS: "",                // path to additional css file - use an array [] for multiple
        pageTitle: "",              // add title to print page
        removeInline: false,        // remove inline styles from print elements
        removeInlineSelector: "*",  // custom selectors to filter inline styles. removeInline must be true
        printDelay: 333,            // variable print delay
        header: null,               // prefix to html
        footer: null,               // postfix to html
        base: false,                // preserve the BASE tag or accept a string for the URL
        formValues: true,           // preserve input/form values
        canvas: false,              // copy canvas content
        doctypeString: '<!DOCTYPE html>', // enter a different doctype for older markup
        removeScripts: false,       // remove script tags from print content
        copyTagClasses: false,      // copy classes from the html & body tag
        beforePrintEvent: null,     // callback function for printEvent in iframe
        beforePrint: null,          // function called before iframe is filled
        afterPrint: null            // function called before iframe is removed
    };
(jQuery);

});

/**
 * 모바일 접속 여부
 * 작성자 : 전하라
 * 작성일 : 2019.12.24
 * @return boolean
 */
function isMobile() {
    var filter = "win16|win32|win64|mac|macintel";
    if ( navigator.platform ) {
        if ( filter.indexOf( navigator.platform.toLowerCase() ) < 0 ) {
            return true;        //mobile

        } else {
            return false;       //pc

        }
    }
}