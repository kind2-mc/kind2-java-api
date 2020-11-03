package edu.uiowa.kind2.lustre;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

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

  public static Type mkArrayType(Type base, int size) {
    return new ArrayType(base, size);
  }

  public static Type mkEnumType(List<String> values) {
    return new EnumType(values);
  }

  public static Type mkNamedType(String name) {
    return new NamedType(name);
  }

  public static Type mkRecordType(Map<String, Type> fields) {
    return new RecordType(fields);
  }

  public static Type mkIntSubrangeType(BigInteger low, BigInteger high) {
    return new SubrangeIntType(low, high);
  }

  public static Type mkIntSubrangeType(long low, long high) {
    return new SubrangeIntType(BigInteger.valueOf(low), BigInteger.valueOf(high));
  }

  public static Type mkTupleType(List<? extends Type> types) {
    return new TupleType(types);
  }
}
