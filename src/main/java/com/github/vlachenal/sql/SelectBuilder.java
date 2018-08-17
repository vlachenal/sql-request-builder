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

import java.util.ArrayList;
import java.util.List;


/**
 * SQL SELECT query builder
 *
 * @author Vincent Lachenal
 */
public class SelectBuilder {

  // Attributes +
  /** SQL request string buffer */
  final StringBuilder buffer;

  /** Clauses values */
  final List<Object> values;
  // Attributes -


  // Constructors +
  /**
   * {@link SelectBuilder} constructor
   */
  public SelectBuilder() {
    buffer = new StringBuilder("SELECT ");
    values = new ArrayList<>();
  }
  // Constructors -


  // Methods +
  /**
   * Add DISTINCT instruction
   *
   * @return {@code this}
   */
  public SelectBuilder distinct() {
    buffer.append("DISTINCT ");
    return this;
  }

  /**
   * Get SQL query.<br>
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return buffer.toString();
  }

  /**
   * Return a new field builder
   *
   * @param column the selected column
   *
   * @return the new field builder
   */
  public FieldsBuilder field(final String column) {
    return new FieldsBuilder(this, column);
  }

  /**
   * Add SQL 'group by' instruction
   *
   * @param column the first column to group by
   *
   * @return a new {@link FieldsBuilder} to add other columns
   */
  public FieldsBuilder groupBy(final String column) {
    buffer.append(" GROUP BY ");
    return new FieldsBuilder(this, column);
  }

  /**
   * Add SQL 'order by' instruction.
   *
   * @param column the first column to order by
   *
   * @return a new {@link FieldsBuilder} to add other columns
   */
  public FieldsBuilder orderBy(final String column) {
    buffer.append(" ORDER BY ");
    return new FieldsBuilder(this, column);
  }

  /**
   * Add SQL HAVING instruction and its clauses if not empty
   *
   * @param clauses the clauses
   *
   * @return {@code this}
   */
  public SelectBuilder having(final ClausesBuilder clauses) {
    if(!clauses.firstClause) {
      buffer.append(" HAVING ").append(clauses.buffer);
      values.addAll(clauses.values);
    }
    return this;
  }

  /**
   * Add union to other SQL request
   *
   * @param other the other SQL request
   *
   * @return {@code this}
   */
  public SelectBuilder union(final SelectBuilder other) {
    buffer.append(" UNION ").append(other.buffer);
    return this;
  }

  /**
   * Add union to other SQL request with duplicate entries
   *
   * @param other the other SQL request
   *
   * @return {@code this}
   */
  public SelectBuilder unionAll(final SelectBuilder other) {
    buffer.append(" UNION ALL ").append(other.buffer);
    return this;
  }

  /**
   * Build SQL query
   *
   * @return the query and its prepared statement values
   */
  public SQLQuery build() {
    return new SQLQuery(buffer.toString(), values);
  }
  // Methods -

}
