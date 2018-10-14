/* Copyright (c) 2017 FastJAX
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

package org.fastjax.util;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A {@code FilterSet} contains some other {@code Set}, which it uses as its
 * basic source of data, possibly transforming the data along the way or
 * providing additional functionality. The class {@code FilterSet} itself simply
 * overrides all methods of {@code AbstractSet} with versions that pass all
 * requests to the source {@code Set}. Subclasses of {@code FilterSet} may
 * further override some of these methods and may also provide additional
 * methods and fields.
 */
public class FilterSet<E> extends AbstractSet<E> {
  /**
   * The Set to be filtered.
   */
  @SuppressWarnings("rawtypes")
  protected volatile Set source;

  /**
   * Creates a new {@code FilterSet}.
   *
   * @param source The source {@code Set} object.
   * @throws NullPointerException If {@code source} is null.
   */
  public FilterSet(final Set<E> source) {
    this.source = Objects.requireNonNull(source);
  }

  /**
   * Creates a new {@code FilterSet} with a null source.
   */
  protected FilterSet() {
  }

  @Override
  public int size() {
    return source.size();
  }

  @Override
  public boolean isEmpty() {
    return source.isEmpty();
  }

  @Override
  public boolean contains(final Object o) {
    return source.contains(o);
  }

  @Override
  public Iterator<E> iterator() {
    return source.iterator();
  }

  @Override
  public Object[] toArray() {
    return source.toArray();
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T>T[] toArray(final T[] a) {
    return (T[])source.toArray(a);
  }

  @Override
  public boolean add(final E e) {
    return source.add(e);
  }

  @Override
  public boolean remove(final Object o) {
    return source.remove(o);
  }

  @Override
  public boolean containsAll(final Collection<?> c) {
    return source.containsAll(c);
  }

  @Override
  public boolean addAll(final Collection<? extends E> c) {
    return source.addAll(c);
  }

  @Override
  public boolean removeAll(final Collection<?> c) {
    return source.removeAll(c);
  }

  @Override
  public boolean retainAll(final Collection<?> c) {
    return source.retainAll(c);
  }

  @Override
  public void clear() {
    source.clear();
  }

  @Override
  public void forEach(final Consumer<? super E> action) {
    source.forEach(action);
  }

  @Override
  public boolean removeIf(final Predicate<? super E> filter) {
    return source.removeIf(filter);
  }

  @Override
  public Spliterator<E> spliterator() {
    return source.spliterator();
  }

  @Override
  public Stream<E> stream() {
    return source.stream();
  }

  @Override
  public Stream<E> parallelStream() {
    return source.parallelStream();
  }
}