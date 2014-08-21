package my.utils;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * 过滤非法标签
 * Created by zino on 8/21/14.
 */
public class HtmlUtils {
    public final static Whitelist whiteList = Whitelist.basicWithImages();
    static {
        whiteList.addTags("embed","object","param","p","span","pre","div","h1","h2","h3","h4","h5","table","tbody","tr","th","td","ul","li");
//        whiteList.addAttributes("span", "style");
//        whiteList.addAttributes("pre", "class");
//        whiteList.addAttributes("div", "class", "align");
//        whiteList.addAttributes("a", "target");
//        whiteList.addAttributes("table", "style","border","bordercolor","cellpadding","cellspacing", "align");
//        whiteList.addAttributes("img", "style","border", "align");
//        whiteList.addAttributes("th", "style", "align","rowspan", "colspan");
//        whiteList.addAttributes("td", "style", "align","rowspan", "colspan");
//        whiteList.addAttributes("object", "width", "height","classid","codebase","data","type");
//        whiteList.addAttributes("param", "name", "value");
//        whiteList.addAttributes("embed", "src","quality","width","height","allowFullScreen","allowScriptAccess","flashvars","name","type","pluginspage");
    }
    public static String filter(String html) {
        if(StringUtils.isBlank(html)) {
            return "";
        }
        return Jsoup.clean(html, whiteList);
    }

    public static void main(String[] args) {
        String html = "<a href='http://www.baidu.com/a.jpg' onclick='javascript:test()'>test</a>" +
                "<script>alert('xxxx')</script><span style='color:red'>test test</span>";
        System.out.println(HtmlUtils.filter(html));
    }
}
