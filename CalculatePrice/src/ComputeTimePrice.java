
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class ComputeTimePrice {
	
	static Integer freeMileage=0;//套餐里程
	static Integer freeTime=0;//套餐时长
	static Integer free_over_time=0;//免超时时长
	static Double unitPrice=0.00;//超里程费
	static Double normalPrice=0.50;//平峰时长费(？)
	static Double peakPrice=1.00;//高峰时长费(？)
	static Double extraWait=1.50;//超时等待费(？)
	static Integer overDistance;//回空里程
	static Double overDistanceUnitPrice;//回空里程附加费
	static Double minitePriceNight;//夜间时长附加费
	static Integer nightMinute=0;//夜间时长
	static Integer callTimes=0;//第几次调用高�?

	public static void main(String args[]) throws ParseException{
		freeMileage = 5;//套餐里程
		freeTime = 10;//套餐时长
		free_over_time = 30+freeTime;//免超时时长
		unitPrice = 3.00;//超里程费
/////////////////////////////////////////////////////////////	//////	/////
		normalPrice=0.50;//平峰时长费
		peakPrice=1.00;//高峰时长费
	    extraWait = 1.50;//超时等待费
		minitePriceNight = 0.50;//夜间时长附加费
///////////////////////////////////////////////////////////		//////////////////
		Double mileage = 0.00;//预估里程（m）
		String phone = "";//登录的手机号
		Integer usage = 0;//用车类型0:出租;1:专车;2:接送机
		
		String date = "2017-05-06 05:10:48";//上车时间
		Integer time = 27*60;//预估时间(sec)分钟

		Map map =calPrice(date,time,mileage);//总价钱	
		Integer order_Type = null;// 订单类型（0.实时;1.预约）
		
		System.out.println("sum:"+map.get("total_price"));
		System.out.println("nightMinute:"+map.get("nightMinute"));
	}

	
	
	/**
	 * 预估专车时长
	 * @param sysDict
	 * @param priceDto
	 * @return
	 * @throws ParseException
	 */
	public static Map<String,Object> calPrice(String date, Integer time, Double mileage) throws ParseException {
		Date startDate = new Date();//上车时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		startDate = sdf.parse(date);//Thu May 04 05:51:48 CST 2017	
		Calendar calendar = Calendar.getInstance();  
		calendar.setTime(startDate);
		int startHour = calendar.get(Calendar.HOUR_OF_DAY);//上车时间，所在小时
		double price=0.00;
		double total_price=0.0;
		Date endDate = new Date(startDate.getTime()+time*1000*60);//用车结束时间
		System.out.println("endDate"+endDate);
		boolean first_call=true;//是否第一次计费
		Integer free_time_remain=0;//
		Integer free_over_time_remain=0;//

		if(startHour >=5 && startHour<7){
			total_price=from_5_to_7(time,startDate,endDate,price,first_call,free_time_remain,free_over_time_remain);
		}else if(startHour >=7 && startHour<10){
			total_price=from_7_to_10(time,startDate,endDate,price,first_call,free_time_remain,free_over_time_remain);
		}else if(startHour >=10 && startHour<16){
			total_price=from_10_to_16(time,startDate,endDate,price,first_call,free_time_remain,free_over_time_remain);
		}else if(startHour >=16 && startHour<22){
			total_price=from_16_to_22(time,startDate,endDate,price,first_call,free_time_remain,free_over_time_remain);
		}else if(startHour >=22 && startHour<23){
			total_price=from_22_to_23(time,startDate,endDate,price,first_call,free_time_remain,free_over_time_remain);
		}else if(startHour >=23 || startHour<5){
			total_price=from_23_to_5(time,startDate,endDate,price,first_call,free_time_remain,free_over_time_remain);
		}
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("total_price", total_price);
		map.put("nightMinute", nightMinute);
	
		System.out.println("startDate:"+startDate);
		System.out.println("time:"+time);
		System.out.println("total_price:"+total_price);
		System.out.println("nightMinute:"+nightMinute);
		return map;
	}
	
	/**
	 * 预估专车时长
	 * @param sysDict
	 * @param priceDto
	 * @return
	 * @throws ParseException
	 */
	public static Map<String,Object> calPrice(String date, Integer time) throws ParseException {
		//String date = priceDto.getDate();//上车时间
		//Integer time = priceDto.getTime()/(60);//预估时间(sec)分钟
		
		Date startDate = new Date();//上车时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		startDate = sdf.parse(date);//Thu May 04 05:51:48 CST 2017	
		Calendar calendar = Calendar.getInstance();  
		calendar.setTime(startDate);
		int startHour = calendar.get(Calendar.HOUR_OF_DAY);//上车时间，所在小�?
		double price=0.00;
		double total_price=0.0;
		Date endDate = new Date(startDate.getTime()+time*1000*60);//用车结束时间
		boolean first_call=true;//是否第一次计�?
		Integer free_time_remain=0;//
		Integer free_over_time_remain=0;//

		if(startHour >=5 && startHour<7){
			total_price=from_5_to_7(time,startDate,endDate,price,first_call,free_time_remain,free_over_time_remain);
		}else if(startHour >=7 && startHour<10){
			total_price=from_7_to_10(time,startDate,endDate,price,first_call,free_time_remain,free_over_time_remain);
		}else if(startHour >=10 && startHour<16){
			total_price=from_10_to_16(time,startDate,endDate,price,first_call,free_time_remain,free_over_time_remain);
		}else if(startHour >=16 && startHour<22){
			total_price=from_16_to_22(time,startDate,endDate,price,first_call,free_time_remain,free_over_time_remain);
		}else if(startHour >=22 && startHour<23){
			total_price=from_22_to_23(time,startDate,endDate,price,first_call,free_time_remain,free_over_time_remain);
		}else if(startHour >=23 || startHour<5){
			total_price=from_23_to_5(time,startDate,endDate,price,first_call,free_time_remain,free_over_time_remain);
		}
		
		Map<String,Object> map=new HashMap();
		map.put("total_price", total_price);
		map.put("nightMinute", nightMinute);
		return map;
	}
	
	

	/**
	 * 平峰  5点到7�?
	 * @param remain_time 剩余分钟
	 * @param start_date  本时段开始计费时�?
	 * @param end_date    结束计费时间
	 * @param accu_price  累加费用：第�?次调用传�?0，被另外函数调用传�?�累加钱�?
	 * @param first_call  是否第一次调�?
	 * @return
	 */
	private static double from_5_to_7(Integer remain_time,Date start_date,Date end_date,double accu_price,
			boolean first_call,Integer free_time_remain,Integer free_over_time_remain){
		if(first_call){
			first_call=false;//把第�?次调用置�?
			if(remain_time<=freeTime){//免费
				return 0;
			}else if(remain_time<=free_over_time){//平峰�?40分钟�?
				if(end_date.getHours()>=7){//夸时间段�?
					Integer this_cost_time =(7-start_date.getHours()-1)*60+(60-start_date.getMinutes());//本时间段耗时
					remain_time=remain_time-this_cost_time;
					free_over_time_remain=free_over_time-this_cost_time;

					if(this_cost_time>freeTime){//在本段收�?
						accu_price=(this_cost_time-freeTime)*normalPrice;
					}else{//在本段免�?
						free_time_remain=freeTime-this_cost_time;
					}
					return from_7_to_10(remain_time,start_date,end_date,accu_price,false,free_time_remain,free_over_time_remain);
				}else{//没有跨时间段
					return (remain_time-freeTime)*normalPrice;
				}
					
			}else{//平峰�?+超时等待�?>40分钟
				//没有跨天且都在本时间�?
				if (end_date.getHours()<7 && end_date.getDate()==start_date.getDate()){
					return (remain_time - freeTime)*normalPrice+(remain_time-free_over_time)*extraWait;	
				}else{ //跨时间段或
					Integer this_cost_time =(7-start_date.getHours()-1)*60+(60-start_date.getMinutes());
					//accu_price=(this_cost_time-freeTime)*normalPrice+(this_cost_time-free_over_time)*extraWait;
					
					if(this_cost_time>free_over_time){//>40   去了freeTime
						accu_price=(this_cost_time-freeTime)*normalPrice+(this_cost_time-free_over_time)*extraWait;
					}else if(this_cost_time>freeTime&&this_cost_time<free_over_time){// >10 && <40 
						accu_price=(this_cost_time-freeTime)*normalPrice;
						free_over_time_remain=free_over_time-this_cost_time;

					}else{//<10  本段免费
						free_time_remain=freeTime-this_cost_time;
						free_over_time_remain=free_over_time-this_cost_time;
					}
					
					remain_time=remain_time-this_cost_time;//剩余计费时间
					
					return from_7_to_10(remain_time,start_date,end_date,accu_price,false,free_time_remain,free_over_time_remain);
				}
			}
			
		}else{//另一个函数调用到此，必然�?5点开始计�?
			Integer current_total_time =(7-5)*60;
			if(remain_time<current_total_time){//在本时间段跑
				
				if(remain_time<=free_over_time_remain){
					return (remain_time-free_time_remain)*normalPrice+accu_price;
				}else{
					return (remain_time-free_time_remain)*normalPrice+accu_price+
							(remain_time-free_over_time_remain)*extraWait;
				}
				
			}else{
				accu_price=accu_price+(current_total_time-free_time_remain)*normalPrice+
						(current_total_time-free_over_time_remain)*extraWait;
				
				free_over_time_remain=0;
				free_time_remain=0;
				
				remain_time=remain_time-current_total_time;
				return from_7_to_10(remain_time,start_date,end_date,accu_price,false,free_time_remain,free_over_time_remain);
			}	
		}
	}
	
	/**
	 * 高峰7点到10�?
	 * @param remain_time 剩余分钟
	 * @param start_date  本时段开始计费时�?
	 * @param end_date    结束计费时间
	 * @param accu_price  累加费用：第�?次调用传�?0，被另外函数调用传�?�累加钱�?
	 * @param first_call  是否第一次调�?
	 * @return
	 */
	private static double from_7_to_10(Integer remain_time,Date start_date,Date end_date,double accu_price,
			boolean first_call,Integer free_time_remain,Integer free_over_time_remain){		
		if(first_call){
			first_call=false;//把第�?次调用置�?
			if(remain_time<=freeTime){//免费
				return 0;
			}else if(remain_time<=free_over_time){//平峰�?
				if(end_date.getHours()>=10){//夸时间段�?
					Integer this_cost_time =(10-start_date.getHours()-1)*60+(60-start_date.getMinutes());//本时间段耗时
					remain_time=remain_time-this_cost_time;
					free_over_time_remain=free_over_time-this_cost_time;

					if(this_cost_time>freeTime){
						accu_price=(this_cost_time-freeTime)*peakPrice;
					}else{
						free_time_remain=freeTime-this_cost_time;
					}
					return from_10_to_16(remain_time,start_date,end_date,accu_price,false,free_time_remain,free_over_time_remain);
				}else{//没有跨时间段
					return (remain_time-freeTime)*peakPrice;
				}
					
			}else{//平峰�?+超时等待�?
				//没有跨天且都在本时间�?
				if (end_date.getHours()<10 && end_date.getDate()==start_date.getDate()){
					return (remain_time - freeTime)*peakPrice+(remain_time-free_over_time)*extraWait;
					
				}else{ //跨时间段或�?�跨�?
					Integer this_cost_time =(10-start_date.getHours()-1)*60+(60-start_date.getMinutes());
					//accu_price=(this_cost_time-freeTime)*peakPrice+(this_cost_time-free_over_time)*extraWait;
					
					if(this_cost_time>free_over_time){//>40   去了freeTime
						accu_price=(this_cost_time-freeTime)*peakPrice+(this_cost_time-free_over_time)*extraWait;
					}else if(this_cost_time>freeTime&&this_cost_time<free_over_time){// >10 && <40
						accu_price=(this_cost_time-freeTime)*peakPrice;
						free_over_time_remain=free_over_time-this_cost_time;

					}else{//<10
						free_time_remain=freeTime-this_cost_time;
						free_over_time_remain=free_over_time-this_cost_time;
					}
					
					remain_time=remain_time-this_cost_time;//剩余计费时间
					
					return from_10_to_16(remain_time,start_date,end_date,accu_price,false,free_time_remain,free_over_time_remain);
				}
			}
			
		}else{//另一个函数调用到此，必然�?7点开始计�?
			Integer current_total_time =(10-7)*60;
			if(remain_time<current_total_time){//在本时间段跑�?
				
				if(remain_time<=free_over_time_remain){
					return (remain_time-free_time_remain)*peakPrice+accu_price;
				}else{
					return (remain_time-free_time_remain)*peakPrice+accu_price+
							(remain_time-free_over_time_remain)*extraWait;
				}
				
			}else{
				accu_price=accu_price+(current_total_time-free_time_remain)*peakPrice+
						(current_total_time-free_over_time_remain)*extraWait;
				
				free_over_time_remain=0;
				free_time_remain=0;
				
				remain_time=remain_time-current_total_time;
				return from_10_to_16(remain_time,start_date,end_date,accu_price,false,free_time_remain,free_over_time_remain);
			}
					
		}
		
	}
	
	/**
	 * 平峰10点到16�?
	 * @param remain_time 剩余分钟
	 * @param start_date  本时段开始计费时�?
	 * @param end_date    结束计费时间
	 * @param accu_price  累加费用：第�?次调用传�?0，被另外函数调用传�?�累加钱�?
	 * @param first_call  是否第一次调�?
	 * @return
	 */
	private static double from_10_to_16(Integer remain_time,Date start_date,Date end_date,
			double accu_price,boolean first_call,Integer free_time_remain,Integer free_over_time_remain){
		if(first_call){
			first_call=false;//把第�?次调用置�?
			if(remain_time<=freeTime){//免费
				return 0;
			}else if(remain_time<=free_over_time){//平峰�?
				if(end_date.getHours()>=16){//夸时间段�?
					Integer this_cost_time =(16-start_date.getHours()-1)*60+(60-start_date.getMinutes());//本时间段耗时
					remain_time=remain_time-this_cost_time;
					free_over_time_remain=free_over_time-this_cost_time;

					if(this_cost_time>freeTime){
						accu_price=(this_cost_time-freeTime)*normalPrice;
					}else{
						free_time_remain=freeTime-this_cost_time;
					}
					return from_16_to_22(remain_time,start_date,end_date,accu_price,false,free_time_remain,free_over_time_remain);
				}else{//没有跨时间段
					return (remain_time-freeTime)*normalPrice;
				}
					
			}else{//平峰�?+超时等待�?
				//没有跨天且都在本时间�?
				if (end_date.getHours()<16 && end_date.getDate()==start_date.getDate()){
					return (remain_time - freeTime)*normalPrice+(remain_time-free_over_time)*extraWait;
					
				}else{ //跨时间段或�?�跨�?
					Integer this_cost_time =(16-start_date.getHours()-1)*60+(60-start_date.getMinutes());
					//accu_price=(this_cost_time-freeTime)*normalPrice+(this_cost_time-free_over_time)*extraWait;
					
					if(this_cost_time>free_over_time){//>40   去了freeTime
						accu_price=(this_cost_time-freeTime)*normalPrice+(this_cost_time-free_over_time)*extraWait;
					}else if(this_cost_time>freeTime&&this_cost_time<free_over_time){// >16 && <40
						accu_price=(this_cost_time-freeTime)*normalPrice;
						free_over_time_remain=free_over_time-this_cost_time;

					}else{//<16
						free_time_remain=freeTime-this_cost_time;
						free_over_time_remain=free_over_time-this_cost_time;
					}
					
					remain_time=remain_time-this_cost_time;//剩余计费时间
					
					return from_16_to_22(remain_time,start_date,end_date,accu_price,false,free_time_remain,free_over_time_remain);
				}
			}
			
		}else{//另一个函数调用到此，必然�?10点开始计�?
			Integer current_total_time =(16-10)*60;
			if(remain_time<current_total_time){//在本时间段跑�?
				
				if(remain_time<=free_over_time_remain){
					return (remain_time-free_time_remain)*normalPrice+accu_price;
				}else{
					return (remain_time-free_time_remain)*normalPrice+accu_price+
							(remain_time-free_over_time_remain)*extraWait;
				}
				
			}else{
				accu_price=accu_price+(current_total_time-free_time_remain)*normalPrice+
						(current_total_time-free_over_time_remain)*extraWait;
				
				free_over_time_remain=0;
				free_time_remain=0;
				
				remain_time=remain_time-current_total_time;
				return from_16_to_22(remain_time,start_date,end_date,accu_price,false,free_time_remain,free_over_time_remain);
			}
					
		}
		
	}
	
	/**
	 * 高峰16点到22�?(法定节假日除�?)
	 * @param remain_time 剩余分钟
	 * @param start_date  本时段开始计费时�?
	 * @param end_date    结束计费时间
	 * @param accu_price  累加费用：第�?次调用传�?0，被另外函数调用传�?�累加钱�?
	 * @param first_call  是否第一次调�?
	 * @return
	 */
	private static double from_16_to_22(Integer remain_time,Date start_date,Date end_date,double accu_price,
			boolean first_call,Integer free_time_remain,Integer free_over_time_remain){
		double realPrice=0.00;
		
		
		if(first_call){
			callTimes++;
			if(isHolidays(start_date)){
				realPrice=normalPrice;
				System.out.println("=======================是节假日�?");
			}else{
				realPrice=peakPrice;
				System.out.println("=======================不是节假日！");
			}
			
			first_call=false;//把第�?次调用置�?
			if(remain_time<=freeTime){//免费
				return 0;
			}else if(remain_time<=free_over_time){//平峰�?
				if(end_date.getHours()>=22){//夸时间段�?
					Integer this_cost_time =(22-start_date.getHours()-1)*60+(60-start_date.getMinutes());//本时间段耗时
					remain_time=remain_time-this_cost_time;
					free_over_time_remain=free_over_time-this_cost_time;

					if(this_cost_time>freeTime){
						accu_price=(this_cost_time-freeTime)*peakPrice;
					}else{
						free_time_remain=freeTime-this_cost_time;
					}

					
					return from_22_to_23(remain_time,start_date,end_date,accu_price,false,free_time_remain,free_over_time_remain);
				}else{//没有跨时间段
					return (remain_time-freeTime)*realPrice;
				}
					
			}else{//平峰�?+超时等待�?
				//没有跨天且都在本时间�?
				if (end_date.getHours()<22 && end_date.getDate()==start_date.getDate()){
					return (remain_time - freeTime)*realPrice+(remain_time-free_over_time)*extraWait;
					
				}else{ //跨时间段或�?�跨�?
					Integer this_cost_time =(22-start_date.getHours()-1)*60+(60-start_date.getMinutes());
					//accu_price=(this_cost_time-freeTime)*peakPrice+(this_cost_time-free_over_time)*extraWait;
					
					if(this_cost_time>free_over_time){//>40   去了freeTime
						accu_price=(this_cost_time-freeTime)*realPrice+(this_cost_time-free_over_time)*extraWait;
					}else if(this_cost_time>freeTime&&this_cost_time<free_over_time){// >22 && <40
						accu_price=(this_cost_time-freeTime)*realPrice;
						free_over_time_remain=free_over_time-this_cost_time;

					}else{//<22
						free_time_remain=freeTime-this_cost_time;
						free_over_time_remain=free_over_time-this_cost_time;
					}
					
					remain_time=remain_time-this_cost_time;//剩余计费时间
					
					return from_22_to_23(remain_time,start_date,end_date,accu_price,false,free_time_remain,free_over_time_remain);
				}
			}
			
		}else{//另一个函数调用到此，必然�?16点开始计�?
			
			Integer current_total_time =(22-16)*60;
			if(remain_time<current_total_time){//在本时间段跑�?
				callTimes++;
				
				if(isHolidays(end_date)){
					realPrice=normalPrice;
				}else{
					realPrice=peakPrice;
				}
				
				if(remain_time<=free_over_time_remain){
					return (remain_time-free_time_remain)*realPrice+accu_price;
				}else{
					return (remain_time-free_time_remain)*realPrice+accu_price+
							(remain_time-free_over_time_remain)*extraWait;
				}
				
			}else{//本时间段跑不�?
				callTimes++;
				//本时间段跑不完，callTimes=2�?3�?4
				int days=0;
				if(start_date.getHours()==23){
					days=callTimes;
				}else{
					days=callTimes-1;
				}
				Date judgeDate=new Date();
			    Calendar calendar = new GregorianCalendar(); 
			    calendar.setTime(start_date); 
			    calendar.add(calendar.DATE,days);//把日期往后增加一�?.整数�?后推,负数�?前移�? 
			    judgeDate=calendar.getTime();   //这个时间就是日期�?后推�?天的结果 			    
				if(isHolidays(judgeDate)){
					realPrice=normalPrice;
				}else{
					realPrice=peakPrice;
				}
				
				
				accu_price=accu_price+(current_total_time-free_time_remain)*realPrice+
						(current_total_time-free_over_time_remain)*extraWait;
				
				free_over_time_remain=0;
				free_time_remain=0;
				
				remain_time=remain_time-current_total_time;
				return from_22_to_23(remain_time,start_date,end_date,accu_price,false,free_time_remain,free_over_time_remain);
			}
					
		}
		
	}
	
	/**
	 * 判断是否法定节假�?
	 * @param start_date
	 * @return
	 */
	private static boolean isHolidays(Date start_date) {
		boolean isHolidays = false;
		isHolidays= JudgeFestival.isHolidays(start_date);
		return isHolidays;
	}

	/**
	 * 平峰22点到23�?
	 * @param remain_time 剩余分钟
	 * @param start_date  本时段开始计费时�?
	 * @param end_date    结束计费时间
	 * @param accu_price  累加费用：第�?次调用传�?0，被另外函数调用传�?�累加钱�?
	 * @param first_call  是否第一次调�?
	 * @return
	 */
	private static double from_22_to_23(Integer remain_time,Date start_date,Date end_date,double accu_price,
			boolean first_call,Integer free_time_remain,Integer free_over_time_remain){
		if(first_call){
			first_call=false;//把第�?次调用置�?
			if(remain_time<=freeTime){//免费
				return 0;
			}else if(remain_time<=free_over_time){//平峰�?
				if(end_date.getHours()>=23){//夸时间段�?
					Integer this_cost_time =(23-start_date.getHours()-1)*60+(60-start_date.getMinutes());//本时间段耗时
					remain_time=remain_time-this_cost_time;
					free_over_time_remain=free_over_time-this_cost_time;

					if(this_cost_time>freeTime){
						accu_price=(this_cost_time-freeTime)*normalPrice;
					}else{
						free_time_remain=freeTime-this_cost_time;
					}
					
					return from_23_to_5(remain_time,start_date,end_date,accu_price,false,free_time_remain,free_over_time_remain);
				}else{//没有跨时间段
					return (remain_time-freeTime)*normalPrice;
				}
					
			}else{//平峰�?+超时等待�?
				//没有跨天且都在本时间�?
				if (end_date.getHours()<23 && end_date.getDate()==start_date.getDate()){
					return (remain_time - freeTime)*normalPrice+(remain_time-free_over_time)*extraWait;
					
				}else{ //跨时间段或�?�跨�?
					Integer this_cost_time =(23-start_date.getHours()-1)*60+(60-start_date.getMinutes());
					//accu_price=(this_cost_time-freeTime)*normalPrice+(this_cost_time-free_over_time)*extraWait;
					
					if(this_cost_time>free_over_time){//>40   去了freeTime
						accu_price=(this_cost_time-freeTime)*normalPrice+(this_cost_time-free_over_time)*extraWait;
					}else if(this_cost_time>freeTime&&this_cost_time<free_over_time){// >23 && <40
						accu_price=(this_cost_time-freeTime)*normalPrice;
						free_over_time_remain=free_over_time-this_cost_time;

					}else{//<23
						free_time_remain=freeTime-this_cost_time;
						free_over_time_remain=free_over_time-this_cost_time;
					}
					
					remain_time=remain_time-this_cost_time;//剩余计费时间
					
					return from_23_to_5(remain_time,start_date,end_date,accu_price,false,free_time_remain,free_over_time_remain);
				}
			}
			
		}else{//另一个函数调用到此，必然�?22点开始计�?
			Integer current_total_time =(23-22)*60;
			if(remain_time<current_total_time){//在本时间段跑�?
				
				if(remain_time<=free_over_time_remain){
					return (remain_time-free_time_remain)*normalPrice+accu_price;
				}else{
					return (remain_time-free_time_remain)*normalPrice+accu_price+
							(remain_time-free_over_time_remain)*extraWait;
				}
				
			}else{
				accu_price=accu_price+(current_total_time-free_time_remain)*normalPrice+
						(current_total_time-free_over_time_remain)*extraWait;
				
				free_over_time_remain=0;
				free_time_remain=0;
				
				remain_time=remain_time-current_total_time;
				return from_23_to_5(remain_time,start_date,end_date,accu_price,false,free_time_remain,free_over_time_remain);
			}
					
		}
		
	}
	
	private static boolean is_next_day(Date today,Date next_day){
		Calendar calendar = new GregorianCalendar(); 
	    calendar.setTime(today); 
	    calendar.add(calendar.DATE,1); 
	    int result_day = calendar.get(Calendar.DAY_OF_MONTH);
	    return result_day==next_day.getDate();
	}
	
	/**
	 * 夜间&平峰
	 * @param remain_time 剩余分钟
	 * @param start_date  本时段开始计费时�?
	 * @param calc_price  累加费用
	 * @return
	 */
	private static double from_23_to_5(Integer remain_time,Date start_date,Date end_date,double accu_price,
			boolean first_call,Integer free_time_remain,Integer free_over_time_remain){
		
		if(first_call){
			first_call=false;//把第�?次调用置�?
			if(remain_time<=freeTime){//免费时常
				nightMinute = remain_time;
				return remain_time*minitePriceNight;//第一次，且小于免费时常，�? 夜间�?
				//return 0;
			}else if(remain_time<=free_over_time){//小于40分钟
				if (((start_date.getDate()== end_date.getDate() && (end_date.getHours()==23||(end_date.getHours()>=0&&end_date.getHours()<5))) //都在同一天，并且�?始时间为23或�?�开始时间在0�?5之间
					|| (is_next_day(start_date,end_date) && end_date.getHours()<5 ))){//没夸时间�?
					nightMinute = remain_time;
					return (remain_time)*minitePriceNight+(remain_time-freeTime)*normalPrice;//夜间时长�?+平峰�?
					
				}else{//夸时间段
					Integer this_cost_time =0;
					if(start_date.getHours()==23)
					{
						this_cost_time=5*60+(60-start_date.getMinutes());
					}else{
						this_cost_time =(5-start_date.getHours()-1)*60+(60-start_date.getMinutes());//本时间段耗时	
					}

					remain_time=remain_time-this_cost_time;
					free_over_time_remain=free_over_time-this_cost_time;

					if(this_cost_time>freeTime){
						accu_price=this_cost_time*minitePriceNight+(this_cost_time-freeTime)*normalPrice;
					}else{
						free_time_remain=freeTime-this_cost_time;
						accu_price=this_cost_time*minitePriceNight;
					}
					nightMinute = this_cost_time;
					return from_5_to_7(remain_time,start_date,end_date,accu_price,false,free_time_remain,free_over_time_remain);
					
				}
					
			}else{//大于40分钟
				//都在本时间段
				if (((start_date.getDate()== end_date.getDate() && (end_date.getHours()==23||(end_date.getHours()>=0&&end_date.getHours()<5))) //都在同一天，并且�?始时间为23或�?�开始时间在0�?5之间
						|| (is_next_day(start_date,end_date) && end_date.getHours()<5 ))){
					nightMinute = remain_time;
					return remain_time*minitePriceNight+(remain_time-free_over_time)*extraWait+
							(remain_time-freeTime)*normalPrice;
					
				}else{ //跨时间段或跨天
					Integer this_cost_time =0;
					if(start_date.getHours()==23)
					{
						this_cost_time=5*60+(60-start_date.getMinutes());
					}else{
						this_cost_time =(5-start_date.getHours()-1)*60+(60-start_date.getMinutes());//
					}
					
					
					
					if(this_cost_time>free_over_time){//>40
						accu_price=this_cost_time*minitePriceNight+(this_cost_time-free_over_time)*extraWait+
								(this_cost_time-freeTime)*normalPrice;
					
					}else if(this_cost_time>freeTime&&this_cost_time<free_over_time){// >10 && <40
						accu_price=this_cost_time*minitePriceNight+(this_cost_time-freeTime)*normalPrice;
						free_over_time_remain=free_over_time-this_cost_time;

					}else{//<10
						accu_price=this_cost_time*minitePriceNight;//只收夜间�?
						free_time_remain=freeTime-this_cost_time;
						free_over_time_remain=free_over_time-this_cost_time;
					}
					nightMinute = this_cost_time;
					remain_time=remain_time-this_cost_time;//剩余计费时间
					return from_5_to_7(remain_time,start_date,end_date,accu_price,false,free_time_remain,free_over_time_remain);
				}
			}
			
		}else{//另一个函数调用到此，必然�?23点开始计�?
			
			Integer current_total_time =6*60;
			if(remain_time<current_total_time){//本段跑完
				nightMinute += remain_time;

				if(remain_time<=free_time_remain){
					return remain_time*minitePriceNight+accu_price+remain_time*normalPrice;
				}else if(remain_time<=free_over_time_remain){
					return remain_time*minitePriceNight+accu_price+(remain_time-free_time_remain)*normalPrice;
				}else{
					return remain_time*minitePriceNight+accu_price+(remain_time-free_over_time_remain)*extraWait
							+(remain_time-free_time_remain)*normalPrice;
				}
			}else{
				accu_price=accu_price+current_total_time*minitePriceNight
						+(current_total_time-free_over_time_remain)*extraWait
						+(current_total_time-free_time_remain)*normalPrice;
				
				free_time_remain=0;
				free_over_time_remain=0;
				nightMinute += current_total_time;
				remain_time=remain_time-current_total_time;
				return from_5_to_7(remain_time,start_date,end_date,accu_price,false,free_time_remain,free_over_time_remain);
			}
					
		}

		
	}
	
}
