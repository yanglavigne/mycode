1、原来的分页是假分页。

2、得到mybatis的sql语句
BoundSql sql=
this.getSqlSession().getConfiguration().getMappedStatement
(getMapperNamespace() + "." + listId)
.getBoundSql(params);

3、设置pageStart,pageSize
if(sql.getSql().contains("limit")||sql.getSql().contains("LIMIT")){
				
				if(params instanceof Map){
					((Map<String,Object>) params).put("pageStart", pageModel.getStartRow());
					((Map<String,Object>) params).put("pageSize", pageModel.getPageSize());
					pageModel.setList(this.getSqlSession().selectList(
							getMapperNamespace() + "." + listId, 
							params));
				}
4、sql语句中加入,从pageStart条开始，查询pageSize条记录
LIMIT #{pageStart},#{pageSize}

			