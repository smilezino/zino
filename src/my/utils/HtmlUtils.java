package my.utils;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * 过滤非法标签
 * Created by zino on 8/21/14.
 */
public class HtmlUtils {
    public final static Whitelist whiteList = Whitelist.basic();

    static {
        whiteList.addTags("embed", "object", "param", "p", "span", "pre", "div", "h1",
                "h2", "h3", "h4", "h5", "h6", "table", "tbody", "tr", "th", "td", "ul", "li",
                "strong", "em", "img");
        whiteList.addAttributes("a", "target");
        whiteList.addAttributes("pre", "class");
        whiteList.addAttributes("img", "src");
    }

    public static String filter(String html) {
        if (StringUtils.isBlank(html)) {
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
