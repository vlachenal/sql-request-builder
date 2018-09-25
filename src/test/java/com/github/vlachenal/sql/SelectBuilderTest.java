package com.github.vlachenal.sql;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;


/**
 * Select builder unit tests.
 *
 * @author Vincent Lachenal
 */
@DisplayName("SELECT request builder unit tests")
@Execution(ExecutionMode.CONCURRENT)
public class SelectBuilderTest {

  // Tests +
  /**
   * Test SQL query without from and where part (like in PostgreSQL store procedures)
   */
  @Test
  @DisplayName("SELECT without FROM nor WHERE")
  public void testPgStoreProcedure() {
    final SQLQuery query = SQL.select().field("nextval()").build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT nextval()", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select single column on single table without clauses
   */
  @Test
  @DisplayName("SELECT one column FROM table whitout where")
  public void testQuerySingleColSingleTableWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("titi")
        .from("toto")
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT titi FROM toto", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select single column on single table without clauses
   */
  @Test
  @DisplayName("SELECT DISTINCT one column FROM table whitout where")
  public void testQueryDistinctSingleColSingleTableWithoutWhere() {
    final SQLQuery query = SQL.select().distinct()
        .field("titi")
        .from("toto")
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT DISTINCT titi FROM toto", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select single column on single table without clauses
   */
  @Test
  @DisplayName("SELECT one column with alias FROM table whitout where")
  public void testQuerySingleColWithAliasSingleTableWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("titi").as("t")
        .from("toto")
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT titi AS t FROM toto", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select on single table without clauses
   */
  @Test
  @DisplayName("SELECT two columns FROM table whitout where")
  public void testQuerySingleTableWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("titi")
        .field("tata")
        .from("toto")
        .build();
    assertAll(() -> assertEquals("SELECT titi,tata FROM toto", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select without clauses
   */
  @Test
  @DisplayName("Self join")
  public void testQueryWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .selfJoin("tutu u")
        .build();
    assertAll(() -> assertEquals("SELECT t.titi,u.tata FROM toto t,tutu u", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select without clauses
   */
  @Test
  @DisplayName("Self join subquery")
  public void testQueryWithSubqueryWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .selfJoin(SQL.select().field("*").from("tutu").done(), "u")
        .selfJoin(SQL.select().field("*").from("titi").build(), "i")
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,u.tata "
                  + "FROM toto t,"
                  + "(SELECT * FROM tutu) u,"
                  + "(SELECT * FROM titi) i", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select without clauses
   */
  @Test
  @DisplayName("Natural join")
  public void testQueryNaturalJoinWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .naturalJoin("tutu u")
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,u.tata FROM toto t NATURAL JOIN tutu u",
                                 query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select without clauses
   */
  @Test
  @DisplayName("Natural join subquery")
  public void testQueryNaturalJoinWithSubqueryWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .naturalJoin(SQL.select().field("*").from("tutu").done(), "u")
        .naturalJoin(SQL.select().field("*").from("titi").build(), "i")
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,u.tata "
        + "FROM toto t "
        + "NATURAL JOIN (SELECT * FROM tutu) u "
        + "NATURAL JOIN (SELECT * FROM titi) i", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select without clauses
   */
  @Test
  @DisplayName("Cross join")
  public void testQueryCrossJoinWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .crossJoin("tutu u")
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,u.tata FROM toto t CROSS JOIN tutu u",
                                 query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select without clauses
   */
  @Test
  @DisplayName("Cross join subquery")
  public void testQueryCrossJoinWithSubqueryWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .crossJoin(SQL.select().field("*").from("tutu").done(), "u")
        .crossJoin(SQL.select().field("*").from("titi").build(), "i")
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,u.tata "
        + "FROM toto t "
        + "CROSS JOIN (SELECT * FROM tutu) u "
        + "CROSS JOIN (SELECT * FROM titi) i", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select without clauses
   */
  @Test
  @DisplayName("Inner join")
  public void testQueryInnerJoinWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .innerJoin("tutu u", SQL.clauses().field("t.i").equals().field("u.a"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,u.tata FROM toto t INNER JOIN tutu u ON t.i = u.a",
                                 query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select without clauses
   */
  @Test
  @DisplayName("Join with invalid clause")
  public void testQueryJoinWithoutValidClause() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .innerJoin("tutu u", SQL.clauses().field("t.i").equals().field("u.a"))
        .leftOuterJoin("tata a", SQL.clauses("a.a", Clauses::equalsTo, null))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,u.tata FROM toto t INNER JOIN tutu u ON t.i = u.a",
                                 query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select without clauses
   */
  @Test
  @DisplayName("Inner join with prepared statement value")
  public void testQueryInnerJoinWithOptionalValueWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .innerJoin("tutu u", SQL.clauses().field("t.i").equals().field("u.a")
                   .and("u.u", Clauses::equalsTo, "UHU")
            )
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,u.tata FROM toto t "
        + "INNER JOIN tutu u ON t.i = u.a AND u.u = ?", query.getQuery()),
              () -> assertEquals(Stream.of("UHU").collect(Collectors.toList()), query.getValues()));
  }

  /**
   * Test select without clauses
   */
  @Test
  @DisplayName("Inner join subquery")
  public void testQueryInnerJoinWithSubqueryWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .innerJoin(SQL.select().field("*").from("tutu").done(), "u", SQL.clauses().field("t.i").equals().field("u.a"))
        .innerJoin(SQL.select().field("*").from("titi").build(), "i", SQL.clauses().field("t.i").equals().field("i.a"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,u.tata "
        + "FROM toto t "
        + "INNER JOIN (SELECT * FROM tutu) u ON t.i = u.a "
        + "INNER JOIN (SELECT * FROM titi) i ON t.i = i.a", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select without clauses
   */
  @Test
  @DisplayName("Join")
  public void testQueryJoinWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .join("tutu u", SQL.clauses().field("t.i").equals().field("u.a"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,u.tata FROM toto t INNER JOIN tutu u ON t.i = u.a",
                                 query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select without clauses
   */
  @Test
  @DisplayName("Join with subquery")
  public void testQueryJoinWithSubqueryWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .join(SQL.select().field("*").from("tutu").done(), "u", SQL.clauses().field("t.i").equals().field("u.a"))
        .join(SQL.select().field("*").from("titi").build(), "i", SQL.clauses().field("t.i").equals().field("i.a"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,u.tata "
        + "FROM toto t "
        + "INNER JOIN (SELECT * FROM tutu) u ON t.i = u.a "
        + "INNER JOIN (SELECT * FROM titi) i ON t.i = i.a", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select without clauses
   */
  @Test
  @DisplayName("Left outer join")
  public void testQueryLeftOuterJoinWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .leftOuterJoin("tutu u", SQL.clauses().field("t.i").equals().field("u.a"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,u.tata FROM toto t LEFT OUTER JOIN tutu u ON t.i = u.a", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select without clauses
   */
  @Test
  @DisplayName("Left outer join with subquery")
  public void testQueryLeftOuterJoinWithSubqueryWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .leftOuterJoin(SQL.select().field("*").from("tutu").done(), "u", SQL.clauses().field("t.i").equals().field("u.a"))
        .leftOuterJoin(SQL.select().field("*").from("titi").build(), "i", SQL.clauses().field("t.i").equals().field("i.a"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,u.tata "
        + "FROM toto t "
        + "LEFT OUTER JOIN (SELECT * FROM tutu) u ON t.i = u.a "
        + "LEFT OUTER JOIN (SELECT * FROM titi) i ON t.i = i.a", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select without clauses
   */
  @Test
  @DisplayName("Left outer join")
  public void testQueryLeftJoinWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .leftJoin("tutu u", SQL.clauses().field("t.i").equals().field("u.a"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,u.tata FROM toto t LEFT OUTER JOIN tutu u ON t.i = u.a", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select without clauses
   */
  @Test
  @DisplayName("Left outer join subquery")
  public void testQueryLeftJoinWithSubqueryWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .leftJoin(SQL.select().field("*").from("tutu").done(), "u", SQL.clauses().field("t.i").equals().field("u.a"))
        .leftJoin(SQL.select().field("*").from("titi").build(), "i", SQL.clauses().field("t.i").equals().field("i.a"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,u.tata "
        + "FROM toto t "
        + "LEFT OUTER JOIN (SELECT * FROM tutu) u ON t.i = u.a "
        + "LEFT OUTER JOIN (SELECT * FROM titi) i ON t.i = i.a", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select without clauses
   */
  @Test
  @DisplayName("Right outer join")
  public void testQueryRightOuterJoinWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .rightOuterJoin("tutu u", SQL.clauses().field("t.i").equals().field("u.a"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,u.tata FROM toto t RIGHT OUTER JOIN tutu u ON t.i = u.a",
                                 query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select without clauses
   */
  @Test
  @DisplayName("Right outer join with subquery")
  public void testQueryRightOuterJoinWithSubqueryWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .rightOuterJoin(SQL.select().field("*").from("tutu").done(), "u", SQL.clauses().field("t.i").equals().field("u.a"))
        .rightOuterJoin(SQL.select().field("*").from("titi").build(), "i", SQL.clauses().field("t.i").equals().field("i.a"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,u.tata "
        + "FROM toto t "
        + "RIGHT OUTER JOIN (SELECT * FROM tutu) u ON t.i = u.a "
        + "RIGHT OUTER JOIN (SELECT * FROM titi) i ON t.i = i.a", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select without clauses
   */
  @Test
  @DisplayName("Right join")
  public void testQueryRightJoinWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .rightJoin("tutu u", SQL.clauses().field("t.i").equals().field("u.a"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,u.tata FROM toto t RIGHT OUTER JOIN tutu u ON t.i = u.a", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select without clauses
   */
  @Test
  @DisplayName("Right join with subquery")
  public void testQueryRightJoinWithSubqueryWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .rightJoin(SQL.select().field("*").from("tutu").done(), "u", SQL.clauses().field("t.i").equals().field("u.a"))
        .rightJoin(SQL.select().field("*").from("titi").build(), "i", SQL.clauses().field("t.i").equals().field("i.a"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,u.tata "
        + "FROM toto t "
        + "RIGHT OUTER JOIN (SELECT * FROM tutu) u ON t.i = u.a "
        + "RIGHT OUTER JOIN (SELECT * FROM titi) i ON t.i = i.a", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select without clauses
   */
  @Test
  @DisplayName("Full outer join")
  public void testQueryFullOuterJoinWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .fullOuterJoin("tutu u", SQL.clauses().field("t.i").equals().field("u.a"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,u.tata FROM toto t FULL OUTER JOIN tutu u ON t.i = u.a",
                                 query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select without clauses
   */
  @Test
  @DisplayName("Full outer join with subquery")
  public void testQueryFullOuterJoinWithSubqueryWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .fullOuterJoin(SQL.select().field("*").from("tutu").done(), "u", SQL.clauses().field("t.i").equals().field("u.a"))
        .fullOuterJoin(SQL.select().field("*").from("titi").build(), "i", SQL.clauses().field("t.i").equals().field("i.a"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,u.tata "
        + "FROM toto t "
        + "FULL OUTER JOIN (SELECT * FROM tutu) u ON t.i = u.a "
        + "FULL OUTER JOIN (SELECT * FROM titi) i ON t.i = i.a", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select without clauses
   */
  @Test
  @DisplayName("Full join")
  public void testQueryFullJoinWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .fullJoin("tutu u", SQL.clauses().field("t.i").equals().field("u.a"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,u.tata FROM toto t FULL OUTER JOIN tutu u ON t.i = u.a",
                                 query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select without clauses
   */
  @Test
  @DisplayName("Full join with subquery")
  public void testQueryFullJoinWithSubqueryWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .fullJoin(SQL.select().field("*").from("tutu").done(), "u", SQL.clauses().field("t.i").equals().field("u.a"))
        .fullJoin(SQL.select().field("*").from("titi").build(), "i", SQL.clauses().field("t.i").equals().field("i.a"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,u.tata "
        + "FROM toto t "
        + "FULL OUTER JOIN (SELECT * FROM tutu) u ON t.i = u.a "
        + "FULL OUTER JOIN (SELECT * FROM titi) i ON t.i = i.a", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Simple clauses (the one which does not have to be check and add values as prepared statement)
   */
  @Test
  @DisplayName("Static clauses")
  public void testSimpleClauses() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses()
               .field("t.a").equals().field("t.b")
               .and().field("t.a").notEquals().field("t.c")
               .and().field("t.a").lesser().field("t.d")
               .and().field("t.a").lesserEquals().field("t.e")
               .and().field("t.a").greater().field("t.f")
               .and().field("t.a").greateEquals().field("t.g")
               .and().field("t.a").isNotNull()
               .and().field("t.h").isNull()
               .and().exists(SQL.select().field("1").from("tata a").where(SQL.clauses().field("a.a").equals().field("t.a")))
               .and().notExists(SQL.select().field("1").from("tutu u").where(SQL.clauses().field("u.a").equals().field("t.a")))
               .and().field("t.i").between().field("1").and().field("10")
               .and().field("t.j").notBetween().field("1").and().field("10")
               .and().field("t.k").like().field(SQL.formatText("%tata%"))
               .and().field("t.l").notLike().field(SQL.formatText("%titi%"))
               .and().not(SQL.clauses().field("t.m").equals().field("t.n")
                          .or().field("t.o").equals().field("t.p"))
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,t.tata FROM toto t "
        + "WHERE t.a = t.b "
        + "AND t.a <> t.c "
        + "AND t.a < t.d "
        + "AND t.a <= t.e "
        + "AND t.a > t.f "
        + "AND t.a >= t.g "
        + "AND t.a IS NOT NULL "
        + "AND t.h IS NULL "
        + "AND EXISTS(SELECT 1 FROM tata a WHERE a.a = t.a) "
        + "AND NOT EXISTS(SELECT 1 FROM tutu u WHERE u.a = t.a) "
        + "AND t.i BETWEEN 1 AND 10 "
        + "AND t.j NOT BETWEEN 1 AND 10 "
        + "AND t.k LIKE '%tata%' "
        + "AND t.l NOT LIKE '%titi%' "
        + "AND NOT (t.m = t.n OR t.o = t.p)", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Simple clauses (the one which does not have to be check and add values as prepared statement)
   */
  @Test
  @DisplayName("Optional clauses with valid values")
  public void testClausesWithValidValues() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses("t.a", Clauses::equalsTo, "b")
               .and("t.a", Clauses::notEquals, "c")
               .and("t.a", Clauses::lesser, "d")
               .and("t.a", Clauses::lesserEquals, "e")
               .and("t.a", Clauses::greater, "f")
               .and("t.a", Clauses::greaterEquals, "g")
               .and("t.i", Clauses::between, 1, 10)
               .and("t.j", Clauses::notBetween, 1 ,10)
               .and("t.k", Clauses::like, "%plip%")
               .and("t.l", Clauses::notLike, "%plop%")
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,t.tata FROM toto t "
        + "WHERE t.a = ? "
        + "AND t.a <> ? "
        + "AND t.a < ? "
        + "AND t.a <= ? "
        + "AND t.a > ? "
        + "AND t.a >= ? "
        + "AND t.i BETWEEN ? AND ? "
        + "AND t.j NOT BETWEEN ? AND ? "
        + "AND t.k LIKE ? "
        + "AND t.l NOT LIKE ?", query.getQuery()),
              () -> assertEquals(Stream.of("b","c","d","e","f","g", 1, 10, 1, 10,"%plip%","%plop%").collect(Collectors.toList()),
                                 query.getValues()));
  }

  /**
   * Simple clauses (the one which does not have to be check and add values as prepared statement)
   */
  @Test
  @DisplayName("Clause custom checker")
  public void testClausesWithValidValuesCustomChecker() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses("t.a", Clauses::equalsTo, "b")
               .and("t.a", Clauses::notEquals, "c", (val) -> !val.equals("z"))
               .and("t.a", Clauses::lesser, "d")
               .and("t.a", Clauses::lesserEquals, "e")
               .and("t.a", Clauses::greater, "f")
               .and("t.a", Clauses::greaterEquals, "g")
               .and("t.i", Clauses::between, 1, 10, (val) -> val > 0 && val < 20)
               .and("t.j", Clauses::notBetween, 1 ,10)
               .and("t.k", Clauses::like, "%plip%")
               .and("t.l", Clauses::notLike, "%plop%")
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,t.tata FROM toto t "
        + "WHERE t.a = ? "
        + "AND t.a <> ? "
        + "AND t.a < ? "
        + "AND t.a <= ? "
        + "AND t.a > ? "
        + "AND t.a >= ? "
        + "AND t.i BETWEEN ? AND ? "
        + "AND t.j NOT BETWEEN ? AND ? "
        + "AND t.k LIKE ? "
        + "AND t.l NOT LIKE ?", query.getQuery()),
              () -> assertEquals(Stream.of("b","c","d","e","f","g", 1, 10, 1, 10,"%plip%","%plop%").collect(Collectors.toList()),
                                 query.getValues()));
  }

  /**
   * Simple clauses (the one which does not have to be check and add values as prepared statement)
   */
  @Test
  @DisplayName("OR clause custom checker")
  public void testOrClausesWithValidValuesCustomChecker() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses("t.a", Clauses::equalsTo, "b")
               .or("t.a", Clauses::notEquals, "c", (val) -> !val.equals("z"))
               .or("t.a", Clauses::lesser, "d")
               .or("t.a", Clauses::lesserEquals, "e")
               .or("t.a", Clauses::greater, "f")
               .or("t.a", Clauses::greaterEquals, "g")
               .or("t.i", Clauses::between, 1, 10, (val) -> val > 0 && val < 20)
               .or("t.j", Clauses::notBetween, 1 ,10)
               .or("t.k", Clauses::like, "%plip%")
               .or("t.l", Clauses::notLike, "%plop%")
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,t.tata FROM toto t "
        + "WHERE t.a = ? "
        + "OR t.a <> ? "
        + "OR t.a < ? "
        + "OR t.a <= ? "
        + "OR t.a > ? "
        + "OR t.a >= ? "
        + "OR t.i BETWEEN ? AND ? "
        + "OR t.j NOT BETWEEN ? AND ? "
        + "OR t.k LIKE ? "
        + "OR t.l NOT LIKE ?", query.getQuery()),
              () -> assertEquals(Stream.of("b","c","d","e","f","g", 1, 10, 1, 10,"%plip%","%plop%").collect(Collectors.toList()),
                                 query.getValues()));
  }

  /**
   * Test clauses with null value
   */
  @Test
  @DisplayName("Optional clause with null value")
  public void testClausesWithNullValue() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses("t.a", Clauses::equalsTo, "b")
               .and("t.a", Clauses::notEquals, null)
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a = ?", query.getQuery()),
              () -> assertEquals(Stream.of("b").collect(Collectors.toList()), query.getValues()));
  }

  /**
   * Test clauses with null value
   */
  @Test
  @DisplayName("No valid values")
  public void testClausesWhereNoValidValue() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses("t.a", Clauses::notEquals, null)).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,t.tata FROM toto t", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test clause with empty value
   */
  @Test
  @DisplayName("Optional clause with empty string value")
  public void testClausesWithEmptyValue() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses("t.a", Clauses::equalsTo, "b")
               .and("t.a", Clauses::notEquals, "")
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a = ?", query.getQuery()),
              () -> assertEquals(Stream.of("b").collect(Collectors.toList()), query.getValues()));
  }

  /**
   * Test clause with first value invalid
   */
  @Test
  @DisplayName("First clause with invalid value")
  public void testClausesWithFirstValueInvalid() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses("t.a", Clauses::equalsTo, null)
               .and("t.a", Clauses::notEquals, "c")
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a <> ?", query.getQuery()),
              () -> assertEquals(Stream.of("c").collect(Collectors.toList()), query.getValues()));
  }

  /**
   * Test clause with empty value
   */
  @Test
  @DisplayName("Optional clause with custom checker and invalid value")
  public void testClausesWithInvalidValueCustomChecker() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses("t.a", Clauses::equalsTo, "b")
               .and("t.a", Clauses::notEquals, 2, (i) -> i != 2)
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a = ?", query.getQuery()),
              () -> assertEquals(Stream.of("b").collect(Collectors.toList()), query.getValues()));
  }

  /**
   * Test select without clauses
   */
  @Test
  @DisplayName("Inner join with optional values")
  public void testQueryInnerJoinWithOptionalValue() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .innerJoin("tutu u", SQL.clauses()
                   .field("t.i").equals().field("u.a")
                   .and("u.u", Clauses::equalsTo, "UHU")
            )
        .where(SQL.clauses("t.a", Clauses::equalsTo, "a"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,u.tata FROM toto t "
        + "INNER JOIN tutu u ON t.i = u.a AND u.u = ? "
        + "WHERE t.a = ?", query.getQuery()),
              () -> assertEquals(Stream.of("UHU","a").collect(Collectors.toList()), query.getValues()));
  }

  public class ExampleRequest {
    public UUID id;
    public String firstName;
    public String lastName;
    public String email;
    public String gender;
    public String country;
  }

  /**
   * Test example query
   */
  @Test
  @DisplayName("README.md example")
  public void testExampleQuery() {
    final ExampleRequest req = new ExampleRequest();
    req.gender = "F";
    req.lastName = "%Croft%";
    final SQLQuery query = SQL.select().field("*")
        .from("Heroes")
        .where(SQL.clauses("id", Clauses::equalsTo, req.id)
               .and("first_name", Clauses::like, req.firstName)
               .and("last_name", Clauses::like, req.lastName)
               .and("email", Clauses::equalsTo, req.email)
               .and("gender", Clauses::equalsTo, req.gender)
               .and("country", Clauses::equalsTo, req.country)
            ).build();
    System.out.println("Query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT * FROM Heroes "
        + "WHERE last_name LIKE ? "
        + "AND gender = ?", query.getQuery()),
              () -> assertEquals(Stream.of("%Croft%","F").collect(Collectors.toList()), query.getValues()));
    System.out.println(SQL.select().field("a").as("b").field("c").from("T").build().getQuery());
  }

  /**
   * Test optional valid IN clause
   */
  @Test
  @DisplayName("IN clause with valid values")
  public void testOptionalValidIn() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses("t.a", Clauses::in, Stream.of("'plip'","'plop'").collect(Collectors.toList()))
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a IN ('plip','plop')", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test optional invalid IN clause
   */
  @Test
  @DisplayName("IN clause with invalid values")
  public void testOptionalInvalidIn() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses("t.a", Clauses::in, Stream.of().collect(Collectors.toList()))
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,t.tata FROM toto t", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test optional valid NOT IN clause
   */
  @Test
  @DisplayName("NOT IN clause with valid values")
  public void testOptionalValidNotIn() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses("t.a", Clauses::notIn, Stream.of("'plip'","'plop'").collect(Collectors.toList()))
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a NOT IN ('plip','plop')", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test optional invalid NOT IN clause
   */
  @Test
  @DisplayName("NOT IN clause with invalid values")
  public void testOptionalInvalidNotIn() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses("t.a", Clauses::notIn, Stream.of().collect(Collectors.toList()))
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,t.tata FROM toto t", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test valid IN clause
   */
  @Test
  @DisplayName("IN clause")
  public void testSelectIn() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses().field("t.a").in(SQL.formatText(Stream.of("plip","plop").collect(Collectors.toList())))
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a IN ('plip','plop')", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test valid NOT IN clause
   */
  @Test
  @DisplayName("NOT IN clause")
  public void testSelectNotIn() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses().field("t.a").notIn(Stream.of("'plip'","'plop'").collect(Collectors.toList()))
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a NOT IN ('plip','plop')", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test IN subquery clause
   */
  @Test
  @DisplayName("IN subquery")
  public void testSelectInSelect() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses().field("t.a").in(SQL.select().field("u.a").from("tutu u").done())
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a IN (SELECT u.a FROM tutu u)", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test NOT IN subquery clause
   */
  @Test
  @DisplayName("NOT IN subquery")
  public void testSelectNotInSelect() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses().field("t.a").notIn(SQL.select().field("u.a").from("tutu u").done())
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a NOT IN (SELECT u.a FROM tutu u)", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test IN subquery clause
   */
  @Test
  @DisplayName("IN subquery result")
  public void testSelectInSelectRes() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses().field("t.a").in(SQL.select().field("u.a").from("tutu u").build())
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a IN (SELECT u.a FROM tutu u)", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test NOT IN subquery clause
   */
  @Test
  @DisplayName("NOT IN subquery result")
  public void testSelectNotInSelectRes() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses().field("t.a").notIn(SQL.select().field("u.a").from("tutu u").build())
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a NOT IN (SELECT u.a FROM tutu u)", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test window function with only limit
   */
  @Test
  @DisplayName("Fetch")
  public void testLimit() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses("t.a", Clauses::notEquals, "a"))
        .fetch(10)
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a <> ? FETCH FIRST 10 ROWS ONLY", query.getQuery()),
              () -> assertEquals(1, query.getValues().size()));
  }

  /**
   * Test window function with limit and offset
   */
  @Test
  @DisplayName("Fetch offset")
  public void testLimitOffset() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses("t.a", Clauses::notEquals, "a"))
        .offset(50).fetch(10)
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a <> ? OFFSET 50 ROWS FETCH FIRST 10 ROWS ONLY", query.getQuery()),
              () -> assertEquals(1, query.getValues().size()));
  }

  /**
   * Test window function with only limit in 'subbuilder'
   */
  @Test
  @DisplayName("Fetch without where")
  public void testLimitWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .fetch(10)
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,t.tata FROM toto t FETCH FIRST 10 ROWS ONLY", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test limit/offset in 'subbuilder'
   */
  @Test
  @DisplayName("Fetch offset without where")
  public void testLimitOffsetWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .offset(50).fetch(10)
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,t.tata FROM toto t OFFSET 50 ROWS FETCH FIRST 10 ROWS ONLY", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test group by
   */
  @Test
  @DisplayName("Group by")
  public void testGroupBy() {
    final SQLQuery query = SQL.select()
        .field("count(t.titi)")
        .field("t.tata")
        .from("toto t")
        .groupBy("t.tata")
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT count(t.titi),t.tata FROM toto t GROUP BY t.tata", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test group by
   */
  @Test
  @DisplayName("Group by having")
  public void testGroupByHaving() {
    final SQLQuery query = SQL.select()
        .field("sum(t.titi)")
        .field("t.tata")
        .from("toto t")
        .groupBy("t.tata")
        .having(SQL.clauses().field("sum(t.titi)").greateEquals().field("2"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT sum(t.titi),t.tata FROM toto t GROUP BY t.tata HAVING sum(t.titi) >= 2", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test order by
   */
  @Test
  @DisplayName("Order by")
  public void testOrderBy() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .orderBy("t.tata")
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,t.tata FROM toto t ORDER BY t.tata", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test order by
   */
  @Test
  @DisplayName("Order by ascending")
  public void testOrderByAsc() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .orderBy("t.tata").asc()
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,t.tata FROM toto t ORDER BY t.tata ASC", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test order by
   */
  @Test
  @DisplayName("Order by descending")
  public void testOrderByDesc() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .orderBy("t.tata").desc()
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi,t.tata FROM toto t ORDER BY t.tata DESC", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test SQL union
   */
  @Test
  @DisplayName("Union")
  public void testUnion() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .from("toto t")
        .union(SQL.select().field("t.titi").from("tutu t").done())
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi FROM toto t UNION SELECT t.titi FROM tutu t", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test SQL union all
   */
  @Test
  @DisplayName("Union all")
  public void testUnionAll() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .from("toto t")
        .unionAll(SQL.select().field("t.titi").from("tutu t").done())
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi FROM toto t UNION ALL SELECT t.titi FROM tutu t", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select from subquery
   */
  @Test
  @DisplayName("From subquery")
  public void testFromSubquery() {
    final SQLQuery query = SQL.select()
        .field("titi")
        .from(SQL.select()
              .field("titi")
              .from("tutu")
              .build())
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT titi FROM (SELECT titi FROM tutu)", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select from subquery
   */
  @Test
  @DisplayName("From subquery builder")
  public void testFromSubqueryBuilder() {
    final SQLQuery query = SQL.select()
        .field("titi")
        .from(SQL.select()
              .field("titi")
              .from("tutu")
              .done())
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT titi FROM (SELECT titi FROM tutu)", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select from subquery
   */
  @Test
  @DisplayName("From subquery with alias")
  public void testFromSubqueryWithAlias() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .from(SQL.select()
              .field("titi")
              .from("tutu")
              .build(), "t")
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi FROM (SELECT titi FROM tutu) t", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test select from subquery
   */
  @Test
  @DisplayName("From subquery builder with alias")
  public void testFromSubqueryBuilderWithAlias() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .from(SQL.select()
              .field("titi")
              .from("tutu")
              .done(),"t")
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi FROM (SELECT titi FROM tutu) t", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Test clause with custom checker
   */
  @Test
  @DisplayName("Clause custom checker")
  void testClauseCustomChecker() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .from("toto t")
        .where(SQL.clauses("t.tata", Clauses::like, "%tutu%", (val) -> val != null && val.contains("%")))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi FROM toto t WHERE t.tata LIKE ?", query.getQuery()),
              () -> assertEquals(1, query.getValues().size()));
  }

  /**
   * Test clause with custom checker
   */
  @Test
  @DisplayName("Two values operator")
  public void testClauseTwoValues() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .from("toto t")
        .where(SQL.clauses("t.tata", Clauses::between, 1, 2))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi FROM toto t WHERE t.tata BETWEEN ? AND ?", query.getQuery()),
              () -> assertEquals(2, query.getValues().size()));
  }

  /**
   * Test clause with custom checker
   */
  @Test
  @DisplayName("Two values operator with custom checker")
  public void testClauseTwoValuesCustomChecker() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .from("toto t")
        .where(SQL.clauses("t.tata", Clauses::between, 1, 2, (val) -> val < 1000))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi FROM toto t WHERE t.tata BETWEEN ? AND ?", query.getQuery()),
              () -> assertEquals(2, query.getValues().size()),
              () -> assertEquals(2, query.values().length));
  }

  /**
   * Test add clauses with AND operator
   */
  @Test
  @DisplayName("AND other clauses")
  public void testAndOtherClauses() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .from("toto t")
        .where(SQL.clauses("t.tata", Clauses::between, 1, 2, (val) -> val < 1000)
               .and(SQL.clauses().field("t.a").equals().field("2")
                    .or().field("t.b").equals().field("3")
                   )
               .and(SQL.clauses("t.c", Clauses::equalsTo, null))
            )
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi "
        + "FROM toto t "
        + "WHERE t.tata BETWEEN ? AND ? "
        + "AND (t.a = 2 OR t.b = 3)", query.getQuery()),
              () -> assertEquals(2, query.getValues().size()),
              () -> assertEquals(2, query.values().length));
  }

  /**
   * Test add clauses with AND operator
   */
  @Test
  @DisplayName("OR other clauses")
  public void testOrOtherClauses() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .from("toto t")
        .where(SQL.clauses("t.tata", Clauses::between, 1, 2, (val) -> val < 1000)
               .or(SQL.clauses().field("t.a").equals().field("2")
                    .and().field("t.b").equals().field("3")
                   )
               .or(SQL.clauses("t.c", Clauses::equalsTo, null))
            )
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi "
        + "FROM toto t "
        + "WHERE t.tata BETWEEN ? AND ? "
        + "OR (t.a = 2 AND t.b = 3)", query.getQuery()),
              () -> assertEquals(2, query.getValues().size()),
              () -> assertEquals(2, query.values().length));
  }

  /**
   * Test add clauses with AND operator
   */
  @Test
  @DisplayName("First clause with invalid value")
  public void testAndOtherClausesWithFirstInvalid() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .from("toto t")
        .where(SQL.clauses("t.tata", Clauses::equalsTo, null)
               .and(SQL.clauses().field("t.a").equals().field("2")
                    .or().field("t.b").equals().field("3")
                   )
               .and(SQL.clauses("t.c", Clauses::equalsTo, null))
            )
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi "
        + "FROM toto t "
        + "WHERE (t.a = 2 OR t.b = 3)", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()),
              () -> assertEquals(0, query.values().length));
  }

