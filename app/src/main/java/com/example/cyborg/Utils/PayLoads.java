package com.example.cyborg.Utils;

public class PayLoads {


    private String getDefaultHeader(String id){
        return String.format("<ENVELOPE><HEADER><VERSION>1</VERSION><TALLYREQUEST>Export</TALLYREQUEST><TYPE>Data</TYPE><ID>%s</ID></HEADER><BODY>",id);
    }

    private String getStaticVar(String fromDate,String toDate){
        return String.format("  <STATICVARIABLES><SVEXPORTFORMAT>$$SysName:XML</SVEXPORTFORMAT><SVFROMDATE Type='DATE'>%s</SVFROMDATE><SVTODATE Type='DATE'>%s</SVTODATE></STATICVARIABLES>",fromDate,toDate);
    }


   public String getLedPayload(){
        String packet = getDefaultHeader("HostAllLedgers");
        packet += "<DESC><STATICVARIABLES><SVEXPORTFORMAT>$$SysName:XML</SVEXPORTFORMAT></STATICVARIABLES><TDL><TDLMESSAGE><REPORT Name='HostAllLedgers'><FORM>HostAllLedgers</FORM></REPORT>";
        packet += "<FORM Name='HostAllLedgers'><PART>HostAllLedgers</PART></FORM><PART Name='HostAllLedgers'><LINE>HostAllLedgers</LINE><REPEAT>HostAllLedgers:ListOfLedgers</REPEAT>";
        packet += "<SCROLLED>BOTH</SCROLLED></PART><LINE Name='HostAllLedgers'><FIELDS>CledName,CledClosingBalance</FIELDS><XMLTAG>LEDGER</XMLTAG></LINE><FIELD Name='CledName'><SETAS>$NAME</SETAS>";
        packet += "</FIELD><FIELD Name='CledClosingBalance'><SETAS>$ClosingBalance</SETAS><USE>Amount Field</USE></FIELD></TDLMESSAGE></TDL></DESC></BODY></ENVELOPE>";
        return packet;
    }

    public String getHtmlPayload(String reportName,String fromDate,String toDate){

       String packet = getDefaultHeader(reportName);
       packet += "<DESC><STATICVARIABLES><SVBROWSERWIDTH>1024</SVBROWSERWIDTH><SVBROWSERHEIGHT>768</SVBROWSERHEIGHT>";
       packet += String.format("<SVFROMDATE Type='DATE'>%s</SVFROMDATE><SVTODATE Type='DATE'>%s</SVTODATE>",fromDate,toDate);
       packet += "<SVEXPORTFORMAT>$$SysName:HTML</SVEXPORTFORMAT></STATICVARIABLES></DESC></BODY></ENVELOPE>";
       return packet;

   }

   public String getLedVchPayLoad(String ledName,String fromDate,String toDate){
       String packet = getDefaultHeader("LedgerVouchers");
       packet += "<DESC><STATICVARIABLES><SVEXPORTFORMAT>$$SysName:XML</SVEXPORTFORMAT>";
       packet += String.format("<LEDGERNAME>%s</LEDGERNAME><SVFROMDATE Type='Date'>%s</SVFROMDATE><SVTODATE Type='Date'>%s</SVTODATE>",ledName,fromDate,toDate);
       packet += "</STATICVARIABLES><TDL><TDLMESSAGE><LINE Name='DSPVchDetail' ISMODIFY='YES'><XMLTAG>LEDINFO</XMLTAG>";
       packet += "</LINE></TDLMESSAGE></TDL></DESC></BODY></ENVELOPE>";
       return packet;
   }


   public String getOutstandingPayLoad(String reportName,String fromDate,String toDate){
       String packet = getDefaultHeader(reportName);
       packet += "<DESC>";
       packet += getStaticVar(fromDate,toDate);
       packet += "<TDL><TDLMESSAGE><LINE Name='BILL Detail' ISMODIFY='YES'><XMLTAG>DATA</XMLTAG></LINE></TDLMESSAGE></TDL></DESC></BODY></ENVELOPE>";
       return packet;


   }

   public String getStockPayLoad(String fromDate,String toDate){
        String packet = getDefaultHeader("StockSummary");
        packet += "<DESC><STATICVARIABLES><SVEXPORTFORMAT>$$SysName:XML</SVEXPORTFORMAT><ISITEMWISE>Yes</ISITEMWISE>";
        packet += String.format("<SVFROMDATE Type='DATE'>%s</SVFROMDATE><SVTODATE Type='DATE'>%s</SVTODATE>",fromDate,toDate);
        packet += "<DSPSHOWALLITEMS>Yes</DSPSHOWALLITEMS></STATICVARIABLES><TDL><TDLMESSAGE><LINE Name='DSPACCLINE' ISMODIFY='YES'>";
        packet += "<XMLTAG>STOCK</XMLTAG></LINE></TDLMESSAGE></TDL></DESC></BODY></ENVELOPE>";

        return packet;
   }

