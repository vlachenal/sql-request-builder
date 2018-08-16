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


/**
 * SQL from table builder
 *
 * @author Vincent Lachenal
 */
public class FromBuilder extends AbstractPartBuilder {

  // Constructors +
  /**
   * {@link FromBuilder} constructor
   *
   * @param select the base {@link SelectBuilder}
   * @param table the table
   */
  public FromBuilder(final SelectBuilder select, final String table) {
    super(select);
    select.buffer.append(" FROM ").append(table);
  }

  /**
   * {@link FromBuilder} constructor
   *
   * @param select the base {@link SelectBuilder}
   * @param subquery the subquery
   */
  public FromBuilder(final SelectBuilder select, final SelectBuilder subquery) {
    super(select);
    select.buffer.append(" FROM (").append(subquery).append(')');
    select.values.addAll(subquery.values);
  }

  /**
   * {@link FromBuilder} constructor
   *
   * @param select the base {@link SelectBuilder}
   * @param subquery the subquery
   */
  public FromBuilder(final SelectBuilder select, final SQLQuery subquery) {
    super(select);
    select.buffer.append(" FROM (").append(subquery.getQuery()).append(')');
    select.values.addAll(subquery.getValues());
  }

  /**
   * {@link FromBuilder} constructor
   *
   * @param select the base {@link SelectBuilder}
   * @param subquery the subquery
   * @param alias the 'table' alias
   */
  public FromBuilder(final SelectBuilder select, final SelectBuilder subquery, final String alias) {
    this(select, subquery);
    select.buffer.append(' ').append(alias);
  }

  /**
   * {@link FromBuilder} constructor
   *
   * @param select the base {@link SelectBuilder}
   * @param subquery the subquery
   * @param alias the 'table' alias
   */
  public FromBuilder(final SelectBuilder select, final SQLQuery subquery, final String alias) {
    this(select, subquery);
    select.buffer.append(' ').append(alias);
  }
  // Constructors -


  // Methods +
  /**
   * Add table to 'from' part (old school join)
   *
   * @param table the table to add
   *
   * @return {@code this}
   */
  public FromBuilder selfJoin(final String table) {
    select.buffer.append(',').append(table);
    return this;
  }

  /**
   * Add table to 'from' part (old school join)
   *
   * @param subquery the subquery to add as table
   * @param alias the 'table' alias
   *
   * @return {@code this}
   */
  public FromBuilder selfJoin(final SQLQuery subquery, final String alias) {
    select.buffer.append(",(").append(subquery.getQuery()).append(") ").append(alias);
    select.values.addAll(subquery.getValues());
    return this;
  }

  /**
   * Add table to 'from' part (old school join)
   *
   * @param subquery the subquery to add as table
   * @param alias the 'table' alias
   *
   * @return {@code this}
   */
  public FromBuilder selfJoin(final SelectBuilder subquery, final String alias) {
    select.buffer.append(",(").append(subquery).append(") ").append(alias);
    select.values.addAll(subquery.values);
    return this;
  }

  /**
   * Add inner join on other table
   *
   * @param table the table
   *
   * @return {@code this}
   */
  public FromBuilder join(final String table) {
    return innerJoin(table);
  }

  /**
   * Add inner join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   *
   * @return {@code this}
   */
  public FromBuilder join(final SQLQuery subquery, final String alias) {
    return innerJoin(subquery, alias);
  }

  /**
   * Add inner join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   *
   * @return {@code this}
   */
  public FromBuilder join(final SelectBuilder subquery, final String alias) {
    return innerJoin(subquery, alias);
  }

  /**
   * Add inner join on other table
   *
   * @param table the table
   *
   * @return {@code this}
   */
  public FromBuilder innerJoin(final String table) {
    select.buffer.append(" INNER JOIN ").append(table);
    return this;
  }

  /**
   * Add inner join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   *
   * @return {@code this}
   */
  public FromBuilder innerJoin(final SQLQuery subquery, final String alias) {
    select.buffer.append(" INNER JOIN (").append(subquery.getQuery()).append(") ").append(alias);
    select.values.addAll(subquery.getValues());
    return this;
  }