  /**
   * Optional exists as first clause
   */
  @Test
  @DisplayName("Optional exists as first clause")
  public void testFirstOptionalExists() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .from("toto t")
        .where(SQL.clauses(Clauses::exists, SQL.select().field("1")
                           .from("tata a")
                           .where(SQL.clauses().field("t.id").equals().field("a.id"))))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi "
        + "FROM toto t "
        + "WHERE EXISTS (SELECT 1 FROM tata a WHERE t.id = a.id)", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Optional exists as first clause
   */
  @Test
  @DisplayName("Optional not exists as first clause")
  public void testFirstOptionalNotExists() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .from("toto t")
        .where(SQL.clauses(Clauses::notExists, SQL.select().field("1")
                           .from("tata a")
                           .where(SQL.clauses().field("t.id").equals().field("a.id")).build()))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi "
        + "FROM toto t "
        + "WHERE NOT EXISTS (SELECT 1 FROM tata a WHERE t.id = a.id)", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Optional exists as first clause
   */
  @Test
  @DisplayName("Invalid optional exists as first clause")
  public void testFirstOptionalExistsInvalid() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .from("toto t")
        .where(SQL.clauses(Clauses::exists, SQL.select().field("1")
                           .from("tata a")
                           .where(SQL.clauses().field("t.id").equals().field("a.id")),
                           (plop) -> false))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi "
        + "FROM toto t", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * Optional exists as first clause
   */
  @Test
  @DisplayName("Optional exists")
  public void testOptionalExists() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .from("toto t")
        .where(SQL.clauses("t.a", Clauses::equalsTo, "plop")
               .and(Clauses::exists, SQL.select().field("1")
                    .from("tata a")
                    .where(SQL.clauses().field("t.id").equals().field("a.id"))))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi "
        + "FROM toto t "
        + "WHERE t.a = ? AND EXISTS (SELECT 1 FROM tata a WHERE t.id = a.id)", query.getQuery()),
              () -> assertEquals(1, query.getValues().size()));
  }

  /**
   * Optional exists as first clause
   */
  @Test
  @DisplayName("Optional not exists")
  public void testOptionalNotExists() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .from("toto t")
        .where(SQL.clauses("t.a", Clauses::equalsTo, "plop")
               .or(Clauses::notExists, SQL.select().field("1")
                    .from("tata a")
                    .where(SQL.clauses().field("t.id").equals().field("a.id")).build()))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi "
        + "FROM toto t "
        + "WHERE t.a = ? OR NOT EXISTS (SELECT 1 FROM tata a WHERE t.id = a.id)", query.getQuery()),
              () -> assertEquals(1, query.getValues().size()));
  }

  /**
   * Optional exists as first clause
   */
  @Test
  @DisplayName("Invalid optional exists as first clause")
  public void testOptionalExistsInvalid() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .from("toto t")
        .where(SQL.clauses("t.a", Clauses::equalsTo, "plop")
               .and(Clauses::exists, SQL.select().field("1")
                    .from("tata a")
                    .where(SQL.clauses().field("t.id").equals().field("a.id")),
                    (plop) -> false))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi "
        + "FROM toto t WHERE t.a = ?", query.getQuery()),
              () -> assertEquals(1, query.getValues().size()));
  }

  /**
   * Optional exists as first clause
   */
  @Test
  @DisplayName("Invalid optional exists as first clause")
  public void testOrOptionalExistsInvalid() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .from("toto t")
        .where(SQL.clauses("t.a", Clauses::equalsTo, "plop")
               .or(Clauses::exists, SQL.select().field("1")
                   .from("tata a")
                   .where(SQL.clauses().field("t.id").equals().field("a.id")),
                   (plop) -> false))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("SELECT t.titi "
        + "FROM toto t WHERE t.a = ?", query.getQuery()),
              () -> assertEquals(1, query.getValues().size()));
  }
  // Tests -

}
