package com.example.cyborg.Parsers;

import android.os.AsyncTask;

import com.example.cyborg.Interface.OnParseCompleted;
import com.example.cyborg.Models.LedgerModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class LedgerParser extends AsyncTask<String,Void, ArrayList<LedgerModel>> {

    private OnParseCompleted onParseCompleted;
    private DocumentBuilderFactory documentBuilderFactory;

    public LedgerParser(OnParseCompleted onParseCompleted){
        this.onParseCompleted = onParseCompleted;
        this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
    }

    @Override
    protected ArrayList<LedgerModel> doInBackground(String... strings) {
         try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
             ArrayList<LedgerModel> ledgerModels = new ArrayList<>();
             Document document = documentBuilder.parse(new InputSource(new StringReader(strings[0])));
             document.normalize();
             NodeList nodes = document.getElementsByTagName("LEDGER");
             for (int i = 0; i < nodes.getLength(); i++) {
                 if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                     Element ledger = (Element) nodes.item(i);
                     String closing = ledger.getElementsByTagName("CLEDCLOSINGBALANCE").item(0).getTextContent();
                     closing = closing.isEmpty()?"0":closing;
                     ledgerModels.add(new LedgerModel(ledger.getElementsByTagName("CLEDNAME").item(0).getTextContent(),
                             Double.parseDouble(closing) < 0 ?String.valueOf(Double.parseDouble(closing) * (-1)).concat("Dr"):(closing.equals("0")?"0":closing.concat("Cr"))));
                 }
             }
             return ledgerModels;
        } catch (ParserConfigurationException | SAXException | IOException |NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<LedgerModel> ledgerModels) {
        if(ledgerModels != null){
            onParseCompleted.OnParsed(ledgerModels);
        }else{
            onParseCompleted.OnParseFailed("Error Occurred while parsing data");
        }
    }
}
