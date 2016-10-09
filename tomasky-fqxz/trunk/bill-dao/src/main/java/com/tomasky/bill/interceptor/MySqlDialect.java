package com.tomasky.bill.interceptor;

public class MySqlDialect extends Dialect {

	@Override
	public String getLimitString(String sql, int offset, int limit) {
		sql = sql.trim();
		StringBuffer sb = new StringBuffer();
		sb.append(sql);
		sb.append(" limit " + offset);
		sb.append(", " + limit);
		return sb.toString();
	}

}
