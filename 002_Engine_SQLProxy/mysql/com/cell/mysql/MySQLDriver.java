package com.cell.mysql;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import com.cell.CUtil;
import com.cell.sql.SQLColumn;
import com.cell.sql.SQLDriver;
import com.cell.sql.SQLStructCLOB;
import com.cell.sql.SQLTableManager;
import com.cell.sql.SQLType;
import com.cell.sql.anno.SQLField;
import com.cell.sql.anno.SQLTable;
import com.cell.sql.util.SQLUtil;
import com.cell.util.EnumManager;
import com.cell.xstream.XStreamAdapter;

/**
 * @author WAZA

<html>
<body>
<table border="1">
<colgroup>
<col>
<col>
<col>
</colgroup>
<thead>
<tr>
<th>MySQL Type Name</th>
<th>Return value of <code class="literal">GetColumnClassName</code>
</th>
<th>Returned as Java Class</th>
</tr></thead>
<tbody>
<tr>
<td>
<span class="type">BIT(1)</span> (new in MySQL-5.0)</td>
<td><span class="type">BIT</span></td>
<td><code class="classname">java.lang.Boolean</code></td>
</tr>
<tr>
<td>
<span class="type">BIT( &gt; 1)</span> (new in MySQL-5.0)</td>
<td><span class="type">BIT</span></td>
<td><code class="classname">byte[]</code></td>
</tr>
<tr>
<td><span class="type">TINYINT</span></td>
<td><span class="type">TINYINT</span></td>
<td>
<code class="classname">java.lang.Boolean</code> if the configuration property
                    <code class="literal">tinyInt1isBit</code> is set to
                    <code class="literal">true</code> (the default) and the
                    storage size is 1, or
                    <code class="classname">java.lang.Integer</code> if not.</td>
</tr>
<tr>
<td>
<span class="type">BOOL</span>, <span class="type">BOOLEAN</span>
</td>
<td><span class="type">TINYINT</span></td>
<td>See <span class="type">TINYINT</span>, above as these are aliases for
                    <span class="type">TINYINT(1)</span>, currently.</td>
</tr>
<tr>
<td><span class="type">SMALLINT[(M)] [UNSIGNED]</span></td>
<td><span class="type">SMALLINT [UNSIGNED]</span></td>
<td>
<code class="classname">java.lang.Integer</code> (regardless if UNSIGNED or not)</td>
</tr>
<tr>
<td><span class="type">MEDIUMINT[(M)] [UNSIGNED]</span></td>
<td><span class="type">MEDIUMINT [UNSIGNED]</span></td>
<td>
<code class="classname">java.lang.Integer,</code> if UNSIGNED
                    <code class="classname">java.lang.Long</code> (C/J 3.1 and
                    earlier), or
                    <code class="classname">java.lang.Integer</code> for C/J 5.0
                    and later</td>
</tr>
<tr>
<td><span class="type">INT,INTEGER[(M)] [UNSIGNED]</span></td>
<td><span class="type">INTEGER [UNSIGNED]</span></td>
<td>
<code class="classname">java.lang.Integer</code>, if UNSIGNED
                    <code class="classname">java.lang.Long</code>
</td>
</tr>
<tr>
<td><span class="type">BIGINT[(M)] [UNSIGNED]</span></td>
<td><span class="type">BIGINT [UNSIGNED]</span></td>
<td>
<code class="classname">java.lang.Long</code>, if UNSIGNED
                    <code class="classname">java.math.BigInteger</code>
</td>
</tr>
<tr>
<td><span class="type">FLOAT[(M,D)]</span></td>
<td><span class="type">FLOAT</span></td>
<td><code class="classname">java.lang.Float</code></td>
</tr>
<tr>
<td><span class="type">DOUBLE[(M,B)]</span></td>
<td><span class="type">DOUBLE</span></td>
<td><code class="classname">java.lang.Double</code></td>
</tr>
<tr>
<td><span class="type">DECIMAL[(M[,D])]</span></td>
<td><span class="type">DECIMAL</span></td>
<td><code class="classname">java.math.BigDecimal</code></td>
</tr>
<tr>
<td><span class="type">DATE</span></td>
<td><span class="type">DATE</span></td>
<td><code class="classname">java.sql.Date</code></td>
</tr>
<tr>
<td><span class="type">DATETIME</span></td>
<td><span class="type">DATETIME</span></td>
<td><code class="classname">java.sql.Timestamp</code></td>
</tr>
<tr>
<td><span class="type">TIMESTAMP[(M)]</span></td>
<td><span class="type">TIMESTAMP</span></td>
<td><code class="classname">java.sql.Timestamp</code></td>
</tr>
<tr>
<td><span class="type">TIME</span></td>
<td><span class="type">TIME</span></td>
<td><code class="classname">java.sql.Time</code></td>
</tr>
<tr>
<td><span class="type">YEAR[(2|4)]</span></td>
<td><span class="type">YEAR</span></td>
<td>If <code class="literal">yearIsDateType</code> configuration property is set to
                    false, then the returned object type is
                    <code class="classname">java.sql.Short</code>. If set to
                    true (the default) then an object of type
                    <code class="classname">java.sql.Date</code> (with the date
                    set to January 1st, at midnight).</td>
</tr>
<tr>
<td><span class="type">CHAR(M)</span></td>
<td><span class="type">CHAR</span></td>
<td>
<code class="classname">java.lang.String</code> (unless the character set for
                    the column is <span class="type">BINARY</span>, then
                    <code class="classname">byte[]</code> is returned.</td>
</tr>
<tr>
<td><span class="type">VARCHAR(M) [BINARY]</span></td>
<td><span class="type">VARCHAR</span></td>
<td>
<code class="classname">java.lang.String</code> (unless the character set for
                    the column is <span class="type">BINARY</span>, then
                    <code class="classname">byte[]</code> is returned.</td>
</tr>
<tr>
<td><span class="type">BINARY(M)</span></td>
<td><span class="type">BINARY</span></td>
<td><code class="classname">byte[]</code></td>
</tr>
<tr>
<td><span class="type">VARBINARY(M)</span></td>
<td><span class="type">VARBINARY</span></td>
<td><code class="classname">byte[]</code></td>
</tr>
<tr>
<td><span class="type">TINYBLOB</span></td>
<td><span class="type">TINYBLOB</span></td>
<td><code class="classname">byte[]</code></td>
</tr>
<tr>
<td><span class="type">TINYTEXT</span></td>
<td><span class="type">VARCHAR</span></td>
<td><code class="classname">java.lang.String</code></td>
</tr>
<tr>
<td><span class="type">BLOB</span></td>
<td><span class="type">BLOB</span></td>
<td><code class="classname">byte[]</code></td>
</tr>
<tr>
<td><span class="type">TEXT</span></td>
<td><span class="type">VARCHAR</span></td>
<td><code class="classname">java.lang.String</code></td>
</tr>
<tr>
<td><span class="type">MEDIUMBLOB</span></td>
<td><span class="type">MEDIUMBLOB</span></td>
<td><code class="classname">byte[]</code></td>
</tr>
<tr>
<td><span class="type">MEDIUMTEXT</span></td>
<td><span class="type">VARCHAR</span></td>
<td><code class="classname">java.lang.String</code></td>
</tr>
<tr>
<td><span class="type">LONGBLOB</span></td>
<td><span class="type">LONGBLOB</span></td>
<td><code class="classname">byte[]</code></td>
</tr>
<tr>
<td><span class="type">LONGTEXT</span></td>
<td><span class="type">VARCHAR</span></td>
<td><code class="classname">java.lang.String</code></td>
</tr>
<tr>
<td><span class="type">ENUM('value1','value2',...)</span></td>
<td><span class="type">CHAR</span></td>
<td><code class="classname">java.lang.String</code></td>
</tr>
<tr>
<td><span class="type">SET('value1','value2',...)</span></td>
<td><span class="type">CHAR</span></td>
<td><code class="classname">java.lang.String</code></td>
</tr>
</tbody>
</table>
</body>
</html>
 */
