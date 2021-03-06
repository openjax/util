/* Copyright (c) 2019 LibJ
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.libj.util.function;

import java.util.Objects;

/**
 * Represents an operation that accepts a single {@code <x>}-valued argument
 * and returns no result. This is the primitive type specialization of
 * {@link java.util.function.Consumer} for {@code <x>}. Unlike most other
 * functional interfaces, {@code <X>Consumer} is expected to operate via
 * side-effects.
 *
 * @see java.util.function.Consumer
 */
@FunctionalInterface
public interface <X>Consumer {
  /**
   * Performs this operation on the given argument.
   *
   * @param value The input argument.
   */
  void accept(<x> value);

  /**
   * Returns a composed {@code <X>Consumer} that performs, in sequence, this
   * operation followed by the {@code after} operation. If performing either
   * operation throws an exception, it is relayed to the caller of the composed
   * operation. If performing this operation throws an exception, the
   * {@code after} operation will not be performed.
   *
   * @param after The operation to perform after this operation.
   * @return A composed {@code <X>Consumer} that performs in sequence this
   *         operation followed by the {@code after} operation
   * @throws NullPointerException If {@code after} is null.
   */
  default <X>Consumer andThen(final <X>Consumer after) {
    Objects.requireNonNull(after);
    return (<x> t) -> {
      accept(t);
      after.accept(t);
    };
  }
}