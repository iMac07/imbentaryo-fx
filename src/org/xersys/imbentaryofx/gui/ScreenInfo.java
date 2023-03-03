package org.xersys.imbentaryofx.gui;

import org.json.simple.JSONObject;

public class ScreenInfo {
    public static final String RESOURCE_URL = "";
    public static final String CONTROLLER_URL = "org.xersys.imbentaryofx.gui.";
    
    public enum NAME{
        AP_CLIENT,
        AR_CLIENT,
        JOB_ORDER,
        JO_DETAIL_UPDATE,
        JOB_ESTIMATE,
        PARTS_INQUIRY,
        PARTS_CATALOGUE,
        POS,
        POS_HISTORY,
        CUSTOMER_ORDER,
        CUSTOMER_ORDER_DETAIL,
        CUSTOMER_ORDER_HISTORY,
        CUSTOMER_ORDER_ISSUANCE,
        CUSTOMER_ORDER_ISSUANCE_DETAIL,
        DASHBOARD,
        CART, 
        QUICK_SEARCH,
        QUICK_SEARCH_FILTER,
        PURCHASE_ORDER,
        PURCHASE_ORDER_HISTORY,
        PO_RECEIVING,
        PO_RECEIVING_HISTORY,
        PO_RETURN,
        CLIENT_MASTER,
        CLIENT_MOBILE,
        CLIENT_EMAIL,
        CLIENT_ADDRESS,
        CLIENT_MASTER_HISTORY,
        CASHIERING,
        PAYMENT,
        PAYMENT_NO_INVOICE,
        PAYMENT_CHARGE,
        PAYMENT_JO,
        SP_SALES,
        SP_SALES_HISTORY,
        SP_INV_MASTER,
        SP_INV_HISTORY,
        POS_DETAIL_UPDATE,
        PO_DETAIL_UPDATE,
        PO_RECEIVING_DETAIL_UPDATE,
        PO_RETURN_DETAIL_UPDATE,
        PO_RETURN_HISTORY,
        JOB_ESTIMATE_DETAIL_UPDATE,
        JOB_ESTIMATE_HISTORY,
        JOB_ORDER_HISTORY,
        AP_PAYMENT,
        AR_PAYMENT,
        REPORTS,
        REPORT_CRITERIA,
        BACKGROUND,
        LOGIN,
        SP_WHOLESALE,
        SP_WHOLESALE_HISTORY,
        MC_SERIAL,
        INV_REQUEST,
        INV_REQUEST_DETAIL,
        INV_REQUEST_HISTORY,
        INV_TRANSFER,
        INV_TRANSFER_DETAIL,
        INV_TRANSFER_HISTORY,
        INV_ADJUSTMENT,
        INV_ADJUSTMENT_DETAIL,
        INV_ADJUSTMENT_HISTORY,
        USER_MANAGER,
        SI_HISTORY,
        OR_HISTORY,
        SP_WHOLESALE_ORDER,
        SP_WHOLESALE_ORDER_DETAIL,
        SP_WHOLESALE_ORDER_HISTORY,
        SP_WHOLESALE_ORDER_ISSUANCE,
        SP_WHOLESALE_ORDER_ISSUANCE_DETAIL,
        LABOR,
        INV_PRICE_CHANGE,
        INV_PRICE_CHANGE_DETAIL,
        INV_PRICE_CHANGE_HISTORY,
        PARAMETERS,
        BRAND,
        MODEL,
        COLOR,
        BARANGAY,
        TOWN,
        PROVINCE,
        SIZES,
        MEASURE,
        MAINTENANCE,
        ABC_CLASSIFY,
        SERVICE_HISTORY
    }
    
