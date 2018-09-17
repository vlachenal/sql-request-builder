package com.github.vlachenal.sql;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * {@link DeleteBuilder} unit tests
 *
 * @author Vincent Lachenal
 */
@DisplayName("DELETE request builder unit tests")
@Execution(ExecutionMode.CONCURRENT)
public class DeleteBuilderTest {

  // Tests +
  /**
   * DELETE table without 'WHERE' unit test
   */
  @Test
  @DisplayName("Delete")
  public void testDeleteTable() {
    final SQLQuery query = SQL.delete("toto").build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("DELETE FROM toto", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * DELETE table with one clause unit test
   */
  @Test
  @DisplayName("Delete one clause")
  public void testDeleteTableWhereOneClause() {
    final SQLQuery query = SQL.delete("toto")
        .where(SQL.clauses().field("a").equals().field(SQL.formatText("titi")))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("DELETE FROM toto WHERE a = 'titi'", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * DELETE table with one optional valid clause unit test
   */
  @Test
  @DisplayName("Delete one optional valid clause")
  public void testDeleteTableWhereOneOptClause() {
    final SQLQuery query = SQL.delete("toto")
        .where(SQL.clauses("a", Clauses::equalsTo, "titi"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("DELETE FROM toto WHERE a = ?", query.getQuery()),
              () -> assertEquals(Stream.of("titi").collect(Collectors.toList()), query.getValues()));
  }

  /**
   * DELETE table with one optional invalid clause unit test
   */
  @Test
  @DisplayName("Delete one optional invalid clause")
  public void testDeleteTableWhereOneOptInvalidClause() {
    final SQLQuery query = SQL.delete("toto")
        .where(SQL.clauses("a", Clauses::equalsTo, null))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("DELETE FROM toto", query.getQuery()),
              () -> assertEquals(0, query.getValues().size()));
  }

  /**
   * DELETE table with two optional clause one invalid unit test
   */
  @Test
  @DisplayName("Delete two optional clause one invalid")
  public void testDeleteTableWhereTwoOptClause() {
    final SQLQuery query = SQL.delete("toto")
        .where(SQL.clauses("a", Clauses::equalsTo, null)
               .and("b", Clauses::equalsTo, "plop"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("DELETE FROM toto WHERE b = ?", query.getQuery()),
              () -> assertEquals(Stream.of("plop").collect(Collectors.toList()), query.getValues()));
  }
  // Tests -

}
