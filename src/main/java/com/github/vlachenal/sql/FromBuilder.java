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
   * @param clauses the join clauses
   *
   * @return {@code this}
   */
  public FromBuilder join(final String table, final ClausesBuilder clauses) {
    return innerJoin(table, clauses);
  }

  /**
   * Add inner join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   * @param clauses the join clauses
   *
   * @return {@code this}
   */
  public FromBuilder join(final SQLQuery subquery, final String alias, final ClausesBuilder clauses) {
    return innerJoin(subquery, alias, clauses);
  }

  /**
   * Add inner join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   * @param clauses the join clauses
   *
   * @return {@code this}
   */
  public FromBuilder join(final SelectBuilder subquery, final String alias, final ClausesBuilder clauses) {
    return innerJoin(subquery, alias, clauses);
  }

  /**
   * Add inner join on other table
   *
   * @param table the table
   * @param clauses the join clauses
   *
   * @return {@code this}
   */
  public FromBuilder innerJoin(final String table, final ClausesBuilder clauses) {
    addJoin(" INNER JOIN ", table, clauses);
    return this;
  }

  /**
   * Add inner join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   * @param clauses the join clauses
   *
   * @return {@code this}
   */
  public FromBuilder innerJoin(final SQLQuery subquery, final String alias, final ClausesBuilder clauses) {
    addJoin(" INNER JOIN ", subquery, alias, clauses);
    return this;
  }

  /**
   * Add inner join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   * @param clauses the join clauses
   *
   * @return {@code this}
   */
  public FromBuilder innerJoin(final SelectBuilder subquery, final String alias, final ClausesBuilder clauses) {
    addJoin(" INNER JOIN ", subquery, alias, clauses);
    return this;
  }

  /**
   * Add left outer join on other table
   *
   * @param table the table
   *
   * @return {@code this}
   */
  public FromBuilder leftJoin(final String table, final ClausesBuilder clauses) {
    return leftOuterJoin(table, clauses);
  }

  /**
   * Add left outer join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   * @param clauses the join clauses
   *
   * @return {@code this}
   */
  public FromBuilder leftJoin(final SQLQuery subquery, final String alias, final ClausesBuilder clauses) {
    return leftOuterJoin(subquery, alias, clauses);
  }

  /**
   * Add left outer join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   * @param clauses the join clauses
   *
   * @return {@code this}
   */
  public FromBuilder leftJoin(final SelectBuilder subquery, final String alias, final ClausesBuilder clauses) {
    return leftOuterJoin(subquery, alias, clauses);
  }

  /**
   * Add left outer join on other table
   *
   * @param table the table
   * @param clauses the join clauses
   *
   * @return {@code this}
   */
  public FromBuilder leftOuterJoin(final String table, final ClausesBuilder clauses) {
    addJoin(" LEFT OUTER JOIN ", table, clauses);
    return this;
  }

  /**
   * Add left outer join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   * @param clauses the join clauses
   *
   * @return {@code this}
   */
  public FromBuilder leftOuterJoin(final SQLQuery subquery, final String alias, final ClausesBuilder clauses) {
    addJoin(" LEFT OUTER JOIN ", subquery, alias, clauses);
    return this;
  }

  /**
   * Add left outer join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   * @param clauses the join clauses
   *
   * @return {@code this}
   */
  public FromBuilder leftOuterJoin(final SelectBuilder subquery, final String alias, final ClausesBuilder clauses) {
    addJoin(" LEFT OUTER JOIN ", subquery, alias, clauses);
    return this;
  }

  /**
   * Add right outer join on other table
   *
   * @param table the table
   * @param clauses the join clauses
   *
   * @return {@code this}
   */
  public FromBuilder rightJoin(final String table, final ClausesBuilder clauses) {
    return rightOuterJoin(table, clauses);
  }

  /**
   * Add right outer join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   * @param clauses the join clauses
   *
   * @return {@code this}
   */
  public FromBuilder rightJoin(final SQLQuery subquery, final String alias, final ClausesBuilder clauses) {
    return rightOuterJoin(subquery, alias, clauses);
  }

  /**
   * Add right outer join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   * @param clauses the join clauses
   *
   * @return {@code this}
   */
  public FromBuilder rightJoin(final SelectBuilder subquery, final String alias, final ClausesBuilder clauses) {
    return rightOuterJoin(subquery, alias, clauses);
  }

  /**
   * Add right outer join on other table
   *
   * @param table the table
   * @param clauses the join clauses
   *
   * @return {@code this}
   */
  public FromBuilder rightOuterJoin(final String table, final ClausesBuilder clauses) {
    addJoin(" RIGHT OUTER JOIN ", table, clauses);
    return this;
  }

  /**
   * Add right outer join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   * @param clauses the join clauses
   *
   * @return {@code this}
   */
  public FromBuilder rightOuterJoin(final SQLQuery subquery, final String alias, final ClausesBuilder clauses) {
    addJoin(" RIGHT OUTER JOIN ", subquery, alias, clauses);
    return this;
  }

  /**
   * Add right outer join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   * @param clauses the join clauses
   *
   * @return {@code this}
   */
  public FromBuilder rightOuterJoin(final SelectBuilder subquery, final String alias, final ClausesBuilder clauses) {
    addJoin(" RIGHT OUTER JOIN ", subquery, alias, clauses);
    return this;
  }

  /**
   * Add full outer join on other table
   *
   * @param table the table
   * @param clauses the join clauses
   *
   * @return {@code this}
   */
  public FromBuilder fullJoin(final String table, final ClausesBuilder clauses) {
    return fullOuterJoin(table, clauses);
  }

  /**
   * Add full outer join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   * @param clauses the join clauses
   *
   * @return {@code this}
   */
  public FromBuilder fullJoin(final SQLQuery subquery, final String alias, final ClausesBuilder clauses) {
    return fullOuterJoin(subquery, alias, clauses);
  }

  /**
   * Add full outer join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   * @param clauses the join clauses
   *
   * @return {@code this}
   */
  public FromBuilder fullJoin(final SelectBuilder subquery, final String alias, final ClausesBuilder clauses) {
    return fullOuterJoin(subquery, alias, clauses);
  }

  /**
   * Add full outer join on other table
   *
   * @param table the table
   * @param clauses the join clauses
   *
   * @return {@code this}
   */
  public FromBuilder fullOuterJoin(final String table, final ClausesBuilder clauses) {
    addJoin(" FULL OUTER JOIN ", table, clauses);
    return this;
  }

  /**
   * Add full outer join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   * @param clauses the join clauses
   *
   * @return {@code this}
   */
  public FromBuilder fullOuterJoin(final SQLQuery subquery, final String alias, final ClausesBuilder clauses) {
    addJoin(" FULL OUTER JOIN ", subquery, alias, clauses);
    return this;
  }

  /**
   * Add full outer join on other table
   *
   * @param subquery the subquery to use as table
   * @param alias the 'table' alias
   * @param clauses the join clauses
   *
   * @return {@code this}
   */
  public FromBuilder fullOuterJoin(final SelectBuilder subquery, final String alias, final ClausesBuilder clauses) {
    addJoin(" FULL OUTER JOIN ", subquery, alias, clauses);
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
   * Add join
   *
   * @param join the join type
   * @param table the table
   * @param clauses the join clauses
   */
  private void addJoin(final String join, final String table, final ClausesBuilder clauses) {
    if(!clauses.firstClause) {
      select.buffer.append(join).append(table).append(" ON ").append(clauses.buffer);
      select.values.addAll(clauses.values);
    }
  }

  /**
   * Add join
   *
   * @param join the join type
   * @param subquery the subquery
   * @param alias the 'table' alias
   * @param clauses the join clauses
   */
  private void addJoin(final String join, final SelectBuilder subquery, final String alias, final ClausesBuilder clauses) {
    if(!clauses.firstClause) {
      select.buffer.append(join).append('(').append(subquery).append(") ").append(alias).append(" ON ").append(clauses.buffer);
      select.values.addAll(subquery.values);
      select.values.addAll(clauses.values);
    }
  }

  /**
   * Add join
   *
   * @param join the join type
   * @param subquery the subquery
   * @param alias the 'table' alias
   * @param clauses the join clauses
   */
  private void addJoin(final String join, final SQLQuery subquery, final String alias, final ClausesBuilder clauses) {
    if(!clauses.firstClause) {
      select.buffer.append(join).append('(').append(subquery.getQuery()).append(") ").append(alias).append(" ON ").append(clauses.buffer);
      select.values.addAll(subquery.getValues());
      select.values.addAll(clauses.values);
    }
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
