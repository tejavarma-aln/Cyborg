package com.example.cyborg.Parsers;

import android.os.AsyncTask;

import com.example.cyborg.Interface.OnParseCompleted;
import com.example.cyborg.Models.StockDetails;
import com.example.cyborg.Models.StockModel;

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

public class StockParser extends AsyncTask<String,Void, ArrayList<StockModel>> {

      private OnParseCompleted onParseCompleted;
      private DocumentBuilderFactory documentBuilderFactory;

      public StockParser(OnParseCompleted onParseCompleted){
          this.onParseCompleted = onParseCompleted;
          this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
      }

    @Override
    protected ArrayList<StockModel> doInBackground(String... strings) {
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            ArrayList<StockModel> stockModels = new ArrayList<>();
            Document document = documentBuilder.parse(new InputSource(new StringReader(strings[0])));
            document.normalize();
            NodeList nodes = document.getElementsByTagName("STOCK");
            for (int i = 0; i < nodes.getLength(); i++) {
                if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element stock = (Element) nodes.item(i);
                    String qty = stock.getElementsByTagName("DSPCLQTY").item(0).getTextContent();
                    String rate = stock.getElementsByTagName("DSPCLRATE").item(0).getTextContent();
                    String closing = stock.getElementsByTagName("DSPCLAMTA").item(0).getTextContent();
                    closing = closing.isEmpty()?"0":closing;
                    closing = Double.parseDouble(closing) < 0?String.valueOf(Double.parseDouble(closing) * (-1)):closing;
                    stockModels.add(new StockModel(stock.getElementsByTagName("DSPDISPNAME").item(0).getTextContent(),
                            new StockDetails(qty.isEmpty()?"0":qty,rate.isEmpty()?"0":rate, closing)));
                }
            }
            return stockModels;

        } catch (ParserConfigurationException | SAXException | IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<StockModel> stockModels) {
          if(stockModels != null){
              onParseCompleted.OnParsed(stockModels);
          }else{
              onParseCompleted.OnParseFailed("Error Occurred while parsing data");
          }

    }
}
