/* Copyright (c) 2017 OpenJAX
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

package org.openjax.standard.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * A directed graph of an arbitrary-sized set of arbitrary-typed vertices,
 * permitting self-loops and parallel edges.
 * <p>
 * This digraph differs from its {@link Digraph} superclass by offering a layer
 * of indirection between the object type {@code T}, and another type {@code R}
 * that is used as the linking value between edges. The required
 * {@code Function<T,R>} parameter in the constructor is used to dereference the
 * object of type {@code T} to objects by which edges are defined of type
 * {@code R}. The references are resolved prior to the {@code dfs()} method
 * call.
 * <p>
 * Upon invocation of any method that invokes {@code dfs()}, the
 * {@code RefDigraph} swaps edges of type {@code R} to their linked object
 * references of type {@code T}, based on the translation of the supplied
 * {@code Function<T,R>} function.
 * <p>
 * It is important to note that this implementation assumes that an object of
 * type {@code T} will be encountered for each reference of type {@code R}.
 * <p>
 * Vertices can be added with {@link Digraph#addVertex(Object)}.
 * <p>
 * Edges can be added with {@link Digraph#addEdge(Object,Object)}.
 *
 * @param <T> The type of elements in this digraph.
 * @param <R> The type of references in this digraph.
 * @see Digraph
 */
public class RefDigraph<T,R> extends Digraph<T> {
  private static final long serialVersionUID = -8038282541169001107L;

  private ArrayList<T> vertices = new ArrayList<>();
  private HashSet<R> references = new HashSet<>();
  private Digraph<Object> digraph = new Digraph<>();
  protected final Function<T,R> reference;

  /**
   * Creates an empty digraph with the specified initial capacity.
   *
   * @param reference The function to obtain the reference of type {@code R}
   *          from an object of type {@code T}.
   * @param initialCapacity The initial capacity of the digraph.
   * @throws IllegalArgumentException If the specified initial capacity is
   *           negative.
   * @throws NullPointerException If {@code reference} is null.
   */
  public RefDigraph(final int initialCapacity, final Function<T,R> reference) {
    super(initialCapacity);
    this.reference = Objects.requireNonNull(reference);
  }

  /**
   * Creates an empty digraph with an initial capacity of ten.
   *
   * @param reference The function to obtain the reference of type {@code R}
   *          from an object of type {@code T}.
   * @throws NullPointerException If {@code reference} is null.
   */
  public RefDigraph(final Function<T,R> reference) {
    super();
    this.reference = Objects.requireNonNull(reference);
  }

  /**
   * Swap vertex reference objects of type {@code R} with their equivalent
   * object of type {@code T}.
   *
   * @throws IllegalStateException If some vertex references have not been
   *           specified before the call of this method.
   */
  private void swapRefs() {
    for (final T vertex : vertices) {
      final R ref = reference.apply(vertex);
      references.remove(ref);
      final Integer index = digraph.objectToIndex.remove(ref);
      if (index != null)
        digraph.objectToIndex.put(vertex, index);
    }

    vertices.clear();
    if (references.size() != 0)
      throw new IllegalStateException("Missing vertex references: " + FastCollections.toString(references, ", "));
  }

  @Override
  public boolean addVertex(final T vertex) {
    vertices.add(Objects.requireNonNull(vertex));
    return digraph.addVertex(reference.apply(vertex));
  }

  /**
   * Add a vertex reference to the graph.
   *
   * @param vertex The vertex reference.
   * @return {@code true} if this digraph has been modified, and {@code false}
   *         if the specified vertex already existed in the digraph.
   * @throws NullPointerException If {@code vertex} is null.
   */
  public boolean addVertexRef(final R vertex) {
    references.add(Objects.requireNonNull(vertex));
    return digraph.addVertex(vertex);
  }

  @Override
  public boolean addEdge(final T from, final T to) {
    if (to == null)
      return addVertex(from);

    vertices.add(from);
    vertices.add(to);
    return digraph.addEdge(reference.apply(from), reference.apply(to));
  }

  /**
   * Add directed edge ({@code from} -&gt; {@code to}) to this digraph. Calling
   * this with {@code to = null} is the equivalent of calling
   * {@code addVertex(from)} (not synchronized).
   *
   * @param from The tail vertex.
   * @param to The head vertex reference.
   * @return {@code true} if this digraph has been modified, and {@code false}
   *         if the specified edge already existed in the digraph.
   * @throws NullPointerException If {@code from} is null.
   */
  public boolean addEdgeRef(final T from, final R to) {
    if (to == null)
      return addVertex(from);

    vertices.add(from);
    references.add(to);
    return digraph.addEdge(reference.apply(from), to);
  }

  @Override
  public int getSize() {
    return digraph.getSize();
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalStateException If some vertex references have not been
   *           specified before the call of this method.
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<T> getEdges() {
    swapRefs();
    return (List<T>)digraph.getEdges();
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalStateException If some vertex references have not been
   *           specified before the call of this method.
   * @throws NullPointerException If {@code v} is null.
   */
  @Override
  public int getInDegree(final T v) {
    swapRefs();
    return digraph.getInDegree(v);
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalStateException If some vertex references have not been
   *           specified before the call of this method.
   * @throws NullPointerException If {@code v} is null.
   */
  @Override
  public int getOutDegree(final T v) {
    swapRefs();
    return digraph.getOutDegree(v);
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalStateException If some vertex references have not been
   *           specified before the call of this method.
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<T> getCycle() {
    swapRefs();
    return (List<T>)digraph.getCycle();
  }

  /**
   * Returns a directed cycle (as a list of reference {@code R} objects) if the
   * digraph has one, and null otherwise (not synchronized).
   *
   * @return A directed cycle (as a list of reference {@code R} objects) if the
   *         digraph has one, and null otherwise (not synchronized).
   * @throws IllegalStateException If some vertex references have not been
   *           specified before the call of this method.
   */
  public List<R> getCycleRef() {
    final List<T> cycle = getCycle();
    if (cycle == null)
      return null;

    final List<R> refCycle = new ArrayList<>(cycle.size());
    for (final T vertex : cycle)
      refCycle.add(reference.apply(vertex));

    return refCycle;
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalStateException If some vertex references have not been
   *           specified before the call of this method.
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<T> getTopologicalOrder() {
    swapRefs();
    return (List<T>)digraph.getTopologicalOrder();
  }

  @Override
  public String toString() {
    return digraph.toString();
  }

  @Override
  @SuppressWarnings("unchecked")
  public RefDigraph<T,R> clone() {
    final RefDigraph<T,R> clone = (RefDigraph<T,R>)super.clone();
    clone.vertices = (ArrayList<T>)vertices.clone();
    clone.references = (HashSet<R>)references.clone();
    clone.digraph = digraph.clone();
    return clone;
  }
}