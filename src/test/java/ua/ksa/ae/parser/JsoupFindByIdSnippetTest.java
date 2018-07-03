package ua.ksa.ae.parser;

import junit.framework.Assert;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.net.URL;

/**
 * @author ksa on 7/2/18.
 * @project parser
 */
public class JsoupFindByIdSnippetTest {

    @Test
    public void originalAndFirst() {
        String originalFile = "sample-0-origin.html";
        String diffFile = "sample-1-evil-gemini.html";
        String originalId = "make-everything-ok-button";
        test(originalFile, diffFile, originalId);
    }

    @Test
    public void originalAndSecond() {
        String originalFile = "sample-0-origin.html";
        String diffFile = "sample-2-container-and-clone.html";
        String originalId = "make-everything-ok-button";
        test(originalFile, diffFile, originalId);
    }

    @Test
    public void originalAndThrid() {
        String originalFile = "sample-0-origin.html";
        String diffFile = "sample-3-the-escape.html";
        String originalId = "make-everything-ok-button";
        test(originalFile, diffFile, originalId);
    }

    @Test
    public void originalAndForth() {
        String originalFile = "sample-0-origin.html";
        String diffFile = "sample-1-evil-gemini.html";
        String originalId = "make-everything-ok-button";
        test(originalFile, diffFile, originalId);
    }

    private void test(String originalFile, String diffFile, String originalId) {
        JsoupFindByIdSnippet jsoupFindByIdSnippet = new JsoupFindByIdSnippet();
        URL url = JsoupFindByIdSnippetTest.class.getClassLoader().getResource(originalFile);
        Assert.assertNotNull(url);
        Element original = jsoupFindByIdSnippet.loadOriginal(url.getFile(), originalId);
        URL diff = JsoupFindByIdSnippetTest.class.getClassLoader().getResource(diffFile);
        Assert.assertNotNull(url);
        Element different = jsoupFindByIdSnippet.search(diff.getFile());
        jsoupFindByIdSnippet.path();
        Assert.assertEquals(original.id(),different.id());
    }


}