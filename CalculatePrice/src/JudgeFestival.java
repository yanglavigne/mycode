

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * 判断是否节假日
 * @author PC
 *
 */
public class JudgeFestival {
	
	public static void main(String[] args) throws ParseException{
	
		String dateString = "2017-05-06";  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		Date date = sdf.parse(dateString);  
		boolean flag = isHolidays(date);
		System.out.println(date.toString()+"是不是节假日"+flag);
	}
	
	
	public static boolean isHolidays(Date date){
		boolean isHolidays=false;
		
		
			String holidays="2017-01-01,2017-01-02,2017-01-27,2017-01-28,2017-01-29,2017-01-30,2017-01-31,2017-02-01,2017-02-02,2017-04-02,2017-04-03,2017-04-04,2017-04-29,2017-04-30,2017-05-01,2017-05-28,2017-05-29,2017-05-30,2017-10-01,2017-10-02,2017-10-03,2017-10-04,2017-10-05,2017-10-06,2017-10-07,2017-10-08";
			String workDays="2017-01-22,2017-02-04,2017-04-01,2017-05-27,2017-09-30";
			
			if(isWeekend(date)){//周六周日
				if(isWorkday(date,workDays)){//法定工作
					isHolidays=false;
				}else{//休息
				    isHolidays=true;
				}
			}else{
				if(isFestival(date,holidays)){
					isHolidays=true;
				}else{
				    isHolidays=false;
				}
			}
		
		return isHolidays;
	}
	
	
	
	 /**
	  * 是节假日
	  * 
	  * @param date
	  * @return
	  */
	 public static boolean isFestival(Date date,String holidays) {
		 boolean isFestival = false;
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 String dateStr = sdf.format(date);
		 List<String> holidayList = Arrays.asList(holidays.split(",")); 
		 if(holidayList.size()>0){
			 for(int i=0;i<holidayList.size();i++){
				 if(holidayList.get(i)==dateStr||dateStr.equals(holidayList.get(i))){
					 isFestival = true;
					 break;
				 }else{
					 isFestival=false;
				 }
			 }
		 }
		 return isFestival;
	 }

	 /**
	  * 周六周日
	  * 
	  * @param date
	  * @return
	  */
	 public static boolean isWeekend(Date date){
		 boolean weekend = false;
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(date);
		 if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
				 || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			 weekend = true;
		 }
		 return weekend;
	 }
	 
	 /**
	  * 是否法定工作�?
	  * @param date
	  * @param workdays
	  * @return
	  */
	 public static boolean isWorkday(Date date,String workdays){
		 boolean workday = false;
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 String dateStr = sdf.format(date);
		 List<String> workdayList = Arrays.asList(workdays.split(",")); 
		 if(workdayList.size()>0){
			 for(int i=0;i<workdayList.size();i++){
				 if(workdayList.get(i)==dateStr||dateStr.equals(workdayList.get(i))){
					 workday = true;//法定工作
					 break;
				 }else{
					 workday=false;//不是工作
				 }
			 }
		 }
		 return workday;
	 }
	
}
