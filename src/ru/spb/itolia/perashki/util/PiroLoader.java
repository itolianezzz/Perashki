package ru.spb.itolia.perashki.util;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import ru.spb.itolia.perashki.beans.Piro;
import ru.spb.itolia.perashki.beans.piroType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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


    public static List<Piro> getNew(int current_page) throws IOException {
        List<Element> elements = getPiros(piroType.NEW, current_page);
        return parsePiros(elements);
    }

    public static List<Piro> getGood(int current_page) throws IOException {
        List<Element> elements = getPiros(piroType.GOOD, current_page);
        return parsePiros(elements);
    }

    public static List<Piro> getBest(int current_page) throws IOException {
        List<Element> elements = getPiros(piroType.BEST, current_page);
        return parsePiros(elements);
    }

    public static List<Piro> getRandom() throws IOException {
        List<Element> elements = getPiros(piroType.RANDOM, 0);
        return parsePiros(elements);
    }

    public static List<Piro> getAll(int current_page) throws IOException {
        List<Element> elements = getPiros(piroType.ALL, current_page);
        return parsePiros(elements);
    }

    private static List<Element> getPiros(piroType type, int page) throws IOException {
        HttpClient client = new HttpClient();
        PostMethod getPiros;
        switch (type) {
            case RANDOM:
                getPiros = new PostMethod(HOST + type.getPath());
                break;
            default:
                getPiros = new PostMethod(HOST + type.getPath() + "?p=" + page);
                break;
        }
        getPiros.addParameter("confirm", "1");
        client.getState().addCookie(new Cookie("www.perashki.ru", "userconfirmation", "true"));
        client.executeMethod(getPiros);
        Source response = new Source(getPiros.getResponseBodyAsStream());

        return response.getAllElementsByClass(PIRO_CLASS_NAME);
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
