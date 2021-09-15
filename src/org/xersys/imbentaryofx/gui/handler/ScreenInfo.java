package org.xersys.imbentaryofx.gui.handler;

import org.json.simple.JSONObject;

public class ScreenInfo {
    public static final String RESOURCE_URL = "../";
    public static final String CONTROLLER_URL = "org.xersys.imbentaryofx.gui.";
    
    public enum NAME{
        JOB_ORDER,
        JOB_ESTIMATE,
        PARTS_INQUIRY,
        PARTS_CATALOGUE,
        POS,
        POS_HISTORY,
        CUSTOMER_ORDER,
        DASHBOARD,
        CART, 
        QUICK_SEARCH,
        QUICK_SEARCH_FILTER,
        POS_DETAIL_UPDATE,
        PURCHASE_ORDER,
        PO_RECEIVING,
        PO_RETURN,
        CLIENT_MASTER,
        CLIENT_MOBILE,
        CLIENT_EMAIL,
        CASHIERING,
        PAYMENT,
        PAYMENT_NO_INVOICE,
        SP_SALES,
        SP_SALES_HISTORY
    }
    
    public static JSONObject get(NAME foModule){
        JSONObject loJSON = new JSONObject();
        
        switch (foModule){
            case JOB_ORDER:
                loJSON.put("resource", RESOURCE_URL + "JobOrder.fxml");
                loJSON.put("controller", CONTROLLER_URL + "JobOrderController");
                return loJSON;
            case JOB_ESTIMATE:
                loJSON.put("resource", RESOURCE_URL + "JobEstimate.fxml");
                loJSON.put("controller", CONTROLLER_URL + "JobEstimateController");
                return loJSON;
            case PARTS_INQUIRY:
                loJSON.put("resource", RESOURCE_URL + "PartsInquiry.fxml");
                loJSON.put("controller", CONTROLLER_URL + "PartsInquiryController");
                return loJSON;
            case PARTS_CATALOGUE:
                loJSON.put("resource", RESOURCE_URL + "PartsCatalogue.fxml");
                loJSON.put("controller", CONTROLLER_URL + "PartsCatalogueController");
                return loJSON;
            case POS:
                loJSON.put("resource", RESOURCE_URL + "POS.fxml");
                loJSON.put("controller", CONTROLLER_URL + "POSController");
                return loJSON;
            case CUSTOMER_ORDER:
                loJSON.put("resource", RESOURCE_URL + "CustomerOrder.fxml");
                loJSON.put("controller", CONTROLLER_URL + "CustomerOrderController");
                return loJSON;
            case DASHBOARD:
                loJSON.put("resource", RESOURCE_URL + "Dashboard.fxml");
                loJSON.put("controller", CONTROLLER_URL + "DashboardController");
                return loJSON;
            case CART:
                loJSON.put("resource", RESOURCE_URL + "Cart2.fxml");
                loJSON.put("controller", CONTROLLER_URL + "Cart2Controller");
                return loJSON;
            case QUICK_SEARCH:
                loJSON.put("resource", RESOURCE_URL + "QuickSearchNeo.fxml");
                loJSON.put("controller", CONTROLLER_URL + "QuickSearchNeoController");
                return loJSON;
            case QUICK_SEARCH_FILTER:
                loJSON.put("resource", RESOURCE_URL + "QuickSearchFilter.fxml");
                loJSON.put("controller", CONTROLLER_URL + "QuickSearchFilterController");
                return loJSON;
            case POS_DETAIL_UPDATE:
                loJSON.put("resource", RESOURCE_URL + "POSDetail.fxml");
                loJSON.put("controller", CONTROLLER_URL + "POSDetailController");
                return loJSON;
            case PURCHASE_ORDER:
                loJSON.put("resource", RESOURCE_URL + "PurchaseOrder.fxml");
                loJSON.put("controller", CONTROLLER_URL + "PurchaseOrderController");
                return loJSON;
            case PO_RECEIVING:
                loJSON.put("resource", RESOURCE_URL + "POReceiving.fxml");
                loJSON.put("controller", CONTROLLER_URL + "POReceivingController");
                return loJSON;
            case PO_RETURN:
                loJSON.put("resource", RESOURCE_URL + "POReturn.fxml");
                loJSON.put("controller", CONTROLLER_URL + "POReturnController");
                return loJSON;
            case CLIENT_MASTER:
                loJSON.put("resource", RESOURCE_URL + "ClientMaster.fxml");
                loJSON.put("controller", CONTROLLER_URL + "ClientMasterController");
                return loJSON;
            case CLIENT_MOBILE:
                loJSON.put("resource", RESOURCE_URL + "ClientMobile.fxml");
                loJSON.put("controller", CONTROLLER_URL + "ClientMobileController");
                return loJSON;
            case CLIENT_EMAIL:
                loJSON.put("resource", RESOURCE_URL + "ClientEMail.fxml");
                loJSON.put("controller", CONTROLLER_URL + "ClientEMailController");
                return loJSON;
            case POS_HISTORY:
                loJSON.put("resource", RESOURCE_URL + "POSHistory.fxml");
                loJSON.put("controller", CONTROLLER_URL + "POSHistoryController");
                return loJSON;
            case CASHIERING:
                loJSON.put("resource", RESOURCE_URL + "Cashiering.fxml");
                loJSON.put("controller", CONTROLLER_URL + "CashieringController");
                return loJSON;
            case PAYMENT:
                loJSON.put("resource", RESOURCE_URL + "Payment.fxml");
                loJSON.put("controller", CONTROLLER_URL + "PaymentController");
                return loJSON;
            case PAYMENT_NO_INVOICE:
                loJSON.put("resource", RESOURCE_URL + "PaymentNoInvoice.fxml");
                loJSON.put("controller", CONTROLLER_URL + "PaymentNoInvoiceController");
                return loJSON;
            case SP_SALES:
                loJSON.put("resource", RESOURCE_URL + "SPSales.fxml");
                loJSON.put("controller", CONTROLLER_URL + "SPSalesController");
                return loJSON;
            case SP_SALES_HISTORY:
                loJSON.put("resource", RESOURCE_URL + "SPSalesHistory.fxml");
                loJSON.put("controller", CONTROLLER_URL + "SPSalesHistoryController");
                return loJSON;
        }
        
        return null;
    }
    
}