public class MySQLDriver implements SQLDriver 
{
	public boolean typeEquals(SQLType type, int jdbc_type) 
	{
		switch(type)
		{
		case BIG_STRUCT:
		case STRUCT:
			switch(jdbc_type){
			case Types.BINARY: 			return true;
			case Types.VARBINARY: 		return true;
			case Types.LONGVARBINARY: 	return true;
			case Types.BLOB: 			return true;
			}
			break;
		case TEXT_STRUCT:
		case XML_STRUCT:
			switch(jdbc_type){
			case Types.VARCHAR: 		return true;
			case Types.LONGVARCHAR: 	return true;
			}
			break;
		case BOOLEAN:
			switch (jdbc_type) {
			case Types.TINYINT: 		return true;
			case Types.BIT:				return true;
			}
			break;
		}
		return type.getJdbcType() == jdbc_type;
	}

	public Object toJavaObject(SQLType type, Class<?> type_clazz, Object sqlObject) throws Exception 
	{
		if (type_clazz.isInstance(sqlObject)) {
			return sqlObject;
		}
		switch(type)
		{
		case BIG_STRUCT:
		case STRUCT:
			return SQLUtil.binToBlob((byte[])sqlObject);
		case TEXT_STRUCT:
			return SQLUtil.stringToClob((String)sqlObject, type_clazz);
		case XML_STRUCT:
			return SQLUtil.stringToXML((String)sqlObject);
		case BYTE:
			return ((Number)sqlObject).byteValue();
		case SHORT:
			return ((Number)sqlObject).shortValue();
		case ENUM:
			return convertValue2Enum(type_clazz, (String)sqlObject);
		default:
			return sqlObject;
		}
	}
	
