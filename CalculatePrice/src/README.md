0、后台项目查看mybits执行语句
BoundSql sql=
this.getSqlSession().getConfiguration().getMappedStatement(
getMapperNamespace() + "." + listId).getBoundSql(params);			
logger.info("拦截的SQL:"+sql.getSql());

1、controller--service(getPrice)--service(computedZC)
--ComputeTimePrice(calPrice)--ComputeTimePrice(from_16_to_22)
--ComputeTimePrice(isHolidays)--JudgeFestivalService(isHolidays)

service中注入：
@Autowired
private JudgeFestivalService judgeFestivalService;


ComputeTimePrice computeTimePrice = new ComputeTimePrice(chargeStandard,priceDto,judgeFestivalService);
		Map<String,Object> result = new HashMap<String,Object>();
		result = computeTimePrice.calPrice(priceDto);
		
ComputeTimePrice中：
JudgeFestivalService judgeFestivalService;		
构造器中：this.judgeFestivalService=judgeFestivalService;
isHolidays中：isHolidays= judgeFestivalService.isHolidays(start_date);		
	

2、JudgeFestival.java判断是否为法定节假日

3、ComputeTimePrice根据需求计算价钱

另加：夜间时长附加费目前还是按0.5元/分钟收取（舒适、商务/多功能、豪华）
高峰时间段：7：00-10：00 、16：00-22：00（法定节假日除外）
夜间服务费：23：00-5：00（夜间同时收平峰费）
总价：基础价格+超时间总价+超公里总价+回空里程附加费+夜间服务里程附加费+夜间时长附加费+超时等待附加费
超时等待费：正常计时30分钟后，另外收取每分钟X元的超时等待附加费。如果存在套餐，走完套餐里面时间再算30分钟开始计算。
注：到达出发地等待10分钟后自动开始正常计价
回空里程附加费：如果形成超过了15公里开始计算回空里程附加费。如果存在套餐（是走完套餐公里数再开始计算公里数超不超过15公里）


4、表设计

INSERT INTO `b_sys_dict`(USE_TYPE,DICT_TYPE,DICT_NAME,DICT_VALUE,DESCRIPTION,DEL_FLAG,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,CITY_ID,city)  VALUES ('0', '节假日', '节假日', '2017-01-01,2017-01-02,2017-01-27,2017-01-28,2017-01-29,2017-01-30,2017-01-31,2017-02-01,2017-02-02,2017-04-02,2017-04-03,2017-04-04,2017-04-29,2017-04-30,2017-05-01,2017-05-28,2017-05-29,2017-05-30,2017-10-01,2017-10-02,2017-10-03,2017-10-04,2017-10-05,2017-10-06,2017-10-07,2017-10-08', '2017年节假日', '1', null, null, null, null, '1', null, null);
INSERT INTO `b_sys_dict`(USE_TYPE,DICT_TYPE,DICT_NAME,DICT_VALUE,DESCRIPTION,DEL_FLAG,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,CITY_ID,city)  VALUES ('0', '法定工作日', '法定工作日', '2017-01-22,2017-02-04,2017-04-01,2017-05-27,2017-09-30', '2017年法定工作日', '1', null, null, null, null, '1', null, null);



