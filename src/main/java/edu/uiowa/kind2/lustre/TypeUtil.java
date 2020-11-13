package edu.uiowa.kind2.lustre;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * A utility class for constructing Lustre types.
 */
public class TypeUtil {
  public static final NamedType BOOL = new NamedType("bool");
  public static final NamedType INT = new NamedType("int");
  public static final NamedType REAL = new NamedType("real");

  public static final NamedType INT8 = new NamedType("int8");
  public static final NamedType INT16 = new NamedType("int16");
  public static final NamedType INT32 = new NamedType("int32");
  public static final NamedType INT64 = new NamedType("int64");

  public static final NamedType UINT8 = new NamedType("uint8");
  public static final NamedType UINT16 = new NamedType("uint16");
  public static final NamedType UINT32 = new NamedType("uint32");
  public static final NamedType UINT64 = new NamedType("uint64");

  /**
   * Construct an array type
   * <p>
   * Lustre: {@code <base>^<size>}
   *
   * @param base type of the elements of the array
   * @param size size of the array
   * @return the array type
   */
  public static Type array(Type base, int size) {
    return new ArrayType(base, size);
  }

  /**
   * Construct an enum type
   * <p>
   * Lustre: <code>enum {{@code <values[0]>, <values[1]>, ...}}</code>
   *
   * @param values a list values for the enumerated type
   * @return the enum type
   */
  public static Type enumeration(List<String> values) {
    return new EnumType(values);
  }

  /**
   * Construct a named type
   * <p>
   * Lustre: {@code <name>}
   *
   * @param name name of the type
   * @return the named type
   */
  public static Type named(String name) {
    return new NamedType(name);
  }

  /**
   * Construct a record type
   * <p>
   * Lustre: <code>struct {{@code <fields[0].key> : <fields[0].value>, ...}}</code>
   *
   * @param fields a mapping from field names to their types
   * @return the record type
   */
  public static Type record(Map<String, Type> fields) {
    return new RecordType(fields);
  }

  /**
   * Construct an integer-subrange type
   * <p>
   * Lustre: {@code subrange [<low>, <high>] of int}
   *
   * @param low  minimum integer value (inclusive)
   * @param high maximum integer value (inclusive)
   * @return the integer-subrange type
   */
  public static Type intSubrange(String low, String high) {
    return new SubrangeIntType(new BigInteger(low), new BigInteger(high));
  }

  /**
   * Construct an integer-subrange type
   * <p>
   * Lustre: {@code subrange [<low>, <high>] of int}
   *
   * @param low  minimum integer value (inclusive)
   * @param high maximum integer value (inclusive)
   * @return the integer-subrange type
   */
  public static Type intSubrange(long low, long high) {
    return new SubrangeIntType(BigInteger.valueOf(low), BigInteger.valueOf(high));
  }

  /**
   * Construct an tuple type
   * <p>
   * Lustre: {@code [<types[0]>, <types[1]>, ...]}
   *
   * @param types a list of subtypes
   * @return the tuple type
   */
  public static Type tuple(List<? extends Type> types) {
    return new TupleType(types);
  }
}
