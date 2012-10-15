package ru.spb.itolia.perashki.util;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.protocol.HTTP;
import ru.spb.itolia.perashki.beans.ParamTypes;
import ru.spb.itolia.perashki.beans.Piro;

import java.io.IOException;
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
    //private static final CharSequence GOOD = "piro/good/";
    private static final String BEST = "piro/best/";
    private static final String NEW = "piro/new/";
    private static final String PIRO_CLASS_NAME = "pirojusttext";


  /*  public static List<Piro> getNew(Map<String, String> params) throws IOException {
        List<Element> elements = getPiros(piroType.NEW, params);
        return parsePiros(elements);
    }
*/
 /*   public static List<Piro> getGood(Map<String, String> params) throws IOException {
        List<Element> elements = getPiros(piroType.GOOD, params);
        return parsePiros(elements);
    }*/

/*    public static List<Piro> getBest(Map<String, String> params) throws IOException {
        List<Element> elements = getPiros(piroType.BEST, params);
        return parsePiros(elements);
    }*/

/*    public static List<Piro> getRandom() throws IOException {
        List<Element> elements = getPiros(piroType.RANDOM, 0);
        return parsePiros(elements);
    }*/

/*    public static List<Piro> getAll(Map<String, String> params) throws IOException {
        List<Element> elements = getPiros(piroType.ALL, params);
        return parsePiros(elements);
    }*/



    public static List<Piro> getPiros(Map<String, String> params) throws IOException {
        HttpClient client = new HttpClient();
        PostMethod getPiros;
        String url = buildUrl(params);
        getPiros = new PostMethod(url);
        getPiros.addParameter("confirm", "1");
        //client.getState().addCookie(new Cookie("www.perashki.ru", "userconfirmation", "true"));
        client.executeMethod(getPiros);
        Source response = new Source(getPiros.getResponseBodyAsStream());
        List<Element> elements = response.getAllElementsByClass(PIRO_CLASS_NAME);
        return parsePiros(elements);
    }

    private static String buildUrl(Map<String, String> params) {
        String url = HOST + params.get(ParamTypes.PIROTYPE);
        params.remove(ParamTypes.PIROTYPE);
        if(!params.isEmpty()) {
            String paramsString = "?";
            for (Map.Entry<String, String> entry: params.entrySet()) {
                if(entry.getValue().isEmpty()) continue;
                String key= entry.getKey();
                String value = entry.getValue();
                paramsString = new StringBuilder(paramsString).append(key).append("=").append(value).toString();
                try {
                    paramsString = URLEncoder.encode(paramsString, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            url = new StringBuilder(url).append(paramsString).toString();
        }
        return url;
    }

    private static List<Piro> parsePiros(List<Element> elements) {
        List<Piro> piros = new ArrayList<Piro>();
        String regexpToRemoveOddTags = "(<(/?(a|h).+?)>)";
        String regexpToChangeTheNewLineSymbol = "<br/>";
        for(Element el: elements) {
            OutputDocument output = new OutputDocument(el.getContent());
            Piro piro = new Piro();
            piro.setId(el.getAttributeValue("id"));
            piro.setText(output.toString().replaceAll(regexpToRemoveOddTags, "").replaceAll(regexpToChangeTheNewLineSymbol, "\n"));
            piros.add(piro);
        }
        return piros;
    }
}