CREATE TABLE `test` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usage` int(11) DEFAULT NULL COMMENT '用车类型0:出租;1专车；2接送机',
  `type` int(11) DEFAULT NULL COMMENT '平台类型（1首汽，0大众）',
  `car_type` int(11) DEFAULT NULL COMMENT '专车类型（0舒适，1商务，2豪华,3经济）',
  `startPrice` decimal(10,2) DEFAULT NULL COMMENT '基础价格',
  `freeMileage` decimal(10,2) DEFAULT NULL COMMENT '里程（含）',
  `freeTime` int(20) DEFAULT NULL COMMENT '时长（含）',
  `unitPrice` decimal(10,2) DEFAULT NULL COMMENT '超里程费',
  `minutePrice` decimal(10,2) DEFAULT NULL COMMENT '平峰时常费',
  `peakMinutePrice` decimal(10,2) DEFAULT NULL COMMENT '高峰时长费',
  `overMinutePrice` decimal(10,2) DEFAULT NULL COMMENT '超时等待费',
  `backMileage` decimal(10,2) DEFAULT NULL COMMENT '回空里程',
  `backMileagePrice` decimal(10,2) DEFAULT NULL COMMENT '回空里程附加费',
  `nightMileagePrice` decimal(10,2) DEFAULT NULL COMMENT '夜间服务里程附加费',
  `nightMinutePrice` decimal(10,2) DEFAULT NULL COMMENT '夜间时长费',
  `nightTimeFrom` varchar(255) DEFAULT NULL COMMENT '夜间开始时间',
  `nightTimeTo` varchar(255) DEFAULT NULL COMMENT '夜间结束时间',
  `city_id` int(11) DEFAULT NULL COMMENT '城市',
  `orderType` int(11) DEFAULT NULL COMMENT '订单类型，0实时，1预约',
  `city` varchar(255) DEFAULT NULL COMMENT '夜间结束时间',
  `waitTime` int(20) DEFAULT NULL COMMENT '超时等待时间',
  `peakStartTime` varchar(20) DEFAULT NULL COMMENT '高峰开始时间',
  `peakEndTime` varchar(20) DEFAULT NULL COMMENT '高峰结束时间',
  `peakStartTime2` varchar(20) DEFAULT NULL,
  `peakEndTime2` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;



INSERT INTO `test` VALUES ('1', '1', '0', '0', '50.00', '5.00', '10', '2.80', '0.50', '1.00', '1.00', '10.00', '1.00', '1.00', '0.50', '23:00:00', '05:00:00', '35', '0', '北京', '30', '07:00', '10:00', '16:00', '22:00');
INSERT INTO `test` VALUES ('2', '1', '0', '1', '70.00', '5.00', '10', '4.50', '0.70', '1.50', '1.00', '10.00', '1.00', '1.00', '0.50', '23:00:00', '05:00:00', '35', '0', '北京', '30', '07:00', '10:00', '16:00', '22:00');
INSERT INTO `test` VALUES ('3', '1', '0', '0', '50.00', '5.00', '10', '3.00', '0.50', '1.00', '1.00', '15.00', '1.00', '1.00', '0.50', '23:00:00', '23:00:00', '107', '0', '上海', '30', '07:00', '10:00', '16:00', '22:00');
INSERT INTO `test` VALUES ('4', '1', '0', '1', '70.00', '5.00', '10', '4.30', '0.70', '1.50', '1.00', '15.00', '1.50', '1.00', '0.50', '23:00:00', '05:00:00', '107', '0', '上海', '30', '07:00', '10:00', '16:00', '22:00');
INSERT INTO `test` VALUES ('5', '1', '0', '2', '80.00', '5.00', '10', '4.60', '0.80', '1.80', '1.00', '15.00', '1.70', '1.00', '0.50', '23:00:00', '23:00:00', '107', '0', '上海', '30', '07:00', '10:00', '16:00', '22:00');
INSERT INTO `test` VALUES ('6', '1', '0', '0', '50.00', '5.00', '10', '2.80', '0.50', '1.00', '1.00', '10.00', '1.00', '1.00', '0.50', '23:00:00', '05:00:00', '35', '1', '北京', '30', '07:00', '10:00', '16:00', '22:00');
INSERT INTO `test` VALUES ('7', '1', '0', '1', '70.00', '5.00', '10', '4.50', '0.70', '1.50', '1.00', '10.00', '1.00', '1.00', '0.50', '23:00:00', '05:00:00', '35', '1', '北京', '30', '07:00', '10:00', '16:00', '22:00');
INSERT INTO `test` VALUES ('8', '1', '0', '0', '50.00', '5.00', '10', '3.00', '0.50', '1.00', '1.00', '15.00', '1.00', '1.00', '0.50', '23:00:00', '23:00:00', '107', '1', '上海', '30', '07:00', '10:00', '16:00', '22:00');
INSERT INTO `test` VALUES ('9', '1', '0', '1', '70.00', '5.00', '10', '4.30', '0.70', '1.50', '1.00', '15.00', '1.50', '1.00', '0.50', '23:00:00', '05:00:00', '107', '1', '上海', '30', '07:00', '10:00', '16:00', '22:00');
INSERT INTO `test` VALUES ('10', '1', '0', '2', '80.00', '5.00', '10', '4.60', '0.80', '1.80', '1.00', '15.00', '1.70', '1.00', '0.50', '23:00:00', '23:00:00', '107', '1', '上海', '30', '07:00', '10:00', '16:00', '22:00');