	public Object toSQLObject(SQLType type, Class<?> type_clazz, Object javaObject) throws Exception 
	{
		switch(type)
		{
		case BIG_STRUCT:
		case STRUCT:
			return SQLUtil.blobToBin((Serializable)javaObject);
		case TEXT_STRUCT:
			if (javaObject instanceof String) {
				return (String)javaObject;
			} else {
				return SQLUtil.clobToString((SQLStructCLOB)javaObject);
			}
		case XML_STRUCT:
			return SQLUtil.xmlToString((Serializable)javaObject);
		case ENUM:
			return convertEnum2Value(javaObject);
		default:
			return javaObject;
		}
	}
	
	private String convertEnum2Value(Object obj) {
		if (obj != null) {
			return obj.toString();
		}else{
			return SQLUtil.ZERO_TEXT;
		}
	}
	
	private Object convertValue2Enum(Class<?> type_clazz, String obj) {
		if (obj == null || obj.isEmpty()) {
			return null;
		}else{
			return EnumManager.valueOf(type_clazz, obj);
		}
	}
	
	@Override
	public ObjectInputStream getXMLInputStream(Reader reader) throws IOException {
		ObjectInputStream ois = XStreamAdapter.getInstance().createReadStream(reader);
		return ois;
	}
	
	@Override
	public ObjectOutputStream getXMLOutputStream(Writer writer) throws IOException {
		ObjectOutputStream oos = XStreamAdapter.getInstance().createWriteStream(writer);
		return oos;
	}
	
	public String getDirverTypeString(SQLType type) 
	{
		switch (type) {
		case BOOLEAN		:return "boolean";
		case BYTE			:return "tinyint";
		case SHORT			:return "smallint";
		case INTEGER		:return "integer";
		case LONG			:return "bigint";
		case FLOAT			:return "float";
		case DOUBLE			:return "double";
		case STRING			:return "varchar";
		case STRUCT			:return "blob";
		case BIG_STRUCT		:return "longblob";
		case TEXT_STRUCT	:return "text";
		case XML_STRUCT		:return "longtext";
		case TIME			:return "time";
		case TIMESTAMP		:return "timestamp";
		case ENUM			:return "varchar(128)";
		}
		return null;
	}
	
	
	
	public String createInsertSQL(String table_name, SQLColumn[] columns)
	{
		StringBuffer sb = new StringBuffer("INSERT INTO ");
		sb.append(table_name);
		sb.append("(\n");
		
		for (int i=0; i<columns.length; i++){
			SQLColumn c = columns[i];
			sb.append("\t"); 
			sb.append(c.getName());
			sb.append(getSplitChar(i, columns.length));
		}
		sb.append(") VALUES (\n");
		for (int i=0; i<columns.length; i++){
			sb.append("\t?"); 
			sb.append(getSplitChar(i, columns.length));
		}
		sb.append(");");
		
		return sb.toString();
	}
	
	public String createUpdateSQL(String table_name, SQLColumn[] columns, String primary_key_name, Object primary_key_value)
	{
		StringBuffer sb = new StringBuffer("UPDATE ");
		sb.append(table_name);
		sb.append(" SET\n");
		
		for (int i=0; i<columns.length; i++){
			SQLColumn c = columns[i];
			sb.append("\t"); 
			sb.append(c.getName()); 
			sb.append("=?"); 
			sb.append(getSplitChar(i, columns.length));
		}
		
		sb.append("WHERE "); 
		sb.append(primary_key_name); 
		sb.append("='");
		sb.append(primary_key_value); 
		sb.append("';");
		
		return sb.toString();
	}
	
