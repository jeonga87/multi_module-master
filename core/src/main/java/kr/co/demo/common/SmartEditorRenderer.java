package kr.co.demo.common;

import org.springframework.stereotype.Component;

@Component
public class SmartEditorRenderer {

	public String renderSmartEditor(String id, String name, String content) {
		String htmlStr = "<div class=\"editor_wrap editor-input\">\n" +
				"\t<textarea id=\""+id+"\" name=\""+name+"\" style=\"display:none;\" class=\"service_reg\">"+content+"</textarea>\n" +
				"\t<script>\n" +
				"\t\tif(!window.oEditors) window.oEditors = [];\n" +
				"\t\tnhn.husky.EZCreator.createInIFrame({\n" +
				"\t\t\toAppRef: window.oEditors\n" +
				"\t\t\t, elPlaceHolder: \""+id+"\" //textarea에서 지정한 id와 일치해야 합니다.\n" +
				"\t\t\t//SmartEditor2Skin.html 파일이 존재하는 경로\n" +
				"\t\t\t, sSkinURI: \"/assets/smarteditor2/SmartEditor2Skin.html\"\n" +
				"\t\t\t, htParams : {\n" +
				"\t\t\t\t// 툴바 사용 여부 (true:사용/ false:사용하지 않음)\n" +
				"\t\t\t\tbUseToolbar : true,\n" +
				"\t\t\t\t// 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)\n" +
				"\t\t\t\tbUseVerticalResizer : true,\n" +
				"\t\t\t\t// 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)\n" +
				"\t\t\t\tbUseModeChanger : true,\n" +
				"\t\t\t\tbSkipXssFilter : false,\t\t\t// client-side xss filter 무시 여부 (true:사용하지 않음 / 그외:사용)\n" +
				"\t\t\t\tfOnBeforeUnload : function(){\n" +
				"\n" +
				"\t\t\t\t}\n" +
				"\t\t\t}\n" +
				"\t\t\t, fCreator: \"createSEditor2\"\n" +
				"\t\t});\n" +
				"\t</script>\n" +
				"</div>";

		return htmlStr;
	}
}
