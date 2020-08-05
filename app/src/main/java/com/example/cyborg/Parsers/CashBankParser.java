package com.example.cyborg.Parsers;

import android.os.AsyncTask;

import com.example.cyborg.Interface.OnParseCompleted;
import com.example.cyborg.Models.CashandBankModel;

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

public class CashBankParser extends AsyncTask<String,Void, ArrayList<CashandBankModel>> {


    private OnParseCompleted onParseCompleted;
    private DocumentBuilderFactory documentBuilderFactory;

    public CashBankParser(OnParseCompleted onParseCompleted){
        this.onParseCompleted = onParseCompleted;
        this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
    }

    @Override
    protected ArrayList<CashandBankModel> doInBackground(String... strings) {
        try {
            ArrayList<CashandBankModel> cashandBankModels = new ArrayList<>();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new InputSource(new StringReader(strings[0])));
            document.normalize();
            NodeList nodes = document.getElementsByTagName("DATA");
            for (int i = 0; i < nodes.getLength(); i++) {
                if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element cashBank = (Element) nodes.item(i);
                    boolean isLed = cashBank.getElementsByTagName("LEDBOOLEAN").item(0).getTextContent().equalsIgnoreCase("YES");
                    String drAmount = cashBank.getElementsByTagName("DSPCLDRAMTA").item(0).getTextContent();
                    String crAmount = cashBank.getElementsByTagName("DSPCLCRAMTA").item(0).getTextContent();
                    cashandBankModels.add(new CashandBankModel(cashBank.getElementsByTagName("DSPDISPNAME").item(0).getTextContent(),
                            isLed,drAmount.isEmpty()?"0":drAmount,
                            crAmount.isEmpty()?"0":crAmount));

                }
            }

            return  cashandBankModels;
        } catch (IOException | SAXException | ParserConfigurationException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<CashandBankModel> cashandBankModels) {
        if(cashandBankModels != null) {
            onParseCompleted.OnParsed(cashandBankModels);
        }else{
            onParseCompleted.OnParseFailed("Error Occurred while parsing data");
        }
    }
}