	public String createDeleteSQL(String table_name, String primary_key_name, Object primary_key_value)
	{
		StringBuffer sb = new StringBuffer("DELETE FROM ");
		sb.append(table_name);
		sb.append(" WHERE ");
		sb.append(primary_key_name); 
		sb.append("='"); 
		sb.append(primary_key_value);
		sb.append("';");
		return sb.toString();
	}
	
	public String createSelectRowSQL(String table_name, String field_name, Object field_value)
	{
		StringBuffer sb = new StringBuffer("SELECT * FROM ");
		sb.append(table_name);
		sb.append(" WHERE ");
		sb.append(field_name);
		sb.append("='");
		sb.append(field_value);
		sb.append("';");
		
		return sb.toString();
	}
	
	public String createSelectAllLimitSQL(String table_name, int startIndex, int count)
	{
		return "SELECT * FROM " + table_name + " LIMIT " + startIndex + "," + count + ";";
	}
	
//	public String createSelectFieldsSQL(String table_name, String field_names[], Object field_value)
//	{
//		String sql = "SELECT " + CUtil.arrayToString(fieldName, ",", "") + " FROM " + table_name + " WHERE player_id='" + key + "';";
//
//		return sql;
//	}
	
	final private String getSplitChar(int i, int len) {
		return ((i != len - 1) ? ",\n" : "\n");
	}


//	---------------------------------------------------------------------------------------------------------------------------------------------------------
//
//	---------------------------------------------------------------------------------------------------------------------------------------------------------

