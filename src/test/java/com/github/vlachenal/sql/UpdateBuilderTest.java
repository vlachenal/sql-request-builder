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
 * {@link UpdateBuilder} unit tests
 *
 * @author Vincent Lachenal
 */
@DisplayName("Update request builder unit tests")
@Execution(ExecutionMode.CONCURRENT)
public class UpdateBuilderTest {

  // Tests +
  /**
   * UPDATE table one field without 'WHERE' unit test
   */
  @Test
  @DisplayName("Update one field")
  public void testUpdateOneFieldTable() {
    final SQLQuery query = SQL.update("toto")
        .field("a", 1)
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("UPDATE toto SET a = ?", query.getQuery()),
              () -> assertEquals(Stream.of(1).collect(Collectors.toList()), query.getValues()));
  }

  /**
   * UPDATE table one field without 'WHERE' unit test
   */
  @Test
  @DisplayName("Update two fields")
  public void testUpdateTwoFieldsTable() {
    final SQLQuery query = SQL.update("toto")
        .field("a", 1)
        .field("b", "titi")
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("UPDATE toto SET a = ?, b = ?", query.getQuery()),
              () -> assertEquals(Stream.of(1, "titi").collect(Collectors.toList()), query.getValues()));
  }

  /**
   * UPDATE table one field with one clause unit test
   */
  @Test
  @DisplayName("Update one field one clause")
  public void testUpdateTableWhereOneClause() {
    final SQLQuery query = SQL.update("toto").field("a", 1)
        .where(SQL.clauses().field("a").equals().field(SQL.formatText("titi")))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("UPDATE toto SET a = ? WHERE a = 'titi'", query.getQuery()),
              () -> assertEquals(Stream.of(1).collect(Collectors.toList()), query.getValues()));
  }

  /**
   * UPDATE table one field with one optional valid clause unit test
   */
  @Test
  @DisplayName("Update one field one optional valid clause")
  public void testUpdateTableWhereOneOptClause() {
    final SQLQuery query = SQL.update("toto").field("a", 1)
        .where(SQL.clauses("a", Clauses::equalsTo, "titi"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("UPDATE toto SET a = ? WHERE a = ?", query.getQuery()),
              () -> assertEquals(Stream.of(1, "titi").collect(Collectors.toList()), query.getValues()));
  }

  /**
   * UPDATE table one field with one optional invalid clause unit test
   */
  @Test
  @DisplayName("Update one field one optional invalid clause")
  public void testUpdateTableWhereOneOptInvalidClause() {
    final SQLQuery query = SQL.update("toto").field("a", 1)
        .where(SQL.clauses("a", Clauses::equalsTo, null))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("UPDATE toto SET a = ?", query.getQuery()),
              () -> assertEquals(Stream.of(1).collect(Collectors.toList()), query.getValues()));
  }

  /**
   * UPDATE table one field with two optional clause one invalid unit test
   */
  @Test
  @DisplayName("Update one field two optional clause one invalid")
  public void testUpdateTableWhereTwoOptClause() {
    final SQLQuery query = SQL.update("toto").field("a", 1)
        .where(SQL.clauses("a", Clauses::equalsTo, null)
               .and("b", Clauses::equalsTo, "plop"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("UPDATE toto SET a = ? WHERE b = ?", query.getQuery()),
              () -> assertEquals(Stream.of(1, "plop").collect(Collectors.toList()), query.getValues()));
  }

  /**
   * Test example query
   */
  @Test
  @DisplayName("Wiki example")
  public void testExampleQuery() {
    final SQLQuery query = SQL.update("Heroes")
        .field("last_name", "Croft")
        .where(SQL.clauses("first_name", Clauses::equalsTo, "Lara")
               .and("last_name", Clauses::equalsTo, "Craft"))
        .build();
    System.out.println("SQL query: " + query.getQuery());
    System.out.println("Values: " + query.getValues());
    assertAll(() -> assertEquals("UPDATE Heroes SET last_name = ? WHERE first_name = ? AND last_name = ?", query.getQuery()),
              () -> assertEquals(Stream.of("Croft", "Lara","Craft").collect(Collectors.toList()), query.getValues()));
  }
  // Tests -

}
