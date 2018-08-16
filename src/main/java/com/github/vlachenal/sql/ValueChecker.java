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
 * Value checker functional interface
 *
 * @param <T> the value type
 *
 * @author Vincent Lachenal
 */
@FunctionalInterface
public interface ValueChecker<T> {

  /**
   * Check if value is valid
   *
   * @param value the value to check
   *
   * @return {@code true} if value is valid, {@code false} otherwise
   */
  boolean isValid(T value);

}