	/***
	 * <b>对InnoDB表的限制（form：mysql中文文档）</b>
<pre>
·	一个表不能包含超过1000列。

·	内部最大键长度是3500字节，但MySQL自己限制这个到1024字节。

·	除了VARCHAR, BLOB和TEXT列，最大行长度稍微小于数据库页的一半。
	即，最大行长度大约8000字节。LONGBLOB和LONGTEXT列必须小于4GB, 
	总的行长度，页包括BLOB和TEXT列，必须小于4GB。
	InnoDB在行中存储VARCHAR，BLOB或TEXT列的前768字节，余下的存储的分散的页面中。

·	虽然InnoDB内部地支持行尺寸大于65535，你不能定义一个包含VARCHAR列的，
	合并尺寸大于65535的行。

	mysql> CREATE TABLE t (a VARCHAR(8000), b VARCHAR(10000),
		-> c VARCHAR(10000), d VARCHAR(10000), e VARCHAR(10000),
		-> f VARCHAR(10000), g VARCHAR(10000));
	ERROR 1118 (42000): Row size too large. The maximum row size for the
	used table type, not counting BLOBs, is 65535. You have to change some
	columns to TEXT or BLOBs
	
·	在一些更老的操作系统上，数据文件必须小于2GB。

·	InnoDB日志文件的合并尺寸必须小于4GB。

·	最小的表空间尺寸是10MB。最大的表空间尺寸是4,000,000,000个数据库页（64TB）。这也是一个表的最大尺寸。

·	InnoDB表不支持FULLTEXT索引。

·	ANALYZE TABLE 通过对每个索引树做八次随机深入并相应地更新索引集估值，这样来计数集。
	注意，因为这是仅有的估值，反复运行ANALYZE TABLE会产生不同数。这使得 ANALYZE TABLE 在 InnoDB 
	表上很快，不是百分百准确，因为它没有考虑所有的行。

	MySQL 不仅在汇合优化中使用索引集估值。如果一些汇合没有以正确的方式优化，
	你可以试一下 ANALYZE TABLE 。很少有情况，ANALYZE TABLE 没有产生对你特定的表足够好的值，
	你可以使用 FORCE INDEX 在你查询中来强制使用特定索引，或者设置 max_seeks_for_key 来确保
	MySQL在表扫描之上运行索引查找。请参阅5.3.3节，"服务器系统变量"。请参阅A.6节，"优化器相关的问题"。

·	在Windows上，InnoDB总是内部地用小写字母存储数据库和表名字。要把数据库以二进制形式从Unix 
	移到Windows，或者从Windows移到Unix，你应该让所有数据库和表的名字都是小写。

·	警告: 不要在MySQL数据库内的把MySQL系统表从MyISAM转为InnoDB表！这是一个不被支持的操作。
	如果你这么做了，MySQL直到你从备份恢复旧系统表，或用mysql_install_db脚本重建系统表才重启动。

·	InnoDB在表内不保留行的内部计数。（因为多版本化，这可能确实有些复杂）。要处理一个
	SELECT COUNT(*) FROM t语句，InnoDB必须扫描表的一个索引，如果这个索引不在缓冲池中，
	扫描需要花一些时间。要获得快速计数，你不得不使用一个自己创建的计数器表，并让你的应用按
	照它做的插入和删除来更新它。如果你的表格不经常改变，使用MySQL查询缓存时一个好的解决方案。
	如果大致的行数就足够了，则SHOW TABLE STATUS也可被使用。请参阅15.2.11节，
	"InnoDB性能调节提示"。

·	对于AUTO_INCREMENT列，你必须总是为表定义一个索引，并且索引必须包含AUTO_INCREMENT列。
	在MyISAM表中，AUTO_INCREMENT列可能时多列索引的一部分。

·	当你重启MySQL服务器之时，InnoDB可能为一个AUTO_INCREMENT列重使用一个旧值
	（即，一个被赋给一个老的已回滚的事务的值）。

·	当一个AUTO_INCREMENT列用完值，InnoDB限制一个BIGINT到－9223372036854775808
	以及BIGINT UNSIGNED到1。尽管如此，BIGINT值有由64位，所以注意到，如果你要一秒输入100万个行，
	在BIGINT到达它上限之前，可能还需要将近30万年。用所有其它整数类型列，产生一个重复键错误。
	这类似于MyISAM如何工作的，因为它主要是一般MySQL行为，并不特别关于任何存储引擎。

·	DELETE FROM tbl_name不重新生成表，但取而代之地删除所有行，一个接一个地删除。

·	TRUNCATE tbl_name为InnoDB而被映射到DELETE FROM tbl_name 并且不重置AUTO_INCREMENT计数器。

·	SHOW TABLE STATUS不能给出关于InnoDB表准确的统计数据，除了被表保留的物理尺寸。
	行计数仅是在SQL优化中粗略的估计。
	
·	在MySQL 5.1中，如果innodb_table_locks=1(1是默认值） MySQL LOCK TABLES操作在
	每一个表上获取两个锁定。除了在MySQL层的表锁定，它也获得一个InnoDB表锁定。旧版的
	MySQL不获取InnoDB表锁定，旧行为可以通过设置innodb_table_locks=0 来选择。如果没有
	InnoDB表锁定被获得，即使表的一些记录被其它事务锁定，LOCK TABLES完成。
	
·	所有被一个事务持有的InnoDB锁定在该事务被提交或中止之时被释放。因此在AUTOCOMMIT=1模式，
	在InnoDB表上调用是没有太多意义的，因为被需求的InnoDB表锁定可能会被立即释放。

·	有时，在事务的过程中锁定更多的表可能是有用的。不幸地，MySQL中的LOCK TABLES执行一个暗地的
	COMMIT和UNLOCK TABLES。LOCK TABLES的一个InnoDB变量已经被计划， 该计划在事务的中间被执行。
	
·	为建立复制从服务器的LOAD TABLE FROM MASTER语句对InnoDB表不起作用。一个工作区在主服务器
	上更换表为MyISAM的，然后做负载，之后更换主服务器表回到InnoDB中。
	
·	在InnoDB中默认数据库页的大小是16KB。通过编译代码，你可以在8KB到64KB之间来设置这个值。
	你不得不更新在univ.i源文件中的UNIV_PAGE_SIZE和UNIV_PAGE_SIZE_SHIFT的值。
	
·	在MySQL 5.1中，触发器不被级联的外键行为激活。

	 * @return
	 */
	public boolean validateTable(Connection conn, SQLTableManager<?, ?> table,
			boolean auto_create_struct,
			boolean sort_field,
			boolean create_comment) throws SQLException
	{
		// 验证表结构
		table.log.info("validating table struct [" + table.table_name + "] ...");
		
		ValidateResult result = validateTableColumns(conn, table);
		
		if (result != ValidateResult.OK)
		{
			table.log.warn("validate error [" + table.table_name + "] : " + result);
			
			if (result == ValidateResult.ERROR_NOT_EXIST && auto_create_struct)
			{
				table.log.info("creating table struct [" + table.table_name + "] ...");
				
				Statement statement = conn.createStatement();
				
				String create = getCreateTableSQL(table, sort_field, create_comment);
				System.out.println(create);
				statement.execute(create);
				statement.close();
				
				result = validateTableColumns(conn, table);
			}
			else
			{
				throw new SQLException("table struct [" + table.table_name + "] not validate !");
			}
		}

		return result == ValidateResult.OK;
	}
	
