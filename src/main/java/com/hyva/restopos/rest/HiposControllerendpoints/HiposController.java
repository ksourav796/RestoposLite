package com.hyva.restopos.rest.HiposControllerendpoints;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyva.restopos.rest.Hiposservice.HiposService;
import com.hyva.restopos.rest.entities.*;
import com.hyva.restopos.rest.entities.Currency;
import com.hyva.restopos.rest.entities.Item;
import com.hyva.restopos.rest.entities.UserAccessRights;
import com.hyva.restopos.rest.entities.UserAccountSetup;
import com.hyva.restopos.rest.pojo.*;
import com.hyva.restopos.rest.repository.*;
import com.hyva.restopos.util.HiposUtil;
import com.hyva.restopos.util.MD5Hashing;
import com.hyva.restopos.util.SmsService;
import com.hyva.restopos.rest.repository.PaymentMethodRepository;
import com.hyva.restopos.rest.repository.RestaurantTempDataRepository;
import com.hyva.restopos.rest.repository.TablesPosRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.hyva.restopos.service.BasePosPrinterService;

import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hipos")
public class HiposController extends HttpServlet {
    @Autowired
    HiposService hiposService;
    @Autowired
    TablesPosRepository tablesPosRepository ;
    @Autowired
    SmsService sendSmsRestaurant;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    FormSetupRepository formSetupRepository;
    @Autowired
    RestaurantTempDataRepository restaurantTempDataRepository;
    @Autowired
    PaymentMethodRepository paymentMethodRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    AccountSetupRepository accountSetupRepository;
    @Autowired
    PusherRepository pusherRepository;
    @Autowired
    RestaurantTokenRecordRepository restaurantTokenRecordRepository;
    @Autowired
    StateRepository stateRepository;
    @Autowired
    CountryRepository countryRepository;
    @Autowired
    CurrencyRepository currencyRepository;
    @Autowired
    AirPayDetailsRepository airpayRepository;
    @Autowired
    InvoicePayRepository invoicePayRepository;