  /**
   * Add inner join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   *
   * @return {@code this}
   */
  public FromBuilder innerJoin(final SelectBuilder subquery, final String alias) {
    select.buffer.append(" INNER JOIN (").append(subquery.toString()).append(") ").append(alias);
    select.values.addAll(subquery.values);
    return this;
  }

  /**
   * Add left outer join on other table
   *
   * @param table the table
   *
   * @return {@code this}
   */
  public FromBuilder leftJoin(final String table) {
    return leftOuterJoin(table);
  }

  /**
   * Add left outer join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   *
   * @return {@code this}
   */
  public FromBuilder leftJoin(final SQLQuery subquery, final String alias) {
    return leftOuterJoin(subquery, alias);
  }

  /**
   * Add left outer join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   *
   * @return {@code this}
   */
  public FromBuilder leftJoin(final SelectBuilder subquery, final String alias) {
    return leftOuterJoin(subquery, alias);
  }

  /**
   * Add left outer join on other table
   *
   * @param table the table
   *
   * @return {@code this}
   */
  public FromBuilder leftOuterJoin(final String table) {
    select.buffer.append(" LEFT OUTER JOIN ").append(table);
    return this;
  }

  /**
   * Add left outer join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   *
   * @return {@code this}
   */
  public FromBuilder leftOuterJoin(final SQLQuery subquery, final String alias) {
    select.buffer.append(" LEFT OUTER JOIN ").append(subquery.getQuery()).append(alias);
    select.values.addAll(subquery.getValues());
    return this;
  }

  /**
   * Add left outer join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   *
   * @return {@code this}
   */
  public FromBuilder leftOuterJoin(final SelectBuilder subquery, final String alias) {
    select.buffer.append(" LEFT OUTER JOIN (").append(subquery).append(") ").append(alias);
    select.values.addAll(subquery.values);
    return this;
  }

  /**
   * Add right outer join on other table
   *
   * @param table the table
   *
   * @return {@code this}
   */
  public FromBuilder rightJoin(final String table) {
    return rightOuterJoin(table);
  }

  /**
   * Add right outer join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   *
   * @return {@code this}
   */
  public FromBuilder rightJoin(final SQLQuery subquery, final String alias) {
    return rightOuterJoin(subquery, alias);
  }

  /**
   * Add right outer join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   *
   * @return {@code this}
   */
  public FromBuilder rightJoin(final SelectBuilder subquery, final String alias) {
    return rightOuterJoin(subquery, alias);
  }

  /**
   * Add right outer join on other table
   *
   * @param table the table
   *
   * @return {@code this}
   */
  public FromBuilder rightOuterJoin(final String table) {
    select.buffer.append(" RIGHT OUTER JOIN ").append(table);
    return this;
  }

  /**
   * Add right outer join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   *
   * @return {@code this}
   */
  public FromBuilder rightOuterJoin(final SQLQuery subquery, final String alias) {
    select.buffer.append(" RIGHT OUTER JOIN (").append(subquery.getQuery()).append(") ").append(alias);
    select.values.addAll(subquery.getValues());
    return this;
  }

  /**
   * Add right outer join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   *
   * @return {@code this}
   */
  public FromBuilder rightOuterJoin(final SelectBuilder subquery, final String alias) {
    select.buffer.append(" RIGHT OUTER JOIN (").append(subquery).append(") ").append(alias);
    select.values.addAll(subquery.values);
    return this;
  }

  /**
   * Add full outer join on other table
   *
   * @param table the table
   *
   * @return {@code this}
   */
  public FromBuilder fullJoin(final String table) {
    return fullOuterJoin(table);
  }

  /**
   * Add full outer join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   *
   * @return {@code this}
   */
  public FromBuilder fullJoin(final SQLQuery subquery, final String alias) {
    return fullOuterJoin(subquery, alias);
  }

