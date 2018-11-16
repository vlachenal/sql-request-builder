/*
  SQL request builder:  Dynamic SQL request builder for java
  Copyright (C) 2018 Vincent Lachenal

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


/**
 * Field builder.<br>
 * This class will be used to append selected 'columns' in {@code SELECT} queries
 * and in {@code ORDER BY} and {@code GROUP BY} command.
 *
 * @since 0.1
 *
 * @author Vincent Lachenal
 */
public class FieldsBuilder extends AbstractPartBuilder {

  // Constructors +
  /**
   * {@link FieldsBuilder} constructor
   *
   * @param select the select query builder
   * @param column the selected column
   */
  public FieldsBuilder(final SelectBuilder select, final String column) {
    super(select);
    select.buffer.append(column);
  }
  // Constructors -


  // Methods +
  /**
   * Add new field
   *
   * @param column the column
   *
   * @return {@code this}
   */
  public FieldsBuilder field(final String column) {
    select.buffer.append(',').append(column);
    return this;
  }

  /**
   * Add {@code AS} alias on field
   *
   * @param alias the alias
   *
   * @return {@code this}
   */
  public FieldsBuilder as(final String alias) {
    select.buffer.append(" AS ").append(alias);
    return this;
  }

  /**
   * Add {@code ASC} order by direction.
   *
   * @return {@code this}
   */
  public FieldsBuilder asc() {
    select.buffer.append(" ASC");
    return this;
  }

  /**
   * Add {@code DESC} order by direction.
   *
   * @return {@code this}
   */
  public FieldsBuilder desc() {
    select.buffer.append(" DESC");
    return this;
  }

  /**
   * Switch to 'from' builder
   *
   * @param table the table where columns are selected from
   *
   * @return the new {@link FromBuilder}
   */
  public FromBuilder from(final String table) {
    return new FromBuilder(select, table);
  }

  /**
   * Switch to 'from' builder
   *
   * @param subquery the 'table' where columns are selected from
   *
   * @return the new {@link FromBuilder}
   */
  public FromBuilder from(final SelectBuilder subquery) {
    return new FromBuilder(select, subquery);
  }

  /**
   * Switch to 'from' builder
   *
   * @param subquery the 'table' where columns are selected from
   *
   * @return the new {@link FromBuilder}
   */
  public FromBuilder from(final SQLQuery subquery) {
    return new FromBuilder(select, subquery);
  }

  /**
   * Switch to 'from' builder
   *
   * @param subquery the 'table' where columns are selected from
   * @param alias the 'table' alias
   *
   * @return the new {@link FromBuilder}
   */
  public FromBuilder from(final SelectBuilder subquery, final String alias) {
    return new FromBuilder(select, subquery, alias);
  }

  /**
   * Switch to 'from' builder
   *
   * @param subquery the 'table' where columns are selected from
   * @param alias the 'table' alias
   *
   * @return the new {@link FromBuilder}
   */
  public FromBuilder from(final SQLQuery subquery, final String alias) {
    return new FromBuilder(select, subquery, alias);
  }
  // Methods -

}
