/*
  SQL request builder:  Dynamic SQL request builder for java
  Copyright (C) 2012 Vincent Lachenal

  This file is part of SQL request builder.

  SQL request builder is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  SQL request builder is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with SQL request builder. If not, see <http://www.gnu.org/licenses/>.
*/
package com.github.vlachenal.sql;

import java.util.Collections;
import java.util.List;


/**
 * SQL query.<br>
 * <br>
 * This is the result of the builders. It provides:
 * <ul>
 * <li>query to send to database</li>
 * <li>values to add to prepared statement</li>
 * </ul>
 *
 * @since 0.1
 *
 * @author Vincent Lachenal
 */
public class SQLQuery {

  // Attributes +
  /** SQL query */
  private final String query;

  /** Prepared statement values */
  private final List<Object> values;
  // Attributes -


  // Constructors +
  /**
   * {@link SQLQuery} constructor
   *
   * @param query the query
   * @param values the values
   */
  public SQLQuery(final String query, final List<Object> values) {
    this.query = query;
    this.values = Collections.unmodifiableList(values);
  }
  // Constructors -


  // Accessors +
  /**
   * SQL query getter
   *
   * @return the query
   */
  public final String getQuery() {
    return query;
  }

  /**
   * Prepared statement values getter
   *
   * @return the values
   */
  public final List<Object> getValues() {
    return values;
  }

  /**
   * Prepared statement values' array getter
   *
   * @return the values' array
   */
  public final Object[] values() {
    return values.toArray();
  }
  // Accessors -

}