    public static JSONObject get(NAME foModule){
        JSONObject loJSON = new JSONObject();
        
        switch (foModule){
            case SERVICE_HISTORY:
                loJSON.put("resource", RESOURCE_URL + "ServiceHistory.fxml");
                loJSON.put("controller", CONTROLLER_URL + "ServiceHistoryController");
                return loJSON;
            case ABC_CLASSIFY:
                loJSON.put("resource", RESOURCE_URL + "ABCClassify.fxml");
                loJSON.put("controller", CONTROLLER_URL + "ABCClassifyController");
                return loJSON;
            case INV_PRICE_CHANGE_HISTORY:
                loJSON.put("resource", RESOURCE_URL + "InvPriceChangeHistory.fxml");
                loJSON.put("controller", CONTROLLER_URL + "InvPriceChangeHistoryController");
                return loJSON;
            case INV_PRICE_CHANGE_DETAIL:
                loJSON.put("resource", RESOURCE_URL + "InvPriceChangeDetail.fxml");
                loJSON.put("controller", CONTROLLER_URL + "InvPriceChangeDetailController");
                return loJSON;
            case INV_PRICE_CHANGE:
                loJSON.put("resource", RESOURCE_URL + "InvPriceChange.fxml");
                loJSON.put("controller", CONTROLLER_URL + "InvPriceChangeController");
                return loJSON;
            case LABOR:
                loJSON.put("resource", RESOURCE_URL + "Labor.fxml");
                loJSON.put("controller", CONTROLLER_URL + "LaborController");
                return loJSON;
            case SP_WHOLESALE_ORDER_ISSUANCE:
                loJSON.put("resource", RESOURCE_URL + "SPWSOrderIssuance.fxml");
                loJSON.put("controller", CONTROLLER_URL + "SPWSOrderIssuanceController");
                return loJSON;
            case SP_WHOLESALE_ORDER_ISSUANCE_DETAIL:
                loJSON.put("resource", RESOURCE_URL + "SPWSOrderIssuanceDetail.fxml");
                loJSON.put("controller", CONTROLLER_URL + "SPWSOrderIssuanceDetailController");
                return loJSON;
            case SP_WHOLESALE_ORDER:
                loJSON.put("resource", RESOURCE_URL + "SPWSOrder.fxml");
                loJSON.put("controller", CONTROLLER_URL + "SPWSOrderController");
                return loJSON;
            case SP_WHOLESALE_ORDER_DETAIL:
                loJSON.put("resource", RESOURCE_URL + "SPWSOrderDetail.fxml");
                loJSON.put("controller", CONTROLLER_URL + "SPWSOrderDetailController");
                return loJSON;
            case SP_WHOLESALE_ORDER_HISTORY:
                loJSON.put("resource", RESOURCE_URL + "SPWSOrderHistory.fxml");
                loJSON.put("controller", CONTROLLER_URL + "SPWSOrderHistoryController");
                return loJSON;
            case SI_HISTORY:
                loJSON.put("resource", RESOURCE_URL + "SIHistory.fxml");
                loJSON.put("controller", CONTROLLER_URL + "SIHistoryController");
                return loJSON;
            case OR_HISTORY:
                loJSON.put("resource", RESOURCE_URL + "ORHistory.fxml");
                loJSON.put("controller", CONTROLLER_URL + "ORHistoryController");
                return loJSON;
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
                loJSON.put("resource", RESOURCE_URL + "SPCustomerOrder.fxml");
                loJSON.put("controller", CONTROLLER_URL + "SPCustomerOrderController");
                return loJSON;
            case CUSTOMER_ORDER_HISTORY:
                loJSON.put("resource", RESOURCE_URL + "SPCustomerOrderHistory.fxml");
                loJSON.put("controller", CONTROLLER_URL + "SPCustomerOrderHistoryController");
                return loJSON;
            case CUSTOMER_ORDER_ISSUANCE:
                loJSON.put("resource", RESOURCE_URL + "SPCustomerOrderIssuance.fxml");
                loJSON.put("controller", CONTROLLER_URL + "SPCustomerOrderIssuanceController");
                return loJSON;
            case CUSTOMER_ORDER_ISSUANCE_DETAIL:
                loJSON.put("resource", RESOURCE_URL + "SPCustomerOrderIssuanceDetail.fxml");
                loJSON.put("controller", CONTROLLER_URL + "SPCustomerOrderIssuanceDetailController");
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
            case JO_DETAIL_UPDATE:
                loJSON.put("resource", RESOURCE_URL + "JODetail.fxml");
                loJSON.put("controller", CONTROLLER_URL + "JODetailController");
                return loJSON;
            case PURCHASE_ORDER:
                loJSON.put("resource", RESOURCE_URL + "PurchaseOrder.fxml");
                loJSON.put("controller", CONTROLLER_URL + "PurchaseOrderController");
                return loJSON;
            case PURCHASE_ORDER_HISTORY:
                loJSON.put("resource", RESOURCE_URL + "PurchaseOrderHistory.fxml");
                loJSON.put("controller", CONTROLLER_URL + "PurchaseOrderHistoryController");
                return loJSON;
            case PO_RECEIVING:
                loJSON.put("resource", RESOURCE_URL + "POReceiving.fxml");
                loJSON.put("controller", CONTROLLER_URL + "POReceivingController");
                return loJSON;
            case PO_RECEIVING_HISTORY:
                loJSON.put("resource", RESOURCE_URL + "POReceivingHistory.fxml");
                loJSON.put("controller", CONTROLLER_URL + "POReceivingHistoryController");
                return loJSON;
            case PO_RETURN:
                loJSON.put("resource", RESOURCE_URL + "POReturn.fxml");
                loJSON.put("controller", CONTROLLER_URL + "POReturnController");
                return loJSON;
            case CLIENT_MASTER_HISTORY:
                loJSON.put("resource", RESOURCE_URL + "ClientMasterHistory.fxml");
                loJSON.put("controller", CONTROLLER_URL + "ClientMasterHistoryController");
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
            case CLIENT_ADDRESS:
                loJSON.put("resource", RESOURCE_URL + "ClientAddress.fxml");
                loJSON.put("controller", CONTROLLER_URL + "ClientAddressController");
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
            case PAYMENT_CHARGE:
                loJSON.put("resource", RESOURCE_URL + "PaymentCharge.fxml");
                loJSON.put("controller", CONTROLLER_URL + "PaymentChargeController");
                return loJSON;
            case PAYMENT_JO:
                loJSON.put("resource", RESOURCE_URL + "PaymentJO.fxml");
                loJSON.put("controller", CONTROLLER_URL + "PaymentJOController");
                return loJSON;
            case SP_SALES:
                loJSON.put("resource", RESOURCE_URL + "SPSales.fxml");
                loJSON.put("controller", CONTROLLER_URL + "SPSalesController");
                return loJSON;
            case SP_SALES_HISTORY:
                loJSON.put("resource", RESOURCE_URL + "SPSalesHistory.fxml");
                loJSON.put("controller", CONTROLLER_URL + "SPSalesHistoryController");
                return loJSON;
            case SP_INV_MASTER:
                loJSON.put("resource", RESOURCE_URL + "SPMaster.fxml");
                loJSON.put("controller", CONTROLLER_URL + "SPMasterController");
                return loJSON;
            case SP_INV_HISTORY:
                loJSON.put("resource", RESOURCE_URL + "SPHistory.fxml");
                loJSON.put("controller", CONTROLLER_URL + "SPHistoryController");
                return loJSON;
            case PO_DETAIL_UPDATE:
                loJSON.put("resource", RESOURCE_URL + "PODetail.fxml");
                loJSON.put("controller", CONTROLLER_URL + "PODetailController");
                return loJSON;
            case PO_RECEIVING_DETAIL_UPDATE:
                loJSON.put("resource", RESOURCE_URL + "POReceivingDetail.fxml");
                loJSON.put("controller", CONTROLLER_URL + "POReceivingDetailController");
                return loJSON;
            case PO_RETURN_DETAIL_UPDATE:
                loJSON.put("resource", RESOURCE_URL + "POReturnDetail.fxml");
                loJSON.put("controller", CONTROLLER_URL + "POReturnDetailController");
                return loJSON;
            case AP_CLIENT:
                loJSON.put("resource", RESOURCE_URL + "APClient.fxml");
                loJSON.put("controller", CONTROLLER_URL + "APClientController");
                return loJSON;
            case AR_CLIENT:
                loJSON.put("resource", RESOURCE_URL + "ARClient.fxml");
                loJSON.put("controller", CONTROLLER_URL + "ARClientController");
                return loJSON;
            case PO_RETURN_HISTORY:
                loJSON.put("resource", RESOURCE_URL + "POReturnHistory.fxml");
                loJSON.put("controller", CONTROLLER_URL + "POReturnHistoryController");
                return loJSON;    
            case JOB_ESTIMATE_DETAIL_UPDATE:
                loJSON.put("resource", RESOURCE_URL + "JOEstimateDetail.fxml");
                loJSON.put("controller", CONTROLLER_URL + "JOEstimateDetailController");
                return loJSON;    
            case JOB_ESTIMATE_HISTORY:
                loJSON.put("resource", RESOURCE_URL + "JobEstimateHistory.fxml");
                loJSON.put("controller", CONTROLLER_URL + "JobEstimateHistoryController");
                return loJSON;    
            case JOB_ORDER_HISTORY:
                loJSON.put("resource", RESOURCE_URL + "JobOrderHistory.fxml");
                loJSON.put("controller", CONTROLLER_URL + "JobOrderHistoryController");
                return loJSON;    
            case AP_PAYMENT:
                loJSON.put("resource", RESOURCE_URL + "APPayment.fxml");
                loJSON.put("controller", CONTROLLER_URL + "APPaymentController");
                return loJSON;    
            case AR_PAYMENT:
                loJSON.put("resource", RESOURCE_URL + "ARPayment.fxml");
                loJSON.put("controller", CONTROLLER_URL + "ARPaymentController");
                return loJSON;    
            case REPORTS:
                loJSON.put("resource", RESOURCE_URL + "Reports.fxml");
                loJSON.put("controller", CONTROLLER_URL + "ReportsController");
                return loJSON;    
            case REPORT_CRITERIA:
                loJSON.put("resource", RESOURCE_URL + "ReportCriteria.fxml");
                loJSON.put("controller", CONTROLLER_URL + "ReportCriteriaController");
                return loJSON;    
            case BACKGROUND:
                loJSON.put("resource", RESOURCE_URL + "Background.fxml");
                loJSON.put("controller", CONTROLLER_URL + "BackgroundController");
                return loJSON;    
            case LOGIN:
                loJSON.put("resource", RESOURCE_URL + "Login.fxml");
                loJSON.put("controller", CONTROLLER_URL + "LoginController");
                return loJSON;        
            case SP_WHOLESALE:
                loJSON.put("resource", RESOURCE_URL + "SPWholeSale.fxml");
                loJSON.put("controller", CONTROLLER_URL + "SPWholeSaleController");
                return loJSON;
            case SP_WHOLESALE_HISTORY:
                loJSON.put("resource", RESOURCE_URL + "SPWholeSaleHistory.fxml");
                loJSON.put("controller", CONTROLLER_URL + "SPWholeSaleHistoryController");
                return loJSON;
            case MC_SERIAL:
                loJSON.put("resource", RESOURCE_URL + "MCSerial.fxml");
                loJSON.put("controller", CONTROLLER_URL + "MCSerialController");
                return loJSON;
            case INV_REQUEST:
                loJSON.put("resource", RESOURCE_URL + "InvRequest.fxml");
                loJSON.put("controller", CONTROLLER_URL + "InvRequestController");
                return loJSON;
            case INV_REQUEST_HISTORY:
                loJSON.put("resource", RESOURCE_URL + "InvRequestHistory.fxml");
                loJSON.put("controller", CONTROLLER_URL + "InvRequestHistoryController");
                return loJSON;
            case INV_TRANSFER:
                loJSON.put("resource", RESOURCE_URL + "InvTransfer.fxml");
                loJSON.put("controller", CONTROLLER_URL + "InvTransferController");
                return loJSON;
            case INV_TRANSFER_HISTORY:
                loJSON.put("resource", RESOURCE_URL + "InvTransferHistory.fxml");
                loJSON.put("controller", CONTROLLER_URL + "InvTransferHistoryController");
                return loJSON;
            case INV_ADJUSTMENT:
                loJSON.put("resource", RESOURCE_URL + "InvAdjustment.fxml");
                loJSON.put("controller", CONTROLLER_URL + "InvAdjustmentController");
                return loJSON;
            case INV_ADJUSTMENT_HISTORY:
                loJSON.put("resource", RESOURCE_URL + "InvAdjustmentHistory.fxml");
                loJSON.put("controller", CONTROLLER_URL + "InvAdjustmentHistoryController");
                return loJSON;
            case INV_ADJUSTMENT_DETAIL:
                loJSON.put("resource", RESOURCE_URL + "InvAdjustmentDetail.fxml");
                loJSON.put("controller", CONTROLLER_URL + "InvAdjustmentDetailController");
                return loJSON;
            case INV_REQUEST_DETAIL:
                loJSON.put("resource", RESOURCE_URL + "InvRequestDetail.fxml");
                loJSON.put("controller", CONTROLLER_URL + "InvRequestDetailController");
                return loJSON;
            case INV_TRANSFER_DETAIL:
                loJSON.put("resource", RESOURCE_URL + "InvTransferDetail.fxml");
                loJSON.put("controller", CONTROLLER_URL + "InvTransferDetailController");
                return loJSON;
            case USER_MANAGER:
                loJSON.put("resource", RESOURCE_URL + "UserManager.fxml");
                loJSON.put("controller", CONTROLLER_URL + "UserManagerController");
                return loJSON;
            case CUSTOMER_ORDER_DETAIL:
                loJSON.put("resource", RESOURCE_URL + "SPCustomerOrderDetail.fxml");
                loJSON.put("controller", CONTROLLER_URL + "SPCustomerOrderController");
                return loJSON;
            case PARAMETERS:
                loJSON.put("resource", RESOURCE_URL + "Parameters.fxml");
                loJSON.put("controller", CONTROLLER_URL + "ParametersController");
                return loJSON;
            case BRAND:
                loJSON.put("resource", RESOURCE_URL + "Brand.fxml");
                loJSON.put("controller", CONTROLLER_URL + "BrandController");
                return loJSON;
            case MODEL:
                loJSON.put("resource", RESOURCE_URL + "Model.fxml");
                loJSON.put("controller", CONTROLLER_URL + "ModelController");
                return loJSON;
            case COLOR:
                loJSON.put("resource", RESOURCE_URL + "Color.fxml");
                loJSON.put("controller", CONTROLLER_URL + "ColorController");
                return loJSON;
            case MAINTENANCE:
                loJSON.put("resource", RESOURCE_URL + "Maintenance.fxml");
                loJSON.put("controller", CONTROLLER_URL + "MaintenanceController");
                return loJSON;
            case MEASURE:
                loJSON.put("resource", RESOURCE_URL + "Measure.fxml");
                loJSON.put("controller", CONTROLLER_URL + "MeasureController");
                return loJSON;
            case SIZES:
                loJSON.put("resource", RESOURCE_URL + "Sizes.fxml");
                loJSON.put("controller", CONTROLLER_URL + "SizesController");
                return loJSON;
            case BARANGAY:
                loJSON.put("resource", RESOURCE_URL + "Barangay.fxml");
                loJSON.put("controller", CONTROLLER_URL + "BarangayController");
                return loJSON;
            case TOWN:
                loJSON.put("resource", RESOURCE_URL + "Town.fxml");
                loJSON.put("controller", CONTROLLER_URL + "TownController");
                return loJSON;
            case PROVINCE:
                loJSON.put("resource", RESOURCE_URL + "Province.fxml");
                loJSON.put("controller", CONTROLLER_URL + "ProvinceController");
                return loJSON;
        }
        
        return null;
    }
    
}