    @RequestMapping(value = "/saveCountry",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity saveCountry(@RequestBody CountryPojo countryPojo){
        return ResponseEntity.status(200).body(hiposService.saveCountry(countryPojo));
    }
    @RequestMapping(value = "/getAllRestaurantNotifications",method = RequestMethod.GET)
    public ResponseEntity getAllRestaurantNotifications(@RequestParam(value = "status",required = false)String status)throws Exception{
//        cartMaster zomato = hiposDAO.getCartMasterByName("Zomato",locationId);
//        cartMaster urbanPiper = hiposDAO.getCartMasterByName("UrbanPiper",locationId);
        List<String> restaurantIds=new ArrayList<>();
//        if(zomato!=null) {
//            restaurantIds.add(zomato.getRestaurantId());
//        }
        Company company =companyRepository.findAllByStatus("Active");
        if(company!=null) {
            restaurantIds.add(company.getConnectNo());
        }
        if(restaurantIds.size()>0) {
            return ResponseEntity.status(200).body(hiposService.getAllRestaurantNotifications(status));
        }
        else {
            return ResponseEntity.status(200).body(null);
        }
    }
    @RequestMapping(value = "/getTablesPosMessages",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity getTablesPosMessages(){
        return ResponseEntity.status(200).body(hiposService.getTablesPosMessages());
    }
    @RequestMapping(value = "/getAllDigiOrders",method = RequestMethod.GET)
    public ResponseEntity getAllDigiOrders(@RequestParam(value = "status",required = false)String status){
        List<String> restaurantIds=new ArrayList<>();
        Company company =companyRepository.findAllByStatus("Active");
        if(company!=null) {
            restaurantIds.add(company.getConnectNo());
        }
        if(restaurantIds.size()>0) {
            return ResponseEntity.status(200).body(hiposService.getAllDigiOrders(status));
        }
        else {
            return ResponseEntity.status(200).body(null);
        }
    }

    @RequestMapping(value = "/getTableReservationList",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity getTableReservationList() throws Exception{
        return ResponseEntity.status(200).body(hiposService.getTableReservationList());
    }
//    @RequestMapping(value = "/getpaginatedOnlineOrders",method = RequestMethod.POST,produces = "application/json")
//    public ResponseEntity getpaginatedOnlineOrders(@RequestBody BasePojo basePojo) throws Exception{
//        return ResponseEntity.status(200).body(hiposService.getpaginatedOnlineOrders(basePojo));
//    }

    @RequestMapping(value = "/getStatusChange",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity getStatusChange(@RequestParam(value = "status")String status,
                                          @RequestParam(value = "item")String item,
                                          @RequestParam(value = "order")String order) throws Exception{
        return ResponseEntity.status(200).body(hiposService.getStatusChange(status,item,order));
    }

    @RequestMapping(value = "/getItemByName",method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity getItemByName(@RequestParam(value = "itemName")String name){
        ItemPojo item = hiposService.getItem(name);
        return ResponseEntity.status(200).body(item);
    }

    @RequestMapping(value = "/getTable",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity getTable(@RequestParam(value = "tableName")String tablename){
        TablesPosPojo tablesPosPojo = hiposService.getTable(tablename);
        return ResponseEntity.status(200).body(tablesPosPojo);
    }
    @RequestMapping(value = "/getTablesListForReserved",method = RequestMethod.POST,produces="application/json")
    public ResponseEntity getTablesListForReserved(@RequestParam(value="id")Long id){
        return ResponseEntity.status(200).body(hiposService.getTablesListForReserved(id));
    }

    @RequestMapping(value = "/saveCurrency",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity saveCurrency(@RequestBody CurrencyPojo currencyPojo){
        return ResponseEntity.status(200).body(hiposService.saveCurrency(currencyPojo));
    }
    @RequestMapping(value = "/saveEmployee",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity saveEmployee(@RequestBody EmployeePojo employeePojo){
        return ResponseEntity.status(200).body(hiposService.saveEmployee(employeePojo));
    }

    @RequestMapping(value = "/savePaymentmethod",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity savePaymentmethod(@RequestBody PaymentMethodDTO paymentMethodDTO){
        return ResponseEntity.status(200).body(hiposService.savePaymentmethod(paymentMethodDTO));
    }
    @RequestMapping(value = "/saveState",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity saveState(@RequestBody StatePojo statePojo){
        return ResponseEntity.status(200).body(hiposService.saveState(statePojo));
    }

    @RequestMapping(value = "/getEmployeeList",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity getEmployeeList(){
        return ResponseEntity.status(200).body(hiposService.getEmployeeList());
    }

    @RequestMapping(value = "/getPaymentMethodList",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity getPaymentMethodList(){
        return ResponseEntity.status(200).body(hiposService.getPaymentMethodList());
    }
    @RequestMapping(value = "/getCustomerListSearch",method = RequestMethod.POST,produces="application/json")
    public ResponseEntity getCustomerListSearch() {
        return ResponseEntity.status(200).body(hiposService.getCustomerDtoObjectOrList());
    }

    @RequestMapping(value = "/getCountryList",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity getCountryList(){
        return ResponseEntity.status(200).body(hiposService.getCountryList());
    }
    @RequestMapping(value = "/getUserAccountSetupList",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity getUserAccountSetupList(){
        return ResponseEntity.status(200).body(hiposService.getUserAccountSetupList());
    }
    @RequestMapping(value = "/getTableConfigList",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity getTableConfigList(){
        return ResponseEntity.status(200).body(hiposService.getTableConfigList());
    }

    @RequestMapping(value = "/getTableNmList",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity getTableNmList(){
        return ResponseEntity.status(200).body(hiposService.getTableNmList());
    }



    @RequestMapping(value = "/getTableListOnConfig",method = RequestMethod.GET,produces="application/json")
    public ResponseEntity getTableListOnConfig(@RequestParam(value="configName")String configName,
                                               @RequestParam(value = "tableSearchText",required = false) String tableSearchText) {
        List<TablesPosPojo> tableList = null;
        tableList = hiposService.getTableListOnConfig(configName,tableSearchText);
        return ResponseEntity.status(200).body(tableList);
    }
    @RequestMapping(value = "/saveSelectedTableName",method = RequestMethod.POST,produces="application/json")
    public ResponseEntity saveSelectedTableName(@RequestParam(value="tableId")Long tableId,
                                                @RequestParam(value = "id") Long id) {
        return ResponseEntity.status(200).body(hiposService.saveSelectedTableName(tableId,id));
    }
    @RequestMapping(value = "/saveTableReservation",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity saveTableReservation(@RequestBody TableReservationPojo tableReservationPojo)  throws Exception{
        return ResponseEntity.status(200).body(hiposService.saveTableReservation(tableReservationPojo));
    }
    @RequestMapping(value = "/getTableList",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity getTableList(@RequestParam(value = "searchText") String searchText,@RequestParam(value = "status") String status){
        return ResponseEntity.status(200).body(hiposService.getTableList(searchText,status));
    }
    @RequestMapping(value = "/saveShift",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity saveShift(@RequestBody ShiftPojo shiftPojo){
        return ResponseEntity.status(200).body(hiposService.saveShift(shiftPojo));
    }
    @RequestMapping(value = "/getShiftList",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity getShiftList(){
        return ResponseEntity.status(200).body(hiposService.getShiftList());
    }

    @RequestMapping(value = "/getStateList",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity getStateList(){
        return ResponseEntity.status(200).body(hiposService.getStateList());
    }
    @RequestMapping(value = "/getCurrencyList",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity getCurrencyList(){
        return ResponseEntity.status(200).body(hiposService.getCurrencyList());
    }
    @RequestMapping(value = "/getItemCategoryList",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity getItemCategoryList(){
        return ResponseEntity.status(200).body(hiposService.getItemCategoryList());
    }
    @RequestMapping(value = "/getItemListOnCategory",method = RequestMethod.GET,produces="application/json")
    public ResponseEntity getItemListOnCategory(@RequestParam(value = "searchText") String searchText,@RequestParam("itemCategoryId") Long itemCategoryId) {
        List<ItemPojo> ItemDTOList = hiposService.retrieveItemListOnCategory(itemCategoryId,searchText);
        return ResponseEntity.status(200).body(ItemDTOList);
    }
    @RequestMapping(value = "/saveNewItemCategory",method = RequestMethod.POST,produces="application/json",consumes = "application/json")
    public ResponseEntity saveNewItemCategory(@RequestBody CategoryPojo categoryPojo) {

        return ResponseEntity.status(200).body(hiposService.saveNewItemCategory(categoryPojo));
    }
    @RequestMapping(value = "/getTempData",method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity getTempData(@RequestParam("currTableName") String currTableName,
                                              @RequestParam("currTableId") String currTableId) {
        return ResponseEntity.status(200).body( hiposService.getTempData(currTableName, currTableId));
    }
    @RequestMapping(value = "/getPageOnloadData",method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity getPageOnLoadData() {
        return ResponseEntity.status(200).body(hiposService.getIntialData(1));
    }

    @RequestMapping(value = "/changeTableNames",method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity changeTableNames(@RequestParam("fromTable") String fromTable,
                                               @RequestParam("toTable") String toTable,@RequestParam("fromTableId") String fromTableId,
                                               @RequestParam("toTableId") String toTableId) {
        return ResponseEntity.status(200).body(hiposService.changeTempData(fromTable, toTable,fromTableId,toTableId));
    }
    @RequestMapping(value = "/getRestaurantConfirmNotification",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity getRestaurantConfirmNotification(@RequestBody ZomatoPojo zomatoPojo)throws Exception{
        return ResponseEntity.status(200).body(hiposService.getRestaurantConfirmNotification(zomatoPojo));
    }

    @RequestMapping(value = "/getUserObject",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity getUserObject(@RequestAttribute(value = "fullName")String name){
        return ResponseEntity.status(200).body(hiposService.getUserObject(name));
    }
    @RequestMapping(value = "/getDeactivatelicense",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity getDeactivatelicense(@RequestParam(value = "orderId")String orderId) throws Exception{
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message",hiposService.getDeactivatelicense(orderId));
        return ResponseEntity.status(200).body(objectNode.toString());
    }
    @RequestMapping(value="/printHTML",method = RequestMethod.POST)
    public ResponseEntity printHTML(@RequestBody String printDIV,
                                    @RequestParam(value = "cssStyle", required = false) String cssStyle,
                                    @RequestParam(value = "printType", required = false) String printerType){
        hiposService.printHTML(printDIV,cssStyle,printerType);
        return null;
    }
    @RequestMapping(value = "/userValidate",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity userValidate(@RequestBody UserAccountSetUpDTO userAccountSetUpDTO)throws Exception{
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message",hiposService.userValidate(userAccountSetUpDTO));
        return ResponseEntity.status(200).body(objectNode.toString());
    }
    @RequestMapping(value="/getKitchenOrders",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getKitchenOrders(@RequestParam(value="waiterName",required = false) String waiterName,
                                           @RequestParam(value="requestFrom",required = false) String requestFrom){

        String productStatusKitchen[] = {"Ordered", "Partially Completed", "Partially Delivered"};
        String productStatusWaiter[] = {"Completed", "Partially Completed", "Partially Delivered"};
        AccountSetup accountSetup =accountSetupRepository.findOne(1L);
        Calendar presentDate = Calendar.getInstance();
        Date date = DateUtils.addMinutes(presentDate.getTime(), -Integer.parseInt(accountSetup.getPompOrderMaxTime()));
        List<Map> mapList = new ArrayList<>();
        if(requestFrom.equals("kitchen")){
           mapList = restaurantTokenRecordRepository.findByProductStatus(productStatusKitchen,date,new Date());
        }
        else {
            mapList = restaurantTokenRecordRepository.findByWaiterName(waiterName,productStatusWaiter,date,new Date());
        }
        for(Map obj:mapList){
            RestaurantTempData restaurantTempData=hiposService.getTempDataBasedOnId(obj.get("tableName").toString(),obj.get("tableId").toString());
            if(restaurantTempData!=null)
            obj.put("orderNo",restaurantTempData.getOrderNo());
        }

        return ResponseEntity.ok(mapList);
    }
    @RequestMapping(value = "/getTableConfig/{configurationName}",method = RequestMethod.GET,produces="application/json")
    public ResponseEntity getTableConfigList(@PathVariable("configurationName") String configurationName) {
        String tableConfigList = hiposService.getTableConfigList(configurationName);
        return ResponseEntity.status(200).body(tableConfigList);
    }
    @RequestMapping(value="/updateTokenDetails",method = RequestMethod.POST, produces = "application/json",consumes ="application/json")
    public ResponseEntity updateTokenDetails(@RequestBody String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            RestaurantTokenRecord restaurantTokenRecord = restaurantTokenRecordRepository.findOne(jsonObject.getLong("restaurantTokenId"));
            restaurantTokenRecord.setRemovedItemsList(jsonObject.getString("tokenItemDetailsList"));
            restaurantTokenRecord.setProductStatus(jsonObject.getString("status"));
            restaurantTokenRecordRepository.save(restaurantTokenRecord);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(jsonString);
    }

    @RequestMapping(value = "/placeOrderNotification",method = RequestMethod.POST,consumes = "application/json",produces = "application/json")
    public ResponseEntity placeOrderNotification(@RequestParam("currTableName") String currTableName,
                                                     @RequestParam(value = "customerName")String customerName,
                                                     @RequestParam(value = "customerNo")String customerNo,
                                                     @RequestParam(value = "currTableId")String currTableId,
                                                     @RequestParam(value = "waiter",required = false)String waiter,
                                                     @RequestBody  String map1,
                                                     @RequestParam(value = "orderNo")String orderNo,
                                                     @RequestParam(value = "pax")String pax,
                                                     @RequestParam(value = "instructions")String instructions,
                                                     @RequestParam(value = "agentId")String agentId, @RequestParam(value="duplicatePrint") boolean duplicatePrint,
                                                     @RequestParam(value="kotRequired") boolean kotRequired)throws Exception{
        Customer customer=  customerRepository.findByCustomerName(customerName);
        if(customer==null){
            CustomerPojo customerAccountMasterDTO=new CustomerPojo();
            customerAccountMasterDTO.setCustomerNumber(customerNo);
            customerAccountMasterDTO.setCustomerName(customerName);
//            Company company = hiposDAO.getCompany();
//            customerAccountMasterDTO.setState(company.getState().getId().toString());
//            customerAccountMasterDTO.setCountry(company.getCountryId().getCountryId().toString());
//            customerAccountMasterDTO.setCurrency(company.getCurrencyId().getCurrencyId().toString());
            hiposService.saveCustomer(customerAccountMasterDTO);
        }
        customer=  customerRepository.findByCustomerName(customerName);
        Gson gson=new Gson();
        Type t=new TypeToken<Map<String,String>>(){}.getType();
        Map<String,String> map=gson.fromJson(map1,t);

        RestaurantTempData restaurantTempData=new RestaurantTempData();
        restaurantTempData=hiposService.saveTempTableData(currTableName, currTableId,waiter,customer.getCustomerId(),map.get("json"),orderNo,agentId);

        return ResponseEntity.status(200).body(restaurantTempData);
    }
    @RequestMapping(value = "/getPaginatedCategoryList", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getPaginatedCategoryList(@RequestParam(value = "type", required = false) String type,
                                                   @RequestParam(value = "searchText", required = false) String searchText,
                                                   @RequestBody BasePojo basePojo) {
        return ResponseEntity.status(200).body(hiposService.getPaginatedCategoryList(type, basePojo, searchText));
    }

    @RequestMapping(value = "/getPaginatedCurrencyList", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getPaginatedCurrencyList(@RequestParam(value = "type", required = false) String type,
                                                   @RequestParam(value = "searchText", required = false) String searchText,
                                                   @RequestBody BasePojo basePojo) {
        return ResponseEntity.status(200).body(hiposService.getPaginatedCurrencyList(type, basePojo, searchText));
    }
    @RequestMapping(value="/placeOrderToKOT",method = RequestMethod.POST)
    public ResponseEntity placeOrderToKOT(@RequestBody List<POSKOTItemOrderDTO> kitchenAllOrders,
                                          @RequestParam(value = "tableName") String tableName,
                                          @RequestParam(value = "waiterName") String waiterName,
                                          @RequestParam(value = "pax") String pax,
                                          @RequestParam(value="customerName") String customerName,
                                          @RequestParam(value="instructions",required = false) String instructions,
                                          @RequestParam(value="tableId") String tableId,
                                          @RequestParam(value="description") String description,
                                          @RequestParam(value="phone") String phone,
                                          @RequestParam(value="kotRequired") boolean kotRequired,
                                          @RequestParam(value="duplicatePrint") boolean duplicatePrint) throws Exception {

        RestaurantTempData restaurantTempData=new RestaurantTempData();
        RestaurantTokenRecord restaurantTokenRecord=new RestaurantTokenRecord();
        Map result= new HashMap();
        if(!StringUtils.isEmpty(tableId)) {
            TablesPos tablesPos = hiposService.getTablePosObj(tableId);
            Gson gson = new Gson();
            if (!StringUtils.isEmpty(tablesPos.getMergeTable())) {
                List<Long> longList = new ArrayList<>();
                Type type = new TypeToken<List<Long>>() {
                }.getType();
                longList = gson.fromJson(tablesPos.getMergeTable(), type);
                for (Long restaurantTempData1 : longList) {
                    TablesPos tablesPos1 = hiposService.getTablePosObj(Long.toString(restaurantTempData1));
                    restaurantTempData = hiposService.getTempDataBasedOnId(tablesPos1.getTableName(), tablesPos1.getTableid().toString());
                    if (StringUtils.isEmpty(waiterName)) {
                        waiterName = restaurantTempData.getUseraccount_id();
                    }
                    Long restaurantTem = restaurantTempData.getId();
                    restaurantTokenRecord = hiposService.createRestaurantTokenRecord(instructions, pax, kitchenAllOrders, tablesPos1.getTableName(), waiterName, restaurantTem, tablesPos1.getTableid().toString(), restaurantTempData.getOrderNo());
                }
            } else {
                restaurantTempData = hiposService.getTempDataBasedOnId(tableName, tableId);
                if (StringUtils.isEmpty(waiterName)) {
                    waiterName = restaurantTempData.getUseraccount_id();
                }
                String orderNo = "";
                Long restaurantTempDataId = null;
                if(restaurantTempData!=null){
                    orderNo = restaurantTempData.getOrderNo();
                    restaurantTempDataId = restaurantTempData.getId();
                }
                restaurantTokenRecord = hiposService.createRestaurantTokenRecord(instructions, pax, kitchenAllOrders, tableName, waiterName, restaurantTempDataId,  tableId, orderNo);
            }
        } else {
            restaurantTempData = hiposService.getTempDataBasedOnId(tableName, tableId);
            if (StringUtils.isEmpty(waiterName)) {
                waiterName = restaurantTempData.getUseraccount_id();
            }
            String orderNo = "";
            Long restaurantTempDataId = null;
            if(restaurantTempData!=null){
                orderNo = restaurantTempData.getOrderNo();
                restaurantTempDataId = restaurantTempData.getId();
            }
            restaurantTokenRecord = hiposService.createRestaurantTokenRecord(instructions, pax, kitchenAllOrders, tableName, waiterName, restaurantTempDataId, tableId, orderNo);
        }
        String tokenNo = restaurantTokenRecord.getToken();
        if(kotRequired) {
            String tableNam=null;
            if(!StringUtils.isEmpty(tableId)) {
                TablesPos tablesPos = hiposService.getTablePosObj(tableId);
                if (!StringUtils.isEmpty(tablesPos.getMergeTable())) {
                    List<Long> longList = new ArrayList<>();
                    Gson json = new Gson();
                    Type type = new TypeToken<List<Long>>() {
                    }.getType();
                    longList = json.fromJson(tablesPos.getMergeTable(), type);
                    for (Long tablesPos1 : longList) {
                        TablesPos tablesPos2 = hiposService.getTablePosObj(tablesPos1.toString());
                        if (StringUtils.isEmpty(tableNam)) {
                            tableNam = tablesPos2.getTableName();
                        } else {
                            tableNam = tableNam + " , " + tablesPos2.getTableName();
                        }
                    }
                }else {
                    tableNam=tableName;
                }
            }else {
                tableNam=tableName;
            }
            String agent = "";
            if(restaurantTempData!=null) {
                agent = restaurantTempData.getAgentId();
            }
            if(duplicatePrint){
                result=hiposService.mapKitchenOrderToPrintersAndPrint(kitchenAllOrders, tableNam, waiterName, tokenNo, customerName,instructions,restaurantTokenRecord.getOrderNo(),pax,agent);
            }
            result=hiposService.mapKitchenOrderToPrintersAndPrint(kitchenAllOrders, tableNam, waiterName, tokenNo, customerName,instructions,restaurantTokenRecord.getOrderNo(),pax,agent);
        }
        if(!StringUtils.isEmpty(description)){
            sendSmsRestaurant.sendSmsRestaurantForKot(String.valueOf(customerName),phone,tokenNo,waiterName,description);
        }
        return ResponseEntity.ok(result);
    }
    @RequestMapping(value = "/saveTableDataTemp",method = RequestMethod.POST,consumes = "application/json",produces = "application/json")
    public ResponseEntity saveTempTableData(@RequestParam("currTableName") String currTableName,
                                                @RequestParam("currTableId") String currTableId,
                                                @RequestParam("employeeName") String employeeName,
                                                @RequestParam(value = "customerId")Long customerId,
                                                @RequestBody String kitchenAllOrders,
                                                @RequestParam(value = "orderNo")String orderNo,
                                                @RequestParam(value = "pax")String pax,
                                                @RequestParam(value = "instructions")String instructions,
                                                @RequestParam(value = "agentId")String agentId){
        Gson gson=new Gson();
        Type t=new TypeToken<Map<String,String>>(){}.getType();
        Map<String,String> map=gson.fromJson(kitchenAllOrders,t);
        RestaurantTempData restaurantTempData=new RestaurantTempData();
        if(!StringUtils.isEmpty(currTableId)) {
            TablesPos tablesPos = hiposService.getTablePosObj(currTableId);
            if (!StringUtils.isEmpty(tablesPos.getMergeTable())) {
                List<Long> longList = new ArrayList<>();
                Type type = new TypeToken<List<Long>>() {
                }.getType();
                longList = gson.fromJson(tablesPos.getMergeTable(), type);
                for (Long restaurantTempData1 : longList) {
                    TablesPos tablesPos1 = hiposService.getTablePosObj(Long.toString(restaurantTempData1));
                    restaurantTempData=hiposService.saveTempTableData(tablesPos1.getTableName(), tablesPos1.getTableid().toString(), employeeName, customerId, map.get("json"), orderNo, agentId);
                }
            }else {
                restaurantTempData=hiposService.saveTempTableData(currTableName, currTableId,employeeName,customerId,map.get("json"),orderNo,agentId);
            }
        }else {
            restaurantTempData=hiposService.saveTempTableData(currTableName, currTableId,employeeName,customerId,map.get("json"),orderNo,agentId);
        }
        return ResponseEntity.status(200).body(restaurantTempData);
    }

    @RequestMapping(value = "/saveMergeTable",method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity saveMergeTable(@RequestParam(value = "fromTableId") String fromTableId,
                                    @RequestParam(value = "toTableId") String toTableId) {
        return ResponseEntity.status(200).body(hiposService.saveMergeTable(fromTableId,toTableId));
    }
    @RequestMapping(value = "/getChangeTableList",method = RequestMethod.GET,produces="application/json")
    public ResponseEntity getChangeTableList(@RequestParam(value="type")String type) {
        List<TablesPosPojo> tableList = null;
        tableList=hiposService.getOccupiedTables(type);
        return ResponseEntity.status(200).body(tableList);
    }

    @RequestMapping(value = "/completeTableList",method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity completeTableList(@RequestParam(value = "type")String type){
        return ResponseEntity.status(200).body(hiposService.completeTableList(type));
    }
    @RequestMapping(value = "/saveNewItem",method = RequestMethod.POST,produces="application/json",consumes = "application/json")
    public ResponseEntity saveNewItem(@RequestBody AddNewItemDTO saveNewItemDetails ) throws Exception {
        AddNewItemDTO itemDTO = null;
        itemDTO = hiposService.createSaveNewItemDetails(saveNewItemDetails);
        return ResponseEntity.status(200).body(itemDTO);
    }
    @RequestMapping(value = "/saveAccountSetup",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity saveAccountSetup(@RequestBody AccountSetupPojo accountSetupPojo){
        return ResponseEntity.status(200).body(hiposService.saveAccountSetup(accountSetupPojo));
    }
    @RequestMapping(value = "/getMergeTableList",method = RequestMethod.GET,produces="application/json")
    public ResponseEntity getMergeTableList(@RequestParam(value="type") String type,@RequestParam(value = "searchText",required = false)String searchText){
        List<TablesPosPojo> tableList = null;
        tableList=hiposService.getMergeOccupiedTables(type,searchText);
        return ResponseEntity.status(200).body(tableList);
    }
    @RequestMapping(value = "/getItemList",method = RequestMethod.GET,produces="application/json")
    public ResponseEntity getItemList(@RequestParam(value = "itemSearchText") String itemSearchText) {
        List<Item> ItemDTOList = hiposService.retrieveItemListForInventoryModule(itemSearchText);
        return ResponseEntity.status(200).body(ItemDTOList);
    }
    @RequestMapping(value = "/saveTable",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity saveTablesPos(@RequestBody TablesPosPojo tablesPosPojo){
        return ResponseEntity.status(200).body(hiposService.saveTablesPos(tablesPosPojo));
    }
    @RequestMapping(value ="/getReportforPeriod", method = RequestMethod.GET)
    public ResponseEntity getReportforPeriod(@RequestParam(value = "dayEndStatus",required = false) String dayEndStatus,
                                                @RequestParam(value = "type",required = false) String type,
                                                @RequestParam(value = "locationId",required = false) String location){
        Map map=hiposService.reportForPeriodList(dayEndStatus,type,location);
        return ResponseEntity.ok(map);

    }
    @RequestMapping(value = "/savetableconfiguration",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity saveTableConfig(@RequestBody TableConfigPojo tableConfigPojo){
        return ResponseEntity.status(200).body(hiposService.saveTableConfig(tableConfigPojo));
    }
    @RequestMapping(value = "/onlinePay",method = RequestMethod.POST,consumes = "application/json")
    public ResponseEntity createOnlinePayment(@RequestBody RetailDTO retailDTO)throws Exception{
        InvoicePayDetails invoicePayDetails = null;
        Company company =companyRepository.findAllByStatus("Active");
        String invoiceNo = randomAlphaNumeric(company.getConnectNo());
        PaymentMethod paymentMethod = paymentMethodRepository.findByPaymentmethodName("invoicePay");
        Gson json = new Gson();

        Map<String,String> customerDetails = new HashMap<>();
        customerDetails.put("FIRST_NAME",retailDTO.getCutomerName());
        customerDetails.put("LAST_NAME","");
        customerDetails.put("EMAIL",retailDTO.getInvoicePaymentList().get(0).getInvmail());
        customerDetails.put("PHONE",retailDTO.getInvoicePaymentList().get(0).getInvmobile());
        List<AirPayInvoicePOJO> invoicePOJOList = new ArrayList<>();
        for(SelectedItem selectedItem:retailDTO.getSelectedItemsList()){
            AirPayInvoicePOJO invoicePOJO = new AirPayInvoicePOJO();
            invoicePOJO.setITEM_NAME(selectedItem.itemName);
            invoicePOJO.setITEM_DESCRIPTION(selectedItem.getItemDescription());
            invoicePOJO.setITEM_PRICE(String.valueOf(selectedItem.getUnitPrice()));
            invoicePOJO.setITEM_TAX(String.valueOf(selectedItem.getTaxamt()));
            invoicePOJO.setITEM_QUANTITY(String.valueOf(selectedItem.getQty()));
            invoicePOJOList.add(invoicePOJO);
        }
        Map<String,Boolean> mailPojo = new HashMap<>();

        if(org.springframework.util.StringUtils.isEmpty(retailDTO.getInvoicePaymentList().get(0).getInvmail())){
            mailPojo.put("EMAIL",false);
        }else{
            mailPojo.put("EMAIL",true);
        }
        if(org.springframework.util.StringUtils.isEmpty(retailDTO.getInvoicePaymentList().get(0).getInvmobile())){
            mailPojo.put("SMS",false);
        }else{
            mailPojo.put("SMS",true);
        }

        AirPayTotalPojo totalPojo = new AirPayTotalPojo();
        totalPojo.setMERCHANT_ID(paymentMethod.getMerchantId());
        totalPojo.setMODE("");
        totalPojo.setCustomer(customerDetails);
        totalPojo.setINVOICE_NUMBER(invoiceNo);
        totalPojo.setTOTAL_AMOUNT(String.valueOf(retailDTO.getTotalTenderedAmount()));
        totalPojo.setSEND_REQUEST(mailPojo);
        totalPojo.setInvoice_item(invoicePOJOList);
        totalPojo.setCUSTOM_DATA("");

        Date presentDate = HiposUtil.parseDate("");
        DateFormat d = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(d.parse(d.format(presentDate)));
        c.add(Calendar.DATE, 1);  // number of days to add
        String date =d.format(c.getTime());
        //String data ="{\"MERCHANT_ID\":\"28555\",\"MODE\":\"\",\"customer\":{\"FIRST_NAME\":\"jayesh\",\"LAST_NAME\":\"khadye\",\"EMAIL\":\"jayesh.khadye@airpay.co.in\",\"PHONE\":\"7019549455\"},\"INVOICE_NUMBER\":\"test845\",\"TOTAL_AMOUNT\":\"1.00\",\"SEND_REQUEST\":{\"EMAIL\":true,\"SMS\":true},\"invoice_item\":[{\"ITEM_NAME\":\"Test\",\"ITEM_DESCRIPTION\":\"Test\",\"ITEM_PRICE\":\"1.00\",\"ITEM_TAX\":\"0.00\",\"ITEM_QUANTITY\":\"1\"}],\"CUSTOM_DATA\":\"\"}";
        String token = paymentMethod.getSecretKey()+"~"+json.toJson(totalPojo) ;
        String tokens = MD5Hashing.getHashedPassword(token);
        String requestUrl = "http://payments.nowpay.co.in/api/invoice/create";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> uriParams = new LinkedMultiValueMap<String, String>();
        uriParams.add("data", json.toJson(totalPojo));
        uriParams.add("token", tokens);
        uriParams.add("expiry_date", date);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(uriParams, headers);
        System.out.println("SSS"   + uriParams);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity( requestUrl, request , String.class );
        response.getBody();
        if(!StringUtils.isEmpty(response.getBody())) {
            Map<String, Object> responseMap = new HashMap<>();
            Gson gson = new Gson();
            Type type1 = new TypeToken<Map<String, Object>>() {
            }.getType();
            responseMap = gson.fromJson(response.getBody(), type1);

            if (StringUtils.equalsIgnoreCase(responseMap.get("success").toString(), "true")) {
                invoicePayDetails = getOnlineInvDetails(invoiceNo);

            }
            else if(StringUtils.equalsIgnoreCase(responseMap.get("success").toString(), "false")){
                String message = responseMap.get("message").toString();
                invoicePayDetails = new InvoicePayDetails();
                invoicePayDetails.setTransactionPaymentStatus("FAIL");

            }
        }
        return ResponseEntity.status(200).body(invoicePayDetails);

    }
    int timelimit=0;
    long t1 =0;
    public InvoicePayDetails getOnlineInvDetails(String orderId)throws Exception {
        if(timelimit ==0){
            t1 = System.currentTimeMillis() +600000;
            timelimit =timelimit + 1;
        }
        InvoicePayDetails invoicePayDetails = new InvoicePayDetails();

        ResponseEntity<String> responseEntity = null;
        String URL="http://pay.restopos.in/dashdoard/getTransactionINVDetails";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<String>(orderId, headers);
        responseEntity = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
        if(!StringUtils.isEmpty(responseEntity.getBody())){
            Gson gson = new Gson();
            invoicePayDetails= gson.fromJson(responseEntity.getBody(),InvoicePayDetails.class);
            invoicePayDetails.setInvoicePayId(null);
            invoicePayRepository.save(invoicePayDetails);
            return invoicePayDetails;

        }
        else {
            if (System.currentTimeMillis() < t1) {
                invoicePayDetails =getOnlineInvDetails(orderId);
                return invoicePayDetails;

            }else{
                timelimit=0;
                t1=0;
                invoicePayDetails.setTransactionPaymentStatus("FAIL");
                return invoicePayDetails;

            }

        }

    }
    @RequestMapping(value = "/save",method = RequestMethod.POST,consumes = "application/json")
    public ResponseEntity save(@RequestParam(value = "tableNo",required = false) String tableNo,
                               @RequestParam(value = "tableName",required = false) String tableName,
                               @RequestParam(value = "waiterName",required = false) String waiterName,
                               @RequestParam(value = "printVal",required = false) String printType,
                               @RequestBody RetailDTO retailDTO)throws Exception{
        retailDTO.setEmployeeName(waiterName);
        Map<String,List<String>> cloudPrinterConf = new HashMap();
        BasePosPrinterService posPrinterService =null;
        AccountSetup accountSetup =hiposService.getAccountSetup();
        Gson gson = new Gson();
        Type type1 = new TypeToken<PrinterSelectionDisplayPojo>(){}.getType();
        PrinterSelectionDisplayPojo printerSelectionDisplayPojo = new PrinterSelectionDisplayPojo();
        printerSelectionDisplayPojo = gson.fromJson(accountSetup.getPrintDetails(),type1);
        Company company =companyRepository.findAllByStatus("Active");
        RetailPrintDTO retailPrintDTO=new RetailPrintDTO();
        retailPrintDTO.setPrinterSelectionDisplayPojo(printerSelectionDisplayPojo);
        FormSetUp formSetUp=formSetupRepository.findAllByTypename("DirectSalesInvoice");
        int incValue = Integer.parseInt(formSetUp.getNextref());
        String ref=String.format("%05d", ++incValue);
        String no = formSetUp.getTypeprefix() + ref;
        retailDTO.setSiNo(no);
        Customer customer=hiposService.getCustomerObject(retailDTO.getCustomerId());
        if(StringUtils.isEmpty(retailDTO.getMobileNo())&&customer!=null){
            retailDTO.setMobileNo(customer.getPhoneNumber1());
        }
        if(!StringUtils.isEmpty(tableNo)) {
            TablesPos tablesPos3 = hiposService.getTablePosObj(tableNo);
            retailPrintDTO=hiposService.getAllDetails(retailDTO,tableNo,tablesPos3.getTableName(),waiterName);
        }else {
            retailPrintDTO=hiposService.getAllDetails(retailDTO,tableNo,tableName,waiterName);
        }
        retailPrintDTO.setPrintType(printType);
        if(StringUtils.equalsIgnoreCase(printType,"mobile")) {
            posPrinterService = hiposService.getMobilePrinterService();
        }
        else {
            posPrinterService = hiposService.getReportPrinterService(tableNo);
        }
        //direct print
        if(StringUtils.isEmpty(retailDTO.getOrderType())){
            retailDTO.setInvoiceType("DineIn");
        }
        if(accountSetup!=null&&accountSetup.isPrintPreview()&&!StringUtils.equalsIgnoreCase(retailDTO.getOrderType(),"DineIn")){
            printType="save";
        }
        retailPrintDTO.getSiData().setMainTable(retailDTO.getTable());
        if(!StringUtils.isEmpty(tableNo)) {
            TablesPos tablesPos = hiposService.getTablePosObj(tableNo);
            if (!StringUtils.isEmpty(tablesPos.getMergeTable())) {
                List<Long> longList = new ArrayList<>();
                Gson json = new Gson();
                Type type = new TypeToken<List<Long>>() {
                }.getType();
                longList = json.fromJson(tablesPos.getMergeTable(), type);
                retailPrintDTO.getSiData().setMainTable(null);
                for (Long tablesPos1 : longList) {
                    TablesPos tablesPos2 = hiposService.getTablePosObj(tablesPos1.toString());
                    if (StringUtils.isEmpty(retailPrintDTO.getSiData().getMainTable())) {
                        retailPrintDTO.getSiData().setMainTable(tablesPos2.getTableName());

                    } else {
                        retailPrintDTO.getSiData().setMainTable(retailPrintDTO.getSiData().getMainTable() + " , " + tablesPos2.getTableName());
                    }
                }
            }
        }
        if(!StringUtils.equalsIgnoreCase(retailPrintDTO.getSiData().getDiscountType(),"itemWise")){
            if(retailPrintDTO.getSiData().getSelectedItemsList().size()>0&&StringUtils.equalsIgnoreCase(retailPrintDTO.getSiData().getSelectedItemsList().get(0).getType(),"Percentage"))
                retailPrintDTO.getSiData().setDiscountAmtInPercentage(retailPrintDTO.getSiData().getSelectedItemsList().get(0).getDiscountConfigAmt().floatValue());
        }
        if(!retailDTO.isBluetoothStatus()&& !StringUtils.equalsIgnoreCase("save",printType) && posPrinterService!=null) {
            cloudPrinterConf = posPrinterService.formatAndPrintBill(retailPrintDTO);
        }
        if(!StringUtils.isEmpty(tableNo)) {
            TablesPos tablesPos3 = hiposService.getTablePosObj(tableNo);
            retailPrintDTO = hiposService.createInoviceNPayment(retailDTO, tableNo, tablesPos3.getTableName(),waiterName,retailPrintDTO);
        }else {
            retailPrintDTO = hiposService.createInoviceNPayment(retailDTO, tableNo, tableName,waiterName,retailPrintDTO);
        }
        retailDTO.setTableNo(tableNo);
        if(!StringUtils.isEmpty(tableNo)) {
            TablesPos tablesPos = hiposService.getTablePosObj(tableNo);
            if (!StringUtils.isEmpty(tablesPos.getMergeTable())) {
                List<Long> longList = new ArrayList<>();
                Gson json = new Gson();
                Type type = new TypeToken<List<Long>>() {
                }.getType();
                longList = json.fromJson(tablesPos.getMergeTable(), type);
                retailPrintDTO.getSiData().setTable("");
                for (Long tablesPos1 : longList) {
                    TablesPos tablesPos2 = hiposService.getTablePosObj(tablesPos1.toString());
                    if (StringUtils.isEmpty(retailPrintDTO.getSiData().getTable())) {
                        retailPrintDTO.getSiData().setTable(tablesPos2.getTableName());

                    } else {
                        retailPrintDTO.getSiData().setTable(retailPrintDTO.getSiData().getTable() + " , " + tablesPos2.getTableName());
                    }
                }
                for (Long ta : longList) {
                    TablesPos tablesPos2 = hiposService.getTablePosObj(ta.toString());
                    tablesPos2.setMergeTable("");
                    tablesPosRepository.save(tablesPos2);
                }
            }
        }
        String token = null;
        if(retailDTO.getAirPayments().getAirPayPaymentList().size() > 0) {
            PaymentMethod paymentMethod = paymentMethodRepository.findByPaymentmethodName("airPay");
            token =randomAlphaNumeric(company.getConnectNo());
            String requestUrl = "http://opi.airpay.co.in/web/api/pos/create";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> uriParams = new LinkedMultiValueMap<String, String>();
            uriParams.add("mercid", paymentMethod.getMerchantId());
            uriParams.add("orderid", token);
            uriParams.add("amount", String.valueOf(retailDTO.getTotalTenderedAmount()));
            uriParams.add("currency", "356");
            uriParams.add("isocurrency", "INR");
            uriParams.add("uniqueid", paymentMethod.getUniqueId());
            uriParams.add("mobile", retailDTO.getAirPayments().getAirPayPaymentList().get(0).getAirPaymobile());
            uriParams.add("postxntype", "01");
            uriParams.add("invoiceNo", token);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(uriParams, headers);
            System.out.println("SSS" + uriParams);
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.postForObject(requestUrl, request, String.class);
            System.out.println("aaa" + response);
            retailPrintDTO.setAirPayMerchantId(paymentMethod.getMerchantId());

        }
        else{
            retailPrintDTO.setAirPayMerchantId(null);
        }

        String phoneNo=retailDTO.getMobileNo();
        if(retailDTO.isSmsType()==true){
            sendSmsRestaurant.sendSmsRestaurant(String.valueOf(retailDTO.getTotalCheckOutamt()),retailDTO.getCutomerName(),phoneNo,retailDTO.getSiNo());
        }
        HashMap<String,Object> multiResponse = new HashMap<>();
        multiResponse.put("Cloud",cloudPrinterConf);
        multiResponse.put("Desktop",retailPrintDTO);
        for(SelectedItem selectedItem:retailPrintDTO.getSiData().getSelectedItemsList()){
            selectedItem.setInclusiveJSON(null);
        }
        LocationDto locationDto = new LocationDto();
        locationDto.setHiconnectNo(company.getConnectNo());
        retailPrintDTO.setInventoryLocationData(locationDto);
        String jsonString = gson.toJson(retailPrintDTO);
        company=hiposService.getCompanyList();

        if(StringUtils.isEmpty(retailDTO.getOrderNo())) {
            ResponseEntity<String> responseEntity = null;
            String URL = hiposService.readDomainNameRestoOrder() + "/services/Account/insertOrder";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonString, headers);
            responseEntity = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
        }else {
            if (!StringUtils.isEmpty(tableNo)){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("connect_id",company.getConnectNo());
                jsonObject.put("order_id",retailDTO.getOrderNo());
                jsonObject.put("order_status","Completed");
                if(retailDTO.getCashPayment().getMultiCashPaymentList().size()>0){
                    jsonObject.put("payment_mode","Cash");
                }else if(retailDTO.getVoucherPayment().getMultiVoucherPayments().size()>0){
                    jsonObject.put("payment_mode","Voucher");
                }
                else if(retailDTO.getBankPayment().getMultiBankPaymentList().size()>0){
                    jsonObject.put("payment_mode","Bank");
                }
                else if(retailDTO.getCreditPayment().getCardPaymentList().size()>0){
                    jsonObject.put("payment_mode","Card");
                }
                jsonObject.put("pay_amount",retailDTO.getTotalCheckOutamt());
                jsonObject.put("invoice_no",no);
                String url =hiposService.readDomainNameRestoOrder()+"/services/Account/orderpaymentDetails";
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Type", "application/json");
                HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
                ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                String jsonString1 = responseEntity.getBody();
            }else{
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("order_id", retailDTO.getOrderNo());
                jsonObject.put("status", "Completed");
                jsonObject.put("message", "Order Completed");
                jsonObject.put("store_id",company.getConnectNo());
                String url =hiposService.readDomainNameRestoOrder()+"/services/Account/orderStatusChange";
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Type", "application/json");
                HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
                ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                String jsonString2 = responseEntity.getBody();
            }
        }
        Pusher pusher = new Pusher();
        String kafkaAddress=hiposService.readDomainNameKafkaUrl();
        String consumerId=hiposService.readDomainNameConsumerId();
        pusher.setJsonData(jsonString);
        pusher.setKafkaServerAddress(kafkaAddress);
        pusher.setKafkaConsumerGroupId(consumerId);
        pusher.setKafkaTopic(company.getConnectNo());
        pusher.setHttpMethodType(HttpMethod.POST.toString());
        pusher.setActive(true);
        pusher = pusherRepository.save(pusher);
        return ResponseEntity.ok(multiResponse);
        }

    @RequestMapping(value="/printDuplicateBillSales", method= RequestMethod.POST)
    public ResponseEntity printDuplicateBill(@RequestBody RetailPrintDTO retailPrintDTO,
                                             @RequestParam(value = "type")String type){
        Map<String,List<String>> cloudPrinterConf = new HashMap();
        BasePosPrinterService posPrinterService = hiposService.getReportPrinterService(null);
        if(posPrinterService!=null) {
            if(StringUtils.equalsIgnoreCase(type,"Restaurant")){
                cloudPrinterConf = posPrinterService.formatAndPrintBill(retailPrintDTO);
            }
        }
        return ResponseEntity.ok(cloudPrinterConf);
    }
    public String randomAlphaNumeric(String hiconnectno) {
        int length = 5;
        Date aa = HiposUtil.parseDate("");
        boolean useLetters = true;
        boolean useNumbers = true;
        String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
        System.out.println(generatedString);
        String keyword;
        keyword = hiconnectno.substring(0,5) + generatedString;
        AirPayDetails airPayDetails = airpayRepository.findByTransactionID(keyword);
        if(airPayDetails !=null){
            randomAlphaNumeric(keyword);
        }
        return keyword;


    }
    @RequestMapping(value = "/getSimplifiedItemList",method = RequestMethod.GET,produces="application/json")
    public ResponseEntity getSimplifiedItemList() {
        List<ItemDTO> ItemDTOList = hiposService.retrieveSimplifiedItemList();
        return  ResponseEntity.status(200).body(ItemDTOList);

    }
    @RequestMapping(value = "/getPosInvoices", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getPosInvoiceList(@RequestParam(value = "itemSearchText") String itemSearchText,
                                            @RequestParam("type") String type) {
        List<SalesInvoiceDTO> posSalesList = hiposService.retrievePOSInvoiceist(itemSearchText,type);
        return ResponseEntity.status(200).body(posSalesList);
    }
    @RequestMapping(value = "/getDuplicateSIPrint", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getDuplicateSIPrint(@RequestParam(value = "invoiceNo") String invoiceNo) throws ParseException {
        Gson gson = new Gson();
        Type type1 = new TypeToken<PrinterSelectionDisplayPojo>(){}.getType();
        PrinterSelectionDisplayPojo printerSelectionDisplayPojo = new PrinterSelectionDisplayPojo();
//        printerSelectionDisplayPojo = gson.fromJson(accountSetup.getPrintDetails(),type1);
        RetailPrintDTO retailDTO = hiposService.retrieveSIDetailsA4(invoiceNo);
        retailDTO.setPrinterSelectionDisplayPojo(printerSelectionDisplayPojo);
//        retailDTO.setPrintType(accountSetup.getPrintType());
        CompanyInfoDto companyInfoDto=new CompanyInfoDto();

//        retailDTO.getSiData().setDiscountType(accountSetup.getDiscountType());
        return  ResponseEntity.status(200).body(retailDTO);
    }
    @RequestMapping(value= "/cancelInvoice", method = RequestMethod.POST,produces = "text/plain")
    public String cancelInvoice(@RequestBody Map<String, String> params)throws JSONException,IOException{
        hiposService.cancelInvoice(params.get("id"));
        Company company =companyRepository.findAllByStatus("Active");
        if(!StringUtils.isEmpty(company.getPhone())) {
            SalesInvoice salesInvoice = hiposService.getSalesInvoice(Long.valueOf(params.get("id")));
            sendSmsRestaurant.sendSmsRestaurantCancelled(String.valueOf(salesInvoice.getTotalAmount()), salesInvoice.getCustomerId().getCustomerName(), company.getPhone(), salesInvoice.getSINo());
        }
        return "Cancelled SuccessFully";
    }
    @RequestMapping(value = "/getConfigureData", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getConfigurationDetails() {
        AccountSetupPojo accountSetupDTO = null;
        accountSetupDTO = hiposService.getConfigureDetails();
        return ResponseEntity.status(200).body(accountSetupDTO);
    }
    @RequestMapping(value = "/printCustomerBill",method = RequestMethod.POST,consumes = "application/json")
    public ResponseEntity printCustomerBill(@RequestBody String jsonString)throws Exception {
        JSONObject jsonObject = new JSONObject(jsonString);
        RestaurantTempData restaurantTempData=hiposService.getTempDataBasedOnId(jsonObject.getString("tableName").toString(),jsonObject.getString("tableNo").toString());
        String tokenNos="";
        TablesPos tablesPos=hiposService.getTablePosObj(String.valueOf(restaurantTempData.getTableId()));
        if(!StringUtils.isEmpty(tablesPos.getMergeTable())){
            List<Long> longList = new ArrayList<>();
            Gson json = new Gson();
            Type type = new TypeToken<List<Long>>(){}.getType();
            longList = json.fromJson(tablesPos.getMergeTable(),type);
            for(Long ta:longList){
                TablesPos tablesPos2 = hiposService.getTablePosObj(ta.toString());
                tablesPos2.setTableStatus("CustomerBill");
                tablesPosRepository.save(tablesPos2);

            }
        }
        BasePosPrinterService posPrinterService = hiposService.getReportPrinterService(tablesPos.getTableid().toString());
        tablesPos.setTableStatus("CustomerBill");
        tablesPosRepository.save(tablesPos);
        if(!restaurantTempData.equals(new RestaurantTempData())){
            List<RestaurantTokenRecord> restaurantTokenRecords = restaurantTokenRecordRepository.findBySiNo(String.valueOf(restaurantTempData.getId()));
            tokenNos= restaurantTokenRecords.stream().map(RestaurantTokenRecord::getToken).collect(Collectors.joining(", "));
            restaurantTempData.setCustomerBill(true);
            restaurantTempDataRepository.save(restaurantTempData);
        }
        jsonObject.put("TokenNos",tokenNos);
        Map result=null;
        if(!StringUtils.isEmpty(tablesPos.getMergeTable())){
            List<Long> longList = new ArrayList<>();
            Gson json = new Gson();
            Type type = new TypeToken<List<Long>>(){}.getType();
            longList = json.fromJson(tablesPos.getMergeTable(),type);
            String tableName=null;
            for(Long tablesPos1 :longList){
                TablesPos tablesPos2 = hiposService.getTablePosObj(tablesPos1.toString());
                if(StringUtils.isEmpty(tableName)) {
                    tableName=tablesPos2.getTableName();
                }else {
                    tableName=tableName+ " , " + tablesPos2.getTableName();
                }
            }
            jsonObject.put("tableName",tableName);
        }
        if(posPrinterService!=null&&jsonObject.get("type").equals("Desktop")) {
            result = posPrinterService.formatAndPrintCustomerBill(jsonObject.toString());
        }else if(jsonObject.get("type").equals("Mobile")){
            result=new HashMap();
            result.put("result","success");
        }
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/saveNewAgent",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity saveAgent(@RequestBody AgentPojo agentPojo){
        return ResponseEntity.status(200).body(hiposService.saveAgent(agentPojo));
    }

    @RequestMapping(value = "/saveCustomer",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity saveCustomer(@RequestBody CustomerPojo customerPojo){
        return ResponseEntity.status(200).body(hiposService.saveCustomer(customerPojo));
    }
    @RequestMapping(value = "/getPaymentTypes",method = RequestMethod.GET,produces="application/json")
    public ResponseEntity getPaymentTypes() {
        String type=null;
        List<PaymentMethodDTO> paymentMethods = hiposService.getPaymentTypes(type);
        return ResponseEntity.status(200).body(paymentMethods);
    }
    @RequestMapping(value = "/saveUserAccountSetup",method = RequestMethod.POST,consumes ="application/json")
    public ResponseEntity saveUserAccountSetup(@RequestBody UserAccountSetUpDTO saveUserAccountSetupDetails)throws Exception {
        return ResponseEntity.status(200).body(hiposService.createSaveUserAccountSetupDetails(saveUserAccountSetupDetails));
    }
    @RequestMapping(value = "/deleteUserAccountSetup",method = RequestMethod.POST,consumes ="application/json")
    public ResponseEntity deleteUserAccountSetup(@RequestAttribute("userId") int userId ,
                                                 @RequestBody UserAccountSetup deleteDetails ){
        UserAccountSetup camDTO = null;
        camDTO = hiposService.deleteUserAccountSetupDetails(deleteDetails);
        return ResponseEntity.status(200).body(camDTO);
    }
    @RequestMapping(value = "/saveUserAccessRights",method = RequestMethod.POST,consumes ="application/json")
    public ResponseEntity saveUserAccessRights(@RequestBody UserAccessRightsDTO savePermission){
        UserAccessRightsDTO camDTO = hiposService.createSaveUserAccessRights(savePermission);
        return ResponseEntity.status(200).body(camDTO);
    }
    @RequestMapping(value = "/getCouponList",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity getCouponList(){
        return ResponseEntity.status(200).body(hiposService.getCouponList());
    }

    @RequestMapping(value = "/validateCoupon",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity validateCoupon(@RequestParam(value = "coupon")String coupon){
        return ResponseEntity.status(200).body(hiposService.validateCoupon(coupon));
    }
    @RequestMapping(value = "/getEditUserAccessRights",method = RequestMethod.POST,produces="application/json")
    public ResponseEntity getEditUserAccessRights(@RequestParam(value = "userIdSearchText") String userIdSearchText) {
        UserAccountSetup userAccObj= hiposService.getUserAccountSetupObj(Integer.parseInt(userIdSearchText));
        UserAccessRightsDTO userAccessRightsDTO = hiposService.retrieveEditUserAccessRightsByNameOrCode(userAccObj);
        return ResponseEntity.status(200).body(userAccessRightsDTO);
    }
    @RequestMapping(value = "/getPaginatedUserAccountSetupList",method = RequestMethod.POST,produces="application/json")
    public ResponseEntity getPaginatedUserAccountSetupList(@RequestParam(value = "userAccountSetupSearchText", required = false) String userAccountSetupSearchText,
                                                           @RequestParam(value="type", required = false)String type,
                                                           @RequestBody BasePojo basePojo) {
        return ResponseEntity.status(200).body(hiposService.getPaginatedUserAccountSetupList(userAccountSetupSearchText,type,basePojo));
    }

    @RequestMapping(value = "/getpaginatedcountry", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getpaginatedcountry(@RequestParam(value = "type", required = false) String type,
                                              @RequestParam(value = "searchText", required = false) String searchText,
                                              @RequestBody BasePojo basePojo) {
        return ResponseEntity.status(200).body(hiposService.getcountrypagelist(type, basePojo, searchText));
    }
    @RequestMapping(value = "/getPaginatedPaymentMethodList", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getPaginatedPaymentMethodList(@RequestParam(value = "type", required = false) String type,
                                              @RequestParam(value = "searchText", required = false) String searchText,
                                              @RequestBody BasePojo basePojo) {
        return ResponseEntity.status(200).body(hiposService.getPaginatedPaymentMethodList(type, basePojo, searchText));
    }
    @RequestMapping(value = "/getPaginatedShiftList", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getPaginatedShiftList(@RequestParam(value = "type", required = false) String type,
                                              @RequestParam(value = "searchText", required = false) String searchText,
                                              @RequestBody BasePojo basePojo) {
        return ResponseEntity.status(200).body(hiposService.getPaginatedShiftList(type, basePojo, searchText));
    }
    @RequestMapping(value = "/getPaginationCustomerList", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getPaginationCustomerList(@RequestParam(value = "type", required = false) String type,
                                              @RequestParam(value = "searchText", required = false) String searchText,
                                              @RequestBody BasePojo basePojo) {
        return ResponseEntity.status(200).body(hiposService.getPaginationCustomerList(type, basePojo, searchText));
    }

    @RequestMapping(value = "/getPaginatedTableconfiList", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getPaginatedTableconfiList(@RequestParam(value = "type", required = false) String type,
                                              @RequestParam(value = "searchText", required = false) String searchText,
                                              @RequestBody BasePojo basePojo) {
        return ResponseEntity.status(200).body(hiposService.getPaginatedTableconfigList(type, basePojo, searchText));
    }

    @RequestMapping(value = "/getpaginatedtable", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getpaginatedtable(@RequestParam(value = "type", required = false) String type,
                                                     @RequestParam(value = "searchText", required = false) String searchText,
                                                     @RequestBody BasePojo basePojo) {
        return ResponseEntity.status(200).body(hiposService.getpaginatedtable(type, basePojo, searchText));
    }

    @RequestMapping(value = "/deleteCountry", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity deleteCountry(@RequestBody CountryPojo details) {
        return ResponseEntity.status(200).body(hiposService.deleteCountry(details));
    }


    @RequestMapping(value = "/deletePayment", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity deletePayment(@RequestBody PaymentMethodDTO details) {
        return ResponseEntity.status(200).body(hiposService.deletePayment(details));
    }

    @RequestMapping(value = "/deleteEmployee", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity deleteEmployee(@RequestBody EmployeePojo details) {
        return ResponseEntity.status(200).body(hiposService.deleteEmployee(details));
    }

    @RequestMapping(value = "/deleteTableConfig", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity deleteTableConfig(@RequestBody TableConfigPojo details) {
        return ResponseEntity.status(200).body(hiposService.deleteTableConfig(details));
    }
    @RequestMapping(value = "/deleteState", method = RequestMethod.POST, produces = "application/json")
    public void deleteState(@RequestParam(value = "stateId", required = false) Long id)
    {
        hiposService.deleteState(id);
    }
    @RequestMapping(value = "/deleteShift", method = RequestMethod.POST, produces = "application/json")
    public void deleteShift(@RequestParam(value = "shiftId", required = false) Long id)
    {
        hiposService.deleteShift(id);
    }
    @RequestMapping(value = "/DeleteVocher", method = RequestMethod.POST, produces = "application/json")
    public void DeleteVocher(@RequestParam(value = "vouId", required = false) Long id)
    {
        hiposService.DeleteVocher(id);
    }


    @RequestMapping(value = "/deletecustomer", method = RequestMethod.POST, produces = "application/json")
    public void deletecustomer(@RequestParam(value = "customerId", required = false) Long id)
    {
        hiposService.deletecustomer(id);
    }


    @RequestMapping(value = "/deleteItem", method = RequestMethod.POST, produces = "application/json")
    public void deleteItem(@RequestParam(value = "itemId", required = false) Long id)
    {
        hiposService.deleteItem(id);
    }

    @RequestMapping(value = "/deleteAgent", method = RequestMethod.POST, produces = "application/json")
    public void deleteAgent(@RequestParam(value = "agentId", required = false) Long id)
    {
        hiposService.deleteAgent(id);
    }
    @RequestMapping(value = "/deleteCategory", method = RequestMethod.POST, produces = "application/json")
    public void deleteCategory(@RequestParam(value = "itemCategoryId", required = false) Long id)
    {
        hiposService.deleteCategory(id);
    }
    @RequestMapping(value = "/deleteCurrency", method = RequestMethod.POST, produces = "application/json")
    public void deleteCurrency(@RequestParam(value = "currencyId", required = false) Long id)
    {
        hiposService.deleteCurrency(id);
    }
    @RequestMapping(value="/deleteTableDataTemp",method = RequestMethod.GET)
    public String deleteTempTableData(@RequestParam("currTableName") String currTableName,
                                      @RequestParam("currTableId") String currTableId){
        return hiposService.deleteTempTableData(currTableName,currTableId);
    }
    @RequestMapping(value = "/deleteTable", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity deleteTable(@RequestBody TablesPosPojo details) {
        return ResponseEntity.status(200).body(hiposService.deleteTable(details));
    }

    @RequestMapping(value = "/getPaginatedState", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getPaginatedState(@RequestParam(value = "type", required = false) String type,
                                              @RequestParam(value = "searchText", required = false) String searchText,
                                              @RequestBody BasePojo basePojo) {
        return ResponseEntity.status(200).body(hiposService.getPaginatedState(type, basePojo, searchText));
    }
    @Transactional
    @RequestMapping(value="/getOnlineOrders",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getOnlineOrders(@RequestParam(value = "orderNo",required = false)String orderNo){
        List<RestaurantTokenRecord> restaurantTokenRecord = restaurantTokenRecordRepository.findByOrderNo(orderNo);
        List<Map> keyMapList = restaurantTempDataRepository.findBy(orderNo);
        for(Map obj:keyMapList){
            Customer customer=customerRepository.findAllByCustomerId(Long.parseLong(obj.get("customerId").toString()));
            obj.put("customerName",customer.getCustomerName());
        }
        if(restaurantTokenRecord.size()>0&&restaurantTokenRecord.get(restaurantTokenRecord.size()-1).getSiId()!=null){
            ObjectNode objectNode = new ObjectMapper().createObjectNode();
            objectNode.put("message","Invoice Done");
            return ResponseEntity.status(200).body(objectNode.toString());
        }
        return ResponseEntity.ok(keyMapList);
    }
    @RequestMapping(value="/getPrinterDeviceList", method=RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getPrinterDeviceList(){
        return ResponseEntity.ok(hiposService.getPrinterDeviceList());
    }

    @RequestMapping(value = "/getPaginatedItemList", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getPaginatedItemList(@RequestParam(value = "type", required = false) String type,
                                            @RequestParam(value = "searchText", required = false) String searchText,
                                            @RequestBody BasePojo basePojo) {
        return ResponseEntity.status(200).body(hiposService.getPaginatedItemList(type, basePojo, searchText));
    }

    @RequestMapping(value = "/getPaginatedAgentList", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getPaginatedAgentList(@RequestParam(value = "type", required = false) String type,
                                            @RequestParam(value = "searchText", required = false) String searchText,
                                            @RequestBody BasePojo basePojo) {
        return ResponseEntity.status(200).body(hiposService.getPaginatedAgentList(type, basePojo, searchText));
    }

    @RequestMapping(value = "/getPaginatedEmployeeList", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getPaginatedEmployeeList(@RequestParam(value = "type", required = false) String type,
                                                @RequestParam(value = "searchText", required = false) String searchText,
                                                @RequestBody BasePojo basePojo) {
        return ResponseEntity.status(200).body(hiposService.getPaginatedEmployeeList(type, basePojo, searchText));
    }
    @RequestMapping(value = "/synchronizeTable",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity synchronizeTable(@RequestBody List<TablesPosPojo> tablesPosPojos) throws Exception{
        return ResponseEntity.status(200).body(hiposService.synchronizeTable(tablesPosPojos));
    }
    @RequestMapping(value = "/synchronizeItem",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity synchronizeItem(@RequestBody List<ItemPojo> itemPojoList) throws Exception{
        return ResponseEntity.status(200).body(hiposService.synchronizeItem(itemPojoList));
    }
    @RequestMapping(value = "/synchronizeCategory",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity synchronizeCategory(@RequestBody List<CategoryPojo> categoryPojoList) throws Exception{
        return ResponseEntity.status(200).body(hiposService.SynchronizeCategory(categoryPojoList));
    }

    @RequestMapping(value = "/restdashboard",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity restdashboard(@RequestParam(value = "fromdate")String fromdate,
                                        @RequestParam(value = "todate")String  todate,@RequestParam(value = "locationId")String  locationId) throws Exception{
        SimpleDateFormat parseFormat =
                new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z ");
        Date startDate = parseFormat.parse(fromdate);
        Date toDate = parseFormat.parse(todate);
        Map<String,String> result=new HashMap<>();
        Gson gson=new Gson();
        result.put("totalSales",gson.toJson(hiposService.restdashboard(startDate,toDate)));
        result.put("top5Items",gson.toJson(hiposService.gettop5Items(startDate,toDate)));
        result.put("paymentType",gson.toJson(hiposService.getPaymentTypeTotal(startDate,toDate)));
        result.put("orderType",gson.toJson(hiposService.getOrderType(startDate,toDate)));
        result.put("runningSales",gson.toJson(hiposService.getRunningSales()));
        result.put("shiftList",gson.toJson(hiposService.shiftSales(startDate,toDate)));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/SaveDetails",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity SaveDetails(@RequestBody CompanyDto companyDto){
        return ResponseEntity.status(200).body(hiposService.SaveDetails(companyDto));
    }

    @RequestMapping(value = "/getCompanyList", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getCompanyList() {
        return ResponseEntity.status(200).body(hiposService.getCompanyList());
    }

    @RequestMapping(value = "/synchronizeCustomer",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity synchronizeCustomer(@RequestBody List<CustomerPojo> customerPojoList) throws Exception{
        return ResponseEntity.status(200).body(hiposService.synchronizeCustomer(customerPojoList));
    }
    @RequestMapping(value = "/syncCategoryAndItems", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity syncCategoryAndItems()throws Exception {
        hiposService.syncCategoryAndItems();
        return ResponseEntity.status(200).body(null);
    }
    @RequestMapping(value = "/syncTables", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity syncTables()throws Exception {
        hiposService.syncTables();
        return ResponseEntity.status(200).body(null);
    }
    @RequestMapping(value = "/syncEmployee", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity syncEmployee()throws Exception {
        hiposService.syncEmployee();
        return ResponseEntity.status(200).body(null);
    }
    @RequestMapping(value = "/syncCustomers", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity syncCustomers()throws Exception {
        hiposService.syncCustomers();
        return ResponseEntity.status(200).body(null);
    }
    @RequestMapping(value = "/syncTableConfig", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity syncTableConfig()throws Exception {
        hiposService.syncTableConfig();
        return ResponseEntity.status(200).body(null);
    }
    @RequestMapping(value = "/syncPaymentVoucherDetails", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity syncPaymentVoucherDetails()throws Exception {
        hiposService.syncPaymentVoucherDetails();
        return ResponseEntity.status(200).body(null);
    }
    @RequestMapping(value = "/syncPaymentmethod", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity syncPaymentmethod()throws Exception {
        hiposService.syncPaymentmethod();
        return ResponseEntity.status(200).body(null);
    }
    @RequestMapping(value = "/syncOrders", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity syncOrders()throws Exception {
        hiposService.syncCategoryAndItems();
        hiposService.syncTableConfig();
        hiposService.syncTables();
        hiposService.syncEmployee();
        hiposService.syncCustomers();
        hiposService.syncPaymentVoucherDetails();
        hiposService.syncPaymentmethod();
        hiposService.syncOrders();
        return ResponseEntity.status(200).body(null);
    };

    @RequestMapping(value = "/getCustomerList", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getCustomerList(@RequestParam(value = "searchText", required = false) String searchText) {
        return ResponseEntity.status(200).body(hiposService.getCustomerList(searchText));
    }

    @RequestMapping(value="/getAllOrderTypes",method=RequestMethod.GET)
    public ResponseEntity getAllOrderTypes(){
        return ResponseEntity.ok(hiposService.getAllOrderTypes());
    }
    @RequestMapping(value = "/saveAccountType", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity saveAccountType(@RequestBody AccountTypePojo accountTypePojo) {
        return ResponseEntity.status(200).body(hiposService.saveAccountType(accountTypePojo));
    }
    @RequestMapping(value = "/saveAccountGroup", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity saveAccountGroup(@RequestBody AccountGroupPojo accountGroupPojo) {
        return ResponseEntity.status(200).body(hiposService.saveAccountGroup(accountGroupPojo));
    }
    @RequestMapping(value = "/getPaginatedAcctGrpList", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getPaginatedAcctGrpList(@RequestParam(value = "type", required = false) String type,
                                                  @RequestParam(value = "searchText", required = false) String searchText,
                                                  @RequestBody BasePojo basePojo) {
        return ResponseEntity.status(200).body(hiposService.getPaginatedAcctGrpList(type, basePojo, searchText));
    }

    @RequestMapping(value = "/getPaginatedAcctTypeList", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getPaginatedAcctTypeList(@RequestParam(value = "type", required = false) String type,
                                                   @RequestParam(value = "searchText", required = false) String searchText,
                                                   @RequestBody BasePojo basePojo) {
        return ResponseEntity.status(200).body(hiposService.getPaginatedAcctTypeList(type, basePojo, searchText));
    }
    @RequestMapping(value = "/deleteAccountType", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity deleteLeave(@RequestBody AccountTypePojo details) {
        return ResponseEntity.status(200).body(hiposService.deleteAccountType(details));
    }
    @RequestMapping(value = "/deleteAccountGroup", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity deleteAccountGroup(@RequestBody AccountGroupPojo details) {
        return ResponseEntity.status(200).body(hiposService.deleteAccountGroup(details));
    }
    @RequestMapping(value = "/getAccountTypeList", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getAccountTypeList(@RequestParam(value = "searchText", required = false) String searchText) {
        return ResponseEntity.status(200).body(hiposService.getAccountTypeList(searchText));
    }
    @RequestMapping(value = "/deleteContact", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity deleteContact(@RequestBody OtherContactsDTO details) {
        return ResponseEntity.status(200).body(hiposService.deleteContact(details));
    }
    @RequestMapping(value = "/getContactsList", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getContactsList(@RequestParam(value = "searchText", required = false) String searchText) {
        return ResponseEntity.status(200).body(hiposService.getContactsList(searchText));
    }
    @RequestMapping(value = "/getContactList", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getContactList(@RequestParam(value = "type", required = false) String type,
                                         @RequestParam(value = "searchText", required = false) String searchText,
                                         @RequestBody BasePojo basePojo) {
        return ResponseEntity.status(200).body(hiposService.getContactList(type, basePojo, searchText));
    }
    @RequestMapping(value = "/saveContact", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity saveContact(@RequestBody OtherContactsDTO otherContactsDTO) {
        return ResponseEntity.status(200).body(hiposService.saveContact(otherContactsDTO));
    }
    @RequestMapping(value = "/saveOrUpDateChartOfAcc", produces = "application/json", method = RequestMethod.POST)
    public ResponseEntity saveOrUpDateChartOfAcc(@RequestBody PostChartOfAccountDto accountDto){
        List<ListChatOfAccountDto> listChatOfAccountDto = null;
        listChatOfAccountDto = hiposService.saveCreateChartAcc(accountDto);
        return ResponseEntity.status(HttpStatus.OK).body(listChatOfAccountDto);
    }

    @RequestMapping(value = "/editCreateChartOfAccount",method = RequestMethod.POST)
    public ResponseEntity editCreateChartOfAccount(@RequestParam(value = "accountid") Long accountId){
        return ResponseEntity.status(HttpStatus.OK).body(hiposService.editAccountMAsterObj(accountId));
    }
    @RequestMapping(value = "/editChartOfAcc", produces = "application/json", method = RequestMethod.POST)
    public ResponseEntity editChartOfAcc(@RequestBody PostChartOfAccountDto accountDto){
        AccountMaster listChatOfAccountDto = null;
        listChatOfAccountDto = hiposService.editChartOfAccounts(accountDto);
        return ResponseEntity.status(HttpStatus.OK).body(listChatOfAccountDto);
    }
    @RequestMapping(value = "/getAccountGroupList", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getAccountGroupList(@RequestParam(value = "searchText", required = false) String searchText) {
        return ResponseEntity.status(200).body(hiposService.getAccountGroupList(searchText));
    }
    @RequestMapping(value = "/getAccountMasterList", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getAccountMasterList(@RequestParam(value = "searchText", required = false) String searchText) {
        return ResponseEntity.status(200).body(hiposService.getAccountMasterList(searchText));
    }
    @RequestMapping(value = "/getAccountMasterListBasedOnReport", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getAccountMasterListBasedOnReport(@RequestParam(value = "searchText", required = false) String searchText) {
        return ResponseEntity.status(200).body(hiposService.getAccountMasterListBasedOnReport(searchText));
    }
    @RequestMapping(value = "/getFirstLevelAccountMaster", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getFirstLevelAccountMaster(@RequestParam(value = "accountType", required = false) String subjectName) {
        return ResponseEntity.status(200).body(hiposService.getFirstLevelAccountMaster(Long.parseLong(subjectName)));
    }
    @RequestMapping(value = "/getSecoundLevelAccount", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getSecoundLevelChartOfAccount(@RequestParam(value = "firstLevelId", required = false) String firstLevelId) {
        return ResponseEntity.status(200).body(hiposService.secoundLevelChartOfAccount(Long.parseLong(firstLevelId)));
    }
    @RequestMapping(value = "/getAccount",method = RequestMethod.GET,produces="application/json")
    public ResponseEntity getAccount(@RequestParam(value = "accountCode") String accountCode) {
        List<AccountMasterDTO> AccountDTOList = hiposService.retrieveAccountMasterList(accountCode);
        return ResponseEntity.status(200).body(AccountDTOList);
    }
    @RequestMapping(value = "/getTransactionDetails",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity getTransDetails(@RequestParam("OrderId") String orderId,
                                @RequestParam("PayID") String paymentId)throws Exception {
        AirPayDetails airpay =hiposService.getPosTransDetails(orderId, paymentId);
        return ResponseEntity.status(200).body(airpay);


    }
    @RequestMapping(value = "/saveNewPosConfig",method = RequestMethod.POST,produces="application/json",consumes = "application/json")
    public ResponseEntity saveNewPosConfig(@RequestBody AccountSetupPojo accountSetupDetails ) {
        AccountSetupPojo accountSetupDTO = null;
        accountSetupDTO = hiposService.createSaveNewPosConfigDetails(accountSetupDetails);
        return ResponseEntity.status(200).body(accountSetupDTO);
    }

    @RequestMapping(value = "/getConfigurationList",method = RequestMethod.POST,produces="application/json")
    public ResponseEntity getConfigurationList()  {
        AccountSetupPojo configurationList = hiposService.getConfigurationList();
        return ResponseEntity.status(200).body(configurationList);
    }
    @RequestMapping(value = "/getConfiguration",method = RequestMethod.POST,produces="application/json")
    public ResponseEntity getConfiguration()  {
        List<AccountSetupPojo> configurationList = hiposService.getConfiguration();
        return ResponseEntity.status(200).body(configurationList);
    }

    @RequestMapping(value = "/itemImport" ,method = RequestMethod.POST)
    public ResponseEntity itemImport(@RequestParam("myFile") MultipartFile uploadfiles) throws Exception {
        System.out.println(uploadfiles.getOriginalFilename());
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(uploadfiles.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                org.apache.poi.ss.usermodel.Row row = sheet.getRow(i);
                if(row==null)
                    break;
                if(row!=null) {
                    AddNewItemDTO pojo = new AddNewItemDTO();
                    Cell itemcode = row.getCell(0);
                    Cell itemname = row.getCell(1);
                    Cell desc = row.getCell(2);
                    Cell itemCat = row.getCell(3);
                    Category category=categoryRepository.findAllByItemCategoryName(itemCat.toString());
                    Cell itemtype = row.getCell(4);
                    Cell hsn = row.getCell(5);
                    Cell foodtype = row.getCell(6);
                    Cell status = row.getCell(7);
                    String salesprice = row.getCell(8).toString();
                    Cell productionitem = row.getCell(9);

                    pojo.setItemCode(itemcode==null?null:itemcode.toString());
                    pojo.setItemName(itemname==null?null:itemname.toString());
                    pojo.setItemDesc(desc==null?null:desc.toString());
                    pojo.setItemCategory(category);
                    pojo.setItemType(itemtype==null?null:itemtype.toString());
                    pojo.setHsnCode(hsn==null?null:new java.text.DecimalFormat("0").format(hsn.getNumericCellValue()));
                    pojo.setFoodtype(foodtype==null?null:foodtype.toString());
                    pojo.setItemStatus(status==null?null:status.toString());
                    pojo.setSalesPrice(Double.parseDouble(salesprice));
                    if(productionitem!=null) {
                        if (StringUtils.equalsIgnoreCase(productionitem.toString(), "true")) {
                            pojo.setProductionItem("true");
                        } else {
                            pojo.setProductionItem("");
                        }
                    }
                    saveNewItem(pojo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.OK);
    }


    @RequestMapping(value = "/saveCategoryImport" ,method = RequestMethod.POST)
    public ResponseEntity saveCategoryImport(@RequestParam("myFile") MultipartFile uploadfiles) throws Exception {
        System.out.println(uploadfiles.getOriginalFilename());
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(uploadfiles.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                org.apache.poi.ss.usermodel.Row row = sheet.getRow(i);
                if(row==null)
                    break;
                if(row!=null) {
                    CategoryPojo categoryPojo = new CategoryPojo();
                    String name = row.getCell(0).toString();
                    Cell desc = row.getCell(1);
                    String defaulttype = row.getCell(2).toString();
                    String status = row.getCell(3).toString();
                    categoryPojo.setItemCategoryName(name);
                    categoryPojo.setItemCategoryDesc(desc==null?null:desc.toString());
                    categoryPojo.setDefaultType(defaulttype==null?null:defaulttype.toString());
                    categoryPojo.setStatus(status==null?null:status.toString());
                    saveNewItemCategory(categoryPojo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.OK);
    }
    @RequestMapping(value = "/saveAgentImport" ,method = RequestMethod.POST)
    public ResponseEntity saveAgentImport(@RequestParam("myFile") MultipartFile uploadfiles) throws Exception {
        System.out.println(uploadfiles.getOriginalFilename());
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(uploadfiles.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                org.apache.poi.ss.usermodel.Row row = sheet.getRow(i);
                if(row==null)
                    break;
                if(row!=null) {
                    AgentPojo agentPojo = new AgentPojo();
                    Cell name = row.getCell(0);
                    Cell Email = row.getCell(1);
                    Cell Mobile = row.getCell(2);
                    Cell Address = row.getCell(3);
                    Cell Commission = row.getCell(4);
                    Cell EffectiveDate = row.getCell(5);
                    Cell GstinNo = row.getCell(6);
                    Cell Ecommerce = row.getCell(7);
                    Cell Status = row.getCell(8);
                    agentPojo.setAgentName(name==null?null:name.toString());
                    agentPojo.setEmail(Email==null?null:Email.toString());
                    agentPojo.setMobile(Mobile==null?null:new java.text.DecimalFormat("0").format(Mobile.getNumericCellValue()));
                    agentPojo.setAddress(Address==null?null:Address.toString());
                    agentPojo.setCommission(Commission==null?null: new BigDecimal(Commission.toString()));
                    DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                    if(EffectiveDate!=null) {
                        java.util.Date date1 = sdf.parse(EffectiveDate.toString());
                        agentPojo.setEffectiveDate(date1 == null ? null : date1);
                    }
                    agentPojo.setStatus(Status==null?null:Status.toString());
                    agentPojo.setGstinNo(GstinNo==null?null:new java.text.DecimalFormat("0").format(GstinNo.getNumericCellValue()));
                    agentPojo.setEcommerce(Ecommerce==null?null:Ecommerce.toString());
                    saveAgent(agentPojo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.OK);
    }
    @RequestMapping(value = "/saveEmployeeImport" ,method = RequestMethod.POST)
    public ResponseEntity saveEmployeeImport(@RequestParam("myFile") MultipartFile uploadfiles) throws Exception {
        System.out.println(uploadfiles.getOriginalFilename());
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(uploadfiles.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                org.apache.poi.ss.usermodel.Row row = sheet.getRow(i);
                if(row==null)
                    break;
                if(row!=null) {
                    EmployeePojo empPojo = new EmployeePojo();
                    Cell employeeName = row.getCell(0);
                    Cell employeeCode = row.getCell(1);
                    Cell incentives = row.getCell(2);
                    Cell employeeAddr = row.getCell(3);
                    Cell employeePhone = row.getCell(4);
                    Cell employeeDob = row.getCell(5);
                    Cell employeeDoj = row.getCell(6);
                    Cell gender = row.getCell(7);
                    Cell waiterPerson = row.getCell(8);
                    Cell deliveryPerson = row.getCell(9);
                    Cell status = row.getCell(10);

                    empPojo.setEmployeeName(employeeName ==null?null:employeeName.toString());
                    empPojo.setEmployeeCode(employeeCode==null?null:new java.text.DecimalFormat("0").format(employeeCode.getNumericCellValue()));
                    empPojo.setIncentives(incentives==null?null:incentives.toString());
                    empPojo.setEmployeeAddr(employeeAddr==null?null:employeeAddr.toString());
                    empPojo.setEmployeePhone(employeePhone==null?null:new java.text.DecimalFormat("0").format(employeePhone.getNumericCellValue()));
                    DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    if(employeeDob!=null) {
                        java.util.Date date1 = sdf.parse(employeeDob.toString());
                        empPojo.setEmployeeDOB(date1 == null ? null : date1);
                    }
                    if(employeeDoj!=null){
                        java.util.Date date = sdf.parse(employeeDoj.toString());
                        empPojo.setEmployeeDOJ(date == null ? null : date);
                    }
                    empPojo.setGender(gender==null?null:gender.toString());
                    if(waiterPerson!=null) {
                        if (StringUtils.equalsIgnoreCase(waiterPerson.toString(), "1.0")) {
                            empPojo.setWaiterFlag(true);
                        } else {
                            empPojo.setWaiterFlag(false);
                        }
                    }
                    if(deliveryPerson!=null) {
                        if (StringUtils.equalsIgnoreCase(deliveryPerson.toString(), "1.0")) {
                            empPojo.setDeliveryFlag(true);
                        } else {
                            empPojo.setDeliveryFlag(false);
                        }
                    }
                    empPojo.setStatus(status==null?null:status.toString());
                    saveEmployee(empPojo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/countryImportSave" ,method = RequestMethod.POST)
    public ResponseEntity countryImportSave(@RequestParam("myFile") MultipartFile uploadfiles) throws Exception {
        System.out.println(uploadfiles.getOriginalFilename());
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(uploadfiles.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                org.apache.poi.ss.usermodel.Row row = sheet.getRow(i);
                if(row==null)
                    break;
                if(row!=null) {
                    CountryPojo countryPojo = new CountryPojo();
                    String name = row.getCell(0).toString();
                    String status = row.getCell(1).toString();
                    countryPojo.setCountryName(name);
                    countryPojo.setStatus(status==null?null:status.toString());
                    saveCountry(countryPojo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.OK);
    }
    @RequestMapping(value = "/stateImportSave" ,method = RequestMethod.POST)
    public ResponseEntity stateImportSave(@RequestParam("myFile") MultipartFile uploadfiles) throws Exception {
        System.out.println(uploadfiles.getOriginalFilename());
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(uploadfiles.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                org.apache.poi.ss.usermodel.Row row = sheet.getRow(i);
                if(row==null)
                    break;
                if(row!=null) {
                    StatePojo statePojo = new StatePojo();
                    String country = row.getCell(0).toString();
                    String state = row.getCell(1).toString();
                    String status = row.getCell(2).toString();
                    statePojo.setCountryName(country);
                    statePojo.setStateName(state);
                    statePojo.setStatus(status==null?null:status.toString());
                    saveState(statePojo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.OK);
    }
    @RequestMapping(value = "/TableConfigImportSave" ,method = RequestMethod.POST)
    public ResponseEntity TableConfigImportSave(@RequestParam("myFile") MultipartFile uploadfiles) throws Exception {
        System.out.println(uploadfiles.getOriginalFilename());
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(uploadfiles.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                org.apache.poi.ss.usermodel.Row row = sheet.getRow(i);
                if(row==null)
                    break;
                if(row!=null) {
                    TableConfigPojo tableConfigPojo = new TableConfigPojo();
                    String name = row.getCell(0).toString();
                    Cell rows = row.getCell(1);
                    Cell columns = row.getCell(2);
                    String status = row.getCell(3).toString();
                    tableConfigPojo.setConfigurationname(name);
                    tableConfigPojo.setRowtableconfig(new Double(rows.toString()).intValue());
                    tableConfigPojo.setColumntableconfig(new Double(columns.toString()).intValue());
                    tableConfigPojo.setStatus(status==null?null:status.toString());
                    saveTableConfig(tableConfigPojo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/CurrencyImportSave" ,method =RequestMethod.POST)
    public ResponseEntity CurrencyImportSave(@RequestParam("myFile") MultipartFile uploadfiles) {
        System.out.println(uploadfiles.getOriginalFilename());
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(uploadfiles.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                Row row = sheet.getRow(i);
                if(row==null)
                    break;
                if(row!=null) {
                    Cell name = row.getCell(0);
                    Cell code = row.getCell(1);
                    Cell symbol = row.getCell(2);
                    Cell desc = row.getCell(3);
                    Cell status = row.getCell(4);
                    CurrencyPojo currencyDTO = new CurrencyPojo();
                    currencyDTO.setCurrencyCode(name == null ? null : name.toString());
                    currencyDTO.setCurrencyName(code == null ? null : code.toString());
                    currencyDTO.setCurrencySymbol(symbol == null ? null : symbol.toString());
                    currencyDTO.setCurrencySymbol(desc == null ? null : desc.toString());
                    currencyDTO.setStatus("Active");
//                    Currency currency = hiposService.getCurrencyNameObj(name.toString());
//                    if(currency!=null){
//                        currencyDTO.setCurrencyId(currency.getCurrencyId());
//                    }
                    hiposService.saveCurrency(currencyDTO);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.OK);
    }
    @RequestMapping(value = "/saveTableImport" ,method =RequestMethod.POST)
    public ResponseEntity saveTableImport(@RequestParam("myFile") MultipartFile uploadfiles) {
        System.out.println(uploadfiles.getOriginalFilename());
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(uploadfiles.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                Row row = sheet.getRow(i);
                if(row!=null) {
                    Cell tableName = row.getCell(0);
                    Cell configurationname = row.getCell(1);
                    Cell gridLocationH = row.getCell(2);
                    Cell gridLocationV = row.getCell(3);
                    Cell status = row.getCell(4);
                    TablesPosPojo tablesPosPojo = new TablesPosPojo();
                        tablesPosPojo.setTableName(tableName == null ? null : tableName.toString());
                        tablesPosPojo.setConfigurationname(configurationname == null ? null : configurationname.toString());
                        tablesPosPojo.setGridLocationH(gridLocationH == null ? null : (int)gridLocationH.getNumericCellValue());
                        tablesPosPojo.setGridLocationV(gridLocationV == null ? null :  (int)gridLocationV.getNumericCellValue());
                        tablesPosPojo.setStatus(status == null ? null : status.toString());
                        hiposService.saveTablesPos(tablesPosPojo);

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.OK);
    }
    @RequestMapping(value = "/payMethodImportSave" ,method =RequestMethod.POST)
    public ResponseEntity payMethodImportSave(@RequestParam("myFile") MultipartFile uploadfiles) {
        System.out.println(uploadfiles.getOriginalFilename());
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(uploadfiles.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                Row row = sheet.getRow(i);
                if(row==null)
                    break;
                if(row!=null) {
                    Cell name = row.getCell(0);
                    Cell desc = row.getCell(1);
                    Cell type = row.getCell(2);
                    Cell status = row.getCell(3);
                    Cell defaultType = row.getCell(4);
                    PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
                    paymentMethodDTO.setPaymentmethodName(name == null ? null : name.toString());
                    paymentMethodDTO.setPaymentmethodDescription(desc == null ? null : desc.toString());
                    paymentMethodDTO.setPaymentmethodType(type == null ? null : type.toString());
                    paymentMethodDTO.setDefaultType(defaultType == null ? null :defaultType.toString());
                    paymentMethodDTO.setStatus(status == null ? null :status.toString());
//
                    hiposService.savePaymentmethod(paymentMethodDTO);                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.OK);
    }
    @RequestMapping(value = "/saveContactImport" ,method = RequestMethod.POST)
    public ResponseEntity saveContactImport(@RequestParam("myFile") MultipartFile uploadfiles) throws Exception {
        System.out.println(uploadfiles.getOriginalFilename());
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(uploadfiles.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                org.apache.poi.ss.usermodel.Row row = sheet.getRow(i);
                if(row==null)
                    break;
                if(row!=null) {
                    OtherContactsDTO contactsDTO = new OtherContactsDTO();
                    Cell name = row.getCell(0);
                    Cell gstn = row.getCell(1);
                    Cell country = row.getCell(2);
                    Cell state = row.getCell(3);
                    Cell email = row.getCell(4);
                    Cell contact = row.getCell(5);
                    Cell address = row.getCell(6);
                    Cell personIncharge = row.getCell(7);
                    Cell status = row.getCell(8);
                    Cell bankname = row.getCell(9);
                    Cell accountNo = row.getCell(10);
                    Cell panNo = row.getCell(11);
                    Cell branch = row.getCell(12);
                    Cell ifsc = row.getCell(13);
                    Cell website = row.getCell(14);

                    contactsDTO.setFullName(name==null?null:name.toString());
                    contactsDTO.setGstCode(gstn==null?null:gstn.toString());
                    contactsDTO.setCountry(country==null?null:country.toString());
                    contactsDTO.setState(state==null?null:state.toString());
                    contactsDTO.setEmail(email==null?null:email.toString());
                    contactsDTO.setContactNumber(contact==null?null:contact.toString());
                    contactsDTO.setAddress(address==null?null:address.toString());
                    contactsDTO.setPersonIncharge(personIncharge==null?null:personIncharge.toString());
                    contactsDTO.setStatus(status==null?null:status.toString());
                    contactsDTO.setBankName(bankname==null?null:bankname.toString());
                    contactsDTO.setAccountNo(accountNo==null?null:accountNo.toString());
                    contactsDTO.setPanNO(panNo==null?null:panNo.toString());
                    contactsDTO.setBranchName(branch==null?null:branch.toString());
                    contactsDTO.setIFSCCode(ifsc==null?null:ifsc.toString());
                    contactsDTO.setWebsite(website==null?null:website.toString());
                    saveContact(contactsDTO);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.OK);
    }
    @RequestMapping(value = "/customerImportSave" ,method =RequestMethod.POST)
    public ResponseEntity customerImportSave(@RequestParam("myFile") MultipartFile uploadfiles) throws Exception {
        System.out.println(uploadfiles.getOriginalFilename());
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(uploadfiles.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                Row row = sheet.getRow(i);
                if(row!=null) {
                    CustomerPojo customerPojo = new CustomerPojo();
                    DataFormatter df = new DataFormatter();
                    Cell CustomerName = row.getCell(0);
                    Cell GSTN = row.getCell(1);
                    Cell State = row.getCell(2);
                    Cell Email = row.getCell(3);
                    Cell Contact = row.getCell(4);
                    Cell Address = row.getCell(5);
                    Cell Pincode = row.getCell(6);
                    Cell PersonIncharge = row.getCell(7);
                    Cell Country = row.getCell(8);
                    Cell Currency = row.getCell(9);
                    Cell Status = row.getCell(10);
                    Cell Bank = row.getCell(11);
                    Cell AccNo  = row.getCell(12);
                    Cell PAN  = row.getCell(13);
                    Cell BankBranch  = row.getCell(14);
                    Cell IFSC  = row.getCell(15);
                    Cell Website  = row.getCell(16);
                    Cell CreditTerm  = row.getCell(17);
                    Cell CreditLimit  = row.getCell(18);

                    customerPojo.setCustomerName(CustomerName==null?null:CustomerName.toString());
                    customerPojo.setGstCode(GSTN==null?null:GSTN.toString());
                    State state = stateRepository .findAllByStateName(State.toString());
                    customerPojo.setStateId(State==null?null:state.getStateId());
                    customerPojo.setEmail(Email==null?null:Email.toString());
                    customerPojo.setCustomerNumber(Contact==null?null:new java.text.DecimalFormat("0").format(Contact.getNumericCellValue()));
                    customerPojo.setAddress(Address==null?null:Address.toString());
                    customerPojo.setPincode(Pincode==null?null:new java.text.DecimalFormat("0").format(Pincode.getNumericCellValue()));
                    customerPojo.setPersonIncharge(PersonIncharge==null?null:PersonIncharge.toString());
                    Country country = countryRepository.findAllByCountryName(Country.toString());
                    customerPojo.setCountry(Country==null?null:country.getCountryId());
                    Currency currency = currencyRepository.findAllByCurrencyName(Currency.toString());
                    customerPojo.setCurrencyId(Currency==null?null:currency.getCurrencyId());
                    customerPojo.setStatus(Status==null?null:Status.toString());
                    customerPojo.setBankName(Bank==null?null:Bank.toString());
                    customerPojo.setAccountNo(AccNo==null?null:AccNo.toString());
                    customerPojo.setPanNo(PAN==null?null:PAN.toString());
                    customerPojo.setBranchName(BankBranch==null?null:BankBranch.toString());
                    customerPojo.setiFSCCode(IFSC==null?null:IFSC.toString());
                    customerPojo.setWebsite(Website==null?null:Website.toString());
                    customerPojo.setCreditedTerm(CreditTerm==null?null:CreditTerm.toString());
                    customerPojo.setCreditedLimit(CreditLimit==null?null:CreditLimit.toString());
                    saveCustomer(customerPojo);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.OK);
    }
    @RequestMapping(value = "/getCountryState", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getCountryState(@RequestParam(value = "countryId", required = false) Long countryName) {
        return ResponseEntity.status(200).body(hiposService.getCountryState(countryName));
    }
    @RequestMapping(value = "/saveVoucher", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity saveVoucher(@RequestBody PaymentVoucherPojo paymentVoucherPojo)throws Exception {
        return ResponseEntity.status(200).body(hiposService.saveVoucher(paymentVoucherPojo));
    }
    @RequestMapping(value = "/getPaginatedVoucherList", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getPaginatedVoucherList(@RequestParam(value = "type", required = false) String type,
                                                   @RequestParam(value = "searchText", required = false) String searchText,
                                                   @RequestBody BasePojo basePojo) {
        return ResponseEntity.status(200).body(hiposService.getPaginatedVoucherList(type, basePojo, searchText));
    }
    @RequestMapping(value="/saveSMSDetails",method= RequestMethod.POST,produces="application/json")
    public ResponseEntity saveSMSDetails(@RequestBody SmsServerDto smsServerDto ){
        return ResponseEntity.status(200).body(hiposService.savesms(smsServerDto));
    }
    @RequestMapping(value = "/getSMSList", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity getSMSList(@RequestParam(value = "SearchText") String searchText) {
        return ResponseEntity.status(200).body(hiposService.getsmslist(searchText));
    }
}