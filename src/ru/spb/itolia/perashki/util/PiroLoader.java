package ru.spb.itolia.perashki.util;

import org.apache.http.protocol.HTTP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ru.spb.itolia.perashki.beans.ParamTypes;
import ru.spb.itolia.perashki.beans.Piro;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: itolia
 * Date: 05.10.12
 * Time: 11:11
 */
public class PiroLoader {
    public static final String HOST = "http://www.perashki.ru/";
    private static final String PIRO_CLASS_NAME = "pirojusttext";
    private static final String PIRO_INFO_CLASS = "simple_frame sml_cnd";
    private static Document doc;

    private static String buildUrl(Map<String, String> params) {
        String url = HOST + params.get(ParamTypes.PIROTYPE);
        params.remove(ParamTypes.PIROTYPE);
        if (!params.isEmpty()) {
            String paramsString = "?";
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getValue().isEmpty()) continue;
                if(!paramsString.equals("?")) {
                    paramsString += "&";
                }
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equalsIgnoreCase(ParamTypes.TEXT)) {
                    try {
                        value = URLEncoder.encode(value, HTTP.UTF_8);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
                paramsString = new StringBuilder(paramsString).append(key).append("=").append(value).toString();
            }
            url = new StringBuilder(url).append(paramsString).toString();
        }
        return url;
    }

    public static List<Piro> getPiros(Map<String, String> params) throws Exception {
        String url = buildUrl(params);
        doc = Jsoup.connect(url).data("confirm", "1").post();
        List<Element> elements = doc.getElementsByClass(PIRO_CLASS_NAME);
        return parsePiros(elements);
    }

    private static List<Piro> parsePiros(List<Element> elements) {
        List<Piro> piros = new ArrayList<Piro>();
        String regexpToRemoveOddTags = "(<(/?(a|h).+?)>)";
        String regexpToChangeTheNewLineSymbol = "<br />";
        for(Element el: elements) {
            Document output = new Document(el.html());
            Piro piro = new Piro();
            piro.setId(el.attr("id"));
            piro.setText(output.baseUri().replaceAll(regexpToRemoveOddTags, "").replaceAll("\n", "").replaceAll(regexpToChangeTheNewLineSymbol, "\n"));
            piros.add(piro);

        }
        return piros;
    }

    public static int getPages() {
        Element element;
        try {
            element = doc.getElementsByClass("pages").get(0);
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
        List<Element> elements = element.getElementsByTag("a");
        if(elements == null | elements.size() == 0){
            return 1;
        }
        int pages = Integer.parseInt(elements.get(elements.size()-1).text());
    return pages;
    }
}