	private void validateTableLimit(
			Class<?> table_class, 
			SQLTable table_type,
			String table_name,
			SQLColumn[] table_columns) throws SQLException
	{
		final int 	max_limit_size = 8000;
		int 		row_limit_size = 0;
		
		for (SQLColumn c : table_columns) {
			switch(c.getAnno().type()) 
			{
			case BOOLEAN: 		row_limit_size += 1; break;
			
			case BYTE:			row_limit_size += 1; break;
			case SHORT:			row_limit_size += 2; break;
			case INTEGER:		row_limit_size += 4; break;
			case LONG:			row_limit_size += 8; break;
			
			case FLOAT:			row_limit_size += 4; break;
			case DOUBLE:		row_limit_size += 8; break;

			case TIME:			row_limit_size += 3; break;
			case TIMESTAMP:		row_limit_size += 4; break;
			
			case ENUM:			row_limit_size += 128; break;
			
			case STRING:		
			case STRUCT:		
			case BIG_STRUCT:	
			case TEXT_STRUCT:	
			case XML_STRUCT:	
				if (c.getAnno().size() > 0) {
					row_limit_size += Math.min(768, c.getAnno().size());
				} else {
					row_limit_size += 768;
				}
				break;
			default:
				throw new SQLException("unknow sql type : " + table_name + " - " + c.getName() + " - " + c.getAnno().type());
			}
		}
		
		if (max_limit_size < row_limit_size) {
			throw new SQLException("row size over limit " + max_limit_size + " < " + row_limit_size);
		}
	}

	
	/**
	 * 检查指定数据库里的表结构是否符合java的结构
	 * @param tableClass
	 * @param statement
	 * @return
	 */
	private ValidateResult validateTableColumns(Connection conn, SQLTableManager<?, ?> table) throws SQLException
	{
		try {
			validateTableLimit(
					table.table_class,
					table.table_type, 
					table.table_name, 
					table.table_columns);
		} catch (SQLException err) {
			table.log.error(err.getMessage(), err);
			return ValidateResult.ERROR_DRIVER_LIMITED;
		}
		
		ValidateResult vresult = validateAndAutoFix(conn, table);
		
		if (vresult == ValidateResult.OK)
		{
			String sql = "SELECT * FROM " + table.table_name + " LIMIT 0;";
			
			Statement statement = conn.createStatement();
			ResultSet result = statement.executeQuery(sql);
			ResultSetMetaData metadata = result.getMetaData();
			result.close();
			statement.close();
			
			// last validate
			for (int rc = 1; rc <= metadata.getColumnCount(); rc++) 
			{
				String rname = metadata.getColumnName(rc);
				SQLColumn c = table.getColumn(rname);
				
				if (c != null) 
				{
					String tname = c.getName();
					
					// 检测名字是否正确
					if (!rname.equalsIgnoreCase(tname)) 
					{
						System.err.println(
								"Table '" + table.table_name + "' " +
								"field name ["+rname+","+tname+"] not equal! " +
								"at column " + rc 
								);
						return ValidateResult.ERROR_NAME;
					}
					
					// 检查类型是否正确
					if (!c.getAnno().type().typeEquals(metadata.getColumnType(rc))) 
					{
						String	rtypename	= metadata.getColumnTypeName(rc);
						int		rtype		= metadata.getColumnType(rc);
						String	ttypename	= c.getAnno().type().getDirverTypeName();
						int		ttype		= c.getAnno().type().getJdbcType();
						
						System.err.println(
								"Table '" + table.table_name + "' " +
								"field type ["+rtypename+"("+rtype+")"+","+ttypename+"("+ttype+")"+"] not equal! " +
								"at column " + rc 
								);
						
						return ValidateResult.ERROR_TYPE;
					}
				}
				
			}
			return ValidateResult.OK;
		}
		
		return vresult;
	}
	
