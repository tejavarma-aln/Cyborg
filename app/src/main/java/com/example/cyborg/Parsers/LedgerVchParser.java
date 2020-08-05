package com.example.cyborg.Parsers;

import android.os.AsyncTask;

import com.example.cyborg.Interface.OnParseCompleted;
import com.example.cyborg.Models.BaseVoucherModel;

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

public class LedgerVchParser extends AsyncTask<String,Void, ArrayList<BaseVoucherModel>> {

    private OnParseCompleted onParseCompleted;
    private DocumentBuilderFactory documentBuilderFactory;

    public LedgerVchParser(OnParseCompleted onParseCompleted){
        this.onParseCompleted = onParseCompleted;
        this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
    }

    @Override
    protected ArrayList<BaseVoucherModel> doInBackground(String... strings) {
        try {
            ArrayList<BaseVoucherModel>   baseVoucherModels = new ArrayList<>();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new InputSource(new StringReader(strings[0])));
            document.normalize();
            NodeList nodes = document.getElementsByTagName("LEDINFO");
            for (int i = 0; i < nodes.getLength(); i++) {
                if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element  voucher = (Element) nodes.item(i);
                    String drAmount = voucher.getElementsByTagName("DSPVCHDRAMT").item(0).getTextContent();
                    String crAmount = voucher.getElementsByTagName("DSPVCHCRAMT").item(0).getTextContent();
                    baseVoucherModels.add(new BaseVoucherModel(voucher.getElementsByTagName("DSPVCHTYPE").item(0).getTextContent(),
                            voucher.getElementsByTagName("DSPVCHLEDACCOUNT").item(0).getTextContent(),
                            "NA",
                            voucher.getElementsByTagName("DSPVCHDATE").item(0).getTextContent(),
                            drAmount.isEmpty()?crAmount.concat("Cr"):String.valueOf(Double.parseDouble(drAmount) * (-1)).concat("Dr")

                    ));

                }
            }
            return baseVoucherModels;
        } catch (IOException | SAXException | ParserConfigurationException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<BaseVoucherModel> baseVoucherModels) {
        if(baseVoucherModels != null){
            onParseCompleted.OnParsed(baseVoucherModels);
        }else{
            onParseCompleted.OnParseFailed("Error Occurred while parsing data");
        }
    }
}
