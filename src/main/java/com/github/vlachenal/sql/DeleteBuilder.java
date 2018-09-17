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
 * SQL {@code DELETE} request builder.<br>
 * This class should be instanciated through {@link SQL} static method.
 *
 * @since 0.5
 *
 * @author Vincent Lachenal
 */
public class DeleteBuilder {

  // Attributes +
  /** SQL request string buffer */
  final StringBuilder buffer;

  /** Clauses values */
  final List<Object> values;
  // Attributes -


  // Constructors +
  /**
   * {@link DeleteBuilder} constructor
   *
   * @param table the table to update
   */
  public DeleteBuilder(final String table) {
    buffer = new StringBuilder("DELETE FROM ").append(table);
    values = new ArrayList<>();
  }
  // Constructors -


  // Methods +
  /**
   * Add 'WHERE' and clauses
   *
   * @param clauses the clauses to add
   *
   * @return the {@link DeleteBuilder}
   */
  public DeleteBuilder where(final ClausesBuilder clauses) {
    if(!clauses.firstClause) { // which means that at least one clause has been added
      buffer.append(" WHERE ").append(clauses.buffer);
      values.addAll(clauses.values);
    }
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
