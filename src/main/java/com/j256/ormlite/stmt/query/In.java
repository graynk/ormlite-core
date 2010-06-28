package com.j256.ormlite.stmt.query;

import java.util.Arrays;
import java.util.List;

import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.Where;

/**
 * Internal class handling the SQL 'in' query part. Used by {@link Where#in}.
 * 
 * @author graywatson
 */
public class In extends BaseComparison {

	private Iterable<?> objects;

	public In(String columnName, boolean isNumber, Iterable<?> objects) {
		super(columnName, isNumber, null);
		this.objects = objects;
	}

	public In(String columnName, boolean isNumber, Object[] objects) {
		super(columnName, isNumber, null);
		// grrrr, Object[] should be Iterable
		this.objects = Arrays.asList(objects);
	}

	@Override
	public StringBuilder appendOperation(StringBuilder sb) {
		sb.append("in ");
		return sb;
	}

	@Override
	public StringBuilder appendValue(DatabaseType databaseType, StringBuilder sb, List<SelectArg> columnArgList) {
		sb.append('(');
		boolean first = true;
		for (Object value : objects) {
			if (value == null) {
				throw new IllegalArgumentException("one of the IN values for '" + columnName + "' is null");
			}
			if (first) {
				first = false;
			} else {
				sb.append(',');
			}
			// for each of our arguments, add it to the output
			super.appendArgOrValue(databaseType, sb, columnArgList, value);
		}
		sb.append(") ");
		return sb;
	}
}