  /**
   * Add full outer join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   *
   * @return {@code this}
   */
  public FromBuilder fullJoin(final SelectBuilder subquery, final String alias) {
    return fullOuterJoin(subquery, alias);
  }

  /**
   * Add full outer join on other table
   *
   * @param table the table
   *
   * @return {@code this}
   */
  public FromBuilder fullOuterJoin(final String table) {
    select.buffer.append(" FULL OUTER JOIN ").append(table);
    return this;
  }

  /**
   * Add full outer join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   *
   * @return {@code this}
   */
  public FromBuilder fullOuterJoin(final SQLQuery subquery, final String alias) {
    select.buffer.append(" FULL OUTER JOIN (").append(subquery.getQuery()).append(") ").append(alias);
    select.values.addAll(subquery.getValues());
    return this;
  }

  /**
   * Add full outer join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   *
   * @return {@code this}
   */
  public FromBuilder fullOuterJoin(final SelectBuilder subquery, final String alias) {
    select.buffer.append(" FULL OUTER JOIN (").append(subquery).append(") ").append(alias);
    select.values.addAll(subquery.values);
    return this;
  }

  /**
   * Add natural join on other table
   *
   * @param table the table
   *
   * @return {@code this}
   */
  public FromBuilder naturalJoin(final String table) {
    select.buffer.append(" NATURAL JOIN ").append(table);
    return this;
  }

  /**
   * Add natural join on other 'table'
   *
   * @param subquery the subquery
   * @param alias the 'table' alias
   *
   * @return {@code this}
   */
  public FromBuilder naturalJoin(final SelectBuilder subquery, final String alias) {
    select.buffer.append(" NATURAL JOIN (").append(subquery).append(") ").append(alias);
    select.values.addAll(subquery.values);
    return this;
  }

  /**
   * Add natural join on other 'table'
   *
   * @param subquery the subquery
   * @param alias the 'table' alias
   *
   * @return {@code this}
   */
  public FromBuilder naturalJoin(final SQLQuery subquery, final String alias) {
    select.buffer.append(" NATURAL JOIN (").append(subquery).append(") ").append(alias);
    select.values.addAll(subquery.getValues());
    return this;
  }

  /**
   * Add cross join on other table
   *
   * @param table the table
   *
   * @return {@code this}
   */
  public FromBuilder crossJoin(final String table) {
    select.buffer.append(" CROSS JOIN ").append(table);
    return this;
  }

  /**
   * Add cross join on other 'table'
   *
   * @param subquery the subquery
   * @param alias the 'table' alias
   *
   * @return {@code this}
   */
  public FromBuilder crossJoin(final SelectBuilder subquery, final String alias) {
    select.buffer.append(" CROSS JOIN (").append(subquery).append(") ").append(alias);
    select.values.addAll(subquery.values);
    return this;
  }

  /**
   * Add cross join on other 'table'
   *
   * @param subquery the subquery
   * @param alias the 'table' alias
   *
   * @return {@code this}
   */
  public FromBuilder crossJoin(final SQLQuery subquery, final String alias) {
    select.buffer.append(" CROSS JOIN (").append(subquery).append(") ").append(alias);
    select.values.addAll(subquery.getValues());
    return this;
  }

  /**
   * Add clauses to join
   *
   * @param clauses the clauses to add
   *
   * @return {@code this}
   */
  public FromBuilder on(final ClausesBuilder clauses) {
    select.buffer.append(" ON ").append(clauses.buffer);
    select.values.addAll(clauses.values);
    return this;
  }

  /**
   * Add 'WHERE' and clauses
   *
   * @param clauses the clauses to add
   *
   * @return the {@link SelectBuilder}
   */
  public SelectBuilder where(final ClausesBuilder clauses) {
    if(!clauses.firstClause) { // which means that at least one clause has been added
      select.buffer.append(" WHERE ").append(clauses.buffer);
      select.values.addAll(clauses.values);
    }
    return select;
  }
  // Methods -

}
