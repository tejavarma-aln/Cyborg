package com.example.cyborg.Parsers;

import android.os.AsyncTask;
import android.util.Log;

import com.example.cyborg.Interface.OnUserDataParsed;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class UserParser extends AsyncTask<String,Void,String[]> {

    private OnUserDataParsed onUserDataParsed;
    private DocumentBuilderFactory documentBuilderFactory;

    public UserParser(OnUserDataParsed onUserDataParsed) {
        this.onUserDataParsed = onUserDataParsed;
        this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
    }

    @Override
    protected String[] doInBackground(String... strings) {
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new InputSource(new StringReader(strings[0])));
            document.normalize();
            Element companyName = (Element) document.getElementsByTagName("SIMPLEFIELD").item(0);
            Element serialNumber = (Element) document.getElementsByTagName("NAMEFIELD").item(0);
            return new String[]{companyName.getTextContent(),serialNumber.getTextContent()};
        } catch (IOException | SAXException | ParserConfigurationException | NullPointerException e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    protected void onPostExecute(String[] strings) {
        if(strings != null){
            onUserDataParsed.OnParsed(strings);
        }else{
            onUserDataParsed.OnParseFailed("Error Occurred while parsing data");
        }
    }
}
