package com.daisy.exceltest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.daisy.utils.ExcelExport;

/*import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.daisy.utils.ExcelExport;
import com.dz.base.PageModel;
import com.dz.entity.Order;
import com.dz.utils.PowerControlUtil;
import com.dz.utils.alipay.UtilDate;*/


public class TestExcelExport{
    
	/**
	 * 测试导出大量数据到excel
	 * @param args
	 * @throws IOException
	 */
	public static void main( String[] args ) throws IOException{
    	long start = new Date().getTime();
    	ExcelExport excelExport=new ExcelExport();
    	String uuid = null;
    	for(int i = 0;i < 2000000;i++) {
    		uuid=UUID.randomUUID().toString();
    		excelExport.addRow(uuid,uuid,uuid,uuid,uuid,uuid,uuid,uuid,uuid,uuid);
		}
    	System.out.println(new Date().getTime()-start);
    	
    	//main是写到本地，但是做项目时要从服务器下载
    	FileOutputStream out = 
    			new FileOutputStream("C:/Users/PC/Downloads/sxssf.xlsx");
    	excelExport.OutPut(out);
        System.out.println(new Date().getTime()-start);
    }
	
	//@RequestMapping("/exportOrderListForCallCenter")
	//@ResponseBody
/*	public void exportOrderListForCallCenter(HttpServletRequest request,HttpServletResponse response){
		long start=new Date().getTime();
		List<Integer> cityIds = PowerControlUtil.getCityIds(userRoleService,roleAreaService);
		//1.得到页面传来的参数
		String businessType=request.getParameter("businessType");//业务类型 0出租车  1约租车
		String callbackPhone=request.getParameter("callbackPhone");//回拨电话
		String phone = request.getParameter("carUserPhone");// 司机电话
		String startDate = request.getParameter("startDate");// 查询开始时间
		String endDate = request.getParameter("endDate");// 查询结束时间
		String customerPhone = request.getParameter("customerPhone");// 乘客电话
		String customerName = request.getParameter("customerName");// 乘客姓名
		String userName = request.getParameter("userName");
		if(StringUtils.isNotEmpty(customerName)){
			customerName = new String(customerName)+"";
		}
		String orderId = request.getParameter("orderId");// 订单号
		String carType = request.getParameter("carType");// 车辆类型  0舒适型，1商务型，2豪华型
		String orderType = request.getParameter("orderType");//订单类型 0：app叫车，1：callcenter，2：app预约，3：callcenter预约
		String carNumber = request.getParameter("carNumber");// 车牌号码
		if(StringUtils.isNotEmpty(carNumber)){
			carNumber = new String(carNumber)+"";
		}
		String status = request.getParameter("status");// 状态
		String startPlace=request.getParameter("startPlace");//出发地点
		String endPlace=request.getParameter("endPlace");//目标地点
		String cityId = request.getParameter("cityId");
		if(StringUtils.isNotEmpty(startPlace)){
			startPlace = new String(startPlace)+"";
		}
		if(StringUtils.isNotEmpty(endPlace)){
			endPlace = new String(endPlace)+"";
		}
		
		//将页面得到数据放到params中
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotEmpty(businessType)){
			params.put("businessType", Integer.parseInt(businessType));
		}
		if (StringUtils.isNotEmpty(phone)) {
			params.put("phone", phone);
		}
		if (StringUtils.isNotEmpty(startDate)) {
			params.put("startDate", startDate);
		}
		if (StringUtils.isNotEmpty(endDate)) {
			params.put("endDate", endDate);
		}
		if (StringUtils.isNotEmpty(customerPhone)) {
			params.put("customerPhone", customerPhone);
		}
		if (StringUtils.isNotEmpty(callbackPhone)) {
			params.put("callbackPhone", callbackPhone);
		}
		if (StringUtils.isNotEmpty(customerName)) {
			params.put("customerName", customerName);
		}
		if (StringUtils.isNotEmpty(orderId)) {
			params.put("orderId", orderId);
		}
		if (StringUtils.isNotEmpty(carType)) {
			params.put("carType", carType);
		}
		if (StringUtils.isNotEmpty(orderType)) {
			params.put("orderType", orderType);
		}
		if (StringUtils.isNotEmpty(status)) {
			params.put("status", status);
		}
		if (StringUtils.isNotEmpty(carNumber)) {
			params.put("carNumber", carNumber);
		}
		if (StringUtils.isNotEmpty(startPlace)) {
			params.put("startPlace", startPlace);
		}
		if (StringUtils.isNotEmpty(endPlace)) {
			params.put("endPlace", endPlace);
		}
		if(StringUtils.isNotBlank(cityId)){
			params.put("cityId", cityId);
		}else{
			if(cityIds!=null && !cityIds.isEmpty()){
				params.put("cityIds", cityIds);
			}else{
				params.put("cityIds", null);
			}
		}
		if(StringUtils.isNotEmpty(userName)){
			params.put("userName", userName);
		}
		
		//2.设置excel标题，文件名等
		String[] title = { "订单号", "乘客电话", "司机编号", "司机电话", "司机类型","车牌号","车型","用车时间",
				"出发地点","目的地","完成时间","订单渠道","状态","备注","订单类型","更新时间","操作人","客服工号","取消原因" };// 表头（列名）		
		String excelName = "调度中心-订单查询"+ UtilDate.getDate() + ".xlsx";
		

		//3.标题写入excel
    	ExcelExport excelExport = new ExcelExport();
    	excelExport.addRow(title);
		
    	
    	//4.数据写入excel
		int pageSize=200;//每页数据量
		int totalPage;//总页数
		int currentPage = 1;//当前页
		boolean flag=true;
		List<Order> queryResults = null;
		
		while(flag){
			PageModel page = new PageModel(pageSize,currentPage);//初始化，每页几行，当前页
			page = orderService.searchOrderForCallCenter(params,page);
			queryResults = page.getList();
			totalPage=page.getTotalPage();//后加
			
			if (queryResults != null && queryResults.size() > 0) {
				for(int i=0;i<queryResults.size();i++){
					if (queryResults.get(i)!=null) {
						String[] strList = new String[title.length];
						strList[0] = queryResults.get(i).getOrderId();//订单号orderId
						strList[1] = queryResults.get(i).getCustomerPhone();//乘客电话customerPhone
						if(queryResults.get(i).getCarId()!=null){
							strList[2] = queryResults.get(i).getCarId().toString();//司机编号carId  integer
						}else{
							strList[2] = "";
						}
						if(queryResults.get(i).getCarUserPhone()!=null){
							strList[3] = queryResults.get(i).getCarUserPhone().getPhone();//司机电话carUserPhone  CarUser
						}else{
							strList[3] = "";
						}
						if(queryResults.get(i).getBusinessType() != null){
							if("0".equals(queryResults.get(i).getBusinessType())){
								strList[4] = "出租";
							}else{
								strList[4] ="约租";
							}
						}else{
							strList[4] ="";
						}
						if(queryResults.get(i).getCarUserPhone()!=null){
							strList[5] = queryResults.get(i).getCarUserPhone().getCarNumber();//车牌号 integer
						}else{
							strList[5]="";
						}
						if(queryResults.get(i).getCarType() != null){
							StringBuffer car_type=new StringBuffer();
							String[] car_types = queryResults.get(i).getCarType().split(";");
							for(int j=0;j<car_types.length;j++){
								if("0".equals(car_types[j])){
									car_type.append("舒适型,");
								}
								if("1".equals(car_types[j])){
									car_type.append("商务型,");
								}
								if("2".equals(car_types[j])){
									car_type.append("豪华型,");
								}
							}
							strList[6]=car_type.toString();
						}else{
							strList[6]="舒适型;商务型;豪华型";
						}
						if(queryResults.get(i).getCallDate()!=null){
							Date callDate = queryResults.get(i).getCallDate();
							SimpleDateFormat OperateDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
							strList[7] = OperateDate.format(callDate);
						}else{
							strList[7] ="";
						}
						strList[8] = queryResults.get(i).getStartPlace();//出发地点startPlace
						strList[9] = queryResults.get(i).getEndPlace();//目的地	endPlace				
						if(queryResults.get(i).getFinishDate()!=null){
							Date finishDate = queryResults.get(i).getFinishDate();
							SimpleDateFormat OperateDate2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							strList[10] = OperateDate2.format(finishDate);
						}else{
							strList[10] = "";
						}
						if(queryResults.get(i).getOrderSource() == 4 && queryResults.get(i).getCarUserChannel() == 0){
							strList[11]="首约订单，大众司机";
						}else if(queryResults.get(i).getOrderSource() != 4 &&  queryResults.get(i).getCarUserChannel() == 1){
							strList[11]="大众订单，首约司机";
						}else if(queryResults.get(i).getOrderSource() != 4 &&  queryResults.get(i).getCarUserChannel() == 0){
							strList[11]="大众订单，大众司机";
						}
						if(queryResults.get(i).getStatus()==0){
							strList[12]="调派中";
						}else if(queryResults.get(i).getStatus()==1){
							strList[12]="接单中";
						}else if(queryResults.get(i).getStatus()==2){
							strList[12]="已完成";
						}else if(queryResults.get(i).getStatus()==3){
							strList[12]="已取消";
						}else if(queryResults.get(i).getStatus()==4){
							strList[12]="放空";
						}else if(queryResults.get(i).getStatus()==5){
							strList[12]="无供";
						}else if(queryResults.get(i).getStatus()==6){
							strList[12]="超时";
						}else if(queryResults.get(i).getStatus()==7){
							strList[12]="指派中";
						}else if(queryResults.get(i).getStatus()==8){
							strList[12]="待支付";
						}else if(queryResults.get(i).getStatus()==9){
							strList[12]="到达目的 未支付";
						}else if(queryResults.get(i).getStatus()==10){
							strList[12]="司机取消";
						}else if(queryResults.get(i).getStatus()==11){
							strList[12]="计价中";
						}else if(queryResults.get(i).getStatus()==12){
							strList[12]="计价完成";
						}else if(queryResults.get(i).getStatus()==13){
							strList[12]="强派拒绝";
						}else{
							strList[12]="全部";
						}
						strList[13] = queryResults.get(i).getBakstr2();//备注bakstr2
						if(queryResults.get(i).getOrderType()==0){
							strList[14]="APP实时";
						}else if(queryResults.get(i).getOrderType()==1){
							strList[14]="正常计费实时";
						}else if(queryResults.get(i).getOrderType()==2){
							strList[14]="APP预约";
						}else if(queryResults.get(i).getOrderType()==3){
							strList[14]="正常计费预约";
						}else if(queryResults.get(i).getOrderType()==4){
							strList[14]="协议价订单";
						}else if(queryResults.get(i).getOrderType()==5){
							strList[14]="接送机预约";
						}else if(queryResults.get(i).getOrderType()==6){
							strList[14]="首汽订单";
						}else if(queryResults.get(i).getOrderType()==7){
							strList[14]="接送机实时";
						}else{
							strList[14]="全部";
						}
						if(queryResults.get(i).getUpdateDate()!=null){
							Date updateDate = queryResults.get(i).getUpdateDate();
							SimpleDateFormat OperateDate3=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							strList[15] = OperateDate3.format(updateDate);
						}else{
							strList[15] = "";
						}
						strList[16] = queryResults.get(i).getUpdateName();//操作人updateName
						if(queryResults.get(i).getCreater()!=null){
							strList[17] = queryResults.get(i).getCreater().getUsername();//客服工号creater  User
						}else{
							strList[17]="";
						}
						strList[18] = queryResults.get(i).getBakstr1();//取消原因bakstr1
						
						excelExport.addRow(strList);
					}
					
				}//得到当前页数据
				
			}else{
				flag=false;
			}//end queryResults
			
			if(currentPage==totalPage){
				flag=false;
			}
			currentPage++;
    	}//end while(flag)
		
    	//6.结束
    	FileOutputStream out;
		try {
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-disposition", 
					"attachment;filename=" 
			+ URLEncoder.encode(excelName,"UTF8"));
			
			OutputStream output= response.getOutputStream();
			excelExport.OutPut(output);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
}
