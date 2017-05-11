1、之前用poi实现导出到excel功能，从数据库导出数据到excel中，数据量很大的时候，OOM内存溢出。

2、poi3.8新出了一个SXSSFWorkbook对象，用来解决大数据量以及超大数据量的导入导出操作的，但是SXSSFWorkbook只支持.xlsx格式，不支持.xls格式的Excel文件。

3、SXSSFWorkbook可以控制excel数据占用的内存，他通过控制在内存中的行数来实现资源管理，即当创建对象超过了设定的行数，它会自动刷新内存，将数据写入文件，这样导致打印时，占用的CPU，和内存很少。

4、Excel2003格式一个sheet只支持65536行，excel2007支持1048576。  

5、数据库分页查询数据，然后再调用函数，一行行添加到excel中

6、main()中测试时，直接保存到本地，但是项目中要从服务器下载
FileOutputStream out = new FileOutputStream("C:/sxssf.xlsx");
excelExport.OutPut(out);

response.setContentType("application/vnd.ms-excel;charset=utf-8");
response.setHeader("Content-disposition",attachment;
					filename=+URLEncoder.encode(excelName,"UTF8"));
OutputStream output= response.getOutputStream();
excelExport.OutPut(output);




6、excel文件下载，不能用ajax提交。
1）导出excel为什么不能用ajax请求？
因为导出excel，实际上是文件下载，后台需要往前端（浏览器）写文件流的。
而Ajax请求获取数据都是“字符串”，整个交互传输用的都是字符串数据，它没法解析后台返回的文件流，但浏览器可以。

2）Ajax与Form表单提交的区别：
Ajax提交不会自动刷新页面，需要手动处理。
Form表单提交在数据提交后会刷新页面，如果是Post提交，点击刷新浏览器会提示 是否再次提交。
window.location="${frontPath}/order/exportOrderListForCallCenter.action.action?customerPhone="+customerPhone+"&carUserPhone="+carUserPhone;