	private ValidateResult validateAndAutoFix(Connection conn, SQLTableManager<?, ?> table) throws SQLException
	{
		String sql = "SELECT * FROM " + table.table_name + " LIMIT 0;";

		Statement statement = conn.createStatement();
		
		while(true)
		{
			ResultSet result = null;

			try {
				result = statement.executeQuery(sql);
			} catch (Exception e) {
				return ValidateResult.ERROR_NOT_EXIST;
			}
			
			ResultSetMetaData metadata = result.getMetaData();
			result.close();
			
			ArrayList<SQLColumn> not_exists = new ArrayList<SQLColumn>();
			
			for (SQLColumn c : table.table_columns)
			{
				int rc = indexOfSQLColumn(metadata, c);
				
				if (rc == 0) 
				{
					not_exists.add(c);
				} 
				else if (rc < 0)
				{
					String	rtypename	= metadata.getColumnTypeName(-rc);
					int		rtype		= metadata.getColumnType(-rc);
					String	ttypename	= c.getAnno().type().getDirverTypeName();
					int		ttype		= c.getAnno().type().getJdbcType();
					
					String reson = (
							"Table '" + table.table_name + "' " +
							"field : " + c.getName() + " : type ["+rtypename+"("+rtype+")"+","+ttypename+"("+ttype+")"+"] not equal! " +
							"at column " + -rc);
					
					throw new SQLException(reson);
				}
			}
			
			if (!not_exists.isEmpty())
			{
				for (SQLColumn c : not_exists)
				{
					String add_sql = getAlterTableAddColumnSQL(c, table.table_name);
					System.out.println(add_sql);
					statement.executeUpdate(add_sql);
				}
			}
			else
			{
				break;
			}
		}
		statement.close();
				
		return ValidateResult.OK;
		
	}


	public void syncTableColumns(Connection conn, SQLTableManager<?, ?> table) throws SQLException
	{
		ValidateResult vresult = validateTableColumns(conn, table);
		
		if (vresult == ValidateResult.OK)
		{
			Statement statement = conn.createStatement();
			
			String sql = "SELECT * FROM " + table.table_name + " LIMIT 0;";
			try
			{
				ResultSet 			result			= statement.executeQuery(sql);
				ResultSetMetaData	metadata		= result.getMetaData();
				result.close();
				
				for (int rc = metadata.getColumnCount() - 1; rc >= 0; --rc)
				{
					SQLColumn c  = table.getColumn(metadata.getColumnName(rc));
					String rtname = metadata.getColumnTypeName(rc);
					String ttname = c.getAnno().type().getDirverTypeName();
					
					if (rtname.equalsIgnoreCase(ttname)) {
						String change_sql = getAlterTableChangeColumnSQL(c, table.table_name);
						System.out.println(change_sql);
						statement.executeUpdate(change_sql);
					}
				}
				
			} finally {
				statement.close();
			}
		}
	}
	
//	---------------------------------------------------------------------------------------------------------------------------------

	
	
	/**
	 * @param metadata
	 * @param c
	 * @return 
	 * >=1 : index of column<br>
	 * 0 : not exist<br>
	 * <=1 : index of column, but type not equal<br>
	 * @throws SQLException
	 */
	int indexOfSQLColumn(ResultSetMetaData metadata, SQLColumn c) throws SQLException
	{
		String	tname	= c.getName();
		SQLType	ttype	= c.getAnno().type();
		
		for (int rc = 1; rc <= metadata.getColumnCount(); rc++) {
			// 检测名字是否正确
			if (tname.equalsIgnoreCase(metadata.getColumnName(rc))) {
				// 检查字段类型是否正确
				if (ttype.typeEquals(metadata.getColumnType(rc))) {
					return rc;
				}else{
					return -rc;
				}
			}
		}
		return 0;
	}
	
	
//	---------------------------------------------------------------------------------------------------------------------------------
//	alter table 'name' change 'columna' 'columnb' longblob
//
//	---------------------------------------------------------------------------------------------------------------------------------

	public String getColumnConstraint(SQLColumn c)
	{
		StringBuilder ret = new StringBuilder();
		
		SQLField anno = c.getAnno();
		
		if (anno.size()>0) {
			ret.append(" (" + c.getAnno().size() +")");
		}
		
		if (anno.not_null()) {
			ret.append(" NOT NULL");
		} else {
			ret.append(" NULL");
		}
		
		if (anno.auto_increment()) {
			ret.append(" AUTO_INCREMENT");
		}
		
		String default_value = anno.default_value();
		if (default_value != null && !default_value.isEmpty()) {
			ret.append(" DEFAULT '" + anno.default_value() + "'");
		}
		
		return ret.toString();
	}
	

//	---------------------------------------------------------------------------------------------------------------------------------
	
	public String getAlterTableChangeColumnSQL(SQLColumn c, String table_name)
	{
		StringBuilder add_sql = new StringBuilder();
		add_sql.append("ALTER TABLE `" + table_name + "` CHANGE ");
		add_sql.append("`" + c.getName() + "` ");
		add_sql.append("`" + c.getName() + "` ");
		add_sql.append(c.getAnno().type() + " ");
		add_sql.append(getColumnConstraint(c));
		String comment = c.getAllComment();
		if (comment != null && comment.length() > 0) {
			add_sql.append(" COMMENT '" + comment + "'");
		}
		add_sql.append(";");
		return add_sql.toString();
	}
	
