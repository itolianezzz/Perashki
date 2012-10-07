package ru.spb.itolia.perashki;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import ru.spb.itolia.perashki.beans.Piro;

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
    private static final String HOST = "http://www.perashki.ru/";
    private static final String GOOD = "piro/good/";
    private static final String BEST = "piro/best/";
    private static final String PIRO_CLASS_NAME = "pirojusttext";

    public static final int PIRO_GOOD = 1;
    public static final int PIRO_BEST = 2;

    public static List<Piro> getGood() throws IOException {
        List<Element> elements = getPiros(GOOD);
        List<Piro> piros = parsePiros(elements);
        return piros;
    }

    public static List<Piro> getBest() throws IOException {
        List<Element> elements = getPiros(BEST);
        List<Piro> piros = parsePiros(elements);
        return piros;
    }

    private static List<Element> getPiros(String piroType) throws IOException {
        HttpClient client = new HttpClient();
        PostMethod getPiros = new PostMethod(HOST + piroType);
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