   public String getCompanyPayLoad(){
        String packet = getDefaultHeader("HostLicenseInfo");
        packet += "<DESC><STATICVARIABLES><SVEXPORTFORMAT>$$SysName:XML</SVEXPORTFORMAT></STATICVARIABLES><TDL><TDLMESSAGE>";
        packet += "<REPORT Name='HostLicenseInfo'><FORM>HostLicenseInfo</FORM></REPORT><FORM Name='HostLicenseInfo'><PART>HostLicenseInfo</PART></FORM>";
        packet += "<PART Name='HostLicenseInfo'><SCROLLED>Vertical</SCROLLED><LINE>HostLicenseInfo</LINE></PART><LINE Name='HostLicenseInfo'><FIELDS>NameField,SimpleField</FIELDS>";
        packet += "<LOCAL>Field:NameField:Setas:$$LicenseInfo:SerialNumber</LOCAL><LOCAL>Field:SimpleField:Setas:##SvCurrentCompany</LOCAL></LINE>";
        packet += "</TDLMESSAGE></TDL></DESC></BODY></ENVELOPE>";

        return packet;
   }

   public String getBankCashPayLoad(String fromDate,String toDate){
       String packet = getDefaultHeader("Bank Group Summary");
       packet += "<DESC>";
       packet += getStaticVar(fromDate,toDate);
       packet += "<TDL><TDLMESSAGE><LINE Name='DSPACCLINE' ISMODIFY='YES'><XMLTAG>DATA</XMLTAG></LINE> <FIELD Name='DSPACCNAME' ISMODIFY='YES'>";
       packet += "<ADD>FIELD:ATEND:LEDBOOLEAN</ADD></FIELD><FIELD Name='LEDBOOLEAN'><SETAS>$$ISLedger:#DSPACCNAME</SETAS>";
       packet += "</FIELD></TDLMESSAGE></TDL></DESC></BODY></ENVELOPE>";
        return packet;
   }

   public String getVouchers(String fromDate,String toDate,String filter){
        String packet = getDefaultHeader("HostDayBook");
        packet += "<DESC>";
        packet += getStaticVar(fromDate,toDate);
        packet += "<TDL><TDLMESSAGE><REPORT Name='HostDayBook'><FORM>HostDayBook</FORM></REPORT><FORM Name='HostDayBook'><PART>HostDayBook</PART></FORM><PART Name='HostDayBook'><SCROLLED>Vertical</SCROLLED>";
        packet += "<LINE>HostDayBookBody</LINE><REPEAT>HostDayBookBody:HostDayCollection</REPEAT></PART><LINE Name='HostDayBookBody'><XMLTAG>Data</XMLTAG><FIELD>HostVchDate,HostVchNo,HostVchType,HostVchAmt,HostVchLed</FIELD>";
        packet += "</LINE><FIELD Name='HostVchDate'><SETAS>$Date</SETAS></FIELD><FIELD Name='HostVchNo'><SETAS>$VoucherNumber</SETAS></FIELD><FIELD Name='HostVchType'>";
        packet += "<SETAS>$VoucherTypeName</SETAS></FIELD><FIELD Name='HostVchAmt'><USE>AmountField</USE><SETAS>$Amount</SETAS></FIELD><FIELD Name='HostVchLed'><SETAS>$BaseLedgerName</SETAS>";
        packet += "</FIELD><COLLECTION Name='HostDayCollection'><TYPE>Voucher</TYPE><BELONSTO>Yes</BELONSTO><FETCH>Date,VoucherNumber,Amount,VoucherTypeName,LedgerEntries.*</FETCH><COMPUTE>BaseLedgerName:$LedgerEntries[0].LedgerName</COMPUTE>";
        packet += getVoucherFilter(filter);

        return packet;

   }
  private String getVoucherFilter(String filter){
        String baseFormula = "<FILTER>OnlyReqBills</FILTER></COLLECTION><SYSTEM Type='Formulae' Name='OnlyReqBills'>%s</SYSTEM></TDLMESSAGE></TDL></DESC></BODY></ENVELOPE>" ;
        switch (filter){
            case Constants.SALES:
              return String.format(baseFormula,"$$IsSales:$VoucherTypeName");
            case Constants.PURCHASE:
                return String.format(baseFormula,"$$IsPurchase:$VoucherTypeName");
            case Constants.PAYMENT:
                return String.format(baseFormula,"$$IsPayment:$VoucherTypeName");
            case Constants.RECEIPT:
                return String.format(baseFormula,"$$IsReceipt:$VoucherTypeName");
            default:
                return "</COLLECTION></TDLMESSAGE></TDL></DESC></BODY></ENVELOPE>";

        }
  }



}

