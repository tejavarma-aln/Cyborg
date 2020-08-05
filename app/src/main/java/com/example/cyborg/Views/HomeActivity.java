package com.example.cyborg.Views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cyborg.Adaptors.DashBoardAdapter;
import com.example.cyborg.Models.DashBoardCardModel;
import com.example.cyborg.R;
import com.example.cyborg.Interface.OnDashBoardClick;
import com.example.cyborg.Utils.Constants;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity implements OnDashBoardClick {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Constants.COMPANY_NAME);
        setSupportActionBar(toolbar);
        InitLayout();
    }

    private void InitLayout() {

        ArrayList<DashBoardCardModel>  dashBoardCardModels = new ArrayList<>();

        dashBoardCardModels.add(new DashBoardCardModel("Ledger Report",R.drawable.ic_baseline_account_box_24));
        dashBoardCardModels.add(new DashBoardCardModel("Stock Summary",R.drawable.ic_baseline_shopping_cart_24));
        dashBoardCardModels.add(new DashBoardCardModel("Balance Sheet",R.drawable.ic_baseline_show_chart_24));
        dashBoardCardModels.add(new DashBoardCardModel("Profit and Loss",R.drawable.ic_baseline_bar_chart_24));
        dashBoardCardModels.add(new DashBoardCardModel("DayBook",R.drawable.ic_baseline_menu_book_24));
        dashBoardCardModels.add(new DashBoardCardModel("Cash and Bank",R.drawable.ic_baseline_account_balance_24));
        dashBoardCardModels.add(new DashBoardCardModel("Sales Register",R.drawable.ic_twotone_add_shopping_cart_24));
        dashBoardCardModels.add(new DashBoardCardModel("Purchase Register",R.drawable.ic_baseline_shop_24));
        dashBoardCardModels.add(new DashBoardCardModel("Payments",R.drawable.ic_baseline_monetization_on_24));
        dashBoardCardModels.add(new DashBoardCardModel("Receipts",R.drawable.ic_baseline_attach_money_24));
        dashBoardCardModels.add(new DashBoardCardModel("Payables",R.drawable.ic_baseline_payment_24));
        dashBoardCardModels.add(new DashBoardCardModel("Receivables",R.drawable.ic_baseline_receipt_24));

        GridView gridView = findViewById(R.id.homeGrid);
        gridView.setAdapter(new DashBoardAdapter(dashBoardCardModels,this));

    }

    @Override
    public void OnClick(int id) {
        switch (id){
            case 0:
                startActivity(new Intent(this,LedgerActivity.class));
                break;
            case 1:
                startActivity(new Intent(this,StockSummary.class));
                break;
            case 2:
                Intent intentBalanceSheet = new Intent(this,HtmlReports.class);
                intentBalanceSheet.putExtra("Report Name",Constants.BALANCE_SHEET);
                startActivity(intentBalanceSheet);
                break;
            case 3:
                Intent intentProfitLoss = new Intent(this,HtmlReports.class);
                intentProfitLoss.putExtra("Report Name",Constants.PROFIT_LOSS);
                startActivity(intentProfitLoss);
                break;
            case 4:
                Intent intentDayBook = new Intent(this,Vouchers.class);
                intentDayBook.putExtra("Report Name",Constants.DAY_BOOK);
                intentDayBook.putExtra("Type","Report");
                startActivity(intentDayBook);
                break;
            case 5:
                startActivity(new Intent(this, CashBank.class));
                break;
            case 6:
                Intent intentSales = new Intent(this,Vouchers.class);
                intentSales.putExtra("Report Name",Constants.SALES);
                intentSales.putExtra("Type","Report");
                startActivity(intentSales);
                break;
            case 7:
                Intent intentPurchase = new Intent(this,Vouchers.class);
                intentPurchase.putExtra("Report Name",Constants.PURCHASE);
                intentPurchase.putExtra("Type","Report");
                startActivity(intentPurchase);
                break;
            case 8:
                Intent intentPayments = new Intent(this,Vouchers.class);
                intentPayments.putExtra("Report Name",Constants.PAYMENT);
                intentPayments.putExtra("Type","Report");
                startActivity(intentPayments);
                break;
            case 9:
                Intent intentReceipt = new Intent(this,Vouchers.class);
                intentReceipt.putExtra("Report Name",Constants.RECEIPT);
                intentReceipt.putExtra("Type","Report");
                startActivity(intentReceipt);
                break;
            case 10:
                Intent intentPayable = new Intent(this,OutStanding.class);
                intentPayable.putExtra("Report Name",Constants.PAYABLE);
                startActivity(intentPayable);
                break;
            case 11:
                Intent intentReceivable = new Intent(this,OutStanding.class);
                intentReceivable.putExtra("Report Name",Constants.RECEIVABLE);
                startActivity(intentReceivable);
                break;
            default:
                break;
        }

    }


}
