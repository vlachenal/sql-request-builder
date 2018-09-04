package com.github.vlachenal.sql;

import static org.junit.Assert.assertEquals;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;


/**
 * Select builder unit tests.
 *
 * @author Vincent Lachenal
 */
public class SelectBuilderTest {

  // Tests +
  /**
   * Test SQL query without from and where part (like in PostgreSQL store procedures)
   */
  @Test
  public void testPgStoreProcedure() {
    final SQLQuery query = SQL.select().field("nextval()").build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT nextval()", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test select single column on single table without clauses
   */
  @Test
  public void testQuerySingleColSingleTableWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("titi")
        .from("toto")
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT titi FROM toto", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test select single column on single table without clauses
   */
  @Test
  public void testQuerySingleColWithAliasSingleTableWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("titi").as("t")
        .from("toto")
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT titi AS t FROM toto", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test select on single table without clauses
   */
  @Test
  public void testQuerySingleTableWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("titi")
        .field("tata")
        .from("toto")
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT titi,tata FROM toto", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test select without clauses
   */
  @Test
  public void testQueryWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .selfJoin("tutu u")
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,u.tata FROM toto t,tutu u", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test select without clauses
   */
  @Test
  public void testQueryWithSubqueryWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .selfJoin(SQL.select().field("*").from("tutu").done(), "u")
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,u.tata FROM toto t,(SELECT * FROM tutu) u", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test select without clauses
   */
  @Test
  public void testQueryNaturalJoinWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .naturalJoin("tutu u")
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,u.tata FROM toto t NATURAL JOIN tutu u", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test select without clauses
   */
  @Test
  public void testQueryNaturalJoinWithSubqueryWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .naturalJoin(SQL.select().field("*").from("tutu").done(), "u")
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,u.tata FROM toto t NATURAL JOIN (SELECT * FROM tutu) u", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test select without clauses
   */
  @Test
  public void testQueryCrossJoinWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .crossJoin("tutu u")
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,u.tata FROM toto t CROSS JOIN tutu u", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test select without clauses
   */
  @Test
  public void testQueryCrossJoinWithSubqueryWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .crossJoin(SQL.select().field("*").from("tutu").done(), "u")
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,u.tata FROM toto t CROSS JOIN (SELECT * FROM tutu) u", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test select without clauses
   */
  @Test
  public void testQueryInnerJoinWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .innerJoin("tutu u", SQL.clauses().field("t.i").equals().field("u.a"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,u.tata FROM toto t INNER JOIN tutu u ON t.i = u.a", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test select without clauses
   */
  @Test
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
    assertEquals("SELECT t.titi,u.tata FROM toto t INNER JOIN tutu u ON t.i = u.a", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test select without clauses
   */
  @Test
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
    assertEquals("SELECT t.titi,u.tata FROM toto t "
        + "INNER JOIN tutu u ON t.i = u.a AND u.u = ?", query.getQuery());
    assertEquals(Stream.of("UHU").collect(Collectors.toList()), query.getValues());
  }

  /**
   * Test select without clauses
   */
  @Test
  public void testQueryInnerJoinWithSubqueryWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .innerJoin(SQL.select().field("*").from("tutu").done(), "u", SQL.clauses().field("t.i").equals().field("u.a"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,u.tata FROM toto t INNER JOIN (SELECT * FROM tutu) u ON t.i = u.a", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test select without clauses
   */
  @Test
  public void testQueryLeftJoinWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .leftOuterJoin("tutu u", SQL.clauses().field("t.i").equals().field("u.a"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,u.tata FROM toto t LEFT OUTER JOIN tutu u ON t.i = u.a", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test select without clauses
   */
  @Test
  public void testQueryLeftJoinWithSubqueryWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .leftOuterJoin(SQL.select().field("*").from("tutu").done(), "u", SQL.clauses().field("t.i").equals().field("u.a"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,u.tata FROM toto t LEFT OUTER JOIN (SELECT * FROM tutu) u ON t.i = u.a", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test select without clauses
   */
  @Test
  public void testQueryRightJoinWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .rightOuterJoin("tutu u", SQL.clauses().field("t.i").equals().field("u.a"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,u.tata FROM toto t RIGHT OUTER JOIN tutu u ON t.i = u.a", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test select without clauses
   */
  @Test
  public void testQueryRightJoinWithSubqueryWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .rightOuterJoin(SQL.select().field("*").from("tutu").done(), "u", SQL.clauses().field("t.i").equals().field("u.a"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,u.tata FROM toto t RIGHT OUTER JOIN (SELECT * FROM tutu) u ON t.i = u.a", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test select without clauses
   */
  @Test
  public void testQueryFullJoinWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .fullOuterJoin("tutu u", SQL.clauses().field("t.i").equals().field("u.a"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,u.tata FROM toto t FULL OUTER JOIN tutu u ON t.i = u.a", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test select without clauses
   */
  @Test
  public void testQueryFullJoinWithSubqueryWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("u.tata")
        .from("toto t")
        .fullOuterJoin(SQL.select().field("*").from("tutu").done(), "u", SQL.clauses().field("t.i").equals().field("u.a"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,u.tata FROM toto t FULL OUTER JOIN (SELECT * FROM tutu) u ON t.i = u.a", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Simple clauses (the one which does not have to be check and add values as prepared statement)
   */
  @Test
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
    assertEquals("SELECT t.titi,t.tata FROM toto t "
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
        + "AND NOT (t.m = t.n OR t.o = t.p)", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Simple clauses (the one which does not have to be check and add values as prepared statement)
   */
  @Test
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
               .and("t.a", Clauses::greateEquals, "g")
               .and("t.i", Clauses::between, 1, 10)
               .and("t.j", Clauses::notBetween, 1 ,10)
               .and("t.k", Clauses::like, "%plip%")
               .and("t.l", Clauses::notLike, "%plop%")
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,t.tata FROM toto t "
        + "WHERE t.a = ? "
        + "AND t.a <> ? "
        + "AND t.a < ? "
        + "AND t.a <= ? "
        + "AND t.a > ? "
        + "AND t.a >= ? "
        + "AND t.i BETWEEN ? AND ? "
        + "AND t.j NOT BETWEEN ? AND ? "
        + "AND t.k LIKE ? "
        + "AND t.l NOT LIKE ?", query.getQuery());
    assertEquals(Stream.of("b","c","d","e","f","g", 1, 10, 1, 10,"%plip%","%plop%").collect(Collectors.toList()), query.getValues());
  }

  /**
   * Test clauses with null value
   */
  @Test
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
    assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a = ?", query.getQuery());
    assertEquals(Stream.of("b").collect(Collectors.toList()), query.getValues());
  }

  /**
   * Test clauses with null value
   */
  @Test
  public void testClausesWhereNoValidValue() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses("t.a", Clauses::notEquals, null)).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,t.tata FROM toto t", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test clause with empty value
   */
  @Test
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
    assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a = ?", query.getQuery());
    assertEquals(Stream.of("b").collect(Collectors.toList()), query.getValues());
  }

  /**
   * Test clause with first value invalid
   */
  @Test
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
    assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a <> ?", query.getQuery());
    assertEquals(Stream.of("c").collect(Collectors.toList()), query.getValues());
  }

  /**
   * Test clause with empty value
   */
  @Test
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
    assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a = ?", query.getQuery());
    assertEquals(Stream.of("b").collect(Collectors.toList()), query.getValues());
  }

  /**
   * Test select without clauses
   */
  @Test
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
    assertEquals("SELECT t.titi,u.tata FROM toto t "
        + "INNER JOIN tutu u ON t.i = u.a AND u.u = ? "
        + "WHERE t.a = ?", query.getQuery());
    assertEquals(Stream.of("UHU","a").collect(Collectors.toList()), query.getValues());
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
   * Test exemple query
   */
  @Test
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
    assertEquals("SELECT * FROM Heroes "
        + "WHERE last_name LIKE ? "
        + "AND gender = ?", query.getQuery());
    assertEquals(Stream.of("%Croft%","F").collect(Collectors.toList()), query.getValues());
    System.out.println(SQL.select().field("a").as("b").field("c").from("T").build().getQuery());
  }

  /**
   * Test optional valid IN clause
   */
  @Test
  public void testOptionalValidIn() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses("t.a", Clauses::in, Stream.of("'plip'","'plop'").collect(Collectors.toList()))
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a IN ('plip','plop')", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test optional invalid IN clause
   */
  @Test
  public void testOptionalInvalidIn() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses("t.a", Clauses::in, Stream.of().collect(Collectors.toList()))
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,t.tata FROM toto t", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test optional valid NOT IN clause
   */
  @Test
  public void testOptionalValidNotIn() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses("t.a", Clauses::notIn, Stream.of("'plip'","'plop'").collect(Collectors.toList()))
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a NOT IN ('plip','plop')", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test optional invalid NOT IN clause
   */
  @Test
  public void testOptionalInvalidNotIn() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses("t.a", Clauses::notIn, Stream.of().collect(Collectors.toList()))
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,t.tata FROM toto t", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test valid IN clause
   */
  @Test
  public void testSelectIn() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses().field("t.a").in(Stream.of("'plip'","'plop'").collect(Collectors.toList()))
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a IN ('plip','plop')", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test valid NOT IN clause
   */
  @Test
  public void testSelectNotIn() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses().field("t.a").notIn(Stream.of("'plip'","'plop'").collect(Collectors.toList()))
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a NOT IN ('plip','plop')", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test IN subquery clause
   */
  @Test
  public void testSelectInSelect() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses().field("t.a").in(SQL.select().field("u.a").from("tutu u").done())
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a IN (SELECT u.a FROM tutu u)", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test NOT IN subquery clause
   */
  @Test
  public void testSelectNotInSelect() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses().field("t.a").notIn(SQL.select().field("u.a").from("tutu u").done())
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a NOT IN (SELECT u.a FROM tutu u)", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test IN subquery clause
   */
  @Test
  public void testSelectInSelectRes() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses().field("t.a").in(SQL.select().field("u.a").from("tutu u").build())
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a IN (SELECT u.a FROM tutu u)", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test NOT IN subquery clause
   */
  @Test
  public void testSelectNotInSelectRes() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .where(SQL.clauses().field("t.a").notIn(SQL.select().field("u.a").from("tutu u").build())
            ).build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a NOT IN (SELECT u.a FROM tutu u)", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test window function with only limit
   */
  @Test
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
    assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a <> ? FETCH FIRST 10 ROWS ONLY", query.getQuery());
    assertEquals(1, query.getValues().size());
  }

  /**
   * Test window function with limit and offset
   */
  @Test
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
    assertEquals("SELECT t.titi,t.tata FROM toto t WHERE t.a <> ? OFFSET 50 ROWS FETCH FIRST 10 ROWS ONLY", query.getQuery());
    assertEquals(1, query.getValues().size());
  }

  /**
   * Test window function with only limit in 'subbuilder'
   */
  @Test
  public void testLimitWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .fetch(10)
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,t.tata FROM toto t FETCH FIRST 10 ROWS ONLY", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test limit/offset in 'subbuilder'
   */
  @Test
  public void testLimitOffsetWithoutWhere() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .offset(50).fetch(10)
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,t.tata FROM toto t OFFSET 50 ROWS FETCH FIRST 10 ROWS ONLY", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test group by
   */
  @Test
  public void testGroupBy() {
    final SQLQuery query = SQL.select()
        .field("count(t.titi)")
        .field("t.tata")
        .from("toto t")
        .groupBy("t.tata")
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT count(t.titi),t.tata FROM toto t GROUP BY t.tata", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test group by
   */
  @Test
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
    assertEquals("SELECT sum(t.titi),t.tata FROM toto t GROUP BY t.tata HAVING sum(t.titi) >= 2", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test order by
   */
  @Test
  public void testOrderBy() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .orderBy("t.tata")
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,t.tata FROM toto t ORDER BY t.tata", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test order by
   */
  @Test
  public void testOrderByAsc() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .orderBy("t.tata").asc()
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,t.tata FROM toto t ORDER BY t.tata ASC", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test order by
   */
  @Test
  public void testOrderByDesc() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .field("t.tata")
        .from("toto t")
        .orderBy("t.tata").desc()
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi,t.tata FROM toto t ORDER BY t.tata DESC", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test SQL union
   */
  @Test
  public void testUnion() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .from("toto t")
        .union(SQL.select().field("t.titi").from("tutu t").done())
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi FROM toto t UNION SELECT t.titi FROM tutu t", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test SQL union all
   */
  @Test
  public void testUnionAll() {
    final SQLQuery query = SQL.select()
        .field("t.titi")
        .from("toto t")
        .unionAll(SQL.select().field("t.titi").from("tutu t").done())
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertEquals("SELECT t.titi FROM toto t UNION ALL SELECT t.titi FROM tutu t", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test select from subquery
   */
  @Test
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
    assertEquals("SELECT titi FROM (SELECT titi FROM tutu)", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test select from subquery
   */
  @Test
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
    assertEquals("SELECT titi FROM (SELECT titi FROM tutu)", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test select from subquery
   */
  @Test
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
    assertEquals("SELECT t.titi FROM (SELECT titi FROM tutu) t", query.getQuery());
    assertEquals(0, query.getValues().size());
  }

  /**
   * Test select from subquery
   */
  @Test
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
    assertEquals("SELECT t.titi FROM (SELECT titi FROM tutu) t", query.getQuery());
    assertEquals(0, query.getValues().size());
  }
  // Tests -

}