	public String getAlterTableAddColumnSQL(SQLColumn c, String table_name) 
	{
		StringBuilder add_sql = new StringBuilder();
		add_sql.append("ALTER TABLE `" + table_name + "` ADD COLUMN ");
		add_sql.append("`" + c.getName() + "` ");
		add_sql.append(c.getAnno().type() + " ");
		add_sql.append(getColumnConstraint(c));
		String comment = c.getAllComment();
		if (comment != null && comment.length() > 0) {
			add_sql.append(" COMMENT '" + comment + "'");
		}
		add_sql.append(";");
		return add_sql.toString();
	}

//	--------------------------------------------------------------------------------------------------------
	
	public class CreateTableColumnSorter implements Comparator<SQLColumn> 
	{
		final SQLTable		table_type;
		final boolean		sort_by_name;
		
		public CreateTableColumnSorter(SQLTable table_type, boolean sort_by_name) {
			this.table_type		= table_type;
			this.sort_by_name	= sort_by_name;
		}
		
		public int compare(SQLColumn a, SQLColumn b)
		{
			if (a.getName().equals(table_type.primary_key_name())) return -1;
			if (b.getName().equals(table_type.primary_key_name())) return 1;
			int priority = b.getAnno().index_priority() - a.getAnno().index_priority();
			if (priority != 0) {
				return priority;
			}
			if (sort_by_name) {
				return a.getName().compareToIgnoreCase(b.getName());
			}
			return 0;
		}
	}
	
	
	/**
	 * 获得该类型对应SQL的创建语句
	 * @param sorter			是否对列进行排序
	 * @param create_comment	是否产生注释信息
	 * @return 
	 * @throws SQLException
	 */
	public String getCreateTableSQL(
			SQLTableManager<?, ?> table,
			Comparator<SQLColumn> sorter, 
			boolean create_comment)
	{
		SQLColumn[] columnss = new SQLColumn[table.table_columns.length];
		System.arraycopy(table.table_columns, 0, columnss, 0, columnss.length);
		Arrays.sort(columnss, sorter);
		
		String sql = "CREATE TABLE `" + table.table_name + "` (\n";
		int name_max_len = 1;
		for (int i = 0; i < columnss.length; i++) {
			name_max_len = Math.max(columnss[i].getName().length()+4, name_max_len);
		}
		
		for (int i = 0; i < columnss.length; i++)
		{
			SQLColumn column = columnss[i];
			
			sql += "\t" + CUtil.snapStringRightSize(
					"`"+ column.getName() + "`", name_max_len, ' ') + 
					" " + column.getAnno().type();
			
				String constraint = getColumnConstraint(column);
				if (constraint != null && constraint.length() > 0) {
					sql += " " + constraint;
				}
				if (create_comment) {
					String comment = column.getAllComment();
					if (comment != null && comment.length() > 0) {
						sql += " COMMENT '" + comment + "'";
					}
				}
				sql += ",\n";
		}
		
		sql += "\tPRIMARY KEY (" + table.table_type.primary_key_name() + ")";
		if (!table.table_type.constraint().trim().isEmpty()) {
			sql += ", " + table.table_type.constraint() + "\n";
		} else {
			sql += "\n";
		}
		sql += ")";
		
		if (!table.table_type.properties().trim().isEmpty()) {
			sql += " " + table.table_type.properties();
		}
		if (create_comment && !table.table_type.comment().trim().isEmpty()) {
			sql += " COMMENT='" + table.table_type.comment() + "'";
		}
		sql += ";";
		return sql;
	
	}
	
	/**
	 * 获得该类型对应SQL的创建语句
	 * @param table
	 * @param sort_fields		是否对列进行默认排序
	 * @param create_comment	是否产生注释信息
	 * @return 
	 * @throws SQLException
	 */
	public String getCreateTableSQL(
			SQLTableManager<?, ?> table,
			boolean sort_fields, 
			boolean create_comment)
	{
		return getCreateTableSQL(table, new CreateTableColumnSorter(table.table_type, sort_fields), create_comment);
	}

	/**
	 * 获得该类型对应SQL的创建语句
	 * @param table
	 * @return
	 * @throws SQLException
	 */
	public String getCreateTableSQL(SQLTableManager<?, ?> table)
	{
		return getCreateTableSQL(table, true, true);
	}
}

